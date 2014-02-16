<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<title>${message(code: 'naming.testtitle')}</title>
	</head>
	<body>
		<g:remoteLink action="test1" update="test1">Test 1 : HelloWorld</g:remoteLink>
		<div id="test1"></div><br/>
		<g:remoteLink action="test2" update="test2">Test 2 : Affichage de l'input d'un personnage sans tag</g:remoteLink>
		<div id="test2"></div><br/>
		<g:remoteLink action="test3" update="test3">Test 3 : Sortie après passage dans le naming d'un personnage sans tag</g:remoteLink>
		<div id="test3"></div><br/>
		
		<g:remoteLink action="test4" update="test4">Test 4 : Affichage de l'input d'un personnage avec tag</g:remoteLink>
		<div id="test4"></div><br/>
		<g:remoteLink action="test5" update="test5">Test 5 : Sortie après passage dans le naming d'un personnage avec tag</g:remoteLink>
		<div id="test5"></div><br/>
		
		<g:remoteLink action="test6" update="test6">Test 6 : Affichage de l'input d'un personnage avec tag qui n'est plus dans la base de données ou n'a aucun lien avec les prenoms</g:remoteLink>
		<div id="test6"></div><br/>
		<g:remoteLink action="test7" update="test7">Test 7 : Sortie après passage dans le naming d'un personnage avec tag qui n'est plus dans la base de données ou n'a aucun lien avec les prenoms</g:remoteLink>
		<div id="test7"></div><br/>
		
		<g:remoteLink action="test8" update="test8">Test 8 : Affichage de l'input de plusieurs personnages de la même famille</g:remoteLink>
		<div id="test8"></div><br/>
		<g:remoteLink action="test9" update="test9">Test 9 : Sortie après passage dans le naming de plusieurs personnages de la même famille</g:remoteLink>
		<div id="test9"></div><br/>
		
		<g:remoteLink action="test10" update="test10">Test 10 : 10 personnages</g:remoteLink>
		<div id="test10"></div><br/>
	</body>
</html>










