package org.gnk.publication

import grails.converters.JSON
import org.apache.catalina.filters.SetCharacterEncodingFilter
import org.docx4j.jaxb.Context
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart
import org.docx4j.wml.Br
import org.docx4j.wml.STBrType
import org.docx4j.wml.Tbl
import org.docx4j.wml.Tr
import org.gnk.gn.Gn
import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.gn.GnXMLWriterService
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericPlaceHasTag
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.GenericResourceHasTag
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.PlaceHasTag
import org.gnk.roletoperso.Character
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasPastscene
import org.gnk.roletoperso.RoleHasTag
import org.gnk.selectintrigue.Plot
import org.gnk.selectintrigue.PlotHasTag
import org.gnk.tag.Tag
import org.gnk.tag.TagService

class PublicationController {
    final int COLUMN_NUMBER_PERSO = 8
    final int COLUMN_NUMBER_JOUEUR = 9
    private WordWriter wordWriter
    private GNKDataContainerService gnk
    private Gn gn
    private SubstitutionPublication substitutionPublication

    def index() {}



    // Méthode qui permet de générer les documents et de les télécharger pour l'utilisateur
    def publication = {
        def id = params.gnId as Integer
        Gn getGn = null
        if (!id.equals(null))
            getGn = Gn.get(id)
        if (getGn.equals(null))
        {
            print "Error : GN not found"
            return
        }

        gnk = new GNKDataContainerService()
        gnk.ReadDTD(getGn.dtd)
        gn = gnk.gn

        def folderName = "${request.getSession().getServletContext().getRealPath("/")}word/"
        def folder = new File(folderName)
        if( !folder.exists() ) {
            folder.mkdirs()
        }

        File output = new File(folderName + "${gnk.gn.name.replaceAll(" ", "_").replaceAll("/","_")}_${System.currentTimeMillis()}.docx")

        WordprocessingMLPackage word = createPublication()
        word.save(output)

        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
        response.setHeader("Content-disposition", "filename=${gnk.gn.name.replaceAll(" ", "_").replaceAll("/","_")}_${System.currentTimeMillis()}.docx")
        response.outputStream << output.newInputStream()
    }

    // Méthode principale de la génération des documents
    public WordprocessingMLPackage createPublication() {
        wordWriter = new WordWriter(WordprocessingMLPackage.createPackage(), Context.getWmlObjectFactory())
        def mainPart = wordWriter.wordMLPackage.getMainDocumentPart()
        // Custom styling for the output document
        wordWriter.alterStyleSheet()

        mainPart.addStyledParagraphOfText("Title", gn.name)
        mainPart.addStyledParagraphOfText("Subtitle", createSubTile())

        mainPart.addStyledParagraphOfText("Heading1", "Synthèse pour les organisateurs")

        // HDU-MEF4
        mainPart.addStyledParagraphOfText("Heading2", "Synthèse des pitchs des Intrigues du GN")
        createPitchOrga(mainPart)

        mainPart.addStyledParagraphOfText("Heading2", "Synthèse des personnages du GN")
        createPlayersTable()

        mainPart.addStyledParagraphOfText("Heading2", "Synthèse des Intrigues du GN")
        createPlotTable()

        mainPart.addStyledParagraphOfText("Heading2", "Synthèse de l'événementiel du GN")
        createEventsTable()

        mainPart.addStyledParagraphOfText("Heading2", "Synthèse des lieux du GN")
        createPlaceTable()

        mainPart.addStyledParagraphOfText("Heading2", "Synthèse logistique du GN")
        createResTable()

        mainPart.addStyledParagraphOfText("Heading2", "Liste des Ingames Clues")
        createICTableOrga(mainPart)

        mainPart.addStyledParagraphOfText("Heading1", "Événementiel Détaillé")
        createDetailedEventsTable()
        mainPart.addStyledParagraphOfText("Heading1", "Logistique détaillée")

        mainPart.addStyledParagraphOfText("Heading1", "Implications Personnages par intrigue")
        createCharactersPerPlotTable()

        mainPart.addStyledParagraphOfText("Heading1", "Dossiers Personnages")
        wordWriter.wordMLPackage.getMainDocumentPart().addParagraphOfText("Vous trouverez ci dessous les dossiers personnages à imprimer et à distribuer aux joueurs")
        wordWriter.wordMLPackage.getMainDocumentPart().addParagraphOfText("-----------------------------------------------------------------------------------------")
        createCharactersFiles();

        return wordWriter.wordMLPackage
    }

    def String createSubTile(){
        String subtitle = "Version "+((gn.version<10)?"0."+gn.version:gn.version.toString().subSequence(0,gn.version.toString().size()-1)+"."+gn.version.toString().subSequence(gn.version.toString().size()-1,gn.version.toString().size()))+" créé à "
        subtitle += getPrintableDate(gn.dateCreated)
        subtitle += ((gn.lastUpdated == gn.dateCreated)?"":" et modifié à "+getPrintableDate(gn.lastUpdated))
        return subtitle
    }

    // Création du tableau de synthèse des personnages pour les organisateurs
    def createPlayersTable() {

        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        // HDU-MEF2
        wordWriter.addTableCell(tableRow, "NOM - Prenom")
        wordWriter.addTableCell(tableRow, "Nb PIP Total")
        wordWriter.addTableCell(tableRow, "Type")
        wordWriter.addTableCell(tableRow, "Sexe")
        wordWriter.addTableCell(tableRow, "Age")
        wordWriter.addTableCell(tableRow, "Role(s)")
        wordWriter.addTableCell(tableRow, "Indication(s) personnage")
        //wordWriter.addTableCell(tableRow, "Description")

        table.getContent().add(tableRow);

        HashSet<Character> lchar = new HashSet<Character>()
        for (Character c : gn.characterSet)
            lchar.add(c.lastname + c.firstname)
        def listPJ = lchar.toList().sort()

        //affichage des PJ dans l'ordre aplhabétique (Nom puis prénom)
        for (String cname : listPJ)
        {
            for (Character c : gn.characterSet)
            {
                if ((c.lastname+c.firstname) == cname)
                {
                    Tr tableRowCharacter = wordWriter.factory.createTr()

                    wordWriter.addTableCell(tableRowCharacter, c.lastname.toUpperCase() + " " + c.firstname)
                    wordWriter.addTableCell(tableRowCharacter, c.nbPIP.toString())
                    wordWriter.addTableCell(tableRowCharacter, c.isPJ() ? "PJ" : c.isPNJ()? "PNJ" : "PHJ")

                    wordWriter.addTableCell(tableRowCharacter, c.gender)
                    wordWriter.addTableCell(tableRowCharacter, c.getCharacterAproximateAge().toString())
                    String resRoles = "Aucun Rôle"
                    String resTag = "Aucune indication"
                    for (Role r : c.selectedRoles)
                    {
                        if (resRoles == "Aucun Rôle")
                            resRoles = r.code + " : " + r.description
                        else
                            resRoles += ";" + r.code + " : " +  r.description
                        for (RoleHasTag rht : r.roleHasTags)
                        {
                            if (resTag == "Aucune indication")
                                resTag = rht.tag.name + " (" + rht.weight + "%)"
                            else
                                resTag += ";" + rht.tag.name + " (" + rht.weight + "%)"
                        }
                    }
                    wordWriter.addTableCell(tableRowCharacter, resRoles)
                    wordWriter.addTableCell(tableRowCharacter, resTag)
                    table.getContent().add(tableRowCharacter)
                    break;
                }
            }
        }


        //affichage des PNJ dans l'ordre aplhabétique (Nom puis prénom)
        lchar = new HashSet<Character>()
        for (Character c : gn.nonPlayerCharSet)
        {
            if (c.isPNJ())
                lchar.add(c.lastname + c.firstname)
        }
        def listPNJ = lchar.toList().sort()
        for (String cname : listPNJ)
        {
            for (Character c : gn.nonPlayerCharSet)
            {
                if ((c.lastname+c.firstname) == cname)
                {
                    Tr tableRowCharacter = wordWriter.factory.createTr()

                    wordWriter.addTableCell(tableRowCharacter, c.lastname.toUpperCase() + " " + c.firstname)
                    wordWriter.addTableCell(tableRowCharacter, c.nbPIP.toString())
                    wordWriter.addTableCell(tableRowCharacter, c.isPJ() ? "PJ" : c.isPNJ()? "PNJ" : "PHJ")

                    wordWriter.addTableCell(tableRowCharacter, c.gender)
                    wordWriter.addTableCell(tableRowCharacter, c.getCharacterAproximateAge().toString())
                    String resRoles = "Aucun Rôle"
                    String resTag = "Aucune indication"
                    for (Role r : c.selectedRoles)
                    {
                        if (resRoles == "Aucun Rôle")
                            resRoles = r.code + " : " + r.description
                        else
                            resRoles += ";" + r.code + " : " +  r.description
                        for (RoleHasTag rht : r.roleHasTags)
                        {
                            if (resTag == "Aucune indication")
                                resTag = rht.tag.name + " (" + rht.weight + "%)"
                            else
                                resTag += ";" + rht.tag.name + " (" + rht.weight + "%)"
                        }
                    }
                    wordWriter.addTableCell(tableRowCharacter, resRoles)
                    wordWriter.addTableCell(tableRowCharacter, resTag)
                    table.getContent().add(tableRowCharacter)
                    break;
                }
            }
        }

        //affichage des PHJ dans l'ordre aplhabétique (Nom puis prénom)
        lchar = new HashSet<Character>()
        for (Character c : gn.nonPlayerCharSet)
        {
            if (c.isPHJ())
                lchar.add(c.lastname + c.firstname)
        }
        def listPHJ = lchar.toList().sort()
        for (String cname : listPHJ)
        {
            for (Character c : gn.nonPlayerCharSet)
            {
                if ((c.lastname+c.firstname) == cname)
                {
                    Tr tableRowCharacter = wordWriter.factory.createTr()

                    wordWriter.addTableCell(tableRowCharacter, c.lastname.toUpperCase() + " " + c.firstname)
                    wordWriter.addTableCell(tableRowCharacter, c.nbPIP.toString())
                    wordWriter.addTableCell(tableRowCharacter, c.isPJ() ? "PJ" : c.isPNJ()? "PNJ" : "PHJ")

                    wordWriter.addTableCell(tableRowCharacter, c.gender)
                    wordWriter.addTableCell(tableRowCharacter, c.getCharacterAproximateAge().toString())
                    String resRoles = "Aucun Rôle"
                    String resTag = "Aucune indication"
                    for (Role r : c.selectedRoles)
                    {
                        if (resRoles == "Aucun Rôle")
                            resRoles = r.code + " : " + r.description
                        else
                            resRoles += ";" + r.code + " : " +  r.description
                        for (RoleHasTag rht : r.roleHasTags)
                        {
                            if (resTag == "Aucune indication")
                                resTag = rht.tag.name + " (" + rht.weight + "%)"
                            else
                                resTag += ";" + rht.tag.name + " (" + rht.weight + "%)"
                        }
                    }
                    wordWriter.addTableCell(tableRowCharacter, resRoles)
                    wordWriter.addTableCell(tableRowCharacter, resTag)
                    table.getContent().add(tableRowCharacter)
                    break;
                }
            }
        }

//        for (Character c : gn.characterSet + gn.nonPlayerCharSet)
//        {
//            Tr tableRowCharacter = wordWriter.factory.createTr()
//
//            wordWriter.addTableCell(tableRowCharacter, c.firstname + " " + c.lastname)
//            wordWriter.addTableCell(tableRowCharacter, c.nbPIP.toString())
//            wordWriter.addTableCell(tableRowCharacter, c.isPJ() ? "PJ" : c.isPNJ()? "PNJ" : "PHJ")
//
//            wordWriter.addTableCell(tableRowCharacter, c.gender)
//            //wordWriter.addTableCell(tableRowCharacter, "TODO :  Description of the characters ?")
//            table.getContent().add(tableRowCharacter);
//        }

        wordWriter.addBorders(table)

        wordWriter.wordMLPackage.getMainDocumentPart().addObject(table);
    }

    // Création du tableau de la synthèse des lieux du GN
    def createPlaceTable() {
        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        wordWriter.addTableCell(tableRow, "Nom du lieu")
        wordWriter.addTableCell(tableRow, "Type du lieu")
        wordWriter.addTableCell(tableRow, "Description")
        wordWriter.addTableCell(tableRow, "Indication(s) lieu")

        table.getContent().add(tableRow)

        for (Place p : gnk.placeMap.values())
        {
            Tr tableRowPlace = wordWriter.factory.createTr()
            wordWriter.addTableCell(tableRowPlace, p.name)
            if (p.genericPlace)
                wordWriter.addTableCell(tableRowPlace, p.genericPlace.code)
            else
                wordWriter.addTableCell(tableRowPlace, "[Pas de type de lieu]")
            wordWriter.addTableCell(tableRowPlace, p.description)
            String resTag = ""
            for (PlaceHasTag pht : p.extTags)
            {
                resTag += pht.tag.name + "(" + pht.weight + "%); "
            }
            wordWriter.addTableCell(tableRowPlace, resTag)


            table.getContent().add(tableRowPlace)
        }

        wordWriter.addBorders(table)
        wordWriter.wordMLPackage.getMainDocumentPart().addObject(table)
    }

    // Création du tableau de la synthèse des ressources
    def createResTable() {

        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        wordWriter.addTableCell(tableRow, "Nom de la ressource")
        wordWriter.addTableCell(tableRow, "Type")
        wordWriter.addTableCell(tableRow, "Description")
        wordWriter.addTableCell(tableRow, "Commentaire")
        wordWriter.addTableCell(tableRow, "Indication(s) Ressource")

        table.getContent().add(tableRow);

        for (GenericResource genericResource : gnk.genericResourceMap.values())
        {
            if (!genericResource.isIngameClue())// Si la générique ressource N'EST PAS un ingame clue alors je l'affiche
            {
                Tr tableRowRes = wordWriter.factory.createTr()

                if (genericResource.selectedResource)
                    wordWriter.addTableCell(tableRowRes, genericResource.selectedResource.name)
                else
                    wordWriter.addTableCell(tableRowRes, "Ressource liée à la ressource générique non trouvée")

                wordWriter.addTableCell(tableRowRes, genericResource.code)

                if (genericResource.selectedResource)
                    wordWriter.addTableCell(tableRowRes, genericResource.selectedResource.description)
                else
                    wordWriter.addTableCell(tableRowRes, "Ressource liée à la ressource générique non trouvée")

                wordWriter.addTableCell(tableRowRes, genericResource.comment)

                String resTag = ""
                for (GenericResourceHasTag grht : genericResource.extTags)
                {
                    resTag += grht.tag.name + "(" +grht.weight +"%); "
                }
                wordWriter.addTableCell(tableRowRes, resTag)

                table.getContent().add(tableRowRes);
            }
        }
        wordWriter.addBorders(table)

        wordWriter.wordMLPackage.getMainDocumentPart().addObject(table)
    }

    // Création du tableau détaillé des évènements
    def createDetailedEventsTable()
    {
        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        wordWriter.addTableCell(tableRow, "Date")
        wordWriter.addTableCell(tableRow, "Titre")
        wordWriter.addTableCell(tableRow, "Intrigue concernée")
        wordWriter.addTableCell(tableRow, "Lieu")
        wordWriter.addTableCell(tableRow, "Description")
        wordWriter.addTableCell(tableRow, "Personnages présents")

        table.getContent().add(tableRow);

        for (Plot p : gn.selectedPlotSet)
        {
            for (Event e : p.events)
            {
                Tr tableRowRes = wordWriter.factory.createTr()
                //wordWriter.addTableCell(tableRowRes, e.absoluteHour + "h" + e.absoluteMinute + " le " + e.absoluteDay + "/" + e.absoluteMonth + "/" + e.absoluteYear)
                wordWriter.addTableCell(tableRowRes, "Le " + ((e.absoluteDay < 10)?"0":"") + e.absoluteDay + " à " + ((e.absoluteHour < 10)?"0":"")+ e.absoluteHour + "h" + ((e.absoluteMinute < 10)?"0":"") + e.absoluteMinute)
                wordWriter.addTableCell(tableRowRes, e.name)
                wordWriter.addTableCell(tableRowRes, p.name)
                if (e.genericPlace)
                    if (e.genericPlace.selectedPlace)
                        wordWriter.addTableCell(tableRowRes, e.genericPlace.selectedPlace.name)
                    else
                        wordWriter.addTableCell(tableRowRes, e.genericPlace.code)
                else
                    wordWriter.addTableCell(tableRowRes, "[Lieu générique]")

                substituteEvent(p, e)
                wordWriter.addTableCell(tableRowRes, e.description)

                String charactersAndRessources = ""
                for (Role r : p.roles)
                {
                    for (Character c : gn.characterSet + gn.nonPlayerCharSet)
                    {
                        for (Role r2 : c.selectedRoles)
                        {
                            if (r.getDTDId().equals(r2.getDTDId()))
                                charactersAndRessources += c.firstname + " " + c.lastname + ", "
                        }
                    }
                }
                for (GenericResource gR : e.plot.genericResources)
                {
                    charactersAndRessources += gR.selectedResource.name + ", "
                }
                if (charactersAndRessources)
                    wordWriter.addTableCell(tableRowRes, charactersAndRessources)
                else
                    wordWriter.addTableCell(tableRowRes, " ")
                table.getContent().add(tableRowRes);
            }
        }

        wordWriter.addBorders(table)
        wordWriter.wordMLPackage.getMainDocumentPart().addObject(table)
    }

    // Substitution du titre, de la description, etc pour un évènement donné
    private substituteEvent(Plot p, Event e) {
        HashMap<String, Role> rolesNames = new HashMap<>()
        for (Character c : gn.characterSet + gn.nonPlayerCharSet) {
            for (Role r : c.selectedRoles) {
                if (r.plot.DTDId.equals(p.DTDId))
                    rolesNames.put(c.firstname + " " + c.lastname, r)
            }
        }

        // Gestion des pnjs pour la substitution des noms
        for (Character c : gn.nonPlayerCharSet) {
            for (Role r : c.selectedRoles) {
                if (r.plot.DTDId.equals(p.DTDId))
                    rolesNames.put(c.firstname + " " + c.lastname, r)
            }
        }

        substitutionPublication = new SubstitutionPublication(rolesNames, gnk.placeMap.values().toList(), gnk.genericResourceMap.values().toList())

        e.description = substitutionPublication.replaceAll(e.description)
        e.name = substitutionPublication.replaceAll(e.name)
    }

    // Création de toutes les fiches de personnages
    def createCharactersFiles() {
        for (Character c : gn.characterSet + gn.nonPlayerCharSet)
        {
            Br br = wordWriter.factory.createBr()
            br.setType(STBrType.PAGE)
            wordWriter.wordMLPackage.getMainDocumentPart().addObject(br)
            wordWriter.wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Heading2", c.firstname + " " + c.lastname)

            String typePerso
            if (c.isPJ()) { typePerso = "PJ" }
            else if (c.isPNJ()) { typePerso = "PNJ" }
            else  { typePerso = "PHJ" }
            wordWriter.wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Heading3", "Introduction")
            createPitchTablePerso(typePerso)

            wordWriter.wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Heading3", "Présentation")
            String sex = c.gender.toUpperCase().equals("M") ? "Homme" : "Femme"
            wordWriter.wordMLPackage.getMainDocumentPart().addParagraphOfText("Sexe du personnage : " + sex)
            wordWriter.wordMLPackage.getMainDocumentPart().addParagraphOfText("Type de personnage : " + typePerso )

            //Todo: Ajouter les relations entre les personnages

            wordWriter.wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Heading3", "Mon Histoire")

            // HDU-MEF1
            wordWriter.wordMLPackage.getMainDocumentPart().addParagraphOfText("Je m'appelle " + c.firstname + " " + c.lastname + ".")
            wordWriter.wordMLPackage.getMainDocumentPart().addParagraphOfText("Voici mon histoire :")

            Map<Integer, RoleHasPastscene> roleHasPastsceneList = new TreeMap<>()

            for (Role r : c.getSelectedRoles())
            {
                for (RoleHasPastscene roleHasPastscene : r.roleHasPastscenes)
                {
                    Integer time = roleHasPastscene.pastscene.timingRelative
                    String unit = roleHasPastscene.pastscene.unitTimingRelative

                    if (unit.toLowerCase().startsWith("y") && roleHasPastscene.pastscene.timingRelative <= 1)
                    {
                        time = 365
                    }
                    if (unit.toLowerCase().startsWith("y") && roleHasPastscene.pastscene.timingRelative > 1)
                    {
                        time = 365 * roleHasPastscene.pastscene.timingRelative
                    }
                    if (unit.toLowerCase().startsWith("m"))
                    {
                        time = 30 *  roleHasPastscene.pastscene.timingRelative
                    }
                    if (!time)
                    {
                        time = 1
                    }
                    roleHasPastsceneList.put(time, roleHasPastscene)
                }
            }

            for (Integer i = roleHasPastsceneList.values().size() - 1; i > 0; i--)
            {
                RoleHasPastscene roleHasPastscene = roleHasPastsceneList.values().toArray()[i]
                String unit = roleHasPastscene.pastscene.unitTimingRelative
                if (unit.toLowerCase().startsWith("y") && roleHasPastscene.pastscene.timingRelative <= 1)
                {
                    unit = "an"
                }
                if (unit.toLowerCase().startsWith("y") && roleHasPastscene.pastscene.timingRelative > 1)
                {
                    unit = "années"
                }
                if ((unit.toLowerCase().startsWith("d") || unit.toLowerCase().startsWith("j")) && roleHasPastscene.pastscene.timingRelative <= 1)
                    unit = "jour"
                if ((unit.toLowerCase().startsWith("d") || unit.toLowerCase().startsWith("j")) && roleHasPastscene.pastscene.timingRelative > 1)
                    unit = "jours"
                if (unit.toLowerCase().startsWith("m"))
                {
                    unit = "mois"
                }
                wordWriter.wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Heading4", "Il y a " + roleHasPastscene.pastscene.timingRelative + " " + unit + " : " + roleHasPastscene.pastscene.title)
                wordWriter.wordMLPackage.getMainDocumentPart().addParagraphOfText(roleHasPastscene.description)
            }

            boolean hasTags = false
            for (Role r : c.getSelectedRoles())
            {
                if (r.roleHasTags)
                {
                    hasTags = true
                    break
                }

            }

            if (!hasTags)
                continue

            wordWriter.wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Heading3", "Conseils d'interprétation");
            wordWriter.wordMLPackage.getMainDocumentPart().addParagraphOfText("Ce personnage est : ")
            for (Role r : c.getSelectedRoles())
            {
                for (RoleHasTag roleHasTag : r.roleHasTags)
                {
                    if ((roleHasTag.tag.name.equals("Homme")) || (roleHasTag.tag.name.equals("homme")) || (roleHasTag.tag.name.equals("Femme")) || (roleHasTag.tag.name.equals("femme")))
                        continue
                    String qualificatif = "";
                    if (roleHasTag.weight < 0)
                        qualificatif = "Surtout pas"
                    if (roleHasTag.weight > 0 && roleHasTag.weight <= 29)
                        qualificatif = "Un peu"
                    if (roleHasTag.weight > 29 && roleHasTag.weight <= 59)
                        qualificatif = "Assez"
                    if (roleHasTag.weight > 59 && roleHasTag.weight <= 89)
                        qualificatif = "Vraiment"
                    if (roleHasTag.weight > 89)
                        qualificatif = "Très"
                    wordWriter.wordMLPackage.getMainDocumentPart().addParagraphOfText(qualificatif + " " + roleHasTag.tag.name)
                }
            }
            // todo: Relations wordWriter.wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Heading3", "Mes relations")

            // todo : wordWriter.wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Heading3", "J'ai sur moi...")
        }
    }

    //Créatiion du tableau de synthèse des pitchs des intrigue pour les PJ, PNJ et PHJ
    def createPitchTablePerso(String typePerso) {
        if (typePerso == "PJ")
        {
            for (Plot p : gn.selectedPlotSet)
            {
                wordWriter.wordMLPackage.getMainDocumentPart().addParagraphOfText(p.pitchPj)
            }
        }
        else if (typePerso == "PNJ" || typePerso == "PHJ")
        {
            for (Plot p : gn.selectedPlotSet)
            {
                wordWriter.wordMLPackage.getMainDocumentPart().addParagraphOfText(p.pitchPnj)
            }
        }
    }

    // Création du tableau de synthèse des pitch des intrigues pour les Orga
    def createPitchOrga(def mainPart) {
        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        // HDU-MEF5
        wordWriter.wordMLPackage.getMainDocumentPart().addParagraphOfText("Le GN se déroule dans l'Univers de : " + gn.univers.name.replace("(Univers)","")+".")
        wordWriter.wordMLPackage.getMainDocumentPart().addParagraphOfText("Il débute à " + getPrintableDate(gn.t0Date)  +" et dure " + gn.duration.toString() + " heures.")

        // Comptage PJ
        int NbPJ = gn.characterSet.size()
        String msgCharacters = "Ce GN accueille "+ NbPJ +" Personnage"+((NbPJ > 1)?"s":"")+" Joueur"+((NbPJ > 1)?"s":"")+" (PJ dont "
        String tmpGender = ""
        int nbMale = 0,nbFemale = 0,nbNoGender = 0
        for (int i = 0; i < NbPJ; i++) {
            org.gnk.roletoperso.Character tmpCharacter = gn.characterSet.asList()[i]
            tmpGender = tmpCharacter.type
            if (tmpGender == "PJ")
            {
                tmpGender = tmpCharacter.gender
                if ( tmpGender == "M")
                    nbMale++
                else if (tmpGender == "F")
                    nbFemale++
                else
                    nbNoGender++
            }
        }
        if (nbFemale > 0)
            msgCharacters += nbFemale +" fille"+ ((nbFemale > 1)?"s":"")
        if (nbFemale > 0 && nbMale > 0)
            msgCharacters += " et "
        if (nbMale > 0)
            msgCharacters += nbMale +" garçon"+ ((nbMale > 1)?"s":"")
        msgCharacters += ") et nécessite "

        //Comptage PNJ
        int NbNJ = gn.nonPlayerCharSet.size()
        tmpGender = "";nbMale = 0;nbFemale = 0;nbNoGender = 0
        int NbPNJ =0
        for (int i = 0; i < NbNJ; i++) {
            org.gnk.roletoperso.Character tmpCharacter = gn.nonPlayerCharSet.asList()[i]
            tmpGender = tmpCharacter.type
            if (tmpGender == "PNJ")
            {
                NbPNJ++
                tmpGender = tmpCharacter.gender
                if ( tmpGender == "M")
                    nbMale++;
                else if (tmpGender == "F")
                    nbFemale++;
                else
                    nbNoGender++;
            }
        }

        msgCharacters += NbPNJ+" Personnage"+((NbPNJ > 1)?"s":"")+" Non Joueur"+((NbPNJ > 1)?"s":"")+" (PNJ)  " + ((nbNoGender==NbPNJ)?"":"(")
        if (nbFemale > 0)
            msgCharacters += nbFemale +" fille"+ ((nbFemale > 1)?"s":"")
        if (nbFemale > 0 && nbMale > 0)
            msgCharacters += " et "
        if (nbMale > 0)
            msgCharacters += nbMale +" garçon"+ ((nbMale > 1)?"s":"")
        msgCharacters += ((nbNoGender==NbPNJ)?". ":"). ")

        //Comptage PHJ
        int NbPHJ = 0
        for (int i = 0; i < NbNJ; i++) {
            org.gnk.roletoperso.Character tmpCharacter = gn.nonPlayerCharSet.asList()[i]
            tmpGender = tmpCharacter.type
            if (tmpGender == "PHJ")
            {
                NbPHJ++
                tmpGender = tmpCharacter.gender
                if ( tmpGender == "M")
                    nbMale++;
                else if (tmpGender == "F")
                    nbFemale++;
                else
                    nbNoGender++;
            }
        }
        msgCharacters += "Il mentionne "+NbPHJ+" Personnage"+((NbPHJ > 1)?"s":"")+" Hors jeu (PHJ). (dans ce document, le timing a été calculé pour un jeu commençant à "
        msgCharacters += gn.t0Date.cdate.hours+"h"+(gn.t0Date.cdate.minutes > 10 ? gn.t0Date.cdate.minutes:"0"+gn.t0Date.cdate.minutes)+" le "+gn.t0Date.cdate.dayOfMonth+"/"+(gn.t0Date.cdate.month > 10 ? gn.t0Date.cdate.month:"0"+gn.t0Date.cdate.month)+"/"+gn.t0Date.cdate.year

        wordWriter.wordMLPackage.getMainDocumentPart().addParagraphOfText(msgCharacters)




//        wordWriter.addTableCell(tableRow, "Nom de l'intrigue")
//        wordWriter.addTableCell(tableRow, "Pitch Orga")

//        table.getContent().add(tableRow);

        for (Plot p : gn.selectedPlotSet)
        {
            if (p.pitchOrga != null)
            {
//                Tr tableRowPlot = wordWriter.factory.createTr()
                mainPart.addStyledParagraphOfText("Heading3", p.name)
//                wordWriter.addTableCell(tableRowPlot, p.name)
                wordWriter.wordMLPackage.getMainDocumentPart().addParagraphOfText(p.pitchOrga)
//                wordWriter.addTableCell(tableRowPlot, p.pitchOrga)
//                table.getContent().add(tableRowPlot);
            }

        }
        wordWriter.addBorders(table)
        wordWriter.wordMLPackage.getMainDocumentPart().addObject(table);
    }

    // Création du tableau de synthèse listant tous les ingames clues du GN pour les Orga
    def createICTableOrga(MainDocumentPart mainPart) {


        for (GenericResource genericResource : gnk.genericResourceMap.values())
        {
            // construction du substitutionPublication
            HashMap<String, Role> rolesNames = new HashMap<>()



            for (Character c : gn.characterSet)
            {
                for (Role r : c.selectedRoles)
                {
                    if (r.plot.DTDId.equals(genericResource.plot.DTDId))
                        rolesNames.put(c.firstname + " " + c.lastname, r)
                }
            }
            for (Character c : gn.nonPlayerCharSet)
            {
                for (Role r : c.selectedRoles)
                {
                    if (r.plot.DTDId.equals(genericResource.plot.DTDId))
                        rolesNames.put(c.firstname + " " + c.lastname, r)
                }
            }
            substitutionPublication = new SubstitutionPublication(rolesNames, gnk.placeMap.values().toList(), gnk.genericResourceMap.values().toList())
            // Fin construction du substitutionPublication

            if (genericResource.isIngameClue()) // Si la générique ressource est un ingame clue alors je l'affiche
            {
                genericResource.title = substitutionPublication.replaceAll(genericResource.title)
                genericResource.description = substitutionPublication.replaceAll(genericResource.description)
                mainPart.addStyledParagraphOfText("Heading3", genericResource.code + " - " + genericResource.title)
                mainPart.addStyledParagraphOfText("Heading4", "Descritpion")
                mainPart.addParagraphOfText(/*type*/"" + " - " + genericResource.selectedResource.name + " - Ingame CLue")
                mainPart.addParagraphOfText(/*Nom de l'intrigue*/"")
                mainPart.addParagraphOfText("Détenu au début du jeu par : "+/*role_has_ingame_clue ou ressource*/"")
                mainPart.addParagraphOfText(genericResource.comment)
                mainPart.addStyledParagraphOfText("Heading4", "Contenu")
                mainPart.addParagraphOfText(/*Champs titre*/"")
                mainPart.addParagraphOfText(genericResource.description)


//                genericResource.title = substitutionPublication.replaceAll(genericResource.title)
//                genericResource.description = substitutionPublication.replaceAll(genericResource.description)
//                wordWriter.addTableCell(tableRowPlot, genericResource.selectedResource.name)
//                wordWriter.addTableCell(tableRowPlot, genericResource.code)
//                wordWriter.addTableCell(tableRowPlot, genericResource.comment)
//                wordWriter.addTableCell(tableRowPlot, genericResource.title)
//                wordWriter.addTableCell(tableRowPlot, genericResource.description)
            }
        }
    }

    // Création du tableau de synthèse des intrigues
    def createPlotTable() {
        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        wordWriter.addTableCell(tableRow, "Nom de l'intrigue")

        // HDU-MEF3
        wordWriter.addTableCell(tableRow, "Nb PIP")
        wordWriter.addTableCell(tableRow, "Indication(s) Intrigue")
        wordWriter.addTableCell(tableRow, "Résumé/Description")


        table.getContent().add(tableRow);

        for (Plot p : gn.selectedPlotSet)
        {
            Tr tableRowPlot = wordWriter.factory.createTr()
            wordWriter.addTableCell(tableRowPlot, p.name)
            wordWriter.addTableCell(tableRowPlot, p.getSumPipRoles(gn.getNbPlayers()).toString())

            String tags = ""
            if (p.isEvenemential)
                tags += "Evènementiel"
            if (p.isEvenemential.and(p.isMainstream))
                tags += " - "
            if (p.isMainstream)
                tags += "Mainstream"
            if (p.isEvenemential.or(p.isMainstream))
                tags += " : "
            boolean first = true
            for (PlotHasTag plotHasTag : p.extTags)
            {
                if (!first)
                    tags += ";"
                else
                    first = false
                tags += plotHasTag.tag.name + " (" + plotHasTag.weight + "%) "
            }
            wordWriter.addTableCell(tableRowPlot, tags.toString())

            substituteRolesAndPlotDescription(p)

            String description = new String(p.description.getBytes("UTF-8"), "UTF-8")
            wordWriter.addTableCell(tableRowPlot, description)
            table.getContent().add(tableRowPlot);
        }

        wordWriter.addBorders(table)
        wordWriter.wordMLPackage.getMainDocumentPart().addObject(table);
    }

    // Handles the substitution of each role, object or place for a plot description and each roles inside this plot
    private substituteRolesAndPlotDescription(Plot p)
    {
        HashMap<String, Role> rolesNames = new HashMap<>()
        for (Character c : gn.characterSet)
        {
            for (Role r : c.selectedRoles)
            {
                if (r.plot.DTDId.equals(p.DTDId))
                    rolesNames.put(c.firstname + " " + c.lastname, r)
            }
        }

        // Gestion des pnjs pour la substitution des noms
        for (Character c : gn.nonPlayerCharSet)
        {
            for (Role r : c.selectedRoles)
            {
                if (r.plot.DTDId.equals(p.DTDId))
                    rolesNames.put(c.firstname + " " + c.lastname, r)
            }
        }

        substitutionPublication = new SubstitutionPublication(rolesNames, gnk.placeMap.values().toList(), gnk.genericResourceMap.values().toList())

        for (Role r : p.roles)
        {
            r.description = substitutionPublication.replaceAll(r.description)
            for (RoleHasPastscene rp : r.roleHasPastscenes)
            {
                // Description globale de la scène passée
                if (rp.pastscene.description)
                    rp.pastscene.description = substitutionPublication.replaceAll(rp.pastscene.description)
                // Description personnalisée de la scène passée
                if (rp.description)
                    rp.description = substitutionPublication.replaceAll(rp.description)
                if (rp.title)
                    rp.title = substitutionPublication.replaceAll(rp.title)
                if (rp.pastscene.title)
                    rp.pastscene.title = substitutionPublication.replaceAll(rp.pastscene.title)

            }
        }
        p.description = substitutionPublication.replaceAll(p.description)
        p.pitchPnj = substitutionPublication.replaceAll(p.pitchPnj)
        p.pitchPj = substitutionPublication.replaceAll(p.pitchPj)
        p.pitchOrga = substitutionPublication.replaceAll(p.pitchOrga)
    }

    // Création du tableau de répartition des rôles pour toutes les intrigues
    def createCharactersPerPlotTable() {
        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        wordWriter.addTableCell(tableRow, "Intrigue")
        wordWriter.addTableCell(tableRow, "Personnage impliqué")
        wordWriter.addTableCell(tableRow, "Code rôle")
        wordWriter.addTableCell(tableRow, "Nombre de PIP intrigue")
        wordWriter.addTableCell(tableRow, "Nombre de PIP relations")
        wordWriter.addTableCell(tableRow, "Description du rôle")

        table.getContent().add(tableRow);

        for (Plot p : gn.selectedPlotSet)
        {
            Tr tableRowPlot = wordWriter.factory.createTr()
            wordWriter.addTableCell(tableRowPlot, p.name)

            boolean first = true
            for (Role r : p.roles)
            {
                if (!first) {
                    tableRowPlot = wordWriter.factory.createTr()
                    wordWriter.addTableCell(tableRowPlot, "")
                }

                String characterName = ""
                if (r.isTPJ() == false) {
                    gn.characterSet.each { character ->
                        character.selectedRoles.each { role ->
                            if (role.DTDId == r.DTDId) {
                                if (characterName == "")
                                    characterName += character.firstname + " " + character.lastname
                                else
                                    characterName += ", " + character.firstname + " " + character.lastname
                            }
                        }
                    }

                    gn.nonPlayerCharSet.each { character ->
                        character.selectedRoles.each { role ->
                            if (role.DTDId == r.DTDId) {
                                if (characterName == "")
                                    characterName += character.firstname + " " + character.lastname
                                else
                                    characterName += ", " + character.firstname + " " + character.lastname
                            }
                        }
                    }
                }
                else
                    characterName = "Tous les personnages joués"
                if (characterName.equals(""))
                    print "Erreur : nom du personnage non trouvé"
                wordWriter.addTableCell(tableRowPlot, characterName)
                wordWriter.addTableCell(tableRowPlot, r.code)
                wordWriter.addTableCell(tableRowPlot, r.pipi.toString())
                wordWriter.addTableCell(tableRowPlot, r.pipr.toString())
                wordWriter.addTableCell(tableRowPlot, r.description)

                table.getContent().add(tableRowPlot);
                first = false
            }
        }

        wordWriter.addBorders(table)
        wordWriter.wordMLPackage.getMainDocumentPart().addObject(table);

    }

    // Création du tableau des évènements
    def createEventsTable() {
        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        wordWriter.addTableCell(tableRow, "Timing événementiel")
        wordWriter.addTableCell(tableRow, "Titre")
        wordWriter.addTableCell(tableRow, "Lieu")
        wordWriter.addTableCell(tableRow, "Intrigue concernée")
        wordWriter.addTableCell(tableRow, "Evènement annoncé")
        table.getContent().add(tableRow);

        Map<Integer,Event> events = new TreeMap<Integer, Event>();
        for (Plot p : gn.selectedPlotSet)
        {
            for (Event e : p.events)
            {
                HashMap<String, Role> rolesNames = new HashMap<>()
                for (Character c : gn.characterSet)
                {
                    for (Role r : c.selectedRoles)
                    {
                        if (r.plot.DTDId.equals(e.plot.DTDId))
                            rolesNames.put(c.firstname + " " + c.lastname, r)
                    }
                }

                // Gestion des pnjs pour la substitution des noms
                for (Character c : gn.nonPlayerCharSet)
                {
                    for (Role r : c.selectedRoles)
                    {
                        if (r.plot.DTDId.equals(e.plot.DTDId))
                            rolesNames.put(c.firstname + " " + c.lastname, r)
                    }
                }

                substitutionPublication = new SubstitutionPublication(rolesNames, gnk.placeMap.values().toList(), gnk.genericResourceMap.values().toList())

                if (e.name)
                    e.name = substitutionPublication.replaceAll(e.name)
                events.put(e.timing, e)
            }

        }

        for (Event e : events.values())
        {
            Tr tableRowEvent = wordWriter.factory.createTr()
            //wordWriter.addTableCell(tableRowEvent, e.timing.toString())
            //Todo: Indiquer l'horaire absolu (pour le moment le parser correspondant n'existe pas donc les champs absolus sont nuls
            //wordWriter.addTableCell(tableRowEvent, e.absoluteHour + "h" + e.absoluteMinute + " le " + e.absoluteDay + "/" + e.absoluteMonth + "/" + e.absoluteYear)
            wordWriter.addTableCell(tableRowEvent, "Le " + ((e.absoluteDay < 10)?"0":"") + e.absoluteDay + " à " + ((e.absoluteHour < 10)?"0":"")+ e.absoluteHour + "h" + ((e.absoluteMinute < 10)?"0":"") + e.absoluteMinute)
            wordWriter.addTableCell(tableRowEvent, e.name)
            if (e.genericPlace && e.genericPlace.proposedPlaces && e.genericPlace.proposedPlaces.size() > 0)
                wordWriter.addTableCell(tableRowEvent, e.genericPlace.proposedPlaces[0].name)
            else
                wordWriter.addTableCell(tableRowEvent, "")
            wordWriter.addTableCell(tableRowEvent, e.plot.name)
            wordWriter.addTableCell(tableRowEvent, e.isPlanned ? "Oui" : "Non")
            table.getContent().add(tableRowEvent);
        }

        wordWriter.addBorders(table)
        wordWriter.wordMLPackage.getMainDocumentPart().addObject(table);


    }

    // Methode permettant d'exporter personnage.CSV pour casting
    def publicationCSV = {
        // Récupération de l'ID du GN
        //Integer id =  request.JSON.gnId
        Integer id = params.gnId as Integer
        String csvType = params.csvType as String

        Gn getGn = null
        if (!id.equals(null))
            getGn = Gn.get(id)
        if (getGn.equals(null))
        {
            print "Error : GN not found"
            return
        }
        gnk = new GNKDataContainerService()
        gnk.ReadDTD(getGn.dtd)
        gn = gnk.gn

        int numberQuestionMax = 5;
        // On récupère la liste des personnages joueurs
        Set<Character> realCharacterList = getPJList(gn.characterSet)
        // On regarde combien de fois chaque tag apparait
        HashMap<Integer, Integer> tagOccurence = orderTagByOccurence(realCharacterList)
        // On récupère uniquement la liste des tag qui vont nous servir, les numberQuestionMax plus présent
        ArrayList<Tag> tagListRetainForQuestion = getListOfTagName(tagOccurence, numberQuestionMax)
        // Transforme les values des tags en valeurs comprises entre 0 et 9
        ArrayList<ArrayList<String>> csvFileArrayBadValues = null
        ArrayList<ArrayList<String>> csvFilArrayCorrectValues = null

        if (csvType == "personnage") {
            // On transforme toutes les info en tableau à double entrée crtières x personnages
            csvFileArrayBadValues = getCSVContentWith(tagListRetainForQuestion, realCharacterList, gn)
            csvFilArrayCorrectValues = adaptValuesforCSV(csvFileArrayBadValues)

        } else if (csvType == "joueur") {
            csvFileArrayBadValues = getCSVContentForJoueurWith(tagListRetainForQuestion, realCharacterList, gn)
            csvFilArrayCorrectValues = csvFileArrayBadValues
        }

        // On transforme le tableau en string pour CSV
        String csvContent = getCSVStringFromArray(csvFilArrayCorrectValues)

        String fileName = "${gnk.gn.name.replaceAll(" ", "_").replaceAll("/","_")}_${System.currentTimeMillis()}-${csvType}.csv"

        String path = "${request.getSession().getServletContext().getRealPath("/")}word/${fileName}"
        File output = new File(path)

        output << csvContent


        response.setContentType("text/csv")
        response.setHeader("Content-disposition", "filename=${fileName}")
        response.outputStream << output.newInputStream()

        //output0.eachLine {line->println(gnkXML)}
    }

    private String getRelatedPlayWithChara(Character chara, Gn gn) {
        String ret = ""
        chara.getRelatedCharactersExceptBijectives(gn).each { related ->
            related.value.each { relation ->
                ret = ret.concat(related.key.firstname + " " + related.key.lastname +", ")
            }
        }
        if (ret.length() > 0) {
            ret = ret.substring(0, ret.length() -2)
        }
        return ret
    }

    // Passe les value des tag en valuers comprises entre 0 et 9
    private ArrayList<ArrayList<String>> adaptValuesforCSV(ArrayList<ArrayList<String>> tabOfPlayer) {
        int colAge = COLUMN_NUMBER_PERSO - 1
        // Traitement de la column Age on réduit 0-100 sur 0-9
        for (int j = 1; j < tabOfPlayer.size(); j++) {
            ArrayList<String> currentPla2 = tabOfPlayer.get(j)
            int ageValue = currentPla2.get(colAge).toInteger()
            int newAgeValue = ((ageValue / 100) * 9)
            tabOfPlayer.get(j).set(colAge,/* "("+ageValue+")" +*/(newAgeValue))
        }

        // Traitement des column trait de carac
        for (int i = COLUMN_NUMBER_PERSO; i < tabOfPlayer.get(0).size(); i++) {
            // Pour chaque column on extrait le max et min
            int maxValue = 1
            int minValue = -1
            for (int j = 1; j < tabOfPlayer.size(); j++) {
                ArrayList<String> currentPla = tabOfPlayer.get(j)
                // On extrait du tableau les valeurs min et max
                int value = currentPla.get(i).toInteger()
                if (value > maxValue) {
                    maxValue = value
                } else if (value < minValue) {
                    minValue = value
                }
            }
            // Maitneant qu'on a les max et min, on met en fonction
            for (int j = 1; j < tabOfPlayer.size(); j++) {
                ArrayList<String> currentPla2 = tabOfPlayer.get(j)
                int value2 = currentPla2.get(i).toInteger()
                int newValue = 4
                if (value2 < 0) {
                    newValue = (4-((value2 / minValue) * 4))
                } else if (value2 > 0) {
                    newValue = ((value2 / maxValue) * 4 + 5)
                } else { // value == 0
                    // On laisse 4
                }
                tabOfPlayer.get(j).set(i, /*"("+value2+")" +*/(newValue))
            }
        }
        return tabOfPlayer
    }

    // Converti tableau de joeur avec caractéristique en string pour CSV
    private String getCSVStringFromArray(ArrayList<ArrayList<String>> tabOfPlayer) {
        String ret = ""
        // Converti le tableau en string de CSV
        for (int i = 0; i < tabOfPlayer.size(); i++) {
            ArrayList<String> currentPla = tabOfPlayer.get(i)
            for (int j = 0; j < currentPla.size(); j++) {
                String val = currentPla.get(j)
                ret = ret.concat(val + ";")
            }
            ret = ret.concat("\n")
        }
        return ret
    }

    // Utilise toutes les données pour faire le fichier personnage.csv
    private ArrayList<ArrayList<String>> getCSVContentForJoueurWith(ArrayList<Tag> consideredTag, Set<Character> charList, Gn gn) {
        // On ajoute les colonnes pour chaque
        println("Taille : " + consideredTag.size())

        ArrayList<ArrayList<String>> tabOfPlayer = new ArrayList<>()
        ArrayList<String> topTitle = new ArrayList<String>(Arrays.asList("Prénom", "Nom","Sexe","Session avec","Session sans","Joue avec","Joue sans","Joueur interdit","Age"))
        for (int i = 0; i < consideredTag.size(); i++) {
            topTitle.add(i + COLUMN_NUMBER_JOUEUR, consideredTag.get(i).name)
        }

        tabOfPlayer.add(0, topTitle)

        ArrayList<String> currentPlayer = new ArrayList<>()
        currentPlayer.add(0, "Votre prénom")// Prénom
        currentPlayer.add(1, "Votre nom") // Nom
        currentPlayer.add(2, "M pour Masculin, F pour Féminin et N pour Neutre") // Sexe
        currentPlayer.add(3, "Le format de la liste est le suivant : Prénom1 Nom1, Prénom2 Nom2") // Session avec
        currentPlayer.add(4, "Le format de la liste est le suivant : Prénom1 Nom1, Prénom2 Nom2") // Session sans
        currentPlayer.add(5, "Le format de la liste est le suivant : Prénom1 Nom1, Prénom2 Nom2") // Joue Avec
        currentPlayer.add(6, "Le format de la liste est le suivant : Prénom1 Nom1, Prénom2 Nom2") // Joue sans
        currentPlayer.add(7, "Le format de la liste est le suivant : Prénom1 Nom1, Prénom2 Nom2") // Joueur interdit
        currentPlayer.add(8, "Valeur entre 0 et 9 : 0 = jeune, 4 = adulte, 9 = vieux") // Age
        for (int i = 0; i < consideredTag.size(); i++) {
            currentPlayer.add(i + COLUMN_NUMBER_JOUEUR, "Valeur entre 0 et 9 : 0 = pas du tout, 4 = neutre, 9 = beaucoup")
        }
        tabOfPlayer.add(currentPlayer)


        return tabOfPlayer
    }

    // Utilise toutes les données pour faire le fichier personnage.csv
    private ArrayList<ArrayList<String>> getCSVContentWith(ArrayList<Tag> consideredTag, Set<Character> charList, Gn gn) {
        // On ajoute les colonnes pour chaque
        println("Taille : " + consideredTag.size())

        ArrayList<ArrayList<String>> tabOfPlayer = new ArrayList<>()
        ArrayList<String> topTitle = new ArrayList<String>(Arrays.asList("Prénom", "Nom","Sexe","Joue avec","Joue sans","Joueur prefere","Joueur facultatif","Age"))
        for (int i = 0; i < consideredTag.size(); i++) {
            topTitle.add(i + COLUMN_NUMBER_PERSO, consideredTag.get(i).name)
        }

        tabOfPlayer.add(0, topTitle)

        // Pour chaque personnes, on rempli le tableau
        charList.each { chara ->
            ArrayList<String> currentPlayer = new ArrayList<>()
            currentPlayer.add(0, chara.firstname)// Prénom
            currentPlayer.add(1, chara.lastname) // Nom
            currentPlayer.add(2, chara.gender) // Sexe
            currentPlayer.add(3, getRelatedPlayWithChara(chara, gn)) // Joue avec
            currentPlayer.add(4, "") // Joue sans
            currentPlayer.add(5, "") // Joueur prefere
            currentPlayer.add(6, "") // Joueur  facultatif
            currentPlayer.add(7, "" + chara.getCharacterAproximateAge()) // Age

            boolean foundTag = false
            // Tag spécifiques
            for (int i = 0; i < consideredTag.size(); i++) {
                foundTag = false
                chara.tags.each { charaTag ->
                    if (!foundTag && (consideredTag.get(i).name == charaTag.key.name)) {
                        currentPlayer.add(i + COLUMN_NUMBER_PERSO, charaTag.value)
                        foundTag = true
                    }
                }
                if (!foundTag)
                    currentPlayer.add(i + COLUMN_NUMBER_PERSO, "0")
            }
            tabOfPlayer.add(currentPlayer)
        }

        return tabOfPlayer
    }

    // Utilise la liste des tag et de leur occurence pour ne garder que les plus important
    private ArrayList<Tag> getListOfTagName(HashMap<Integer, Integer> tagOccurence, int numberQuestionMax) {
        ArrayList<Tag> selectedTagList = new ArrayList<>()
        int i = numberQuestionMax;
        tagOccurence.each { hmap ->
            Tag t = Tag.findById(hmap.key)
            i--;
            if (i >= 0) {
                selectedTagList.push(t)
            }
        }
        return selectedTagList
    }

    // Utilise la liste des personnages pour en extraire les tag et les classer par ordre d'importance d'apparition
    private HashMap<Integer, Integer> orderTagByOccurence(Set<Character> charSet){

        HashMap<Integer /*tag id*/, Integer /* occurence */> returnedList = new HashMap<>()
        // Pour chaque personnage
        charSet.each { chara->
            // Pour chacun de ces tags on regarde combien de fois il est déjà apparu dans les personnages
            chara.tags.each { tag ->
                if(tag.key.tagFamily.value == "Trait de personnalité") {
                    if (returnedList.get(tag.key.id) == null) {
                        returnedList.put(tag.key.id, 1)
                    } else {
                        int nb = returnedList.get(tag.key.id).intValue()
                        nb += 1
                        returnedList.remove(tag.key.id)
                        returnedList.put(tag.key.id, new Integer(nb))
                    }
                }
            }
        }
        // Trie la liste par occurence
        returnedList = returnedList.sort {a, b -> b.value <=> a.value}
        // debug display
        returnedList.each { hmap ->
            Tag t = Tag.findById(hmap.key)
            //println(t.name + " ---> " + hmap.value)
        }
        return returnedList
    }

    // Récupère la liste des PJ uniquement
    private Set<Character> getPJList(Set<Character> allPersoList) {
        Set<Character> returnedSet = new HashSet<>()
        allPersoList.each { chara ->
            if (chara.isPJ())
                returnedSet.add(chara)
        }
        return returnedSet
    }

    private String getPrintableDate(Date date)
    {
        if (date != null && date.cdate != null)
            return date.cdate.hours+"h"+(date.cdate.minutes > 10 ? date.cdate.minutes:"0"+date.cdate.minutes)+" le "+(date.cdate.dayOfMonth > 10 ? date.cdate.dayOfMonth:"0"+date.cdate.dayOfMonth)+"/"+(date.cdate.month > 10 ? date.cdate.month:"0"+date.cdate.month)+"/"+date.cdate.year
        else
            return date.hours+"h"+(date.minutes > 10 ? date.minutes:"0"+date.minutes)+" le "+(date.day > 10 ? date.day:"0"+date.day)+"/"+(date.month > 10 ? date.month:"0"+date.month)+"/"+((date.year < 2000)?(date.year+1900).toString():date.year.toString())
    }
}
