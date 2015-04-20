package org.gnk.roletoperso

import groovy.sql.Sql
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.Pastscene
import org.gnk.selectintrigue.Plot
import org.gnk.tag.Tag
import org.gnk.tag.TagService
import org.gnk.utils.Sex

class Role implements Comparable {

    Integer id
    Integer version
    Date lastUpdated
    Date dateCreated
    String code
    Integer pipr
    Integer pipi
    String type
    String description
    Integer pjgp
    Plot plot

    static transients = ["DTDId"]

    Integer DTDId;

    static hasMany = [roleHasEvents: RoleHasEvent,
            roleHasPastscenes: RoleHasPastscene,
            roleHasRelationWithRolesForRole1Id: RoleHasRelationWithRole,
            roleHasRelationWithRolesForRole2Id: RoleHasRelationWithRole,
            roleHasTags: RoleHasTag]

    static belongsTo = Plot

    static mappedBy = [genericTextualCluesForFromRoleId: "fromRole",
            genericTextualCluesForPossededByRoleId: "possessedByRole",
            genericTextualCluesForToRoleId: "toRole",
            roleHasRelationWithRolesForRole1Id: "role1",
            roleHasRelationWithRolesForRole2Id: "role2"]

    static constraints = {
        code maxSize: 45
        type maxSize: 3, inList: ["PJ", "PNJ", "PHJ", "TPJ", "PJG", "STF"]
    }

    static mapping = {
        description type: 'text'
        id type: 'integer'
        version type: 'integer'
        roleHasPastscenes cascade: 'all-delete-orphan'
        roleHasEvents cascade: 'all-delete-orphan'
        roleHasTags cascade: 'all-delete-orphan'
    }

    public void addTag(RoleHasTag roleHasTag) {
        if (roleHasTags == null) {
            roleHasTags = new HashSet<RoleHasTag>()
        }
        roleHasTags.add(roleHasTags)
    }

    public boolean hasRoleTag(Tag parRoleTag) {
        return (RoleHasTag.findByTagAndRole(parRoleTag, this) != null);
    }

    public Plot getterPlot() {
        return plot;
    }

    public Set<RoleHasTag> getterRoleHasTag() {
        return getRoleHasTags();
    }

    public Set<RoleHasRelationWithRole> getAllRelationsWhichConcernRole(boolean evenIfNotBijective) {
        Set<RoleHasRelationWithRole> result = new HashSet<RoleHasRelationWithRole>()
        if (roleHasRelationWithRolesForRole1Id != null) {
            result.addAll(roleHasRelationWithRolesForRole1Id)
        }
        if (roleHasRelationWithRolesForRole2Id != null) {
            for (RoleHasRelationWithRole roleHasRelationWithRole : roleHasRelationWithRolesForRole2Id) {
                if (evenIfNotBijective || roleHasRelationWithRole.getIsBijective())
                    result.add(roleHasRelationWithRole)
            }
        }
        return result;
    }

    public boolean isMen() {
        final Sex sex = getterSex()
        return sex == Sex.MALE;
    }

    public boolean isWomen() {
        final Sex sex = getterSex()
        return sex == Sex.FEMALE;
    }

    private Sex getSexOnlyTags() {
        for (RoleHasTag rolehasTag : roleHasTags) {
            Tag tag = rolehasTag.tag

            if (tag.name.equals("Homme")) {
                if (rolehasTag.getterWeight() >= TagService.LOCKED)
                    return Sex.MALE
                if (rolehasTag.getterWeight() <= TagService.BANNED)
                    return Sex.FEMALE
            }

            if (tag.name.equals("Femme")) {
                if (rolehasTag.getterWeight() >= TagService.LOCKED)
                    return Sex.FEMALE
                if (rolehasTag.getterWeight() <= TagService.BANNED)
                    return Sex.MALE
            }
        }

        return Sex.NEUTRAL;
    }

    public Sex getterSex() {
        return getterSexBody(new HashSet<Role>());
    }

    private Sex getterSexBody(Set<Role> processedRoleList) {
        processedRoleList.add(this);
        final Sex sexOnlyTags = getSexOnlyTags()
        if (sexOnlyTags != Sex.NEUTRAL)
            return sexOnlyTags;
        for (RoleHasRelationWithRole roleHasRelationWithRole : getAllRelationsWhichConcernRole(true)) {
            if (roleHasRelationWithRole.getterRoleRelationType().getterSexDiffers()) {
                final Role otherRole = roleHasRelationWithRole.getterOtherRole(this)
                if (processedRoleList.contains(otherRole)) {
                    final Sex otherSexOnlyTags = otherRole.getSexOnlyTags()
                    if (otherSexOnlyTags != Sex.NEUTRAL)
                        return otherSexOnlyTags.getOpposite();
                } else {
                    final Sex otherSexFull = otherRole.getterSexBody(processedRoleList)
                    if (otherSexFull != Sex.NEUTRAL) {
                        return otherSexFull.getOpposite();
                    }
                }
            }
        }
        return Sex.NEUTRAL
    }

    public boolean isPJ() {
        return (type != null &&
                (type.toUpperCase().equals("PJ") || type.toUpperCase().equals("TPJ") || type.toUpperCase().equals("PJG")));
    }

    // Check if the role is Tout Personnage Joueur (cf balise TOUS)
    public boolean isTPJ() {
        return (type != null && type.toUpperCase().equals("TPJ"))
    }

    // Check if the role is STAF
    public boolean isSTF() {
        return (type != null && type.toUpperCase().equals("STF"))
    }

    // Check if the role is Personnage Joueur Générique (cf balise OTHER)
    public boolean isPJG() {
        return (type != null && type.toUpperCase().equals("PJG"))
    }

    public int getPIPTotal() {
        return pipr + pipi;
    }

    @Override
    int compareTo(Object o) {
        if (o instanceof Role) {
            int o1PIP = getPIPTotal();
            int o2PIP = o.getPIPTotal();
            if (o1PIP == o2PIP) {
                return getCode().compareTo(o.getCode());
            }
            return o1PIP - o2PIP;
        }
        return 1  //To change body of implemented methods use File | Settings | File Templates.
    }

    String getterCode() {
        return code;
    }

    public getRoleHasTag(Tag tag) {
        return RoleHasTag.findByTagAndRole(tag, this);
    }

    public getRoleHasEvent(Event event) {
        List<RoleHasEvent> roleHasEvents = RoleHasEvent.createCriteria().list {
            like("role", this)
            like("event", event)
        }
        if (roleHasEvents.size() == 0) {
            return null;
        }
        return roleHasEvents.first();
    }

    public getRoleHasPastScene(Pastscene pastscene) {
        List<RoleHasPastscene> roleHasPastscenes = RoleHasPastscene.createCriteria().list {
            like("role", this)
            like("pastscene", pastscene)
        }
        if (roleHasPastscenes.size() == 0) {
            return null;
        }
        return roleHasPastscenes.first();
    }
}