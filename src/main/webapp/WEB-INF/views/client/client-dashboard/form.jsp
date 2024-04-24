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

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.numberOfProgressLogs"/>
		</th>
		<td>
			<acme:print value="${numberOfProgressLogs}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.progressLogsCompletenessRate"/>
		</th>
		<td>
			<acme:print value="${progressLogsCompletenessRate}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.avgContractBudget"/>
		</th>
		<td>
			<acme:print value="${avgContractBudget}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.minContractBudget"/>
		</th>
		<td>
			<acme:print value="${minContractBudget}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.maxContractBudget"/>
		</th>
		<td>
			<acme:print value="${maxContractBudget}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="client.dashboard.form.label.deviationContractBudget"/>
		</th>
		<td>
			<acme:print value="${deviationContractBudget}"/>
		</td>
	</tr>	
</table>

<acme:return/>
