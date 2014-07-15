package org.gnk.selectintrigue
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericPlace
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.Pastscene
import org.gnk.roletoperso.Role
import org.gnk.tag.Tag
import org.gnk.tag.TagService
import org.gnk.tag.Univers
import org.gnk.user.User
import org.hibernate.Hibernate
import org.hibernate.proxy.HibernateProxy
import org.hibernate.proxy.HibernateProxyHelper

class Plot {

    Integer id
    Integer version

    Date lastUpdated
    Date dateCreated
    private static List<Tag> tagList
    public static List getTagList() {

        return TagService.getPlotTagQuery()
    }


    String name
    //  float plotVersion

    String pitchOrga
    String pitchPj
    String pitchPnj

	Boolean isEvenemential
    Boolean isMainstream
    Boolean isPublic
    Boolean isDraft
	Date creationDate
	Date updatedDate
	String description
	User user

    static transients = ["roleListBuffer", "sumPipRolesBuffer", "plotHasPlotTagListBuffer", "DTDId"]

    Integer sumPipRolesBuffer;
    Integer DTDId;

    static hasMany = [ events: Event,
            extTags: PlotHasTag,
            plotHasUniverses: PlotHasUnivers,
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
        int count = 0;
        int nbPJG_PIP = 0;
		if (!sumPipRolesBuffer){
			sumPipRolesBuffer = 0;
			for(Role role : getRoles()) {
                if (role.isPJ()) {
                    sumPipRolesBuffer += role.getPipi() + role.getPipr();
                    count++
                }
                if (role.isTPJ())
                    sumPipRolesBuffer += nbPlayer * (role.getPipi() + role.getPipr());
                if (role.isPJG())
                    nbPJG_PIP = role.getPipr() + role.getPipi();
			}
            sumPipRolesBuffer += (nbPlayer - count) * nbPJG_PIP;
		}
		return sumPipRolesBuffer;
	}

    public boolean hasPlotTag(Tag parPlotTag) {
        if (parPlotTag instanceof HibernateProxy) {
            Hibernate.initialize(parPlotTag);
            parPlotTag = (Tag) ((HibernateProxy) parPlotTag).getHibernateLazyInitializer().getImplementation();
        }
        for (PlotHasTag plotHasPlotTag : extTags) {
            if (plotHasPlotTag.tag == parPlotTag) {
                return true;
            }
        }
        return false;
    }

    public getPlotHasTag(Tag tag) {
        List<PlotHasTag> plotHasTags = PlotHasTag.createCriteria().list {
            like("plot", this)
            like("tag", tag)
        }
        if (plotHasTags.size() == 0) {
            return null;
        }
        return plotHasTags.first();
    }

    public boolean hasUnivers(Univers parUnivers) {
        for (PlotHasUnivers plotHasUnivers : plotHasUniverses) {
            if (plotHasUnivers.univers == parUnivers) {
                return true;
            }
        }
        return false;
    }

    public boolean isUniversGeneric() {
        int i = 0;
        for (PlotHasUnivers plotHasUnivers : plotHasUniverses) {
            i++;
        }
        return i == 0;
    }

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
            if (role.getPipi() + role.getPipr() >= gnPipcore) {
                pipCoreOkNumber += 1;
            }
        }
        return pipCoreOkNumber;
    }
}
