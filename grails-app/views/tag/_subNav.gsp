<div class="btn-group subnav">
    <g:link class="btn ${(controllerName == 'tag' && actionName == 'list') ? 'disabled' : ''}" controller="tag" action="list"><g:message code="adminRef.navbar.tags"/></g:link>
    <g:link class="btn ${controllerName == 'tagRelation' ? 'disabled' : ''}" controller="tagRelation" action="list"><g:message code="adminRef.navbar.tagRelation"/></g:link>
    %{--<g:link class="btn ${controllerName == 'tagFamily' ? 'disabled' : ''}" controller="tagFamily" action="list"><g:message code="adminRef.navbar.tagFamilies"/></g:link>--}%
<g:link class="btn ${controllerName == 'univers' ? 'disabled' : ''}" controller="Tagunivers" action="list"><g:message code="adminRef.univers"/></g:link>
%{--<g:link class="btn ${controllerName == 'plot' ? 'disabled' : ''}" controller="plot" action="list"><g:message code="adminRef.plot"/></g:link>--}%
<g:link class="btn ${controllerName == 'resource' ? 'disabled' : ''}" controller="resource" action="list"><g:message code="adminRef.resource"/></g:link>
%{--<g:link class="btn ${controllerName == 'genericResource' ? 'disabled' : ''}" controller="genericResource" action="list"><g:message code="adminRef.GenResource"/></g:link>--}%
<g:link class="btn ${controllerName == 'place' ? 'disabled' : ''}" controller="place" action="list"><g:message code="adminRef.navbar.place"/></g:link>
%{--<g:link class="btn ${controllerName == 'genericPlace' ? 'disabled' : ''}" controller="genericPlace" action="list"><g:message code="adminRef.navbar.genericPlace"/></g:link>--}%
<g:link class="btn ${(controllerName == 'tag' && actionName == 'stats') ? 'disabled' : ''}" controller="tag" action="stats"><g:message code="adminRef.navbar.stats"/></g:link>
<g:link class="btn ${(controllerName == 'dbCoherence') ? 'disabled' : ''}" controller="dbCoherence" action="index">Cohérence de la base de données</g:link>
</div>