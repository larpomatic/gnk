<%@ page import="org.gnk.tag.Tag" %>
<%@ page import="org.gnk.resplacetime.Resource" %>

<div id="create-tag" class="content scaffold-create" role="main">
	<legend>Ajout d'un tag sur une ressource</legend>
	<g:form action="save" >
		<form class="form-inline">
			<div class="row">
			<div class="span3">
	      			<g:select
		              name="Resource_select"
		              optionKey="id"
		              optionValue="name"
		              from="${Resource.list()}"
		              noSelection="['':'-Choix de la ressource-']"/>
             	</div>
             	
     			<div class="span3">
	     			<g:select
		              name="Tag_select"
		              optionKey="id"
		              optionValue="name"
		              from="${Tag.list()}"
		              noSelection="['':'-Choix du tag-']"/>
				</div>
				
				<div class="span6">
					Poids (%) :
					<input class="span1" id="weight_select" name="weight" value="100" min=-101 max=101 type="number">
					<div></div>
					Valeur entre -101% (incompatibilité absolue) et 101% (l'un découle de l'autre)
				</div>
   			</div>
  			<g:actionSubmit class="btn btn-primary" action="addTagToResource" value="${message(code: 'default.add')}" />
		</form>
	</g:form>
</div>
