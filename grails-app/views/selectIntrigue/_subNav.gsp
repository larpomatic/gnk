<div class="btn-group subnav">
    <h3><g:message code="selectintrigue.subNav.create" default="Plot List"/></h3>
    <g:hasRights lvlright="${right.MGNMODIFY.value()}">
        <g:link class="btn" controller="selectIntrigue" action="selectIntrigue"><g:message code="selectintrigue.edit"/></g:link>
    </g:hasRights>
</div>