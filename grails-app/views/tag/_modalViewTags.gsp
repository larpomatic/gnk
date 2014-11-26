<%@ page import="org.gnk.tag.Tag" %>
<g:each status="id" in="${Tag.list()}" var="tag">
    <div id="modal${tag.id}" class="modal hide fade" style="width: 800px; margin-left: -400px;"
         tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-header">
            <g:each in="${listTagParent[tag.id]}"  var="tagParent">
                ${tagParent.name.encodeAsHTML()} >
            </g:each>

            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3 id="myModalLabel">Détail de l'utilisation du tag ${tag.name.encodeAsHTML()}</h3>
        </div>

        <div class="modal-body">
            <h4 class="cap">Intrigues</h4>
            <table class="table table-condensed">
                <thead>
                <tr class="upper">
                    <th>#</th>
                    <th>nom de l'intrigue</th>
                    <th>description de l'intrigue</th>
                    <th>login du créateur de l'intrigue</th>
                    <th>poids du tag</th>
                </tr>
                </thead>
                <tbody>
                <g:each status="j" in="${tag.extPlotTags}" var="PlotTag">
                    <tr>
                        <g:if test="${!PlotTag.weight.equals(null)}">
                            <td>${j + 1}</td>
                            <td>${PlotTag.plot.name}</td>
                            <td>${PlotTag.plot.description}</td>
                            <td>${PlotTag.plot.user.username}</td>
                            <td>${PlotTag.weight}%</td>
                        </g:if>
                        <g:else>
                            <td/>
                            <td/>
                            <td/>
                            <td/>
                            <td/>
                        </g:else>
                    </tr>
                </g:each>
                <tbody>
            </table>

            <h4 class="cap">Rôles</h4>
            <table class="table table-condensed">
                <thead>
                <tr class="upper">
                    <th>#</th>
                    <th>code du rôle</th>
                    <th>description du rôle</th>
                    <th>poids du tag</th>
                </tr>
                </thead>
                <tbody>
                <g:set var="index" value="${0}"/>
                <g:each in="${org.gnk.roletoperso.Role.list()}" var="role">
                    <g:each in="${role.roleHasTags}" var="roleHasTags">
                    <tr>
                        <g:if test="${roleHasTags.tag.equals(tag)}">
                            <g:set var="index" value="${index + 1}"/>
                            <td>${index}</td>
                            <td>${role.code}</td>
                            <td>${role.description}</td>
                            <td>${roleHasTags.weight}%</td>
                        </g:if>
                    </tr>
                    </g:each>
                </g:each>
                <tbody>
            </table>

            <h4 class="cap">Ressources</h4>
            <table class="table table-condensed">
                <thead>
                <tr class="upper">
                    <th>#</th>
                    <th>nom de la ressource</th>
                    <th>genre de la ressource</th>
                    <th>description de la ressource</th>
                    <th>poids du tag</th>
                </tr>
                </thead>
                <tbody>
                <g:each status="j" in="${tag.extResourceTags}" var="ResourceTag">
                    <tr>
                        <g:if test="${!ResourceTag.weight.equals(null)}">
                            <td>${j + 1}</td>
                            <td>${ResourceTag.resource.name}</td>
                            <td>${ResourceTag.resource.gender}</td>
                            <td>${ResourceTag.resource.description}</td>
                            <td>${ResourceTag.weight}%</td>
                        </g:if>
                        <g:else>
                            <td/>
                            <td/>
                            <td/>
                            <td/>
                            <td/>
                        </g:else>
                    </tr>
                </g:each>
                <tbody>
            </table>

            <h4 class="cap">Relations entre tag</h4>
            <table class="table table-condensed">
                <thead>
                <tr class="upper">
                    <th>#</th>
                    <th>nom de l'autre tag</th>
                    <th>pourcentage de compatibilité</th>
                </tr>
                </thead>
                <tbody>
                <g:each status="j" in="${org.gnk.tag.TagRelation.findAllByTag1(tag)}" var="tagRelation">
                    <tr>
                        <g:if test="${!tagRelation.weight.equals(null)}">
                            <td>${j + 1}</td>
                            <td>${tagRelation.tag2.name}</td>
                            <td>${tagRelation.weight}</td>
                        </g:if>
                        <g:else>
                            <td/>
                            <td/>
                            <td/>
                            <td/>
                            <td/>
                        </g:else>
                    </tr>
                </g:each>
                <g:each status="j" in="${org.gnk.tag.TagRelation.findAllByTag2(tag)}" var="tagRelation">
                    <tr>
                        <g:if test="${!tagRelation.weight.equals(null)}">
                            <td>${j + 1}</td>
                            <td>${tagRelation.tag1.name}</td>
                            <td>${tagRelation.weight}</td>
                        </g:if>
                        <g:else>
                            <td/>
                            <td/>
                            <td/>
                            <td/>
                            <td/>
                        </g:else>
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

