<%@ page import="org.gnk.roletoperso.Role" %>
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
    %{--<g:message code="roletoperso.result"--}%
    %{--default="RoleToPerso result"/>--}%
    Resultat life
</h3>

<div class="container-fluid">
    <g:hiddenField name="gnId" value="${gnInstance?.id}"/>
    <g:hiddenField name="gnDTD" value="${gnInstance?.dtd}"/>
    <g:hiddenField name="version" value="${gnInstance?.version}"/>

    <g:each in="${characterList}" status="characterIter" var="character">

        <g:if test="${characterIter % 2 == 0}">
            <div class="row-fluid">
        </g:if>
        <div class="span6" id="Char${character.DTDId}">
            <div class="panel panel-default">
                <div class="panel-heading" style="margin-top: 20px">
                    <g:message code="roletoperso.character"
                               default="Character"/> ${character.DTDId}
                    <div class="pull-right" style="margin-top: -5px">
                        <g:if test="${((Character) character).getGender() == 'M'}">
                            <label>Homme</label>
                        </g:if>
                        <g:elseif test="${((Character) character).getGender() == 'F'}">
                            <label>Femme</label>
                        </g:elseif>
                        <g:else>
                            <label>Neutre</label>
                        </g:else>
                    </div>
                </div>

                <div style="overflow: auto; max-height:150px;">
                    %{--<table class="table table-bordered-no-top">--}%
                    <table class="table table-bordered-no-top">
                        <thead>
                        <tr>
                            <th>Nom de l'intrigue</th>
                            <th>Age</th>
                            <th width="25"><g:img dir="images/selectIntrigue"
                                                  file="locked.png"/></th>
                            <th width="25"><g:img dir="images/selectIntrigue"
                                                  file="forbidden.png"/></th>
                            <th width="25"><g:img dir="images/selectIntrigue"
                                                  file="validate.png"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${((Character) character).specificRoles}" var="role">
                            <g:each in="${((Role) role).roleHasPastscenes}" status="roleIter" var="roleHasPastscenes">
                                <g:if test="${((Role) role)?.code == "Life"}">
                                    <tr class="${(roleIter % 2) == 0 ? 'even' : 'odd'}">
                                        <td>
                                            ${roleHasPastscenes.title}
                                        </td>
                                        <td>
                                            ${roleHasPastscenes.description}
                                        </td>
                                        <g:if test="${((Character) character).roleIsLocked(((Role) role))}">
                                            <g:set var="selectedRadioButtonLock" value="${1}"/>
                                        </g:if>
                                        <g:else>
                                            <g:set var="selectedRadioButtonLock" value="${3}"/>
                                        </g:else>
                                        <g:radioGroup
                                                name="role_status_${((Character) character).DTDId}_${((Role) role).DTDId}_${roleIter}"
                                                values="[1, 2, 3]"
                                                value="${selectedRadioButtonLock}">
                                            <td align="center">
                                                ${it.radio}
                                            </td>
                                        </g:radioGroup>
                                    </tr>
                                </g:if>
                            </g:each>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
        <g:if test="${characterIter % 2 == 1}">
            </div>
        </g:if>
    </g:each>

</div>
<div class="form-actions">
            <div class="form-inline">

<g:form>
        <div class="span1">
            <g:hiddenField name="gnId" class="selectedMainstream" value="${gnInstance.id}"/>

            <g:actionSubmit class="btn btn-primary" action="life" controller="life"
                            value="${message(code: 'selectintrigue.step1.reload', default: 'Reload')}"/>
        </div>
    </g:form>
    %{--<div class="span1">--}%
        <g:form method="post" controller="substitution">
            <g:each in="${characterList}" var="PHJ">
                <g:hiddenField id="${"sexe_" + ((Character) PHJ).getDTDId()}" name="sexe" value="NO"/>
            </g:each>
            <g:each in="${PHJList}" var="PHJ">
                <g:hiddenField id="${"sexe_" + ((Character) PHJ).getDTDId()}" name="sexe" value="NO"/>
            </g:each>
            <g:hiddenField name="gnId" value="${gnInstance?.id}"/>
            <div class="span1">
                <g:actionSubmit class="btn btn-primary" action="index"
                                value="${message(code: 'navbar.substitution', default: 'Substitution')}"/>
            </div>
        </g:form>
    </div>
</div>