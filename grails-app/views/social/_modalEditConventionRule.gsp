<%@ page import="org.gnk.tag.Tag" %>
<%@ page import="org.gnk.naming.Rule" %>
<%@ page import="org.gnk.naming.Convention" %>
<%@ page import="org.gnk.naming.ConventionHasRule" %>
<g:each status="i" in="${Convention.list()}" var="conv">
    <div id="editmodal${conv.id}" class="modal hide fade" style="width: 800px; margin-left: -400px;"
         tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>

            <h3 id="myModalLabel">Editer les Règles liées</h3>
        </div>
        <g:form action="editRelevantRule" id="${conv.id}">
            <div class="modal-body">
                <g:if test="${ConventionHasRule.list()}">
                    <table>
                        <thead>
                        <tr class="upper">
                            <th style="text-align: center;">#</th>
                            <th>Rule</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each status="n" in="${Rule.list()}" var="rule">
                            <tr id="char${rule.id}">
                                <!-- convention rule -->
                                <td style="text-align: center;">${n + 1}</td>
                                <!-- description -->
                                <td class="cap">${rule.description}</td>
                                <!-- checkBox -->
                                <!--check(${conv}, ${rule}, ${ConventionHasRule.list()})-->
                                <td>
                                    <g:set var="foo" value="${false}"/>
                                    <g:each in="${ConventionHasRule.list()}" var="chr">
                                        <g:if test="${chr.convention.equals(conv) && chr.rule.equals(rule)}">
                                            <g:set var="foo" value="${true}"/>
                                        </g:if>
                                    </g:each>
                                    <g:checkBox name="rule.${rule.id}.checkBox" checked="${foo}"/>
                                </td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </g:if>
            </div>

            <div class="modal-footer">
                <button class="btn btn-primary" type="submit">Valider</button>
                <a class="btn" data-dismiss="modal" aria-hidden="true">Annuler</a>
            </div>
        </g:form>
    </div>
</g:each>

<script type="text/javascript">
    function check() {
        /*function check(conv, rule, chrs) {
         for each (chr in chrs) {
         if(chr.convention == conv && chr.rule == rule){
         return false;
         }
         }*/
        return true;
    }
</script>