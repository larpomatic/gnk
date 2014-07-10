%{--<%@ page import="org.gnk.selectintrigue.Plot" %>--}%

%{--<style type="text/css">--}%
%{--th, td {--}%
    %{--padding: 0.2em;--}%
    %{--margin: 0.1em;--}%
%{--}--}%

%{--.tab-pane.active {--}%
    %{--height: 350pt;--}%
%{--}--}%
%{--</style>--}%

%{--<!-- <div class="fieldcontain {hasErrors(bean: plotInstance, field: 'name', 'error')} ">--}%
	%{--<label for="name">--}%
%{--<g:message code="plot.name.label" default="Name"/>--}%

%{--</label>--}%
%{--<g:textField name="name" maxlength="45" value="${plotInstance?.name}"/>--}%
%{--</div>-->--}%

%{--<!-- <div class="fieldcontain {hasErrors(bean: plotInstance, field: 'events', 'error')} ">--}%
	%{--<label for="events">--}%
%{--<g:message code="plot.events.label" default="Events"/>--}%

%{--</label>--}%

%{--<ul class="one-to-many">--}%
%{--<g:each in="{plotInstance?.events?}" var="e">--}%
    %{--<li><g:link controller="event" action="show" id="{e.id}">${e?.encodeAsHTML()}</g:link></li>--}%
%{--</g:each>--}%
%{--<li class="add">--}%
%{--<g:link controller="event" action="create"--}%
        %{--params="['plot.id': plotInstance?.id]">{message(code: 'default.add.label', args: [message(code: 'event.label', default: 'Event')])}</g:link>--}%
%{--</li>--}%
%{--</ul>--}%

%{--</div>-->--}%

%{--<div class="tabbable">--}%
%{--<!-- Only required for left/right tabs -->--}%
%{--<ul class="nav nav-tabs">--}%
    %{--<li class="active"><a href="#generalDescription"--}%
                          %{--data-toggle="tab"><g:message--}%
                %{--code="redactintrigue.tabs.generalDescription" args="${[]}"/></a></li>--}%
    %{--<li><a href="#roles" data-toggle="tab"><g:message--}%
            %{--code="redactintrigue.tabs.roles" args="${[]}"/></a></li>--}%
    %{--<li><a href="#relations" data-toggle="tab"><g:message--}%
            %{--code="redactintrigue.tabs.relations" args="${[]}"/></a></li>--}%
    %{--<li><a href="#places" data-toggle="tab"><g:message--}%
            %{--code="redactintrigue.tabs.places" args="${[]}"/></a></li>--}%
    %{--<li><a href="#objects" data-toggle="tab"><g:message--}%
            %{--code="redactintrigue.tabs.objects" args="${[]}"/></a></li>--}%
    %{--<li><a href="#textualClues" data-toggle="tab"><g:message--}%
            %{--code="redactintrigue.tabs.textualClues" args="${[]}"/></a></li>--}%
    %{--<li><a href="#pastScenes" data-toggle="tab"><g:message--}%
            %{--code="redactintrigue.tabs.pastScenes" args="${[]}"/></a></li>--}%
    %{--<li><a href="#events" data-toggle="tab"><g:message--}%
            %{--code="redactintrigue.tabs.events" args="${[]}"/></a></li>--}%
%{--</ul>--}%

%{--<div class="tab-content">--}%
    %{--<div class="tab-pane active" id="generalDescription">--}%
        %{--<table>--}%
            %{--<tr>--}%
                %{--<td><label for="plotName"><g:message--}%
                        %{--code="redactintrigue.generalDescription.plotName"--}%
                        %{--default="Plot's Name"/>--}%
                %{--</label></td>--}%
                %{--<td><g:textField name="name" value="${plotInstance?.name}"--}%
                                 %{--required=""/>--}%
    %{--</div></td>--}%
    %{--<td><label for="plotUnivers"><g:message--}%
            %{--code="redactintrigue.generalDescription.plotUnivers"--}%
            %{--default="Universes"/>--}%
    %{--</label></td>--}%
    %{--<td><a href="#universesModal" role="button" class="btn"--}%
           %{--data-toggle="modal">Choisir les univers</a></td>--}%
%{--</tr>--}%
    %{--<tr>--}%
        %{--<td><label for="isPublic"><g:message--}%
                %{--code="redactintrigue.generalDescription.isPublic"--}%
                %{--default="Public"/>--}%
        %{--</label></td>--}%
        %{--<td><g:checkBox id="isPublic" name="isPublic"--}%
                        %{--checked="${plotInstance.isPublic}"/></td>--}%
        %{--<td><label for="tags"><g:message--}%
                %{--code="redactintrigue.generalDescription.tags" default="Tags"/>--}%
        %{--</label></td>--}%

        %{--<td><a href="#tagsModal" role="button" class="btn"--}%
               %{--data-toggle="modal">Choisir les tags</a></td>--}%
    %{--</tr>--}%
    %{--<tr>--}%
        %{--<td><label for="isMainstream"><g:message--}%
                %{--code="redactintrigue.generalDescription.isMainstream"--}%
                %{--default="Mainstream"/>--}%
        %{--</label></td>--}%
        %{--<td><g:checkBox name="isMainstream" id="isMainstream"--}%
                        %{--checked="${plotInstance.isMainstream}"/></td>--}%
        %{--<td><label for="isEvenemential"><g:message--}%
                %{--code="redactintrigue.generalDescription.isEvenemential"--}%
                %{--default="Evenemential"/>--}%
        %{--</label></td>--}%
        %{--<td><g:checkBox name="isEvenemential" id="isEvenemential"--}%
                        %{--checked="${plotInstance.isEvenemential}"/></td>--}%
    %{--</tr>--}%
    %{--<tr>--}%
        %{--<td colspan="4"><label for="plotDescription"><g:message--}%
                %{--code="redactintrigue.generalDescription.plotDescription"--}%
                %{--default="Plot Description"/>--}%
        %{--</label></td>--}%
    %{--</tr>--}%
    %{--<tr>--}%
        %{--<td colspan="4"><g:textArea name="plotDescription"--}%
                                    %{--id="plotDescription" value="${plotInstance.description}" rows="5"--}%
                                    %{--cols="100"/></td>--}%
    %{--</tr>--}%
%{--</table>--}%
%{--</div>--}%

%{--<div class="tab-pane" id="roles">--}%
    %{--<div class="tabbable tabs-left">--}%
        %{--<ul class="nav nav-tabs">--}%
            %{--<li class="active"><a href="#newRole" data-toggle="tab">Ajouter--}%
            %{--un nouveau rôle</a></li>--}%
            %{--<g:each in="${plotInstance.roles}" status="i" var="role">--}%
                %{--<li class=""><a href="#role${role.id}" data-toggle="tab">--}%
                    %{--${role.code}--}%
                %{--</a></li>--}%
            %{--</g:each>--}%
        %{--</ul>--}%

        %{--<div class="tab-content">--}%
            %{--<div class="tab-pane active" id="newRole">--}%
            %{--<g:formRemote name="myForm" update="updateMe"--}%
                          %{--url="[controller: 'role', action: 'save']"></g:formRemote><!-- the magic thing which makes it work -->--}%
                %{--<g:formRemote name="myForm" update="updateMe" url="[controller: 'role', action: 'save']"--}%
                              %{--onSuccess="alert('Rôle correctement mis à jour !')" onFailing="alert('Erreur !')">--}%
                    %{--<input type="hidden" name="plotId" value="${plotInstance?.id}"/>--}%
                    %{--<table>--}%
                        %{--<tr>--}%
                            %{--<td><label for="roleCode"><g:message--}%
                                    %{--code="redactintrigue.role.roleCode" default="Role code"/>--}%
                            %{--</label></td>--}%
                            %{--<td colspan="2"><g:textField name="roleCode"--}%
                                                         %{--id="roleCode" value="" required=""/></td>--}%
                        %{--</tr>--}%
                        %{--<tr>--}%
                            %{--<td><label for="roleTags"><g:message--}%
                                    %{--code="redactintrigue.generalDescription.tags"--}%
                                    %{--default="Tags"/>--}%
                            %{--</label></td>--}%
                            %{--<td><a href="#roleTagsModal" role="button" class="btn"--}%
                                   %{--data-toggle="modal">Choisir les tags</a></td>--}%
                            %{--<td><label for="roleType"><g:message--}%
                                    %{--code="redactintrigue.role.roleType" default="Type"/>--}%
                            %{--</label></td>--}%
                            %{--<td><g:textField name="roleType" id="roleType" value=""--}%
                                             %{--required=""/></td>--}%
                        %{--</tr>--}%
                        %{--<tr>--}%
                            %{--<td><label for="rolePipi"><g:message--}%
                                    %{--code="redactintrigue.role.rolePipi" default="PIPI"/>--}%
                            %{--</label></td>--}%
                            %{--<td><g:textField name="rolePipi" id="rolePipi" value=""--}%
                                             %{--required=""/></td>--}%
                            %{--<td><label for="rolePipr"><g:message--}%
                                    %{--code="redactintrigue.role.rolePipr" default="PIPR"/>--}%
                            %{--</label></td>--}%
                            %{--<td><g:textField name="rolePipr" id="rolePipr" value=""--}%
                                             %{--required=""/></td>--}%
                        %{--</tr>--}%
                        %{--<tr>--}%
                            %{--<td colspan="4"><label for="roleDescription"><g:message--}%
                                    %{--code="redactintrigue.role.roleDescription"--}%
                                    %{--default="Description"/>--}%
                            %{--</label></td>--}%
                        %{--</tr>--}%
                        %{--<tr>--}%
                            %{--<td colspan="4"><g:textArea name="roleDescription"--}%
                                                        %{--id="roleDescription" value="" rows="5" cols="100"/></td>--}%
                        %{--</tr>--}%
                    %{--</table>--}%

                    %{--<div id="roleTagsModal" class="modal hide fade" tabindex="-1">--}%
                        %{--<div class="modal-header">--}%
                            %{--<button type="button" class="close" data-dismiss="modal">×</button>--}%
                            %{--<h3>Tags</h3>--}%
                        %{--</div>--}%

                        %{--<div class="modal-body">--}%

                            %{--<table>--}%
                                %{--<g:each in="${roleTagList}" status="i" var="roleTagInstance">--}%
                                    %{--<g:if test="${i % 3 == 0}">--}%
                                        %{--<tr>--}%
                                    %{--</g:if>--}%
                                    %{--<td><label for="roleTags_${roleTagInstance.id}"><g:checkBox--}%
                                            %{--name="roleTags_${roleTagInstance.id}"--}%
                                            %{--id="roleTags_${roleTagInstance.id}"--}%
                                            %{--checked="false"/> ${fieldValue(bean: roleTagInstance, field: "tag")}</label>--}%
                                    %{--</td>--}%
                                    %{--<g:if test="${(i + 1) % 3 == 0}">--}%
                                        %{--</tr>--}%
                                    %{--</g:if>--}%
                                %{--</g:each>--}%

                            %{--</table>--}%
                        %{--</div>--}%

                        %{--<div class="modal-footer">--}%
                            %{--<button class="btn" data-dismiss="modal" aria-hidden="true">Ok</button>--}%
                        %{--</div>--}%
                    %{--</div>--}%
                    %{--<g:submitButton name="Update" value="Update"/>--}%
                %{--</g:formRemote>--}%
            %{--</div>--}%
            %{--<g:each in="${plotInstance.roles}" status="i" var="role">--}%
                %{--<div class="tab-pane" id="role${role.id}">--}%

                    %{--<g:formRemote name="myForm"--}%
                                  %{--update="updateMe" url="[controller: 'role', action: 'save']"--}%
                                  %{--onSuccess="alert('Rôle correctement mis à jour !')" onFailing="alert('Erreur !')">--}%
                        %{--<input type="hidden" name="plotId" value="${plotInstance?.id}"/>--}%
                        %{--<table>--}%
                            %{--<tr>--}%
                                %{--<td><label for="roleCode"><g:message--}%
                                        %{--code="redactintrigue.role.roleCode" default="Role code"/>--}%
                                %{--</label></td>--}%
                                %{--<td colspan="2"><g:textField name="roleCode"--}%
                                                             %{--id="roleCode" value="" required=""/></td>--}%
                            %{--</tr>--}%
                            %{--<tr>--}%
                                %{--<td><label for="roleTags"><g:message--}%
                                        %{--code="redactintrigue.generalDescription.tags"--}%
                                        %{--default="Tags"/>--}%
                                %{--</label></td>--}%
                                %{--<td><a href="#roleTagsModal" role="button" class="btn"--}%
                                       %{--data-toggle="modal">Choisir les tags</a></td>--}%
                                %{--<td><label for="roleType"><g:message--}%
                                        %{--code="redactintrigue.role.roleType" default="Type"/>--}%
                                %{--</label></td>--}%
                                %{--<td><g:textField name="roleType" id="roleType" value=""--}%
                                                 %{--required=""/></td>--}%
                            %{--</tr>--}%
                            %{--<tr>--}%
                                %{--<td><label for="rolePipi"><g:message--}%
                                        %{--code="redactintrigue.role.rolePipi" default="PIPI"/>--}%
                                %{--</label></td>--}%
                                %{--<td><g:textField name="rolePipi" id="rolePipi" value=""--}%
                                                 %{--required=""/></td>--}%
                                %{--<td><label for="rolePipr"><g:message--}%
                                        %{--code="redactintrigue.role.rolePipr" default="PIPR"/>--}%
                                %{--</label></td>--}%
                                %{--<td><g:textField name="rolePipr" id="rolePipr" value=""--}%
                                                 %{--required=""/></td>--}%
                            %{--</tr>--}%
                            %{--<tr>--}%
                                %{--<td colspan="4"><label for="roleDescription"><g:message--}%
                                        %{--code="redactintrigue.role.roleDescription"--}%
                                        %{--default="Description"/>--}%
                                %{--</label></td>--}%
                            %{--</tr>--}%
                            %{--<tr>--}%
                                %{--<td colspan="4"><g:textArea name="roleDescription"--}%
                                                            %{--id="roleDescription" value="" rows="5" cols="100"/></td>--}%
                            %{--</tr>--}%
                        %{--</table>--}%
                        %{--<g:submitButton name="Update" value="Update"/>--}%
                    %{--</g:formRemote>--}%

                    %{--<div id="updateMe">this div is updated with the result of the show call</div>--}%

                %{--</div>--}%
            %{--</g:each>--}%
        %{--</div>--}%
    %{--</div>--}%

    %{--<!--<table style="height: 90%;">--}%
		%{--<tr>--}%
		%{--<td style="height:100%;">--}%
			%{--<div style="height: 100%; width: 75pt; overflow: auto; border: 1px solid black;border-radius: 10px;">--}%

			%{--</div>--}%
			%{--</td>--}%
			%{--<td>--}%
			%{--<div style="height: 100%; overflow: auto; border: 1px solid black;border-radius: 10px;">--}%
				%{--<table>--}%
					%{--<tr>--}%
						%{--<td><label for="roleCode"> <g:message--}%
            %{--code="redactintrigue.role.roleCode" default="Role code"/>--}%
						%{--</label></td>--}%
						%{--<td><g:textField name="roleCode"/></td>--}%
					%{--</tr>--}%

					%{--<tr>--}%
						%{--<td><label for="pipNumber"> <g:message--}%
            %{--code="redactintrigue.role.pipNumber" default="PIP"/>--}%
						%{--</label></td>--}%
						%{--<td><g:textField name="pipNumber"/></td>--}%
					%{--</tr>--}%
				%{--</table>--}%
			%{--</div>--}%
			%{--</td>--}%
			%{--</tr>--}%
			%{--</table>-->--}%

%{--</div>--}%
%{--</div>--}%

%{--<div class="tab-pane" id="relations"></div>--}%

%{--<div class="tab-pane" id="places"></div>--}%

%{--<div class="tab-pane" id="objects"></div>--}%

%{--<div class="tab-pane" id="textualClues"></div>--}%

%{--<div class="tab-pane" id="pastScenes"></div>--}%

%{--<div class="tab-pane" id="events"></div>--}%
%{--</div>--}%


%{--<div id="universesModal" class="modal hide fade" tabindex="-1"--}%
     %{--role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">--}%
    %{--<div class="modal-header">--}%
        %{--<button type="button" class="close" data-dismiss="modal"--}%
                %{--aria-hidden="true">×</button>--}%

        %{--<h3 id="myModalLabel">Univers</h3>--}%
    %{--</div>--}%

    %{--<div class="modal-body">--}%

        %{--<table>--}%
            %{--<g:each in="${universList}" status="i" var="universInstance">--}%
                %{--<g:if test="${i % 3 == 0}">--}%
                    %{--<tr>--}%
                %{--</g:if>--}%
                %{--<td><label for="universes_${universInstance.id}"><g:checkBox--}%
                        %{--name="universes_${universInstance.id}"--}%
                        %{--id="universes_${universInstance.id}"--}%
                        %{--checked="${plotInstance.hasUnivers(universInstance)}"/> ${fieldValue(bean: universInstance, field: "name")}</label>--}%
                %{--</td>--}%
                %{--<g:if test="${(i + 1) % 3 == 0}">--}%
                    %{--</tr>--}%
                %{--</g:if>--}%
            %{--</g:each>--}%

        %{--</table>--}%
    %{--</div>--}%

    %{--<div class="modal-footer">--}%
        %{--<button class="btn" data-dismiss="modal" aria-hidden="true">Ok</button>--}%
    %{--</div>--}%
%{--</div>--}%

%{--<div id="tagsModal" class="modal hide fade" tabindex="-1" role="dialog"--}%
     %{--aria-labelledby="myModalLabel" aria-hidden="true">--}%
    %{--<div class="modal-header">--}%
        %{--<button type="button" class="close" data-dismiss="modal"--}%
                %{--aria-hidden="true">×</button>--}%

        %{--<h3 id="myModalLabel">Tags</h3>--}%
    %{--</div>--}%

    %{--<div class="modal-body">--}%

        %{--<table>--}%
            %{--<g:each in="${plotTagList}" status="i" var="plotTagInstance">--}%
                %{--<g:if test="${i % 3 == 0}">--}%
                    %{--<tr>--}%
                %{--</g:if>--}%
                %{--<td><label for="tags_${plotTagInstance.id}"><g:checkBox--}%
                        %{--name="tags_${plotTagInstance.id}" id="tags_${plotTagInstance.id}"--}%
                        %{--checked="${plotInstance.hasPlotTag(plotTagInstance)}"/> ${fieldValue(bean: plotTagInstance, field: "tag")}</label>--}%
                %{--</td>--}%
                %{--<g:if test="${(i + 1) % 3 == 0}">--}%
                    %{--</tr>--}%
                %{--</g:if>--}%
            %{--</g:each>--}%

        %{--</table>--}%
    %{--</div>--}%

    %{--<div class="modal-footer">--}%
        %{--<button class="btn" data-dismiss="modal" aria-hidden="true">Ok</button>--}%
    %{--</div>--}%
%{--</div>--}%



