
<link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<link rel="stylesheet" href="${resource(dir: 'css', file: 'stepProgressBar.css')}" type="text/css">
<script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script>
    function submitSelectIntrigue()
    {
        $('#edit-plot form').append('<input type=\'hidden\' name=\'_action_goToRoleToPerso\'>');
        $('#edit-plot form').submit()
    }
</script>

<g:javascript src="stepProgressBar/stepProgressBar.js"/>

<div class="container auto-width">
    <div class="row">
        <section>
            <div class="wizard">
                <div class="wizard-inner">
                    <div class="connecting-line"></div>
                    <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation"<g:if test="${currentStep == 'selectIntrigue'}"> class="active"</g:if>>
                            <g:if test="${currentStep == 'roleToPerso'}">
                                <g:link action="getBack" id="${gnId}" class="prev">
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Intrigues',glyph='pencil']"/>
                                </g:link>
                            </g:if>
                            <g:else>
                                <a href="#"<g:if test="${currentStep != 'selectIntrigue'}"> class="inactive"</g:if>>
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Intrigues',glyph='pencil']"/>
                                </a>
                            </g:else>
                        </li>

                        <li role="presentation"<g:if test="${currentStep == 'roleToPerso'}"> class="active"</g:if>>
                            <g:if test="${currentStep == 'life'}">
                                <g:link action="getBack" controller="life" id="${gnInstance.id}" class="prev">
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Personnages',glyph='user']"/>
                                </g:link>
                            </g:if>
                            <g:elseif test="${currentStep == 'selectIntrigue'}">
                                <a onclick="submitSelectIntrigue()" class="next">
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Personnages',glyph='user']"/>
                                </a>
                            </g:elseif>
                            <g:else>
                                <a href="#"<g:if test="${currentStep != 'roleToPerso'}"> class="inactive"</g:if>>
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Personnages',glyph='user']"/>
                                </a>
                            </g:else>
                        </li>

                        <li role="presentation"<g:if test="${currentStep == 'life'}"> class="active"</g:if>>
                            <g:if test="${currentStep == 'naming'}">
                                <g:link action="getBack" id="${gnId}" class="prev">
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Vie',glyph='tower']"/>
                                </g:link>
                            </g:if>
                            <g:elseif test="${currentStep == 'roleToPerso'}">
                                <a onclick="$('form#form-life').submit()" class="next">
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Vie',glyph='tower']"/>
                                </a>
                            </g:elseif>
                            <g:else>
                                <a href="#"<g:if test="${currentStep != 'life'}"> class="inactive"</g:if>>
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Vie',glyph='tower']"/>
                                </a>
                            </g:else>
                        </li>

                        <li role="presentation"<g:if test="${currentStep == 'naming'}"> class="active"</g:if>>
                            <g:if test="${currentStep == 'ressource'}">
                                <g:link action="getBack" id="${gnId}" class="prev">
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Identité',glyph='font']"/>
                                </g:link>
                            </g:if>
                            <g:elseif test="${currentStep == 'roleToPerso'}">
                                <a onclick="$('form#form-naming').submit()" class="next">
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Identité',glyph='font']"/>
                                </a>
                            </g:elseif>
                            <g:elseif test="${currentStep == 'life'}">
                                <a onclick="return publicationAccess()" action="index" class="next">
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Identité',glyph='font']"/>
                                </a>
                            </g:elseif>
                            <g:else>
                                <a href="#"<g:if test="${currentStep != 'naming'}"> class="inactive"</g:if>>
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Identité',glyph='font']"/>
                                </a>
                            </g:else>
                        </li>

                        <li role="presentation"<g:if test="${currentStep == 'ressource'}"> class="active"</g:if>>
                            <g:if test="${currentStep == 'place'}">
                                <g:link action="getBack" id="${gnId}" class="prev">
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Ressources',glyph='cog']"/>
                                </g:link>
                            </g:if>
                            <g:elseif test="${currentStep == 'naming'}">
                                <a onclick="return publicationAccess()" action="index" class="next">
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Ressources',glyph='cog']"/>
                                </a>
                            </g:elseif>
                            <g:else>
                                <a href="#"<g:if test="${currentStep != 'ressource'}"> class="inactive"</g:if>>
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Ressources',glyph='cog']"/>
                                </a>
                            </g:else>
                        </li>

                        <li role="presentation"<g:if test="${currentStep == 'place'}"> class="active"</g:if>>
                            <g:if test="${currentStep == 'time'}">
                                <g:link action="getBack" id="${gnId}" class="prev">
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Lieux',glyph='map-marker']"/>
                                </g:link>
                            </g:if>
                            <g:elseif test="${currentStep == 'ressource'}">
                                <a onclick="return publicationAccess()" action="index" class="next">
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Lieux',glyph='map-marker']"/>
                                </a>
                            </g:elseif>
                            <g:else>
                                <a href="#"<g:if test="${currentStep != 'place'}"> class="inactive"</g:if>>
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Lieux',glyph='map-marker']"/>
                                </a>
                            </g:else>
                        </li>

                        <li role="presentation"<g:if test="${currentStep == 'time'}"> class="active"</g:if>>
                            <g:if test="${currentStep == 'publication'}">
                                <g:link action="getBack" id="${gnId}" class="prev">
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Époque',glyph='calendar']"/>
                                </g:link>
                            </g:if>
                            <g:elseif test="${currentStep == 'place'}">
                                <a onclick="return publicationAccess()" action="index" class="next">
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Époque',glyph='calendar']"/>
                                </a>
                            </g:elseif>
                            <g:else>
                                <a href="#"<g:if test="${currentStep != 'time'}"> class="inactive"</g:if>>
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Époque',glyph='calendar']"/>
                                </a>
                            </g:else>
                        </li>

                        <li role="presentation"<g:if test="${currentStep == 'publication'}"> class="active"</g:if>>
                            <g:if test="${currentStep == 'time'}">
                                <a onclick="return publicationAccess()" action="index" class="next">
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Publication',glyph='film']"/>
                                </a>
                            </g:if>
                            <g:else>
                                <a href="#"<g:if test="${currentStep != 'publication'}"> class="inactive"</g:if>>
                                    <g:render template="../stepBarProgress/stepContent" model="[label='Publication',glyph='film']"/>
                                </a>
                            </g:else>
                        </li>
                    </ul>
                </div>
            </div>
        </section>
    </div>
</div>