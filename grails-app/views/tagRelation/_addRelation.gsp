<%@ page import="org.gnk.tag.Tag" %>
<%@ page import="org.gnk.resplacetime.Resource" %>

<div id="create-relation" class="content scaffold-create" role="main">
	<legend>Ajout d'une relation entre 2 tags</legend>
	<g:form action="save" >
		<form class="form-inline">
			<div class="row">
				<div class="span2.5">
	      			<g:select
		              name="Tag_select"
		              optionKey="id"
		              optionValue="name"
		              from="${Tag.list()}"
		              noSelection="['':'-Choix du premier tag-']"/>
	           	</div>
	             	
	   			<div class="span2.5">
	     			<g:select
		              name="Tag2_select"
		              optionKey="id"
		              optionValue="name"
		              from="${Tag.list()}"
		              noSelection="['':'-Choix du deuxième tag-']"/>
				</div>
					
				<div class="span2">
					Relation bijective :
	     			<g:checkBox name="bijective" value="${true}" />
				</div>
					
				<div class="span4">
					Poids (%) :
					<input class="span1" id="weight_select" name="weight" value="100" min=-101 max=101 type="number">
					<div></div>
					Valeur entre -101% (incompatibilité absolue) et 101% (l'un découle de l'autre)
				</div>
   			</div>
  			<g:actionSubmit class="btn btn-primary" action="addRelation" value="${message(code: 'default.add')}" />
		</form>
	</g:form>
</div>
