%{--<%@ page import="org.gnk.roletoperso.RoleRelationType" %>--}%
%{--<div id="editRelationModal_${roleHasRelationWithRoles?.id}"--}%
	%{--class="modal hide fade" tabindex="-1" role="dialog"--}%
	%{--aria-labelledby="myModalLabel" aria-hidden="true">--}%
	%{--<g:set var="formName"--}%
		%{--value="${(create?'createRelation':'updateRelation_'+roleHasRelationWithRoles.id)}" />--}%
	%{--<g:set var="action" value="${(create?'create':'update')}" />--}%
	%{--<form name="${formName}" url="[controller: 'role', action:'${action}']">--}%
		%{--<div class="modal-header">--}%
			%{--<button type="button" class="close" data-dismiss="modal"--}%
				%{--aria-hidden="true">×</button>--}%
			%{--<h3 id="myModalLabel">--}%
				%{--<g:message code="redactintrigue.relation.edition"--}%
					%{--default="Relation edit" />--}%
			%{--</h3>--}%
		%{--</div>--}%
		%{--<div class="modal-body">--}%
			%{--<table class="table table-bordered table-striped">--}%
				%{--<thead>--}%
					%{--<!-- En-tête du tableau -->--}%
					%{--<tr>--}%
						%{--<th><g:message code="redactintrigue.relation.from"--}%
								%{--default="From" /></th>--}%
						%{--<th><g:message code="redactintrigue.relation.to" default="To" /></th>--}%
						%{--<th><g:message code="redactintrigue.relation.bijective"--}%
								%{--default="Bijective" /></th>--}%
						%{--<th><g:message code="redactintrigue.relation.type"--}%
								%{--default="Relation type" /></th>--}%
						%{--<th><g:message code="redactintrigue.relation.weight"--}%
								%{--default="Weight" /></th>--}%
					%{--</tr>--}%
				%{--</thead>--}%

				%{--<tbody>--}%
					%{--<!-- Corps du tableau -->--}%

					%{--<tr>--}%
						%{--<td><g:select id="role1" name="role1"--}%
								%{--from="${plotInstance.roles}" optionKey="id" required=""--}%
								%{--value="${roleHasRelationWithRoles?.role1.code}"--}%
								%{--class="many-to-one" /></td>--}%
						%{--<td><g:select id="role2" name="role1"--}%
								%{--from="${plotInstance.roles}" optionKey="id" required=""--}%
								%{--value="${roleHasRelationWithRoles?.role2.code}"--}%
								%{--class="many-to-one" /></td>--}%
							%{--<td><g:checkBox--}%
							%{--name="isBijective"--}%
							%{--id="isBijective"--}%
							%{--checked="${roleHasRelationWithRoles?.isBijective}"--}%
							%{--/></td>--}%
						%{--<td>--}%
						%{--<g:select id="roleRelationType" name="roleRelationType"--}%
								%{--from="${RoleRelationType.list()}" optionKey="id" required=""--}%
								%{--value="${roleHasRelationWithRoles?.roleRelationType}"--}%
								%{--class="many-to-one" />--}%
						%{--</td>--}%
						%{--<td>--}%
							%{--<input id="weight"--}%
							%{--name="weight" value="${(create?'50':roleHasRelationWithRoles?.weight)}"--}%
							%{--type="number" style="width: 40px;">--}%
						%{--</td>--}%
					%{--</tr>--}%

				%{--</tbody>--}%
			%{--</table>--}%
		%{--</div>--}%
		%{--<div class="modal-footer">--}%

			%{--<button type="button" class="btn btn-default" data-dismiss="modal">--}%
				%{--<g:message code="default.cancel" default="Cancel" />--}%
			%{--</button>--}%
			%{--<g:submitButton name="Update"--}%
				%{--value="${message(code: 'default.save', default: 'Save')}"--}%
				%{--class="btn btn-primary" />--}%

		%{--</div>--}%
	%{--</form>--}%
%{--</div>--}%