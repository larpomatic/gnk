<link rel="stylesheet" href="<g:resource dir="css" file="testMenuSlide.css"/>" type="text/css">
<section id="article27" class="crayon article-css-27 demoTime">
    <div id="demoWrap">
        <div id="wrap1">
        <div id="wrap2">
        <div id="wrap3">
        <div id="wrap4">
        <div id="wrap5">
        <div id="wrap6">
        <div id="wrap7">
        <div id="wrap8">
        <div id="wrap9">
            <div id="background">
                <ul class="wrapUl">
                    <li class="wrapLi roleLi">
                        <a class="wrapLink" href="#wrap1" onclick="bgenScroll();">
                            <span>Roles</span>
                        </a>
                        <span class="badge badge-inverse">${plotInstance.roles.size()}</span>
                    </li>
                    <li class="wrapLi placeLi">
                        <a class="wrapLink" href="#wrap2" onclick="bgenScroll();">
                            <span>Lieux</span>
                        </a>
                        <span class="badge badge-inverse">3</span>
                    </li>
                    <li class="wrapLi resourceLi">
                        <a class="wrapLink" href="#wrap3" onclick="bgenScroll();">
                            <span>Objets</span>
                        </a>
                        <span class="badge badge-inverse">12</span>
                    </li>
                    <li class="wrapLi relationLi">
                        <a class="wrapLink" href="#wrap4" onclick="bgenScroll();">
                            <span>Relations</span>
                        </a>
                        <span class="badge badge-inverse">0</span>
                    </li>
                    <li class="wrapLi cluesLi">
                        <a class="wrapLink" href="#wrap5" onclick="bgenScroll();">
                            Indices
                        </a>
                        <span class="badge badge-inverse">0</span>
                    </li>
                    <li class="wrapLi pastScenesLi">
                        <a class="wrapLink" href="#wrap6" onclick="bgenScroll();">
                            PastScenes
                        </a>
                        <span class="badge badge-inverse">0</span>
                    </li>
                    <li class="wrapLi eventsLi">
                        <a class="wrapLink" href="#wrap7" onclick="bgenScroll();">
                            Evenements
                        </a>
                        <span class="badge badge-inverse">0</span>
                    </li>
                    <li class="wrapLi universLi">
                        <a class="wrapLink" href="#wrap8" onclick="bgenScroll();">
                            Univers
                        </a>
                        <span class="badge badge-inverse">0</span>
                    </li>
                    <li class="wrapLi plotLi">
                        <a class="wrapLink" href="#wrap9" onclick="bgenScroll();">
                            Intrigue
                        </a>
                    </li>
                </ul>

                <div id="fleche"></div>

                <div id="textes">
                    <div class="container">
                        <g:render template="rolesForm"/>
                    </div>

                    <div class="container">
                        <g:render template="placesForm"/>
                    </div>

                    <div class="container">
                        <g:render template="resourcesForm"/>
                    </div>

                    <div class="container">
                        Relations
                        <g:render template="relationsForm"/>
                    </div>

                    <div class="container">
                        Indices
                    </div>

                    <div class="container">
                        Past Scenes
                    </div>

                    <div class="container">
                        Evenements
                    </div>

                    <div class="container">
                        Univers
                    </div>

                    <div class="container">
                        <g:render template="generalDescriptionForm"/>
                    </div>
                </div>
            </div>
        </div>
        </div>
        </div>
        </div>
        </div>
        </div>
        </div>
        </div>
        </div>
    </div>
</section>