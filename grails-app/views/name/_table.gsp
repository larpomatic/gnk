<%@ page import="org.gnk.name.*" %>
<%@ page import="org.gnk.admin.right" %>
<%@ page import="org.gnk.tag.Tag" %>


%{--<legend>${message(code: 'adminRef.tag.list')}</legend>--}%
<legend>Liste des Patronymes</legend>

<table id="listTable" class="table table-bordered">
    <thead>
    <tr>
        <th>Nom</th>
        <th>Tag</th>
        <th>Date Creation</th>
        <th>Date Modification</th>
        <g:hasRights lvlright="${right.REFMODIFY.value()}">
            <th>
                Dupliquer
            </th>
        </g:hasRights>
            <g:hasRights lvlright="${right.REFDELETE.value()}">
                <th>
                    Supprimer
                </th>
            </g:hasRights>
    </tr>
    </thead>
    <tbody>
    <g:each in="${NameInstanceList}" status="i" var="NameInstance">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td>
                <g:hasRights lvlright="${right.REFMODIFY.value()}">
                    <a href="${createLink(action: "edit", id: "${NameInstance?.id}")}">
                        ${fieldValue(bean: NameInstance, field: "name")}</a>
                </g:hasRights>
            </td>


            <td>
                <g:each in="${NameInstance.getNameHasTag()}" var="tag">
                    <g:if test="${tag.weight > 50}">
                        <span class="label mytool label-success" data-tag="none" contenteditable="false" toggle="tooltip"  data-placement="top" data-original-title="${tag.weight}" title="">${tag.tag.name}</span>
                    </g:if>
                    <g:elseif test="${tag.weight >=0}">
                        <span class="label mytool label-info" data-tag="none" contenteditable="false" toggle="tooltip" data-placement="top" data-original-title="${tag.weight}" title="">${tag.tag.name}</span>
                    </g:elseif>
                    <g:elseif test="${tag.weight >= -50}">
                        <span class="label mytool label-warning" data-tag="none" contenteditable="false" toggle="tooltip" data-placement="top" data-original-title="${tag.weight}" title="">${tag.tag.name}</span>
                    </g:elseif>
                    <g:else>
                        <span class="label mytool label-important" data-tag="none" contenteditable="false" toggle="tooltip" data-placement="top" data-original-title="${tag.weight}" title="">${tag.tag.name}</span>
                    </g:else>
                </g:each>
            </td>
            <td>${NameInstance.dateCreated.format("dd/MM/yyyy")}</td>
            <td>${NameInstance.lastUpdated.format("dd/MM/yyyy")}</td>
            <td>
                <g:form>
                    <fieldset class="buttons">
                        <g:hasRights lvlright="${right.REFMODIFY.value()}">
                            <a href="${createLink(action: "dupplicate", id: "${NameInstance?.id}")}">
                               Dupliquer</a>
                        </g:hasRights>
                    </fieldset>
                </g:form>
            </td>
            <td>
        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${NameInstance.id}"/>
                <g:hasRights lvlright="${right.REFDELETE.value()}">
                    <g:actionSubmit class="btn btn-danger" action="delete"
                                    value="${message(code: 'default.delete')}"
                                    onclick="return confirm('Etes vous sûre de vouloir supprimer ce Patronyme?');"/>
                </g:hasRights>
            </fieldset>
        </g:form>
            </td>

        </tr>
    </g:each>
%{--<g:each in="${NameInstanceList}" status="a" var="NameInstance">--}%
    %{----}%%{----}%%{--Modal--}%%{----}%%{----}%
    %{--<div id="tagsModalHasTag${NameInstance.id}" class="modal hide fade tags-modal" tabindex="-1">--}%
        %{--<div class="modal-header">--}%
            %{--<button type="button" class="close" data-dismiss="modal">×</button>--}%
            %{--<h3>Nom</h3>--}%
            %{--<g:textField name="name" value="${NameInstance.name}" required=""/>--}%
            %{--<h3>Tags</h3>--}%
            %{--<input class="input-medium search-query" data-content="NameTags"--}%
                   %{--placeholder="Search..."/>--}%
            %{--<button type="button" class="btn" value="Rechercher">--}%
            %{--</button>--}%
        %{--</div>--}%

        %{----}%%{----}%%{--<form>--}%%{----}%%{----}%
        %{--<g:hiddenField name="NameHasTagAdd" value="true"/>--}%
        %{--<div class="modal-body">--}%
            %{--<ul class="NameTags">--}%
                %{--<g:each in="${TagInstanceList}" status="j" var="tagInstance">--}%
                    %{--<g:render template="NameTagTree"--}%
                              %{--model="[tagInstance : tagInstance,--}%
                                      %{--NameInstance: NameInstance,--}%
                                      %{--referenceTag: NameInstance.nameHasTag]"/>--}%
                %{--</g:each>--}%
            %{--</ul>--}%
        %{--</div>--}%

        %{--<div class="modal-footer">--}%
            %{----}%%{----}%%{--<button class="btn" data-dismiss="modal">Ok</button>--}%%{----}%%{----}%
            %{--<g:actionSubmit class="save" action="edit" value="Ok"/>--}%
        %{--</div>--}%
    %{--</div>--}%
%{--</g:each>--}%
    </tbody>
</table>
<script type="application/javascript">
    $(".mytool").tooltip({ html: true });
</script>
