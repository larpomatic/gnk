<%--
  Created by IntelliJ IDEA.
  User: Nico
  Date: 29/03/14
  Time: 18:33
--%>

<%@ page import="org.gnk.admin.right" contentType="text/html;charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="main"/>
    <title>Profil Utilisateur</title>
    <style>
    .radmc .raddivider {
        display: block;
        font-size: 1px;
        border-width: 0px;
        border-style: solid;
        position: relative;
        z-index: 1;
    }
    .radmc .raddividery {
        float: left;
        width: 0px;
    }
    .radmc .radtitle {
        display: block;
        cursor: default;
        white-space: nowrap;
        position: relative;
        z-index: 1;
    }
    .radclear {
        font-size: 1px;
        height: 0px;
        width: 0px;
        clear: left;
        line-height: 0px;
        display: block;
        float: none !important;
    }
    .radmc {
        position: relative;
        zoom: 1;
        z-index: 10;
    }
    .radmc a, .radmc li {
        float: left;
        display: block;
        white-space: nowrap;
        position: relative;
        z-index: 1;
    }
    .radmc div a, .radmc ul a, .radmc ul li {
        float: none;
    }
    .radsh div a {
        float: left;
    }
    .radmc div {
        visibility: hidden;
        position: absolute;
    }
    .radmc li {
        z-index: auto;
    }
    .radmc ul {
        left: -10000px;
        position: absolute;
        z-index: 10;
    }
    .radmc, .radmc ul {
        list-style: none;
        padding: 0px;
        margin: 0px;
    }
    .radmc li a {
        float: none
    }
    .radmc li:hover>ul {
        left: auto;
    }
    #rad0 li {
        float: none;
    }
    #rad0 li:hover>ul {
        top: 0px;
        left: 100%;
    }
    #rad0 {
        width: 200px;
        background-color: transparent;
        border-width: 1px 2px 1px 2px;
        border-style: solid;
        border-color: #ffffff;
    }
    #rad0 a {
        padding: 5px 5px 5px 8px;
        background-color: #ffffff;
        color: #080808;
        font-family: Arial;
        font-size: 13px;
        text-decoration: none;
        border-width: 1px 0px 1px 0px;
        border-style: solid;
        padding-top: 8px;
        padding-bottom: 8px;
        margin-top: 2px;
        margin-bottom: 2px;
        border-radius: 5px;
        border-color: rgba(82, 168, 236, 0.8);
    }
    body #rad0 .radactive, body #rad0 .radactive:hover {
        text-decoration: underline;
    }
    #rad0 div, #rad0 ul {
        padding: 10px 0px 10px 0px;
        background-color: #FFFFFF;
        border-width: 1px;
        border-style: none;
        border-color: #080808;
    }
    #rad0 div a, #rad0 ul a {
        padding: 2px 0px 2px 15px;
        margin: 0px 5px 0px 5px;
        background-color: #ffffff;
        background-image: none;
        border-style: none;
    }
    #rad0 div a:hover, #rad0 ul a:hover {
        text-decoration: underline;
        background-color: rgba(146, 217, 255, 0.80);
    }
    body #rad0 div .radactive, body #rad0 div .radactive:hover {
        background-color: rgba(146, 217, 255, 0.80);
        padding-top: 8px;
        padding-bottom: 8px;
        margin-top: 2px;
        margin-bottom: 2px;
        border-radius: 5px;
    }

    </style>
</head>
<g:hasRights lvlright="${right.REFOPEN.value()}">
    <g:hasRights lvlright="${right.RIGHTSHOW.value()}">
<body>
<br/>
<br/>
<ul class="nav nav-pills">
    <li><g:link controller="user" action="profil"><g:message code="navbar.profil"/></g:link></li>
    <g:hasRights lvlright="${right.USEROPEN.value()}">
        <li><g:link controller="adminUser" action="list"><g:message code="navbar.gestion_user"/></g:link></li>
        <li class="active"><g:link controller="consolSql" action="terminal"><g:message code="navbar.gestion_console"/> </g:link> </li>
    </g:hasRights>

</ul>

<div role="main">

    <h3><g:message code="navbar.gestion_console"/></h3>
    <ul id="rad0" class="radmc">
        <li>
            <g:each in="${listCategory}" var="l">
                <a class="radparent" href="#">${l.name}</a>
                <ul>
                    <li>
                        <g:each in="${l.sqlRequestSubcategory}" var="ls">
                            <a class="radparent" href="#">${ls.name}</a>
                            <ul>
                                <li>
                                    <g:each in="${ls.sqlRequest}" var="rq">
                                        <a class="requestLink" href="#" data-request="${rq.sqlrequest}">${rq.name}</a>
                                    </g:each>
                                </li>
                            </ul>
                        </g:each>
                    </li>
                </ul>
            </g:each>
        </li>
    </ul>
    <g:form action="request" method="post">
        <br/>
        <div class="input-group">
            <g:textArea class="size-console" name="sqlconsol" id="sqlcompile"/>
        </div>
        <input class="form-control" name="categoryname" id="categoryname" placeholder="Catégorie"> <br/>
        <input name="subcategory" id="subcategory" placeholder="Sous Catégorie"><br/>
        <input name="requestname" id="requestname" placeholder="Nom de la Requête"><br/>
        <br/>
        <div class="btn-group text-center">
        <button type="submit" class="button-submit btn btn-default btn-large">Soumettre</button>
            <p id="SavedSqlRequest">
                <input type='checkbox' class='chk' name='isSaved' id='isSaved'/>
                <label>Sauvegarder</label>
            </p>
        </div>
    </g:form>
    <h3><g:message code="gnk.consol.Delete"/></h3>
    <g:form action="deleteRequest" method="post">
        <div id="delSelect">
        <label><g:message code="gnk.consol.cat"/></label><select name="categoryname" id="categorynameD" data-url="<g:createLink controller="consolSql" action="getSubCategory"/>">
            <option value=""></option>
            <g:each in="${listCategory}" var="l">
                <option value="${l.id}">${l.name}</option>
            </g:each>
        </select><br/>
            <label><g:message code="gnk.consol.subcat"/></label><select name="subcategory" id="subcategoryD" data-url="<g:createLink controller="consolSql" action="getRequest"/>">
                <option value=""></option>
            </select><br/>
            <label><g:message code="gnk.consol.request"/></label> <select name="requestname" id="requestnameD">
                <option value=""></option>
            </select><br/>
        </div>

        <br/>
        <button type="submit" class="btn btn-danger btn-large">Supprimer</button>
    </g:form>
</div>
<script type="text/javascript">

$("#categorynameD").change(function(){
    var cat = $( "#categorynameD");
    var subCatSelect = $("#subcategoryD");
    $('option:not([value=""])', subCatSelect).remove();
    $.ajax({
        type: "POST",
        url: cat.attr("data-url"),
        data: { cat: cat.val() },
        success : function(subCatList){
            $(subCatList).each(function() {
                subCatSelect.append('<option value="'+this.id+'">' + this.name + '</option>');
            });
        }
    })
 });

$("#subcategoryD").change(function(){
    var cat = $( "#subcategoryD");
    var requestSelect = $("#requestnameD");
    $('option:not([value=""])', requestSelect).remove();
    $.ajax({
        type: "POST",
        url: cat.attr("data-url"),
        data: { cat: cat.val() },
        success : function(requestList){
            $(requestList).each(function() {
                requestSelect.append('<option value="'+this.id+'">' + this.name + '</option>');
            });
        }
    })
});

$(function() {
    $('.requestLink').click(function() {
        displayrequest($(this).attr("data-request"));
    });
});
function displayrequest(req){
    $('#sqlcompile').attr("value", req);
}
eval(function(p,a,c,k,e,d){e=function(c){return(c<a?"":e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)d[e(c)]=k[c]||e(c);k=[function(e){return d[e]}];e=function(){return'\\w+'};c=1;};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p;}('f 47,14,5M,1b,3F,5m,1C,2I,3E;f O="5N";f 1u="1D";f 23=2a.3A;f 5j=23.F("5K")+1;f 38=23.F("5L")+1;f 2s=38&&23.F("57/2")+1;f 5q=38&&23.F("57/3")+1;f 5h=23.F("3B")+1;f 5b=3u(2a.3o);u 5c(y,v,34,30,1d,2g,Q,2n,2p,4S,l){f w="1L";f 4h=w;f e="4i";9(1d){9(1d=="40"||(1d=="5R"&&l>=2)){w=e;34=0}9(1d=="40"||1d=="2A"){4h=e;30=0}}9(!l){l=1;3F=30;y=P.3k("S"+y);9(B.3N)y=3N(y);y[w]=u(e){1B(e)};P[4h]=4U;9(1d=="2A"){3E=L;y[e]=u(1V){2I=L;2f(R 1t(),1C,1);1B(1V)};P.1L=u(){1C=G;2O(1b);1b=G}}y.k.4l=1;9(Q)W("5Q",y,1);9(!v)y.12=1}n 9(Q)y.12=1;9(1d)y.1d=1d;9(Q)y.Q=1;9(2n)y.2n=1;9(2p)y.2p=1;9(2g)y.2g=1;y.k.5g=l+""+1;f 2z;f 2K=y.21;U(f i=0;i<2K.1A;i++){f b=2K[i];9(b.1i=="A"){2z=b;b[w]=2f;9(w==e)b.1L=u(1V){2O(1b);1b=G;1C=G;1B(1V)};b.49=34;9(l==1&&v){b.k.5E="39";b.k.5H="39"}}n 9(b.1i=="2h"){9(B.3q&&!B.5J)2K[i].5T("63","<1N 67=\'4q\'> </1N>");W("6a",2z,1);2z.I=b;b.2u=2z;9(5h&&5b<8&&!b.k.1y)b.k.1y=b.25+"H";R 5c(b,G,34,30,1d,2g,Q,2n,2p,4S,l+1)}}};u 4U(e){2I=1a;1C=G;2O(1b);1b=G;9(14)1b=2X("4R()",3F)};u 4R(){f a;9((a=14)){62{1Q(a)}1T((a=a[O])&&!1J(a))}14=G};u 1J(a){9(a[1u].F("5Z")+1)J 1};u 1Q(a,17){9(!17&&a.37)J;9(B.g&&g.31)1Z(g.31);a.k.2x="";W("2B",a.2u)};u 5Y(a,b){J 5X.69(a.6b(0)-(b-(E(b/2)*2)))};u 2f(e,o,2L){9(!o)o=4A;9(1C==o&&!2L)J;9(B.4W&&!2L)4W(o);9(B.3y){1B(e);J}2O(1b);1b=G;1C=o;9(!2L&&o.49){47=o;1b=2X("2f(R 1t(),47,1)",o.49);J}f a=o;9(a[O].68){1B(e);J}9(3E&&!2I)J;f 17=L;1T((a=a[O])&&!1J(a)){9(a==14)17=1a}9(14&&17){a=o;9((!a.I)||(a.I&&a.I!=14))1Q(14);a=14;1T((a=a[O])&&!1J(a)){9(a!=o[O]&&a!=o.I)1Q(a);n 60}}f b=o;f c=o.I;9(b.I){f 2Z=b.25;f 43=b.1I;f 1X=b.5V;f 2e=b.5U;9(c[O].12){2Z=0;9(c.2n)1X=0}n{9(c.2p)2e=0;9(c.2g){1X=1X-c.25;2Z=0}43=0}9(5j){1X-=b[O].66;2e-=b[O].5F}9(2s&&!5q){1X-=46(b[O],"1z-18-1y","5O");2e-=46(b[O],"1z-1n-1y","5W")}9(!c.4B){c.k.18=(1X+2Z)+"H";c.k.1n=(2e+43)+"H"}W("2B",o,1);9(B.g&&g.1P)1Z(g.1P);c.k.2x="59";14=c}n 9(!1J(b[O]))14=b[O];n 14=G;1B(e)};u 46(2Y,4F,4I){f v;9(P.44&&P.44.4t)v=P.44.4t(2Y,G).61(4F);n 9(2Y.4G)v=2Y.4G[4I];9(v&&!3e(v=E(v)))J v;n J 0};u W(2w,b,4H){f a=b[1u];9(4H){9(a.F(2w)==-1)b[1u]+=(a?\' \':\'\')+2w}n{b[1u]=a.4s(" "+2w,"");b[1u]=b[1u].4s(2w,"")}};u 1B(e){9(!e)e=1V;e.64=L;9(e.4n&&!(38&&e.D=="65"))e.4n()};u 3N(y){9(y.1i=="4o"){f 1U=P.33("2h");1U.5G=1;f c;9(c=y.k.35)1U.k.35=c;48(y,1U);f 2M=P.33("3Q");2M.1D="4q";2M.55=" ";1U.45(2M);y=y[O].5I(1U,y);y=1U}J y};u 48(a,2D,l){9(!l)2D[1u]=a[1u];2D.1g=a.1g;f 12=a.21;U(f i=0;i<12.1A;i++){9(12[i].1i=="5P"){f Q=12[i].21;U(f j=0;j<Q.1A;j++){9(Q[j]&&(Q[j].1i=="A"||Q[j].1i=="3Q"))2D.45(12[i].5S(Q[j]));9(Q[j]&&Q[j].1i=="4o"){f 22=P.33("2h");f c;9(c=Q[j].k.35)22.k.35=c;9(c=Q[j].1D)22.1D=c;22=2D.45(22);R 48(Q[j],22,1)}}}}}P.3s(\'<k D="2W/3t">.3m{2x:6Z !Z;}.2t{2x:3I !Z;}</k><4p D="2W/77">f g = R 1t();g.1P="";g.31="";</4p>\');f a=g.6L=R 1t();a.4P="3Z";a.3i="28";a.5w="2d";a.5y=5;a.5B="#6M";a.6W="#3l";a.6S="#3l";a.6T="#4r";a.5z=-16;a.5A=-5;a.5C="1K";a.5x="2S";a.4a="28";a.4X="2d";a.4Y=6;a.4Z="#6R";a.6V="#6U";a.6Q="#4r";a.52=-7;a.51=-4;a.50="18";a.4V="2S";a.2i=L;a.3J=15;a.4K=L;a.4L=L;a.4w=2;a.4M=8;a.4x=3;a.4N=15;g.36=2a.3A.F("3B")+1;g.3c=3u(2a.3o);g.4v=g.36&&g.3c<7.1;g.2o=R 1t();9(g.1P.F("4b(b.I);")==-1){g.1P+="4b(b.I);";4m()}9(B.1F)B.1F("5t",3O);n 9(B.1H)B.1H("5k",3O,1);u 4m(){f a,b;9(g){f i;U(i 3r g){9(i.F("S")!=0||i.F("1w")+1)2J;f h=g[i];9(h.6K)h.2i=L;9(h&&h.2i){f 3R="";9(B.3q)3R="4l:1;";f 3T="";9(2s)3T="2v:39;19:1r;";f 3H=\'<k D="2W/3t">.6P\'+i+\'{} #\'+i+\'{19:1r !Z;} #\'+i+\' a{6O:39 !Z;6N-76:75 !Z;19:3S !Z}#\'+i+\' 2H{1y:78 !Z;18:1m !Z;1n:1m !Z;4T:3I !Z;\'+3T+3R+\'1z-1n-1y:1m !Z;1z-56-1y:1m !Z;3W-18:1m !Z;3W-1n:1m !Z;}\';9(h.3J)3H+=\'#\'+i+\' 2H 2H{79-18:\'+h.3J+\'H}\';P.3s(3H+\'</k>\')}}}};u 3O(1V,24){f q=g.2o;f a,b;f i;U(i 3r g){9(i.F("S")!=0||i.F("1w")+1||i.F("74")+1||(!3e(24)&&24!=i))2J;f h=g[i];9(h&&h.2i){q.2c=h.4M;9(!q.2c)q.2c=1;q.1Y=h.4N;9(!q.1Y)q.1Y=1;q.4z=h.4L;q.4C=h.4K;q.1h=h.4w;9(q.1h)q.1h=E(q.1h);9(!q.1h)q.1h=0;q.1c=h.4x;9(q.1c)q.1c=E(q.1c);9(!q.1c)q.1c=0;9(g.4v){q.1h=0;q.1c=0}41(P.3k(i))}i++}};u 41(a,3n){f w,b;f q=g.2o;f p;p=a.21;U(f j=0;j<p.1A;j++){9(p[j].1i=="A"){9(p[j].I){p[j].I.4B=1;p[j].I.37=1}9(!p[j].4i){p[j].4i=p[j].1L;p[j].1L=G}9(q.4C){p[j].6Y=u(){4A.6X()}}9(p[j].I)R 41(p[j].I,1);9(p[j].70("73"))2f(R 1t(),p[j],1)}}};u 4b(a,72){f z;9(!a.37&&!((z=B.1w)&&z.71)){f 1g=3C(a).1g;9(B.g&&g[1g]&&g[1g].2i)W("2t",a,1);J}9((z=B.1w)&&(z=z.5n)&&(z=z.6n)&&!z["2Q"+6o(a)])J;W("2t",a);f q=g.2o;9(q.3v)J;1C=G;q.M=R 1t();f 2G="a"+5r(a);f 3D=1a;f 2V=1a;9(q.4z){f 4y=3C(a);f 1S=4y.4u("2h");U(f i=0;i<1S.1A;i++){9(1S[i].k.19=="1r"&&1S[i]!=a){f 17=L;f 2l=a[O];1T(!1J(2l)){9(1S[i]==2l)17=1a;2l=2l[O]}9(17){2V=L;q.M["a"+i]=1S[i];1Q(1S[i],1)}}}}9(a.k.19=="1r"){2V=L;q.M["b"]=a;f d=a.4u("2h");U(f i=0;i<d.1A;i++){9(d[i].k.19=="1r"){q.M["b"+i]=d[i];1Q(d[i],1)}}a.4J=1;1Q(a,1);9(B.4D)4D(G,a.2u)}n{3D=L;9(2s)a.k.2v="3U";a.k.19="1r";q.3d=a.1I;a.k.X="1m";W("3m",a,1);W("2t",a);a.4J=0;q.1G=a}3y=L;3g(3D,2V,2G)};u 3g(3f,3x,2G){f q=g.2o;f 17=1a;f T=1;9(3x){U(f i 3r q.M){9(!q.M[i].k.X&&q.M[i].k.19=="1r"){q.M[i].k.X=(q.M[i].1I)+"H";q.M[i].4E=E(q.M[i].k.X)}T=E((q.M[i].1I/E(q.M[i].4E))*q.1Y);9(q.1c==1)T=q.1Y-T+1;n 9(q.1c==2)T=T+1;n 9(q.1c==3)T=q.1Y;9(q.1c&&E(q.M[i].k.X)-T>0){q.M[i].k.X=E(q.M[i].k.X)-T+"H";17=L}n{q.M[i].k.X="";q.M[i].k.19="";9(2s)q.M[i].k.2v="";W("2t",q.M[i],1);W("3m",q.M[i])}}}9(3f){T=E((q.1G.1I/q.3d)*q.2c);9(q.1h==2)T=q.2c-T;n 9(q.1h==1)T=T+1;n 9(q.1h==3)T=q.2c;9(q.1h&&q.1G.1I<(q.3d-T)){q.1G.k.X=E(q.1G.k.X)+T+"H";17=L;9(B.3b)3b()}n{q.1G.6l=q.1G.k.X;q.1G.k.X="";9(B.3b)3b()}}9(17){q.3v=2X("3g("+3f+","+3x+",\'"+2G+"\')",10)}n{3y=1a;q.3v=G}};u 5r(a){3z=0;1T(!1J(a)&&(a=a[O]))3z++;J 3z};u 3C(a){1T(!1J(a)&&(a=a[O]))2J;J a}g.36=2a.3A.F("3B")+1;g.3c=3u(2a.3o);g.5u=g.36&&g.3c<7;g.5v=(5p=P.6r)&&5p=="6s";g.5s=B.3q;g.29=(g.5s&&!g.5v);9(!g.5u){9(!g.N)g.N=R 1t();9(g.1P.F("2C(o,1a);")==-1){g.1P+="2C(o,1a);";g.31+="2C(a,1);";9(B.1F)B.1F("5t",3w);n 9(B.1H)B.1H("5k",3w,1);9(B.1F)P.1F("1L",2R);n 9(B.1H)P.1H("5d",2R,1a);f V=\'<k D="2W/3t">.6p{}\';V+=3p("2A");V+=3p("3n");P.3s(V+\'</k>\')}};u 3p(Y,1g){f V=\'\';f a="#3l";f b="#6q";f t,q;2j="";9(Y=="3n")2j="2H ";f 2U="5i"+Y+"6k";f 2N="5i"+Y+"6e";U(f i=0;i<10;i++){9(q=g["S"+i]){9(t=q[2U])a=t;9(t=q[2N])b=t;V+=\'#S\'+i+\' \'+2j+\'.S-N-3S 1N{2E-1R:\'+a+\';1z-1R:\'+b+\';}\';9(t=q[2U+"5l"])a=t;9(t=q[2N+"5l"])b=t;V+=\'#S\'+i+\'  \'+2j+\'.S-N-4d 1N{2E-1R:\'+a+\';1z-1R:\'+b+\';}\';9(t=q[2U+"5o"])a=t;9(t=q[2N+"5o"])b=t;V+=\'#S\'+i+\'  \'+2j+\'.S-N-4j 1N{2E-1R:\'+a+\';1z-1R:\'+b+\';}\'}}J V};u 3w(e,24){f z;9((z=B.1w)&&(z=z.5n)&&(z=z.N)&&(!z["2Q"+1w.1g]&&z["2Q"+1w.1g]!=6c&&z["2Q"+1w.1g]!=G))J;5m=1;f q=g.N;f a,b,r,1x,1v;z=B.1w;U(i=0;i<10;i++){9(!(a=P.3k("S"+i))||(!3e(24)&&24!=i))2J;f h=g[a.1g];9(h&&(h.3i||h.4a)){q.6d=h.3i;q.3h=h.5y;9(!q.3h)q.3h=5;q.3j=h.5w;9(!q.3j)3j="1K";q.6i=h.5B;q.6j=h.6g;1x=h.5z;1v=h.5A;9(!1x)1x=0;9(!1v)1v=0;q.6h=1Z("R 2T(\'"+1x+"\',\'"+1v+"\')");q.5D=1Z("R 2T(\'"+h.5C+"\',\'"+h.5x+"\')");r=q.5D;9(!r[0])r[0]="1K";9(!r[1])r[1]="53";q.6E=h.4a;q.42=h.4Y;9(!q.42)q.42=5;q.y=h.4X;9(!q.y)y="1K";q.6F=h.6C;q.6D=h.4Z;1x=h.52;1v=h.51;9(!1x)1x=0;9(!1v)1v=0;q.6I=1Z("R 2T(\'"+1x+"\',\'"+1v+"\')");q.4Q=1Z("R 2T(\'"+h.50+"\',\'"+h.4V+"\')");r=q.4Q;9(!r[0])r[0]="1K";9(!r[1])r[1]="2S";q.D=h.4P;4g("m");4g("s");3V(a,1,"S"+i)}}};u 4g(Y){f q=g.N;f V="";f s=q[Y+"C"];f D=q[Y+"D"];f 1q;9(D.F("1q")+1)1q=L;f 11;9(D.F("11")+1)11=L;f v;9(D.F("-v")+1)v=L;9(D.F("28")+1)D="28";9(D=="28"){U(f i=0;i<s;i++)V+=2m(s,i,Y,D,G,G,v);9(1q||11)V+=2m(s,G,Y,G,1q,11,G)}n 9(D.F("3K")+1){f 1o;9(D.F("-1o")+1)1o=L;f 2b;9(D.F("-2b")+1)2b=L;D="3K";U(f i=0;i<3;i++)V+=2m(s,i,Y,D,G,G,G,1o,2b);9(1o)V+=2m(s,i,Y,"1o")}q[Y+"1o"]=V};u 2m(C,i,Y,D,1q,11,v,4O,2b){f q=g.N;f d=q[Y+"d"];f 13=i;f 1k=i;f K=1;f x=1;f 20=0;f 2k=0;f 1f=0;f 1e=0;f 2r=0;f 2q=0;f 1O=0;f 1M=0;f 1p=0;f 1E=0;9(g.29){1p=2;1E=1}f 3G="";9(v||4O)3G="2E-1R:6G;";9(D=="28"){9(d=="2d"||d=="3P"){9(d=="3P")i=C-i-1;1f=1;1e=1;20=i;2k=i;x=((C-i)*2)-2;1k=-C;K=1;9(i==0&&!v){1f=x+2;1e=0;20=0;2k=0;x=0;9(g.29)x=1f}n{x+=1p}}n 9(d=="1K"||d=="18"){9(d=="18")i=C-i-1;1O=1;1M=1;2r=i;2q=i;x=1;13=-C;K=((C-i)*2)-2;9(i==0&&!v){1O=K+2;1M=0;2r=0;2q=0;K=0}n K+=1p}}n 9(1q||11){1O=1;1e=1;1M=1;1f=1;2r=0;2k=0;2q=0;20=0;f 1W=0;9(11)1W=2;f 3a=1;9(11)3a=0;9(d=="2d"||d=="3P"){x=E(C/2);9(x%2)x--;K=x+3a;1k=-(E((x+2)/2));9(1q&&11)K+=1E;n K+=1p;x+=1p;9(d=="2d"){9(11)1W++;13=-K-1W+1E;1M=0}n{13=C-1+1W+1E;1O=0}}n{K=E(C/2);9(K%2)K--;x=K+3a;13=-(E((x+2)/2));9(1q&&11)x+=1E;n x+=1p;K+=1p;9(d=="1K"){1k=-K-1-1W+1E;1e=0}n{1k=C-1+1W+1E;1f=0}}9(11){1O=1;1e=1;1M=1;1f=1}}n 9(D=="3K"){9(2b){9(i==2)J"";x=C;K=C;13=0;1k=0;9(i==0){x=0;K=C;1e=C;13=1;1k=1;9(g.29)x=1e}}n{9(C%2)C++;13=1;K=C;x=C;1f=1;1e=1;1k=0;x+=1p;9(i==0||i==2){20=1;13=0;K=1;1f=C;1e=0;x=0;9(g.29)x=1f;9(i==2)13=C+1}}}n 9(D=="1o"){9(C%2)C++;x=E(C/2);9(x%2)x++;K=x;13=E(C/2)+1-E(x/2);1k=13}f 3Y="";9(g.29)3Y=" ";J\'<1N k="\'+3G+\'1z-1y:\'+1O+\'H \'+1e+\'H \'+1M+\'H \'+1f+\'H;1z-k:6v;2v:3U;19:6w;4T:3I;6t-C:5e;6u-X:1m;X:\'+K+\'H;3W:\'+2r+\'H \'+2k+\'H \'+2q+\'H \'+20+\'H;1y:\'+x+\'H;1n:\'+13+\'H;18:\'+1k+\'H;">\'+3Y+\'</1N>\'};u 3V(a,2A){f q=g.N;f p,1s;p=a.21;U(f j=0;j<p.1A;j++){9(p[j].1i=="A"){9(B.1F)p[j].1F("1L",3M);n 9(B.1H)p[j].1H("5d",3M,1a);f 32=1a;9(q.D!="40"){9(q.D=="3Z"&&!p[j].I)32=L;9(q.D=="6z-3Z"&&p[j].I)32=L}9(!32){9(2A)1s="m";n 1s="s";f h=P.33("3Q");h.1D="S-N-3S";f 27=h.k;27.2v="3U";27.19="1r";27.6x="5e";27.6y="1m";27.5g=1;h.3X=q[1s+"5f"][0];h.3L=q[1s+"5f"][1];h.2F=q[1s+"5a"][0];h.2P=q[1s+"5a"][1];h.2y=q[1s+"C"];4c(p[j],h);h.55=q[1s+"1o"];p[j].1j=p[j].6A(h,p[j].6B);h.54("6H",1);h.54("6J",1);9(p[j].1D.F("2B")+1)2C(p[j])}9(p[j].I)R 3V(p[j].I,G)}}};u 4c(a,b){9(b.3X=="1K")b.k.18=(a.25+E(b.2F)-b.2y)+"H";n 9(b.3X=="53")b.k.18=(E(a.25/2)-E(b.2y/2)+E(b.2F))+"H";n b.k.18=b.2F+"H";9(b.3L=="56")b.k.1n=(a.1I+E(b.2P)-b.2y)+"H";n 9(b.3L=="2S")b.k.1n=E((a.1I/2)-E(b.2y/2)+E(b.2P))+"H";n b.k.1n=b.2P+"H"};u 3M(e,1l){e=e||B.1V;9(!1l){f 1l=e.6f||e.6m;1T(1l.1i!="A")1l=1l[O]}f 12=g.N.4f;9(12&&12!=1l&&12.1j)2R(R 1t(),12);9(1l.1D.F("2B")+1)J;f 26=1l.1j;9(26){W("S-N-4d",26,1);g.N.4f=1l}9(e)1B(e)};u 2R(e,o){9(!o)o=g.N.4f;9(o&&o.1j)W("S-N-4d",o.1j)};u 2C(a,4e){9(!4e&&a.1D.F("2B")==-1)J;9(4e&&a.2u){f o=a.2u;9(o&&o.1j){W("S-N-4j",o.1j)}}n{9(!a.I.25)a.I.k.2x="59";4k(a);f 26=a.1j;9(26)W("S-N-4j",26,1)}};u 4k(a){9(!a)a=g.N.58;9(a.I){9(a.I.37&&a.I.k.19!="1r"){g.N.58=a;2X("4k()",10);J}f p=a.I.21;U(f i=0;i<p.1A;i++){9(p[i].1i=="A"&&p[i].1j)4c(p[i],p[i].1j)}}}',62,444,'|||||||||if||||||var|radad|ss|||style|||else||aa|||||function|||iw|sd|||window|size|type|parseInt|indexOf|null|px|cdiv|return|ih|true|co|ibcss|qp|document|sh|new|rad|cs|for|wt|x2|height|pfix|important||gap|ch|it|rad_li|||go|left|position|false|rad_tt|ctype|oc|br|bl|id|etype|tagName|radibulletcss|il|targ|0px|top|inner|af|head|relative|pf|Object|qc|sy|radv|sx|width|border|length|rad_kille|rad_la|className|ag|attachEvent|eo|addEventListener|offsetHeight|rad_a|right|onmouseover|bb|span|bt|bvis|rad_uo|color|ds|while|nd|event|pp|ax|cstep|eval|ml|childNodes|na|rad_t|spec|offsetWidth|wo|s1|arrow|str|navigator|raised|estep|down|ay|rad_oo|rl|DIV|tree_enabled|add_div|mr|cp|rad_ibcss_get_span|fl|tree|ft|mb|mt|rad_s2|radfh|idiv|display|name|visibility|ibsize|lsp|main|radactive|rad_ibcss_active|bm|background|ibposx|levid|div|rad_ic|continue|sp|nt|csp|r2|clearTimeout|ibposy|on|rad_ibcss_hover_off|middle|Array|r1|cx|text|setTimeout|obj|aw|th|bhide|skip|createElement|ts|cssText|br_navigator|radtree|rad_s|none|pp1|radv_position_pointer|br_version|eh|isNaN|expand|rad_tree_item_expand|msize|ibcss_main_type|md|getElementById|ffffff|radfv|sub|vendorSub|rad_ibcss_init_styles|showHelp|in|write|css|parseFloat|timer|rad_ibcss_init|collapse|radwait|lev|userAgent|Netscape|rad_get_menu|ex|rad_ib|rad_th|addc|wv|hidden|tree_sub_sub_indent|square|ibvalign|rad_ibcss_hover|rad_pure|rad_tree_init|up|SPAN|az|static|a2|block|rad_ibcss_init_items|margin|ibhalign|iic|parent|all|rad_tree_init_items|ssize|ah|defaultView|appendChild|rad_gcs|rad_si|rad_convert|radts|ibcss_sub_type|rad_tree_item_click|rad_ibcss_position|hover|hide|lasth|rad_ibcss_create_inner|ww|onclick|active|rad_ibcss_wait_relative|zoom|rad_tree_init_styles|stopPropagation|UL|script|radclear|AE2323|replace|getComputedStyle|getElementsByTagName|br_oldnav|tree_expand_animation|tree_collapse_animation|mobj|acollapse|this|ismove|no_focus|rad_ibullets_hover|radtreeht|sname|currentStyle|add|jname|radtreecollapse|tree_hide_focus_box|tree_auto_collapse|tree_expand_step_size|tree_collapse_step_size|trans|ibcss_apply_to|salign|x0|aux|overflow|rad_bo|ibcss_sub_align_y|radv_a|ibcss_sub_direction|ibcss_sub_size|ibcss_sub_border_color|ibcss_sub_align_x|ibcss_sub_position_y|ibcss_sub_position_x|center|setAttribute|innerHTML|bottom|ersion|cura|inherit|pos|rad_v|rad_create|mouseover|1px|align|zIndex|rad_n|ibcss_|rad_o|load|_hover|rad_ts|addons|_active|dcm|rad_s3|rad_get_level|br_ie|onload|br_oldnav6|br_strict|ibcss_main_direction|ibcss_main_align_y|ibcss_main_size|ibcss_main_position_x|ibcss_main_position_y|ibcss_main_bg_color|ibcss_main_align_x|malign|styleFloat|clientTop|radpure|cssFloat|replaceChild|XMLHttpRequest|Opera|afari|rad_lo|parentNode|borderLeftWidth|LI|radsh|lev2|removeChild|insertAdjacentHTML|offsetTop|offsetLeft|borderTopWidth|String|qa|radmc|break|getPropertyValue|do|afterBegin|cancelBubble|click|clientLeft|class|isrun|fromCharCode|radparent|charCodeAt|undefined|mtype|_border_color|srcElement|ibcss_main_border_color|mpos|mbg|mborder|_bg_color|radtreeh|target|tree_menu|rad_index|radvibcssmenu|000000|compatMode|CSS1Compat|font|line|solid|absolute|fontSize|lineHeight|non|insertBefore|firstChild|ibcss_sub_bg_color|sborder|stype|sbg|transparent|radvbefore|spos|isibulletcss|tree_width|rad0|bbbbbb|white|float|radistreestyles|ibcss_sub_border_color_active|C70101|ibcss_main_bg_color_active|ibcss_main_border_color_active|C72828|ibcss_sub_border_color_hover|ibcss_main_bg_color_hover|blur|onfocus|visible|getAttribute|loaded|close|radtreeopen|rads|normal|space|JavaScript|auto|padding'.split('|'),0,{}))
rad_create(0,false,0,500,'all',false,false,false,false);
</script>
</body>
    </g:hasRights>
</g:hasRights>
</html>