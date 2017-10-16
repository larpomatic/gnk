package org.gnk.resplacetime

import org.gnk.ressplacetime.GenericObject
import org.gnk.ressplacetime.ReferentialObject
import org.gnk.ressplacetime.ReferentialPlace
import org.gnk.tag.Tag
import org.gnk.selectintrigue.Plot
import org.gnk.utils.Pair

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
    static transients = ["DTDId", "proposedPlaces", "bannedPlaces", "selectedPlace", "lockedPlace", "resultsAllUniverses", "resultService", "plotId", "gnId", "totalNumberOfTags"]


    ArrayList<Place> proposedPlaces
    ArrayList<Place> bannedPlaces
    Place selectedPlace
    Place lockedPlace
    List<Tag> taglist
    ArrayList<Pair<Tag, ArrayList<Pair<ReferentialObject, Integer>>>> resultsAllUniverses
    ArrayList<Pair<ReferentialObject, Integer>> resultService
    Integer plotId
    Integer gnId
    Integer totalNumberOfTags

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

         for (Tag t in this.taglist)
            tags.add(t)

         return tagsList;
     }

    Map<Tag, Integer> getTagsAndWeights(Float ponderation) {
        Map<Tag, Integer> mapTagInt = new HashMap<>();
        if (taglist != null) {
            for (Tag t in this.taglist)
                mapTagInt.put(t, new Integer((int) (t.weight_substitution * ponderation)))
        } else {
            for (GenericPlaceHasTag tag in this.extTags) {
                mapTagInt.put(tag.getTag(), new Integer((int) (tag.getWeight() * ponderation)))
            }
        }
         return mapTagInt;
     }

     ArrayList<ReferentialObject> getProposedObject() {
         return this.proposedPlaces;
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

    String getName() {
        return this.name;
    }

    Plot getPlotbyId() {
        return Plot.findById(this.plotId);
    }

    ArrayList<Tag> getTaglist() {
        return this.taglist;
    }

//    boolean isIngameClue()
//    {
//        return ((this.code != null) || (this.comment != null)) && ((!this.code.isEmpty()) || (!this.comment.isEmpty()))
//    }
}
