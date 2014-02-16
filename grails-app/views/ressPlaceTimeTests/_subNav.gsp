<div class="btn-group subnav">
    <g:link class="btn ${(controllerName == 'ressPlaceTimeTests' && actionName == 'resourceTest') ? 'disabled' : ''}" controller="ressPlaceTimeTests" action="resourceTest"><g:message code="ressplacetime.subnav.resource"/></g:link>
    <g:link class="btn ${(controllerName == 'ressPlaceTimeTests' && actionName == 'placeTest') ? 'disabled' : ''}" controller="ressPlaceTimeTests" action="placeTest"><g:message code="ressplacetime.subnav.place"/></g:link>
    <g:link class="btn ${(controllerName == 'ressPlaceTimeTests' && actionName == 'timeTest') ? 'disabled' : ''}" controller="ressPlaceTimeTests" action="timeTest"><g:message code="ressplacetime.subnav.time"/></g:link>
</div>