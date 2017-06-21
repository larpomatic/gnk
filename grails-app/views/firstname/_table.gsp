<%@ page import="org.gnk.firstname.*" %>
<%@ page import="org.gnk.admin.right" %>
<%@ page import="org.gnk.tag.Tag" %>


%{--<legend>${message(code: 'adminRef.tag.list')}</legend>--}%
<legend>Liste des Firstnames</legend>

<table id="listTable" class="table table-bordered">
    <thead>
    <tr>
        <th>Prénom</th>
        <th>Genre</th>
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
    <g:each in="${FirstnameInstanceList}" status="i" var="FirstnameInstance">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td>
                <g:hasRights lvlright="${right.REFMODIFY.value()}">
                    <a href="${createLink(action: "edit", id: "${FirstnameInstance?.id}")}">
                        ${fieldValue(bean: FirstnameInstance, field: "name")}</a>
                </g:hasRights>
            </td>
            <td><span style="text-transform:uppercase">${FirstnameInstance.gender}</span></td>
            <td>
                <g:each in="${FirstnameInstance.getFirstnameHasTag()}" var="tag">
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
            <td>${FirstnameInstance.dateCreated.format("dd/MM/yyyy")}</td>
            <td>${FirstnameInstance.lastUpdated.format("dd/MM/yyyy")}</td>
            <td>
                <g:form>
                    <fieldset class="buttons">
                        <g:hasRights lvlright="${right.REFMODIFY.value()}">
                            <a href="${createLink(action: "dupplicate", id: "${FirstnameInstance?.id}")}">
                                Dupliquer</a>
                        </g:hasRights>
                    </fieldset>
                </g:form>
            </td>
            <td>
        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${FirstnameInstance.id}"/>
                <g:hasRights lvlright="${right.REFDELETE.value()}">
                    <g:actionSubmit class="btn btn-danger" action="delete"
                                    value="${message(code: 'default.delete')}"
                                    onclick="return confirm('Etes vous sûre de vouloir supprimer ce prénom?');"/>
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