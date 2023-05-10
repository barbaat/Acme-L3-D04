<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<jstl:choose>
		<jstl:when test="${_command == 'show' }">
		 	<acme:input-moment code="any.peep.form.label.moment" path="moment" readonly="true"/>
			<acme:input-textbox code="any.peep.form.label.title" path="title" readonly="true"/>	
			<acme:input-textbox code="any.peep.form.label.nick" path="nick" readonly="true"/>
			<acme:input-textbox code="any.peep.form.label.message" path="message" readonly="true"/>
			<acme:input-email code="any.peep.form.label.email" path="email" readonly="true"/>
			<acme:input-url code="any.peep.form.label.link" path="link" readonly="true"/>			
		</jstl:when>
		<jstl:when test="${_command == 'create' }">
			<acme:input-textbox code="any.peep.form.label.title" path="title"/>	
			<acme:input-textbox code="any.peep.form.label.nick" path="nick"/>
			<acme:input-textbox code="any.peep.form.label.message" path="message"/>
			<acme:input-email code="any.peep.form.label.email" path="email"/>
			<acme:input-url code="any.peep.form.label.link" path="link"/>			
			<acme:submit code="any.peep.form.button.publish" action="/any/peep/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>