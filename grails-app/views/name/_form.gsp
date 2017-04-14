<%@ page import="org.gnk.naming.Name" %>
<%@ page import="org.gnk.naming.NameHasTag" %>
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
                    <label for="name">
                        <g:message code="Name.name.label" default="Titre"/>
                        <span class="required-indicator">*</span>
                    </label>
                    <g:textField name="name" value="${NameInstance?.name}" required=""/>
                </div>
            </div>

            <div class="span4">
                <div class="fieldcontain ${hasErrors(bean: NameInstance, field: 'gender', 'error')} required">
                    <label for="gender">
                        <g:message code="Name.gender.label" default="Genre"/>
                        <span class="required-indicator">*</span>
                    </label>
                    <g:field name="gender" type="" min="0" value="${NameInstance.gender}"
                             required=""/>
                </div>
            </div>

        </div>
    </div>


    %{--Generic event has tag--}%
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">
                Tags
                <a href="#tagsModalHasTag" class="btn" data-toggle="modal">
                    Choix des tags
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
                            %{--<g:if test="${!(Tag.findByName("Tag Univers").id == genericEventHasTag.tag.parent.id)}">--}%
                            <li class="badge badge-info">
                                <g:hiddenField name="tableTag_${NameHasTagList.id}_${i}" value="${NameHasTagList.tag.id}_${NameHasTagList.value}"/>
                                <span style="color:black">${NameHasTagList.tag.name} ${NameHasTagList.value}%</span>
                            </li>
                            %{--</g:if>--}%
                        </ul>
                        </g:each>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
        <div class="modal-footer">
            %{--<button class="btn" data-dismiss="modal">Ok</button>--}%
            <g:actionSubmit class="save" action="edit" value="Ok"/>

        </div>
    </div>
</div>