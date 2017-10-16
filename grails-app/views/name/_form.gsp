<%@ page import="org.gnk.naming.Name" %>
<%@ page import="org.gnk.tag.Tag" %>
<%@ page import="org.gnk.admin.right" %>
<style>
.modalLi {
    list-style-type: none;
}
</style>

%{--<div class="form-inline">--}%
<div>
    <g:hiddenField name="first" value="true"/>

    <div class="form-group">
        <div class="row">
            <div class="span4">
                <div class="fieldcontain ${hasErrors(bean: NameInstance, field: 'name', 'error')} required">
                    <label for="title">
                        <g:message code="Name.title.label" default="Name"/>
                        <span class="required-indicator">*</span>
                    </label>
                    <g:textField name="name" value="${NameInstance?.name}" required=""/>
                </div>
            </div>
        </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">
                Tags
                <a href="#tagsModalHasTag" class="btn btn-primary" data-toggle="modal">
                    Selection de tags
                </a>
            </h3>

        </div>

        <div class="panel-body">
            <table id="listTableNameHasTag" class="table table-bordered">
                <thead>
                <tr>
                    <th>Tags</th>
                </tr>

                </thead>
                <tbody>
                <tr>
                    <td>
                        <g:each in="${NameHasTagList}" status="i" var="NameHasTag">
                            <ul class="inline">
                                <li class="badge badge-info">
                                    <g:hiddenField name="tableTag_${NameHasTag.id}_${i}" value="${NameHasTag.tag.id}_${NameHasTag.weight}"/>
                                    <span style="color:black">${NameHasTag.tag.name} ${NameHasTag.weight}%</span>
                                </li>
                            </ul>
                        </g:each>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>


    %{--Modal--}%
    <div id="tagsModalHasTag" class="modal hide fade tags-modal" tabindex="-1">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">×</button>

            <h3>Tags</h3>
            <input class="input-medium search-query" data-content="NameTags"
                   placeholder="<g:message code="redactintrigue.generalDescription.search" default="Search..."/>"/>
            <button type="button" class="btn btn-primary modifyTag push">
                <g:message code="redactintrigue.generalDescription.validatedTags" default="Validated tags"/>
            </button>
        </div>

        %{--<form>--}%
        <g:hiddenField name="NameHasTagAdd" value="true"/>
        <div class="modal-body">
            <ul class="NameTags">
                <g:each in="${TagInstanceList}" status="i" var="tagInstance">
                    <g:render template="NameTagTree"
                              model="[tagInstance : tagInstance, NameInstance: NameInstance,
                                      referenceTag: NameInstance.extTags]"/>
                </g:each>
            </ul>
        </div>

        <div class="modal-footer">
            %{--<button class="btn" data-dismiss="modal">Ok</button>--}%
            <g:actionSubmit class="btn btn-primary" action="edit" value="Ok"/>
        </div>
        %{--</form>--}%


    </div>
</div>