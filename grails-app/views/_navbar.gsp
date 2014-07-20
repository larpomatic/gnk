<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
            <g:link class="brand" controller="home" action="index"><div class="brand-logo"></div>Genotron</g:link>
            <ul class="nav">
                <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_USER">
                <li>
                    <g:link controller="tag" action="list"><g:message code="navbar.adminRef"/></g:link>
                </li>
                <li>
                    <g:link controller="redactIntrigue"><g:message code="navbar.redactintrigue"/></g:link>
                </li>
                <li>
                    <g:link controller="selectIntrigue" action="list"><g:message code="navbar.selectintrigue"/></g:link>
                </li>
                <li>
                    <g:link controller="user" action="profil"><g:message code="navbar.configuration"/></g:link>
                </li>
                <li>
                    <g:if env="development">
                        <g:link controller="roleToPerso" action="roleToPerso"><g:message code="navbar.role2perso"/></g:link>
                    </g:if>
                </li>
                <g:if env="development">
                    <li>
                        <g:link controller="substitution"><g:message code="navbar.substitution"/></g:link>
                    </li>
                    <li>
                        <g:link controller="publication"><g:message code="navbar.publication"/></g:link>
                    </li>
                    <sec:ifAnyGranted roles="ROLE_ADMIN">
                    <li>
                        <g:link controller="ressPlaceTimeTests" action="list"><g:message code="navbar.ressPlaceTimeTest"/></g:link>
                    </li>
                    <li>
                        <g:link controller="namingTest" action="index"><g:message code="navbar.namingTest"/></g:link>
                    </li>
                     </sec:ifAnyGranted>
                    <li>
                        <g:link controller="logout"><g:message code="navbar.logout"/></g:link>
                    </li>
                </g:if>
                  </sec:ifAnyGranted>
            </ul>
    </div>
</div>