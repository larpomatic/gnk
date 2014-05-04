package org.gnk.roletoperso

import org.gnk.gn.Gn
import org.gnk.naming.Firstname
import org.gnk.naming.Name
import org.gnk.selectintrigue.Plot
import org.gnk.tag.Tag

class Character {
    static CharacterService cs
    Integer DTDId
    Integer DTDFirstNameId
    Integer DTDLastNameId
    String lastname
    String firstname
    // m, f, n
    private String gender
    private String type;

    private List<Role> lockedRoles = []
    private List<Role> selectedRoles = []
    private List<Role> selectedPJG = []
    private List<Role> bannedRoles = []
    private List<Role> specificRoles = []

    // Substitution
    List<Firstname> proposedFirstnames
    List<Firstname> bannedFirstnames
    Firstname selectedFirstname
    List<Name> proposedLastnames
    List<Name> bannedLastnames
    Name selectedLastname

    public void setGender(String gender) {
        assert (gender != null)
        if (gender == null) {
            this.gender = "N"
            return
        }
        String formattedGender = gender.toUpperCase()
        assert (formattedGender.equals("M") || formattedGender.equals("F") || formattedGender.equals("N"))
        if (!(formattedGender.equals("M") || formattedGender.equals("F") || formattedGender.equals("N"))) {
            this.gender = "N"
            return
        }
        this.gender = formattedGender
    }

    public void setType(String type) {
        assert (type != null)
        if (type == null) {
            this.type = "PHJ"
            return
        }
        String formattedType = type.toUpperCase()
        assert (formattedType.equals("PNJ") || formattedType.equals("PHJ") || formattedType.equals("PJ")
                || formattedType.equals("TPJ") || formattedType.equals("PJG"))
        if (!(formattedType.equals("PNJ") || formattedType.equals("PHJ") || formattedType.equals("PJ")
        || formattedType.equals("PJG") || formattedType.equals("TPJ"))) {
            this.type = "PHJ"
            return
        }
        this.type = formattedType
    }

    public boolean isPJ() {
        return this.type.equals("PJ");
    }

    public boolean isPNJ() {
        return this.type.equals("PNJ");
    }

    public boolean isPHJ() {
        return this.type.equals("PHJ");
    }

    public boolean isNeutralGender() {
        return this.gender.equals("N");
    }

    public boolean isMen() {
        return this.gender.equals("M");
    }

    public boolean isWomen() {
        return this.gender.equals("F");
    }

    public String getGender() {
        return gender;
    }

    public List<Role> getLockedRoles() {
        return lockedRoles
    }

    public List<Role> getSelectedRoles() {
        return selectedRoles
    }

    public List<Role> getSelectedPJG() {
        return selectedPJG;
    }

    public List<Role> getSpecificRoles() {
        return specificRoles
    }

    public List<Role> getGenericRoles() {
        return this.genericRoles
    }

    public List<Role> getBannedRoles() {
        return bannedRoles
    }

    public List<Tag> getRoleTags() {
        return roleTags
    }

    public addRole(Role role) {
        bannedRoles.remove(role)
            if (!selectedRoles.contains(role))
                selectedRoles.add(role)
            if ((role.isPJG()) && (!selectedPJG.contains(role)))
                selectedPJG.add(role)
            if ((!role.isTPJ()) && (!role.isPJG()) && (!specificRoles.contains(role)))
                specificRoles.add(role)
    }

    public lockRole(Role role) {
        addRole(role)
        lockedRoles.add(role)
    }

    public void removeSelectedAndLockedRoles() {
        selectedRoles = new ArrayList<Role>();
        lockedRoles = new ArrayList<Role>();
    }

    public removeRole(Role role) {
        selectedRoles.remove(role)
        lockedRoles.remove(role)
    }

    public banRole(Role role) {
        removeRole(role)
        bannedRoles.add(role)
    }

    public unbanRole(Role role) {
        bannedRoles.remove(role)
    }

    public Character() {
        this.DTDId = -1
    }

    public Character(int DTDId, String gender) {
        this.DTDId = DTDId
        setType("PJ")
        setGender(gender)
    }

    public Character(int DTDId, String gender, Role NPCRole) {
        this.DTDId = DTDId
        setGender(gender)
        setType(NPCRole.type)
        lockRole(NPCRole);
    }

    public int getNbPIP() {
        int result = 0;
        for (Role role : selectedRoles) {
            result += role.getPIPTotal();
        }
        return result;
    }

    public Map<Tag, Boolean> getlockedAndBannedTags() {
        Map<Tag, Boolean> lockBanTags = new HashMap<Tag, Boolean>();
        for (Role role : getSelectedRoles()) {
            for (RoleHasTag roleHasTag : role.getterRoleHasTag()) {
                if (roleHasTag.tagIsLocked() || roleHasTag.tagIsBanned()) {
                    lockBanTags.put(roleHasTag.getterTag(), roleHasTag.tagIsLocked());
                }
            }
        }
        return lockBanTags;
    }

    public Map<Tag, Integer> getTags() {
        Map<Tag, Integer> tagMap = new HashMap<Tag, Integer>()
        for (Role role : selectedRoles) {
            for (RoleHasTag roleHasTag : role.getRoleHasTags()) {
                final Tag tag = roleHasTag.getTag()
                Integer weight = roleHasTag.getWeight() * (isPJ() ? roleHasTag.getRole().getPIPTotal() : 99)
                if (tagMap.containsKey(tag)) {
                    weight += tagMap.get(tag)
                }
                tagMap.put(tag, weight)
            }
        }
        final Integer nbPIP = getNbPIP()
        if (!isPJ())
            nbPIP = 99;
        Set<Tag> tagKeySet = tagMap.keySet()
        for (Tag tagKey : tagKeySet) {
            int weight = tagMap.get(tagKey)
            if (nbPIP != 0)
                weight /= nbPIP
            tagMap.put(tagKey, weight)
        }
        return tagMap
    }

    public Map<RoleHasRelationWithRole, Integer> getRelationsExceptBijectives() {
        Map<RoleHasRelationWithRole, Integer> relationMap = new HashMap<RoleHasRelationWithRole, Integer>()
        for (Role role : selectedRoles) {
            for (RoleHasRelationWithRole roleHasRelation : role.getRoleHasRelationWithRolesForRole1Id()) {
                Integer weight = roleHasRelation.getWeight() * roleHasRelation.getRole1().getPIPTotal()
                if (relationMap.containsKey(roleHasRelation)) {
                    weight += relationMap.get(roleHasRelation)
                }
                relationMap.put(roleHasRelation, weight)
            }
        }
        final Integer nbPIP = getNbPIP()
        Set<RoleHasRelationWithRole> tagKeySet = relationMap.keySet()
        for (RoleHasRelationWithRole tagKey : tagKeySet) {
            relationMap.put(tagKey, relationMap.get(tagKey) / nbPIP)
        }
        return relationMap
    }

    public Map<Character, Set<RoleHasRelationWithRole>> getRelatedCharactersExceptBijectives(Gn gnInstance) {
        final Map<RoleHasRelationWithRole, Integer> relations = getRelationsExceptBijectives();
        final Map<Character, Set<RoleHasRelationWithRole>> result = new HashMap<Character, Set<RoleHasRelationWithRole>>();
        for (Character character : gnInstance.getterCharacterSet()) {
            final Set<RoleHasRelationWithRole> relationsForThisChar = new HashSet<RoleHasRelationWithRole>();
            result.put(character, relationsForThisChar);
        }
        for (RoleHasRelationWithRole relationWithRole : relations.keySet()) {
            final Role otherRole = relationWithRole.getterRole2();
            if (otherRole.isPJ()) {
                Character other = gnInstance.getCharacterContainingRole(otherRole);
                if (other != null) {
                    result.get(other).add(relationWithRole);
                }
            }
        }
        return result;
    }

    public Map<Character, String> getRelatedCharactersExceptBijectivesLabel(Gn gnInstance) {
        final Map<Character, Set<RoleHasRelationWithRole>> relations = getRelatedCharactersExceptBijectives(gnInstance);
        final Map<Character, String> result = new HashMap<Character, String>(relations.size());

        for (Character character : relations.keySet()) {
            StringBuilder label = new StringBuilder();
            for (RoleHasRelationWithRole roleHasRelationWithRole : relations.get(character)) {
                label.append(" - ").append(roleHasRelationWithRole.getterRoleRelationType().getterName());
            }
            label.delete(0, 2);
            result.put(character, label.toString());
        }
        return result;
    }

    public Map<RoleHasRelationWithRole, Integer> getRelations(boolean evenIfNonBijective) {
        Map<RoleHasRelationWithRole, Integer> relationMap = new HashMap<RoleHasRelationWithRole, Integer>()
        for (Role role : selectedRoles) {
            for (RoleHasRelationWithRole roleHasRelation : role.getRoleHasRelationWithRolesForRole1Id()) {
                Integer weight = roleHasRelation.getWeight() * roleHasRelation.getRole1().getPIPTotal()
                if (relationMap.containsKey(roleHasRelation)) {
                    weight += relationMap.get(roleHasRelation)
                }
                relationMap.put(roleHasRelation, weight)
            }

            for (RoleHasRelationWithRole roleHasRelation : role.getRoleHasRelationWithRolesForRole2Id()) {
                if (!evenIfNonBijective && !roleHasRelation.getIsBijective())
                    continue;

                Integer weight = roleHasRelation.getWeight() * roleHasRelation.getRole2().getPIPTotal()
                if (relationMap.containsKey(roleHasRelation)) {
                    weight += relationMap.get(roleHasRelation)
                }
                relationMap.put(roleHasRelation, weight)
            }
        }
        final Integer nbPIP = getNbPIP()
        Set<RoleHasRelationWithRole> tagKeySet = relationMap.keySet()
        for (RoleHasRelationWithRole tagKey : tagKeySet) {
            relationMap.put(tagKey, relationMap.get(tagKey) / nbPIP)
        }
        return relationMap
    }

    public boolean roleIsBanned(Role role) {
        return bannedRoles.contains(role);
    }

    public boolean roleIsSelected(Role role) {
        return selectedRoles.contains(role) || lockedRoles.contains(role);
    }

    public boolean roleIsLocked(Role role) {
        return lockedRoles.contains(role)
    }

    public Set<Plot> getPlotSet() {
        Set<Plot> result = new HashSet<Plot>(getSelectedRoles().size() + getLockedRoles().size());
        for (Role role : getSelectedRoles()) {
            result.add(role.getPlot())
        }
        for (Role role : getLockedRoles()) {
            result.add(role.getPlot())
        }
        return result
    }

    public String getCharacterType() {
        final List<Role> roles = getSelectedRoles()
        if (roles) {
            return roles.first().getType();
        }
        return ""
    }

}
