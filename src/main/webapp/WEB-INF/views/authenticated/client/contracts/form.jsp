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
	<acme:input-textbox code="authenticated.client.contracts.form.label.code" path="code"/>
	<acme:input-textbox code="authenticated.client.contracts.form.label.providerName" path="providerName"/>
	<acme:input-textbox code="authenticated.client.contracts.form.label.customerName" path="customerName"/>
	<acme:input-textbox code="authenticated.client.contracts.form.label.goals" path="goals"/>
	<acme:input-textbox code="authenticated.client.contracts.form.label.budget" path="budget"/>
	<acme:input-textbox code="authenticated.client.contracts.form.label.project" path="project"/>
	
	<acme:submit test="${_command == 'create'}" code="authenticated.client.contracts.form.button.create" action="/authenticated/client/contracts/create"/>
	<acme:submit test="${_command == 'update'}" code="authenticated.client.contracts.form.button.update" action="/authenticated/consumer/contracts/update"/>
	<acme:submit test="${_command == 'publish'}" code="authenticated.client.contracts.form.button.publish" action="/authenticated/consumer/contracts/publish"/>
	<acme:submit test="${_command == 'delete'}" code="authenticated.client.contracts.form.button.delete" action="/authenticated/consumer/contracts/delete"/>
</acme:form>