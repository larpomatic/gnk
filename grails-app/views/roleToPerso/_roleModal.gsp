<%@ page import="groovy.json.JsonBuilder; org.codehaus.groovy.grails.web.json.JSONObject; org.gnk.roletoperso.RoleHasRelationWithRole; org.gnk.roletoperso.Role" %>

<div id="roleModal-${((Role) role).getDTDId()}" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="roleModalLabel-${((Role) role).getDTDId()}" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="roleModalLabel-${((Role) role).getDTDId()}">Détails du rôle</h3>
    </div>
    <div class="modal-body">
        <div class="row-fluid">
            <table class="table table-striped" style="border-bottom: 1pt solid lightgrey; border-top: 1pt solid lightgrey; border-left: 1pt solid lightgrey; border-right: 1pt solid lightgrey">
                <tbody>
                <tr>
                    <td>Intrigue :</td>
                    <td>${role.plot.name}</td>
                </tr>
                <tr>
                    <td>Nom :</td>
                    <td>${role.code}</td>
                </tr>
                <tr>
                    <td>Type :</td>
                    <td>${role.type}</td>
                </tr>
                <tr>
                    <td>PIP :</td>
                    <td>${role.pip}</td>
                </tr>
                <tr>
                    <td>Description :</td>
                    <td>${role.description}</td>
                </tr>
                <tr>
                    <td>Tags :</td>
                    <td>
                        <g:each in="${role.getterRoleHasTag()}" var="roleHasTag">
                            ${roleHasTag.tag.name} (${roleHasTag.weight})<br>
                        </g:each>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">Fermer</button>
    </div>
</div>
