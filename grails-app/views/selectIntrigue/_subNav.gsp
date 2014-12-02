<div class="btn-group subnav">
    <g:link class="btn ${actionName == 'list' ? 'disabled' : ''}" controller="selectIntrigue" action="list"><g:message code="selectintrigue.list"/></g:link>
    <g:hasRights lvlright="${right.MGNMODIFY.value()}">
        <g:link class="btn ${actionName == 'selectIntrigue' ? 'disabled' : ''}" controller="selectIntrigue" action="selectIntrigue"><g:message code="selectintrigue.edit"/></g:link>
    </g:hasRights>
</div>