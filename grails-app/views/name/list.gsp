<%@ page import="org.gnk.admin.right" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title>Administration Name</title>
</head>
	<body>

    <g:render template="../tag/subNav" />
<div id="list-event" class="content scaffold-list" role="main">
    <fieldset>
        <h2>List des Noms</h2>
    </fieldset>

    <g:render template="../infosAndErrors" />

        <g:form>
            <fieldset class="buttons">
                <g:hasRights lvlright="${right.REFMODIFY.value()}">
                    <g:actionSubmit class="btn btn-primary" action="create"
                                    value="${message(code: 'default.button.create.label')}"/>
                </g:hasRights>
            </fieldset>
        </g:form>
    <g:render template="table" model="[NameInstanceList : NameInstanceList]" />

    </div>

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
