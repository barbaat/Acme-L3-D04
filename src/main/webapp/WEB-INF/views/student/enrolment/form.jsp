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
<jstl:choose>
 	<jstl:when test="${_command == 'show-workbook'}">
		<acme:form>
		    <acme:input-textbox readonly="true" code="student.enrolment.form.label.courseTitle" path="courseTitle"/>
		    <acme:input-textbox readonly="true" code="student.enrolment.form.label.workTime" path="workTime"/>
		    <acme:input-textbox readonly="true" code="student.enrolment.form.label.numActivities" path="numActivities"/>
		    
		    <acme:button code="student.enrolment.form.button.activities" action="/student/activity/list?enrolmentId=${id}"/>
		</acme:form>
	</jstl:when>
	<jstl:when test="${_command == 'publish'}">
		<acme:form>
		    <acme:input-textbox code="student.enrolment.publish.label.cardLowerNibble" path="cardLowerNibble"/>
		    <acme:input-textbox code="student.enrolment.publish.label.cardHolder" path="cardHolder"/>
		    <acme:input-integer code="student.enrolment.publish.label.cvv" path="cvv" placeholder="123"/>
		    <acme:input-moment code="student.enrolment.publish.label.expireDate" path="expireDate" placeholder="student.enrolment.publish.placeholder.expireDate"/>
		    
			<acme:submit code="student.enrolment.publish.button.publish" action="/student/enrolment/publish"/>
		</acme:form>
	</jstl:when>
	<jstl:otherwise>
		<acme:form>
		    <acme:input-textbox code="student.enrolment.form.label.code" path="code"/>
		    <acme:input-select code="student.enrolment.form.label.course" path="course" choices="${courses}"/>
		    <acme:input-textbox readonly="true" code="student.enrolment.form.label.workTime" path="workTime"/>
		    <acme:input-textarea code="student.enrolment.form.label.motivation" path="motivation"/>
		    <acme:input-textarea code="student.enrolment.form.label.goals" path="goals"/>
		    
		    <jstl:choose>
		        <jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
		        	<jstl:choose>
		        		<jstl:when test="${draftMode}">
				            <acme:submit code="student.enrolment.form.button.update" action="/student/enrolment/update"/>
				            <acme:submit code="student.enrolment.form.button.delete" action="/student/enrolment/delete"/>
				             <acme:button code="student.enrolment.form.button.payment" action="/student/enrolment/publish?id=${id}"/>
		        		</jstl:when>
		        		<jstl:otherwise>
		        			<acme:input-textbox code="student.enrolment.form.label.cardHolder" path="cardHolder"/>
						    <acme:input-textbox code="student.enrolment.form.label.cardLowerNibble" path="cardLowerNibble"/>
		            		<acme:button code="student.enrolment.form.button.workbook" action="/student/enrolment/show-workbook?enrolmentId=${id}"/>
		        		</jstl:otherwise>
		        	</jstl:choose>
		        </jstl:when>
		        <jstl:when test="${_command == 'register'}">
					<acme:submit code="student.enrolment.form.button.register" action="/student/enrolment/register?courseId=${courseId}"/>
		        </jstl:when>
		    </jstl:choose>
		</acme:form>
	</jstl:otherwise>
</jstl:choose>