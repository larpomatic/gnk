package org.gnk.genericevent

import org.gnk.gn.Gn
import org.gnk.naming.Convention
import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.gn.GnXMLWriterService
import org.gnk.resplacetime.Pastscene
import org.gnk.roletoperso.Character
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasPastscene
import org.gnk.selectintrigue.Plot
import org.gnk.tag.TagService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User

class LifeController {

    def index() {
        redirect(action: "life", params: params)
    }


    def life(Long id) {
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

        //
//    /*Lock manager*/
        for (Character character1 : gn.getterCharacterSet()) {
            character1.getSelectedRoles().clear()
            character1.getLockedRoles().clear()
            character1.getBannedRoles().clear()
            character1.getSpecificRoles().clear()
            character1.getplotid_role().clear();
        }

        //TODO
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.step = "life"
        gn.isLife = true
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn)
        gn.save(flush: true)

        params.each {
            final String key = it.key as String
            if (key.startsWith("Life_") && it.value != "3") {
                // Locked = 1, Banned= 2, Selected = 3
                String[] str = key.split("_")
                assert (str.length > 5)
                    Integer characterId = str[1] as Integer
                    String geTitle = str[4]
                    String geAge = str[5]
                for (Character character : gn.getCharacterSet()) {
                    if (character.DTDId == characterId) {
                        GenericEvent ge = GenericEvent.findByTitle(geTitle)
                        if (ge != null) {
                            int dummyInt = 999897
                            Plot p = new Plot()
                            p.dateCreated = gn.getSelectedPlotSet().first().dateCreated
                            p.lastUpdated = gn.getSelectedPlotSet().first().lastUpdated
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

                            Role role = new Role()
                            role.roleHasPastscenes = new HashSet<>()
                            RoleHasPastscene rhp = new RoleHasPastscene()
                            rhp.title = ge.title
                            rhp.description = geAge
                            role.roleHasPastscenes.add(rhp)
                            role.code = geTitle
                            role.description = geAge
                            role.type = "PJ"
                            role.pipi = 0
                            role.pipr = 0
                            role.code = "Life"

                            p.roles.add(rhp)
                            p.pastescenes.add(rhp)

                            if (it.value == "1") {
                                character.lockRole(role)
                            } else if (it.value == "2") {
                                character.banRole(role)
                            }
                            gn.addPlot(p)
                            break;
                        }
                    }
                }
            }
        }
//        removeLife(gn)

        addLifeEvents(gn)

        //TODO
//        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
//        gn.step = "life"
//        gn.isLife = true
//        gn.dtd = gnXMLWriterService.getGNKDTDString(gn)
//        gn.save(flush: true)

        [gnInstance   : gn,
         characterListLife: gn.characterSet.sort { it.getDTDId() },
         PHJList      : gn.nonPlayerCharSet]
    }

    private void addLifeEvents(Gn gn) {
        for (Character character : gn.getCharacterSet()) {
            addLifeEventToCharacter(character, gn)
        }
    }

    public void addLifeEventToCharacter(Character character, Gn gn) {

        int dummyInt = 999897
        Plot p = new Plot()
        p.dateCreated = gn.getSelectedPlotSet().first().dateCreated
        p.lastUpdated = gn.getSelectedPlotSet().first().lastUpdated
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

        int INTERVAL = 5
        int age = 1;
        GenericEvent lastGE = null
//        System.out.println("AGE : " + character.age)

        //ajouter ceux qui vienne du lock
        for (Role role : character.lockedRoles) {
            if (role.code.toLowerCase() == "life") {
                for (RoleHasPastscene selectedPastScene : role.roleHasPastscenes) {
//                    GenericEvent ge = GenericEvent.findByDescription(selectedPastScene.description.tokenize("\n")[0])
                    GenericEvent ge = GenericEvent.findByTitle(selectedPastScene.title)
                    //TODO age
                    p = addEvent(p, ge, 200, dummyInt, character, roleForLife)
//                private Plot addEvent(Plot p, GenericEvent lastGE, int age, int dummyInt, Character character, Role roleForLife) {

                }
            }
        }

        while (age < character.age) {

            /* STEP 1 on parcours les events qui existent */
            def query = GenericEvent.where {
                ageMin <= age && ageMax >= age
            }

            ArrayList<GenericEvent> listEvent = query.findAll()
            Collections.shuffle(listEvent)
            ArrayList<GenericEvent> listEventCopy = new ArrayList<GenericEvent>(listEvent);

            // On enlève tous les events déjà utilisé
            for (GenericEvent ge : listEventCopy) {
                if (p.roles.size() > 0) {
                    for (RoleHasPastscene selectedPastScene : p.roles.first().roleHasPastscenes) {
                        if (selectedPastScene.title == ge.title) {
                            listEvent.remove(ge)
                        }
                    }
                }

                //supprimer ceux qui viennent du banned
                for (Role role : character.bannedRoles) {
                    if (role.code.toLowerCase() == "life") {
                        for (RoleHasPastscene selectedPastScene : role.roleHasPastscenes)
                            if (selectedPastScene.title == ge.title) {
                                listEvent.remove(ge)
                            }
                    }
                }
            }

            if (listEvent.size() == 0) {
                // La c'est le cas ou on a eu pu trouver aucun event qui n'ai pas déjà été selectionné,
                // du coup on en prend un qu'on a déjà par defaut
                lastGE = listEventCopy.first();
            } else {
                // On choisi parmi les ceux qui n'ont pas été choisi
                GenericEvent bestGE = null;
                ArrayList<Integer> listDesIdDeGEDispo = new ArrayList<Integer>();
                int diffValueBetweenGEandTAG = 300;
                for (GenericEvent ge : listEvent)
                    listDesIdDeGEDispo.add(ge.id)

                for (GenericEvent ge : listEvent) {
                    character.getTags().each { charTag ->
                        def queryTag = GenericEventCanImplyTag.where {
                            tag.id == charTag.key.id
                        }
                        ArrayList<GenericEventCanImplyTag> genericImplyTags = queryTag.findAll()

                        for (GenericEventCanImplyTag gecit : genericImplyTags) {
                            boolean isdispo = false;
                            // Est ce que il est disponible dans la liste
                            for (GenericEvent e : listEvent) {
                                if (e.description == gecit.genericEvent.description) {
                                    isdispo = true
                                    break;
                                }
                            }
                            if (!isdispo) {
                                //System.out.println("OUI CA CONTIENT DEJA")
                            } else {
                                int diff = Math.abs((charTag.value + 100) - (gecit.value + 100));
                                //System.out.println("diff:" + diff + "/" + diffValueBetweenGEandTAG)
                                if (diffValueBetweenGEandTAG > diff) {
                                    // On est sur un GE qui est plus apte que les autres.
                                    System.out.println("L'event (" + gecit.genericEvent.description + ") match")
                                    diffValueBetweenGEandTAG = diff;
                                    bestGE = gecit.genericEvent;
                                }
                            }
                        }
                    }
                }
                if (bestGE != null) {
                    lastGE = bestGE
                } else {
                    lastGE = listEvent.first();
                }
            }

            if (lastGE != null) { // On ajoute l'event
//                p = addEvent(p, lastGE, age, dummyInt, character, roleForLife)
                Pastscene pastSceneLife = new Pastscene()
                pastSceneLife.plot = p
                pastSceneLife.title = lastGE.title
                pastSceneLife.description = lastGE.description + "\nEvénement passé à l'age de " + age + " ans"
                pastSceneLife.isPublic = true
                pastSceneLife.setDTDId((dummyInt + character.getDTDId() * 100) + age)
                pastSceneLife.dateYear = character.age - age
                pastSceneLife.isAbsoluteYear = false

                // Associer past Scene au rôle, Role Has Past Scene
                RoleHasPastscene rhpsLife = new RoleHasPastscene()
                rhpsLife.description = age
                rhpsLife.role = roleForLife
                rhpsLife.pastscene = pastSceneLife
                rhpsLife.title = lastGE.title


                roleForLife.roleHasPastscenes.add(rhpsLife)
                p.roles.add(roleForLife)
                p.pastescenes.add(pastSceneLife)
            }

            age += INTERVAL + (((new Random()).nextInt() % 2))
        }
//        p.roles.each { ps ->
//            ps.roleHasPastscenes.each { rhpsfez ->
//                System.out.print(rhpsfez.pastscene.title + "-" + rhpsfez.description)
//            }
//        }
        System.out.print("____")

        print(roleForLife.roleHasPastscenes.sort{it.description}*.description)

        roleForLife.roleHasPastscenes = roleForLife.roleHasPastscenes.sort{it.description}
        character.addRole(roleForLife)
        gn.addPlot(p)
    }

    private Plot addEvent(Plot p, GenericEvent lastGE, int age, int dummyInt, Character character, Role roleForLife) {
        Pastscene pastSceneLife = new Pastscene()
        pastSceneLife.plot = p
        pastSceneLife.title = lastGE.title
        pastSceneLife.description = lastGE.description + "\nEvénement passé à l'age de " + age + " ans"
        pastSceneLife.isPublic = true
        pastSceneLife.setDTDId((dummyInt + character.getDTDId() * 100) + age)
        pastSceneLife.dateYear = character.age - age
        pastSceneLife.isAbsoluteYear = false

        // Associer past Scene au rôle, Role Has Past Scene
        RoleHasPastscene rhpsLife = new RoleHasPastscene()
        rhpsLife.description = age
        rhpsLife.role = roleForLife
        rhpsLife.pastscene = pastSceneLife
        rhpsLife.title = lastGE.title


        roleForLife.roleHasPastscenes.add(rhpsLife)
        p.roles.add(roleForLife)
        p.pastescenes.add(pastSceneLife)

        return p
    }

    def getBack(Long id) {
        Gn gn = Gn.get(id);
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gn);
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.step = "role2perso";
        gn.isLife = false
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);
        gn.save(flush: true);
        Integer evenementialId = 0;
        Integer mainstreamId = 0;

        removeLife(gn)

        for (Plot plot in gn.selectedPlotSet) {
            if (plot.isEvenemential) {
                evenementialId = Plot.findByName(plot.name).id;
            } else if (plot.isMainstream && gn.isMainstream) {
                mainstreamId = Plot.findByName(plot.name).id; ;
            }
        }
        redirect(controller: 'roleToPerso', action: 'roleToPerso', params: [gnId                : id as String,
                                                                            selectedMainstream  : mainstreamId as String,
                                                                            selectedEvenemential: evenementialId as String]);
    }

//    private void removeLife(Gn gn) {
//
//        def iterator = gn.selectedPlotSet.iterator()
//        while (iterator.hasNext()) {
//            def next = iterator.next()
//
//            if (next.name == "Life") {
//                iterator.remove()
//            }
//        }
//
//        for (character in gn.characterSet) {
//            def specificRoles = character.specificRoles.iterator()
//            while (specificRoles.hasNext()) {
//                def next = specificRoles.next()
//                if (next.code == "Life") {
//                    specificRoles.remove()
//                }
//            }
//            def bannedRoles = character.bannedRoles.iterator()
//            while (bannedRoles.hasNext()) {
//                def next = bannedRoles.next()
//                if (next.code == "Life") {
//                    bannedRoles.remove()
//                }
//            }
//
//            def selectedRoles = character.selectedRoles.iterator()
//            while (selectedRoles.hasNext()) {
//                def next = selectedRoles.next()
//                if (next.code == "Life") {
//                    selectedRoles.remove()
//                }
//            }
//
//            def lockedRoles = character.lockedRoles.iterator()
//            while (lockedRoles.hasNext()) {
//                def next = lockedRoles.next()
//                if (next.code == "Life") {
//                    lockedRoles.remove()
//                }
//            }
//        }
//    }
}
