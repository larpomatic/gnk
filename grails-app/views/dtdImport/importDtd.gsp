<%--
  Created by IntelliJ IDEA.
  User: aurel_000
  Date: 25/11/13
  Time: 08:26
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title>Import de la DTD</title>
</head>
<body>
<g:render template="../tag/subNav" />
<g:render template="../infosAndErrors" />
<legend>Import d'un GN au format DTD</legend>
<g:uploadForm controller="dtdImport" action="readFromDTD" method="post" >
    <tr class="prop">
        <td valign="top">
            <input type="file" id="file" name="file"/>
        </td>
    </tr>
    <fieldset class="buttons">
        <g:submitButton name="create" class="btn btn-primary" value="Envoyer" />
    </fieldset>
</g:uploadForm>