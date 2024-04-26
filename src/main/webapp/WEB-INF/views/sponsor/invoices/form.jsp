<%--
- form.jsp
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

<acme:form>
	<acme:input-textbox code="sponsor.invoices.form.label.code" path="code"/>
	<acme:input-moment code="sponsor.invoices.form.label.registrationTime" path="registrationTime"/>
	<acme:input-moment code="sponsor.invoices.form.label.dueDate" path="dueDate"/>	
	<acme:input-money code="sponsor.invoices.form.label.quantity" path="quantity"/>	
	<acme:input-double code="sponsor.invoices.form.label.tax" path="tax"/>
	<acme:input-url code="sponsor.invoices.form.label.optionalLink" path="optionalLink"/>
	
	<jstl:choose>	 
		
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="sponsor.invoices.form.button.update" action="/sponsor/invoices/update"/>
			<acme:submit code="sponsor.invoices.form.button.delete" action="/sponsor/invoices/delete"/>
			<acme:submit code="sponsor.invoices.form.button.publish" action="/sponsor/invoices/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="sponsor.invoices.form.button.create" action="/sponsor/invoices/create?masterId=${masterId}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>