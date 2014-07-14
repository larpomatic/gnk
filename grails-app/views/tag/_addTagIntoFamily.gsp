<%@ page import="org.gnk.tag.Tag" %>
%{--<%@ page import="org.gnk.tag.TagFamily" %>--}%
<div id="create-tag" class="content scaffold-create" role="main">
	<legend>${message(code: 'adminRef.tag.TagIntoFamily')}</legend>
	<g:form action="save" >
		<form class="form-inline">
			<div class="row">
     			<div class="span3">
	     			<g:select
		              name="Tag_select"
		              optionKey="id"
		              optionValue="name"
		              from="${Tag.list()}"
		              noSelection="['':'-Choix du tag-']"/>
				</div>
      			<div class="span3">
	      			%{--<g:select--}%
		              %{--name="TagFamily_select"--}%
		              %{--optionKey="id"--}%
		              %{--optionValue="value"--}%
		              %{--from="${TagFamily.list()}"--}%
		              %{--noSelection="['':'-Choix de la famille de tag-']"/>--}%
             	</div>
   			</div>
			<g:actionSubmit class="btn btn-primary" action="addTagIntoFamily" value="${message(code: 'default.add')}" />
		</form>
	</g:form>
</div>
