package org.gnk.publication

import org.apache.commons.codec.binary.Base64
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.wml.Br
import org.docx4j.wml.STBrType
import org.docx4j.wml.Tbl
import org.docx4j.wml.Tr
import org.gnk.gn.Gn
import org.gnk.naming.Firstname
import org.gnk.naming.Name
import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.gn.GnXMLWriterService
import org.gnk.resplacetime.Event
import org.gnk.resplacetime.GenericResource
import org.gnk.resplacetime.GenericResourceHasTag
import org.gnk.resplacetime.Pastscene
import org.gnk.resplacetime.Place
import org.gnk.resplacetime.PlaceHasTag
import org.gnk.resplacetime.Resource
import org.gnk.ressplacetime.GenericPlace
import org.gnk.ressplacetime.ReferentialPlace
import org.gnk.roletoperso.Character
import org.gnk.roletoperso.Graph
import org.gnk.roletoperso.Role
import org.gnk.roletoperso.RoleHasPastscene
import org.gnk.roletoperso.RoleHasTag
import org.gnk.selectintrigue.Plot
import org.gnk.selectintrigue.PlotHasTag
import org.gnk.tag.Tag
import org.hibernate.annotations.GenericGenerator

import javax.validation.constraints.Past
import java.text.DateFormat
import java.text.SimpleDateFormat;

class PublicationController {
    final int COLUMN_NUMBER_PERSO = 8
    final int COLUMN_NUMBER_JOUEUR = 9
    private WordWriter wordWriter
    private GNKDataContainerService gnk
    private Gn gn
    private SubstitutionPublication substitutionPublication
    private String publicationFolder

    def getBack(Long id) {
        Gn gn = Gn.get(id);
        final gnData = new GNKDataContainerService();
        gnData.ReadDTD(gn);
        //gn.step = "substitution";

        /* for (Plot p : gn.getSelectedPlotSet())
         {
             for (GenericPlace gnplace : p.getGenericPlaces())
                 gnplace.setResultList(new ArrayList<ReferentialPlace>());
         }
         gn.setPlaceSet(new HashSet<Place>());*/

        //gn.dtd = gn.dtd.replace("<STEPS last_step_id=\"publication\">", "<STEPS last_step_id=\"substitution\">");
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.dtd = gnXMLWriterService.getGNKDTDString(gn);

        // trouver un moyen de supprimer les places, les ressources et les names
        gn.dtd = gn.dtd.replace("<STEPS last_step_id=\"publication\">", "<STEPS last_step_id=\"substitution\">");
        gn.save(flush: true);
        List<String> sexes = new ArrayList<>();
        for (Character character in gn.characterSet) {
            sexes.add("sexe_" + character.getDTDId() as String);
        }
        for (Character character in gn.nonPlayerCharSet) {
            sexes.add("sexe_" + character.getDTDId() as String);
        }
        redirect(controller: 'substitution', action: 'index', params: [gnId: id as String, sexe: sexes]);
    }

    def index() {
        def id = params.gnId as Integer
//        Gn getGn = null
        if (!id.equals(null))
            gn = Gn.get(id)
        if (gn.equals(null)) {
            print "Error : GN not found"
            return
        }

        gnk = new GNKDataContainerService()
        gnk.ReadDTD(gn);

        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
        gn.step = "publication";
        gn.dtd = gn.dtd.replace("<STEPS last_step_id=\"substitution\">", "<STEPS last_step_id=\"publication\">");
        if (!gn.save(flush: true, failOnError: true)) {

        }

        def folderName = "${request.getSession().getServletContext().getRealPath("/")}word/"
        def folder = new File(folderName)

        if (!folder.exists()) {
            folder.mkdirs()
        }

        return collectPublicationInfo(id)
    }


    public collectPublicationInfo(def id) {
        ArrayList<String> pitchOrgaList = new ArrayList<String>()
        for (Plot p : gn.selectedPlotSet)
            if (p.pitchOrga != null) {
                substituteRolesAndPlotDescription(p)
                pitchOrgaList.add(p.name)
                pitchOrgaList.add(p.pitchOrga)
            }

        ArrayList<String> templateWordList = new ArrayList<String>()
        publicationFolder = "${request.getSession().getServletContext().getRealPath("/")}publication/"
        File folder = new File(publicationFolder);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            templateWordList.add("DEFAULT")
        } else {
            for (int i = 0; i < listOfFiles.length; i++)
                if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".docx"))
                    templateWordList.add(listOfFiles[i].getName().replace(".docx", "").replace(" (Univers)", ""));
        }
        String uniName = gn.univers.name.replace(" (Univers)", "").replace("/", "-")
        Graph globalGraph = new Graph(gn)
        Graph charGraph = new Graph(gn, false)
        ArrayList<String> JsonList = new ArrayList<String>()
        ArrayList<String> JsonCharList = new ArrayList<String>()
        for (org.gnk.roletoperso.Node node : charGraph.nodeList) {
            JsonList.add(charGraph.buildCharGraphJSON(node))
            JsonCharList.add(node.name)
        }
        [
                title: gn.name,
                subtitle: createSubTile(),
                GNinfo1: "Le GN se déroule dans l'Univers de : " + uniName + ".",
                GNinfo2: "Il débute à " + getPrintableDate(gn.date) + " et dure " + gn.duration.toString() + " heures.",
                msgCharacters: PitchOrgaMsgCharacters(),
                pitchOrgaList: pitchOrgaList,
                charactersList: createPlayersList(),
                gnId: id,
                universName: uniName,
                templateWordList: templateWordList,
                globalrelationjson: globalGraph.buildGlobalGraphJSON(),
                relationjsonlist: JsonList,
                jsoncharlist: JsonCharList
        ]
    }

    // Méthode qui permet de générer les documents et de les télécharger pour l'utilisateur
    def publication = {
        ArrayList<String> imgSrcList = new ArrayList<String>()
        imgSrcList = params.get("imgsrc").replace("data:image/png;base64,", "").split(";;")
        ArrayList<String> jsoncharlist = new ArrayList<String>()
        imgSrcList.remove(new String(""))
        if (!imgSrcList.isEmpty()) {
            jsoncharlist.add("GLOBAL")
            jsoncharlist.addAll(params.get("jsoncharlist").toString().replace("[", "").replace("]", "").split(", "))
        }
        def id = params.gnId as Integer
        String tmpWord = params.templateWordSelect as String
        // Gn getGn = null
        if (!id.equals(null))
            gn = Gn.get(id)
        if (gn.equals(null)) {
            print "Error : GN not found"
            return
        }

        gnk = new GNKDataContainerService()
        gnk.ReadDTD(gn)
        // gn = gnk.gn

        for (Place place : gn.placeSet) {
            int lastIndexOf = place.name.lastIndexOf(" -")
            if (lastIndexOf != -1)
                place.name = place.name.substring(0, lastIndexOf)
            else
                place.name = place.name
        }

        def folderName = "${request.getSession().getServletContext().getRealPath("/")}word/"
        def folder = new File(folderName)
        if (!folder.exists()) {
            folder.mkdirs()
        }

        String fileName = folderName + "${gnk.gn.name.replaceAll(" ", "_").replaceAll("/", "_")}_${System.currentTimeMillis()}"
        File output = new File(fileName + ".docx")

        ArrayList<String> imgFileNameList = new ArrayList<String>()
        int i = 0
        for (String imgb64 : imgSrcList) {
            String relationGraphFileName = fileName + "-" + i.toString() + ".png"
            i++
            imgFileNameList.add(relationGraphFileName)
            byte[] data = Base64.decodeBase64(imgb64);
            try {
                OutputStream stream = new FileOutputStream(relationGraphFileName)
                stream.write(data);
                stream.close()
            } catch (Exception e) {
                e.stackTrace();
            }
        }

        publicationFolder = "${request.getSession().getServletContext().getRealPath("/")}publication/"
        wordWriter = new WordWriter(tmpWord, publicationFolder)
        wordWriter.wordMLPackage = createPublication(jsoncharlist, fileName)
        wordWriter.wordMLPackage.save(output)

        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
        response.setHeader("Content-disposition", "filename=${gnk.gn.name.replaceAll(" ", "_").replaceAll("/", "_")}_${System.currentTimeMillis()}.docx")
        response.outputStream << output.newInputStream()
    }

    // Méthode principale de la génération des documents
    public WordprocessingMLPackage createPublication(ArrayList<String> jsoncharlist, String fileName) {
        // Custom styling for the output document

        wordWriter.addStyledParagraphOfText("T", gn.name)
        wordWriter.addStyledParagraphOfText("ST", createSubTile())
        wordWriter.addStyledParagraphOfText("T1", "Dossier Organisateur")
        wordWriter.addStyledParagraphOfText("ST", "Ne lisez ce qui suit QUE si vous faites partie de l'organisation du GN\"\n" +
                "VOUS NE POUVEZ LIRE CE SUIT SI VOUS ÊTES JOUEUR(SE)S")

        wordWriter.addStyledParagraphOfText("T2", "Introduction")
        createPitchOrga()

        wordWriter.addStyledParagraphOfText("T2", "Synthèse des personnages")
        createPlayersTable(jsoncharlist, fileName)

        wordWriter.addStyledParagraphOfText("T2", "Scénario")
        wordWriter.addStyledParagraphOfText("T3", "Synthèse des Intrigues")
        createPlotTable()
        wordWriter.addStyledParagraphOfText("T3", "Implication personnage")
        createPlayersTableImplication()

        wordWriter.addStyledParagraphOfText("T2", "Synthèse des lieux")
        createPlaceTable()

        wordWriter.addStyledParagraphOfText("T2", "Resources Logistiques")
        wordWriter.addStyledParagraphOfText("T3", "Synthèse")
        createResTable()

        // Liste Ingame CLues & détail
        wordWriter.addStyledParagraphOfText("T3", "Indices Textuels")
        createICTableOrga()



        wordWriter.addStyledParagraphOfText("T1", "Implications Personnages par intrigue")
        createCharactersPerPlotTable()

        wordWriter.addStyledParagraphOfText("T2", "Évènementiel")
        wordWriter.addStyledParagraphOfText("T3", "Synthèse des évènements publics")
        createEventsTable()
        wordWriter.addStyledParagraphOfText("T3", "Événementiel Détaillé")
        createDetailedEventsTable()
        wordWriter.addStyledParagraphOfText("T3", "Synthèse des Scènes passées")
        createPastSceneTable()

        createPJFile(jsoncharlist, fileName)
        createPNJFile(jsoncharlist, fileName)
        createPHJFile(jsoncharlist, fileName)
        createStaffFolder()

        return wordWriter.wordMLPackage
    }

    def String createSubTile() {
        String subtitle = "Version " + ((gn.version < 10) ? "0." + gn.version : gn.version.toString().subSequence(0, gn.version.toString().size() - 1) + "." + gn.version.toString().subSequence(gn.version.toString().size() - 1, gn.version.toString().size())) + " créé à "
        subtitle += getPrintableDate(gn.dateCreated)
        subtitle += ((gn.lastUpdated == gn.dateCreated) ? "" : " et modifié à " + getPrintableDate(gn.lastUpdated))
        return subtitle
    }

    // Création du tableau de synthèse des personnages pour les organisateurs
    def createPlayersTable(ArrayList<String> jsoncharlist, String fileName) {

        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        wordWriter.addTableStyledCell("Table1L", tableRow, "NOM - Prenom")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Nb PIP Total")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Type")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Sexe")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Age")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Role(s)")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Indication(s) personnage")

        table.getContent().add(tableRow);

        HashSet<Character> lchar = new HashSet<Character>()
        for (Character c : gn.characterSet)
            lchar.add(c.lastname + c.firstname)
        def listPJ = lchar.toList().sort()

        //affichage des PJ dans l'ordre aplhabétique (Nom puis prénom)
        for (String cname : listPJ) {
            for (Character c : gn.characterSet) {
                if ((c.lastname + c.firstname) == cname) {
                    Tr tableRowCharacter = wordWriter.factory.createTr()

                    wordWriter.addTableStyledCell("Table1C", tableRowCharacter, c.lastname.toUpperCase() + " " + c.firstname)
                    wordWriter.addTableStyledCell("small", tableRowCharacter, c.nbPIP.toString())
                    wordWriter.addTableStyledCell("small", tableRowCharacter, c.isPJ() ? "PJ" : c.isPNJ() ? "PNJ" : "PHJ")

                    wordWriter.addTableStyledCell("small", tableRowCharacter, c.gender)
                    wordWriter.addTableStyledCell("small", tableRowCharacter, c.getAge().toString())
                    String resRoles = "Aucun Rôle"
                    String resTag = "Aucune indication"



                    for (Role r : c.selectedRoles) {
                        substituteRolesAndPlotDescription(r.getterPlot())
                        if (resRoles == "Aucun Rôle")
                            resRoles = "- " + r.code + " : " + r.description
                        else
                            resRoles += "\n- " + r.code + " : " + r.description
                        for (RoleHasTag rht : r.roleHasTags) {
                            if (resTag == "Aucune indication")
                                resTag = "- " + rht.tag.name + " (" + rht.weight + "%)"
                            else
                                resTag += "\n- " + rht.tag.name + " (" + rht.weight + "%)"
                        }
                    }
                    wordWriter.addTableStyledCell("small", tableRowCharacter, resRoles)
                    wordWriter.addTableStyledCell("small", tableRowCharacter, resTag)
                    table.getContent().add(tableRowCharacter)
                    break;
                }
            }
        }

        //affichage des PNJ dans l'ordre aplhabétique (Nom puis prénom)
        lchar = new HashSet<Character>()
        for (Character c : gn.nonPlayerCharSet) {
            if (c.isPNJ())
                lchar.add(c.lastname + c.firstname)
        }
        def listPNJ = lchar.toList().sort()
        for (String cname : listPNJ) {
            for (Character c : gn.nonPlayerCharSet) {
                if ((c.lastname + c.firstname) == cname) {
                    Tr tableRowCharacter = wordWriter.factory.createTr()

                    wordWriter.addTableStyledCell("Table1C", tableRowCharacter, c.lastname.toUpperCase() + " " + c.firstname)
                    wordWriter.addTableStyledCell("small", tableRowCharacter, c.nbPIP.toString())
                    wordWriter.addTableStyledCell("small", tableRowCharacter, c.isPJ() ? "PJ" : c.isPNJ() ? "PNJ" : "PHJ")

                    wordWriter.addTableStyledCell("small", tableRowCharacter, c.gender)
                    wordWriter.addTableStyledCell("small", tableRowCharacter, c.getAge().toString())
                    String resRoles = "Aucun Rôle"
                    String resTag = "Aucune indication"
                    for (Role r : c.selectedRoles) {
                        if (resRoles == "Aucun Rôle")
                            resRoles = r.code + " : " + r.description
                        else
                            resRoles += "; " + r.code + " : " + r.description
                        for (RoleHasTag rht : r.roleHasTags) {
                            if (resTag == "Aucune indication")
                                resTag = rht.tag.name + " (" + rht.weight + "%)"
                            else
                                resTag += "; " + rht.tag.name + " (" + rht.weight + "%)"
                        }
                    }
                    wordWriter.addTableStyledCell("small", tableRowCharacter, resRoles)
                    wordWriter.addTableStyledCell("small", tableRowCharacter, resTag)
                    table.getContent().add(tableRowCharacter)
                    break;
                }
            }
        }

        //affichage des PHJ dans l'ordre aplhabétique (Nom puis prénom)
        lchar = new HashSet<Character>()
        for (Character c : gn.nonPlayerCharSet) {
            if (c.isPHJ())
                lchar.add(c.lastname + c.firstname)
        }
        def listPHJ = lchar.toList().sort()
        for (String cname : listPHJ) {
            for (Character c : gn.nonPlayerCharSet) {
                if ((c.lastname + c.firstname) == cname) {
                    Tr tableRowCharacter = wordWriter.factory.createTr()

                    wordWriter.addTableStyledCell("Table1C", tableRowCharacter, c.lastname.toUpperCase() + " " + c.firstname)
                    wordWriter.addTableStyledCell("small", tableRowCharacter, c.nbPIP.toString())
                    wordWriter.addTableStyledCell("small", tableRowCharacter, c.isPJ() ? "PJ" : c.isPNJ() ? "PNJ" : "PHJ")

                    wordWriter.addTableStyledCell("small", tableRowCharacter, c.gender)
                    wordWriter.addTableStyledCell("small", tableRowCharacter, c.getAge().toString())
                    String resRoles = "Aucun Rôle"
                    String resTag = "Aucune indication"
                    for (Role r : c.selectedRoles) {
                        if (resRoles == "Aucun Rôle")
                            resRoles = r.code + " : " + r.description
                        else
                            resRoles += +r.code + " : " + r.description
                        for (RoleHasTag rht : r.roleHasTags) {
                            if (resTag == "Aucune indication")
                                resTag = rht.tag.name + " (" + rht.weight + "%)"
                            else
                                resTag += "; " + rht.tag.name + " (" + rht.weight + "%)"
                        }
                    }
                    wordWriter.addTableStyledCell("small", tableRowCharacter, resRoles)
                    wordWriter.addTableStyledCell("small", tableRowCharacter, resTag)
                    table.getContent().add(tableRowCharacter)
                    break;
                }
            }
        }
        wordWriter.addBorders(table)
        wordWriter.addObject(table);

        if (!jsoncharlist.isEmpty()) {
            wordWriter.addStyledParagraphOfText("T3", "Synthèse relationnelle des personnages du GN")
            wordWriter.addRelationGraph(jsoncharlist, fileName, "GLOBAL")
        }
    }

    def createPlayersList() {
        ArrayList<Character> resLChar = new ArrayList<Character>()
        HashSet<Character> lchar = new HashSet<Character>()
        for (Character c : gn.characterSet)
            lchar.add(c.lastname + c.firstname)
        def listPJ = lchar.toList().sort()

        //affichage des PJ dans l'ordre aplhabétique (Nom puis prénom)
        for (String cname : listPJ)
            for (Character c : gn.characterSet)
                if ((c.lastname + c.firstname) == cname) {
                    resLChar.add(c)
                    break;
                }
        //affichage des PNJ dans l'ordre aplhabétique (Nom puis prénom)
        lchar = new HashSet<Character>()
        for (Character c : gn.nonPlayerCharSet) {
            if (c.isPNJ())
                lchar.add(c.lastname + c.firstname)
        }
        def listPNJ = lchar.toList().sort()
        for (String cname : listPNJ)
            for (Character c : gn.nonPlayerCharSet)
                if ((c.lastname + c.firstname) == cname) {
                    resLChar.add(c)
                    break;
                }

        //affichage des PHJ dans l'ordre aplhabétique (Nom puis prénom)
        lchar = new HashSet<Character>()
        for (Character c : gn.nonPlayerCharSet) {
            if (c.isPHJ())
                lchar.add(c.lastname + c.firstname)
        }
        def listPHJ = lchar.toList().sort()
        for (String cname : listPHJ) {
            for (Character c : gn.nonPlayerCharSet) {
                if ((c.lastname + c.firstname) == cname) {
                    resLChar.add(c)
                    break;
                }
            }
        }
        return resLChar
    }

    def sortPlaceList(ArrayList<Place> PList) {
        ArrayList<String> nameList = new ArrayList<String>()
        ArrayList<Place> resList = new ArrayList<Place>()

        for (Place p : PList)
            nameList.add(p.name)
        nameList = nameList.sort()
        for (String n : nameList)
            for (Place p : PList)
                if (n == p.name) {
                    resList.add(p)
                    PList.remove(p)
                    break
                }
        return resList
    }

    def sortGenericPlaceObjectTypeList(ArrayList<Place> PList) {
        ArrayList<Place> resList = new ArrayList<Place>()
        for (int i = 0; i <= 3; i++) {
            ArrayList<Place> tmpList = new ArrayList<Place>()
            ArrayList<String> nameList = new ArrayList<String>()
            for (Place p : PList)
                if (p.genericPlace.objectType.id == i)
                    nameList.add(p.name)
            nameList = nameList.sort()
            for (String n : nameList)
                for (Place p : PList)
                    if (n == p.name)
                        tmpList.add(p)
            resList += tmpList
        }
        return resList
    }

    // Création du tableau de la synthèse des lieux du GN
    def createPlaceTable() {
        ArrayList<Place> PList = new ArrayList<Place>() // Liste des place
        ArrayList<Place> GPList = new ArrayList<Place>() // Liste des generic_place
        ArrayList<Place> GPOTList = new ArrayList<Place>() // Liste des generic_place ayant un objectType renseigné
        for (Place p : gnk.placeMap.values()) {
            if (p.genericPlace)
                if (p.genericPlace.objectType != null)
                    GPOTList.add(p)
                else
                    GPList.add(p)
            else
                PList.add(p)
        }

        PList = sortPlaceList(PList)
        GPList = sortPlaceList(GPList)
        GPOTList = sortGenericPlaceObjectTypeList(GPOTList)

        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        wordWriter.addTableStyledCell("Table1L", tableRow, "Nom du lieu")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Type du lieu")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Description")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Indication(s) lieu")

        table.getContent().add(tableRow)
        for (Place p : GPOTList + GPList + PList) {
            Tr tableRowPlace = wordWriter.factory.createTr()
            int lastIndexOf = p.name.lastIndexOf(" -")
            if (lastIndexOf != -1)
                wordWriter.addTableStyledCell("Table1C", tableRowPlace, p.name.substring(0, lastIndexOf))
            else
                wordWriter.addTableStyledCell("Table1C", tableRowPlace, p.name)
            if (p.genericPlace) {
                String typeStr = p.genericPlace.code
                if (p.genericPlace.objectType != null)
                    typeStr += " (" + p.genericPlace.objectType.type + ")"
                wordWriter.addTableStyledCell("small", tableRowPlace, typeStr)
            } else
                wordWriter.addTableStyledCell("small", tableRowPlace, "[Pas de type de lieu]")
            wordWriter.addTableStyledCell("small", tableRowPlace, p.description)
            String resTag = ""
            for (PlaceHasTag pht : p.extTags) {
                resTag += pht.tag.name + "(" + pht.weight + "%); "
            }
            wordWriter.addTableStyledCell("small", tableRowPlace, resTag)


            table.getContent().add(tableRowPlace)
        }

        wordWriter.addBorders(table)
        wordWriter.addObject(table)
    }

    def sortGenericResourceObjectTypeList(ArrayList<GenericResource> PList) {
        ArrayList<GenericResource> resList = new ArrayList<GenericResource>()
        for (Integer i = 0; i <= 3; i++) {
            ArrayList<GenericResource> tmpList = new ArrayList<GenericResource>()
            ArrayList<String> nameList = new ArrayList<String>()
            for (GenericResource gr : PList) {
                if (gr.objectType.id == i && gr.selectedResource != null) {
                    nameList.add(gr.selectedResource.name)
                }
            }
            nameList = nameList.sort()
            for (String n : nameList)
                for (GenericResource gr : PList)
                    if (n == gr.selectedResource.name) {
                        tmpList.add(gr)
                        PList.remove(gr)
                        break
                    }
            resList += tmpList
        }
        return resList
    }

    def sortGenericResourceList(ArrayList<GenericResource> GRList) {
        ArrayList<String> nameList = new ArrayList<String>()
        ArrayList<GenericResource> resList = new ArrayList<GenericResource>()

        for (GenericResource gr : GRList)
            nameList.add(gr.selectedResource.name)
        nameList = nameList.sort()
        for (String n : nameList)
            for (GenericResource gr : GRList)
                if (n == gr.selectedResource.name) {
                    resList.add(gr)
                    GRList.remove(gr)
                    break
                }
        return resList
    }

    // Création du tableau de la synthèse des ressources
    def createResTable() {
        ArrayList<GenericResource> GRList = new ArrayList<GenericResource>() // Liste des GenericResource
        ArrayList<GenericResource> GROTList = new ArrayList<GenericResource>()
        // Liste des generic_GenericResource ayant un objectType renseigné
        for (GenericResource gr : gnk.genericResourceMap.values()) {
            if (gr.objectType != null)
                GROTList.add(gr)
            else
                GRList.add(gr)
        }

        GRList = sortGenericResourceList(GRList)
        GROTList = sortGenericResourceObjectTypeList(GROTList)

        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        wordWriter.addTableStyledCell("Table1L", tableRow, "Nom de la ressource")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Type")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Descriptions")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Indication(s) Ressource")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Détenu par")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Indices textuel")

        table.getContent().add(tableRow);

        for (GenericResource genericResource : GROTList + GRList) {
            Tr tableRowRes = wordWriter.factory.createTr()

            if (genericResource.selectedResource)
                wordWriter.addTableStyledCell("Table1C", tableRowRes, genericResource.selectedResource.name)
            else
                wordWriter.addTableStyledCell("Table1C", tableRowRes, "Ressource liée à la ressource générique non trouvée")

            String typeStr = genericResource.code
            if (genericResource.objectType != null)
                typeStr += " (" + genericResource.objectType.type + ")"
            wordWriter.addTableStyledCell("small", tableRowRes, typeStr)

            String resDescritpion = "Ressource liée à la ressource générique non trouvée\n"
            if (genericResource.selectedResource)
                resDescritpion = (genericResource.selectedResource.description.isEmpty() ? "" : genericResource.selectedResource.description + "\n")
            wordWriter.addTableStyledCell("small", tableRowRes, genericResource.comment + resDescritpion)

            String resTag = ""
            for (GenericResourceHasTag grht : genericResource.extTags) {
                resTag += grht.tag.name + "(" + grht.weight + "%); "
            }
            wordWriter.addTableStyledCell("small", tableRowRes, resTag)
            String possessedByCharacters = ""
            if (genericResource.getPossessedByRole() != null) {
                for (Character c : gn.characterSet + gn.nonPlayerCharSet + gn.staffCharSet) {
                    for (Role r : c.selectedRoles) {
                        if (r.getDTDId() == genericResource.getPossessedByRole().getDTDId()) {
                            possessedByCharacters = (possessedByCharacters.isEmpty() ? "" : ", ") c.firstname + " " + c.lastname.toUpperCase()
                        }
                    }
                }
            } else {
                possessedByCharacters = "personne"
            }
            wordWriter.addTableStyledCell("small", tableRowRes, possessedByCharacters)
            wordWriter.addTableStyledCell("small", tableRowRes, (genericResource.isIngameClue() ? "oui" : ""))
            table.getContent().add(tableRowRes);
        }
        wordWriter.addBorders(table)

        wordWriter.addObject(table)
    }

    // Création du tableau Synthèse des personnages du GN des évènements
    def createDetailedEventsTable() {
        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        wordWriter.addTableStyledCell("Table1L", tableRow, "Date")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Titre")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Intrigue concernée")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Lieu")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Description")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Personnages et objets présents")

        table.getContent().add(tableRow);

        for (Plot p : gn.selectedPlotSet) {
            for (Event e : p.events) {
                Tr tableRowRes = wordWriter.factory.createTr()
                //wordWriter.addTableStyledCell("small",tableRowRes, e.absoluteHour + "h" + e.absoluteMinute + " le " + e.absoluteDay + "/" + e.absoluteMonth + "/" + e.absoluteYear)
                wordWriter.addTableStyledCell("Table1C", tableRowRes, "Le " + ((e.absoluteDay < 10) ? "0" : "") + e.absoluteDay + " à " + ((e.absoluteHour < 10) ? "0" : "") + e.absoluteHour + "h" + ((e.absoluteMinute < 10) ? "0" : "") + e.absoluteMinute)
                wordWriter.addTableStyledCell("small", tableRowRes, e.name)
                wordWriter.addTableStyledCell("small", tableRowRes, p.name)
                if (e.genericPlace)
                    if (e.genericPlace.selectedPlace) {
                        int lastIndexOf = e.genericPlace.selectedPlace.name.lastIndexOf(" -")
                        if (lastIndexOf != -1)
                            wordWriter.addTableStyledCell("small", tableRowRes, e.genericPlace.selectedPlace.name.substring(0, lastIndexOf))
                        else
                            wordWriter.addTableStyledCell("small", tableRowRes, e.genericPlace.selectedPlace.name)
                    } else
                        wordWriter.addTableStyledCell("small", tableRowRes, e.genericPlace.code)
                else
                    wordWriter.addTableStyledCell("small", tableRowRes, "[Lieu générique]")

                substituteEvent(p, e)
                wordWriter.addTableStyledCell("small", tableRowRes, e.description)

                String charactersAndRessources = ""
                for (Role r : p.roles) {
                    for (Character c : gn.characterSet + gn.nonPlayerCharSet) {
                        for (Role r2 : c.selectedRoles) {
                            if (r.getDTDId().equals(r2.getDTDId()))
                                charactersAndRessources += c.firstname + " " + c.lastname + ", "
                        }
                    }
                }
                for (GenericResource gR : e.plot.genericResources) {
                    charactersAndRessources += gR.selectedResource.name + ", "
                }
                if (charactersAndRessources)
                    wordWriter.addTableStyledCell("small", tableRowRes, charactersAndRessources)
                else
                    wordWriter.addTableStyledCell("small", tableRowRes, " ")
                table.getContent().add(tableRowRes);
            }
        }

        wordWriter.addBorders(table)
        wordWriter.addObject(table)
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

    //Génère le dossier PJ
    private createPJFile(ArrayList<String> jsoncharlist = [], String fileName = null) {
        HashSet<String> lchar = new HashSet<Character>()
        for (Character c : gn.characterSet)
            lchar.add(c.lastname + c.firstname)
        ArrayList<String> lcharSorted = lchar.toList().sort()
        ArrayList<Character> listPJ = new ArrayList<Character>()
        for (String cname : lcharSorted)
            for (Character c2 : gn.characterSet)
                if (cname == (c2.lastname + c2.firstname))
                    listPJ.add(c2)

        Br br = wordWriter.factory.createBr()
        br.setType(STBrType.PAGE)
        wordWriter.addObject(br)
        wordWriter.addStyledParagraphOfText("T1", "Dossier Personnage Joueurs")
        wordWriter.addParagraphOfText("Il y a " + listPJ.size() + " Personnages Joueurs(PJ) dans ce GN dont voici la liste : ")
        String resListPJ = ""
        for (Character c : listPJ)
            resListPJ += c.lastname.toUpperCase() + " " + c.firstname + "\n"
        wordWriter.addParagraphOfText(resListPJ)
        wordWriter.addParagraphOfText("Vous trouverez ci-dessous les dossiers Personnages joueurs, triés par ordre Alphabétique, à distribuer aux joueurs")
        wordWriter.addParagraphOfText("------------------------------------------------------------------------------------------------")
        createCharactersFile(listPJ, jsoncharlist, fileName)
    }

    //Génère le dossier PNJ
    private createPNJFile(ArrayList<String> jsoncharlist = [], String fileName = null) {
        HashSet<String> lchar = new HashSet<Character>()
        for (Character c : gn.nonPlayerCharSet)
            lchar.add(c.lastname + c.firstname)
        ArrayList<String> lcharSorted = lchar.toList().sort()
        ArrayList<Character> listPNJ = new ArrayList<Character>()
        for (String cname : lcharSorted)
            for (Character c2 : gn.nonPlayerCharSet)
                if (cname == (c2.lastname + c2.firstname) && c2.isPNJ())
                    listPNJ.add(c2)

        Br br = wordWriter.factory.createBr()
        br.setType(STBrType.PAGE)
        wordWriter.addObject(br)
        wordWriter.addStyledParagraphOfText("T1", "Dossier Personnage Non-Joueurs")
        wordWriter.addParagraphOfText("Il y a " + listPNJ.size() + " Personnages Non-Joueurs(PNJ) dans ce GN dont voici la liste : ")
        String resListPNJ = ""
        for (Character c : listPNJ)
            resListPNJ += c.lastname.toUpperCase() + " " + c.firstname + "\n"
        wordWriter.addParagraphOfText(resListPNJ)
        wordWriter.addParagraphOfText("Vous trouverez ci-dessous les dossiers Personnages non-joueurs, triés par ordre Alphabétique, à distribuer aux joueurs")
        wordWriter.addParagraphOfText("------------------------------------------------------------------------------------------------")
        createCharactersFile(listPNJ, jsoncharlist, fileName)
    }

    //Génère le dossier PHJ
    private createPHJFile(ArrayList<String> jsoncharlist = [], String fileName = null) {
        HashSet<String> lchar = new HashSet<Character>()
        for (Character c : gn.nonPlayerCharSet)
            lchar.add(c.lastname + c.firstname)
        ArrayList<String> lcharSorted = lchar.toList().sort()
        ArrayList<Character> listPHJ = new ArrayList<Character>()
        for (String cname : lcharSorted)
            for (Character c2 : gn.nonPlayerCharSet)
                if (cname == (c2.lastname + c2.firstname) && c2.isPHJ())
                    listPHJ.add(c2)

        Br br = wordWriter.factory.createBr()
        br.setType(STBrType.PAGE)
        wordWriter.addObject(br)
        wordWriter.addStyledParagraphOfText("T1", "Dossier Personnage Hors-jeu")
        wordWriter.addParagraphOfText("Il y a " + listPHJ.size() + " Personnages Hors-jeu(PHJ) dans ce GN dont voici la liste : ")
        String resListPHJ = ""
        for (Character c : listPHJ)
            resListPHJ += c.lastname.toUpperCase() + " " + c.firstname + "\n"
        wordWriter.addParagraphOfText(resListPHJ)
        wordWriter.addParagraphOfText("Vous trouverez ci-dessous les dossiers Personnages Hors-jeu, triés par ordre Alphabétique")
        wordWriter.addParagraphOfText("------------------------------------------------------------------------------------------------")
        createCharactersFile(listPHJ, jsoncharlist, fileName)
    }

    // Création de toutes les fiches de personnages de la liste entrée en paramètre
    private createCharactersFile(ArrayList<Character> listCharacters, ArrayList<String> jsoncharlist = [], String fileName = null) {
        for (Character c : listCharacters) {
            Br br = wordWriter.factory.createBr()
            br.setType(STBrType.PAGE)
            wordWriter.addObject(br)

            String typePerso
            if (c.isPJ()) {
                typePerso = "PJ"
            } else if (c.isPNJ()) {
                typePerso = "PNJ"
            } else {
                typePerso = "PHJ"
            }
            wordWriter.addStyledParagraphOfText("T2", c.firstname + " " + c.lastname)

            wordWriter.addStyledParagraphOfText("T3", "Profil")
            String sex = c.gender.toUpperCase().equals("M") ? "Homme" : "Femme"
            wordWriter.addParagraphOfText("Sexe du personnage : " + sex)
            wordWriter.addParagraphOfText("Age du personnage : " + c.getAge())
            wordWriter.addParagraphOfText("Type de personnage : " + typePerso)

            wordWriter.addStyledParagraphOfText("T3", "Introduction")
            createPitchTablePerso(typePerso)

            //Todo: Ajouter les relations entre les personnages

            wordWriter.addStyledParagraphOfText("T3", "Mon Histoire")

            wordWriter.addParagraphOfText("Je m'appelle " + c.firstname + " " + c.lastname + ".")
            wordWriter.addParagraphOfText("Voici mon histoire :")

            Map<Integer, RoleHasPastscene> roleHasPastsceneList = new TreeMap<>()

            for (Role r : c.getSelectedRoles()) {
                for (RoleHasPastscene roleHasPastscene : r.roleHasPastscenes) {

                    Integer time = roleHasPastscene.pastscene.timingRelative
                    String unit = roleHasPastscene.pastscene.unitTimingRelative
                    /*
                    if (unit.toLowerCase().startsWith("y") && roleHasPastscene.pastscene.timingRelative <= 1) {
                        time = 365
                    }
                    if (unit.toLowerCase().startsWith("y") && roleHasPastscene.pastscene.timingRelative > 1) {
                        time = 365 * roleHasPastscene.pastscene.timingRelative
                    }
                    if (unit.toLowerCase().startsWith("m")) {
                        time = 30 * roleHasPastscene.pastscene.timingRelative
                    }
                    if (!time) {
                        time = 1
                    }
                    */
                    String beforeUnit = "";
                    String beforeValue = "";

                    if (gn.t0Date.getAt(Calendar.YEAR) == roleHasPastscene.pastscene.dateYear) {
                        if (gn.t0Date.getAt(Calendar.MONTH) == roleHasPastscene.pastscene.dateMonth) {
                            if (gn.t0Date.getAt(Calendar.DAY_OF_MONTH) == roleHasPastscene.pastscene.dateDay) {
                                beforeUnit = "heure(s)"
                                beforeValue = gn.t0Date.getAt(Calendar.HOUR_OF_DAY) - roleHasPastscene.pastscene.dateHour
                            } else {
                                beforeUnit = "jour(s)"
                                beforeValue = gn.t0Date.getAt(Calendar.DAY_OF_MONTH) - roleHasPastscene.pastscene.dateDay
                            }
                        } else {
                            beforeUnit = "mois"
                            beforeValue = gn.t0Date.getAt(Calendar.MONTH) - roleHasPastscene.pastscene.dateMonth
                        }
                    } else {
                        beforeUnit = "an(s)"
                        beforeValue = gn.t0Date.getAt(Calendar.YEAR) - roleHasPastscene.pastscene.dateYear
                    }

                    String GnRelat = "Il y a " + beforeValue + " " + beforeUnit
                    String GnFixDate = "Le " + roleHasPastscene.pastscene.dateDay + "/" + roleHasPastscene.pastscene.dateMonth + "/" + roleHasPastscene.pastscene.dateYear + " à " + roleHasPastscene.pastscene.dateHour + "h" + roleHasPastscene.pastscene.dateMinute;
                    //String GnFixDate = roleHasPastscene.pastscene.printDate(gn.date)
                    //le 3 juin 1995: il y a 4 ans, voici le titre de la past
                    int lastIndexOf = roleHasPastscene.pastscene.title.lastIndexOf(" -")
                    if (lastIndexOf != -1)
                        roleHasPastscene.pastscene.title = roleHasPastscene.pastscene.title.substring(0, lastIndexOf)
                    wordWriter.addStyledParagraphOfText("T4", GnFixDate + " : " + GnRelat + ", " + roleHasPastscene.pastscene.title)
                    wordWriter.addParagraphOfText(roleHasPastscene.description)

                    try {
                        roleHasPastsceneList.put(time, roleHasPastscene)
                    } catch (Exception e) {
                        continue
                    }

                }
            }

            for (Integer i = roleHasPastsceneList.values().size() - 1; i > 0; i--) {
                RoleHasPastscene roleHasPastscene = roleHasPastsceneList.values().toArray()[i]
                String unit = roleHasPastscene.pastscene.unitTimingRelative
/*
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
                wordWriter.addStyledParagraphOfText("T4", "Il y a " + roleHasPastscene.pastscene.timingRelative + " " + unit + " : " + roleHasPastscene.pastscene.title)
*/

                /*
                if (unit.toLowerCase().startsWith("y") && roleHasPastscene.pastscene.timingRelative <= 1) {
                    unit = "an"
                }
                if (unit.toLowerCase().startsWith("y") && roleHasPastscene.pastscene.timingRelative > 1) {
                    unit = "années"
                }
                if ((unit.toLowerCase().startsWith("d") || unit.toLowerCase().startsWith("j")) && roleHasPastscene.pastscene.timingRelative <= 1)
                    unit = "jour"
                if ((unit.toLowerCase().startsWith("d") || unit.toLowerCase().startsWith("j")) && roleHasPastscene.pastscene.timingRelative > 1)
                    unit = "jours"
                if (unit.toLowerCase().startsWith("m")) {
                    unit = "mois"
                }
                String GnRelat = "Il y a " + roleHasPastscene.pastscene.timingRelative + " " + unit
                String GnFixDate = "Le "+ roleHasPastscene.pastscene.dateDay +"//"+roleHasPastscene.pastscene.dateMonth+"//"+roleHasPastscene.pastscene.dateYear
                + " à " + roleHasPastscene.pastscene.dateHour + "h" + roleHasPastscene.pastscene.dateMinute;
                //String GnFixDate = roleHasPastscene.pastscene.printDate(gn.date)
                //le 3 juin 1995: il y a 4 ans, voici le titre de la past
                String GnFixDate = roleHasPastscene.pastscene.printDate(gn.date)
                int lastIndexOf = roleHasPastscene.pastscene.title.lastIndexOf(" -")
                if (lastIndexOf != -1)
                    roleHasPastscene.pastscene.title = roleHasPastscene.pastscene.title.substring(0, lastIndexOf)

                wordWriter.addStyledParagraphOfText("T4", GnFixDate + " : " + GnRelat + ", " + roleHasPastscene.pastscene.title)
                wordWriter.addParagraphOfText(roleHasPastscene.description)
                */
            }

            boolean hasTags = false
            for (Role r : c.getSelectedRoles()) {
                if (r.roleHasTags) {
                    hasTags = true
                    break
                }

            }

            // Ajout du Graphe relationnel du personnage
            if (!jsoncharlist.isEmpty()) {
                wordWriter.addStyledParagraphOfText("T3", "Vous connaissez...")
                String charName = c.firstname + " " + c.lastname.toUpperCase()
                wordWriter.addRelationGraph(jsoncharlist, fileName, charName)
                Graph charGraph = new Graph(gn, false)
                wordWriter.addParagraphOfText(charGraph.getRelation(charName))
                wordWriter.addStyledParagraphOfText("T4", "Synthèses des personnages connus")

                Tbl table = wordWriter.factory.createTbl()
                Tr tableRow = wordWriter.factory.createTr()

                wordWriter.addTableStyledCell("Table1L", tableRow, "Nom")
                wordWriter.addTableStyledCell("Table1L", tableRow, "Description")
                wordWriter.addTableStyledCell("Table1L", tableRow, "Lien")
                table.getContent().add(tableRow);
                HashMap <String,String> cMap = charGraph.getRelationList(charName)
                for (String char2 : cMap.keySet()){
                    Tr tableRowChar = wordWriter.factory.createTr()
                    String link = cMap.get(char2)
                    wordWriter.addTableStyledCell("Table1C", tableRowChar, char2)
                    wordWriter.addTableStyledCell("small", tableRowChar, "description")
                    wordWriter.addTableStyledCell("small", tableRowChar, link)
                    table.getContent().add(tableRowChar);
                }
                wordWriter.addBorders(table)
                wordWriter.addObject(table)
            }

            if (!hasTags)
                continue

            wordWriter.addStyledParagraphOfText("T3", "Conseils d'interprétation");
            wordWriter.addParagraphOfText("Ce personnage est : ")
            for (Role r : c.getSelectedRoles()) {
                for (RoleHasTag roleHasTag : r.roleHasTags) {
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
                    wordWriter.addParagraphOfText(qualificatif + " " + roleHasTag.tag.name)
                }
            }

            // todo : wordWriter.addStyledParagraphOfText("T3", "J'ai sur moi...")
        }
    }

    //Création du dossier destiné aux "personnages" dit Staff
    def createStaffFolder() {
        Br br = wordWriter.factory.createBr()
        br.setType(STBrType.PAGE)
        wordWriter.addObject(br)
        wordWriter.addStyledParagraphOfText("T1", "Dossier Staff")
        wordWriter.addParagraphOfText("Il y a " + gn.staffCharSet.size() + " personnes dans l'équipe Staff de ce GN")
        wordWriter.addParagraphOfText("\n\n")
        wordWriter.addParagraphOfText("Vous trouverez ci-dessous les dossiers de cette équipe, à distribuer aux staff")
        wordWriter.addParagraphOfText("------------------------------------------------------------------------------------------------")
        createStaffFile(new ArrayList<Character>(gn.staffCharSet))
    }

    def createStaffFile(ArrayList<Character> listStaff) {
        int i = 1
        for (Character c : listStaff) {
            Br br = wordWriter.factory.createBr()
            br.setType(STBrType.PAGE)
            wordWriter.addObject(br)
            wordWriter.addStyledParagraphOfText("T2", "Staff #" + i.toString())
            wordWriter.addParagraphOfText("Type : " + c.type)

            // L'événnementiel du staff
            wordWriter.addStyledParagraphOfText("T3", "Evénementiel")
            Map<Integer, Event> events = new TreeMap<Integer, Event>();
            // Substitution pour l'evennementiel du staff
            for (Plot p : gn.selectedPlotSet)
                for (Event e : p.events) {
                    HashMap<String, Role> rolesNames = new HashMap<>()
                    for (Role r : c.selectedRoles)
                        if (r.plot.DTDId.equals(e.plot.DTDId))
                            rolesNames.put(c.firstname + " " + c.lastname, r)
                    substitutionPublication = new SubstitutionPublication(rolesNames, gnk.placeMap.values().toList(), gnk.genericResourceMap.values().toList())
                    if (e.name)
                        e.name = substitutionPublication.replaceAll(e.name)
                    events.put(e.timing, e)
                }

            // Publication de l'evennementiel du staff
            for (Plot p : gn.selectedPlotSet)
                for (Event e : p.events)
                    for (Role r : c.selectedRoles)
                        if (r.plot.DTDId.equals(e.plot.DTDId)) {
                            wordWriter.addStyledParagraphOfText("T4", p.name)
                            wordWriter.addParagraphOfText(p.description)
                            wordWriter.addStyledParagraphOfText("T5", "Votre rôle : " + r.code)
                            wordWriter.addParagraphOfText(r.type + " - " + r.description)
                            // Les ressources du staff liés à son évennementiel
                            wordWriter.addStyledParagraphOfText("T5", "Vos ressources")
                            boolean hasRessource = false
                            for (GenericResource gr : gnk.genericResourceMap.values())
                                if (gr.selectedResource && gr.possessedByRole != null && (gr.possessedByRole.id == r.id)) {
                                    hasRessource = true
                                    String pubResource = (gr.code ? gr.code + " - " : "")
                                    pubResource += (gr.description ? gr.description + " - " : "")
                                    pubResource += (gr.comment ? gr.comment + " - " : "")
                                    pubResource += (gr.selectedResource.name ? gr.selectedResource.name : "")
                                    pubResource += (gr.selectedResource.description && !gr.selectedResource.name.equals(gr.selectedResource.description) ? " - " + gr.selectedResource.description : "")
                                    wordWriter.addParagraphOfText(pubResource)
                                }
                            (hasRessource ?: wordWriter.addParagraphOfText("Vous ne possédez aucun objet lié à cet événement"))
                        }
            i++
        }
    }

    //Créatiion du tableau de synthèse des pitchs des intrigue pour les PJ, PNJ et PHJ
    def createPitchTablePerso(String typePerso) {
        if (typePerso == "PJ") {
            for (Plot p : gn.selectedPlotSet) {
                wordWriter.addParagraphOfText(p.pitchPj)
            }
        } else if (typePerso == "PNJ" || typePerso == "PHJ") {
            for (Plot p : gn.selectedPlotSet) {
                wordWriter.addParagraphOfText(p.pitchPnj)
            }
        }
    }

    // Création du tableau de synthèse des pitch des intrigues pour les Orga
    def createPitchOrga() {
        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        wordWriter.addParagraphOfText("Le GN se déroule dans l'Univers " + gn.univers.name.replace("(Univers)", "") + ".")
        wordWriter.addParagraphOfText("Au début du GN, la scène est censée se dérouler " + getPrintableDate(gn.date, "'le' dd MMMM yyyy 'à' HH'h'mm") + ". La durée du GN est estimée à " + gn.duration.toString() + " heures.")

        // Comptage PJ
        String msgCharacters = PitchOrgaMsgCharacters()
        wordWriter.addParagraphOfText(msgCharacters)

        // On affiche d'abord les intrigue evennementiel
        for (Plot p : gn.selectedPlotSet)
            if (p.pitchOrga != null && p.isEvenemential) {
                wordWriter.addStyledParagraphOfText("T5", p.name)
                substituteRolesAndPlotDescription(p)
                wordWriter.addParagraphOfText(p.pitchOrga)
            }

        // Puis on affiche les autres intrigues
        for (Plot p : gn.selectedPlotSet) {
            if (p.isEvenemential) // car déjà traité dans la boucle for d'avant
                continue
            if (p.pitchOrga != null) {
                wordWriter.addStyledParagraphOfText("T5", p.name)
                substituteRolesAndPlotDescription(p)
                wordWriter.addParagraphOfText(p.pitchOrga)
            }
        }
        wordWriter.addBorders(table)
        wordWriter.addObject(table);
    }

    // Fonction renvoyant un message sur le nombre des personnages dans le pitch orga
    def PitchOrgaMsgCharacters() {
        int NbPJ = gn.characterSet.size()
        String msgCharacters = "Ce GN accueille " + NbPJ + " Personnage" + ((NbPJ > 1) ? "s" : "") + " Joueur" + ((NbPJ > 1) ? "s" : "") + " (PJ dont "
        String tmpGender = ""
        int nbMale = 0, nbFemale = 0, nbNoGender = 0
        for (int i = 0; i < NbPJ; i++) {
            org.gnk.roletoperso.Character tmpCharacter = gn.characterSet.asList()[i]
            tmpGender = tmpCharacter.type
            if (tmpGender == "PJ") {
                tmpGender = tmpCharacter.gender
                if (tmpGender == "M")
                    nbMale++
                else if (tmpGender == "F")
                    nbFemale++
                else
                    nbNoGender++
            }
        }
        if (nbFemale > 0)
            msgCharacters += nbFemale + " fille" + ((nbFemale > 1) ? "s" : "")
        if (nbFemale > 0 && nbMale > 0)
            msgCharacters += " et "
        if (nbMale > 0)
            msgCharacters += nbMale + " garçon" + ((nbMale > 1) ? "s" : "")
        msgCharacters += ") et nécessite "

        //Comptage PNJ
        int NbNJ = gn.nonPlayerCharSet.size()
        tmpGender = ""; nbMale = 0; nbFemale = 0; nbNoGender = 0
        int NbPNJ = 0
        for (int i = 0; i < NbNJ; i++) {
            org.gnk.roletoperso.Character tmpCharacter = gn.nonPlayerCharSet.asList()[i]
            tmpGender = tmpCharacter.type
            if (tmpGender == "PNJ") {
                NbPNJ++
                tmpGender = tmpCharacter.gender
                if (tmpGender == "M")
                    nbMale++;
                else if (tmpGender == "F")
                    nbFemale++;
                else
                    nbNoGender++;
            }
        }

        msgCharacters += NbPNJ + " Personnage" + ((NbPNJ > 1) ? "s" : "") + " Non Joueur" + ((NbPNJ > 1) ? "s" : "") + " (PNJ)  " + ((nbNoGender == NbPNJ) ? "" : "(")
        if (nbFemale > 0)
            msgCharacters += nbFemale + " fille" + ((nbFemale > 1) ? "s" : "")
        if (nbFemale > 0 && nbMale > 0)
            msgCharacters += " et "
        if (nbMale > 0)
            msgCharacters += nbMale + " garçon" + ((nbMale > 1) ? "s" : "")
        msgCharacters += ((nbNoGender == NbPNJ) ? ". " : "). ")

        //Comptage PHJ
        int NbPHJ = 0
        for (int i = 0; i < NbNJ; i++) {
            org.gnk.roletoperso.Character tmpCharacter = gn.nonPlayerCharSet.asList()[i]
            tmpGender = tmpCharacter.type
            if (tmpGender == "PHJ") {
                NbPHJ++
                tmpGender = tmpCharacter.gender
                if (tmpGender == "M")
                    nbMale++;
                else if (tmpGender == "F")
                    nbFemale++;
                else
                    nbNoGender++;
            }
        }
        msgCharacters += "Il mentionne " + NbPHJ + " Personnage" + ((NbPHJ > 1) ? "s" : "") + " Hors jeu (PHJ). Dans ce document, le timing a été calculé pour un jeu commençant à "
        msgCharacters += getPrintableDate(gn.t0Date)
        return msgCharacters
    }

    // Création du tableau de synthèse listant tous les ingames clues du GN pour les Orga
    def createICTableOrga() {
        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()
        wordWriter.addTableStyledCell("Table1L", tableRow, "Indice en Jeu")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Détenu au début du Jeu par")
        table.getContent().add(tableRow);
        for (GenericResource genericResource : gnk.genericResourceMap.values())
            if (genericResource.isIngameClue()) // Si la générique ressource est un ingame clue alors je l'affiche
            {
                Tr tableRowPlot = wordWriter.factory.createTr()
                wordWriter.addTableStyledCell("Table1C", tableRowPlot, genericResource.code + " - " + genericResource.comment)
                if (genericResource.getPossessedByRole() != null) {
                    String possessedByCharacters = ""
                    for (Character c : gn.characterSet + gn.nonPlayerCharSet + gn.staffCharSet) {
                        for (Role r : c.selectedRoles) {
                            if (r.getDTDId() == genericResource.getPossessedByRole().getDTDId()) {
                                possessedByCharacters += (possessedByCharacters.isEmpty() ? genericResource.getPossessedByRole().code + " : " : ", ") + c.firstname + " " + c.lastname.toUpperCase()
                            }
                        }
                    }
                    wordWriter.addTableStyledCell("small", tableRowPlot, possessedByCharacters)
                } else
                    wordWriter.addTableStyledCell("small", tableRowPlot, "Personne")
                table.getContent().add(tableRowPlot);
            }
        wordWriter.addBorders(table)
        wordWriter.addObject(table);



        wordWriter.addStyledParagraphOfText("T3", "Détails")
        for (GenericResource genericResource : gnk.genericResourceMap.values())
            if (genericResource.isIngameClue()) // Si la générique ressource est un ingame clue alors je l'affiche
            {
                // construction du substitutionPublication
                HashMap<String, Role> rolesNames = new HashMap<>()
                for (Character c : gn.characterSet)
                    for (Role r : c.selectedRoles)
                        if (r.plot.DTDId.equals(genericResource.plot.DTDId))
                            rolesNames.put(c.firstname + " " + c.lastname, r)
                for (Character c : gn.nonPlayerCharSet)
                    for (Role r : c.selectedRoles)
                        if (r.plot.DTDId.equals(genericResource.plot.DTDId))
                            rolesNames.put(c.firstname + " " + c.lastname, r)
                substitutionPublication = new SubstitutionPublication(rolesNames, gnk.placeMap.values().toList(), gnk.genericResourceMap.values().toList())
                // Fin construction du substitutionPublication

                genericResource.title = substitutionPublication.replaceAll(genericResource.title)
                genericResource.description = substitutionPublication.replaceAll(genericResource.description)
                wordWriter.addStyledParagraphOfText("T5", genericResource.code + " - " + genericResource.comment)
                if (genericResource.fromRole)
                    wordWriter.addStyledParagraphOfText("clueFrom", "De " + genericResource.fromRole.code)
                if (genericResource.toRole)
                    wordWriter.addStyledParagraphOfText("clueTo", "Pour " + genericResource.toRole.code)
                wordWriter.addStyledParagraphOfText("clueTitle", genericResource.title)
                wordWriter.addStyledParagraphOfText("clueDescription", genericResource.description)
            }
    }

    // Création du tableau de synthèse des intrigues
    def createPlotTable() {
        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        wordWriter.addTableStyledCell("Table1L", tableRow, "Nom de l'intrigue")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Nb PIP")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Indication(s) Intrigue")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Résumé/Description")


        table.getContent().add(tableRow);

        //Priorisation des plot événementiels
        for (Plot p : gn.selectedPlotSet) {
            if (p.name == "Life")
                continue
            if (p.isEvenemential) {
                Tr tableRowPlot = wordWriter.factory.createTr()
                wordWriter.addTableStyledCell("Table1C", tableRowPlot, p.name)
                wordWriter.addTableStyledCell("small", tableRowPlot, p.getSumPipRoles(gn.getNbPlayers()).toString())
                String tags = "Evènementiel"
                if (p.isMainstream)
                    tags += " - Mainstream"
                tags += " : "
                for (PlotHasTag plotHasTag : p.extTags) {
                    if (plotHasTag.tag.parent.name.equals("Tag Univers") && !plotHasTag.tag.name.equals(gn.univers.name))
                        continue
                    tags += plotHasTag.tag.name + " (" + plotHasTag.weight + "%, " + plotHasTag.tag.parent.name + ") " + "\n"
                }
                wordWriter.addTableStyledCell("small", tableRowPlot, tags.substring(0, tags.length() - 1))

                substituteRolesAndPlotDescription(p)

                String description = new String(p.description.getBytes("UTF-8"), "UTF-8")
                wordWriter.addTableStyledCell("small", tableRowPlot, description)
                table.getContent().add(tableRowPlot);
            }
        }

        //Puis, le même traitement sur les plots non événementiels
        for (Plot p : gn.selectedPlotSet) {
            //Ignorer Life
            if (p.name == "Life" || p.isEvenemential)
                continue
            Tr tableRowPlot = wordWriter.factory.createTr()
            wordWriter.addTableStyledCell("Table1C", tableRowPlot, p.name)
            wordWriter.addTableStyledCell("small", tableRowPlot, p.getSumPipRoles(gn.getNbPlayers()).toString())

            String tags = ""
            if (p.isMainstream)
                tags += "Mainstream : "
            for (PlotHasTag plotHasTag : p.extTags) {
                if (plotHasTag.tag.parent.name.equals("Tag Univers") && !plotHasTag.tag.name.equals(gn.univers.name))
                    continue
                tags += plotHasTag.tag.name + " (" + plotHasTag.weight + "%, " + plotHasTag.tag.parent.name + ") " + "\n"
            }
            wordWriter.addTableStyledCell("small", tableRowPlot, tags.substring(0, tags.length() - 1))

            substituteRolesAndPlotDescription(p)

            String description = new String(p.description.getBytes("UTF-8"), "UTF-8")
            wordWriter.addTableStyledCell("small", tableRowPlot, description)
            table.getContent().add(tableRowPlot);
        }

        wordWriter.addBorders(table)
        wordWriter.addObject(table);
    }

    def createPlayersTableImplication() {
        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()
        wordWriter.addTableStyledCell("Table1L", tableRow, "Nom de l'intrigue")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Implication des Personnages")
        table.getContent().add(tableRow);

        for (Plot p : gn.selectedPlotSet) {
            if (p.name == "Life")
                continue
            Tr tableRowPlot = wordWriter.factory.createTr()
            wordWriter.addTableStyledCell("Table1C", tableRowPlot, p.name)
            String playerImplication = ""
            HashSet roles = p.getRoles()
            for (Role r1 : roles) {
                for (Character c : gn.characterSet + gn.nonPlayerCharSet + gn.staffCharSet) {
                    for (Role r2 : c.selectedRoles) {
                        if (r1.DTDId == r2.DTDId) {
                            playerImplication += (playerImplication.isEmpty() ? "" : "\n") + c.firstname + " " + c.lastname.toUpperCase() + " : " + r1.description
                        }
                    }
                }
            }
            wordWriter.addTableStyledCell("small", tableRowPlot, playerImplication)
            table.getContent().add(tableRowPlot);
        }
        wordWriter.addBorders(table)
        wordWriter.addObject(table);
    }

    // Handles the substitution of each role, object or place for a plot description and each roles inside this plot
    private substituteRolesAndPlotDescription(Plot p) {
        HashMap<String, Role> rolesNames = new HashMap<>()
        for (Character c : gn.characterSet) {
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

        for (Role r : p.roles) {
            r.description = substitutionPublication.replaceAll(r.description)
            for (RoleHasPastscene rp : r.roleHasPastscenes) {
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

        wordWriter.addTableStyledCell("Table1L", tableRow, "Intrigue")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Personnage impliqué")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Code rôle")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Nombre de PIP intrigue")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Nombre de PIP relations")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Description du rôle")

        table.getContent().add(tableRow);

        for (Plot p : gn.selectedPlotSet) {
            //Ignorer Life
            if (p.name == "Life")
                continue
            Tr tableRowPlot = wordWriter.factory.createTr()
            wordWriter.addTableStyledCell("Table1C", tableRowPlot, p.name)

            boolean first = true
            for (Role r : p.roles) {
                if (!first) {
                    tableRowPlot = wordWriter.factory.createTr()
                    wordWriter.addTableStyledCell("Table1C", tableRowPlot, "")
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
                } else
                    characterName = "Tous les personnages joués"
                if (characterName.equals(""))
                    print "Erreur : nom du personnage non trouvé"
                wordWriter.addTableStyledCell("small", tableRowPlot, characterName)
                wordWriter.addTableStyledCell("small", tableRowPlot, r.code)
                wordWriter.addTableStyledCell("small", tableRowPlot, r.pipi.toString())
                wordWriter.addTableStyledCell("small", tableRowPlot, r.pipr.toString())
                wordWriter.addTableStyledCell("small", tableRowPlot, r.description)

                table.getContent().add(tableRowPlot);
                first = false
            }
        }

        wordWriter.addBorders(table)
        wordWriter.addObject(table);

    }

    // Création du tableau des évènements
    def createEventsTable() {
        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        wordWriter.addTableStyledCell("Table1L", tableRow, "Timing événementiel")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Titre")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Lieu")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Intrigue concernée")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Evènement annoncé")
        table.getContent().add(tableRow);

        Map<Integer, Event> events = new TreeMap<Integer, Event>();
        for (Plot p : gn.selectedPlotSet) {
            for (Event e : p.events) {
                HashMap<String, Role> rolesNames = new HashMap<>()
                for (Character c : gn.characterSet) {
                    for (Role r : c.selectedRoles) {
                        if (r.plot.DTDId.equals(e.plot.DTDId))
                            rolesNames.put(c.firstname + " " + c.lastname, r)
                    }
                }

                // Gestion des pnjs pour la substitution des noms
                for (Character c : gn.nonPlayerCharSet) {
                    for (Role r : c.selectedRoles) {
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

        events = events.sort {
            a,b ->
            Date dateA = new Date(a.value.absoluteYear, a.value.absoluteMonth, a.value.absoluteDay, a.value.absoluteHour, a.value.absoluteMinute);
            Date dateB = new Date(b.value.absoluteYear, b.value.absoluteMonth, b.value.absoluteDay, b.value.absoluteHour, b.value.absoluteMinute);
            dateA <=> dateB
        }

        for (Event e : events.values()) {
            Tr tableRowEvent = wordWriter.factory.createTr()
            //wordWriter.addTableStyledCell("small",tableRowEvent, e.timing.toString())
            //Todo: Indiquer l'horaire absolu (pour le moment le parser correspondant n'existe pas donc les champs absolus sont nuls
            //wordWriter.addTableStyledCell("small",tableRowEvent, e.absoluteHour + "h" + e.absoluteMinute + " le " + e.absoluteDay + "/" + e.absoluteMonth + "/" + e.absoluteYear)
            wordWriter.addTableStyledCell("Table1C", tableRowEvent, "Le " + ((e.absoluteDay < 10) ? "0" : "") + e.absoluteDay + " à " + ((e.absoluteHour < 10) ? "0" : "") + e.absoluteHour + "h" + ((e.absoluteMinute < 10) ? "0" : "") + e.absoluteMinute)
            wordWriter.addTableStyledCell("small", tableRowEvent, e.name)
            if (e.genericPlace && e.genericPlace.proposedPlaces && e.genericPlace.proposedPlaces.size() > 0) {
                int lastIndexOf = e.genericPlace.proposedPlaces[0].name.lastIndexOf(" -")
                if (lastIndexOf != -1)
                    wordWriter.addTableStyledCell("small", tableRowEvent, e.genericPlace.proposedPlaces[0].name.substring(0, lastIndexOf))
                else
                    wordWriter.addTableStyledCell("small", tableRowEvent, e.genericPlace.proposedPlaces[0].name)
            } else
                wordWriter.addTableStyledCell("small", tableRowEvent, "")
            wordWriter.addTableStyledCell("small", tableRowEvent, e.plot.name)
            wordWriter.addTableStyledCell("small", tableRowEvent, e.isPlanned ? "Oui" : "Non")
            table.getContent().add(tableRowEvent);
        }

        wordWriter.addBorders(table)
        wordWriter.addObject(table);


    }

    def createPastSceneTable() {
        Tbl table = wordWriter.factory.createTbl()
        Tr tableRow = wordWriter.factory.createTr()

        wordWriter.addTableStyledCell("Table1L", tableRow, "Date")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Nom Intrigue")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Titre pastscene")
        wordWriter.addTableStyledCell("Table1L", tableRow, "Description pastscene")
        wordWriter.addTableStyledCell("Table1L", tableRow, "personnages concernés")
        table.getContent().add(tableRow);

        // Optimisation : Hashmap permettant de récupéré tous les personnages concernés par la pastscene
        // Map <DTDid Pastscene, DTDid character>
        Map<Pastscene, ArrayList<Character>> pMap = new HashMap();

        for (Character c : gn.characterSet + gn.nonPlayerCharSet + gn.staffCharSet) {
            for (Role r : c.getSelectedRoles()) {
                for (RoleHasPastscene rhp : r.roleHasPastscenes) {
                    Pastscene p = rhp.getPastscene()
                    if (p.plot.name.equals("Life"))
                        continue
                    if (pMap.containsKey(p))
                        pMap.get(p).add(c)
                    else {
                        ArrayList<Character> cList = new ArrayList<Character>()
                        cList.add(c)
                        pMap.put(p, cList)
                    }
                }
            }
        }
        pMap = pMap.sort {a,b ->
                    Date dateA = new Date(a.key.dateYear, a.key.dateMonth,a.key.dateDay,a.key.dateHour,a.key.dateMinute);
                    Date dateB = new Date(b.key.dateYear, b.key.dateMonth,b.key.dateDay,b.key.dateHour,b.key.dateMinute);
                    dateA <=> dateB};

        for (Pastscene p : pMap.keySet()) {
            Tr tableRowEvent = wordWriter.factory.createTr()
            String GnFixDate = "Le " + (p.dateDay < 10 ? "0" : "") + p.dateDay + "/"
            GnFixDate += (p.dateMonth < 10 ? "0" : "") + p.dateMonth + "/"
            GnFixDate += (p.dateYear < 10 ? "0" : "") + p.dateYear + " à "
            GnFixDate += (p.dateHour < 10 ? "0" : "") + p.dateHour + "h"
            GnFixDate += (p.dateMinute < 10 ? "0" : "") + p.dateMinute
            wordWriter.addTableStyledCell("Table1C", tableRowEvent, GnFixDate)
            wordWriter.addTableStyledCell("small", tableRowEvent, p.getPlot().name)
            wordWriter.addTableStyledCell("small", tableRowEvent, p.title)
            wordWriter.addTableStyledCell("small", tableRowEvent, p.description)
            String concernedCharacters = ""
            for (Character c : pMap.get(p))
                concernedCharacters += c.firstname + " " + c.lastname.toUpperCase() + "\n"
            concernedCharacters = concernedCharacters.substring(0, concernedCharacters.length() - 1)
            wordWriter.addTableStyledCell("small", tableRowEvent, concernedCharacters)
            table.getContent().add(tableRowEvent);
        }
        wordWriter.addBorders(table)
        wordWriter.addObject(table);
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
        if (getGn.equals(null)) {
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

        String fileName = "${gnk.gn.name.replaceAll(" ", "_").replaceAll("/", "_")}_${System.currentTimeMillis()}-${csvType}.csv"

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
                ret = ret.concat(related.key.firstname + " " + related.key.lastname + ", ")
            }
        }
        if (ret.length() > 0) {
            ret = ret.substring(0, ret.length() - 2)
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
            tabOfPlayer.get(j).set(colAge,/* "("+ageValue+")" +*/ (newAgeValue))
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
                    newValue = (4 - ((value2 / minValue) * 4))
                } else if (value2 > 0) {
                    newValue = ((value2 / maxValue) * 4 + 5)
                } else { // value == 0
                    // On laisse 4
                }
                tabOfPlayer.get(j).set(i, /*"("+value2+")" +*/ (newValue))
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
                ret = ret.concat(val + "; ")
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
        ArrayList<String> topTitle = new ArrayList<String>(Arrays.asList("Prénom", "Nom", "Sexe", "Session avec", "Session sans", "Joue avec", "Joue sans", "Joueur interdit", "Age"))
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
        ArrayList<String> topTitle = new ArrayList<String>(Arrays.asList("Prénom", "Nom", "Sexe", "Joue avec", "Joue sans", "Joueur prefere", "Joueur facultatif", "Age"))
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
            currentPlayer.add(7, "" + chara.age) // Age

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
    private HashMap<Integer, Integer> orderTagByOccurence(Set<Character> charSet) {

        HashMap<Integer /*tag id*/, Integer /* occurence */> returnedList = new HashMap<>()
        // Pour chaque personnage
        charSet.each { chara ->
            // Pour chacun de ces tags on regarde combien de fois il est déjà apparu dans les personnages
            chara.tags.each { tag ->
                if (tag.key.parent.name == "Trait de personnalité") {
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
        returnedList = returnedList.sort { a, b -> b.value <=> a.value }
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


    private String getPrintableDate(Date date, String formater = null, int format1 = DateFormat.MEDIUM, int format2 = DateFormat.SHORT) {
        if (formater == null) {
            DateFormat shortDateFormat = DateFormat.getDateTimeInstance(
                    format1,
                    format2,
                    new Locale("FR", "fr"));
            return shortDateFormat.format(date)
        } else {
            return new SimpleDateFormat(formater).format(date)
        }

    }
}
