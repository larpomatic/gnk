<li data-name="${tagInstance.name.toLowerCase()}" class="row" style="list-style-type: none">
    <label class="pull-left">
        <input type="radio" name="Tag_select" value="${tagInstance.id}">
        ${tagInstance.name}
    </label>
</li>
<g:if test="${tagInstance.children.size() != 0}">
    <li class="modalLi" style="list-style-type: none">
        <ul>
            <g:each in="${tagInstance.children}" var="child">
                <g:render template="TagTree" model="[tagInstance: child]"/>
            </g:each>
        </ul>
    </li>
</g:if>