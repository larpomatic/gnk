 <div id="${idPopup}" class="modal hide fade tags-modal" tabindex="-1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">×</button>
        <h3 id="myModalLabel">${namePopup }</h3>
        <input class="input-medium search-query" data-content="${idPopup}"
               placeholder="<g:message code="selectintrigue.search" default="Search..."/>"/>
    </div>
    <div class="modal-body">
        <ul class="${idPopup}">
            <g:each in="${tagList}" status="i" var="tagInstance">
                <g:render template="plotTagTree" model="[tagInstance: tagInstance, tagPrefix: tagPrefix, myOwner: myOwner, weightTagPrefix: weightTagPrefix, tagListName: tagListName]"/>
            </g:each>
        </ul>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal">Ok</button>
    </div>
</div>