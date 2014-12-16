<%@ page import="org.gnk.tag.Tag" %>
<div id="modals">
<g:each status="id" in="${Tag.list()}" var="tag">
<div id="modal${tag.id}" class="modal hide" style="width: 800px; margin-left: -400px;"
     tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
<div class="modal-header">
    <div id="fa-container${tag.id}" class="tagRefLink">
        <div id="fa${tag.id}">
            <g:if test="${listTagParent != null}">
            <g:each in="${listTagParent[tag.id]}" var="tagParent">
                <a id="tagparent${tagParent.id}"
                   onclick="goTo(${tagParent.id}, ${tag.id})">${tagParent.name.encodeAsHTML()}</a> >
            </g:each>
            </g:if>
            <a id="tagparent${tag.id}" onclick="goTo(${tag.id}, ${tag.id})">${tag.name.encodeAsHTML()}</a>
        </div>
    </div>

    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>

    <h3 id="myModalLabel">Détail de l'utilisation du tag ${tag.name.encodeAsHTML()}</h3>
</div>

<div class="modal-body">

    <div class="accordion" id="accordion${tag.id}+plot">
        <div class="accordion-group">
            <div class="accordion-heading">
                <a class="accordion-toggle" data-toggle="collapse"
                   data-parent="#accordion${tag.id}plot"
                   href="#collapse${tag.id}plot"><h4 class="cap">${tag.extPlotTags.size()} Intrigues</h4>
                </a>
            </div>

            <div id="collapse${tag.id}plot" class="accordion-body collapse">
                <div class="accordion-inner">
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
                </div>
            </div>
        </div>
    </div>

    <div class="accordion" id="accordion${tag.id}+role">
        <div class="accordion-group">
            <div class="accordion-heading">
                <a class="accordion-toggle" data-toggle="collapse"
                   data-parent="#accordion${tag.id}role"
                   href="#collapse${tag.id}role"><h4 class="cap">${tag.extRoleTags.size()} Rôles</h4>
                </a>
            </div>

            <div id="collapse${tag.id}role" class="accordion-body collapse">
                <div class="accordion-inner">
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
                </div>
            </div>
        </div>
    </div>

    <div class="accordion" id="accordion${tag.id}+ress">
        <div class="accordion-group">
            <div class="accordion-heading">
                <a class="accordion-toggle" data-toggle="collapse"
                   data-parent="#accordion${tag.id}ress"
                   href="#collapse${tag.id}ress"><h4 class="cap">${tag.extResourceTags.size()} Ressources</h4>
                </a>
            </div>

            <div id="collapse${tag.id}ress" class="accordion-body collapse">
                <div class="accordion-inner">
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
                </div>
            </div>
        </div>
    </div>

    <div class="accordion" id="accordion${tag.id}+rel">
        <div class="accordion-group">
            <div class="accordion-heading">
                <a class="accordion-toggle" data-toggle="collapse"
                   data-parent="#accordion${tag.id}rel"
                   href="#collapse${tag.id}rel"><h4
                        class="cap">${org.gnk.tag.TagRelation.findAllByTag1(tag).size() + org.gnk.tag.TagRelation.findAllByTag2(tag).size()} Relations entre tag</h4>
                </a>
            </div>

            <div id="collapse${tag.id}rel" class="accordion-body collapse">
                <div class="accordion-inner">
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
                                    <td>${org.gnk.tag.TagRelation.findAllByTag1(tag).size() + j + 1}</td>
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
            </div>
        </div>
    </div>

</div>

<div class="modal-footer">
    <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
</div>
</div>
</g:each>
</div>

<script>
    function goTo(tagPid, tagId) {
        if (tagId != tagPid) {
            $('#fa-container' + tagPid).children('#fa' + tagId).remove();
            $('#fa' + tagId).clone().appendTo('#fa-container' + tagPid);
            $('#fa' + tagPid).remove();
        }
        $("#modals").children().each(function () {
            $(this).modal('hide');
        });
        $("#modal" + tagPid).modal('show');
    }
</script>