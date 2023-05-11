<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="lecturer.course.form.label.code" path="code" width="20%" />
	<acme:list-column code="lecturer.course.form.label.title" path="title"  width="60%%"/>
	<acme:list-column code="lecturer.course.form.label.retailPrice" path="retailPrice" width="20%" />
</acme:list>

<acme:button code="lecturer.course.form.button.create" action="/lecturer/course/create"/>