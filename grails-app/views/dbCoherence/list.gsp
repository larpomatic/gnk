<%--
  Created by IntelliJ IDEA.
  User: aurel_000
  Date: 19/01/14
  Time: 21:04
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main">
  <title>Cohérence de la base de données</title>
</head>
<body>
<g:render template="../tag/subNav" />
    <table class="table table-bordered">
    <thead>
    <tr>
        <th>#</th>
        <g:sortableColumn property="title" title="titre" />
        <g:sortableColumn property="description" title="description" />
        <g:sortableColumn property="query" title="query" />
        <th>Résultat</th>
    </tr>
    </thead>
    <tbody>
        <g:each in="${queryList}" status="i" var="queryInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                <td>${i+1}</td>
                <td>${fieldValue(bean: queryInstance, field: "title")}</td>
                <td>${fieldValue(bean: queryInstance, field: "description")}</td>
                <td>${fieldValue(bean: queryInstance, field: "query")}</td>
                <td>${fieldValue(bean: queryInstance, field: "result")}</td>
            </tr>
        </g:each>
        </tbody>
    </table>
</body>
</html>