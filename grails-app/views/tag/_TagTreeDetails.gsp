<li data-name="${tagInstance.name.toLowerCase()}" class="row" style="list-style-type: none">
    <label class="pull-left">
        <a href="#modal${tagInstance.id}" role="button" class="btn" data-dismiss="modal" data-toggle="modal">${tagInstance.name}</a>
    </label>
</li>
<g:if test="${tagInstance.children.size() != 0}">
    <li class="modalLi" style="list-style-type: none">
        <ul>
            <g:each in="${tagInstance.children}" var="child">
                <g:render template="TagTreeDetails" model="[tagInstance: child]"/>
            </g:each>
        </ul>
    </li>
</g:if>