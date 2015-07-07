<div class="btn-group subnav">
    <g:link class="btn ${(controllerName == 'GenericEvent') ? 'disabled' : ''}" controller="GenericEvent" action="list">Life</g:link>
    <g:link class="btn ${(controllerName == 'tag' && actionName == 'list') ? 'disabled' : ''}" controller="tag" action="list"><g:message code="adminRef.navbar.tags"/></g:link>
    <g:link class="btn ${controllerName == 'tagRelation' ? 'disabled' : ''}" controller="tagRelation" action="list"><g:message code="adminRef.navbar.tagRelation"/></g:link><g:link class="btn ${controllerName == 'resource' ? 'disabled' : ''}" controller="resource" action="list"><g:message code="adminRef.resource"/></g:link>
    <g:link class="btn ${controllerName == 'place' ? 'disabled' : ''}" controller="place" action="list"><g:message code="adminRef.navbar.place"/></g:link>
    <g:link class="btn ${(controllerName == 'tag' && actionName == 'stats') ? 'disabled' : ''}" controller="tag" action="stats"><g:message code="adminRef.navbar.stats"/></g:link>
    <g:link class="btn ${(controllerName == 'dbCoherence') ? 'disabled' : ''}" controller="dbCoherence" action="index">Cohérence de la base de données</g:link>
    <g:link class="btn ${(controllerName == 'social') ? 'disabled' : ''}" controller="social" action="listConventionRule">Convention</g:link>
</div>