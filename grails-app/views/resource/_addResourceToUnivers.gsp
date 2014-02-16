<%@ page import="org.gnk.tag.Tag" %>
<%@ page import="org.gnk.tag.Univers" %>
<%@ page import="org.gnk.resplacetime.Resource" %>
<div id="create-tag" class="content scaffold-create" role="main">
	<legend>${message(code: 'adminRef.resource.ResourceToUnivers')}</legend>
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
		              name="Univers_select"
		              optionKey="id"
		              optionValue="name"
		              from="${Univers.list()}"
		              noSelection="['':'-Choix de l\'univers-']"/>
				</div>
				
				<div class="span4">
					poids (%) :
					<input title="poids" class="span1" id="weight_select" name="weight" value="100" type="number"> 
				</div>
   			</div>
  			<g:actionSubmit class="btn btn-primary" action="addResourceToUnivers" value="${message(code: 'default.add')}" />
		</form>
	</g:form>
</div>
