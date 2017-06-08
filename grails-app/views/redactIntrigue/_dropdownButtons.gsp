<style type="text/css">


/* The switch - the box around the slider */
.switch {
    position: relative;
    display: inline-block;
    width: 60px;
    height: 34px;
}

/* Hide default HTML checkbox */
.switch input {display:none;}

/* The slider */
.slider {
    position: absolute;
    cursor: pointer;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: pink;
    -webkit-transition: .4s;
    transition: .4s;
}

.slider:before {
    position: absolute;
    content: "";
    height: 26px;
    width: 26px;
    left: 4px;
    bottom: 4px;
    background-color: white;
    -webkit-transition: .4s;
    transition: .4s;
}

input:checked + .slider {
    background-color: #2196F3;
}

input:focus + .slider {
    box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
    -webkit-transform: translateX(26px);
    -ms-transform: translateX(26px);
    transform: translateX(26px);
}


</style>
<!-- navbar des différentes catégories d'objets insérables -->
<div class="btn-group">
    <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
        Personnage <span class="caret"></span>
    </button>
    <ul class="dropdown-menu roleSelector">
        <g:each in="${plotInstance.roles}" status="i5" var="role">
            <g:if test="${!(role.type == "STF")}">
                <li data-id="${role.id}">
                    <a class="buttonRichTextEditor" href="#">
                        ${role.code}
                    </a>
                </li>
            </g:if>
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
        <g:each in="${gnConstantPlaceList}" status="i5" var="gnConstantPlace">
            <li data-id="${gnConstantPlace.id}">
                <a class="buttonRichTextEditor btn-warning gnPlaceSelector">
                    ${gnConstantPlace.name}
                </a>
            </li>
        </g:each>
        <g:each in="${plotInstance.genericPlaces}" status="i5" var="genericPlace">
            <li data-id="${genericPlace.id}">
                <a class="buttonRichTextEditor">
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
        <g:each in="${gnConstantResourceList}" status="i5" var="gnConstantResource">
            <li data-id="${gnConstantResource.id}">
                <a class="buttonRichTextEditor btn-danger gnPlaceSelector">
                    ${gnConstantResource.name}
                </a>
            </li>
        </g:each>
        <g:each in="${plotInstance.genericResources}" status="i5" var="genericResource">
            <li data-id="${genericResource.id}">
                <a class="buttonRichTextEditor">
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
    <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown">
        Date et heure <span class="caret"></span>
    </button>
    <ul class="dropdown-menu buttonRichTextEditor">
        <li data-id="0">
            <a class="buttonRichTextEditor gnDateButton">
                GN-Date
            </a>
        </li>
        <li data-id="1">
            <a class="buttonRichTextEditor gnDateButton">
                GN-Heure
            </a>
        </li>

    </ul>
</div>
<button type="button" onclick="toBalise()">Mode balise</button>


<!--
<div class="btn-group">
<label class="switch">
    <input type="checkbox">
    <div class="slider"></div>
</label>
</div> -->
<div class="btn-group">
    <div class="btnFullScreen"></div>
</div>