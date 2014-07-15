%{--<tr class="RelationRow">--}%
	%{--<td><g:if test="${isRole1}">--}%
			%{--${roleHasRelationWithRoles.role2.code}--}%
		%{--</g:if> <g:if test="${isRole1 == false}">--}%
			%{--${roleHasRelationWithRoles.role1.code}--}%
		%{--</g:if></td>--}%
	%{--<g:if test="${roleHasRelationWithRoles.isBijective}">--}%
		%{--<td><g:img dir="images/redactIntrigue/relations"--}%
				%{--file="validate.png" /></td>--}%
	%{--</g:if>--}%
	%{--<g:if test="${roleHasRelationWithRoles.isBijective == false}">--}%
		%{--<td><g:img dir="images/redactIntrigue/relations"--}%
				%{--file="forbidden.png" /></td>--}%
	%{--</g:if>--}%
	%{--<td>--}%
		%{--${roleHasRelationWithRoles.roleRelationType.name}--}%
	%{--</td>--}%
	%{--<td>--}%
		%{--${roleHasRelationWithRoles.weight}--}%
	%{--</td>--}%
	%{--<td><a href="#editRelationModal_${roleHasRelationWithRoles.id}"--}%
		%{--role="button" class="btn" data-toggle="modal"><g:img--}%
				%{--dir="images/redactIntrigue/relations" file="edit.png" /></a>--}%
				%{--<g:render--}%
			%{--template="editRelationModal"--}%
			%{--model="['roleHasRelationWithRoles':roleHasRelationWithRoles, 'create':false, 'plotInstance':plotInstance]" />--}%
	%{--</td>--}%
	%{--<td><g:actionSubmitImage action="delete" class="btn"--}%
			%{--value="${message(code: 'default.button.delete.label', default: 'Delete')}"--}%
			%{--formnovalidate=""--}%
			%{--src="${resource(dir: 'images/redactIntrigue/relations', file: 'delete.png')}"--}%
			%{--onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />--}%
	%{--</td>--}%
%{--</tr>--}%