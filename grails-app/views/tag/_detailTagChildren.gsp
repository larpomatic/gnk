<%@ page import="org.gnk.tag.Tag" %>
<g:each in="${Tag.list()}" var="tag">
<div id="tagListmodal${tag.id}" class="modal hide fade tags-modal" tabindex="-1">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">×</button>
        <h3 id="myModalLabel">Choix du parent</h3>
        <input class="input-medium search-query" data-content="tagList"
               placeholder="<g:message code="selectintrigue.search" default="Search..."/>"/>
    </div>
    <div class="modal-body">
        <ul class="tagList">
            <g:each in="${tag}" status="i" var="tagInstance">
                <g:render template="TagTreeDetails" model="[tagInstance: tagInstance, tagInstanceId : 'first']"/>
            </g:each>
        </ul>
    </div>
</div>
</g:each>