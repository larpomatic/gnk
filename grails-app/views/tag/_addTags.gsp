<%@ page import="org.gnk.tag.Tag" %>
<%@ page import="org.gnk.admin.right" %>
%{--<%@ page import="org.gnk.tag.TagFamily" %>--}%
<div id="addchildmodal" class="modal hide fade" style="width: 400px; margin-left: -400px;"
     role="dialog" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>

        <h3 id="myModalLabel">Ajouter un Tag en fils</h3>
    </div>
    <g:form action="save">

        <input type="hidden" id="idParentSave" name="idParentSave"/>

        <div class="modal-body">
            <label>Nom du tag : <input id="nameTag" name="nameTag"/></label>

            <div class="modal-footer">
                <button class="btn btn-primary" type="submit">Valider</button>
                <a class="btn" data-dismiss="modal" aria-hidden="true">Annuler</a>
            </div>
        </div>
    </g:form>
</div>
