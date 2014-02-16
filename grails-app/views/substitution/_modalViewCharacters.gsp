<g:each status="i" in="${characterList}" var="character">
    <div id="modalChar${i + 1}" class="modal hide fade" style="width: 800px; margin-left: -400px;" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3 id="myModalLabel">
                CHAR ${character.id.encodeAsHTML()}
            </h3>
        </div>

        <div class="modal-body">
            <h4 class="cap">genre</h4>
            <div class="upper">
                ${character.gender.encodeAsHTML()}
            </div>

            <h4 class="cap">tags</h4>
            <table class="table table-condensed">
                <thead>
                <tr class="upper">
                    <th>#</th>
                    <th>valeur</th>
                    <th>famille</th>
                    <th>poids</th>
                    <th>status</th>
                </tr>
                </thead>
                <tbody>
                <g:each status="j" in="${character.tags}" var="tag">
                    <tr class="cap">
                        <td>
                            ${j + 1}
                        </td>
                        <td><strong> ${tag.value.encodeAsHTML()}
                        </strong></td>
                        <td>
                            ${tag.type.encodeAsHTML()}
                        </td>
                        <td>
                            ${tag.weight.encodeAsHTML()}
                        </td>
                        <td>
                            ${tag.status.encodeAsHTML()}
                        </td>
                    </tr>
                </g:each>
                <tbody>
            </table>

            <h4 class="cap">rôles</h4>
            <table class="table table-condensed">
                <thead>
                <tr class="upper">
                    <th>#</th>
                    <th>code</th>
                    <th>type</th>
                    <th>description</th>
                    <th>plot</th>
                    <th>pipr</th>
                    <th>pipi</th>
                    <th>score</th>
                    <th>status</th>
                </tr>
                </thead>
                <tbody>
                <g:each status="j" in="${character.roles}" var="role">
                    <tr>
                        <td>
                            ${j + 1}
                        </td>
                        <td class="cap"><strong> ${role.role.code.encodeAsHTML()}
                        </strong></td>
                        <td class="cap">
                            ${role.role.type.encodeAsHTML()}
                        </td>
                        <td class="cap">
                            ${role.role.description.encodeAsHTML()}
                        </td>
                        <td class="cap">
                            ${role.plot.title.encodeAsHTML()}
                        </td>
                        <td>
                            ${role.role.pipr.encodeAsHTML()}
                        </td>
                        <td>
                            ${role.role.pipi.encodeAsHTML()}
                        </td>
                        <td>
                            ${role.scoring.encodeAsHTML()}
                        </td>
                        <td class="cap">
                            ${role.status.encodeAsHTML()}
                        </td>
                    </tr>
                </g:each>
                <tbody>
            </table>

            <h4 class="cap">intrigues</h4>
            <table class="table table-condensed">
                <thead>
                <tr class="upper">
                    <th>#</th>
                    <th>nom</th>
                    <th>description</th>
                    <th>pipp</th>
                </tr>
                </thead>
                <tbody>
                <g:each status="j" in="${character.roles}" var="role">
                    <tr>
                        <td>
                            ${j + 1}
                        </td>
                        <td class="cap"><strong> ${role.plot.title.encodeAsHTML()}
                        </strong></td>
                        <td>
                            ${role.plot.description.encodeAsHTML()}
                        </td>
                        <td class="cap">
                            ${role.plot.pipp.encodeAsHTML()}
                        </td>
                    </tr>
                </g:each>
                <tbody>
            </table>

            <h4 class="cap">relations</h4>
            <table class="table table-condensed">
                <thead>
                <tr class="upper">
                    <th>#</th>
                    <th>personnage</th>
                    <th>type</th>
                    <th>bijectivité</th>
                    <th>poids</th>
                </tr>
                </thead>
                <tbody>
                <g:each status="j" in="${character.relations}" var="relation">
                    <tr>
                        <td>
                            ${j + 1}
                        </td>
                        <td class="cap"><strong>CHAR ${relation.character.id.encodeAsHTML()}</strong></td>
                        <td class="cap">
                            ${relation.type.encodeAsHTML()}
                        </td>
                        <td class="cap"><g:if test="${relation.isBijective == true}">
                            oui
                        </g:if> <g:else>
                            non
                        </g:else></td>
                        <td class="cap">
                            ${relation.weight.encodeAsHTML()}
                        </td>
                    </tr>
                </g:each>
                <tbody>
            </table>
        </div>

        <div class="modal-footer">
            <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
        </div>
    </div>
</g:each>