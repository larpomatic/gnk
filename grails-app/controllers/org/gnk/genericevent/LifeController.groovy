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


    def life(){
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

        removeLife(gn)
        addLifeEvents(gn)

        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.step = "life"
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn)
        gn.save(flush: true)

        [gnInstance: gn,
         characterList: gn.characterSet.sort { it.getDTDId() },
         PHJList: gn.nonPlayerCharSet]
    }

    private void addLifeEvents(Gn gn) {
        for (Character character : gn.getCharacterSet()) {
            addLifeEventToCharacter(character, gn)
        }
    }

    public void addLifeEventToCharacter(Character character, Gn gn) {
//        character.getTags().each { t ->
//            System.out.println("t(" + t.key.id + "):" + t.key + " " + t.value)
//        }
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
                        if (selectedPastScene.description == ge.description) {
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
                Pastscene pastSceneLife = new Pastscene()
                pastSceneLife.plot = p
//                pastSceneLife.title = "Evénement passé à l'age de " + age + " ans"
//                pastSceneLife.description = "Past Scene de " + age
                pastSceneLife.title = lastGE.title
                pastSceneLife.description = lastGE.description + "\nEvénement passé à l'age de " + age + " ans"
                pastSceneLife.isPublic = true
                pastSceneLife.setDTDId((dummyInt + character.getDTDId() * 100) + age)
                // Ca il faut changer
                pastSceneLife.dateYear = character.age - age
                pastSceneLife.isAbsoluteYear = false
                /*
                pastSceneLife.unitTimingRelative = "Y"
                pastSceneLife.timingRelative = character.age - age
                */

                // Associer past Scene au rôle, Role Has Past Scene
                RoleHasPastscene rhpsLife = new RoleHasPastscene()
                rhpsLife.description = age//listEvent.first().description
                rhpsLife.role = roleForLife
                rhpsLife.pastscene = pastSceneLife
//                rhpsLife.title = "Evénement passé"
                rhpsLife.title = lastGE.title


                roleForLife.roleHasPastscenes.add(rhpsLife)
                p.roles.add(roleForLife)
                p.pastescenes.add(pastSceneLife)
            }

            age += INTERVAL + (((new Random()).nextInt() % 2))
        }
//        p.roles.each { ps ->
//            ps.roleHasPastscenes.each { rhpsfez ->
//                System.out.print(rhpsfez.pastscene.description + "-" + rhpsfez.description)
//            }
//        }
        System.out.print("____")
        character.addRole(roleForLife)
        gn.addPlot(p)
    }

    def getBack(Long id) {
        Gn gn = Gn.get(id);
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gn);
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.step = "role2perso";
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
        redirect(controller: 'roleToPerso', action: 'roleToPerso', params: [gnId: id as String,
                                                                            selectedMainstream: mainstreamId as String,
                                                                            selectedEvenemential: evenementialId as String]);
    }

    private void removeLife(Gn gn) {

        def iterator = gn.selectedPlotSet.iterator()
        while (iterator.hasNext()) {
            def next = iterator.next()

            if (next.name == "Life") {
                iterator.remove()
            }
        }

        for (character in gn.characterSet) {
            def specificRoles = character.specificRoles.iterator()
            while (specificRoles.hasNext()) {
                def next = specificRoles.next()
                if (next.code == "Life") {
                    specificRoles.remove()
                }
            }
            def bannedRoles = character.bannedRoles.iterator()
            while (bannedRoles.hasNext()) {
                def next = bannedRoles.next()
                if (next.code == "Life") {
                    bannedRoles.remove()
                }
            }

            def selectedRoles = character.selectedRoles.iterator()
            while (selectedRoles.hasNext()) {
                def next = selectedRoles.next()
                if (next.code == "Life") {
                    selectedRoles.remove()
                }
            }

            def lockedRoles = character.lockedRoles.iterator()
            while (lockedRoles.hasNext()) {
                def next = lockedRoles.next()
                if (next.code == "Life") {
                    lockedRoles.remove()
                }
            }
        }
    }

}
