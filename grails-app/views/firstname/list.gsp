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

    <g:render template="../infosAndErrors" />

        <g:form>
            <fieldset class="buttons">
                <g:hasRights lvlright="${right.REFMODIFY.value()}">
                    <g:actionSubmit class="btn btn-primary" action="create"
                                    value="+ Ajouter nouveau prénom"/>
               </g:hasRights>
           </fieldset>
       </g:form>
   <g:render template="table" model="[FirstnameInstanceList : FirstnameInstanceList]" />

<script type="application/javascript">
$(function(){
$("#listTable").DataTable({
    "language": {
        "url": "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/French.json"
    }
});
});
</script>
<g:javascript src="redactIntrigue/bootstrap-confirmation.js"/>

</body>
</html>
