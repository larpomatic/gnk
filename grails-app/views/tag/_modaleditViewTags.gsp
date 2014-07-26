<%@ page import="org.gnk.tag.Tag" %>
<g:each status="i" in="${Tag.list()}" var="tag">
    <div id="editmodal${tag.id}" class="modal hide fade" style="width: 800px; margin-left: -400px;"
         tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3 id="myModalLabel">Détail de l'utilisation du tag ${tag.name.encodeAsHTML()}</h3>
        </div>
        <g:form action="editRelevantTag" id="${tag.id}">
        <div class="modal-body">
            <g:if test="${tag.getTagRelevant()}">
            RelevantPlace : <g:checkBox name="checkboxRelevantPlace" value="${tag.getTagRelevant().relevantPlace}"/>
            RelevantFirstName : <g:checkBox name="checkboxRelevantFirstName" value="${tag.getTagRelevant().relevantFirstname}"/>
            RelevantLastName : <g:checkBox name="checkboxRelevantLastName" value="${tag.getTagRelevant().relevantLastname}"/>
            RelevantPlot : <g:checkBox name="checkboxRelevantPlot" value="${tag.getTagRelevant().relevantPlot}"/>
            RelevantResource : <g:checkBox name="checkboxRelevantResource" value="${tag.getTagRelevant().relevantResource}"/>
            RelevantRole : <g:checkBox name="checkboxRelevantRole" value="${tag.getTagRelevant().relevantRole}"/>
            </g:if>
        </div>

        <div class="modal-footer">
            <button class="btn btn-primary" type="submit">Valider</button>
            <a class="btn" data-dismiss="modal" aria-hidden="true">Annuler</a>
        </div>
        </g:form>
    </div>
</g:each>