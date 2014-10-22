<%@ page import="org.gnk.user.User" %>
<g:each status="i" in="${User.list()}" var="user">
    <div id="deletemodal${user.id}" class="modal hide fade" style="width: 400px; margin-left: -400px;"
         tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3 id="myModalLabel">Suppression</h3>
        </div>
        <g:form action="deleteUser" id="${user.id}">
            <div class="modal-body">

            Vous allez supprimer l'utilisateur : ${user.username} <br/>
            Veuillez confirmer cette action. <br/>
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary" type="submit">Valider</button>
                <a class="btn" data-dismiss="modal" aria-hidden="true">Annuler</a>
            </div>
        </g:form>
    </div>
</g:each>