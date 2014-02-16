<%@ page import="org.gnk.selectintrigue.Plot"%>
<g:hiddenField name="screenStep" value="1" />

<div class="tabbable tabs-left">

	<ul class="nav nav-tabs"
		style="height: 400pt; width: 175pt; overflow-y: auto; overflow-x: hidden;">
		<g:each in="${plotInstance.roles}" status="i5" var="role">
			<li class="${(i5==0?'active':'null')}"><a href="#role_${role.id}" data-toggle="tab"> ${role.code}
			</a></li>
		</g:each>
	</ul>

	<div class="tab-content">
		<g:each in="${plotInstance.roles}" status="i4" var="role">
			<div class="${(i4==0?'tab-pane active':'tab-pane')}" id="role_${role.id}">
				<table class="table table-bordered table-striped">
					<thead>
						<!-- En-tête du tableau -->
						<tr>
							<th><g:message code="redactintrigue.relation.to"
									default="To" /></th>
							<th><g:message code="redactintrigue.relation.bijective"
									default="Bijective" /></th>
							<th><g:message code="redactintrigue.relation.type"
									default="Relation type" /></th>
							<th><g:message code="redactintrigue.relation.weight"
									default="Weight" /></th>
							<th width="50"></th>
							<th width="50"></th>
						</tr>
					</thead>

					<tbody>
						<!-- Corps du tableau -->

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