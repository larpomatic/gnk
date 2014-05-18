<%@ page import="org.gnk.selectintrigue.Plot"%>
<g:hiddenField name="screenStep" value="1" />

<div class="tabbable tabs-left">

	<ul class="nav nav-tabs leftUl">
        <li class="active leftMenuList">
            <a href="#newRelation" data-toggle="tab" class="addRelation">
                <g:message code="redactintrigue.relation.addRelation" default="New relation"/>
            </a>
        </li>
		<g:each in="${plotInstance.roles}" status="i5" var="role">
            <li class="leftMenuList">
                <a href="#roleRelation_${role.id}" data-toggle="tab">
                    ${role.code}
                </a>
            </li>
		</g:each>
	</ul>

	<div class="tab-content">
        <div class="tab-pane active" id="newRelation">
            <form name="newRelationForm" data-url="">
                %{--<div style="margin:auto">--}%
                    <div class="row formRow">
                        <div class="span1">
                            <label for="relationType">
                                <g:message code="redactintrigue.relation.type" default="Relation type"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:select name="relationType" id="relationType" from="${['Filiation', 'Parent', 'Fratrie', 'Amour']}"
                                      keys="${['Filiation', 'Parent', 'Fratrie', 'Amour']}" required=""/>
                        </div>
                        <div class="span1">
                            <label for="relationHidden">
                                <g:message code="redactintrigue.relation.hidden" default="Hidden"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:checkBox name="relationHidden" id="relationHidden"/>
                        </div>
                    </div>
                    <div class="row formRow">
                        <div class="span1">
                            <label for="relationFrom">
                                <g:message code="redactintrigue.relation.from" default="From"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:select name="relationFrom" id="relationFrom" from="${['Role1', 'Role2', 'Role3', 'Role4']}"
                                      keys="${['Role1', 'Role2', 'Role3', 'Role4']}" required=""/>
                        </div>
                        <div class="span1">
                            <label for="relationTo">
                                <g:message code="redactintrigue.relation.to" default="To"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:select name="relationTo" id="relationTo" from="${['Role1', 'Role2', 'Role3', 'Role4']}"
                                      keys="${['Role1', 'Role2', 'Role3', 'Role4']}" required=""/>
                        </div>
                    </div>
                    <div class="row formRow">
                        <div class="span1">
                            <label for="relationBijective">
                                <g:message code="redactintrigue.relation.bijective" default="Bijective"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:checkBox name="relationBijective" id="relationBijective"/>
                        </div>
                        <div class="span1">
                            <label for="relationWeight">
                                <g:message code="redactintrigue.relation.weight" default="Weight"/>
                            </label>
                        </div>
                        <div class="span4">
                            <g:field type="number" name="relationWeight" id="relationWeight" required=""/>
                        </div>
                    </div>
                    <div class="row formRow text-center">
                        <label for="relationDescription">
                            <g:message code="redactintrigue.relation.description" default="Description"/>
                        </label>
                    </div>
                    <g:textArea name="relationDescription" id="relationDescription" value="" rows="5" cols="100"/>
                %{--</div>--}%
                <input type="button" name="Insert" value="Insert" class="btn btn-primary insertRelation"/>
            </form>
        </div>


		<g:each in="${plotInstance.roles}" status="i4" var="role">
			<div class="tab-pane" id="roleRelation_${role.id}">
				<table class="table table-bordered table-striped">
					<thead>
						<tr>
							<th><g:message code="redactintrigue.relation.to"
									default="To" /></th>
							<th><g:message code="redactintrigue.relation.bijective"
									default="Bijective" /></th>
							<th><g:message code="redactintrigue.relation.type"
									default="Relation type" /></th>
							<th><g:message code="redactintrigue.relation.weight"
									default="Weight" /></th>
							<th></th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<g:each in="${role.roleHasRelationWithRolesForRole1Id}"
							status="relationNumber" var="roleHasRelationWithRoles">
							<g:render template="relationRow"
								model="['roleHasRelationWithRoles':roleHasRelationWithRoles, 'plotInstance':plotInstance,  'isRole1':true]" />
						</g:each>
						<g:each in="${role.roleHasRelationWithRolesForRole2Id}"
							status="relationNumber" var="roleHasRelationWithRoles">
							<g:if test="${roleHasRelationWithRoles.isBijective}">
								<g:render template="relationRow"
									model="['roleHasRelationWithRoles':roleHasRelationWithRoles, 'isRole1':false]" />
							</g:if>
						</g:each>
					</tbody>
				</table>
			</div>
		</g:each>
	</div>
</div>