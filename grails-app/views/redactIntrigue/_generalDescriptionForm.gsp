<%@ page import="org.gnk.selectintrigue.Plot"%>
<script>
	function toggle(checkboxID, toggleID) {
		var checkbox = document.getElementById(checkboxID);
		var toggle = document.getElementById(toggleID);
		updateToggle = toggle.disabled = !checkbox.checked;
	}
</script>

<div class="span3" style="height: 100%; border-right: solid 1px #ddd; margin: 2px">
    <ul class="nav nav-tabs" style="border-bottom: none">
        <li class="active" style="width: 100%">
            <a href="#" data-toggle="tab">
            Dans l'intrigue
            </a>
        </li>
    </ul>
    <div class="dropdown">
        <a href="#" data-toggle="dropdown" class="btn btn-primary btn-large btn-block">
            4 rôles
        </a>
        <ul class="dropdown-menu" role="menu" aria-labelledby="dLabel" style="width: 100%">
            <li><a tabindex="-1" href="#">Dark Vador</a></li>
            <li><a tabindex="-1" href="#">Luke</a></li>
            <li><a tabindex="-1" href="#">Yoda</a></li>
            <li><a tabindex="-1" href="#">Obiwan</a></li>
        </ul>
    </div>
    <div class="dropdown">
        <a href="#" data-toggle="dropdown" class="btn btn-primary btn-large btn-block">
            2 lieux
        </a>
        <ul class="dropdown-menu" role="menu" aria-labelledby="dLabel" style="width: 100%">
            <li><a tabindex="-1" href="#">Etoile noire</a></li>
            <li><a tabindex="-1" href="#">Hoth</a></li>
        </ul>
    </div>
    <div class="dropdown">
        <a href="#" data-toggle="dropdown" class="btn btn-primary btn-large btn-block">
            3 objets
        </a>
        <ul class="dropdown-menu" role="menu" aria-labelledby="dLabel" style="width: 100%">
            <li><a tabindex="-1" href="#">Sabre laser</a></li>
            <li><a tabindex="-1" href="#">Pisto laser</a></li>
            <li><a tabindex="-1" href="#">X-Wing</a></li>
        </ul>
    </div>
    <div class="dropdown">
        <a href="#" data-toggle="dropdown" class="btn btn-primary btn-large btn-block">
            0 relation
        </a>
        <ul class="dropdown-menu" role="menu" aria-labelledby="dLabel" style="width: 100%">
        </ul>
    </div>
</div>

<div class="span8">
    <g:form method="post">
        <g:hiddenField name="id" value="${plotInstance?.id}" />
        <g:hiddenField name="version" value="${plotInstance?.version}" />
        <fieldset class="form">
            <g:hiddenField name="screenStep" value="0" />
            <table style="margin:auto">
                <tr>
                    <td><label for="plotName"> <g:message
                                code="redactintrigue.generalDescription.plotName"
                                default="Plot's Name" />
                    </label></td>
                    <td><g:textField name="name" value="${plotInstance?.name}"
                            required="" />
                        </div></td>
                    <td><label for="plotUnivers"> <g:message
                                code="redactintrigue.generalDescription.plotUnivers"
                                default="Universes" />
                    </label></td>
                    <td><a href="#universesModal" role="button" class="btn"
                        data-toggle="modal">Choisir les univers</a></td>
                </tr>
                <tr>
                    <td><label for="isPublic"> <g:message
                                code="redactintrigue.generalDescription.isPublic"
                                default="Public" />
                    </label></td>
                    <td><g:checkBox id="isPublic" name="isPublic"
                            checked="${plotInstance.isPublic}" /></td>
                    <td><label for="tags"> <g:message
                                code="redactintrigue.generalDescription.tags" default="Tags" />
                    </label></td>

                    <td><a href="#tagsModal" role="button" class="btn"
                        data-toggle="modal">Choisir les tags</a></td>
                </tr>
                <tr>
                    <td><label for="isMainstream"> <g:message
                                code="redactintrigue.generalDescription.isMainstream"
                                default="Mainstream" />
                    </label></td>
                    <td><g:checkBox name="isMainstream" id="isMainstream"
                            checked="${plotInstance.isMainstream}" /></td>
                    <td><label for="isEvenemential"> <g:message
                                code="redactintrigue.generalDescription.isEvenemential"
                                default="Evenemential" />
                    </label></td>
                    <td><g:checkBox name="isEvenemential" id="isEvenemential"
                            checked="${plotInstance.isEvenemential}" /></td>
                </tr>
                <tr>
                    <td colspan="4"><label for="plotDescription"> <g:message
                                code="redactintrigue.generalDescription.plotDescription"
                                default="Plot Description" />
                    </label></td>
                </tr>
                <tr>
                    <td colspan="4"><g:textArea name="plotDescription"
                            id="plotDescription" value="${plotInstance.description}" rows="5"
                            cols="100" /></td>
                </tr>
            <tr>
                <td colspan="5">
                    <g:render template="richTextEditor" />
                </td>
            </tr>
            </table>

            <div id="universesModal" class="modal hide fade" tabindex="-1"
                role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×</button>
                    <h3 id="myModalLabel">Univers</h3>
                </div>
                <div class="modal-body">

                    <table>
                        <g:each in="${universList}" status="i" var="universInstance">
                            <g:if test="${i % 3 == 0}">
                                <tr>
                            </g:if>
                            <td><label for="universes_${universInstance.id}"><g:checkBox
                                        name="universes_${universInstance.id}"
                                        id="universes_${universInstance.id}"
                                        checked="${plotInstance.hasUnivers(universInstance)}" /> ${fieldValue(bean: universInstance, field: "name")}</label></td>
                            <g:if test="${(i+1) % 3 == 0}">
                                </tr>
                            </g:if>
                        </g:each>

                    </table>
                </div>
                <div class="modal-footer">
                    <button class="btn" data-dismiss="modal" aria-hidden="true">Ok</button>
                </div>
            </div>

            <div id="tagsModal" class="modal hide fade" tabindex="-1"
                role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">×</button>
                    <h3 id="myModalLabel">Tags</h3>
                </div>
                <div class="modal-body">

                    <table>
                        <g:each in="${plotTagList}" status="i" var="plotTagInstance">
                            <g:if test="${i % 3 == 0}">
                                <tr>
                            </g:if>
                            <td><label for="tags_${plotTagInstance.id}" style="float: left"><g:checkBox
                                        name="tags_${plotTagInstance.id}"
                                        id="tags_${plotTagInstance.id}"
                                        onClick="toggle('tags_${plotTagInstance?.id}', 'weight_tags_${plotTagInstance?.id}')"
                                        checked="${plotInstance.hasPlotTag(plotTagInstance)}" /> ${fieldValue(bean: plotTagInstance, field: "name")}</label>
                                        <div style="overflow: hidden; padding-left: .5em;">
                                        <g:if test="${plotInstance.hasPlotTag(plotTagInstance)}">
                                            <g:set var="tagValue" value="${plotInstance.getTagWeight(plotTagInstance)}" scope="page" />
                                        </g:if>
                                        <g:if test="${!plotInstance.hasPlotTag(plotTagInstance)}">
                                            <g:set var="tagValue" value="50" scope="page" />
                                        </g:if>
                                        <input id="weight_tags_${plotTagInstance?.id}" name="weight_tags_${plotTagInstance?.id}" value="${tagValue}" type="number" style="width:40px;"></div>
                                        <script>
                                            toggle('tags_${plotTagInstance?.id}', 'weight_tags_${plotTagInstance?.id}')
                                        </script>


                                        </td>
                            <g:if test="${(i+1) % 3 == 0}">
                                </tr>
                            </g:if>
                        </g:each>

                    </table>
                </div>
                <div class="modal-footer">
                    <button class="btn" data-dismiss="modal" aria-hidden="true">Ok</button>
                </div>
            </div>
        </fieldset>
        <fieldset class="buttons text-center">
            <g:actionSubmit class="save" action="update"
                value="${message(code: 'default.button.update.label', default: 'Update')}" />
            <g:actionSubmit class="delete" action="delete"
                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                formnovalidate=""
                onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
    </g:form>
</div>