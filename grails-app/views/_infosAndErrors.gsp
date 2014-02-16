<g:if test="${flash.message}">
	<div class="alert alert-error" role="status">${flash.message}</div>
</g:if>

<g:if test="${flash.messageInfo}">
	<div class="alert alert-success" role="status">${flash.messageInfo}</div>
</g:if>