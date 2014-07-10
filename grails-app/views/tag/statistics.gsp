<%@ page import="org.gnk.tag.Univers; org.gnk.roletoperso.Role; org.gnk.resplacetime.Resource; org.gnk.tag.Tag" %>
%{--<%@ page import="org.gnk.tag.TagFamily" %>--}%
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<title>${message(code: 'adminRef.tag.stats.title')}</title>
	</head>
	<body>
        <g:render template="/tag/subNav" />
		<div id="list-tag" class="content scaffold-list" role="main">
            <fieldset>
                <legend><g:message code="adminRef.tag.stats.title" /></legend>
            </fieldset>
			<h4>${message(code: 'adminRef.tag.stats.global')}</h4>
			<ul>
				<li>Nombre de tags dans la base : ${Tag.count()}</li>
				%{--<li>Nombre de familles de tag dans la base : ${TagFamily.count()}</li>--}%
                <li>Nombre d'univers dans la base : ${Univers.count()}</li>
                <li>Nombre de rôles dans la base : ${Role.count()}</li>
                <li>Nombre de ressources dans la base : ${Resource.count()}</li>
			</ul>
			
			<h4>${message(code: 'adminRef.tag.stats.detail')}</h4>
			
			<div>Le module de statistiques est encore en version 0.1 et ne gère pas les statistiques détaillées. </div>
             <div>Si vous voulez voir le détail de l'utilisation d'un tag, cliquez sur "voir le détail".</div>

            <div class="row">
                <div class="span6">
                    <g:render template="tableTagsStat" />
                </div>
                <div class="span6">
                    %{--<g:render template="../tagFamily/tableTagFamiliesStat" />--}%
                </div>
            </div>
        </div>
	</body>
</html>










