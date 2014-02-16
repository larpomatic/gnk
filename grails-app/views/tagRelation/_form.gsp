<%@ page import="org.gnk.tag.TagRelation" %>



<div class="fieldcontain ${hasErrors(bean: tagRelationInstance, field: 'isBijective', 'error')} ">
	<label for="isBijective">
		<g:message code="tagRelation.isBijective.label" default="Is Bijective" />
		
	</label>
	<g:checkBox name="isBijective" value="${tagRelationInstance?.isBijective}" />
</div>

<div class="fieldcontain ${hasErrors(bean: tagRelationInstance, field: 'tag1', 'error')} required">
	<label for="tag1">
		<g:message code="tagRelation.tag1.label" default="Tag1" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="tag1" name="tag1.id" from="${org.gnk.tag.Tag.list()}" optionKey="id" required="" value="${tagRelationInstance?.tag1?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tagRelationInstance, field: 'tag2', 'error')} required">
	<label for="tag2">
		<g:message code="tagRelation.tag2.label" default="Tag2" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="tag2" name="tag2.id" from="${org.gnk.tag.Tag.list()}" optionKey="id" required="" value="${tagRelationInstance?.tag2?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tagRelationInstance, field: 'weight', 'error')} required">
	<label for="weight">
		<g:message code="tagRelation.weight.label" default="Weight" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="weight" type="number" value="${tagRelationInstance.weight}" required=""/>
</div>

