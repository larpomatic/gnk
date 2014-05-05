<div class="btn-group-vertical pull-left">
    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#roleModal" style="width:100px">
        <span class="glyphicon glyphicon-user"></span> Roles
    </button>
    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#objectModal" style="width:100px">
        <span class="glyphicon glyphicon-briefcase"></span> Objets
    </button>
    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#placeModal" style="width:100px">
        <span class="glyphicon glyphicon-home"></span> Lieux
    </button>
    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#relationModal" style="width:100px">
        <span class="glyphicon glyphicon-random"></span> Relations
    </button>
</div>

<g:render template="generalDescriptionForm" />

<g:render template="modalContainer" model="${[id: "roleModal", template: "rolesForm", title: "Roles"]}" />
%{--<g:render template="modalContainer" model="${[id: "objectModal", template: "objectForm", title: "Objets"]}" />--}%
<g:render template="modalContainer" model="${[id: "relationModal", template: "relationsForm", title: "Relations"]}" />
