<%@ page import="org.gnk.tag.Tag" %>
<g:each status="i" in="${Tag.list()}" var="tag">
    <div id="editmodal${tag.id}" class="modal hide fade" style="width: 800px; margin-left: -400px;"
         tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3 id="myModalLabel">Editer les Tags Relevant</h3>
        </div>
        <g:form action="editRelevantTag" id="${tag.id}">
        <div class="modal-body">
            <g:if test="${tag.getTagRelevant()}">
            <table>
                <tr>
                    <td>RelevantPlace</td>
                    <td><g:checkBox name="checkboxRelevantPlace" value="${tag.getTagRelevant().relevantPlace}"/></td>
                </tr>
                <tr>
                    <td>RelevantFirstName</td>
                    <td><g:checkBox name="checkboxRelevantFirstName" value="${tag.getTagRelevant().relevantFirstname}"/></td>
                </tr>
                <tr>
                    <td>RelevantLastName</td>
                    <td> <g:checkBox name="checkboxRelevantLastName" value="${tag.getTagRelevant().relevantLastname}"/></td>
                </tr>
                <tr><td>RelevantPlot</td>
                    <td><g:checkBox name="checkboxRelevantPlot" value="${tag.getTagRelevant().relevantPlot}"/></td>
                </tr>
                <tr>
                    <td>RelevantResource</td>
                    <td> <g:checkBox name="checkboxRelevantResource" value="${tag.getTagRelevant().relevantResource}"/></td>
                </tr>
                <tr>
                    <td>RelevantRole</td>
                    <td> <g:checkBox name="checkboxRelevantRole" value="${tag.getTagRelevant().relevantRole}"/></td>
                </tr>
            </table>
            </g:if>
        </div>

        <div class="modal-footer">
            <button class="btn btn-primary" type="submit">Valider</button>
            <a class="btn" data-dismiss="modal" aria-hidden="true">Annuler</a>
        </div>
        </g:form>
    </div>
</g:each>