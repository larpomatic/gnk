package org.gnk.resplacetime

import com.gnk.substitution.Tag
import org.gnk.ressplacetime.GenericPlace
import org.gnk.ressplacetime.ReferentialPlace

class PlaceService {

    Integer hintNumber = 5
    Integer universeMatchingRate = 100

    /* !Exposed Methods*/

    def GenericPlace findReferentialPlace (GenericPlace genericPlace, String universe) {
        LinkedList<Tag> tagsList
        Tag areaTag = null
        Integer maximumRate = 0
        LinkedList<ReferentialPlace> referentialPlacesList

        // Get the Area Tag
        tagsList = genericPlace.getTagList()
        if (tagsList) {
            for (Tag tag : tagsList) {
                maximumRate += tag.weight
                areaTag = tag
            }
        }

        if (areaTag) {
            referentialPlacesList = getReferentialPlacesOfArea(areaTag.value)
            referentialPlacesList = removeAllBannedReferentialItems(referentialPlacesList, genericPlace)
            referentialPlacesList = computeTagMatchingRate(referentialPlacesList, tagsList, universe)

            // Sort the referentialResourcesList by matchingRate
            referentialPlacesList.sort(new Comparator<ReferentialPlace>() {
                @Override
                int compare(ReferentialPlace o1, ReferentialPlace o2) {
                    return (o1.matchingRate > o2.matchingRate ? -1 : 1)
                }
            })

            if (referentialPlacesList.size() > hintNumber)
                genericPlace.resultList = referentialPlacesList.subList(0, hintNumber)
            else
                genericPlace.resultList = referentialPlacesList
        }

        return genericPlace
    }

    /* !Exposed Methods*/

    private LinkedList<ReferentialPlace> getReferentialPlacesOfArea(String type) {
        LinkedList<org.gnk.tag.Tag> tagSuperficieList
        LinkedList<PlaceHasTag> placeHasTagList
        LinkedList<ReferentialPlace> referentialPlacesList = new LinkedList<ReferentialPlace>()

        // Get the tag (database version) which gives the type of the Place
        tagSuperficieList = org.gnk.tag.Tag.createCriteria().list {
            eq ('name', type)
        }
        if (tagSuperficieList)
        {
            for (org.gnk.tag.Tag tagSuperficie : tagSuperficieList)
            {
                placeHasTagList = tagSuperficie.extPlaceTags
                for (PlaceHasTag placeHasTag : placeHasTagList) {
                    ReferentialPlace referentialPlace = new ReferentialPlace()
                    referentialPlace.name = placeHasTag.place.name
                    referentialPlace.matchingRate = 0
                    referentialPlace.tagList = new LinkedList<Tag>()
                    referentialPlace.universeList = new LinkedList<String>()

                    // Classic tags addition
                    for (PlaceHasTag anotherTagOfResource : placeHasTag.place.extTags)
                    {
                        Tag tag = new Tag()
                        tag.type = anotherTagOfResource.tag.parent.name
                        tag.value = anotherTagOfResource.tag.name
                        tag.weight = anotherTagOfResource.weight

                        referentialPlace.tagList.add(tag)
                    }

                    // Universe information addition
                    if (placeHasTag.place.placeHasUniverses)
                    {
                        LinkedList<PlaceHasUnivers> placeHasUniversesList = placeHasTag.place.placeHasUniverses
                        for (PlaceHasUnivers placeHasUnivers : placeHasUniversesList) {
                            String universe = placeHasUnivers.univers.name
                            referentialPlace.universeList.add(universe)
                        }
                    }

                    referentialPlacesList.add(referentialPlace)
                }
            }
        }

        return referentialPlacesList
    }

    private LinkedList<ReferentialPlace> removeAllBannedReferentialItems (LinkedList<ReferentialPlace> itemsList, GenericPlace genericItem) {
        // Add old placesResults in bannedPlacesList
        if (!genericItem.bannedItemsList) {
            genericItem.bannedItemsList = new LinkedList<ReferentialPlace>()
        }
        if (genericItem.resultList)
            genericItem.bannedItemsList.addAll(genericItem.resultList)

        // Remove the banned referentialPlaces
        for (ReferentialPlace bannedItem : genericItem.bannedItemsList) {
            for (ReferentialPlace item : itemsList) {
                if (item.name.toUpperCase().equals(bannedItem.name.toUpperCase())) {
                    itemsList.remove(item)
                    break
                }
            }
        }

        return itemsList
    }

    private LinkedList<ReferentialPlace> computeTagMatchingRate (LinkedList<ReferentialPlace> itemsList, List<Tag> tagsList, String expectedUniverse){
        for (ReferentialPlace item : itemsList) {
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

            // If match rate <= 0, remove the referentialPlace
            if (item.matchingRate <= 0) {
                itemsList.remove(item)
            }
        }

        return itemsList
    }
}
