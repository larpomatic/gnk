%{--<%@ page import="org.gnk.tag.TagFamily" %>--}%

%{--<legend>${message(code: 'adminRef.tagFamily.list')}</legend>--}%

%{--<table class="table table-bordered">--}%
	%{--<thead>--}%
		%{--<tr>--}%
            %{--<th>#</th>--}%
			%{--<g:sortableColumn property="value" title="${message(code: 'adminRef.tagFamily.name')}" />--}%
            %{--<th><g:message code="default.delete"/></th>--}%
		%{--</tr>--}%
	%{--</thead>--}%
	%{--<tbody>--}%
	%{--<g:each in="${tagFamilyInstanceList}" status="i" var="tagFamilyInstance">--}%
		%{--<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">--}%
            %{--<td>${i+1}</td>--}%
			%{--<td><a href="#modal${tagFamilyInstance.id}" role="button" class="btn" data-toggle="modal">${fieldValue(bean: tagFamilyInstance, field: "value")}</a></td>--}%
			%{--<td>--}%
				%{--<g:form>--}%
					%{--<fieldset class="buttons">--}%
						%{--<g:hiddenField name="idFamily" value="${tagFamilyInstance?.id}" />--}%
						%{--<g:actionSubmit class="btn btn-warning" action="deleteFamily" value="${message(code: 'default.delete')}" onclick="return confirm('${message(code: 'adminRef.tagFamily.deleteTagFamily')}');" />--}%
					%{--</fieldset>--}%
				%{--</g:form>--}%
			%{--</td>--}%
		%{--</tr>--}%
	%{--</g:each>--}%
	%{--</tbody>--}%
%{--</table>--}%

%{--<!-- Modal Views -->--}%
%{--<g:render template="modalViewTagFamilies" />--}%