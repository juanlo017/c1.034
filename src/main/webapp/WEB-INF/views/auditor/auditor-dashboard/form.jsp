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

<h2>
	<acme:message code="auditor.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">

	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.totalNumberOfStaticCodeAudit"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfStaticCodeAudit}"/>
		</td>
	</tr>	


	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.totalNumberOfDynamicCodeAudit"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfDynamicCodeAudit}"/>
		</td>
	</tr>	

	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.averageNumberOfAuditRecords"/>
		</th>
		<td>
			<acme:print value="${averageNumberOfAuditRecords}"/>
		</td>
	</tr>	

	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.deviationNumberOfAuditRecords"/>
		</th>
		<td>
			<acme:print value="${deviationNumberOfAuditRecords}"/>
		</td>
	</tr>	

	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.minimumNumberOfAuditRecords"/>
		</th>
		<td>
			<acme:print value="${minimumNumberOfAuditRecords}"/>
		</td>
	</tr>	

	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.maximumNumberOfAuditRecords"/>
		</th>
		<td>
			<acme:print value="${maximumNumberOfAuditRecords}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.averageNumberOfPeriod"/>
		</th>
		<td>
			<acme:print value="${averageNumberOfPeriod}"/>
		</td>
	</tr>	

	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.deviationNumberOfPeriod"/>
		</th>
		<td>
			<acme:print value="${deviationNumberOfPeriod}"/>
		</td>
	</tr>	

	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.minimumNumberOfPeriod"/>
		</th>
		<td>
			<acme:print value="${minimumNumberOfPeriod}"/>
		</td>
	</tr>	

	<tr>
		<th scope="row">
			<acme:message code="auditor.dashboard.form.label.maximumNumberOfPeriod"/>
		</th>
		<td>
			<acme:print value="${maximumNumberOfPeriod}"/>
		</td>
	</tr>	
</table>


<acme:return/>