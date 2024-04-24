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

<%@page  language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="client.contracts.form.label.code" path="code"/>
	<acme:input-textbox code="client.contracts.form.label.providerName" path="providerName"/>
	<acme:input-textbox code="client.contracts.form.label.customerName" path="customerName"/>
	<acme:input-textbox code="client.contracts.form.label.goals" path="goals"/>
	<acme:input-textbox code="client.contracts.form.label.budget" path="budget"/>
	<acme:input-textbox code="client.contracts.form.label.project" path="project"/>
	<acme:hidden-data path="draftMode"/>
	
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="client.contracts.form.button.stories" action="/client/contracts/list?contractId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="client.contracts.form.button.update" action="client/contracts/update"/>
			<acme:submit code="client.contracts.form.button.delete" action="client/contracts/delete"/>
			<acme:submit code="client.contracts.form.button.publish" action="client/contracts/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="client.contracts.form.button.create" action="client/contracts/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>