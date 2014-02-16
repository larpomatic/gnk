<%@ page import="org.gnk.tag.Tag" %>
<%@ page import="org.gnk.selectintrigue.Plot" %>
<div id="create-tag" class="content scaffold-create" role="main">
	<legend>${message(code: 'adminRef.plot.TagToPlot')}</legend>
	<g:form action="save" >
		<form class="form-inline">
			<div class="row">
			<div class="span3">
	      			<g:select
		              name="Plot_select"
		              optionKey="id"
		              optionValue="name"
		              from="${Plot.list()}"
		              noSelection="['':'-Choix de l\'intrigue-']"/>
             	</div>
             	
     			<div class="span3">
	     			<g:select
		              name="Tag_select"
		              optionKey="id"
		              optionValue="name"
		              from="${Plot.getTagList()}"
		              noSelection="['':'-Choix du tag-']"/>
				</div>
				
				<div class="span4">
				poids (%) :
					<input class="span1" id="weight_select" name="weight" value="100" type="number">
				</div>
   			</div>
  			<g:actionSubmit class="btn btn-primary" action="addTagToPlot" value="${message(code: 'default.add')}" />
			
		</form>
	</g:form>
</div>
