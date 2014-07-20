package org.gnk.tag

class TagService {

    public static final int LOCKED = 101;
    public static final int BANNED = -101;

    def serviceMethod() {
    }

    def findChildren(Tag t) {
        def tags = Tag.findAllWhere(parent: t)
        def tagsTmp = new ArrayList()
        tagsTmp.addAll(tags)
        if (tagsTmp == null)
            return tags
        for (Tag tag : tagsTmp) {
            tags.addAll(findChildren(tag))
        }
        return tags
    }

    def List<Tag> getPlotTagQuery() {
        Set<Tag> genericPlot = Tag.findAllByName("Générique Intrigue");
        ArrayList<Tag> result = new ArrayList<>();
        result = fillResult(result, genericPlot);
        return result;
    }

    def List<Tag> getResourceTagQuery() {
        Set<Tag> genericResource = Tag.findAllByName("Générique Ressources");
        ArrayList<Tag> result = new ArrayList<>();
        result = fillResult(result, genericResource);
        return result;
    }

    def List<Tag> getPlaceTagQuery() {
        Set<Tag> genericPlace = Tag.findAllByName("Générique Lieux");
        ArrayList<Tag> result = new ArrayList<>();
        result = fillResult(result, genericPlace);
        return result;
    }

    def List<Tag> getRoleTagQuery() {
        Set<Tag> genericRole = Tag.findAllByName("Générique Rôle");
        ArrayList<Tag> result = new ArrayList<>();
        result = fillResult(result, genericRole);
        return result;
    }

    def ArrayList<Tag> fillResult(ArrayList<Tag> result, Set<Tag> tagsToInclude) {
        for (Tag child in tagsToInclude) {
            result.add(child);
        }
        return result;
    }

    def tagIsLocked(Map.Entry<Tag, Integer> valuedTag) {
        return valuedTag.getValue() == TagService.LOCKED;
    }

    def tagIsBanned(Map.Entry<Tag, Integer> valuedTag) {
        return valuedTag.getValue() == TagService.BANNED;
    }

    /**
     * Returns the matching score of two lists of weighted tags.
     * This function consider the matching between tags of each list and the matching throw the relations between tags
     *
     * @param refTagList The weighted tag list of the reference object (like a character for R2P)
     * @param challengerTagList The weighted tag list of the object whose we want to test the compatibility with the reference object (like a role in R2P)
     * @param refLockedBannedTags The list of tags that are locked or banned form the reference object (True for locked, False for banned)
     * @return the matching score between the challenger object and the reference object ; Integer.MIN_VALUE if the challenger object break a ban if put with reference object
     * @see org.gnk.roletoperso.RoleToPersoProcessing().getRoleRank()
     */

    public int getTagsMatching(Map<Tag, Integer> refTagList, Map<Tag, Integer> challengerTagList, Map<Tag, Boolean> refLockedBannedTags) {
        Integer rankTag = 0;
        if (challengerTagList != null) {
            for (Map.Entry<Tag, Integer> challengerTag : challengerTagList.entrySet()) {
                Boolean tagIsLockedOrBannedForRef = refLockedBannedTags.get(challengerTag.getKey());
                if (tagIsLockedOrBannedForRef != null) {
                    if ((tagIsLocked(challengerTag) && !tagIsLockedOrBannedForRef) || (tagIsBanned(challengerTag) && tagIsLockedOrBannedForRef)) {
                        return Integer.MIN_VALUE;
                    }
                }
                for (Tag refTag : refTagList.keySet()) {
                    if (refTag == challengerTag.getKey()) {
                        return (challengerTag.getValue() * refTagList.get(refTag)) * 100;
                    }
                    TagRelation tagRelation1 = TagRelation.myFindWhere(challengerTag.getKey(), refTag);
                    TagRelation tagRelation2 = TagRelation.myFindWhere(refTag, challengerTag.getKey());
                    int lockSignOfChallengerTag = tagIsLocked(challengerTag) ? 1 : tagIsBanned(challengerTag) ? -1 : 0;
                    Boolean refTagIsLockedOrBanned = refLockedBannedTags.get(refTag);
                    int lockSignOfRefTag = refTagIsLockedOrBanned == null ? 0 : refTagIsLockedOrBanned ? 1 : -1;

                    if (tagRelation1 != null || tagRelation2 != null) {
                        int weight = 0;
                        int divider = 0;
                        int lockSignOfTagRelation1 = tagRelation1 == null ? 0 : tagRelation1.isLocked() ? 1 : tagRelation1.isBanned() ? -1 : 0;
                        int lockSignOfTagRelation2 = tagRelation2 == null ? 0 : tagRelation2.isLocked() ? 1 : tagRelation2.isBanned() ? -1 : 0;
                        if ((lockSignOfRefTag * lockSignOfChallengerTag * lockSignOfTagRelation1) < 0 || (lockSignOfRefTag * lockSignOfChallengerTag * lockSignOfTagRelation2) < 0) {
                            return Integer.MIN_VALUE;
                        }
                        if (tagRelation1 != null) {
                            weight += tagRelation1.getterWeight();
                            divider++;
                        }
                        if (tagRelation2 != null) {
                            weight += tagRelation2.getWeight();
                            divider++;
                        }
                        if (divider != 0) {
                            Integer challengerTagWeight = challengerTag.getValue();
                            Integer refTagWeight = refTagList.get(refTag);
                            int factor = weight / divider;
                            rankTag += (challengerTagWeight * refTagWeight) * factor;
                        }
                    }
                }
            }
        }
        return rankTag;
    }

/**
 * Returns the difference between the ponderations of weighted tags of two lists. In other terms the distance to reach the objective
 *
 * @param refTagList The weighted tag list of the reference object (the objective to reach)
 * @param challengerTagList The weighted tag list of the object whose we want to test the compatibility with the reference object
 * @return the difference between the ponderations of weighted tags of two lists.
 * @see org.gnk.selectintrigue.SelectIntrigueProcessing
 */
    public int getTagsDifferenceToObjective(Map<Tag, Integer> refTagList, Map<Tag, Integer> challengerTagList) {
        Integer rankTag = 0;
        for (Tag refTag : refTagList.keySet()) {
            Integer challengerTagWeight = challengerTagList.get(refTag);
            if (challengerTagWeight == null)
                challengerTagWeight = 0;
            rankTag -= Math.pow(Math.abs(refTagList.get(refTag) - challengerTagWeight), 2);
        }
        return rankTag;
    }

    public int getTagsMatchingWithoutRelation(Map<Tag, Integer> refTagList, Map<Tag, Integer> challengerTagList, Map<Tag, Boolean> refLockedBannedTags) {
        Integer rankTag = 0;
        if (challengerTagList != null) {
            for (Map.Entry<Tag, Integer> challengerTag : challengerTagList.entrySet()) {
                Boolean tagIsLockedOrBannedForRef = refLockedBannedTags.get(challengerTag.getKey());
                if (tagIsLockedOrBannedForRef != null) {
                    if ((tagIsLocked(challengerTag) && !tagIsLockedOrBannedForRef) || (tagIsBanned(challengerTag) && tagIsLockedOrBannedForRef)) {
                        return Integer.MIN_VALUE;
                    }
                }
                for (Tag refTag : refTagList.keySet()) {
                    if (refTag == challengerTag.getKey()) {
                        if (tagIsLocked(challengerTag) || tagIsLocked(challengerTag)){
                            rankTag += (challengerTag.getValue() * refTagList.get(refTag)) * 100;
                        }
                        else{
                            rankTag += (challengerTag.getValue() * refTagList.get(refTag));
                        }
                        print "GOOD"
                        break;
                    }
                    //print"-----------"
                    if (refTag.name.toString().equals("french")){
                        print("reftag ==> ")
                        print(refTag)
                    }
                    /*if (challengerTag.getKey().toString().equals("french")){
                        print("challengerTag ==> ")
                        print(challengerTag.getKey())
                    }*/
                }
            }
        }
        return rankTag;
    }

    public int getTagMatching(Tag t1, int w1, Tag t2, int w2) {
        Integer score = 0;
        if (t1 == t2)
            return 100;
        else {
            TagRelation tagRelation1 = TagRelation.myFindWhere(t1, t2);
            TagRelation tagRelation2 = TagRelation.myFindWhere(t2, t1);
            TagRelation tagRelation = tagRelation1;
            if ((tagRelation1 == null) && (tagRelation2 != null) && (tagRelation2.isBijective == true))
                tagRelation = tagRelation2;

            if (tagRelation == null)
                return 0;
            else
                return tagRelation.weight;
        }
    }
}
