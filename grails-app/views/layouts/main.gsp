<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Genotron"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
        <link href='http://fonts.googleapis.com/css?family=Gudea' rel='stylesheet' type='text/css'>
		<r:require modules="bootstrap"/>
		<g:layoutHead/>
		<r:layoutResources />
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">

        %{--<link rel="stylesheet" href="${resource(dir: 'css', file: 'redactIntrigue.css')}" type="text/css">--}%
	</head>
	<body>
        <div class="gnk">
            <g:render template="/navbar"/>
            <g:javascript src="handlebars-v1.3.0.js"/>
            <g:javascript src="lodash.js"/>
            <g:javascript library="application"/>
            <r:layoutResources />
            <div class="page-body container">
                <g:layoutBody/>
            </div>
            <g:render template="/footer" />
            <div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>

        </div>
	</body>
</html>
