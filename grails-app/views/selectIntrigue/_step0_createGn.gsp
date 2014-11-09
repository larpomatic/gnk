<%@ page import="org.gnk.selectintrigue.Plot" %>
<%@ page import="org.gnk.gn.Gn" %>

<style type="text/css">
input {
    text-align: right;
    width: 180px;
    margin-right: 25px;
}

select {
    width: 195px;
}
</style>
<legend>
    <g:message code="default.edit.label" args="[entityName]"/>
</legend>
<g:if test="${flash.message}">
    <div class="alert notificationBox alert-block alert-danger fade in">
        <button type="button" class="close" data-dismiss="alert">×</button>
        <h4 class="alert-heading"><g:message code="${flash.message}" args="[]"/></h4>
    </div>
</g:if>

<g:form method="post" class="gnSubmitForm">
    <g:hiddenField name="id" value="${gnInstance?.id}"/>
    <g:hiddenField name="version" value="${gnInstance?.version}"/>
    <fieldset class="form">
        <g:hiddenField name="screenStep" value="0"/>
        <g:hiddenField name="gnStep" value="selectIntrigue"/>
        <table>
            <tr>
                <td><label for="name"><g:message
                        code="selectintrigue.step0.gnName" default="GN Name"/>
                </label></td>
                <td><g:textField name="name" value="${gnInstance?.name}"
                                 required="required"/></td>

                <td><label for="gnArchitechture"><g:message
                        code="selectintrigue.step0.gnArchitecture" default="Architecture"/>
                </label></td>
                <td><g:select name="gnArchitechture" id="gnArchitechture"
                              from="${['Mainstream', 'Parallelisé']}" keys="${[true, false]}"
                              value="${gnInstance?.isMainstream}" required=""/></td>


                <td><label for="gnUnivers"><g:message
                        code="selectintrigue.step0.gnUnivers" default="Univers"/>
                </label></td>
                <td>
                    <g:select name="univers" id="gnUnivers" from="${universList}" optionKey="id" value="${gnInstance?.univers?.id}" />
                </td>
            </tr>
            <tr>
                <td colspan="2" class="text-center">
                    <a href="#tagsModal" class="btn" data-toggle="modal">Choisir l'ambiance du GN</a>
                </td>
                <td colspan="2" class="text-center">
                    <a href="#tagsEvenementialModal" class="btn"
                       data-toggle="modal">Choisir l'ambiance évenementielle</a>
                </td>
                <td colspan="2" class="text-center">
                    <a href="#tagsMainstreamModal" class="btn" data-toggle="modal">Choisir l'ambiance Mainstream</a>
                </td>
            </tr>
            <tr>

                <td><label for="gnPIPMin"><g:message
                        code="selectintrigue.step0.gnPIPMin" default="PIP Min"/></label></td>
                <td><input id="gnPIPMin" name="gnPIPMin" required="required"
                           value="${gnInstance?.pipMin}" type="number"></td>


                <td><label for="gnPIPMax"><g:message
                        code="selectintrigue.step0.gnPIPMax" default="PIP Max"/></label></td>
                <td><input id="gnPIPMax" name="gnPIPMax" required="required"
                           value="${gnInstance?.pipMax}" type="number"></td>
                <td>
                    <label for="gnPIPCore">
                        <g:message code="selectintrigue.step0.gnPIPCore" default="PIP Max"/>
                    </label>
                </td>
                <td>
                    <input id="gnPIPCore" name="gnPIPCore" value="${gnInstance?.pipCore}" type="number"
                           required="required">
                </td>

            </tr>

            <tr>
                <td><label for="gnNbPlayers"><g:message
                        code="selectintrigue.step0.gnNbPlayers"
                        default="Number of players"/></label></td>
                <td><input id="gnNbPlayers" name="gnNbPlayers" required="required"
                           value="${gnInstance?.nbPlayers}" type="number"></td>

                <td><label for="gnNbWomen"><g:message
                        code="selectintrigue.step0.gnNbWomen"
                        default="Minimal women number"/></label></td>
                <td><input id="gnNbWomen" name="gnNbWomen" required="required"
                           value="${gnInstance?.nbWomen}" type="number"></td>


                <td><label for="gnNbMen"><g:message
                        code="selectintrigue.step0.gnNbMen" default="Minimal men number"/></label></td>
                <td><input id="gnNbMen" name="gnNbMen" required="required"
                           value="${gnInstance?.nbMen}" type="number"></td>

            </tr>

            <tr>
                <td><label for="gnDateHour"><g:message
                        code="selectintrigue.step0.gnDate" default="Virtual GN Date"/></label></td>
                <td>
                    <div class="input-append">
                        <input type="text" id="gnDateHour" name="gnDateHour" placeholder="jj/mm/aaaa hh:mm"
                               required="required"
                               value="${formatDate(format: 'dd/MM/yyyy HH:mm', date: gnInstance?.date)}"/>
                        <g:if test="${formatDate(format: 'G', date: gnInstance?.date) == 'BC'}">
                            <input type="hidden" name="gnDateHourUnity" value="-"/>
                            <span class="add-on btn">
                                <i>- JC</i>
                            </span>
                        </g:if>
                        <g:else>
                            <input type="hidden" name="gnDateHourUnity" value="+"/>
                            <span class="add-on btn">
                                <i>+ JC</i>
                            </span>
                        </g:else>
                    </div>
                </td>
                <td colspan="2"></td>
                <td><label for="t0DateHour"><g:message
                        code="selectintrigue.step0.t0Date" default="Actual GN Date"/></label></td>
                <td>
                    <div class="input-append">
                        <input type="text" id="t0DateHour" name="t0DateHour" placeholder="jj/mm/aaaa hh:mm"
                               required="required"
                               value="${formatDate(format: 'dd/MM/yyyy HH:mm', date: gnInstance?.t0Date)}"/>
                        <g:if test="${formatDate(format: 'G', date: gnInstance?.t0Date) == 'BC'}">
                            <input type="hidden" name="t0DateHourUnity" value="-"/>
                            <span class="add-on btn">
                                <i>- JC</i>
                            </span>
                        </g:if>
                        <g:else>
                            <input type="hidden" name="t0DateHourUnity" value="+"/>
                            <span class="add-on btn">
                                <i>+ JC</i>
                            </span>
                        </g:else>
                    </div>
                </td>

            </tr>

            <tr>
                <td><label for="gnDuration"><g:message
                        code="selectintrigue.step0.gnDuration" default="GN duration"/></label></td>
                <td><div class="input-append">
                    <input class="span2" name="gnDuration" id="gnDuration"
                           type="number" value="${gnInstance?.duration}" required="required" pattern="\d*"
                           style="margin-right: 0px;" min="1"/><span class="add-on">h</span>
                </div></td>

                <td colspan="2"></td>

                <td><label for="gnConvention"><g:message
                        code="selectintrigue.step0.gnConvention" default="Convention"/>
                </label></td>
                <td><g:select name="convention" id="gnConvention"
                              from="${conventionList}" optionKey="id"/></td>

            </tr>
        </table>
        <g:render template="tagsPopup"
                  model="['idPopup': 'tagsModal', 'namePopup': 'Tags', 'myOwner': gnInstance, 'tagList': plotTagList, 'tagPrefix': 'tags_', 'weightTagPrefix': 'weight_tags_', 'tagListName': 'BaseTags']"/>
        <g:render template="tagsPopup"
                  model="['idPopup': 'tagsMainstreamModal', 'namePopup': 'Tags Mainstream', 'myOwner': gnInstance, 'tagList': plotTagList, 'tagPrefix': 'tagsMainstream_', 'weightTagPrefix': 'weight_tagsMainstream_', 'tagListName': 'MainstreamTags']"/>
        <g:render template="tagsPopup"
                  model="['idPopup': 'tagsEvenementialModal', 'namePopup': 'Tags évenementiels', 'myOwner': gnInstance, 'tagList': plotTagList, 'tagPrefix': 'tagsEvenemential_', 'weightTagPrefix': 'weight_tagsEvenemential_', 'tagListName': 'EvenementialTags']"/>
    </fieldset>

    <div class="form-actions">
        <g:actionSubmit class="btn btn-primary" action="saveOrUpdate"
                        value="${message(code: 'default.button.update.label', default: 'Update')}"/>
        <g:actionSubmit action="delete" class="btn btn-primary"
                        value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                        formnovalidate=""
                        onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
    </div>
</g:form>