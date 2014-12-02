<%@ page import="org.gnk.admin.right" contentType="text/html;charset=UTF-8" %>
<head>
<meta name='layout' content='main' />
<title><g:message code="springSecurity.denied.title" /></title>
</head>

<body>
<div class='body'>
	<div class='errors'><g:message code="security.denied" /></div>
    <g:link  controller="home" action="index" type="button" class="btn btn-primary">Retour à la page d'accueil</g:link>
</div>
</body>
