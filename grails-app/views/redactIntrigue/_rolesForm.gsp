<%@ page import="org.gnk.selectintrigue.Plot" %>
<g:hiddenField name="screenStep" value="1"/>

<div class="tabbable tabs-left">

    <ul class="nav nav-tabs" style="height:400pt;width:175pt;overflow-y: auto;overflow-x: hidden;">
        <li class="active"><a href="#newRole" data-toggle="tab">Ajouter
        un nouveau rôle</a></li>
        <g:each in="${plotInstance.roles}" status="i5" var="role">
            <li class=""><a href="#role_${role.id}" data-toggle="tab">
                ${role.code}
            </a></li>
        </g:each>
    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="newRole">
            <g:form name="newRoleForm" url="[controller: 'role', action: 'save']">
                <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
                <table>
                    <tr>
                        <td><label for="roleCode"><g:message
                                code="redactintrigue.role.roleCode" default="Role code"/>
                        </label></td>
                        <td colspan="2"><g:textField name="roleCode"
                                                     id="roleCode" value="" required=""/></td>
                    </tr>
                    <tr>
                        <td><label for="roleTags"><g:message
                                code="redactintrigue.generalDescription.tags"
                                default="Tags"/>
                        </label></td>
                        <td><a href="#roleTagsModal" role="button" class="btn"
                               data-toggle="modal">Choisir les tags</a></td>
                        <td><label for="roleType"><g:message
                                code="redactintrigue.role.roleType" default="Type"/>
                        </label></td>
                        <td><g:select name="roleType" id="roleType" from="${['PJ', 'PNJ', 'PHJ']}"
                                      keys="${['PJ', 'PNJ', 'PHJ']}" required=""/></td>
                    </tr>
                    <tr>
                        <td><label for="rolePipi"><g:message
                                code="redactintrigue.role.rolePipi" default="PIPI"/>
                        </label></td>
                        <td><g:field type="number" name="rolePipi" id="rolePipi" value=""
                                     required=""/></td>
                        <td><label for="rolePipr"><g:message
                                code="redactintrigue.role.rolePipr" default="PIPR"/>
                        </label></td>
                        <td><g:field type="number" name="rolePipr" id="rolePipr" value=""
                                     required=""/></td>
                    </tr>
                    <tr>
                        <td colspan="4"><label for="roleDescription"><g:message
                                code="redactintrigue.role.roleDescription"
                                default="Description"/>
                        </label></td>
                    </tr>
                    <tr>
                        <td colspan="4"><g:textArea name="roleDescription"
                                                    id="roleDescription" value="" rows="5" cols="100"/></td>
                    </tr>
                </table>

                <div id="roleTagsModal" class="modal hide fade" tabindex="-1"
                     role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                                aria-hidden="true">×</button>

                        <h3 id="myModalLabel">Tags</h3>
                    </div>

                    <div class="modal-body">

                        <table>
                            <g:each in="${roleTagList}" status="i2" var="roleTagInstance">
                                <g:if test="${i2 % 3 == 0}">
                                    <tr>
                                </g:if>
                                <td><label for="roleTags_${roleTagInstance.id}"><g:checkBox
                                        name="roleTags_${roleTagInstance.id}"
                                        id="roleTags_${roleTagInstance.id}"
                                        checked="false"/> ${fieldValue(bean: roleTagInstance, field: "name")}</label>
                                </td>
                                <g:if test="${(i2 + 1) % 3 == 0}">
                                    </tr>
                                </g:if>
                            </g:each>

                        </table>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal" aria-hidden="true">Ok</button>
                    </div>
                </div>
                <g:submitButton name="Insert" value="Insert"/>
            </g:form>
        </div>
        <g:each in="${plotInstance.roles}" status="i4" var="role">
            <div class="tab-pane" id="role_${role.id}">

                <g:form name="updateRole_${role.id}" url="[controller: 'role', action: 'update']">
                    <g:hiddenField name="id" value="${role.id}"/>
                    <input type="hidden" name="plotId" id="plotId" value="${plotInstance?.id}"/>
                    <table>
                        <tr>
                            <td><label for="roleCode"><g:message
                                    code="redactintrigue.role.roleCode" default="Role code"/>
                            </label></td>
                            <td colspan="2"><g:textField name="roleCode"
                                                         id="roleCode" value="${role.code}" required=""/></td>
                        </tr>
                        <tr>
                            <td><label for="roleTags"><g:message
                                    code="redactintrigue.generalDescription.tags"
                                    default="Tags"/>
                            </label></td>
                            <td><a href="#roleTagsModal_${role.id}" role="button" class="btn"
                                   data-toggle="modal">Choisir les tags</a></td>
                            <td><label for="roleType"><g:message
                                    code="redactintrigue.role.roleType" default="Type"/>
                            </label></td>
                            <td><g:select name="roleType" id="roleType" from="${['PJ', 'PNJ', 'PHJ']}"
                                          keys="${['PJ', 'PNJ', 'PHJ']}" value="${role.type}" required=""/></td>
                        </tr>
                        <tr>
                            <td><label for="rolePipi"><g:message
                                    code="redactintrigue.role.rolePipi" default="PIPI"/>
                            </label></td>
                            <td><g:field name="rolePipi" id="rolePipi" type="number" value="${role.pipi}"
                                         required=""/></td>
                            <td><label for="rolePipr"><g:message
                                    code="redactintrigue.role.rolePipr" default="PIPR"/>
                            </label></td>
                            <td><g:field type="number" name="rolePipr" id="rolePipr" value="${role.pipr}"
                                         required=""/></td>
                        </tr>
                        <tr>
                            <td colspan="4"><label for="roleDescription"><g:message
                                    code="redactintrigue.role.roleDescription"
                                    default="Description"/>
                            </label></td>
                        </tr>
                        <tr>
                            <td colspan="4"><g:textArea name="roleDescription"
                                                        id="roleDescription" value="${role.description}" rows="5"
                                                        cols="100"/></td>
                        </tr>
                    </table>

                    <div id="roleTagsModal_${role.id}" class="modal hide fade" tabindex="-1"
                         role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"
                                    aria-hidden="true">×</button>

                            <h3 id="myModalLabel">Tags</h3>
                        </div>

                        <div class="modal-body">

                            <table>
                                <g:each in="${roleTagList}" status="i3" var="roleTagInstance">
                                    <g:if test="${i3 % 3 == 0}">
                                        <tr>
                                    </g:if>
                                    <td><label><g:checkBox
                                            name="roleTags_${roleTagInstance.id}"
                                            id="roleTags_${roleTagInstance.id}"
                                            checked="${role.hasRoleTag(roleTagInstance)}"/> ${fieldValue(bean: roleTagInstance, field: "name")}</label>
                                    </td>
                                    <g:if test="${(i3 + 1) % 3 == 0}">
                                        </tr>
                                    </g:if>
                                </g:each>

                            </table>
                        </div>

                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal" aria-hidden="true">Ok</button>
                        </div>
                    </div>
                    <g:submitButton name="Update" value="Update"/>
                    <g:actionSubmit class="delete" controller="role" action="delete"
                                    value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                    formnovalidate=""
                                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
                </g:form>
            </div>
        </g:each>
    </div>
</div>