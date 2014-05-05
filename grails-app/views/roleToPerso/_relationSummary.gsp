<%@ page import="org.gnk.roletoperso.RoleHasRelationWithRole" %>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<g:javascript src="role2perso/springy.js"></g:javascript>
<g:javascript src="role2perso/springyui.js"></g:javascript>
<script>
    var graph = new Springy.Graph();
    <g:each in="${characterList}" status="characterIter" var="character">
    graph.addNodes('P${character.DTDId}');
    </g:each>

    <g:each in="${characterList}" status="characterIter" var="character">
    <g:set var="characterRel1" value="${character.DTDId}"/>
    <g:each in="${characterList}" status="charIter"
            var="char2">
    <g:set var="characterRel2" value="${char2.DTDId}"/>
    <g:set var="label" value="${character.getRelatedCharactersExceptBijectivesLabel(gnInstance).get(char2)}"/>
    <g:if test="${label.isEmpty() == false}">
    graph.addEdges(
            ['P${characterRel1}', 'P${characterRel2}', {color: '#7DBE3C', label: '${label}', weight: 1.0}]
    );
    </g:if>

    </g:each>
    </g:each>

    jQuery(function () {
        var springy = jQuery('#canvas').springy({
            graph: graph
        });
    });
</script>

<canvas id="canvas"></canvas>

<script>
    var canvas = document.querySelector('canvas');
    fitToContainer(canvas);

    function fitToContainer(canvas) {
        // Make it visually fill the positioned parent
        canvas.style.width = '100%';
        canvas.style.height = '100%';
        // ...then set the internal size to match
        canvas.width = canvas.offsetWidth;
        canvas.height = canvas.offsetHeight;
    }
</script>

