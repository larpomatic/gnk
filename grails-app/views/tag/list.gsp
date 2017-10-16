<%@ page import="org.gnk.tag.Tag; org.gnk.admin.right" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<title>${message(code: 'adminRef.tag.title')}</title>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
            <meta name="viewport" content="width=device-width" />
            <link rel="stylesheet" href="//static.jstree.com/latest/assets/dist/themes/default/style.min.css" />
            <style>
            #container { min-width:320px; margin:0px auto 0 auto; background:white; border-radius:0px; padding:0px; overflow:hidden; }
            #tree { float:left; min-width:319px; border-right:1px solid silver; overflow:auto; padding:0px 0; }
            #data { margin-left:320px; }
            #data textarea { margin:0; padding:0; height:100%; width:100%; border:0; background:white; display:block; line-height:18px; }
            #data #code { font: normal normal normal 12px/18px 'Consolas', monospace !important; }
            </style>
	</head>
	<body>
    <input type="hidden" id="path" value="<g:resource dir="images/tag" file="true.png"/>"/>
        <g:render template="subNav" />
		<div id="list-tag" class="content scaffold-list" role="main">
            <fieldset>
                <h2><g:message code="adminRef.tag.title"/></h2>
            </fieldset>

			<g:render template="../infosAndErrors" />
        <div class="modal-body">
            <ul class="Tags">
                <g:render template="jstreeold"/>

    <g:javascript src="redactIntrigue/bootstrap-confirmation.js"/>
    <g:javascript src="tag/addTagChild.js"/>
    <g:javascript src="tag/deleteTag.js"/>
    <g:javascript src="tag/modalTag.js"/>
    </body>
</html>







