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
<g:form method="post" controller="life">

    <g:hiddenField name="tagFirst" value="true"/>
    <g:hiddenField name="gnId" value="${gnInstance?.id}"/>
    <g:hiddenField name="gnDTD" value="${gnInstance?.dtd}"/>
    <g:hiddenField name="version" value="${gnInstance?.version}"/>

    <g:each in="${characterListLife}" status="characterIter" var="character">

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
                            <label>Homme ${((Character) character).age} ans</label>
                        </g:if>
                        <g:elseif test="${((Character) character).getGender() == 'F'}">
                            <label>Femme ${((Character) character).age} ans</label>
                        </g:elseif>
                        <g:else>
                            <label>Neutre ${((Character) character).age} ans</label>
                        </g:else>
                    </div>
                </div>

                <div style="overflow: auto; max-height:150px;">
                    %{--<table class="table table-bordered-no-top">--}%
                    <table class="table table-bordered-no-top" id="listTable${characterIter}">
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
                        <g:each in="${((Character) character).bannedRoles}" var="role">
                            <g:each in="${((Role) role).roleHasPastscenes}" status="roleIter" var="roleHasPastscenes">
                                <g:if test="${((Role) role)?.code.startsWith("Life")}">
                                    <tr class="${(roleIter % 2) == 0 ? 'even' : 'odd'}">
                                        <td>
                                            <a href="#" data-toggle="tooltip" data-placement="top"
                                               title="${roleHasPastscenes.pastscene.description}!">${roleHasPastscenes.title}</a>
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
                                                name="Life_${character.DTDId}_${roleHasPastscenes.roleId}_${roleIter}_${roleHasPastscenes.title}_${roleHasPastscenes.description}"
                                                values="[1, 2, 3]"
                                                value="2">
                                            <td align="center">
                                                ${it.radio}
                                            </td>
                                        </g:radioGroup>
                                    </tr>
                                </g:if>
                            </g:each>
                        </g:each>
                        <g:each in="${((Character) character).lockedRoles}" var="role">
                            <g:each in="${((Role) role).roleHasPastscenes}" status="roleIter" var="roleHasPastscenes">
                                <g:if test="${((Role) role)?.code.startsWith("Life")}">
                                    <tr class="${(roleIter % 2) == 0 ? 'even' : 'odd'}">
                                        <td>
                                            <a href="#" data-toggle="tooltip" data-placement="top"
                                               title="${roleHasPastscenes.pastscene.description}!">${roleHasPastscenes.title}</a>
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
                                                name="Life_${character.DTDId}_${roleHasPastscenes.roleId}_${roleIter}_${roleHasPastscenes.title}_${roleHasPastscenes.description}"
                                                values="[1, 2, 3]"
                                                value="1">
                                            <td align="center">
                                                ${it.radio}
                                            </td>
                                        </g:radioGroup>
                                    </tr>
                                </g:if>
                            </g:each>
                        </g:each>
                        <g:each in="${((Character) character).specificRoles}" var="role">
                            <g:each in="${((Role) role).roleHasPastscenes}" status="roleIter" var="roleHasPastscenes">
                                <g:if test="${((Role) role)?.code.startsWith("Life")}">
                                    <tr class="${(roleIter % 2) == 0 ? 'even' : 'odd'}">
                                        <td>
                                            <a href="#" data-toggle="tooltip" data-placement="top"
                                               title="${roleHasPastscenes.pastscene.description}!">${roleHasPastscenes.title}</a>
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
                                                name="Life_${character.DTDId}_${roleHasPastscenes.roleId}_${roleIter}_${roleHasPastscenes.title}_${roleHasPastscenes.description}"
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
    <div class="form-inline">
%{--<g:form method="post" controller="life">--}%
    <div class="form-actions">

    <div class="span1">
%{--<g:hiddenField name="gnId" value="${gnInstance.id}"/>--}%

    <g:actionSubmit class="btn btn-primary" action="life"
                    value="${message(code: 'selectintrigue.step1.reload', default: 'Reload')}"/>
</g:form>

</div>
%{----}%
%{--<div class="form-actions">--}%
%{--<div class="span1">--}%
<g:form method="post" controller="substitution">
    <g:each in="${characterListLife}" var="PHJ">
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

%{--</div>--}%

%{--<script>--}%
%{--function sortData(){--}%
%{--// Read table body node.--}%
%{--var tableData = document.getElementById('data_table').getElementsByTagName('tbody').item(0);--}%

%{--// Read table row nodes.--}%
%{--var rowData = tableData.getElementsByTagName('tr');--}%

%{--for(var i = 0; i < rowData.length - 1; i++){--}%
%{--for(var j = 0; j < rowData.length - (i + 1); j++{--}%

%{--//Swap row nodes if short condition matches--}%
%{--if(parseInt(rowData.item(j).getElementsByTagName('td').item(0).innerHTML) > parseInt(rowData.item(j+1).getElementsByTagName('td').item(0).innerHTML)){--}%
%{--tableData.insertBefore(rowData.item(j+1),rowData.item(j));--}%
%{--}--}%
%{--}--}%
%{--}--}%
%{--}--}%
%{--// Table data sorting ends....--}%
%{--</script>--}%

<script type="application/javascript">
    //    $(function(){
    //        $("#listTable1").DataTable( {
    //            "order": [[ 1, "asc" ]],
    //            "info":     false,
    //            "paging" : false,
    //            "sDom": '<"top"i>rt<"bottom"flp><"clear">'
    //        } );
    //    });
    //    $(function(){
    //        $("#listTable2").DataTable( {
    //            "order": [[ 1, "asc" ]],
    //            "info":     false,
    //            "paging" : false,
    //            "sDom": '<"top"i>rt<"bottom"flp><"clear">'
    //        } );
    //    });
    //    $(function(){
    //        $("#listTable3").DataTable( {
    //            "order": [[ 1, "asc" ]],
    //            "info":     false,
    //            "paging" : false,
    //            "sDom": '<"top"i>rt<"bottom"flp><"clear">'
    //        } );
    //    });
    //    $(function(){
    //        $("#listTable4").DataTable( {
    //            "order": [[ 1, "asc" ]],
    //            "info":     false,
    //            "paging" : false,
    //            "sDom": '<"top"i>rt<"bottom"flp><"clear">'
    //        } );
    //    });
    //    $(function(){
    //        $("#listTable5").DataTable( {
    //            "order": [[ 1, "asc" ]],
    //            "info":     false,
    //            "paging" : false,
    //            "sDom": '<"top"i>rt<"bottom"flp><"clear">'
    //        } );
    //    });
    //    $(function(){
    //        $("#listTable6").DataTable( {
    //            "order": [[ 1, "asc" ]],
    //            "info":     false,
    //            "paging" : false,
    //            "sDom": '<"top"i>rt<"bottom"flp><"clear">'
    //        } );
    //    });
    //    $(function(){
    //        $("#listTable7").DataTable( {
    //            "order": [[ 1, "asc" ]],
    //            "info":     false,
    //            "paging" : false,
    //            "sDom": '<"top"i>rt<"bottom"flp><"clear">'
    //        } );
    //    });
    //    $(function(){
    //        $("#listTable8").DataTable( {
    //            "order": [[ 1, "asc" ]],
    //            "info":     false,
    //            "paging" : false,
    //            "sDom": '<"top"i>rt<"bottom"flp><"clear">'
    //        } );
    //    });
    //    $(function(){
    //        $("#listTable9").DataTable( {
    //            "order": [[ 1, "asc" ]],
    //            "info":     false,
    //            "paging" : false,
    //            "sDom": '<"top"i>rt<"bottom"flp><"clear">'
    //        } );
    //    });
    //    $(function(){
    //        $("#listTable10").DataTable( {
    //            "order": [[ 1, "asc" ]],
    //            "info":     false,
    //            "paging" : false,
    //            "sDom": '<"top"i>rt<"bottom"flp><"clear">'
    //        } );
    //    });
</script>