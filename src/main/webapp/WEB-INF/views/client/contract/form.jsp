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
	<acme:input-textbox code="client.contract.form.label.code" path="code"/>
	<acme:input-moment code="client.contract.form.label.instantiationMoment" path="instantiationMoment" readonly="true"/>
	<acme:input-textbox code="client.contract.form.label.providerName" path="providerName"/>
	<acme:input-textbox code="client.contract.form.label.customerName" path="customerName"/>
	<acme:input-textarea code="client.contract.form.label.goals" path="goals"/>
	<acme:input-money code="client.contract.form.label.budget" path="budget"/>
	<acme:input-select code="client.contract.form.label.project" path="project" choices="${choices}"/>
	<jstl:choose>
		<jstl:when test="${remainingBudget != null}">
			<p>
				<acme:print value="Remaining budget: ${remainingBudget}"/>
			</p>			
		</jstl:when>
	</jstl:choose>
	<acme:hidden-data path="draftMode"/>
	
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="client.contract.form.button.progress-logs" action="/client/progress-log/list-mine?contractId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:button code="client.contract.form.button.progress-logs" action="/client/progress-log/list-mine?contractId=${id}"/>
			<acme:submit code="client.contract.form.button.update" action="/client/contract/update"/>
			<acme:submit code="client.contract.form.button.delete" action="/client/contract/delete"/>
			<acme:submit code="client.contract.form.button.publish" action="/client/contract/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="client.contract.form.button.create" action="/client/contract/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>