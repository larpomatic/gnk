package org.gnk.resplacetime

import org.gnk.ressplacetime.GenericObject
import org.gnk.ressplacetime.ReferentialObject
import org.gnk.tag.Tag
import org.gnk.selectintrigue.Plot

class GenericPlace extends GenericObject{


    Integer id
    Integer version

    Date lastUpdated
    Date dateCreated
    String code
    String comment
    GnConstant gnConstant
    Integer DTDId

    // Id referenced into DTD
    static transients = ["DTDId", "proposedPlaces", "bannedPlaces", "selectedPlace", "lockedPlace"]


    List<Place> proposedPlaces
    List<Place> bannedPlaces
    Place selectedPlace
    Place lockedPlace

    static belongsTo = [plot: Plot, objectType: ObjectType]

    static hasMany = [ events: Event,
	                   extTags: GenericPlaceHasTag,
	                   pastscenes: Pastscene ]

	static constraints = {
		code (blank: false, maxSize: 45, unique: false)
		comment (nullable: true)
        gnConstant (nullable: true)

    }

    static mapping = {
        comment type: 'text'
        id type:'integer'
        version type: 'integer'
        extTags cascade:'all-delete-orphan'
    }

    public boolean hasGenericPlaceTag(Tag parGenericPlaceTag) {
        return (GenericPlaceHasTag.findByTagAndGenericPlace(parGenericPlaceTag, this) != null)
    }

    public getGenericPlaceHasTag(Tag tag) {
        return GenericPlaceHasTag.findByTagAndGenericPlace(tag, this);
    }

     ArrayList<Tag> getTags() {
         ArrayList<Tag> tagsList = new ArrayList<>();

         for (GenericPlaceHasTag genericPlaceHasTag in this.extTags)
            tags.add(genericPlaceHasTag.tag)

         return tagsList;
     }

    Map<Tag, Integer> getTagsAndWeights(Float ponderation) {
        Map<Tag, Integer> mapTagInt = new HashMap<>();

         for (GenericPlaceHasTag genericPlaceHasTag in this.extTags)
             mapTagInt.put(genericPlaceHasTag.tag, genericPlaceHasTag.weight * ponderation)

         return mapTagInt;
     }

     ArrayList<ReferentialObject> getReferentialObject() {
         return Place.findAll();
     }

    ReferentialObject getLockedObject() {
        return this.lockedPlace;
    }

    Plot getPlotFromGenericObject () {
        return getPlot();
    }
    String getSubType() {
        return "genericPlace";
    }

//    boolean isIngameClue()
//    {
//        return ((this.code != null) || (this.comment != null)) && ((!this.code.isEmpty()) || (!this.comment.isEmpty()))
//    }
}
