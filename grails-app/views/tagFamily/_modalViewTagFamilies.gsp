<%@ page import="org.gnk.tag.TagFamily; org.gnk.tag.Tag" %>
<g:each status="i" in="${TagFamily.list()}" var="tagFamily">
    <div id="modal${tagFamily.id}" class="modal hide fade" style="width: 800px; margin-left: -400px;"
         tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3 id="myModalLabel">Détail de l'utilisation de la famille ${tagFamily.value.encodeAsHTML()}</h3>
        </div>

        <div class="modal-body">
            <h4 class="cap">Intrigues</h4>
            <table class="table table-condensed">
                <thead>
                <tr class="upper">
                    <th>#</th>
                    <th>nom du tag</th>
                </tr>
                </thead>
                <tbody>
                <g:set var="index" value="${0}"/>
                <g:each in="${Tag.list()}" var="tag">
                    <tr>
                        <g:if test="${tag.tagFamily.equals(tagFamily)}">
                            <g:set var="index" value="${index + 1}"/>
                            <td>${index}</td>
                            <td>${tag.name}</td>
                        </g:if>
                    </tr>
                </g:each>
                <tbody>
            </table>
    </div>

        <div class="modal-footer">
            <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
        </div>
    </div>
</g:each>