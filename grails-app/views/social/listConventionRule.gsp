<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title>${message(code: 'adminRef.social.conventionRule.title')}</title>
</head>

<body>
<g:render template="../tag/subNav"/>
<fieldset>
    <h2><g:message code="adminRef.social.conventionRule.title"/></h2>
</fieldset>
<g:render template="../infosAndErrors" />
<g:render template="addConventions"/>
<table class="table table-bordered">
    <thead>
    <tr class="upper">
        <th style="text-align: center;">#</th>
        <th>Nom de la Cpnvention</th>
        <th>Règles</th>
        <th>Supprimer</th>
    </tr>
    </thead>
    <tbody>
    <g:each status="n" in="${conventionList}" var="conv">
        <tr id="char${conv.id}">
            <!-- convention rule -->
            <td style="text-align: center;">${n + 1}</td>
            <!-- description -->
            <td class="cap">${conv.description}</td>
            <!-- edition -->
            <td>
                <a href="#editmodal${conv.id}" role="button" class="btn" data-toggle="modal">Editer</a>
            </td>
            <!-- suppression -->
            <td>
                <g:form>
                    <fieldset class="buttons">
                        <g:hiddenField name="idConvention" value="${conv?.id}" />
                        <g:actionSubmit class="btn btn-warning" action="deleteConvention" value="${message(code: 'default.delete')}" onclick="return confirm('${message(code: 'adminRef.social.conventionRule.deleteConvention')}');" />
                    </fieldset>
                </g:form>
            </td>
        </tr>
    </g:each>
    </tbody>

</table>

<!-- Modal Views -->
<g:render template="modalEditConventionRule"/>

</body>
</html>
