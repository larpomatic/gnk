<%@ page import="org.gnk.gn.Gn; org.gnk.selectintrigue.Plot" %>
<%@ page import="org.gnk.admin.right" contentType="text/html;charset=UTF-8" %>

<link rel="stylesheet" href="${resource(dir: 'css', file: 'step1_result.css')}" type="text/css">

<g:form method="post">
    <g:hiddenField name="id" value="${gnInstance?.id}"/>
    <g:hiddenField name="gnDTD" value="${gnInstance?.dtd}"/>
    <g:hiddenField name="version" value="${gnInstance?.version}"/>
    <g:hiddenField name="screenStep" value="1"/>
    <table class="table table-bordered">
        <thead>
    <tr>
        <th><g:message code="selectintrigue.plotName"
                       default="Plot name"/></th>
        <th><g:img dir="images/selectIntrigue"
                   file="locked.png"/></th>
        <th><g:img dir="images/selectIntrigue"
                   file="forbidden.png"/></th>
        <th><g:img dir="images/selectIntrigue"
                   file="validate.png"/></th>
    </tr>
    </thead>
        <tbody>

        <g:each in="${plotInstanceList}" status="i" var="plotInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                <td>
                    <g:link controller="redactIntrigue" action="edit" id="${plotInstance.id}" target="_blank">
                        ${fieldValue(bean: plotInstance, field: "name")}
                    </g:link>
                </td>
                <g:radioGroup name="plot_status_${plotInstance.id}" onclick="saveRadioBouton()" values="[1, 2, 3]"
                              value="${((Gn) gnInstance).getLockedPlotSet()?.contains(plotInstance) ? "1" : (((Gn) gnInstance).getBannedPlotSet()?.contains(plotInstance) ? "2" : "3")}">
                    <td>
                        ${it.radio}
                    </td>
                </g:radioGroup>
            </tr>
        </g:each>
        </tbody>
    </table>
%{--Evenemential table--}%
    <table class="table table-bordered evenemential-table">
        <thead>
        <tr>
            <th><g:message code="selectintrigue.evenementialPlotName"
                           default="Evenemential plot name"/></th>
            <th><g:img dir="images/selectIntrigue"
                       file="validate.png"/></th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${evenementialPlotInstanceList}" status="i" var="evenementialPlotInstance">
            <tr>
                <td>
                    <g:link controller="redactIntrigue" action="edit"
                            id="${evenementialPlotInstance.id}" target="_blank">
                        ${fieldValue(bean: evenementialPlotInstance, field: "name")}
                    </g:link>
                </td>
                <td>
                    <g:if test="${evenementialPlotInstance.id.equals(evenementialId)}">
                        <g:radio name="selected_evenemential" onclick="saveRadioBouton()" checked="true" value="${evenementialPlotInstance.id}"
                                 class="radioEvenemential"/>
                    </g:if>
                    <g:else>
                        <g:radio name="selected_evenemential" onclick="saveRadioBouton()" value="${evenementialPlotInstance.id}"
                                 class="radioEvenemential"/>
                    </g:else>
                </td>
            </tr>
        </g:each>
        <tr>
            <td colspan="2">
                <button type="button" class="moreEvenemential btn btn-primary">
                    <g:message code="selectintrigue.step1.moreEvenemential" default="Display more evenementials plots"/>
                </button>
            </td>
        </tr>
        </tbody>
    </table>

    <g:if test="${gnInstance.isMainstream}">
    %{--Mainstream table--}%
        <table class="table table-bordered mainstream-table">
            <thead>
            <tr>
                <th><g:message code="selectintrigue.plotName"
                               default="Plot name"/></th>
                <th><g:img dir="images/selectIntrigue"
                           file="locked.png"/></th>
                <th><g:img dir="images/selectIntrigue"
                           file="forbidden.png"/></th>
                <th><g:img dir="images/selectIntrigue"
                           file="validate.png"/></th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${mainstreamPlotInstanceList}" status="i" var="mainstreamPlotInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                    <td>
                        <g:link controller="redactIntrigue" action="edit"
                            id="${mainstreamPlotInstance.id}" target="_blank">
                            ${fieldValue(bean: mainstreamPlotInstance, field: "name")}
                        </g:link>
                    </td>
                    <g:radioGroup name="selected_mainstream${mainstreamPlotInstance.id}" onclick="saveRadioBouton()" values="[1, 2, 3]"
                                  value="${((Gn) gnInstance).getLockedPlotSet()?.contains(mainstreamPlotInstance) ? "1" : (((Gn) gnInstance).getBannedPlotSet()?.contains(mainstreamPlotInstance) ? "2" : "3")}">
                        <td>
                            ${it.radio}
                        </td>
                    </g:radioGroup>
                </tr>
            </g:each>
            <tr>
                <td colspan="4">
                    <button type="button" class="moreMainstream btn btn-primary">
                        <g:message code="selectintrigue.step1.moreMainstream" default="Display more mainstreams plots"/>
                    </button>
                </td>
            </tr>
            </tbody>
        </table>
    </g:if>
    <!-- Banned -->
    <table class="table table-bordered">
        <thead>
        <tr>
            <th><g:message code="selectintrigue.bannedPlotName"
                           default="Plot name"/></th>
            <th><g:img dir="images/selectIntrigue"
                       file="forbidden.png"/></th>

        </tr>
        </thead>
        <tbody>
        <g:each in="${bannedPlotInstanceList}" status="i" var="plotInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link controller="redactIntrigue" action="edit"
                            id="${plotInstance.id}" target="_blank">
                    ${fieldValue(bean: plotInstance, field: "name")}
                </g:link></td>
                <td>
                    <g:checkBox name="keepBanned_${plotInstance.id}" onclick="saveRadioBouton()" checked="true"/>
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>

    <!-- Banned -->
    <div class="accordion" id="accordion3">
        <div class="accordion-group">
            <div class="accordion-heading">
                <a class="accordion-toggle" data-toggle="collapse"
                   data-parent="#accordion3" href="#collapseTwo"><g:message
                        code="selectintrigue.step1.unselectedplots" default="Unselected Plots"/>
                </a>
            </div>

            <div id="collapseTwo" class="accordion-body collapse">
                <div class="accordion-inner">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th><g:message code="selectintrigue.plotName"
                                           default="Plot name"/></th>
                            <th><g:img dir="images/selectIntrigue"
                                       file="locked.png"/></th>

                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${nonTreatedPlots}" status="i" var="plotInstance">
                            <g:if test="${!plotInstance.isEvenemential}">
                                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                    <td><g:link controller="redactIntrigue" action="edit"
                                                id="${plotInstance.id}" target="_blank">
                                        ${fieldValue(bean: plotInstance, field: "name")}
                                    </g:link></td>
                                    <td>
                                        <g:checkBox name="toLock_${plotInstance.id}" checked="false"/>
                                    </td>
                                </tr>
                            </g:if>
                        </g:each>
                        </tbody>
                    </table>

                </div>
            </div>
            <br/>

            <div class="accordion" id="accordion2">
                <div class="accordion-group">
                    <div class="accordion-heading">
                        <a class="accordion-toggle" data-toggle="collapse"
                           data-parent="#accordion2" href="#collapseOne"><g:message
                                code="selectintrigue.step1.statistics" default="Statistics"/>
                        </a>
                    </div>

                    <div id="collapseOne" class="accordion-body collapse">
                        <div class="accordion-inner">
                            <table>
                                <thead>
                                <tr>
                                    <th></th>
                                    <th><g:message code="selectintrigue.step1.stat.objective"
                                                   default="Objective"/></th>
                                    <th><g:message code="selectintrigue.step1.stat.result"
                                                   default="Result"/></th>
                                </tr>
                                </thead>
                                <g:each in="${statisticResultList}" status="i" var="value">
                                    <tr>
                                        <td>
                                            ${value.get(0)}
                                        </td>
                                        <td>
                                            ${value.get(1)}
                                        </td>
                                        <td>
                                            ${value.get(2)}
                                        </td>
                                    </tr>
                                </g:each>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <fieldset class="buttons">
%{--
        <g:hasRights lvlright="${right.MGNMODIFY.value()}">
            <g:actionSubmit class="btn btn-primary" action="selectIntrigue"
                            value=" ${message(code: 'selectintrigue.step1.reload', default: 'Reload')}"/>
            <g:actionSubmit class="btn btn-primary" action="goToRoleToPerso"
                            value="${message(code: 'navbar.role2perso', default: 'Role2Perso')}"/>
        </g:hasRights>
--}%
    </fieldset>
    %{--<button type="button" class="btn btn-primary btn-circle btn-xl"><i class="glyphicon glyphicon-refresh"></i></button>--}%
    %{--<button type="button" class="btn btn-primary btn-circle btn-xl"><i class="glyphicon glyphicon-chevron-right"></i></button>--}%
</g:form>