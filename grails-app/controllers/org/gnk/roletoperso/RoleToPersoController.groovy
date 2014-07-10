package org.gnk.roletoperso

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
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
            if (params.selectedMainstream) {
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
        GnXMLWriterService gnXMLWriterService = new GnXMLWriterService()
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

        String json_relation = "";


        JSONArray json_array = new JSONArray();
        Set<Character> characters = gn.characterSet.clone();
        characters.addAll(gn.nonPlayerCharSet);

        for (Character c : characters) {
            JSONObject json_object = new JSONObject();

            JSONArray json_adjacencies = new JSONArray();

            for (Character c2 : characters) {
                if (c != c2)
                {
                    String lien1 = c.getRelatedCharactersExceptBijectivesLabel(gn).get(c2);
                    String lien2 = c2.getRelatedCharactersExceptBijectivesLabel(gn).get(c);
                    if ((lien1 != null) && (lien1.isEmpty() == false))
                    {
                        JSONObject json_adjacencies_obj = new JSONObject();
                        json_adjacencies_obj.put("nodeTo", "CHAR" + c2.getDTDId());
                        json_adjacencies_obj.put("nodeFrom", "CHAR" + c.getDTDId());
                        JSONObject json_data = new JSONObject();
                        json_data.put("lien", lien1);
                        if ((lien2 != null) && (lien2.isEmpty() == false))
                            json_data.put("lien2", lien2);
                        json_adjacencies_obj.put("data", json_data);
                        json_adjacencies.add(json_adjacencies_obj);
                    }
                }
            }
            json_object.put("adjacencies", json_adjacencies);

            JSONObject json_colortype = new JSONObject();
            if (c.isMen())
                json_colortype.put("\$color", "#0040FF");
            else if (c.isWomen())
                json_colortype.put("\$color", "#FE2EC8");
            else
                json_colortype.put("\$color", "#848484");
            if (c.isPJ())
                json_colortype.put("\$type", "circle");
            else if (c.isPHJ())
                json_colortype.put("\$type", "triangle");
            else
                json_colortype.put("\$type", "square")
            json_object.put("data", json_colortype);

            json_object.put("id", "CHAR" + c.getDTDId());
            json_object.put("name", "CHAR" + c.getDTDId());
            json_array.add(json_object);
        }

        json_relation = json_array.toString();

        ArrayList<String> values = new ArrayList<>();
        for (Character c in gn.characterSet)
        {
            Map<Tag, Integer> test = c.getTags();
            Set<Tag> tags = test.keySet();
            TagService tagservice = new TagService();
            for (Tag tag1 in tags)
            {
                for (Tag tag2 in tags)
                {
                    if (tag1 != tag2)
                    {
                        String val = "" + c.getDTDId();
                        val += "#" + tag1.id + "#" + tag1.name + "#" + test.get(tag1);
                        val += "#" + tag2.id + "#" + tag2.name + "#" + test.get(tag2);
                        val += "#" + tagservice.getTagMatching(tag1, 0, tag2, 0);
                        values.add(val);
                    }
                }
            }
        }

        [gnInstance: gn,
         characterList: gn.characterSet,
         allList: algo.gnTPJRoleSet,
         PHJList: gn.nonPlayerCharSet,
         characterListToDropDownLock: characterListToDropDownLock,
         evenementialId: evenementialId,
         relationjson: json_relation,
         tagcompatibility: values,
         mainstreamId: mainstreamId]
    }

    def management(Long id) {
        if (id && id >= 0) {
            gnInstance = Gn.get(id)
        }
        [screenStep: 0, plotTagList: PlotTag.list(), universList: Univers.list(), characters: characterService.characters]
    }

    def edit(Long id) {
        def Character c
        if (id && id >= 0) {
            c = characterService.getCharacter(id as int)
        }
        [screenStep: 0, roles: c.roles, plotTagList: PlotTag.list(), universList: Univers.list(), character: c]
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
        return render(contentType: "application/json") { object(test: id_from, roles: new_role)};
    }
}
