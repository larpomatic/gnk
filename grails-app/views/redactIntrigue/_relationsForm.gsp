<%@ page import="org.gnk.selectintrigue.Plot"%>
%{--<g:hiddenField name="screenStep" value="1" />--}%

<div class="tabbable tabs-left relationScreen">
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
            <form name="newRelationForm" data-url="<g:createLink controller="Relation" action="Save"/>">
                <div class="row formRow">
                    <div class="span2">
                        <label for="relationType">
                            <g:message code="redactintrigue.relation.type" default="Relation type"/>
                        </label>
                    </div>
                    <div class="span7">
                        <g:select name="relationType" id="relationType" from="${relationTypes}"
                                  optionKey="id" required="" optionValue="name"/>
                    </div>
                </div>
                <div class="row formRow">
                    <div class="span1">
                        <label for="relationFrom">
                            <g:message code="redactintrigue.relation.from" default="From"/>
                        </label>
                    </div>
                    <div class="span4">
                        <g:select name="relationFrom" id="relationFrom" from="${plotInstance.roles}"
                                  optionKey="id" required="" optionValue="code"/>
                    </div>
                    <div class="span1">
                        <label for="relationTo">
                            <g:message code="redactintrigue.relation.to" default="To"/>
                        </label>
                    </div>
                    <div class="span4">
                        <g:select name="relationTo" id="relationTo" from="${plotInstance.roles}"
                                  optionKey="id" required="" optionValue="code"/>
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
                        <label for="relationExclusive">
                            <g:message code="redactintrigue.relation.exclusive" default="Exclusive"/>
                        </label>
                    </div>
                    <div class="span4">
                        <g:checkBox name="relationExclusive" id="relationExclusive"/>
                    </div>

                </div>
                <div class="row formRow">
                    <div class="span1">
                        <label for="relationWeight">
                            <g:message code="redactintrigue.relation.weight" default="Weight"/>
                        </label>
                    </div>
                    <div class="span4">
                        <g:field type="number" name="relationWeight" id="relationWeight" required=""/>
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
                <div class="row formRow text-center">
                    <label for="relationDescription">
                        <g:message code="redactintrigue.relation.description" default="Description"/>
                    </label>
                </div>
                <g:textArea name="relationDescription" id="relationDescription" value="" rows="5" cols="100"/>
                <input type="button" name="Insert" value="Insert" class="btn btn-primary insertRelation"/>
            </form>
        </div>

		<g:each in="${plotInstance.roles}" status="i4" var="role">
            <div class="tab-pane" id="roleRelation_${role.id}">
                <div class="accordion" id="accordionRelation${role.id}">
                    <g:each in="${role.roleHasRelationWithRolesForRole1Id}" var="relationFrom">
                        <div class="accordion-group" data-relation="${relationFrom.id}" data-roleTo="${relationFrom.role2.id}">
                            <div class="accordion-heading">
                                <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordionRelation${role.id}"
                                   href="#collapseRelation${role.id}-${relationFrom.id}">
                                    ${relationFrom.roleRelationType.name}
                                </a>
                                <span class="text-center" data-roleId="${relationFrom.role2.id}">
                                    <g:message code="redactintrigue.relation.to" default="To"/>: ${relationFrom.role2.code}
                                </span>
                                <span class="text-right">
                                    <g:if test="${relationFrom.isBijective}">
                                        <span><g:img dir="images/redactIntrigue/relations" file="validate.png" /></span>
                                    </g:if>
                                    <g:else>
                                        <span><g:img dir="images/redactIntrigue/relations" file="forbidden.png" /></span>
                                    </g:else>
                                </span>
                                <button data-toggle="confirmation-popout" data-placement="left" class="btn btn-danger pull-right" title="Supprimer cette relation?"
                                        data-url="<g:createLink controller="Relation" action="Delete" id="${relationFrom.id}"/>" data-object="relation" data-id="${relationFrom.id}">
                                    <i class="icon-remove pull-right"></i>
                                </button>
                            </div>
                            <div id="collapseRelation${role.id}-${relationFrom.id}" class="accordion-body collapse">
                                <div class="accordion-inner">
                                    <g:render template="updateRelation" model="['relationFrom':relationFrom, 'role':role, 'isRole1':true]" />
                                </div>
                            </div>
                        </div>
                    </g:each>
                    <g:each in="${role.roleHasRelationWithRolesForRole2Id}" var="relationTo">
                        <g:if test="${relationTo.isBijective}">
                            <div class="accordion-group" data-relation="${relationTo.id}" data-roleTo="${relationTo.role1.id}">
                                <div class="accordion-heading">
                                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordionRelation${role.id}"
                                       href="#collapseRelation${role.id}-${relationTo.id}">
                                        ${relationTo.roleRelationType.name}
                                    </a>
                                    <span class="text-center" data-roleId="${relationTo.role1.id}">
                                        <g:message code="redactintrigue.relation.to" default="To"/>: ${relationTo.role1.code}
                                    </span>
                                    <span class="text-right">
                                        <g:if test="${relationTo.isBijective}">
                                            <span><g:img dir="images/redactIntrigue/relations" file="validate.png" /></span>
                                        </g:if>
                                        <g:else>
                                            <span><g:img dir="images/redactIntrigue/relations" file="forbidden.png" /></span>
                                        </g:else>
                                    </span>
                                    <button data-toggle="confirmation-popout" data-placement="left" class="btn btn-danger pull-right" title="Supprimer cette relation?"
                                            data-url="<g:createLink controller="Relation" action="Delete" id="${relationTo.id}"/>" data-object="relation" data-id="${relationTo.id}">
                                        <i class="icon-remove pull-right"></i>
                                    </button>
                                </div>
                                <div id="collapseRelation${role.id}-${relationTo.id}" class="accordion-body collapse">
                                    <div class="accordion-inner">
                                        <g:render template="updateRelation" model="['relationFrom':relationTo, 'role':role, 'isRole1':false]" />
                                    </div>
                                </div>
                            </div>
                        </g:if>
                    </g:each>
                </div>
            </div>

			%{--<div class="tab-pane" id="roleRelation_${role.id}">--}%
				%{--<table class="table table-bordered table-striped">--}%
					%{--<thead>--}%
						%{--<tr>--}%
							%{--<th><g:message code="redactintrigue.relation.to"--}%
									%{--default="To" /></th>--}%
							%{--<th><g:message code="redactintrigue.relation.bijective"--}%
									%{--default="Bijective" /></th>--}%
							%{--<th><g:message code="redactintrigue.relation.type"--}%
									%{--default="Relation type" /></th>--}%
							%{--<th><g:message code="redactintrigue.relation.weight"--}%
									%{--default="Weight" /></th>--}%
							%{--<th></th>--}%
							%{--<th></th>--}%
						%{--</tr>--}%
					%{--</thead>--}%
					%{--<tbody>--}%
						%{--<g:each in="${role.roleHasRelationWithRolesForRole1Id}"--}%
							%{--status="relationNumber" var="roleHasRelationWithRoles">--}%
							%{--<g:render template="relationRow"--}%
								%{--model="['roleHasRelationWithRoles':roleHasRelationWithRoles, 'plotInstance':plotInstance,  'isRole1':true]" />--}%
						%{--</g:each>--}%
						%{--<g:each in="${role.roleHasRelationWithRolesForRole2Id}"--}%
							%{--status="relationNumber" var="roleHasRelationWithRoles">--}%
							%{--<g:if test="${roleHasRelationWithRoles.isBijective}">--}%
								%{--<g:render template="relationRow"--}%
									%{--model="['roleHasRelationWithRoles':roleHasRelationWithRoles, 'isRole1':false]" />--}%
							%{--</g:if>--}%
						%{--</g:each>--}%
					%{--</tbody>--}%
				%{--</table>--}%
			%{--</div>--}%
		</g:each>
	</div>
</div>