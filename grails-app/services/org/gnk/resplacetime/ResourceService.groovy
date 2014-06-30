package org.gnk.resplacetime

// Import des classes Groovy
import org.gnk.ressplacetime.GenericResource
import org.gnk.ressplacetime.ReferentialResource
import com.gnk.substitution.Tag

import java.text.Collator

class ResourceService {

    Integer hintNumber = 5
    Integer universeMatchingRate = 100

    /* Exposed Methods */

    def GenericResource findReferentialResource (GenericResource genericResource, String univers) {
        LinkedList<Tag> tagsList
        Tag typeTag = null
        Integer maximumRate = 0
        LinkedList<ReferentialResource> referentialResourcesList

        // Get the Type Tag
        tagsList = genericResource.getTagList()
        if (tagsList) {
            for (Tag tag : tagsList) {
                maximumRate += tag.weight
                typeTag = tag
            }
        }

        if (typeTag) {
            referentialResourcesList = getReferentialResourcesOfType(typeTag.value)
            referentialResourcesList = removeAllBannedReferentialItems(referentialResourcesList, genericResource)
            referentialResourcesList = computeTagMatchingRate(referentialResourcesList, tagsList, univers)

            // Sort the referentialResourcesList by matchingRate
            referentialResourcesList.sort(new Comparator<ReferentialResource>() {
                @Override
                int compare(ReferentialResource o1, ReferentialResource o2) {
                    return (o1.matchingRate > o2.matchingRate ? -1 : 1)
                }
            })

            if (referentialResourcesList.size() > hintNumber)
                genericResource.resultList = referentialResourcesList.subList(0, hintNumber)
            else
                genericResource.resultList = referentialResourcesList
        }

        return genericResource
    }

    /* !Exposed Methods */

    private LinkedList<ReferentialResource> getReferentialResourcesOfType(String type) {
        LinkedList<org.gnk.tag.Tag> tagTypeList
        LinkedList<ResourceHasTag> resourceHasTagList
        LinkedList<ReferentialResource> referentialResourcesList = new LinkedList<ReferentialResource>()

        // Get the tag (database version) which gives the type of the Resource
        tagTypeList = org.gnk.tag.Tag.createCriteria().list {
            eq ('name', type)
        }
        if (tagTypeList) {
            for (org.gnk.tag.Tag tagType : tagTypeList)
            {
//                resourceHasTagList = tagType.extResourceTags
                for (ResourceHasTag resourceHasTag : resourceHasTagList) {
                    ReferentialResource referentialResource = new ReferentialResource()
                    referentialResource.name = resourceHasTag.resource.name
                    referentialResource.matchingRate = 0
                    referentialResource.tagList = new LinkedList<Tag>()
                    referentialResource.universeList = new LinkedList<String>()

                    // Tags addition
                    for (ResourceHasTag anotherTagOfResource : resourceHasTag.resource.extTags)
                    {
                        Tag tag = new Tag()
                        tag.type = anotherTagOfResource.tag.parent.name
                        tag.value = anotherTagOfResource.tag.name
                        tag.weight = anotherTagOfResource.weight

                        referentialResource.tagList.add(tag)
                    }

                    // Universe information addition
                    if (resourceHasTag.resource.resourceHasUniverses)
                    {
                        LinkedList<ResourceHasUnivers> resourceHasUniversesList = resourceHasTag.resource.resourceHasUniverses
                        for (ResourceHasUnivers resourceHasUnivers : resourceHasUniversesList) {
                            String universe = resourceHasUnivers.univers.name
                            referentialResource.universeList.add(universe)
                        }
                    }

                    referentialResourcesList.add(referentialResource)
                }
            }
        }

        return referentialResourcesList
    }

    private LinkedList<ReferentialResource> removeAllBannedReferentialItems (LinkedList<ReferentialResource> itemsList, GenericResource genericItem) {
        // Add old resourceResults in bannedResourcesList
        if (!genericItem.bannedItemsList) {
            genericItem.bannedItemsList = new LinkedList<ReferentialResource>()
        }
        if (genericItem.resultList)
            genericItem.bannedItemsList.addAll(genericItem.resultList)

        // Remove the banned referentialResources
        for (ReferentialResource bannedItem : genericItem.bannedItemsList) {
            for (ReferentialResource item : itemsList) {
                if (item.name.toUpperCase().equals(bannedItem.name.toUpperCase())) {
                    itemsList.remove(item)
                    break
                }
            }
        }

        return itemsList
    }

    private LinkedList<ReferentialResource> computeTagMatchingRate (LinkedList<ReferentialResource> itemsList, List<Tag> tagsList, String expectedUniverse){
        for (ReferentialResource item : itemsList) {
            // Computes the tag-matching rate
            for (Tag itemTag : item.tagList) {
                for (Tag expectedTag : tagsList) {
                    if (itemTag.type.toUpperCase().equals(expectedTag.type.toUpperCase()) & itemTag.value.toUpperCase().equals(expectedTag.value.toUpperCase())) {
                        // The expectedTag is mandatory for the item
                        if (expectedTag.weight.equals(101)) {
                            item.matchingRate += (expectedTag.weight * 100)
                        }
                        // The expectedTag is forbidden for the item
                        else if (expectedTag.weight.equals(-101)) {
                            itemsList.remove(item);
                        }
                        else {
                            item.matchingRate += itemTag.weight
                        }
                    }
                }
            }
            // Computes the universe-matching rate
            for (String itemUniverse : item.universeList) {
                if (itemUniverse.toUpperCase().equals(expectedUniverse.toUpperCase())) {
                    item.matchingRate += universeMatchingRate
                }
            }

            // If match rate <= 0, remove the referentialResource
            if (item.matchingRate <= 0) {
                itemsList.remove(item)
            }
        }

        return itemsList
    }
}
