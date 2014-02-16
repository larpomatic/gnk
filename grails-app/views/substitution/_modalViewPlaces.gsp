<g:each status="i" in="${places}" var="place">
	<div id="modalPla${i + 1}" class="modal hide fade"
		style="width: 800px; margin-left: -400px;" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">×</button>
			<h3 id="myModalLabel">
				PLA ${place.id.encodeAsHTML()}
			</h3>
		</div>

		<div class="modal-body">
			<h4 class="cap">intrigue</h4>
			<table class="table table-condensed">
				<thead>
					<tr class="upper">
						<th>nom</th>
						<th>description</th>
						<th>pipp</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="cap"><strong> ${place.plot.title.encodeAsHTML()}
						</strong></td>
						<td>
							${place.plot.description.encodeAsHTML()}
						</td>
						<td class="cap">
							${place.plot.pipp.encodeAsHTML()}
						</td>
					<tr>
				<tbody>
			</table>
		</div>
			
		<div class="modal-footer">
			<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
		</div>
	</div>
</g:each>