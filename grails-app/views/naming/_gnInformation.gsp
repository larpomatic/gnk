<div class="row-fluid">
    <div class="span4"><h3 class="cap">information gn</h3></div>
    <div class="span2"><button type="button" class="btn" data-toggle="collapse" data-target="#gnInformation"><i class="icon-arrow-down"></i></button></div>
</div>

<div id="gnInformation" class="collapse in">
    <table class="table table-condensed">
        <thead>
        <tr class="upper">
            <th>titre</th>
            <th>tag(s)</th>
            <th>univers</th>
            <th>date de début</th>
            <th>durée (h)</th>
            <th>nb joueurs</th>
            <th>création</th>
            <th>mise à jour</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="cap">${gnInfo.name}</td>
            <td>
                <ul class="unstyled">
                    <g:each status="i" in="${gnInfo.tagList}" var="tag">
                        <li><strong class="cap">${tag.value}</strong> (<span class="cap">${tag.family}</span> / ${tag.weight})</li>
                    </g:each>
                </ul>
            </td>
            <td class="cap">${gnInfo.universe}</td>
            <td title="yyyy-MM-dd"><g:formatDate format="dd/MM/yyyy HH:mm" date="${gnInfo.t0Date}"/></td>
            <td class="cap">${gnInfo.duration}</td>
            <td class="cap">${gnInfo.nbPlayers}</td>
            <td title="yyyy-MM-dd"><g:formatDate format="dd/MM/yyyy" date="${gnInfo.dateCreated}"/></td>
            <td title="yyyy-MM-dd"><g:formatDate format="dd/MM/yyyy" date="${gnInfo.lastUpdated}"/></td>
        </tr>
        </tbody>
    </table>
</div>
