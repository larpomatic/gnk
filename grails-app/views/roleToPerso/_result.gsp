<%@ page import="org.gnk.roletoperso.RoleHasRelationWithRole; org.gnk.roletoperso.Role" %>
<%@ page import="org.gnk.roletoperso.Character" %>
<%@ page import="org.gnk.selectintrigue.Plot" %>
<%@ page import="org.gnk.gn.Gn" %>
<style>
.panel-default > .panel-heading {
    color: #333333;
    background-color: #f5f5f5;
    font-weight: bold;
    border: 1px solid #dddddd;
}

.panel-heading {
    padding: 10px 15px;
    border-bottom: 1px solid transparent;
    border-top-right-radius: 3px;
    border-top-left-radius: 3px;
}

.table-bordered-no-top {
    border: 1px solid #dddddd;
    border-collapse: separate;
    *border-collapse: collapse;
    border-left: 0;
    border-top: 0px;
    border-top-left-radius: 0px;
    border-top-right-radius: 0px;
    margin-bottom: 0;

}

.table-bordered-no-top th,
.table-bordered-no-top td {
    border-left: 1px solid #dddddd;
}
</style>

<h3>
    <g:message code="roletoperso.result"
               default="RoleToPerso result"/>
</h3>
<g:form method="post">
<g:hiddenField name="gnId" value="${gnInstance?.id}"/>
<g:hiddenField name="gnDTD" value="${gnInstance?.dtd}"/>
<g:hiddenField name="version" value="${gnInstance?.version}"/>
<div class="container-fluid">
<g:each in="${characterList}" status="characterIter" var="character">

<g:if test="${characterIter % 2 == 0}">
    <div class="row-fluid">
</g:if>
<div class="span6" id="Char${character.DTDId}">
<div class="panel panel-default">
<div class="panel-heading" style="margin-top: 20px">
    <g:message code="roletoperso.character"
               default="Character"/> ${character.DTDId}
</div>

<div style="overflow: auto; max-height:150px;">
    <table class="table table-bordered-no-top">
        <thead>
        <tr>
            <th><g:message code="roletoperso.roleCode"
                           default="Role code"/></th>
            <th><g:message code="selectintrigue.plotName"
                           default="Plot name"/></th>
            <th width="25"><g:img dir="images/selectIntrigue"
                                  file="locked.png"/></th>
            <th width="25"><g:img dir="images/selectIntrigue"
                                  file="forbidden.png"/></th>
            <th width="25"><g:img dir="images/selectIntrigue"
                                  file="validate.png"/></th>
            <th width="50" align="center"><g:img dir="images/selectIntrigue"
                                                 file="locked.png"/> <g:message code="roletoperso.character"
                                                                                default="Character"/></th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${((Character) character).specificRoles}" status="roleIter" var="role">
            <tr class="${(roleIter % 2) == 0 ? 'even' : 'odd'}">

                <td>
                    <a href="#"
                       title="${((Role) role).description}">${((Role) role).code}</a>
                </td>
                <td><g:link controller="redactIntrigue" action="edit"
                            id="${((Role) role).plot?.id}" target="_blank">
                    ${((Role) role).plot?.name}
                </g:link></td>
                <g:if test="${((Character) character).roleIsLocked(((Role) role))}">
                    <g:set var="selectedRadioButtonLock" value="${1}"/>
                </g:if>
                <g:else>
                    <g:set var="selectedRadioButtonLock" value="${3}"/>
                </g:else>
                <g:radioGroup
                        name="role_status_${((Character) character).DTDId}_${((Plot) ((Role) role).plot).DTDId}_${((Role) role).DTDId}"
                        values="[1, 2, 3]"
                        value="${selectedRadioButtonLock}">
                    <td align="center">
                        ${it.radio}
                    </td>
                </g:radioGroup>
                <td align="center">
                    <g:select style="width: 140px"
                              name="lock_on_${((Character) character).DTDId}_${((Plot) ((Role) role).plot).DTDId}_${((Role) role).DTDId}"
                              id="lock_on_${((Character) character).DTDId}_${((Plot) ((Role) role).plot).DTDId}_${((Role) role).DTDId}"
                              from="${characterListToDropDownLock}"
                              keys="${characterListToDropDownLock}"/>
                </td>
            </tr>
        </g:each>
        <!-- Start NJA Work Step0 -->
        <g:each in="${((Character) character).getSelectedPJG()}" status="roleIter" var="role">
            <tr class="${(roleIter % 2) == 0 ? 'even' : 'odd'} warning">

                <td>
                    <a href="#"
                       title="${((Role) role).description}">${((Role) role).code}</a>
                </td>
                <td><g:link controller="redactIntrigue" action="edit"
                            id="${((Role) role).plot?.id}" target="_blank">
                    ${((Role) role).plot?.name}
                </g:link></td>
                <g:if test="${((Character) character).roleIsLocked(((Role) role))}">
                    <g:set var="selectedRadioButtonLock" value="${1}"/>
                </g:if>
                <g:else>
                    <g:set var="selectedRadioButtonLock" value="${3}"/>
                </g:else>
                <g:radioGroup
                        name="role_status_${((Character) character).DTDId}_${((Plot) ((Role) role).plot).DTDId}_${((Role) role).DTDId}"
                        values="[1, 2, 3]"
                        value="${selectedRadioButtonLock}">
                    <td align="center">
                        ${it.radio}
                    </td>
                </g:radioGroup>
                <td align="center">
                    <g:select style="width: 140px"
                              name="lock_on_${((Character) character).DTDId}_${((Plot) ((Role) role).plot).DTDId}_${((Role) role).DTDId}"
                              id="lock_on_${((Character) character).DTDId}_${((Plot) ((Role) role).plot).DTDId}_${((Role) role).DTDId}"
                              from="${characterListToDropDownLock}"
                              keys="${characterListToDropDownLock}"/>
                </td>
            </tr>
        </g:each>
        <!-- End NJA Work Step0 -->
        </tbody>
    </table>
</div>

<div class="accordion" style="margin-bottom: 0" id="accordionStat${characterIter}">
    <div class="accordion-group">
        <div class="accordion-heading">
            <a class="accordion-toggle" data-toggle="collapse"
               data-parent="#accordionStat${characterIter}"
               href="#collapseStat${characterIter}"><g:message
                    code="selectintrigue.step1.statistics" default="Statistics"/>
            </a>
        </div>

        <div id="collapseStat${characterIter}" class="accordion-body collapse">
            <div class="accordion-inner">
                <table>
                    <thead>
                    <tr>
                        <th></th>
                        <th><g:message code="selectintrigue.step1.stat.result"
                                       default="Result"/></th>
                    </tr>
                    </thead>

                    <tr>
                        <td>
                            <g:message code="roletoperso.nbpip"
                                       default="PIP Number"/>
                        </td>
                        <td>
                            ${character.getNbPIP()}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <g:message code="roletoperso.persoGender"
                                       default="Gender"/>
                        </td>
                        <td>
                            ${character.getGender()}
                        </td>
                    </tr>
                    <g:each in="${character.getTags()}" status="i" var="entryMap">
                        <tr>
                            <td>
                                ${entryMap.getKey()}
                            </td>
                            <td>
                                ${entryMap.getValue()}%
                            </td>
                        </tr>
                    </g:each>
                </table>
            </div>
        </div>
    </div>
</div>

<div class="accordion" id="accordionRelations${characterIter}">
    <div class="accordion-group">
        <div class="accordion-heading">
            <a class="accordion-toggle" data-toggle="collapse"
               data-parent="#accordionRelations${characterIter}"
               href="#collapseRelations${characterIter}"><g:message
                    code="roletoperso.relations" default="Relations"/>
            </a>
        </div>

        <div id="collapseRelations${characterIter}" class="accordion-body collapse">
            <div class="accordion-inner">
                <div style="overflow: auto; max-height:150px;">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th><g:message code="roletoperso.from"
                                           default="From"/></th>
                            <th><g:message code="roletoperso.to"
                                           default="To"/></th>
                            <th><g:message code="selectintrigue.plotName"
                                           default="Plot name"/></th>
                            <th><g:message code="roletoperso.relation"
                                           default="Relation"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${((Character) character).getRelations(false)?.keySet()}" status="roleIter"
                                var="roleHasRelationWithRole">
                            <tr class="${(roleIter % 2) == 0 ? 'even' : 'odd'}">

                                <td>
                                    <g:set var="characterRel"
                                           value="${gnInstance?.getCharacterContainingRole(roleHasRelationWithRole.getterRole1())?.DTDId}"/>

                                    <a href="#Char${characterRel}">P${characterRel}:&#160;${((RoleHasRelationWithRole) roleHasRelationWithRole).getterRole1().code}</a>
                                </td>
                                <td>
                                    <g:set var="characterRel"
                                           value="${gnInstance?.getCharacterContainingRole(roleHasRelationWithRole.getterRole2())?.DTDId}"/>
                                    <a href="#Char${characterRel}">P${characterRel}:&#160;${((RoleHasRelationWithRole) roleHasRelationWithRole).getterRole2().code}</a>
                                </td>
                                <td><g:link controller="redactIntrigue" action="edit"
                                            id="${((Role) ((RoleHasRelationWithRole) roleHasRelationWithRole).getterRole1()).plot?.id}"
                                            target="_blank">
                                    ${((Role) ((RoleHasRelationWithRole) roleHasRelationWithRole).getterRole1()).plot?.name}
                                </g:link></td>
                                <td>
                                    ${((RoleHasRelationWithRole) roleHasRelationWithRole).getterRoleRelationType().name}
                                </td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="accordion" id="accordionBannedRoles${characterIter}">
    <div class="accordion-group">
        <div class="accordion-heading">
            <a class="accordion-toggle" data-toggle="collapse"
               data-parent="#accordionBannedRoles${characterIter}"
               href="#collapseBannedRoles${characterIter}"><g:message
                    code="roletoperso.bannedRoles" default="Banned roles"/>
            </a>
        </div>

        <div id="collapseBannedRoles${characterIter}" class="accordion-body collapse">
            <div class="accordion-inner">
                <div style="overflow: auto; max-height:150px;">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th><g:message code="roletoperso.roleCode"
                                           default="Role code"/></th>
                            <th><g:message code="selectintrigue.plotName"
                                           default="Plot name"/></th>
                            <th width="25"><g:img dir="images/selectIntrigue"
                                                  file="forbidden.png"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${((Character) character).bannedRoles}" status="roleIter"
                                var="role">
                            <tr class="${(roleIter % 2) == 0 ? 'even' : 'odd'}">

                                <td>
                                    <a href="#"
                                       title="${((Role) role).description}">${((Role) role).code}</a>
                                </td>
                                <td><g:link controller="redactIntrigue" action="edit"
                                            id="${((Role) role).plot?.id}" target="_blank">
                                    ${((Role) role).plot?.name}
                                </g:link></td>
                                <td>
                                    <g:checkBox
                                            name="keepRoleBanned_${((Character) character).DTDId}_${((Plot) ((Role) role).plot).DTDId}_${((Role) role).DTDId}"
                                            checked="true"/>
                                </td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

</div>

</div>
<g:if test="${characterIter % 2 == 1}">
    </div>
</g:if>
</g:each>

<g:if test="${characterList.size() % 2 == 0}">
    <div class="row-fluid">
</g:if>
<div class="span6">
    <br/>

    <div class="panel panel-default">
        <div class="accordion" id="accordionAll">
            <div class="accordion-group">
                <div class="accordion-heading">
                    <a class="accordion-toggle" data-toggle="collapse"
                       data-parent="#accordionAll"
                       href="#collapseAll">
                        <g:message code="roletoperso.gestionTPJ"
                                   default="Common roles for all characters"/>
                    </a>
                </div>

                <div id="collapseAll" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <div style="overflow: auto; max-height:150px;">
                            <table class="table table-bordered">
                                <thead>
                                <tr>
                                    <th><g:message code="roletoperso.roleCode"
                                                   default="Role code"/></th>
                                    <th><g:message code="selectintrigue.plotName"
                                                   default="Plot name"/></th>
                                </tr>
                                </thead>
                                <tbody>
                                <g:each in="${allList}" var="allrole">
                                    <tr>
                                        <td>
                                            <a href="#"
                                               title="${((Role) allrole).description}">${((Role) allrole).code}</a>
                                        </td>
                                        <td>
                                            <g:link controller="redactIntrigue" action="edit"
                                                    id="${((Role) allrole).plot?.id}" target="_blank">
                                                ${((Role) allrole).plot?.name}
                                            </g:link>
                                        </td>
                                    </tr>
                                </g:each>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<g:if test="${characterList.size() % 2 == 0}">
    </div>
</g:if>
<br/>

<g:if test="${true}">
    <div class="row-fluid">
        <div class="span12" id="Relations">
            <div class="panel panel-default">
                <div class="panel-heading" style="margin-top: 20px">
                    <g:message code="roletoperso.allRelationsSummary"
                               default="All relations between characters summary"/>
                </div>

                <div style="overflow: auto; height:500px;" id="container">
                    <g:hiddenField id="relationjson" name="relationjson" value="${relationjson}"/>
                    <div id="infovis">
                    </div>
                    <div id="right-container">
                        <div id="inner-details"></div>
                    </div>
                    <g:render template="relationSummary2"></g:render>
                </div>
            </div>
        </div>
    </div>
</g:if>

</div>
<div class="accordion" id="accordionPNJ">
    <div class="accordion-group">
        <div class="accordion-heading">
            <a class="accordion-toggle" data-toggle="collapse"
               data-parent="#accordionPNJ"
               href="#collapsePNJ">
                <g:message code="roletoperso.gestionPHJ"
                           default="Management of offside characters"/>
            </a>
        </div>

        <div id="collapsePNJ" class="accordion-body collapse">
            <div class="accordion-inner">
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th><g:message code="roletoperso.roleCode"
                                       default="Role code"/></th>
                        <th><g:message code="selectintrigue.plotName"
                                       default="Plot name"/></th>
                        <th><g:message code="roletoperso.sexe"
                                       default="Sex"/></th>
                        <th><g:message code="roletoperso.action"
                                       default="Actions"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${PHJList}" var="PHJ">
                        <g:if test="${(((Character) PHJ).isPHJ() == true) || (((Character) PHJ).isPNJ() == true)}">
                            <tr id="${"line" + ((Character) PHJ).getDTDId()}">
                                <td>CHAR-${((Character) PHJ).getDTDId()}</td>
                                <td>"Test"</td>
                                <td>${((Character) PHJ).getGender()}</td>
                                <td>
                                    <a href="#fusionModal" data-id="${((Character) PHJ).getDTDId()}" role="button" class="btn fusion_modale" data-toggle="modal">
                                        <g:message code="roletoperso.fusion" default="Merge"></g:message>
                                    </a>
                            </tr>
                        </g:if>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- Modal -->
<div id="fusionModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="fusionModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="fusionModalLabel">Fusion de Personnages Non Joués et Hors Jeu</h3>
    </div>
    <div class="modal-body">
        <div class="row-fluid">
            <div class="span5">
                <h4 id="modalPHJ1">Personnage 1</h4>
            </div>
            <div class="span2"></div>
            <div class="span5">
                <select id="modaloption" class="form-control">
                    <option value="" disabled selected style='display:none;'>Fusionner avec ...</option>
                    <g:each in="${PHJList}" var="player">
                        <g:if test="${((Character) player).isPHJ() == true}">
                            <option value="${'CHAR-' + ((Character) player).getDTDId()}" id="${'CHAR-' + ((Character) player).getDTDId()}" class="optionschar hidden">CHAR-${((Character) player).getDTDId()}</option>
                        </g:if>
                    </g:each>
                </select>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">Annuler</button>
        <a id="merge" type="button" href="#" class="btn btn-primary" data-gn="${gnInstance?.id}" data-url="${createLink(controller: "RoleToPerso", action: "Merge")}">
            <g:message code="roletoperso.fusion" default="Merge"></g:message>
        </a>
    </div>
</div>

<table class="table table-bordered">
    <thead>
    <tr>
        <th>Character</th>
        <th>Tag1 - ID</th>
        <th>Tag1 - Name</th>
        <th>Tag1 - Value</th>
        <th>Tag2 - ID</th>
        <th>Tag2 - Name</th>
        <th>Tag2 - Value</th>
        <th>Compatibility</th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${tagcompatibility}" var="string">
        <tr>
            <td>CHAR-${((String)string).split("#")[0]}</td>
            <td>${((String)string).split("#")[1]}</td>
            <td>${((String)string).split("#")[2]}</td>
            <td>${((String)string).split("#")[3]}</td>
            <td>${((String)string).split("#")[4]}</td>
            <td>${((String)string).split("#")[5]}</td>
            <td>${((String)string).split("#")[6]}</td>
            <td>${((String)string).split("#")[7]}</td>
        </tr>
    </g:each>
    </tbody>
</table>

<g:hiddenField name="selectedEvenemential" class="selectedEvenemential" value="${evenementialId}"/>
<g:hiddenField name="selectedMainstream" class="selectedMainstream" value="${mainstreamId}"/>

<g:actionSubmit class="btn btn-primary" action="roleToPerso"
                value="${message(code: 'selectintrigue.step1.reload', default: 'Reload')}"/>
</g:form>

<g:form method="post" controller="substitution">
    <g:hiddenField name="gnId" value="${gnInstance?.id}"/>
    <div class="form-actions">
        <g:actionSubmit class="btn btn-primary" action="index"
                        value="${message(code: 'navbar.substitution', default: 'Substitution')}"/>
    </div>
</g:form>

<script>
    $(".fusion_modale").click(function() {
        var id_p1 = $(this).attr("data-id");
        $("#modalPHJ1").html("CHAR-" + id_p1);
        var selected = false;
        var option_name = "#CHAR-" + id_p1;
        $(".optionschar").each(function() {
            if (($(this).attr("id") == option_name) && (selected == false))
            {
                selected = true;
                $("#modaloption").val($(this).attr("value"));
            }
            $(this).removeClass("hidden");
        });
        $(option_name).addClass("hidden");
        $("#merge").attr("data-id", $(this).attr("data-id"));
    });

    $("#merge").click(function() {
        var from_char = $(this).attr("data-id");
        var opt = document.getElementById("modaloption");
        var selected_char = opt[opt.selectedIndex].value;
        var gn_id = $(this).attr("data-gn");
        $.ajax({
            type: 'POST',
            url: $("#merge").attr("data-url"),
            dataType: "json",
            data: {char_to: selected_char, char_from: from_char, gn_id: gn_id},
            timeout: 3000,
            success: function(data) {
                console.log('ok');
                var remove_id = "line" + data.object.test;
                $("tr#"+remove_id).remove();
                $("#fusionModal").modal('hide');
            },
            error: function() {
                alert('La requête n\'a pas abouti'); }
        });
    });
</script>
