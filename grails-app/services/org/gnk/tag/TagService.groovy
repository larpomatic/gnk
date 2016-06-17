package org.gnk.tag

import org.gnk.naming.NamingService
import org.gnk.utils.ComparateurTag
import org.javatuples.Pair

import java.lang.reflect.Array

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
        ArrayList<Tag> genericChilds = getGenericChilds();
        ArrayList<Tag> result = new ArrayList<>();
        for (Tag child in genericChilds) {
            TagRelevant tagRelevant = TagRelevant.findByTag(child);
            if (tagRelevant && tagRelevant.relevantPlot) {
                result.add(child);
            }
        }
        return result;
    }

    def List<Tag> getUniversTagQuery() {
        Tag genericUnivers = Tag.findByName("Tag Univers");
        ArrayList<Tag> result = new ArrayList<>();
        for (Tag child in genericUnivers.children) {
            result.add(child);
        }
        Collections.sort(result, new ComparateurTag())
        return result;
    }

    def List<Tag> getResourceTagQuery() {
        ArrayList<Tag> genericChilds = getGenericChilds();
        ArrayList<Tag> result = new ArrayList<>();
        for (Tag child in genericChilds) {
            TagRelevant tagRelevant = TagRelevant.findByTag(child);
            if (tagRelevant && tagRelevant.relevantResource) {
                result.add(child);
            }
        }
        return result;
    }

    def List<Tag> getPlaceTagQuery() {
        ArrayList<Tag> genericChilds = getGenericChilds();
        ArrayList<Tag> result = new ArrayList<>();
        for (Tag child in genericChilds) {
            TagRelevant tagRelevant = TagRelevant.findByTag(child);
            if (tagRelevant && tagRelevant.relevantPlace) {
                result.add(child);
            }
        }
        return result;
    }

    def List<Tag> getRoleTagQuery() {
        ArrayList<Tag> genericChilds = getGenericChilds();
        ArrayList<Tag> result = new ArrayList<>();
        for (Tag child in genericChilds) {
            TagRelevant tagRelevant = TagRelevant.findByTag(child);
            if (tagRelevant && tagRelevant.relevantRole) {
                result.add(child);
            }
        }
        return result;
    }

    def getGenericChilds() {
        Tag generics = Tag.findById(0);
        ArrayList<Tag> result = generics.children;
        result.remove(Tag.findByName("Tag Univers"));
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

    public int getTagsMatching(Map<Tag, Integer> refTagList,
                               Map<Tag,Integer> challengerTagList,
                               Map<Tag, Boolean> refLockedBannedTags,
                               HashMap<Pair<com.gnk.substitution.Tag, com.gnk.substitution.Tag>, Integer> dictionnaryTagFirstnameName) {
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
//                    TagRelation tagRelation1 = null
//                    TagRelation tagRelation2 = null


                    TagRelation tagRelation1 = dictionnaryTagFirstnameName.get(new Pair<Tag, Tag>(challengerTag.getKey(), refTag))
                    TagRelation tagRelation2 = dictionnaryTagFirstnameName.get(new Pair<Tag, Tag>(refTag, challengerTag.getKey()))

                    if (tagRelation1 == null) {
                        tagRelation1 = TagRelation.myFindWhere(challengerTag.getKey(), refTag);

                        if (tagRelation1 != null) {
                            dictionnaryTagFirstnameName.put(new Pair<Tag, Tag>(refTag, challengerTag.getKey()), tagRelation1.weight)
                        }
                        else{
                            //NamingService.dictionnaryTagFirstnameName.put(new Pair<Tag, Tag>(refTag, challengerTag.getKey()), tagRelation1.weight)
                        }
                    }

                    if (tagRelation2 == null) {
                        tagRelation2 = TagRelation.myFindWhere(refTag, challengerTag.getKey());
                        if (tagRelation2 != null) {
                            dictionnaryTagFirstnameName.put(new Pair<Tag, Tag>(refTag, challengerTag.getKey()), tagRelation2.weight)
                        }
                        else{
                            //NamingService.dictionnaryTagFirstnameName.put(new Pair<Tag, Tag>(refTag, challengerTag.getKey()), tagRelation2.weight)
                        }
                    }

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

    public int getTagsMatchingCalculate(Map<Tag, Integer> refTagList,
                                Map<Tag,Integer> challengerTagList,
                                List<TagRelation> allRelation,
                                Map<Tag, Boolean> refLockedBannedTags) {
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
                    TagRelation tagRelation1 = null
                    TagRelation tagRelation2 = null

                    tagRelation1 = allRelation.find {it.tag1 == challengerTag.getKey() && it.tag2 == refTag}
                    tagRelation2 = allRelation.find {it.tag2 == challengerTag.getKey() && it.tag1 == refTag}

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
