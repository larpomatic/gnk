<!-- navbar des différentes catégories d'objets insérables -->
<div class="btn-group">
    <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
        Personnage <span class="caret"></span>
    </button>
    <ul class="dropdown-menu roleSelector">
        <g:each in="${plotInstance.roles}" status="i5" var="role">
            <li data-id="${role.id}">
                <a onclick="setCarretPos(); pasteHtmlAtCaret('<span class=&quot;label label-success&quot; contenteditable=&quot;false&quot;>${role.code}</span>'); return false;">
                    ${role.code}
                </a>
            </li>
        </g:each>
        <li>
            <input data-entity="role" data-label="success" type="text" class="inputOther" id="roleOther"
                   placeholder="<g:message code="redactintrigue.generalDescription.other" default="Other"/>"/>
        </li>
    </ul>
</div>

<div class="btn-group">
    <button type="button" class="btn btn-warning dropdown-toggle" data-toggle="dropdown">
        Lieu <span class="caret"></span>
    </button>
    <ul class="dropdown-menu placeSelector">
        <g:each in="${plotInstance.genericPlaces}" status="i5" var="genericPlace">
            <li data-id="${genericPlace.id}">
                <a onclick="setCarretPos(); pasteHtmlAtCaret('<span class=&quot;label label-warning&quot; contenteditable=&quot;false&quot;>${genericPlace.code}</span>'); return false;">
                    ${genericPlace.code}
                </a>
            </li>
        </g:each>
        <li>
            <input data-entity="place" data-label="warning" type="text" class="inputOther" id="placeOther"
                   placeholder="<g:message code="redactintrigue.generalDescription.other" default="Other"/>"/>
        </li>
    </ul>
</div>

<div class="btn-group">
    <button type="button" class="btn btn-danger dropdown-toggle" data-toggle="dropdown">
        Ressource <span class="caret"></span>
    </button>
    <ul class="dropdown-menu resourceSelector">
        <g:each in="${plotInstance.genericResources}" status="i5" var="genericResource">
            <li data-id="${genericResource.id}">
                <a onclick="setCarretPos(); pasteHtmlAtCaret('<span class=&quot;label label-important&quot; contenteditable=&quot;false&quot;>${genericResource.code}</span>'); return false;">
                    ${genericResource.code}
                </a>
            </li>
        </g:each>
        <li>
            <input data-entity="resource" data-label="important" type="text" class="inputOther" id="resourceOther"
                   placeholder="<g:message code="redactintrigue.generalDescription.other" default="Other"/>"/>
        </li>
    </ul>
</div>

<div class="btn-group">
    <div class="btnFullScreen"></div>
</div>