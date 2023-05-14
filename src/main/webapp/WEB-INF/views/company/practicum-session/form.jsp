<%--
- form.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>



<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="company.practicum-session.form.label.title" path="title"/>
	<acme:input-textarea code="company.practicum-session.form.label.abstract$" path="abstract$"/>
	<acme:input-moment code="company.practicum-session.form.label.startPeriod" path="startPeriod"/>
	<acme:input-moment code="company.practicum-session.form.label.finishPeriod" path="finishPeriod"/>
	<acme:input-textbox code="company.practicum-session.form.label.optionalLink" path="optionalLink"/>
		
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
			<acme:submit code="company.practicum-session.form.button.update" action="/company/practicum-session/update"/>
			<acme:submit code="company.practicum-session.form.button.delete" action="/company/practicum-session/delete"/>
		</jstl:when>
		
		<jstl:when test="${_command == 'create' && draftMode}">
			<acme:submit code="company.practicum-session.form.button.create" action="/company/practicum-session/create?masterId=${masterId}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'&& !draftMode }">
			<acme:input-checkbox code="company.practicum-session.form.button.confirmation" path="confirmation"/>
			<acme:submit code="company.practicum-session.form.button.create-exceptional" action="/company/practicum-session/create?masterId=${masterId}"/>
		</jstl:when>
	</jstl:choose>
	<jstl:if test="${exceptional == true}">
		<acme:message code="company.practicum-session.form.message.exceptional"/>
	</jstl:if>
</acme:form>