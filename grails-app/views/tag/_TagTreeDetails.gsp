<g:if test="${'first' == tagInstanceId}">
    <g:set var="tagInstanceId" value="${tagInstance.id}"/>
</g:if>


<div class="accordion" id="accordion${tagInstanceId}-${tagInstance.id}">
<div class="accordion-group">
    <div class="accordion-heading">
        <div class="row">
            <div class="span3">
                <a class="accordion-toggle" data-toggle="collapse"
                   data-parent="#accordion${tagInstanceId}-${tagInstance.id}"
                   href="#collapse${tagInstanceId}-${tagInstance.id}">${tagInstance.name}
                </a>
                </div>
                <div class="pull-right" style="margin-top: 3px; margin-right: 3px">
                    <a href="#modal${tagInstance.id}" data-dismiss="modal" class="btn btn-small"
                       data-toggle="modal">Détail</a>
                </div>
        </div>
    </div>
    <g:if test="${tagInstance == tagInstanceId}">
        <div id="collapse${tagInstanceId}-${tagInstance.id}" class="accordion-body collapse in">
    </g:if>
    <g:else>
        <div id="collapse${tagInstanceId}-${tagInstance.id}" class="accordion-body collapse">
    </g:else>
    <div class="accordion-inner">
        <g:if test="${tagInstance.children.size() != 0}">
            <li class="modalLi" style="list-style-type: none">
                <ul>
                    <g:each in="${tagInstance.children}" var="child">
                        <g:render template="TagTreeDetails"
                                  model="[tagInstance: child, tagInstanceId: tagInstanceId]"/>
                    </g:each>
                </ul>
            </li>
        </g:if>
    </div>
</div>
</div>
</div>