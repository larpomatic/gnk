package org.gnk.roletoperso

import com.esotericsoftware.minlog.Log
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import org.gnk.genericevent.GenericEvent
import org.gnk.resplacetime.Pastscene
import org.gnk.selectintrigue.Plot
import org.gnk.gn.Gn
import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.gn.GnXMLWriterService
import org.gnk.tag.TagService
import org.gnk.tag.Univers
import org.gnk.tag.Tag

class RoleToPersoController {

    CharacterService characterService

    def index() {
        redirect(action: "roleToPerso", params: params)
    }

    def list(Integer max) {

        [characters: characterService.characters]
    }

    def roleToPerso() {
        final gnIdStr = params.gnId
        assert (gnIdStr != "null" && (gnIdStr as String).isInteger())
        if (!(gnIdStr != "null" && (gnIdStr as String).isInteger())) {
            redirect(action: "list", params: params)
        }
        Integer gnId = gnIdStr as Integer;

        Gn gn = Gn.get(gnId)
        assert (gn != null)
        if (gn == null) {
            redirect(action: "list", params: params)
        }

        final gnData = new GNKDataContainerService()
        gnData.ReadDTD(gn)
        for (Character character1 : gn.getterCharacterSet()) {
            character1.getSelectedRoles().clear()
            character1.getLockedRoles().clear()
            character1.getBannedRoles().clear()
        }

        int mainstreamId = 0;
        if (gn.getIsMainstream()) {
            if (params.selectedMainstream && params.selectedMainstream != "0") {
                mainstreamId = params.selectedMainstream as int;
                Plot mainstreamPlot = Plot.findById(mainstreamId);
                gn.addPlot(mainstreamPlot);
            }
        }
        int evenementialId = params.selectedEvenemential as int;
        Plot evenementialPlot = Plot.findById(evenementialId);
        gn.addPlot(evenementialPlot);

        params.each {
            final String key = it.key as String
            if (key.startsWith("role_status_") && it.value != "3") {
                // Locked = 1, Banned= 2, Selected = 3
                String[] str = key.split("_")
                assert (str.length > 2)
                if ((str[str.length - 1] as String).isInteger() && (str[str.length - 2] as String).isInteger() && (str[str.length - 3] as String).isInteger()) {
                    Integer roleId = str[str.length - 1] as Integer
                    Integer plotId = str[str.length - 2] as Integer
                    Integer characterId = str[str.length - 3] as Integer
                    for (Character character : gn.getCharacterSet()) {
                        if (character.DTDId == characterId) {
                            Role role = gnData.getRole(roleId, plotId)
                            if (role != null) {
                                if (it.value == "1") {
                                    character.lockRole(role)
                                } else if (it.value == "2") {
                                    character.banRole(role)
                                }
                                break;
                            }
                        }
                    }
                }
            } else if (key.startsWith("keepRoleBanned_") && (it.value.equals("true") || it.value.equals("on"))) {
                String[] str = key.split("_")
                assert (str.length > 2)
                if ((str[str.length - 1] as String).isInteger() && (str[str.length - 2] as String).isInteger() && (str[str.length - 3] as String).isInteger()) {
                    Integer roleId = str[str.length - 1] as Integer
                    Integer plotId = str[str.length - 2] as Integer
                    Integer characterId = str[str.length - 3] as Integer
                    for (Character character : gn.getCharacterSet()) {
                        if (character.DTDId == characterId) {
                            Role role = gnData.getRole(roleId, plotId)
                            if (role != null) {
                                character.banRole(role)
                                break;
                            }
                        }
                    }
                }
            } else if (key.startsWith("lock_on") && it.value != "") {
                String[] str = key.split("_")
                assert (str.length > 2)
                if ((str[str.length - 1] as String).isInteger() && (str[str.length - 2] as String).isInteger() && (str[str.length - 3] as String).isInteger()) {
                    Integer roleId = str[str.length - 1] as Integer
                    Integer plotId = str[str.length - 2] as Integer
                    Integer characterId = str[str.length - 3] as Integer
                    for (Character character : gn.getCharacterSet()) {
                        if (character.DTDId == characterId) {
                            Role role = gnData.getRole(roleId, plotId)
                            if (role != null) {
                                assert ((it.value as String).isInteger())
                                if ((it.value as String).isInteger()) {
                                    int lockedCharacterId = it.value as Integer
                                    for (Character characterLocked : gn.getCharacterSet()) {
                                        if (characterLocked.DTDId == lockedCharacterId) {
                                            characterLocked.lockRole(role)
                                            break
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }



        RoleToPersoProcessing algo = new RoleToPersoProcessing(gn)
        /**********/
        /** AGE  **/
        /**********/

        // On donne à chaque joueur un age selon un algo simple

        gn.characterSet.each { charact ->
            charact.age = charact.getCharacterAproximateAge();
            //print("AGE_0 :" + charact.age)
        }
        gn.nonPlayerCharSet.each { charact ->
            charact.age = charact.getCharacterAproximateAge();
            //print("AGE_0 :" + charact.age)
        }
        Set<Character> charSum = new HashSet<Character>()
        charSum.addAll(gn.characterSet)
        charSum.addAll(gn.nonPlayerCharSet)

        // On affine en fonction des relation père/fils
        boolean noModifDoneOnParents = true;
        while (noModifDoneOnParents) { // Tant qu'on doit faire des ajustements
            noModifDoneOnParents = false
            charSum.each { charact -> // Pour tous les caractère
                charact.getRelationsExceptBijectives().each { related -> // On récupère leurs relation
                    if (related.key.getterRole1().DTDId != null && related.key.getterRole2().DTDId != null) { // Si c'est pas un cas chelou
                        if (related.key.roleRelationType.id == 21) { //Parent direct
                            // On est sur un papa, on va donc aller vérifier qu'il a plus de 20 ans et qu'il a plus de 20 ans que son fils
                            // On recherche son fils
                            if (charact.age < 20) {
                                charact.age = 20
                                if ((gn.characterSet.find {ch -> ch.DTDId == charact.getDTDId()}) != null)
                                    (gn.characterSet.find {ch -> ch.DTDId = charact.getDTDId()}).age = 20
                                if ((gn.nonPlayerCharSet.find {ch -> ch.DTDId == charact.getDTDId()}) != null)
                                    (gn.nonPlayerCharSet.find {ch -> ch.DTDId == charact.getDTDId()}).age = 20
                                noModifDoneOnParents = true
                            } else {
                                charSum.each { sonChar ->
                                    if (sonChar.DTDId == related.key.getterRole2().DTDId) { // On est sur le sonChar qui est le fils de character
                                        if (charact.age < sonChar.age + 20) { // On donne au père au moins 20 ans de plus que le fils
                                            charact.age = sonChar.age + 20
                                            if ((gn.characterSet.find {ch -> ch.DTDId == charact.getDTDId()}) != null)
                                                (gn.characterSet.find {ch -> ch.DTDId == charact.getDTDId()}).age = sonChar.age + 20
                                            if ((gn.nonPlayerCharSet.find {ch -> ch.DTDId == charact.getDTDId()}) != null)
                                                (gn.nonPlayerCharSet.find {ch -> ch.DTDId == charact.getDTDId()}).age = sonChar.age + 20
                                            noModifDoneOnParents = true
                                        }
                                    }
                                }
                            }
                        }
                        if (related.key.roleRelationType.id == 20) { //Filiation
                            // On est sur un fils, on va aller vérifier que son père a au moins 20 ans de plus que lui
                            charSum.each { pereChar ->
                                if (pereChar.DTDId == related.key.getterRole2().DTDId) { // On est sur le pereChar qui est le pere de character
                                    if (pereChar.age < charact.age + 20) { // On donne au père au moins 20 ans de plus que le fils
                                        pereChar.age = charact.age + 20
                                        if ((gn.characterSet.find {ch -> ch.DTDId == charact.getDTDId()}) != null)
                                            (gn.characterSet.find {ch -> ch.DTDId == charact.getDTDId()}).age = charact.age + 20
                                        if ((gn.nonPlayerCharSet.find {ch -> ch.DTDId == charact.getDTDId()}) != null)
                                            (gn.nonPlayerCharSet.find {ch -> ch.DTDId == charact.getDTDId()}).age = charact.age + 20
                                        noModifDoneOnParents = true
                                    }
                                }
                            }
                        }
                        if (related.key.roleRelationType.id == 24) { //Grand père
                            // On est sur un grandpapa, on va donc aller vérifier qu'il a plus de 45 ans et qu'il a plus de 45 ans que son petit fils
                            // On recherche son petit fils
                            if (charact.age < 45) {
                                charact.age = 45
                                if ((gn.characterSet.find {ch -> ch.DTDId == charact.getDTDId()}) != null)
                                    (gn.characterSet.find {ch -> ch.DTDId == charact.getDTDId()}).age = 45
                                if ((gn.nonPlayerCharSet.find {ch -> ch.DTDId == charact.getDTDId()}) != null)
                                    (gn.nonPlayerCharSet.find {ch -> ch.DTDId == charact.getDTDId()}).age = 45
                                noModifDoneOnParents = true
                            } else {
                                charSum.each { grandSonChar ->
                                    if (grandSonChar.DTDId == related.key.getterRole2().DTDId) { // On est sur le sonChar qui est le fils de character
                                        if (charact.age < grandSonChar.age + 45) { // On donne au père au moins 20 ans de plus que le fils
                                            charact.age = grandSonChar.age + 45
                                            if ((gn.characterSet.find {ch -> ch.DTDId == charact.getDTDId()}) != null)
                                                (gn.characterSet.find {ch -> ch.DTDId == charact.getDTDId()}).age = grandSonChar.age + 45
                                            if ((gn.nonPlayerCharSet.find {ch -> ch.DTDId == charact.getDTDId()}) != null)
                                                (gn.nonPlayerCharSet.find {ch -> ch.DTDId == charact.getDTDId()}).age = grandSonChar.age + 45
                                            noModifDoneOnParents = true
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        gn.characterSet.each { charact ->
            //print("AGE_1 :" + charact.age + " for IDs :")
        }
        gn.nonPlayerCharSet.each { charact ->
            //print("AGE_2 :" + charact.age + " for IDs :")
        }

        /***********/
        /**FIN AGE**/
        /***********/

        // Life
        addLifeEvents(gn)


        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.step = "role2perso"
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn)
        if (!gn.save(flush: true)) {
            render(view: "roleToPerso", model: [gnInstance: gn])
            return
        }

        List<String> characterListToDropDownLock = new LinkedList<String>()
        characterListToDropDownLock.add(0, "");
        for (Character c : gn.characterSet) {
            characterListToDropDownLock.add(c.DTDId);
        }

        RelationshipGraphService graph = new RelationshipGraphService();
        String json_relation = graph.create_graph(gn);

        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> values_relation = new ArrayList<>();
        for (Character c in gn.characterSet)
        {
            Map<Tag, Integer> map_tag = c.getTags();
            Set<Tag> tags = map_tag.keySet();
            TagService tagservice = new TagService();
            Tag bad_tag1;
            Tag bad_tag2;
            int result = 0;
            for (Tag tag1 in tags)
            {
                for (Tag tag2 in tags)
                {
                    if (tag1 != tag2)
                    {
                        int isgood = tagservice.getTagMatching(tag1, 0, tag2, 0) * map_tag.get(tag1) * map_tag.get(tag2);
                        if (isgood < result)
                        {
                            result = isgood;
                            bad_tag1 = tag1;
                            bad_tag2 = tag2;
                        }
                    }
                }
            }
            if ((bad_tag1 != null) && (bad_tag2 != null))
            {
                String val = "" + c.getDTDId();
                val += "#" + bad_tag1.name + "#" + map_tag.get(bad_tag1) + "#" +  bad_tag2.name + "#" + map_tag.get(bad_tag2) + "#" + (int)((result/1000000) *100);
                values.add(val);
            }

            Map<Character, List<RoleHasRelationWithRole>> map_relation = c.getCharacterRelations(gn);
            map_relation = map_relation;
            for (List<RoleHasRelationWithRole> relation_list : map_relation.values()) {
                // Test on same character
                for (RoleHasRelationWithRole r1 : relation_list) {
                    if (relation_list.size() > 1) {
                        for (RoleHasRelationWithRole r2 : relation_list) {
                            if (r1 != r2) {
                                RoleRelationTypeCompatibility comp = RoleRelationTypeCompatibility.myFindWhere(r1.getterRoleRelationType(), r2.getterRoleRelationType(), true);
                                if (comp != null) {
                                    int val = comp.getWeight() * r1.getWeight() * r2.getWeight();
                                    Log.info("RelationCompatibility1 : " + r1.getRoleRelationType().getName() + " / " + r2.getterRoleRelationType().getterName() + " : " + val);
                                    if (val < 0)
                                        values_relation.add(c.getDTDId() + "#"
                                                + r1.getterRoleRelationType().getName() + "#"
                                                + r2.getterRoleRelationType().getName() + "#"
                                                + (int)((val/1000000) *100));
                                }
                            }
                        }

                    }
                    for (List<RoleHasRelationWithRole> relation_list2 : map_relation.values())
                    {
                        if (relation_list != relation_list2)
                        {
                            for (RoleHasRelationWithRole r2 : relation_list2)
                            {
                                if (r1 != r2) {
                                    RoleRelationTypeCompatibility comp = RoleRelationTypeCompatibility.myFindWhere(r1.getterRoleRelationType(), r2.getterRoleRelationType(), false);
                                    if (comp != null) {
                                        int val = comp.getWeight() * r1.getWeight() * r2.getWeight();
                                        Log.info("RelationCompatibility2 : " + r1.getRoleRelationType().getName() + " / " + r2.getterRoleRelationType().getterName() + " : " + val);
                                        if (val < 0)
                                        {
                                            values_relation.add(c.getDTDId() + "#"
                                                    + r1.getterRoleRelationType().getName() + "#"
                                                    + r2.getterRoleRelationType().getName() + "#"
                                                    + (int)((val/1000000) *100));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (values.size() == 0)
            values = null;

        if (values_relation.size() == 0)
            values_relation = null;
        else
        {
            Set set = new HashSet() ;
            set.addAll(values_relation) ;
            values_relation = new ArrayList<>(set);
        }
        [gnInstance: gn,
         characterList: gn.characterSet,
         allList: algo.gnTPJRoleSet,
         PHJList: gn.nonPlayerCharSet,
         characterListToDropDownLock: characterListToDropDownLock,
         evenementialId: evenementialId,
         relationjson: json_relation,
         tagcompatibility: values,
         tagrelationcompatibility: values_relation,
         mainstreamId: mainstreamId]
    }

    public void addLifeEvents(Gn gn) {
        for (Character character : gn.getCharacterSet()) {
            // Créer un plot
            int dummyInt = 999897
            Plot p = new Plot()
            p.creationDate = gn.getSelectedPlotSet().first().creationDate
            p.dateCreated = gn.getSelectedPlotSet().first().dateCreated
            p.updatedDate = gn.getSelectedPlotSet().first().updatedDate
            p.user = gn.getSelectedPlotSet().first().user

            p.isDraft = true
            p.isEvenemential = false
            p.isMainstream = false
            p.isPublic = false
            p.description = "Life"
            p.name = p.description
            p.setDTDId(dummyInt + character.getDTDId())
            p.roles = new HashSet<>()
            p.pastescenes = new HashSet<>()

            // Créer un role
            Role roleForLife = new Role()
            roleForLife.type = "PJ"
            roleForLife.pipi = 0
            roleForLife.pipr = 0
            roleForLife.code = "Life"
            roleForLife.description = "Vie du personnage"
            roleForLife.plot = p
            roleForLife.setDTDId(dummyInt + character.getDTDId())
            roleForLife.roleHasPastscenes = new HashSet<>()


            /*
                Si c’est l’âge 0, on choisit un GE qui est proche de l’âge 0.
                        Sinon {
                            On regarde le GE de (âge - 1 itération) et on va regarder quels GE peuvent être engendré depuis ce GE
                            Pour tous les GE qu’il est possible d’engendrer {
                                On choisit le GE qu’on veut. (Voir Etape 2)
                            }
                            Si aucun GE ne peut être engendré alors on choisit le GE en fonction de l’Age
                            (Etape 4.2)
                        }
            }*/

            int INTERVAL = 5
            int age = 1;
            GenericEvent lastGE = null
            while (age < character.age) {
                // Créer Past scene
                //def query = GenericEvent.where {
                //    averageAge in (age-3)..(age+3)
                //}
                def query = GenericEvent.where {
                    ageMin <= age && ageMax >= age
                }
                //def query2 = GenericEvent.where {
                //    averageAge == 0
                //}
                ArrayList<GenericEvent> listEvent = query.findAll()
                Collections.shuffle(listEvent)
                lastGE = listEvent.first()
                //Vérification duplicata
                // On parcours les resultats de la requete
                for (GenericEvent ge : listEvent) {
                    // On parcours ceux qu'on a déjà pour voir si ils existent
                    lastGE = ge
                    boolean exist = false
                    if (p.roles.size() > 0) {
                        for (RoleHasPastscene selectedPastScene : p.roles.first().roleHasPastscenes) {
                            if (selectedPastScene.description == ge.description) {
                                exist = true
                            }
                        }
                    }
                    if (exist == false)
                        break
                }
                if (lastGE == null) {
                    // Debut, on associe un GE
                    //GenericEvent.findByAverageAge(age)

                } else {
                    // On trouve un GE en fonction du GE précédent

                }

                Pastscene pastSceneLife = new Pastscene()
                pastSceneLife.plot = p
                pastSceneLife.title = "Evénement passé à l'age de " + age + " ans"
                pastSceneLife.description = "Past Scene de " + age
                pastSceneLife.isPublic = true
                pastSceneLife.setDTDId((dummyInt +character.getDTDId() * 100)+ age)
                pastSceneLife.unitTimingRelative = "Y"
                pastSceneLife.timingRelative = character.age - age

                // Associer past Scene au rôle, Role Has Past Scene
                RoleHasPastscene rhpsLife = new RoleHasPastscene()
                rhpsLife.description = lastGE.description//listEvent.first().description
                rhpsLife.role = roleForLife
                rhpsLife.pastscene = pastSceneLife
                rhpsLife.title = "Evénement passé"

                roleForLife.roleHasPastscenes.add(rhpsLife)
                p.roles.add(roleForLife)
                p.pastescenes.add(pastSceneLife)

                age += INTERVAL + (((new Random()).nextInt() % 2))
            }
            p.roles.each { ps ->
                ps.roleHasPastscenes.each { rhpsfez ->
                    System.out.print(rhpsfez.description)
                }
            }
            System.out.print("____")
            character.addRole(roleForLife)
            gn.addPlot(p)
        }
    }

    def save() {
        Character c = new Character()

        c.firstname = params.firstname
        c.lastname = params.lastname

        characterService.addCharacter(c)

        redirect(action: "list")
    }

    def deleteCharacter(Long id) {
        print params
        characterService.removeCharacter(params.id as int)
        redirect(action: "list")
    }

    def deleteFromList(Long id) {
        characterService.removeCharacter(params.selectedCharacterId as int)
        redirect(action: "list")
    }

    def merge (String char_to, String char_from, String gn_id) {
        int id_from = Integer.parseInt(char_from);
        int id_to = Integer.parseInt(char_to.split("-")[1]);
        int id_gn = Integer.parseInt(gn_id);

        Character c_from = null;
        Character c_to = null;

        Gn gn = Gn.get(id_gn);
        assert (gn != null)
        final gnData = new GNKDataContainerService()
        gnData.ReadDTD(gn)
        Set<Character> charnotplay = gn.getterNonPlayerCharSet();
        for (Character c in charnotplay)
        {
            if (c.DTDId == id_from)
            {
                c_from = c;
            }
            else if (c.DTDId == id_to)
            {
                c_to = c;
            }
            if ((c_to != null) && (c_from != null))
            {
                break;
            }
        }
        if (c_to.isPNJ() || c_from.isPNJ())
            c_to.type = "PNJ";
        else
            c_to.type = "PHJ";
        c_to.lockedRoles.addAll(c_from.lockedRoles);
        c_to.selectedRoles.addAll(c_from.selectedRoles);
        c_to.selectedPJG.addAll(c_from.selectedPJG);
        c_to.bannedRoles.addAll(c_from.bannedRoles);
        c_to.specificRoles.addAll(c_from.specificRoles);
        c_to.addplotid_role(c_from.getplotid_role());
        charnotplay.remove(c_from);
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn)
        gn.save();
        String new_role = c_to.rolesToString();
        return render(contentType: "application/json") { object(test: id_from, roles: new_role, type: c_to.type)};
    }
}
