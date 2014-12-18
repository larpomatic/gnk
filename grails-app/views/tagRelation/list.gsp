<%@ page import="org.gnk.tag.TagRelation" %>
<%@ page import="org.gnk.tag.Tag" %>

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'tagRelation.label', default: 'TagRelation')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<g:render template="/tag/subNav"/>
<div id="list-resource" class="content scaffold-list" role="main">
    <fieldset>
        <h2>Administration des relations entre tags</h2>
    </fieldset>

    <h2>Recherche</h2>
    <g:form action="search" controller="tagRelation">
        <div class="row">
            <div class="span4">
                <label>Tag1</label>
                <input name="tag1Rel" placeholder="Tag1">
            </div>

            <div class="span4">
                <label>Tag2</label>
                <input name="tag2Rel" placeholder="Tag2">
            </div>

            <div class="span4">
                <label>Type</label>
                <select name="selectTypeRelation">
                    <option selected value="1">Indifférencié</option>
                    <option value="2">Bijective</option>
                    <option value="3">Non Bijective</option>
                </select>
            </div>
        </div>
        <button type="submit" class="btn btn-primary">Rechercher</button>
    </g:form>
    <div>
        <g:render template="../infosAndErrors"/>
    </div>

    <g:render template="addRelation"/>
    <g:render template="tableRelations" model="[listTagParent: listTagParent, trs : trs]"/>

    <div class="pagination">
        <g:if test="${trs != null}">
            <g:paginate total="${trs.totalCount}"/>
        </g:if>
        <g:else>
            <g:paginate total="${tagRelationInstanceList.totalCount}"/>
        </g:else>
    </div>

</div>
</body>
</html>
