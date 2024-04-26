<%--
- list.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.sponsorship.list.label.code" path="code" width="10%"/>
	<acme:list-column code="any.sponsorship.list.label.moment" path="moment" width="10%"/>
	<acme:list-column code="any.sponsorship.list.label.startTime" path="startTime" width="10%"/>	
	<acme:list-column code="any.sponsorship.list.label.endTime" path="endTime" width="10%"/>	
	<acme:list-column code="any.sponsorship.list.label.amount" path="amount" width="10%"/>	
	<acme:list-column code="any.sponsorship.list.label.type" path="type" width="10%"/>
	<acme:list-column code="any.sponsorship.list.label.optionalEmail" path="optionalEmail" width="10%"/>
	<acme:list-column code="any.sponsorship.list.label.optionalLink" path="optionalLink" width="10%"/>
	<acme:list-column code="any.sponsorship.list.label.project" path="project" width="10%"/>

</acme:list>