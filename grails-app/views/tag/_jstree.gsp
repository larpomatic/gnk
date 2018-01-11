<%@ page import="org.gnk.tag.Tag; org.gnk.admin.right" %>
<style>
#container {
    min-width: 320px;
    margin: 0px auto 0 auto;
    background: white;
    border-radius: 0px;
    padding: 0px;
    overflow: hidden;
}
#SimpleJSTree {
    float: left;
    min-width: 319px;
    border-right: 1px solid silver;
    overflow: auto;
    padding: 0px 0;
}
#data {
    margin-left: 320px;
}
#data textarea {
    margin: 0;
    padding: 0;
    height: 100%;
    width: 100%;
    border: 0;
    background: white;
    display: block;
    line-height: 18px;
}
#data, {
    font: normal normal normal 12px/18px 'Consolas', monospace !important;
}
</style>
</head>
<body>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>

<div class="row page" id="demo">
    <div class="col-md-12">
        <div class="row">
            <div class="col-md-4 col-sm-8 col-xs-8">
                <div class="col-md-2 col-sm-4 col-xs-4" style="text-align:right;">
                    <input type="text" value="" class="search-input form-control"
                           style="box-shadow:inset 0 0 4px #eee; width:120px; margin:0; padding:6px 12px; border-radius:4px; border:1px solid silver; font-size:1.1em;"
                           id="demo_q" placeholder="Search"/>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="container" role="main">
    <div id="SimpleJSTree"></div>
    <imput id="data">
        <table border="1">
            <tr>
                <td>

                    <div style="text-align:center; ">
                        <h4> Informations Tag</h4>
                        <form method="post" action="/gnk/tag/editRelevantTag">
                            Nom du Tag : <input type="text" id="NameEditRelTag" name="NameEditRelTag"/>
                            <input type="hidden" name="idEditRelTag" id="idEditRelTag"/>
                            Tag Parent : <select id="idParentSave" name="idParentSave">
                            <g:each in="${tagInstanceList}" status="i" var="tagInstance">
                                <g:if test="${params.idParentSave == tagInstance?.id}">
                                    <option value=${tagInstance?.id} selected>${tagInstance?.name}</option>
                                </g:if>
                                <g:else>
                                    <option value= ${tagInstance?.id}>${tagInstance?.name}</option>
                                </g:else>
                            </g:each>
                        </select>
                            <table style="width:100%">
                                <tr>
                                    <td>

                                        <input id="relevantFirstname" name="relevantFirstname" type="checkbox"/>
                                        <a href="#colfirst" data-toggle="collapse">RelevantFirstName</a>
                                        <div id="colfirst" class="collapse">
                                            <label ></label>
                                            <textarea DISABLED id="firstname" rows=3 name="firstname" style="text-align:center" ></textarea>
                                        </div>
                                    </td>
                                    <td>
                                        <input name="relevantLastname" id="relevantLastname" type="checkbox"/>
                                        <a href="#collast" data-toggle="collapse">RelevantLastName</a>
                                        <div id="collast" class="collapse">
                                            <label ></label>
                                            <textarea DISABLED id="lastname" rows=3 name="lastname" style="text-align:center" ></textarea>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input name="relevantPlace" id="relevantPlace" type="checkbox"/>
                                        <a href="#colplace" data-toggle="collapse">RelevantPlace</a>
                                        <div id="colplace" class="collapse">
                                            <label ></label>
                                            <textarea  DISABLED id="place" rows=3 name="place" style="text-align:center"></textarea>
                                        </div>
                                    </td>
                                    <td>
                                        <input name="relevantPlot" id="relevantPlot" type="checkbox"/>
                                        <a href="#colplot" data-toggle="collapse">RelevantPlot</a>
                                        <div id="colplot" class="collapse">
                                            <label ></label>
                                            <textarea DISABLED id="plot" rows=3 name="plot" style="text-align:center" ></textarea>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>

                                        <input name="relevantResource" id="relevantResource" type="checkbox"/>
                                        <a href="#colres" data-toggle="collapse">RelevantResource</a>
                                        <div id="colres" class="panel-collapse collapse">
                                            <label ></label>
                                            <textarea DISABLED id="resource" rows=3 name="resource" style="text-align:center"></textarea>
                                        </div>
                                    </td>
                                    <td>
                                        <input name="relevantRole" id="relevantRole" type="checkbox"/>
                                        <a href="#colrole" data-toggle="collapse">RelevantRole</a>
                                        <div id="colrole" class="collapse">
                                            <label ></label>
                                            <textarea DISABLED id="role" rows=3 name="role" style="text-align:center" ></textarea>
                                        </div>
                                    </td>
                                </tr>
                            </table>

                            <button type="submit" class="btn btn-warning btn-sm">Modifier</button>
                        </form>
                        <g:hasRights lvlright="${right.REFMODIFY.value()}">
                            <g:hasRights lvlright="${right.REFDELETE.value()}">
                                <input type="hidden" name="idEditRelTagson" ID="idEditRelTagson"/>
                                <button data-toggle="confirmation-popout" data-placement="left"
                                        class="btn btn-danger btn-sm">${message(code: 'default.delete')}</button>
                            </g:hasRights>
                        </g:hasRights>
                    </div>
            </tr>
        </td>
            <tr>
                <td>

                    <div style="text-align:center; ">
                        <h4> Ajouter Fils</h4>
                        <g:form action="save">
                            <div>
                                <input type="hidden" name="idTag" id="idTag"/>

                                <div>
                                    Nouveau tag fils : <input id="nameTag" name="nameTag"/>
                                    <button class="btn btn-success btn-sm" type="submit">Créer</button>
                                </div>

                                <div>
                                    <input id="relevantFirstnameson" name="relevantFirstnameson" type="checkbox"/>
                                    RelevantFirstName

                                    <input name="relevantLastnameson" id="relevantLastnameson" type="checkbox"/>
                                    RelevantLastName

                                    <input name="relevantPlaceson" id="relevantPlaceson" type="checkbox"/>
                                    RelevantPlace

                                    <input name="relevantPlotson" id="relevantPlotson" type="checkbox"/>
                                    RelevantPlot

                                    <input name="relevantResourceson" id="relevantResourceson" type="checkbox"/>
                                    RelevantResource

                                    <input name="relevantRoleson" id="relevantRoleson" type="checkbox"/>
                                    RelevantRole
                                </div>
                            </div>
                        </g:form>
                    </div>
                </td>
            </tr>
        </table>
        <g:javascript src="redactIntrigue/bootstrap-confirmation.js"/>

</div>
<input type="hidden" name="idTagurl" id="idTagurl" data-url="<g:createLink controller="tag" action="deleteTag"/>"/>
<script type="text/javascript">
    $(function () {
        createJSTree();
    });
    function demo_create() {
        var ref = $('#SimpleJSTree').jstree(true),
            sel = ref.get_selected();
        if (!sel.length) {
            return false;
        }
        sel = sel[0];
        var test = $('#event_resulttag')
        sel = ref.create_node(test, {"type": "file"});
        if (sel) {
            ref.edit(sel);
        }
    }
    ;
    function demo_rename() {
        var ref = $('#SimpleJSTree').jstree(true),
            sel = ref.get_selected();
        if (!sel.length) {
            return false;
        }
        sel = $('#event_resulttag');
        ref.edit(sel);
    }
    ;
    function demo_delete() {
        var ref = $('#SimpleJSTree').jstree(true),
            sel = ref.get_selected();
        if (!sel.length) {
            return false;
        }
        ref.delete_node(sel);
    }
    ;
    function createJSTree() {
        $(window).resize(function () {
            var h = Math.max($(window).height() - 0, 420);
            $('#container, #data, #SimpleJSTree, #data .content').height(h).filter('.default').css('lineHeight', h + 'px');
        }).resize();
        $(".search-input").keyup(function () {
            var searchString = $(this).val();
            $('#SimpleJSTree').jstree('search', searchString);
        });
        $('#SimpleJSTree')
            .on('changed.jstree', function (e, data) {
                var i, j, t = [], p = [], n = [], relevantFirstname = [], relevantLastname = [], relevantPlace = [], relevantPlot = [], relevantResource = [], relevantRole = [], Place = [], Plot = [], Resource = [], Role = [], Lastname = [], Firstname = [];
                for (i = 0, j = data.selected.length; i < j; i++) {
                    t.push(data.instance.get_node(data.selected[i]).id);
                    p.push(data.instance.get_node(data.selected[i]).parent);
                    n.push(data.instance.get_node(data.selected[i]).text);
                    relevantFirstname.push(data.instance.get_node(data.selected[i]).a_attr.relevantFirstname);
                    relevantLastname.push(data.instance.get_node(data.selected[i]).a_attr.relevantLastname);
                    relevantPlace.push(data.instance.get_node(data.selected[i]).a_attr.relevantPlace);
                    relevantPlot.push(data.instance.get_node(data.selected[i]).a_attr.relevantPlot);
                    relevantResource.push(data.instance.get_node(data.selected[i]).a_attr.relevantResource);
                    relevantRole.push(data.instance.get_node(data.selected[i]).a_attr.relevantRole);
                    Place.push(data.instance.get_node(data.selected[i]).a_attr.place);
                    Plot.push(data.instance.get_node(data.selected[i]).a_attr.plot);
                    Resource.push(data.instance.get_node(data.selected[i]).a_attr.resource);
                    Role.push(data.instance.get_node(data.selected[i]).a_attr.role);
                    Firstname.push(data.instance.get_node(data.selected[i]).a_attr.firstname);
                    Lastname.push(data.instance.get_node(data.selected[i]).a_attr.lastname);
                }
                document.getElementById('idTag').value = t.join(', ');
                document.getElementById('NameEditRelTag').value = n.join(', ');
                document.getElementById('idEditRelTag').value = t.join(', ');
                document.getElementById('idEditRelTagson').value = t.join(', ');
                document.getElementById('idParentSave').value = p.join(', ');
                document.getElementById('relevantFirstname').checked = relevantFirstname.join(', ');
                document.getElementById('plot').value = Plot.join(', ');
                document.getElementById('place').value = Place.join(', ');
                document.getElementById('role').value = Role.join(', ');
                document.getElementById('resource').value = Resource.join(', ');
                document.getElementById('firstname').value = Firstname.join(', ');
                document.getElementById('lastname').value = Lastname.join(', ');
                if (relevantFirstname.join(', ') == 'true')
                    document.getElementById('relevantFirstname').checked = true;
                else
                    document.getElementById('relevantFirstname').checked = false;
                if (relevantLastname.join(', ') == 'true')
                    document.getElementById('relevantLastname').checked = true;
                else
                    document.getElementById('relevantLastname').checked = false;
                if (relevantPlace.join(', ') == 'true')
                    document.getElementById('relevantPlace').checked = true;
                else
                    document.getElementById('relevantPlace').checked = false;
                if (relevantPlot.join(', ') == 'true')
                    document.getElementById('relevantPlot').checked = true;
                else
                    document.getElementById('relevantPlot').checked = false;
                if (relevantResource.join(', ') == 'true')
                    document.getElementById('relevantResource').checked = true;
                else
                    document.getElementById('relevantResource').checked = false;
                if (relevantRole.join(', ') == 'true')
                    document.getElementById('relevantRole').checked = true;
                else
                    document.getElementById('relevantRole').checked = false;
                $('#').html();
                $('#').html();
            }).jstree({
            'core': {
                'data': ${json},
                "check_callback": true
            },
            "search": {
                "case_insensitive": true,
                "show_only_matches": true
            },
            "plugins": ["search"]
        });
    }
    $(function () {
        initConfirm();
    });
    function initConfirm() {
        $('[data-toggle="confirmation-popout"]').confirmation({
            popout: true,
            btnOkLabel: "Oui",
            btnCancelLabel: "Non",
            btnOkClass: "btn-success",
            btnCancelClass: "btn-danger",
            onConfirm: function () {
                // var button = $(this).closest(".popover").prev();
                var idEditRelTagson = document.getElementById('idEditRelTagson').value
                var url = $("#idTagurl").attr("data-url");
                $.ajax({
                    type: "POST",
                    url: url,
                    data: "idEditRelTagson=" + idEditRelTagson,
                    success: function () {
                        window.location.reload()
                    },
                    error: function () {
//                    idTag.parentNode.parentNode.remove()
                    }
                })
            }
        });
    }
</script>

</body>