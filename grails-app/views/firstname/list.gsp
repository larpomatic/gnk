<%@ page import="org.gnk.admin.right" %>
<%@ page import="org.gnk.tag.Tag" %>


<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title>Administration Firstname</title>
</head>
	<body>

    <g:render template="../tag/subNav" />
<div id="list-event" class="content scaffold-list" role="main">
    <fieldset>
        <h2>Firstnane existants</h2>
    </fieldset>



        <g:form>
            <fieldset class="buttons">

                    <g:actionSubmit class="btn btn-primary" action="create"
                                    value="+ Ajouter nouveau prénom"/>

           </fieldset>
       </g:form>
        <div id="list-name" class="content scaffold-list" role="main">


            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <fieldset class="form">
                <g:form action="list" method="GET">
                    <div class="fieldcontain">
                        <g:textField name="query" value="${params.query}" placeholder= "Rechercher"/>
                    </div>
                </g:form>
            </fieldset>
            <g:render template="table" model="[FirstnameInstanceList : FirstnameInstanceList]" />

            <div class="pagination">
                <g:paginate total="${firstnameTotal}" action="list" params="${params}"/>
            </div>
        </div>


</body>
</html>
