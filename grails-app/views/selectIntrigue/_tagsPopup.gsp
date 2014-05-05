 <div id="${idPopup}" class="modal hide fade" tabindex="-1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">×</button>
        <h3 id="myModalLabel">${namePopup }</h3>
        <input class="input-medium search-query" data-content="${idPopup}"
               placeholder="<g:message code="selectintrigue.search" default="Search..."/>"/>
    </div>
    <div class="modal-body">
        <ul class="${idPopup}">
            <g:each in="${tagList}" status="i" var="tagInstance">
                <li data-name="${tagInstance.name.toLowerCase()}" class="row">
                    <label for="${tagPrefix }${tagInstance?.id}" class="pull-left">
                        <g:checkBox name="${tagPrefix }${tagInstance?.id}" id="${tagPrefix }${tagInstance?.id}"
                            checked="${myOwner?.hasTag(tagInstance, tagListName)}"
                            onClick="toggle('${tagPrefix }${tagInstance?.id}', '${weightTagPrefix}${tagInstance?.id}')"/>
                        ${fieldValue(bean: tagInstance, field: "name")}
                    </label>
                    <div class="pull-right">
                        <button type="button" class="btn btn-danger banTag"><i class="icon-ban-circle"></i></button>
                    </div>
                    <div class="pull-right tagWeight">
                        <g:if test="${myOwner != null}">
                            <input id="${weightTagPrefix}${tagInstance?.id}" name="${weightTagPrefix}${tagInstance?.id}"
                                   value="${myOwner?.getTagWeight(tagInstance, tagListName)}" style="width:45px;"
                                   type="number" max="101" min="-101">
                        </g:if>
                        <g:if test="${myOwner == null}">
                            <input id="${weightTagPrefix}${tagInstance?.id}" name="${weightTagPrefix}${tagInstance?.id}"
                                   value="50" type="number" max="101" min="-101" style="width:45px;">
                        </g:if>
                    </div>
                    <div class="pull-right">
                        <button type="button" class="btn btn-success chooseTag"><i class="icon-ok"></i></button>
                    </div>
                </li>
            </g:each>
        </ul>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal">Ok</button>
    </div>
</div>