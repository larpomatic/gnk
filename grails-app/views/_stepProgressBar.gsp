<head>
    <meta charset="utf-8">
    <meta name="robots" content="noindex">

    <title>Form wizard (using tabs) - Bootsnipp.com</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'stepProgressBar.css')}" type="text/css">
    <script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>

    <g:javascript src="stepProgressBar/stepProgressBar.js"/>

    <script type="text/javascript">
        window.alert = function () {
        };
        var defaultCSS = document.getElementById('bootstrap-css');
        function changeCSS(css) {
            if (css) $('head > link').filter(':first').replaceWith('<link rel="stylesheet" href="' + css + '" type="text/css" />');
            else $('head > link').filter(':first').replaceWith(defaultCSS);
        }
        $(document).ready(function () {
            var iframe_height = parseInt($('html').height());
            window.parent.postMessage(iframe_height, 'http://bootsnipp.com');
        });
    </script>
</head>


<div class="container auto-width">
    <div class="row">
        <section>
            <div class="wizard">
                <div class="wizard-inner">
                    <div class="connecting-line"></div>
                    <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation" class="active">
                            <a class="first-icon" href="#selecintrigue" data-toggle="tab" aria-controls="step1" role="tab" title="Select your plot">
                                <span class="round-tab">
                                    <i class="glyphicon glyphicon-pencil"></i>
                                </span>
                            </a>
                        </li>

                        <li role="presentation" class="disabled">
                            <a href="#role2perso" data-toggle="tab" aria-controls="step2" role="tab" title="Give a role to your character">
                                <span class="round-tab">
                                    <i class="glyphicon glyphicon-user"></i>
                                </span>
                            </a>
                        </li>

                        <li role="presentation" class="disabled">
                            <a href="#life" data-toggle="tab" aria-controls="step3" role="tab" title="Life">
                                <span class="round-tab">
                                    <i class="glyphicon glyphicon-tower"></i>
                                </span>
                            </a>
                        </li>

                        <li role="presentation" class="disabled">
                            <a href="#naming" data-toggle="tab" aria-controls="step4" role="tab" title="Naming">
                                <span class="round-tab">
                                    <i class="glyphicon glyphicon-film"></i>
                                </span>
                            </a>
                        </li>

                        <li role="presentation" class="disabled">
                            <a href="#ressource" data-toggle="tab" aria-controls="step5" role="tab" title="Ressource">
                                <span class="round-tab">
                                    <i class="glyphicon glyphicon-film"></i>
                                </span>
                            </a>
                        </li>

                        <li role="presentation" class="disabled">
                            <a href="#place" data-toggle="tab" aria-controls="step6" role="tab" title="Place">
                                <span class="round-tab">
                                    <i class="glyphicon glyphicon-film"></i>
                                </span>
                            </a>
                        </li>

                        <li role="presentation" class="disabled">
                            <a href="#time" data-toggle="tab" aria-controls="step7" role="tab" title="Time">
                                <span class="round-tab">
                                    <i class="glyphicon glyphicon-film"></i>
                                </span>
                            </a>
                        </li>

                        <li role="presentation" class="disabled">
                            <a href="#publication" data-toggle="tab" aria-controls="step8" role="tab" title="Publication">
                                <span class="round-tab">
                                    <i class="glyphicon glyphicon-film"></i>
                                </span>
                            </a>
                        </li>

                    </ul>
                </div>

                <form role="form">
                    <div class="tab-content">
                        <div class="tab-pane active margin-top20 no-padding" role="tabpanel" id="selecintrigue">
                            <div class="span4 no-margin float-left">
                                <div class="center-button">
                                    <g:link action="getBack" id="${gnId}" class="btn btn-primary pull-right">
                                        <g:message code="default.back.label" default="Back"/>
                                    </g:link>
                                </div>
                            </div>
                            <div class="span4 text-center no-margin cadre">
                                <h4>Selectionnez vôtre intrigue</h4>
                                <p>Dans cette partie, creez ou editez vos GN.</p>
                            </div>
                            <div class="span4">
                                <div class="center-button">
                                    <button id="publication" onclick="return publicationAccess()"
                                            class="btn btn-primary"
                                            action="index">
                                        ${message(code: 'navbar.publication', default: 'Publication')}</button>
                                </div>
                            </div>
                        </div>

                        <div class="tab-pane active margin-top20 no-padding" role="tabpanel" id="role2perso">
                            <div class="span4 no-margin float-left">
                                <div class="center-button">
                                    <g:link action="getBack" id="${gnId}" class="btn btn-primary pull-right">
                                        <g:message code="default.back.label" default="Back"/>
                                    </g:link>
                                </div>
                            </div>
                            <div class="span4 text-center no-margin cadre">
                                <h4>Donnez un rôle à votre personnage</h4>

                                <p>Faites vivre votre personnage avec la personnalité qui vous séduira...</p>
                            </div>
                            <div class="span4">
                                <div class="center-button">
                                    <button id="publication" onclick="return publicationAccess()"
                                            class="btn btn-primary"
                                            action="index">
                                        ${message(code: 'navbar.publication', default: 'Publication')}</button>
                                </div>
                            </div>
                        </div>

                        <div class="tab-pane active margin-top20 no-padding" role="tabpanel" id="life">
                            <div class="span4 no-margin float-left">
                                <div class="center-button">
                                    <g:link action="getBack" id="${gnId}" class="btn btn-primary pull-right">
                                        <g:message code="default.back.label" default="Back"/>
                                    </g:link>
                                </div>
                            </div>

                            <div class="span4 text-center no-margin cadre">
                                <h4>Life</h4>

                                <p>Donnez vie à votre personnage...</p>
                            </div>

                            <div class="span4">
                                <div class="center-button">
                                    <button id="publication" onclick="return publicationAccess()"
                                            class="btn btn-primary"
                                            action="index">
                                        ${message(code: 'navbar.publication', default: 'Publication')}</button>
                                </div>
                            </div>
                        </div>

                        <div class="tab-pane active margin-top20 no-padding" role="tabpanel" id="naming">
                            <div class="span4 no-margin float-left">
                                <div class="center-button">
                                    <g:link action="getBack" id="${gnId}" class="btn btn-primary pull-right">
                                        <g:message code="default.back.label" default="Back"/>
                                    </g:link>
                                </div>
                            </div>

                            <div class="span4 text-center no-margin cadre">
                                <h4>Naming</h4>

                                <p>Donnez les noms les plus mysterieux à vos personnages...</p>
                            </div>

                            <div class="span4">
                                <div class="center-button">
                                    <button id="publication" onclick="return publicationAccess()"
                                            class="btn btn-primary"
                                            action="index">
                                        ${message(code: 'navbar.publication', default: 'Publication')}</button>
                                </div>
                            </div>
                        </div>

                        <div class="tab-pane active margin-top20 no-padding" role="tabpanel" id="ressource">
                            <div class="span4 no-margin float-left">
                                <div class="center-button">
                                    <g:link action="getBack" id="${gnId}" class="btn btn-primary pull-right">
                                        <g:message code="default.back.label" default="Back"/>
                                    </g:link>
                                </div>
                            </div>

                            <div class="span4 text-center no-margin cadre">
                                <h4>Ressource</h4>

                                <p>Donnez les ressources nécessaires donner vie à votre histoire...</p>
                            </div>

                            <div class="span4">
                                <div class="center-button">
                                    <button id="publication" onclick="return publicationAccess()"
                                            class="btn btn-primary"
                                            action="index">
                                        ${message(code: 'navbar.publication', default: 'Publication')}</button>
                                </div>
                            </div>
                        </div>

                        <div class="tab-pane active margin-top20 no-padding" role="tabpanel" id="place">
                            <div class="span4 no-margin float-left">
                                <div class="center-button">
                                    <g:link action="getBack" id="${gnId}" class="btn btn-primary pull-right">
                                        <g:message code="default.back.label" default="Back"/>
                                    </g:link>
                                </div>
                            </div>

                            <div class="span4 text-center no-margin cadre">
                                <h4>Place</h4>

                                <p>De la grèce antique à la planete Tatooine, l'univers est à vous...</p>
                            </div>

                            <div class="span4">
                                <div class="center-button">
                                    <button id="publication" onclick="return publicationAccess()"
                                            class="btn btn-primary"
                                            action="index">
                                        ${message(code: 'navbar.publication', default: 'Publication')}</button>
                                </div>
                            </div>
                        </div>

                        <div class="tab-pane active margin-top20 no-padding" role="tabpanel" id="time">
                            <div class="span4 no-margin float-left">
                                <div class="center-button">
                                    <g:link action="getBack" id="${gnId}" class="btn btn-primary pull-right">
                                        <g:message code="default.back.label" default="Back"/>
                                    </g:link>
                                </div>
                            </div>

                            <div class="span4 text-center no-margin cadre">
                                <h4>Time</h4>

                                <p>La courbe du temps n'a aucune emprise sur vous, soyez le maitre du temps de vôtre scénierio...</p>
                            </div>

                            <div class="span4">
                                <div class="center-button">
                                    <button id="publication" onclick="return publicationAccess()"
                                            class="btn btn-primary"
                                            action="index">
                                        ${message(code: 'navbar.publication', default: 'Publication')}</button>
                                </div>
                            </div>
                        </div>

                        <div class="tab-pane active margin-top20 no-padding" role="tabpanel" id="publication">
                            <div class="span4 no-margin float-left">
                                <div class="center-button">
                                    <g:link action="getBack" id="${gnId}" class="btn btn-primary pull-right">
                                        <g:message code="default.back.label" default="Back"/>
                                    </g:link>
                                </div>
                            </div>

                            <div class="span4 text-center no-margin cadre">
                                <h4>Publication</h4>

                                <p>Votre histoire est prête à être publiée...</p>
                            </div>

                            <div class="span4">
                                <div class="center-button">
                                    <button id="publication" onclick="return publicationAccess()"
                                            class="btn btn-primary"
                                            action="index">
                                        ${message(code: 'navbar.publication', default: 'Publication')}</button>
                                </div>
                            </div>
                        </div>

                        <div class="clearfix"></div>
                    </div>
                </form>
            </div>
        </section>
    </div>
</div>