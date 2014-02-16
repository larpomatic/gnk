package org.gnk.roletoperso

import org.gnk.gn.Gn
import org.gnk.parser.GNKDataContainerService
import org.gnk.parser.gn.GnXMLWriterService
import org.gnk.tag.Univers

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
        [gnInstance: gn, characterList: gn.characterSet, characterListToDropDownLock: characterListToDropDownLock]
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
}
