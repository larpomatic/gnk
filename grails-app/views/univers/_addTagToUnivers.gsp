<%@ page import="org.gnk.tag.Tag" %>
<%@ page import="org.gnk.tag.Univers" %>
<div id="create-tag" class="content scaffold-create" role="main">
	<h3>${message(code: 'adminRef.univers.TagToUnivers')}</h1>
	<g:form action="save" >
		<form class="form-inline">
			<div class="row">
			<div class="span3">
	      			<g:select
		              name="Univers_select"
		              optionKey="id"
		              optionValue="name"
		              from="${Univers.list()}"
		              noSelection="['':'-Choix de l\'univers-']"/>
             	</div>
             	
     			<div class="span3">
	     			<g:select
		              name="Tag_select"
		              optionKey="id"
		              optionValue="name"
		              from="${Tag.list()}"
		              noSelection="['':'-Choix du tag-']"/>
				</div>
   			</div>
  			<fieldset class="buttons">
				<g:actionSubmit class="btn btn-primary" action="addTagToUnivers" value="${message(code: 'default.add')}" />
			</fieldset>
		</form>
	</g:form>
</div>
