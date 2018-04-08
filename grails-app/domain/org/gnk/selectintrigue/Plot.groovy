package org.gnk.selectintrigue
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.Pastscene
import org.gnk.roletoperso.Role
import org.gnk.tag.Tag

import org.gnk.user.User

class Plot {

    Integer id
    Integer version

    Date lastUpdated
    Date dateCreated
    private static List<Tag> tagList
    public static List getTagList() {

        return getPlotTagQuery()
    }

    String name
    //  float plotVersion

    String pitchOrga
    String pitchPj
    String pitchPnj

    List<Description> list_Description

	Boolean isEvenemential
    Boolean isMainstream
    Boolean isPublic
    Boolean isDraft
//	Date creationDate
//	Date updatedDate
	String description
	User user
    Integer variant

    static transients = ["roleListBuffer", "sumPipRolesBuffer", "plotHasPlotTagListBuffer", "DTDId", "List<Description> list_Description"]

    Integer sumPipRolesBuffer;
    Integer DTDId;

    static hasMany = [ events: Event,
                       extTags: PlotHasTag,
                       roles: Role,
                       pastescenes: Pastscene,
                       genericResources: GenericResource,
                       genericPlaces: GenericPlace]

    static belongsTo = User

    static constraints = {
        name maxSize: 45
        pitchOrga (nullable: true)
        pitchPj (nullable: true)
        pitchPnj (nullable: true)
        variant (nullable: true)
    }

    static mapping = {
        autoTimestamp true
        pitchOrga type: 'text'
        pitchPj type: 'text'
        pitchPnj type: 'text'
		description type: 'text'
        id type:'integer'
        version type: 'integer'
        events cascade: 'all-delete-orphan'
//        plotHasUniverses cascade: 'all-delete-orphan'
        roles cascade: 'all-delete-orphan'
//        extTags cascade: 'all-delete-orphan'
        pastescenes cascade: 'all-delete-orphan'
    }

    public Set<Role> getterRoles (){
        return roles;
    }

    public void addARole(Role role) {
        if (roles == null) {
            roles = new HashSet<Role>()
        }
        roles.add(role)
        role.plot = this
    }

    public int getTagWeight (Tag plotTag){
        for(PlotHasTag plotHasPlotTag : extTags) {
            if (plotHasPlotTag.tag == plotTag)
                return plotHasPlotTag.weight
        }
        return 0;
    }

	public int getSumPipRoles(int nbPlayer){
        assert (nbPlayer != null);
        int count = 0;
        int nbPJG_PIP = 0;
        int nbPJ = 0
        for (Role role : getterRoles())
            if (role.isPJ())
                nbPJ++
		if (!sumPipRolesBuffer){
			sumPipRolesBuffer = 0;
			for(Role role : getRoles()) {
                if (role.isPJ()) {
                    sumPipRolesBuffer += role.getPip();
                    count++
                }
                if (role.isTPJ())
                    sumPipRolesBuffer += nbPlayer * role.getPip();
                if (role.isPJG() && role.getPjgp() != null)
                    nbPJG_PIP = role.getPip() * role.getPjgp() / 100 *(nbPlayer - nbPJ);
			}
            sumPipRolesBuffer += (nbPlayer - count) * nbPJG_PIP;
		}
		return sumPipRolesBuffer;
	}

    public boolean hasPlotTag(Tag parPlotTag) {
        return (PlotHasTag.findByTagAndPlot(parPlotTag, this) != null)
    }

    public getPlotHasTag(Tag tag) {
        return PlotHasTag.findByTagAndPlot(tag, this);
    }

    public getPlotHasTag() {
        return PlotHasTag.findAllByPlot(this).sort { -it.weight };
    }

//    public boolean hasUnivers(Tag tagUnivers) {
//        for (PlotHasUnivers plotHasUnivers : plotHasUniverses) {
//            if (plotHasUnivers.univers == parUnivers) {
//                return true;
//            }
//        }
//        return false;
//    }

//    public boolean isUniversGeneric() {
//        int i = 0;
//        for (PlotHasUnivers plotHasUnivers : plotHasUniverses) {
//            i++;
//        }
//        return i == 0;
//    }

    public int getNbMinMen () {
        int number = 0;
        for (Role role : getRoles()) {
            if (role.isPJ() && role.isMen()) {
                number++;
            }
        }
        return number;
    }

    public int getNbMinWomen () {
        int number = 0;
        for (Role role : getRoles()) {
            if (role.isPJ() && role.isWomen()) {
                number++;
            }
        }
        return number;
    }

    Integer getterId() {
        return id;
    }

    public int getNbRoleOverPipcore(int gnPipcore) {
        int pipCoreOkNumber = 0;
        for (Role role in roles) {
            if (role.getPip() >= gnPipcore) {
                pipCoreOkNumber += 1;
            }
        }
        return pipCoreOkNumber;
    }

    def static List<Tag> getPlotTagQuery() {
        ArrayList<Tag> genericChilds = getGenericChilds();
        ArrayList<Tag> result = new ArrayList<>();
        for (Tag child in genericChilds) {
            Tag tagRelevant = Tag.findByTag(child);
            if (tagRelevant && tagRelevant.relevantPlot) {
                result.add(child);
            }
        }
        return result;
    }

    public String getMetric() {
        String newLigne = "<br />";
        String role = "Nb Roles : " + this.roles.size();
        String res = "Nb Ressources : " + this.genericResources.size();
        String place = "Nb Lieux : " + this.genericPlaces.size();
        String evt = "Nb Evénements : " + this.events.size();
        return "<b>" + this.name + "</b>" + newLigne + role + newLigne + res + newLigne + place + newLigne +evt;
    }

    public void add_Description(Description description)
    {
        if (list_Description == null)
            list_Description = new ArrayList<Description>()
        list_Description.add(description)
    }
}
