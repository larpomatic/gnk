<%@ page import="org.gnk.tag.TagRelation; org.gnk.tag.Tag" %>
<g:each status="i" in="${TagRelation.list()}" var="tagRelation">
    <div id="modaledit${tagRelation.id}" class="modal hide fade" style="width: 800px; margin-left: -400px;"
         tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3 id="myModalLabel">Relation entre ${tagRelation.tag1.name.encodeAsHTML()} et ${tagRelation.tag2.name.encodeAsHTML()} </h3>
        </div>

        <div class="modal-body">
          Weight : <input name="weightTag${tagRelation.id}" value="${tagRelation.weight}"/>
          Bijective : <input name="bijectivTag${tagRelation.id}" value="${tagRelation.isBijective}"/>
        </div>

        <div class="modal-footer">
            <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
        </div>
    </div>
</g:each>