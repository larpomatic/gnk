package org.gnk.resplacetime

import org.gnk.ressplacetime.GenericObject
import org.gnk.ressplacetime.ReferentialObject
import org.gnk.ressplacetime.ReferentialResource
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasEvent
import org.gnk.roletoperso.RoleHasEventHasGenericResource
import org.gnk.tag.Tag
import org.gnk.selectintrigue.Plot
import org.gnk.utils.Pair

class GenericResource extends GenericObject{

    Integer id
    Integer version

    Date lastUpdated
    Date dateCreated
    String code
    String comment
    GnConstant gnConstant
    Integer DTDId


    // Ingame Clue :
    String title
    String description
    String fromRoleText
    String toRoleText


    static belongsTo = [fromRole: Role, toRole: Role, possessedByRole: Role, plot: Plot, objectType: ObjectType]

    // Id referenced into DTD
    static transients = ["DTDId", "proposedResources", "bannedResources", "selectedResource", "lockedResource", "resultsAllUniverses", "resultService", "plotId", "gnId", "totalNumberOfTags"]


    ArrayList<Resource> proposedResources
    ArrayList<Resource> bannedResources
    Resource selectedResource
    Resource lockedResource
    List<Tag> taglist
    ArrayList<Pair<Tag, ArrayList<Pair<ReferentialObject, Integer>>>> resultsAllUniverses
    ArrayList<Pair<ReferentialObject, Integer>> resultService
    Integer plotId
    Integer gnId
    Integer totalNumberOfTags

	static hasMany = [ extTags: GenericResourceHasTag,
	                   roleHasEventHasRessources: RoleHasEventHasGenericResource]

    static constraints = {
        title maxSize: 75
        code (blank: false, maxSize: 45, unique: false)
        comment (nullable: true)
        title (nullable: true)
        description (nullable: true)
        fromRole (nullable: true)
        toRole (nullable: true)
        possessedByRole (nullable: true)
        fromRoleText (nullable: true)
        toRoleText (nullable: true)
        gnConstant (nullable: true)
    }



    static mapping = {
        comment type: 'text'
        description type: 'text'
        fromRoleText: 'text'
        toRoleText: 'text'
        id type:'integer'
        version type: 'integer'
        extTags cascade:'all-delete-orphan'
    }

    public boolean hasGenericResourceTag(Tag parGenericResourceTag) {
        return (GenericResourceHasTag.findByTagAndGenericResource(parGenericResourceTag, this) != null)
    }

    public getGenericResourceHasRoleHasEvent(RoleHasEvent roleHasEvent) {
        if (roleHasEvent == null) {
            return null;
        }
        List<RoleHasEventHasGenericResource> roleHasEventHasGenericResources = RoleHasEventHasGenericResource.createCriteria().list {
            like("genericResource", this)
            like("roleHasEvent", roleHasEvent)
        }
        if (roleHasEventHasGenericResources.size() == 0) {
            return null;
        }
        return roleHasEventHasGenericResources.first();
    }

    public getGenericResourceHasTag(Tag tag) {
        return GenericResourceHasTag.findByTagAndGenericResource(tag, this);
    }

    boolean isIngameClue()
    {
        return ((this.title != null) && (!this.title.isEmpty()) && (this.description != null)) && (!this.description.isEmpty())
    }


    ArrayList<ReferentialObject> getProposedObject() {
        return this.proposedResources;
    }

    ArrayList<ReferentialObject> getReferentialObject() {
        return Resource.findAll();
    }

    ArrayList<Tag> getTags() {
        ArrayList<Tag> tagsList = new ArrayList<>();

        for (Tag t in this.taglist)
            tags.add(t)

        return tagsList;
    }

    Map<Tag, Integer> getTagsAndWeights(Float ponderation) {
        Map<Tag, Integer> mapTagInt = new HashMap<>();

        for (Tag t in this.taglist)
            mapTagInt.put(t, new Integer((int)(t.weight_substitution * ponderation)))

        return mapTagInt;
    }


    ReferentialObject getLockedObject() {
        return this.lockedResource;
    }

    Plot getPlotFromGenericObject() {
        return getPlot();
    }

    String getSubType() {
        return "genericRessource";
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
}


/*
GenericRessourceHasIngameClue.groovy :


package org.gnk.resplacetime

class GenericResourceHasIngameClue {

    Integer id
    Integer version

    Date lastUpdated
	Date dateCreated
    String title
	String description

	static belongsTo = [ genericResource: GenericResource ]

    static constraints = {
        title (maxSize: 75)
//        description (nullable: true)
    }

    static mapping = {
        description type: 'text'
        id type:'integer'
        version type: 'integer'
    }

    GenericResourceHasIngameClue(GenericResource genericResource, String title, String description) {
        this.genericResource = genericResource
        this.title = title
        this.description = description
    }
}

 */