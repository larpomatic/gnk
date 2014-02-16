<legend>Import d'un GN au format DTD</legend>
<g:uploadForm controller="tag" action="readFromDTD" method="post" >
    <tr class="prop">
        <td valign="top">
            <input type="file" id="file" name="file"/>
        </td>
    </tr>
    <fieldset class="buttons">
        <g:submitButton name="create" class="btn btn-primary" value="Envoyer" />
    </fieldset>
</g:uploadForm>