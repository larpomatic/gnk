<%@ page import="org.gnk.admin.right" %>
<%@ page import="org.gnk.tag.Tag" %>


<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title>Administration Patronymes</title>
</head>
	<body>

    <g:render template="../tag/subNav" />
<div id="list-event" class="content scaffold-list" role="main">
    <fieldset>
        <h2>Patronymes existants</h2>
    </fieldset>

    <g:render template="../infosAndErrors" />

        <g:form>
            <fieldset class="buttons">
                <g:hasRights lvlright="${right.REFMODIFY.value()}">
                    <g:actionSubmit class="btn btn-primary" action="create"
                                    value="+ Ajouter nouveau patronyme"/>
               </g:hasRights>
           </fieldset>
       </g:form>
        <div id="list-name" class="content scaffold-list" role="main">


            <fieldset class="form">
                <g:form action="list" method="GET">
                    <div class="fieldcontain">
                        <g:textField name="query" value="${params.query}" placeholder= "Rechercher"/>
                    </div>
                </g:form>
            </fieldset>
            <g:render template="table" model="[NameInstanceList : NameInstanceList]" />

            <div class="pagination">
                <g:paginate total="${nameTotal}" action="list" params="${params}"/>
            </div>
        </div>



</body>
</html>
