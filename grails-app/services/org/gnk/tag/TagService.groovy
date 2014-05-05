package org.gnk.tag

import java.util.List
import org.gnk.tag.Tag

class TagService {

    public static final int LOCKED = 101;
    public static final int BANNED = -101;

	def serviceMethod() {
	}

	def List<Tag> getPlotTagQuery() {
		def result = Tag.withCriteria{
			tagFamily { eq ("relevantPlot", true) }
		}

		return result;
	}

	def List<Tag> getRoleTagQuery() {
		def result = Tag.withCriteria{
			tagFamily { eq ("relevantPlot", true) }
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
     * @param  refTagList  The weighted tag list of the reference object (like a character for R2P)
     * @param  challengerTagList The weighted tag list of the object whose we want to test the compatibility with the reference object (like a role in R2P)
     * @param  refLockedBannedTags The list of tags that are locked or banned form the reference object (True for locked, False for banned)
     * @return the matching score between the challenger object and the reference object ; Integer.MIN_VALUE if the challenger object break a ban if put with reference object
     * @see    org.gnk.roletoperso.RoleToPersoProcessing().getRoleRank()
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
                        return (challengerTag.getValue() + refTagList.get(refTag)) * 100;
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
                            rankTag += (challengerTagWeight + refTagWeight) * factor;
                        }
                    }
                }
            }
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
}
