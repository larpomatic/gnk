package org.gnk.roletoperso;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.gnk.gn.Gn;
import org.gnk.selectintrigue.Plot;
import org.gnk.tag.Tag;
import org.gnk.tag.TagRelation;
import org.gnk.tag.TagService
import org.javatuples.Pair
import sun.nio.cs.StreamDecoder
import sun.rmi.runtime.Log;

import java.util.*;

/**
 * User: Bastien
 * Date: 19/10/13
 * Time: 20:09
 */
public class RoleToPersoProcessing {
    static private final Logger LOG = Logger.getLogger(RoleToPersoProcessing.class);

    private Gn gn;
    private OrderedRoleSet gnRoleSetToProcess;
    private Set<Role> gnNPCRoleSet;
    public Set<Role> gnTPJRoleSet;
    public Set<Role> gnPJGRoleSet;
    public Set<Role> gnSTFRoleSet;
    public Set<Role> gnPJBRoleSet;
    private Map<Role, String> unAttribuedRoleWithSettedSex;


    public RoleToPersoProcessing(Gn gn) {
        /* OFF, INFO, DEBUG, TRACE */
        LOG.setLevel(Level.DEBUG);
        LOG.info("<R2P>");
        assert (gn != null);
        if (gn == null) {
            LOG.error("R2P : Gn is null");
            return;
        }
        this.gn = gn;
        unAttribuedRoleWithSettedSex = new HashMap<Role, String>();
        process();
        LOG.info("</R2P>");
    }


    private void process() {
        LOG.info("\t<Algo>");

        initRoles();
        initCharacters();
        // add rôle TOUS
        addTPJ();
        associateRolesToCharacters();
        // add rôle OTHER
        addPJB();
        // determine pnjsable
        addPJG();
        // Create & add STF Role
        createSTFCharacter();
        // Harmonize Sex -- Warning to relation type
        reHarmonizeRole();
        LOG.info("\t</Algo>");

    }

    private void createSTFCharacter()
    {
        gn.setStaffCharSet(new HashSet<Character>());
        int nb = this.gn.getterNonPlayerCharSet().size() + this.gn.getterCharacterSet().size();
        for (Role r in this.gnSTFRoleSet) {
            nb += 1;
            Character c = new Character();
            c.setDTDId(nb);
            c.setType("STF");
            c.setGender('N');
            c.addRole(r);
            this.gn.getStaffCharSet().add(c);
        }
    }

    private void reHarmonizeRole()
    {
        int nb_men = gn.getNbMen();
        int nb_women = gn.getNbWomen();
        Map<Integer, Character> women_list = new HashMap<Integer, Character>();
        Map<Integer, Character> men_list = new HashMap<Integer, Character>();
        for (Character c : gn.getterCharacterSet())
        {
            Map<Tag, Integer> tags = c.getTags();
            int wWomen = 0;
            int wMen = 0;
            for (Tag t : tags.keySet())
            {
                if (t.getName() == "Homme")
                    wMen += tags.get(t);
                if (t.getName() == "Femme")
                    wWomen += tags.get(t);
            }
            if (wWomen > wMen) {
                LOG.info("Harmonize Character " + c.getDTDId() + " FROM " + c.getGender() + " TO " + "F");
                c.setGender("F");
                nb_women -= 1;
                women_list.put(wWomen - wMen, c);
            }
            if (wMen > wWomen) {
                LOG.info("Harmonize Character " + c.getDTDId() + " FROM " + c.getGender() + " TO " + "M");
                c.setGender("M");
                nb_men -= 1;
                men_list.put(wMen - wWomen, c);
            }
            if (wMen == wWomen) {
                if (c.isMen()) {
                    men_list.put(0, c);
                    nb_men -= 1;
                }
                else {
                    women_list.put(0, c);
                    nb_women -= 1;
                }
            }
        }
        if (nb_men > 0) {
            LOG.info("NEED MORE MEN");
            List<Integer> dif = new ArrayList<Integer>();
            dif.addAll(women_list.keySet());
            dif.sort { a, b -> a.compareTo(b)};
            while (nb_men > 0) {
                Character change = women_list.get(dif.first());
                LOG.info("BEST : " + dif.first());
                dif.remove(0);
                nb_men -= 1;
                change.setGender("M");
                LOG.info("Harmonize : CHAR-" + change.getDTDId() + " To M");
            }
        }
        if (nb_women > 0) {
            LOG.info("NEED MORE WOMEN");
            List<Integer> dif = new ArrayList<Integer>();
            dif.addAll(men_list.keySet());
            dif.sort { a, b -> a.compareTo(b)};
            while (nb_women > 0) {
                Character change = men_list.get(dif.first());
                LOG.info("BEST : " + dif.first());
                dif.remove(0);
                nb_women -= 1;
                change.setGender("F");
                LOG.info("Harmonize : CHAR-" + change.getDTDId() + " To F");
            }
        }
    }

    private void associateRolesToCharacters() {
        OrderedRoleSet gnRoleSetToProcess = this.gnRoleSetToProcess;
        int equilibrageALaConQuandOnPeutPlusRienFaireDAutreAFlagelerBastienF = 0;
        final Set<Character> characterSet = gn.getCharacterSet();
        for (Character character : characterSet) {
            gnRoleSetToProcess.removeAll(character.getSelectedRoles());
        }
        phase1SelectCharacterCoreRole(gnRoleSetToProcess);
        Map<Character, Map<Tag, Boolean>> lockedBannedTagForCharacters = getAllLockedBannedTagsForCharacters(characterSet);
        while (!gnRoleSetToProcess.isEmpty()) {
            Role role = gnRoleSetToProcess.first();
            Set<Character> bannedCauseBanTagCharacters = new HashSet<Character>();
            Integer correspondenceRank = null;
            Character bestCharRanked = null;
            for (Character character : characterSet) {
                int characterNbPIP = character.getNbPIP();
                if (characterNbPIP < gn.getPipMin()) {
                    if (roleIsCompatibleNoPIP(role, character) && characterNbPIP + role.getPIPTotal() < gn.getPipMax()) {
                        Integer rank = getRoleRank(character, role, lockedBannedTagForCharacters.get(character));
                        if (rank == Integer.MIN_VALUE)
                            bannedCauseBanTagCharacters.add(character);
                        else if (bestCharRanked == null || correspondenceRank == null || rank > correspondenceRank) {
                            correspondenceRank = rank;
                            bestCharRanked = character;
                        }
                    }
                }
            }
            if (bestCharRanked == null) {
                Character lowerCharacter = null;
                int lowerCharacterNbPIP = 0;
                Set<Character> charactersWhereToFindLeastBad = new HashSet<Character>(characterSet);
                charactersWhereToFindLeastBad.removeAll(bannedCauseBanTagCharacters);
                for (Character c : charactersWhereToFindLeastBad) {
                    if (roleIsCompatibleNoPIP(role, c)) {
                        if (lowerCharacter == null) {
                            lowerCharacter = c;
                            lowerCharacterNbPIP = lowerCharacter.getNbPIP();
                        } else if (lowerCharacterNbPIP > c.getNbPIP()) {
                            lowerCharacter = c;
                            lowerCharacterNbPIP = lowerCharacter.getNbPIP();
                        }
                    }
                }
                if (lowerCharacter == null) {
                    int varPourrieLocaleAFlagelerBastienF = 0;
                    for (Character c : characterSet) {
                        if (varPourrieLocaleAFlagelerBastienF == equilibrageALaConQuandOnPeutPlusRienFaireDAutreAFlagelerBastienF) {
                            lowerCharacter = c;
                            equilibrageALaConQuandOnPeutPlusRienFaireDAutreAFlagelerBastienF++;
                            break;
                        }
                        varPourrieLocaleAFlagelerBastienF++;
                    }
                }
                bestCharRanked = lowerCharacter;
            }
            if (bestCharRanked == null) {
                bestCharRanked = gn.characterSet.first()
            }
            addRoleAndSexualizeIFN(bestCharRanked, role);
            gnRoleSetToProcess.remove(role);
            lockedBannedTagForCharacters.put(bestCharRanked, bestCharRanked.getlockedAndBannedTags());
        }
        sexualizeAllNeutralCharacters();
    }

    private Map<Character, Map<Tag, Boolean>> getAllLockedBannedTagsForCharacters(Set<Character> characters) {
        Map<Character, Map<Tag, Boolean>> result = new HashMap<Character, Map<Tag, Boolean>>(characters.size());
        for (Character c : characters) {
            result.put(c, c.getlockedAndBannedTags());
        }
        return result;
    }

    private void sexualizeAllNeutralCharacters() {
        for (Character c : gn.getCharacterSet()) {
            if (c.isNeutralGender()) {
                final String gender = getBestSexIsMenForCharacter(c) ? "M" : "F";
                c.setGender(gender);
                LOG.debug(new StringBuilder("Character ").append(c.getDTDId().toString()).append(" setted to ").append(gender).append(" because he was neutral at the end."));
            }
        }
        for (Character c : gn.getNonPlayerCharSet()) {
            final String gender_nonplayed = getBestSexIsMenForCharacter(c) ? "M" : "F";
            c.setGender(gender_nonplayed);
            LOG.debug(new StringBuilder("Character ").append(c.getDTDId().toString()).append(" setted to ").append(gender_nonplayed).append(" because he was neutral at the end."));
        }
    }

    private boolean getBestSexIsMenForCharacter(Character c) {
        int menWeight = 0;
        int womenWeight = 0;
        Map<Tag, Integer> tags = c.getTags();
        for (Tag tag : tags.keySet()) {
            if (tag.getterName().equals("Homme")) {
                menWeight += tags.get(tag);
            } else if (tag.getterName().equals("Femme")) {
                womenWeight += tags.get(tag);
            }
        }
        if (menWeight == womenWeight) {
            if (new Random().nextInt(2) == 0)
                menWeight--;
        }
        return menWeight > womenWeight;
    }

    /* Have to be passed by roleIsCompatibleNoPIP to be sure it's OK */
    private void addRoleAndSexualizeIFN(Character c, Role role) {
        if (c.isNeutralGender() == false) { // Si le personnage est déjà sexué on cherche pas plus loin le rôle prend le même sexe
            sexualizeRole(role, c.isMen());
        } else {
            // Si le personnage n'est pas sexué mais le rôle si, le personnage prend le sexe du rôle
            String sexDefinedPreviously = unAttribuedRoleWithSettedSex.get(role);
            if (role.isMen() || role.isWomen()) { // Les tags de l'auteur de l'intrigue gagnent sur les relations définies par R2P
                sexualizeCharacter(c, role.isMen());
                if (sexDefinedPreviously == null) { // Si ce rôle n'est jamais passé par la sexualisation alors il faut l'y faire passer pour sexuer ses relations
                    sexualizeRole(role, role.isMen());
                }
            } else if (sexDefinedPreviously != null) {
                sexualizeCharacter(c, sexDefinedPreviously.equals("M"));
            } else { // Si ce rôle n'est jamais passé par la sexualisation alors il faut vérifier s'il a des relations qui imposent le sexe, si tel est le cas il faudra sexuer le personnage
                boolean hasToSex = false;
                for (RoleHasRelationWithRole relation : role.getAllRelationsWhichConcernRole(true)) {
                    if (relation.getterRoleRelationType().getterSexDiffers()) {
                        hasToSex = true;
                        break;
                    }
                }
                if (hasToSex) {
                    boolean toMan = getBestSexIsMenForCharacter(c);
                    sexualizeCharacter(c, toMan);
                    sexualizeRole(role, toMan);
                }
            }
        }
        unAttribuedRoleWithSettedSex.remove(role);
        c.addRole(role);
    }

    private boolean roleIsCompatibleNoPIP(Role role, Character character) {
        if (!character.isNeutralGender()) {
            if ((role.isMen() && character.isWomen()) || (role.isWomen() && character.isMen()))
                return false;
            if (unAttribuedRoleWithSettedSex.containsKey(role)) {
                String roleGender = unAttribuedRoleWithSettedSex.get(role);
                if ((character.isWomen() && roleGender.equals("M")) || (character.isMen() && roleGender.equals("F")))
                    return false;
            }
        }

        // Variant processing, a character can't be a part of 2 variant plots
        for (Role selectedRole : character.getSelectedRoles()) {
            if (role.getPlot().getVariant() != null && selectedRole.getPlot().getId() == role.getPlot().getVariant()
            || (selectedRole.getPlot().getVariant() != null && selectedRole.getPlot().getVariant() == role.getPlot().getVariant())
            || (selectedRole.getPlot().getVariant() != null && selectedRole.getPlot().getVariant() == role.getPlot().getDTDId()))
                return false;
        }

        return !character.roleIsBanned(role) && !character.getPlotSet().contains(role.getterPlot());
    }

    private void phase1SelectCharacterCoreRole(OrderedRoleSet gnRoleSetToProcess) {
        for (Character character : gn.getCharacterSet()) {
            int characterNbPIP = character.getNbPIP();
            if (characterNbPIP > 0) // Le personnage à des roles locked sur lui donc on lui remet pas de core par dessus
                continue;
            if (characterNbPIP < gn.getPipMin()) {
                for (Role role : gnRoleSetToProcess) {
                    if (roleIsCompatibleNoPIP(role, character) && characterNbPIP + role.getPIPTotal() < gn.getPipMax()) { //FIXME Faire correct
                        addRoleAndSexualizeIFN(character, role);
                        gnRoleSetToProcess.remove(role);
                        break;
                    }
                }
                if (gnRoleSetToProcess.isEmpty())
                    break;
            }
        }
    }

    private boolean tagIsLocked(Map.Entry<Tag, Integer> valuedTag) {
        return valuedTag.getValue() == TagService.LOCKED;
    }

    private boolean tagIsBanned(Map.Entry<Tag, Integer> valuedTag) {
        return valuedTag.getValue() == TagService.BANNED;
    }

    private int getRankTag(Map<Tag, Integer> refTagList, Map<Tag, Integer> challengerTagList, Map<Tag, Boolean> refLockedBannedTags) {
        Integer rankTag = 0;
        if (challengerTagList != null) {
            for (Map.Entry<Tag, Integer> challengerTag : challengerTagList.entrySet()) {
                Boolean tagIsLockedOrBannedForCharacter = refLockedBannedTags.get(challengerTag.getKey());
                if (tagIsLockedOrBannedForCharacter != null) {
                    if ((tagIsLocked(challengerTag) && !tagIsLockedOrBannedForCharacter) || (tagIsBanned(challengerTag) && tagIsLockedOrBannedForCharacter)) {
                        return Integer.MIN_VALUE;
                    }
                }
                for (Tag refTag : refTagList.keySet()) {
                    TagRelation tagRelation1 = TagRelation.myFindWhere(challengerTag.getKey(), refTag);
                    TagRelation tagRelation2 = TagRelation.myFindWhere(refTag, challengerTag.getKey());
                    int lockSignOfRoleTag = tagIsLocked(challengerTag) ? 1 : tagIsBanned(challengerTag) ? -1 : 0;
                    Boolean characterTagIsLockedOrBanned = refLockedBannedTags.get(refTag);
                    int lockSignOfCharTag = characterTagIsLockedOrBanned == null ? 0 : characterTagIsLockedOrBanned ? 1 : -1;

                    if (tagRelation1 != null || tagRelation2 != null) {
                        int weight = 0;
                        int divider = 0;
                        int lockSignOfTagRelation1 = tagRelation1 == null ? 0 : tagRelation1.isLocked() ? 1 : tagRelation1.isBanned() ? -1 : 0;
                        int lockSignOfTagRelation2 = tagRelation2 == null ? 0 : tagRelation2.isLocked() ? 1 : tagRelation2.isBanned() ? -1 : 0;
                        if ((lockSignOfCharTag * lockSignOfRoleTag * lockSignOfTagRelation1) < 0 || (lockSignOfCharTag * lockSignOfRoleTag * lockSignOfTagRelation2) < 0) {
                            return Integer.MIN_VALUE;
                        }
                        if (tagRelation1 != null) {
                            weight = tagRelation1.getterWeight();
                            divider++;
                        }
                        if (tagRelation2 != null) {
                            weight = tagRelation2.getWeight();
                            divider++;
                        }
                        if (divider != 0) {
                            Integer roleHasTagWeight = challengerTag.getValue();
                            Integer characterTagWeight = 0;
                            characterTagWeight = refTagList.get(refTag);
                            int factor = weight / divider;
                            rankTag += (roleHasTagWeight + characterTagWeight) * factor;
                        }
                    }
                }
            }
        }
        return rankTag;
    }

    private int getRoleRank(Character character, Role role, Map<Tag, Boolean> characterLockedBannedTags) {
        Integer rankRelation = 0;
        Integer rankGlobal = 0;
        Integer rankTag = 0;

        Set<RoleHasTag> roleHasTags = role.getterRoleHasTag();
        Map<Tag, Integer> challengerTagList = new HashMap<Tag, Integer>();
        if (roleHasTags != null) {
            for (RoleHasTag rolehasTag : roleHasTags) {
                challengerTagList.put(rolehasTag.getterTag(), rolehasTag.getterWeight());
            }
        }
        HashMap<Pair<com.gnk.substitution.Tag, com.gnk.substitution.Tag>, Integer> dictionnaryTagFirstnameName =
                new HashMap<Pair<com.gnk.substitution.Tag, com.gnk.substitution.Tag>, Integer>();

        rankTag = (new TagService()).getTagsMatching(character.getTags(), challengerTagList,
                characterLockedBannedTags, dictionnaryTagFirstnameName);
        if (rankTag == Integer.MIN_VALUE)
            return Integer.MIN_VALUE;
        for (Map.Entry<Tag, Integer> challengerTag : challengerTagList.entrySet()) {
            /* Ajout d'une grosse heuristique moche qui bave pour renforcer les contraintes de sexe qui peuvent rapidement rendre un perso pas très cohérent.*/
            if (!character.isNeutralGender() && challengerTag.getKey().getName().equals("Femme")) {
                final int rankForSex = challengerTag.getValue() * (100 * (character.isWomen() ? 1 : -1)) * 100;
                rankTag += rankForSex;
                if (rankForSex == -100 * 100 * TagService.LOCKED) {
                    LOG.debug(new StringBuilder("Role ").append(role.getterCode()).append(" has been banned form P")
                            .append(character.getDTDId().toString()).append(" because of sex of the character ").toString());
                    return Integer.MIN_VALUE;
                }
            }
            if (!character.isNeutralGender() && challengerTag.getKey().getName().equals("Homme")) {
                final int rankForSex = challengerTag.getValue() * (100 * (character.isMen() ? 1 : -1)) * 100;
                rankTag += rankForSex;
                if (rankForSex == -100 * 100 * TagService.LOCKED) {
                    LOG.debug(new StringBuilder("Role ").append(role.getterCode()).append(" has been banned form P")
                            .append(character.getDTDId().toString()).append(" because of sex of the character ").toString());
                    return Integer.MIN_VALUE;
                }
            }

            /* !Ajout d'une grosse heuristique moche qui bave pour renforcer les contraintes de sexe qui peuvent rapidement rendre un perso pas très cohérent. */
        }
        Map<RoleHasRelationWithRole, Integer> characterRelations = character.getRelations(false);
        Set<RoleHasRelationWithRole> roleHasRelations = role.getAllRelationsWhichConcernRole(false);
        if (roleHasRelations != null) {
            for (RoleHasRelationWithRole roleHasRelation : roleHasRelations) {
                Character roleReceiver = null;
                for (Character c : gn.getCharacterSet()) {
                    if (c.roleIsSelected(roleHasRelation.getterRole1()) || c.roleIsSelected(roleHasRelation.getterRole2())) {
                        roleReceiver = c;
                        break;
                    }
                }

                for (RoleHasRelationWithRole characterRelation : characterRelations.keySet()) {
                    Character characterReceiver = null;
                    for (Character c : gn.getCharacterSet()) {
                        if (c != character && (c.roleIsSelected(characterRelation.getterRole1()) || c.roleIsSelected(characterRelation.getterRole2()))) {
                            characterReceiver = c;
                            break;
                        }
                    }

                    RoleRelationTypeCompatibility roleRelationTypeCompatibility1 = RoleRelationTypeCompatibility.myFindWhere(roleHasRelation.getterRoleRelationType(), characterRelation.getterRoleRelationType(), false);
                    RoleRelationTypeCompatibility roleRelationTypeCompatibility2 = RoleRelationTypeCompatibility.myFindWhere(characterRelation.getterRoleRelationType(), roleHasRelation.getterRoleRelationType(), false);

                    RoleRelationTypeCompatibility roleRelationTypeCompatibility1SameReceiver = RoleRelationTypeCompatibility.myFindWhere(roleHasRelation.getterRoleRelationType(), characterRelation.getterRoleRelationType(), true);
                    RoleRelationTypeCompatibility roleRelationTypeCompatibility2SameReceiver = RoleRelationTypeCompatibility.myFindWhere(characterRelation.getterRoleRelationType(), roleHasRelation.getterRoleRelationType(), true);

                    int relationCompatibility = 0;
                    boolean sameReceiver = roleReceiver != null && characterReceiver != null && roleReceiver == characterReceiver;
                    int divider = 0;
                    if (sameReceiver) {
                        if (roleRelationTypeCompatibility1SameReceiver != null) {
                            if (roleRelationTypeCompatibility1SameReceiver.isBanned()) {
                                LOG.debug(new StringBuilder("Role ").append(role.getterCode()).append(" has been banned from P").append(character.getDTDId()).append(" because of same receiver 1, role relation ").append(roleHasRelation.getterRoleRelationType().getterName()).append(" and character relation ").append(characterRelation.getterRoleRelationType().getterName()));
                                return Integer.MIN_VALUE;
                            }
                            divider++;
                            relationCompatibility += roleRelationTypeCompatibility1SameReceiver.getterWeight();
                        }
                        if (roleRelationTypeCompatibility2SameReceiver != null) {
                            if (roleRelationTypeCompatibility2SameReceiver.isBanned()) {
                                LOG.debug(new StringBuilder("Role ").append(role.getterCode()).append(" has been banned from P").append(character.getDTDId()).append(" because of same receiver 2, role relation ").append(roleHasRelation.getterRoleRelationType().getterName()).append(" and character relation ").append(characterRelation.getterRoleRelationType().getterName()));
                                return Integer.MIN_VALUE;
                            }
                            divider++;
                            relationCompatibility += roleRelationTypeCompatibility2SameReceiver.getterWeight();
                        }
                    } else {
                        if (roleRelationTypeCompatibility1 != null) {
                            if (roleRelationTypeCompatibility1.isBanned()) {
                                LOG.debug(new StringBuilder("Role ").append(role.getterCode()).append(" has been banned from P").append(character.getDTDId()).append(" because of different receiver 1, role relation ").append(roleHasRelation.getterRoleRelationType().getterName()).append(" and character relation ").append(characterRelation.getterRoleRelationType().getterName()));
                                return Integer.MIN_VALUE;
                            }
                            divider++;
                            relationCompatibility += roleRelationTypeCompatibility1.getterWeight();
                        }
                        if (roleRelationTypeCompatibility2 != null) {
                            if (roleRelationTypeCompatibility2.isBanned()) {
                                LOG.debug(new StringBuilder("Role ").append(role.getterCode()).append(" has been banned from P").append(character.getDTDId()).append(" because of different receiver 1, role relation ").append(roleHasRelation.getterRoleRelationType().getterName()).append(" and character relation ").append(characterRelation.getterRoleRelationType().getterName()));
                                return Integer.MIN_VALUE;
                            }
                            divider++;
                            relationCompatibility += roleRelationTypeCompatibility2.getterWeight();
                        }
                    }
                    if (divider != 0) {
                        relationCompatibility /= divider;
                        rankRelation += (roleHasRelation.getterWeight() + characterRelation.getterWeight()) * relationCompatibility;
                    }
                }
            }
        }


        int characterNbPIP = character.getNbPIP();
        if (characterNbPIP != 0)
            rankTag /= characterNbPIP;

        rankGlobal = rankTag + rankRelation;

        //TODO diversité

        /* Smoothing pour prévilégier les characters à faible nombre de PIP et pénaliser ceux à fort nombre de PIP */

        rankGlobal += (gn.getPipMin() - characterNbPIP) * 10; // Heuristique pour différencier les ranks 0 qui sinon ne seraient pas affectés par l'ajustement dessous;
        int deltaToPIPSmoothing;
        if (characterNbPIP != 0 && gn.getPipMin() != 0)
            deltaToPIPSmoothing = Math.abs(Math.round(((float)rankGlobal)/(((float) (characterNbPIP))/((float) gn.getPipMin())))) - Math.abs(rankGlobal);
        else
            deltaToPIPSmoothing = 0;
        if (deltaToPIPSmoothing > 0 && deltaToPIPSmoothing == rankGlobal * -1) {
            deltaToPIPSmoothing--;
        } else if (deltaToPIPSmoothing < 0 && deltaToPIPSmoothing == rankGlobal * -1)
        {
            deltaToPIPSmoothing++;
        }
        rankGlobal += deltaToPIPSmoothing;
        return rankGlobal;
    }

    private void sexualizeRole(Role role, boolean toMan) {
        String mySex = toMan ? "M" : "F";
        String oppositeSex = toMan ? "F" : "M";

        //Si ce rôle n'as pas encore été attribué alors on le met dans le buffer des sexués non attribués
        if (unAttribuedRoleWithSettedSex.containsKey(role) == false && gnRoleSetToProcess.contains(role)) {
            unAttribuedRoleWithSettedSex.put(role, mySex);
        }

        for (RoleHasRelationWithRole relation : role.getAllRelationsWhichConcernRole(true)) {
            if (relation.getterRoleRelationType().getterSexDiffers()) {
                Role otherRole = relation.getterRole1().equals(role) ? relation.getterRole2() : relation.getterRole1();
                //Si il n'a pas déjà été sexué (mis dans le buffer d'attente ou attribué à un perso ce qui aurait provoqué sa sexuation)
                if (unAttribuedRoleWithSettedSex.containsKey(otherRole) == false && gnRoleSetToProcess.contains(otherRole)) {
                    sexualizeRole(otherRole, !toMan);
                }
            }
        }
    }

    private void sexualizeCharacter(Character c, boolean toMan) {
        if (!c.isNeutralGender())
            return;
        final String gender = toMan ? "M" : "F";
        c.setGender(gender);
        LOG.debug(new StringBuilder("Character ").append(c.getDTDId().toString()).append(" setted to ").append(gender).append(" because of role added."));
        for (Role role : c.getSelectedRoles()) {
            sexualizeRole(role, toMan);
        }
    }

    private void initRoles() {
        LOG.info("\t\t<Roles initialisation>");
        gnRoleSetToProcess = new OrderedRoleSet();
        gnNPCRoleSet = new HashSet<Role>();
        gnTPJRoleSet = new HashSet<Role>();
        gnPJGRoleSet = new HashSet<Role>();
        gnSTFRoleSet = new HashSet<Role>();
        gnPJBRoleSet = new HashSet<Role>()

        assert (gn != null);
        if (gn == null) {
            LOG.error("R2P : initRoles -> gn is null");
            return;
        }
        final Set<Plot> selectedPlotSet = gn.getSelectedPlotSet();
        assert (selectedPlotSet != null);
        if (selectedPlotSet == null) {
            LOG.error("R2P : initRoles -> selectedPlotSet is null");
            return;
        }
        for (Plot plot : selectedPlotSet) {
            final Set<Role> roles = new HashSet<Role>();
            Set<Role> roleSet = plot.getterRoles();
            assert (roleSet != null);
            if (roleSet == null) {
                LOG.error("R2P : initRoles -> roleSet of plot " + plot.getterId().toString() + " is null");
                continue;
            }
            for (Role role : roleSet) {
                if (role.isTPJ()) {
                    gnTPJRoleSet.add(role);
                    LOG.trace("\t\t\trole : " + role.getterCode() + " is TPJ and not added to role set to process");
                } else if (role.isPJG()) {
                    gnPJGRoleSet.add(role);
                    LOG.trace("\t\t\trole : " + role.getterCode() + " is PJG and not added to role set to process");
                } else if (role.isPJ()) {
                    roles.add(role);
                    LOG.trace("\t\t\trole : " + role.getterCode() + " is PJ and added to role set to process");
                } else if (role.isPJB()){
                    gnPJBRoleSet.add(role);
                    LOG.trace("\t\t\trole : " + role.getterCode() + " is PJB and not added to role set to process");
                } else if (role.isSTF()) {
                    gnSTFRoleSet.add(role);
                    LOG.trace("\t\t\trole : " + role.getterCode() + " is STF and not added to role set to process");
                } else {
                    gnNPCRoleSet.add(role);
                    LOG.trace("\t\t\trole : " + role.getterCode() + " is PNJ and not added to role set to process");
                }
            }
            gnRoleSetToProcess.addAll(roles);
        }
        LOG.info("\t\t</Roles initialisation>");
    }

    private void initCharacters() {
        LOG.info("\t\t<Characters initialisation>");
        assert (gn != null);
        if (gn == null) {
            LOG.error("\t\t\tR2P : initCharacters -> gn is null");
            return;
        }
        int nbMen = gn.getNbMen();
        int nbWomen = gn.getNbWomen();
        LOG.trace("\t\t\tThere is " + Integer.toString(nbMen) + " locked men and " + Integer.toString(nbWomen) + " locked women in the gn");
        Set<Character> gnCharacterSet = gn.getterCharacterSet();
        if (gnCharacterSet == null) {
            LOG.info("\t\t\tfirst run of R2P init from scratch");
            gnCharacterSet = new HashSet<Character>();
            gn.setCharacterSet(gnCharacterSet);
            Set<Character> menCharacterSet = new HashSet<Character>();
            Set<Character> womenCharacterSet = new HashSet<Character>();
            LOG.debug("\t\t\t\tCharacters from 1 to " + Integer.toString((nbMen)) + " are locked to man");
            for (int i = 0; i < nbMen; i++) {
                final Character c = new Character(i + 1, "M");
                gn.getCharacterSet().add(c);
                menCharacterSet.add(c);
            }
            LOG.debug("\t\t\t\tCharacters from " + Integer.toString(nbMen + 1) + " to " + Integer.toString((nbMen + nbWomen)) + " are locked to woman");
            for (int i = 0; i < nbWomen; i++) {
                final Character c = new Character(i + nbMen + 1, "F");
                gn.getCharacterSet().add(c);
                womenCharacterSet.add(c);
            }

            for (int i = 0; i < gn.getNbPlayers() - (nbMen + nbWomen); i++) {
                final Character c = new Character(i + nbMen + nbWomen + 1, "N");
                gn.getCharacterSet().add(c);
            }
            LOG.debug("\t\t\tCharacters from " + Integer.toString((nbMen + nbWomen + 1)) + " to " + Integer.toString((gn.getNbPlayers() - (nbMen + nbWomen))) + " have no sex lock");
            int incr = 1;
            for (Role NPCRole : gnNPCRoleSet) {
                final Character c = new Character(incr + gn.getNbPlayers() + 1, "N", NPCRole);
                gn.getterNonPlayerCharSet().add(c);
                incr++;
            }
        } else {
            LOG.info("\t\t\tRerun of R2P : remove all unlocked sex and roles");
            StringBuilder sexLockBecauseOfGnLOG = new StringBuilder("\t\t\tFollowing characters have locked sex because of Gn settings : ");
            Map<Character, Set<Role>> sexuedCharactersWithLockedRoles = new HashMap<Character, Set<Role>>();
            Map<Character, Set<Role>> nonSexuedCharactersWithLockedRoles = new HashMap<Character, Set<Role>>();
            for (Character character : gnCharacterSet) {
                final HashSet<Role> lockedRoles = new HashSet<Role>(character.getLockedRoles());
                if (character.getDTDId() <= nbMen) {
                    character.setGender("M");
                    sexuedCharactersWithLockedRoles.put(character, lockedRoles);
                    sexLockBecauseOfGnLOG.append(character.getDTDId().toString() + "M, ");
                } else if (character.getDTDId() <= nbMen + nbWomen) {
                    character.setGender("F");
                    sexuedCharactersWithLockedRoles.put(character, lockedRoles);
                    sexLockBecauseOfGnLOG.append(character.getDTDId().toString() + "F, ");
                } else {
                    character.setGender("N");
                    nonSexuedCharactersWithLockedRoles.put(character, lockedRoles);
                }
                character.removeSelectedAndLockedRoles();
            }
            LOG.info("\t\t\t<Sexualization of character if implied by locked roles>");
            for (Character character : sexuedCharactersWithLockedRoles.keySet()) {
                for (Role role : sexuedCharactersWithLockedRoles.get(character)) {
                    addRoleAndSexualizeIFN(character, role);
                    character.lockRole(role);
                }
            }
            for (Character character : nonSexuedCharactersWithLockedRoles.keySet()) {
                for (Role role : nonSexuedCharactersWithLockedRoles.get(character)) {
                    addRoleAndSexualizeIFN(character, role);
                    character.lockRole(role);
                }
            }
            LOG.info("\t\t\t</Sexualization of character if implied by locked roles>");
        }
        LOG.info("\t\t</Characters initialisation>");
    }

    /*
        Function to add all roles TPJ to all characters of the plot
     */
    private void addTPJ () {
        for (Role role : gnTPJRoleSet) {
            for (Character character : gn.getCharacterSet()) {
                character.addRole(role);
            }
        }
    }

    private void addPJB() {
        if (!gnPJBRoleSet.empty) {
            boolean empty = true
            for (Plot plot : gn.selectedPlotSet) {
                int nb_pjg = 0
                Set<Character> no_role = new HashSet<Character>()
                for (Role role : plot.getterRoles()) {
                    if (role.isPJG())
                        nb_pjg++
                }
                for (Character character : gn.getterCharacterSet()) {
                    empty = true
                    for (Role role : character.getSelectedRoles()) {
                        if (role.getterPlot().getName().equals(plot.getName()) && !role.isTPJ()) {
                            empty = false;
                        }
                    }
                    if (empty == true)
                        no_role.add(character)
                }
                //On vérifie qu'il y a de la place pour au moins un PJG
                if (no_role.size() > nb_pjg) {

                    Iterator<Role> iterator_role = gnPJBRoleSet.iterator()
                    while (iterator_role.hasNext() && no_role.size() > nb_pjg)
                    {
                        Role role = iterator_role.next()
                        if (role.getterPlot().getName().equals(plot.getName())) {
                            //Tag compatibility check
                            int good_result = 0
                            SortedMap<Integer,Character> list_char = new TreeMap<Integer,Character>()
                            for (Character character1 : no_role)
                            {
                                Map<Tag, Integer> map_tag = character1.getTags()
                                Map<Tag, Integer> map_tag2 = role.getRoleTags()
                                int result = 0
                                if (map_tag2 && map_tag) {
                                    Set<Tag> tags = map_tag.keySet()
                                    Set<Tag> tags_role = map_tag2.keySet()
                                    TagService tagservice = new TagService()

                                    for (Tag tag1 in tags) {
                                        for (Tag tag2 in tags_role) {
                                            int isgood = tagservice.getTagMatching(tag1, 0, tag2, 0) * map_tag.get(tag1) * map_tag2.get(tag2)
                                            result += isgood
                                        }
                                    }
                                }
                                //you can't have 2 value with the same key
                                list_char.keySet().each { key ->
                                    if (result == key)
                                        (result > 0) ? result++ : result--
                                }
                                list_char.put(result,character1)
                            }
                            for (Map.Entry<Integer,Character> entry : list_char.entrySet()){
                                Character character1 = entry.getValue()
                                Integer comp = entry.getKey()
                                boolean done = false
                                if (comp >= 0 && !done)
                                {
                                    if ((character1.getNbPIP() + role.getPIPTotal() <= gn.pipMax) && no_role.contains(character1)) {
                                        character1.addRole(role)
                                        no_role.remove(character1)
                                        iterator_role.remove()
                                        done = true
                                    }
                                }
                            }
                        }
                    }
                }
                no_role.clear()
            }
            int incr = 1

            // Remove the NPC created based on PNJsable
            def list = []
            for (Character car : gn.getterNonPlayerCharSet()){
                Role role = Role.findById(car.selectedRoles[0].getDTDId())
                //TODO Why can the DTDid and role be null Here ?!
                if (role != null && role.type == "PJB")
                    list.add(car)
            }
            for (Character car : list)
                gn.getterNonPlayerCharSet().remove(car)

            for (Role NPCRole : gnPJBRoleSet) {
                NPCRole.setType("PNJ")
                final Character c = new Character(gn.getNbPlayers() +  gn.getterNonPlayerCharSet().size() + 1 + incr, "N", NPCRole);
                boolean add = true
                for (Character car : gn.getterNonPlayerCharSet() + gn.getterCharacterSet())
                {
                    if (car.selectedRoles[0].getDTDId() == NPCRole.id)
                        add = false;
                }
                if (add) {
                    gn.getterNonPlayerCharSet().add(c);
                    incr++
                }
                NPCRole.setType("PJB")
            }
        }
    }
    /*
        Function to add all roles PJG to all characters of the plot
     */
    private void addPJG () {
        ArrayList<Integer> char_id = new ArrayList<Integer>();
        int nb_player = gn.getterCharacterSet().size();
        int nb_pl_plot = 0
        boolean pjg = true
        for (Plot plot : gn.selectedPlotSet)
        {
            int percent_before = 0
            ArrayList<Role> pjg_role = new ArrayList<Role>()
            for (Role role : plot.roles)
            {
                if (role.type.equals("PJG"))
                {
                    percent_before += role.pjgp
                    pjg_role.add(role)
                }
            }
            // The sum of the PJGP must be equal to 100
            if (percent_before < 100 && percent_before > 0)
            {
                for (Role role : pjg_role)
                {
                    role.pjgp = role.pjgp * 100 / percent_before;
                }
            }
            nb_pl_plot = nb_player
            for (Role role :plot.getRoles())
                if (role.type.equals("PJ"))
                    nb_pl_plot--
            for (Role role : pjg_role)
            {
                    int nb_pjg = (nb_pl_plot  * role.pjgp) / 100
                        for (Character character : gn.getCharacterSet())
                        {
                            if (nb_pjg > 0){
                                if (!char_id.contains(character.getDTDId())) {
                                    character.initPlotID();
                                    char_id.add(character.getDTDId())
                                }
                                pjg = true;
                                for (Role role_pers : character.getSelectedRoles())
                                    if (role_pers.getPlot().getName().equals(plot.getName()) && (role_pers.type.equals("PJ") || role_pers.type.equals("PJG") || role_pers.type.equals("PJB")))
                                        pjg = false

                                if (pjg == true) {
                                    character.addRole(role)
                                    if (!character.getplotid_role().contains(plot.getId()))
                                        character.getplotid_role().add(plot.getId())
                                    nb_pjg--
                                }
                            }
                        }

            }
            // i is here to be able to give different role to the remaining characters
            int i = 0
            for (Character character : gn.getCharacterSet())
            {
                if (pjg_role.size() > 0) {
                    if (!char_id.contains(character.getDTDId())) {
                        character.initPlotID();
                        char_id.add(character.getDTDId())
                    }
                    pjg = true;
                    for (Role role_pers : character.getSelectedRoles())
                        if (role_pers.getPlot().getName().equals(plot.getName()) && (role_pers.type.equals("PJ") || role_pers.type.equals("PJG") || role_pers.type.equals("PJB")))
                            pjg = false


                    if (pjg == true) {
                        character.addRole(pjg_role.get(i))
                        if (!character.getplotid_role().contains(plot.getId()))
                            character.getplotid_role().add(plot.getId())
                        pjg = false
                        i++
                        if (pjg_role.size() == i)
                            i = 0;
                    }
                }
            }
            pjg_role.clear();
        }
    }

    private class OrderedRoleSet extends TreeSet<Role> {
        public OrderedRoleSet(OrderedRoleSet gnRoleSet) {
            super(gnRoleSet);
        }

        public OrderedRoleSet() {
            super();
        }

        @Override
        public Comparator<? super Role> comparator() {
            return new Comparator<Role>() {
                @Override
                public int compare(Role o1, Role o2) {
                    int o1PIP = o1.getPIPTotal();
                    int o2PIP = o2.getPIPTotal();
                    if (o1PIP == o2PIP) {
                        return o1.getCode().compareTo(o2.getCode());
                    }
                    return o1PIP - o2PIP;
                }

                @Override
                public boolean equals(Object obj) {
                    return false; //FIXME c'est moche
                }
            };
        }
    }
}
