%{--<%@ page import="org.gnk.tag.TagFamily" %>--}%

%{--<h4>${message(code: 'adminRef.tagFamily.list')}</h4>--}%

%{--<table class="table table-bordered">--}%
	%{--<thead>--}%
		%{--<tr>	--}%
			%{--<g:sortableColumn property="value" title="${message(code: 'adminRef.tagFamily.name')}" />--}%
		%{--</tr>--}%
	%{--</thead>--}%
	%{--<tbody>--}%
	%{--<g:each in="${TagFamily.list()}" status="i" var="tagFamilyInstance">--}%
		%{--<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">--}%
			%{--<td><g:link action="show" id="${tagFamilyInstance.id}">${fieldValue(bean: tagFamilyInstance, field: "value")}</g:link></td>--}%
		%{--</tr>--}%
	%{--</g:each>--}%
	%{--</tbody>--}%
%{--</table>--}%