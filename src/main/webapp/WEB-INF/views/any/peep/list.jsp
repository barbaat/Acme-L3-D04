<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.peep.list.label.moment" path="moment" width="20%"/>	
	<acme:list-column code="any.peep.list.label.title" path="title" width="20%"/>
	<acme:list-column code="any.peep.list.label.nick" path="nick" width="20%"/>
	<acme:list-column code="any.peep.list.label.message" path="message" width="50%"/>	
</acme:list>

<acme:button code="any.peep.list.button.publish" action="/any/peep/create"/>