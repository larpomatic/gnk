<script>
	function toggle(checkboxID, toggleID) {
		var checkbox = document.getElementById(checkboxID);
		var toggle = document.getElementById(toggleID);
		updateToggle = toggle.disabled = !checkbox.checked;
	}
</script>
		 <div id="${idPopup}" class="modal hide fade" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h3 id="myModalLabel">${namePopup }</h3>
			</div>
			<div class="modal-body">
				<table>
					<g:each in="${tagList}" status="i" var="tagInstance">
						<g:if test="${i % 3 == 0}">
							<tr>
						</g:if>
						<td><label for="${tagPrefix }${tagInstance?.id}" style="float: left"><g:checkBox
									name="${tagPrefix }${tagInstance?.id}"
									id="${tagPrefix }${tagInstance?.id}"
									checked="${myOwner?.hasTag(tagInstance, tagListName)}"
									onClick="toggle('${tagPrefix }${tagInstance?.id}', '${weightTagPrefix}${tagInstance?.id}')"/>  ${fieldValue(bean: tagInstance, field: "name")}</label>
									<div style="overflow: hidden; padding-left: .5em;">
									<g:if test="${myOwner != null}">
										<g:set var="tagValue" value="${myOwner?.getTagWeight(tagInstance, tagListName)}" scope="page" />
									</g:if>
									<g:if test="${myOwner == null}">
										<g:set var="tagValue" value="50" scope="page" />
									</g:if>
									<input id="${weightTagPrefix}${tagInstance?.id}" name="${weightTagPrefix}${tagInstance?.id}" value="${tagValue}" type="number" style="width:40px;"></div>
									<script>
										toggle('${tagPrefix }${tagInstance?.id}', '${weightTagPrefix}${tagInstance?.id}')
									</script>
						</td>
						<g:if test="${(i+1) % 3 == 0}">
							</tr>
						</g:if>
					</g:each>

				</table>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">Ok</button>
			</div>
		</div>