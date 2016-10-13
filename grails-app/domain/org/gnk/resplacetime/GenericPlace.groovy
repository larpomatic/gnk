package org.gnk.resplacetime

import javafx.util.Pair
import org.gnk.selectintrigue.Plot
import org.gnk.tag.Tag
import org.gnk.resplacetime.GenericPlaceHasTag

class GenericPlace extends GenericObject{


    // Id referenced into DTD
    static transients = ["DTDId", "proposedPlaces", "bannedPlaces", "selectedPlace"]


    List<Place> proposedPlaces
    List<Place> bannedPlaces
    Place selectedPlace


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
         ArrayList<Tag> tags = GenericPlaceHasTag.
         return null;
     }
     Map<Tag, Integer> getTagsAndWeights() {
         return null;
     }
     ArrayList<ReferentialObject> getReferentialObject() {
         return Place.all;
     }

//    boolean isIngameClue()
//    {
//        return ((this.code != null) || (this.comment != null)) && ((!this.code.isEmpty()) || (!this.comment.isEmpty()))
//    }
}
