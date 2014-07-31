package org.gnk.roletoperso

import com.granicus.grails.plugins.cookiesession.SessionPersistenceListener
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
    private String type

    Integer getAge() {
        return age
    }

    void setAge(Integer age) {
        this.age = age
    }
    private Integer age

    private List<Role> lockedRoles = []
    private List<Role> selectedRoles = []
    private List<Role> selectedPJG = []
    private List<Role> bannedRoles = []
    private List<Role> specificRoles = []
    private List<Integer> plotid_role = []
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
        assert (formattedType.equals("PNJ") || formattedType.equals("PHJ") || formattedType.equals("PJ"))
        if (!(formattedType.equals("PNJ") || formattedType.equals("PHJ") || formattedType.equals("PJ")))
        {
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

    public List<Integer> getplotid_role() {
        return plotid_role
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
        if ((!role.isTPJ()) && (!role.isPJG()) && (!specificRoles.contains(role))) {
            specificRoles.add(role);
            plotid_role.add(role.plotId as Integer);
        }

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
        Map<Tag, Integer> tagMap = new HashMap<Tag, Integer>();
        List<Tag> tag_101 = new ArrayList<Tag>();
        List<Tag> tag_m101 = new ArrayList<>();
        for (Role role : selectedRoles) {
            for (RoleHasTag roleHasTag : role.getRoleHasTags()) {
                final Tag tag = roleHasTag.getTag()
                Integer weight = roleHasTag.getWeight() * (isPJ() ? roleHasTag.getRole().getPIPTotal() : 99)
                if (weight == 101)
                    tag_101.add(tag);
                if (weight == -101)
                    tag_m101.add(tag);
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
            int weight = tagMap.get(tagKey);
            if (tag_101.contains(tagKey)) {
                tagMap.put(tagKey, 101);
            }
            else if (tag_m101.contains(tagKey)) {
                tagMap.put(tagKey, -101);
            }
            else
            {
                if (nbPIP != 0)
                    weight /= nbPIP
                tagMap.put(tagKey, weight)
            }
        }
        if (isMen())
            tagMap.put(Tag.get(21) , 101);
        if (isWomen())
            tagMap.put(Tag.get(22) , 101);
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

            if(nbPIP != 0)
                relationMap.put(tagKey, relationMap.get(tagKey) / nbPIP)
            else
                relationMap.put(tagKey, relationMap.get(tagKey))
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

    public Map<Character, List<RoleHasRelationWithRole>> getCharacterRelations(Gn gnInstance) {
        final Map<Character, Set<RoleHasRelationWithRole>> relations = getRelatedCharactersExceptBijectives(gnInstance);
        final Map<Character, List<RoleHasRelationWithRole>> result = new HashMap<Character, List<RoleHasRelationWithRole>>();

        for (Character character : relations.keySet()) {
            List<RoleHasRelationWithRole> list = new ArrayList<RoleHasRelationWithRole>();
            for (RoleHasRelationWithRole roleHasRelationWithRole : relations.get(character)) {
                list.add(roleHasRelationWithRole);
            }
            if (list.size() > 0)
                result.put(character, list);
        }

        for (Character c : gnInstance.getterCharacterSet())
        {
            if (c != this)
            {
                Map<Character, Set<RoleHasRelationWithRole>> bijrela = c.getRelatedCharactersExceptBijectives(gnInstance);
                Set<RoleHasRelationWithRole> bijective = bijrela.get(this);
                if (bijective.size() > 0)
                {
                    for (RoleHasRelationWithRole bij : bijective)
                    {
                        if (bij.isBijective == true)
                        {
                            List<RoleHasRelationWithRole> listbij = result.get(c);
                            if (listbij == null)
                                listbij = new ArrayList<RoleHasRelationWithRole>();
                            listbij.add(bij);
                            result.remove(c);
                            result.put(c, listbij);
                        }
                    }
                }
            }
        }

        return result;
    }

    public String rolesToString()
    {
        String result = "";

        for (Role r in selectedRoles)

        {
            if (result != "")
                result += ", "
            result += r.code;
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

            if(nbPIP != 0)
                relationMap.put(tagKey, relationMap.get(tagKey) / nbPIP)
            else
                relationMap.put(tagKey, relationMap.get(tagKey))
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

    public void addplotid_role(List<Integer> list) {
        this.plotid_role.addAll(list);
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

    public int getCharacterAproximateAge() {
        int sum = 0
        int number = 0
        int finalAge = 0

        tags.each { tag ->
            int sumTmp = sum
            sum += getAgeForTagAge(tag.key, tag.value)
            if (sumTmp != sum)
                number += 1
        }
        if (number == 0) {
            finalAge = 40
        } else {
            finalAge = sum / number
        }

        //Fun
        finalAge += (((new Random()).nextInt() % 8))
        if (finalAge < 0)
            finalAge = 0

        return finalAge;
    }

    public static int getAgeForTagAge(Tag t, int value) {
        int age = 0

        //if (t.id == 34 || t.id == 33|| t.id == 32 || t.id == 31)
        //    println(t.name + " - " + value)

        // Formule de maths toutes arbitraires
        if (t.id == 34) { //Vieux 80
            age = 80 + Math.pow(((value - 25)/23), 3)/3
        } else if (t.id == 33) { //Âge mur 50
            age = 50 + Math.pow((value/38), 3)/3
        } else if (t.id == 32) { //Jeune adulte 30
            age = 30 - Math.pow((value/38), 3)/3
        } else if (t.id == 31) { // Très jeune 15
            age = 15 - Math.pow(((value - 25)/23), 3)/3
        }
        //if (t.id == 34 || t.id == 33|| t.id == 32 || t.id == 31)
        //    println(t.name + " - " + value + "   AGE :" + age)
        return age
    }
}
