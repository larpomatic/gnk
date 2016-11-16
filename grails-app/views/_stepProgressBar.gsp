<head>
    <meta charset="utf-8">
    <meta name="robots" content="noindex">

    <title>Form wizard (using tabs) - Bootsnipp.com</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <link rel="stylesheet" href="../">
    <script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        window.alert = function(){};
        var defaultCSS = document.getElementById('bootstrap-css');
        function changeCSS(css){
            if(css) $('head > link').filter(':first').replaceWith('<link rel="stylesheet" href="'+ css +'" type="text/css" />');
            else $('head > link').filter(':first').replaceWith(defaultCSS);
        }
        $( document ).ready(function() {
            var iframe_height = parseInt($('html').height());
            window.parent.postMessage( iframe_height, 'http://bootsnipp.com');
        });
    </script>
</head>




<div class="container">
    <div class="row">
        <section>
            <div class="wizard">
                <div class="wizard-inner">
                    <div class="connecting-line"></div>
                    <ul class="nav nav-tabs" role="tablist">

                        <li role="presentation" class="active">
                            <a href="#step1" data-toggle="tab" aria-controls="step1" role="tab" title="Step 1">
                                <span class="round-tab">
                                    <i class="glyphico
                                    n glyphicon-folder-open"></i>
                                </span>
                            </a>
                        </li>

                        <li role="presentation" class="disabled">
                            <a href="#step2" data-toggle="tab" aria-controls="step2" role="tab" title="Step 2">
                                <span class="round-tab">
                                    <i class="glyphicon glyphicon-pencil"></i>
                                </span>
                            </a>
                        </li>
                        <li role="presentation" class="disabled">
                            <a href="#step3" data-toggle="tab" aria-controls="step3" role="tab" title="Step 3">
                                <span class="round-tab">
                                    <i class="glyphicon glyphicon-picture"></i>
                                </span>
                            </a>
                        </li>

                        <li role="presentation" class="disabled">
                            <a href="#complete" data-toggle="tab" aria-controls="complete" role="tab" title="Complete">
                                <span class="round-tab">
                                    <i class="glyphicon glyphicon-ok"></i>
                                </span>
                            </a>
                        </li>
                    </ul>
                </div>

                <form role="form">
                    <div class="tab-content">
                        <div class="tab-pane active center" role="tabpanel" id="step1">
                            <h3>Substitution</h3>

                            <p>Dans cette partie, creez ou editez vos GN.</p>
                            <ul class="list-inline pull-right">
                                <li><button type="button" class="btn btn-primary next-step">Save and continue</button>
                                </li>
                            </ul>
                        </div>

                        <div class="tab-pane center" role="tabpanel" id="step2">
                                <h3>Step 2</h3>

                            <p>This is step 2</p>
                            <div class="form-actions">
                                <g:link action="getBack" id="${gnId}" class="btn btn-primary pull-right">
                                    <g:message code="default.back.label" default="Back"/>
                                </g:link>
                                <button id="publication" onclick="return publicationAccess()" class="btn btn-primary" action="index">
                                    ${message(code: 'navbar. publication', default: 'Publication')}</button>
                            </div>
                        </div>

                        <div class="tab-pane center" role="tabpanel" id="step3">
                            <h3>Step 3</h3>

                            <p>This is step 3</p>
                            <ul class="list-inline pull-right">
                                <li><button type="button" class="btn btn-default prev-step">Previous</button></li>
                                <li><button type="button" class="btn btn-default next-step">Skip</button></li>
                                <li><button type="button"
                                            class="btn btn-primary btn-info-full next-step">Save and continue</button>
                                </li>
                            </ul>
                        </div>

                        <div class="tab-pane center" role="tabpanel" id="complete">
                            <h3>Complete</h3>

                            <p>You have successfully completed all steps.</p>
                        </div>

                        <div class="clearfix"></div>
                    </div>
                </form>
            </div>
        </section>
    </div>
</div>