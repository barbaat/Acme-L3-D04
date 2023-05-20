<%@page language="java" import="acme.framework.helpers.PrincipalHelper,acme.roles.Provider,acme.roles.Consumer"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:menu-bar code="master.menu.home">
	<acme:menu-left>
		
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-student1" action="https://shop.realbetisbalompie.es/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-student2" action="https://es.shein.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-student3" action="https://www.youtube.com/channel/UCxT6yeg3kKoi6-RJ5ut3wmw"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-student4" action="https://www.rafanadalacademy.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link-student5" action="https://www.youtube.com/watch?v=o1dfTZS8E90"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.any">
			<acme:menu-suboption code="master.menu.any.courses" action="/any/course/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.anonymous.peep.list" action="/any/peep/list"/>
			<acme:menu-suboption code="master.menu.anonymous.peep.create" action="/any/peep/create"/>
		</acme:menu-option>
		
		
		<acme:menu-option code="master.menu.authenticated" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.authenticated.note.create" action="/authenticated/note/create"/>	
			<acme:menu-suboption code="master.menu.authenticated.note.list" action="/authenticated/note/list"/>
			<acme:menu-separator/>	
			<acme:menu-suboption code="master.menu.authenticated.offer.list" action="/authenticated/offer/list"/>
			<acme:menu-separator/>	
			<acme:menu-suboption code="master.menu.authenticated.assistant.list" action="/authenticated/assistant/list"/>
		</acme:menu-option>


		<acme:menu-option code="master.menu.administrator" access="hasRole('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.offer.create" action="/administrator/offer/create"/>
			<acme:menu-suboption code="master.menu.administrator.offer.list" action="/administrator/offer/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.banner.create" action="/administrator/banner/create"/>
			<acme:menu-suboption code="master.menu.administrator.banner.list" action="/administrator/banner/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-initial" action="/administrator/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-sample" action="/administrator/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-sample" action="/administrator/populate-sample"/>		
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-down" action="/administrator/shut-down"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.provider" access="hasRole('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRole('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.lecturer" access="hasRole('Lecturer')">
			<acme:menu-suboption code="master.menu.lecturer.lecture.create" action="/lecturer/lecture/create"/>
			<acme:menu-suboption code="master.menu.lecturer.lecture.list" action="/lecturer/lecture/list-all"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.lecturer.course.create" action="/lecturer/course/create"/>
			<acme:menu-suboption code="master.menu.lecturer.course.list" action="/lecturer/course/list"/>	
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.lecturer.lecturer-dashboard.show" action="/lecturer/lecturer-dashboard/show"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.student" access="hasRole('Student')">
		    <acme:menu-suboption code="master.menu.student.course.list" action="/student/course/list"/>
		    <acme:menu-separator/>
			<acme:menu-suboption code="master.menu.student.enrolment.register" action="/student/enrolment/register"/>
			<acme:menu-suboption code="master.menu.student.enrolment.list" action="/student/enrolment/list"/>			
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.auditor" access="hasRole('Auditor')">
			<acme:menu-suboption code="master.menu.auditor.audit.create" action="/auditor/audit/create"/>
			<acme:menu-suboption code="master.menu.auditor.audit.list" action="/auditor/audit/list"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.company" access="hasRole('Company')">
			<acme:menu-suboption code="master.menu.company.practicum.create" action="/company/practicum/create"/>		
			<acme:menu-suboption code="master.menu.company.practicum.list" action="/company/practicum/list"/>	
			<acme:menu-separator/>	
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.assistant" access="hasRole('Assistant')">
			<acme:menu-suboption code="master.menu.assistant.tutorial.list" action="/assistant/tutorial/list"/>
			<acme:menu-suboption code="master.menu.assistant.tutorial.create" action="/assistant/tutorial/create"/>	
		</acme:menu-option>
	</acme:menu-left>

	<acme:menu-right>
		<acme:menu-option code="master.menu.sign-up" action="/anonymous/user-account/create" access="isAnonymous()"/>
		<acme:menu-option code="master.menu.sign-in" action="/master/sign-in" access="isAnonymous()"/>

		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-data" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRole('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider" action="/authenticated/provider/update" access="hasRole('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRole('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer" action="/authenticated/consumer/update" access="hasRole('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-student" action="/authenticated/student/create" access="!hasRole('Student')"/>
		 	<acme:menu-suboption code="master.menu.user-account.student" action="/authenticated/student/update" access="hasRole('Student')"/>
		 	<acme:menu-suboption code="master.menu.user-account.become-company" action="/authenticated/company/create" access="!hasRole('Company')"/>
		 	<acme:menu-suboption code="master.menu.user-account.company" action="/authenticated/company/update" access="hasRole('Company')"/>
		 	<acme:menu-suboption code="master.menu.user-account.become-lecturer" action="/authenticated/lecturer/create" access="!hasRole('Lecturer')"/>
		 	<acme:menu-suboption code="master.menu.user-account.lecturer" action="/authenticated/lecturer/update" access="hasRole('Lecturer')"/>
		 	<acme:menu-suboption code="master.menu.user-account.become-auditor" action="/authenticated/auditor/create" access="!hasRole('Auditor')"/>
		 	<acme:menu-suboption code="master.menu.user-account.auditor" action="/authenticated/auditor/update" access="hasRole('Auditor')"/>
		 	<acme:menu-suboption code="master.menu.user-account.become-assistant" action="/authenticated/assistant/create" access="!hasRole('Assistant')"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.sign-out" action="/master/sign-out" access="isAuthenticated()"/>
	</acme:menu-right>
</acme:menu-bar>

