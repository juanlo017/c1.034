<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:message code="developer.developer-dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">

	<tr>
		<th scope="row">
			<acme:message code="developer.developer-dashboard.form.label.total-number-of-training-modules-with-update-moment"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfTrainingModulesWithUpdateMoment}"/>
		</td>
	</tr>	


	<tr>
		<th scope="row">
			<acme:message code="developer.developer-dashboard.form.label.total-number-training-sessions-with-link"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfTrainingSessionsWithLink}"/>
		</td>
	</tr>	

	<tr>
		<th scope="row">
			<acme:message code="developer.developer-dashboard.form.label.avg-time-training-module"/>
		</th>
		<td>
			<acme:print value="${avgTimeOfTrainingModule}"/>
		</td>
	</tr>	

	<tr>
		<th scope="row">
			<acme:message code="developer.developer-dashboard.form.label.min-time-training-module"/>
		</th>
		<td>
			<acme:print value="${minTimeOfTrainingModule}"/>
		</td>
	</tr>	

	<tr>
		<th scope="row">
			<acme:message code="developer.developer-dashboard.form.label.max-time-training-module"/>
		</th>
		<td>
			<acme:print value="${maxTimeOfTrainingModule}"/>
		</td>
	</tr>	
	
	<tr>
		<th scope="row">
			<acme:message code="developer.developer-dashboard.form.label.deviation-time-training-module"/>
		</th>
		<td>
			<acme:print value="${deviationTimeOfTrainingModule}"/>
		</td>
	</tr>
</table>


<acme:return/>