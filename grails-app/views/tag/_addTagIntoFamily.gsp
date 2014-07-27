<%@ page import="org.gnk.tag.Tag" %>
<div id="show-tag" class="content scaffold-create" role="main">
	<legend>${message(code: 'adminRef.tag.TagParent')}</legend>
	<g:form action="list" >
		<form class="form-inline">
			<div class="row">
     			<div class="span3">
	     			%{--<g:select--}%
		              %{--name="Tag_select"--}%
		              %{--optionKey="id"--}%
		              %{--optionValue="name"--}%
		              %{--from="${org.gnk.tag.Tag.list()}"--}%
		              %{--noSelection="['':'-Choix du parent-']"/>--}%
                    <a href="#tagList" class="btn" data-toggle="modal">
                    Choisir le Tag parent
                    </a>
                    <input data-content="tagList" disabled="disabled" name="Tag_parent" value="${tagParent}" />
				</div>
                <div id="tagList" class="modal hide fade tags-modal" tabindex="-1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">×</button>
                        <h3 id="myModalLabel">Choix du parent</h3>
                        <input class="input-medium search-query" data-content="tagList"
                               placeholder="<g:message code="selectintrigue.search" default="Search..."/>"/>
                    </div>
                    <div class="modal-body">
                        <ul class="tagList">
                            <g:each in="${genericTags}" status="i" var="tagInstance">
                                <g:render template="TagTree" model="[tagInstance: tagInstance]"/>
                            </g:each>
                        </ul>
                    </div>
                    <div class="modal-footer">
                        %{--<button class="btn" data-dismiss="modal">Ok</button>--}%
                        <g:actionSubmit class="btn btn-primary" id="children" action="list" value="${message(code: 'default.ok')}" />
                    </div>
                </div>
                <g:hiddenField name="showChildren" value="true"/>
   			</div>
		</form>
	</g:form>
</div>
