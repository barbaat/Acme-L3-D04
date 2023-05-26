<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="assistant.tutorial.form.label.code" placeholder="ABC123" path="code"/>	
	<acme:input-textbox code="assistant.tutorial.form.label.title" path="title"/>	
	<acme:input-textarea code="assistant.tutorial.form.label.abstract" path="abstractTutorial"/>
	<acme:input-textarea code="assistant.tutorial.form.label.goals" path="goals"/>
	<acme:input-select code="assistant.tutorial.form.label.course" path="course" choices="${courses}"/>
	<acme:input-double code="assistant.tutorial.form.label.estimatedTotalTime" path="estimatedTotalTime"/>

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="assistant.tutorial.form.button.update" action="/assistant/tutorial/update"/>
			<acme:submit code="assistant.tutorial.form.button.delete" action="/assistant/tutorial/delete"/>
			<acme:submit code="assistant.tutorial.form.button.publish" action="/assistant/tutorial/publish"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show')}">
			<acme:button code="assistant.tutorial.form.label.sessions" action="/assistant/session/list?tutorialId=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistant.tutorial.form.button.create" action="/assistant/tutorial/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>