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
			<acme:message code="manager.dashboard.form.label.mustUserStories"/>
		</th>
		<td>
			<acme:print value="${mustUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.shouldUserStories"/>
		</th>
		<td>
			<acme:print value="${shouldUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.couldUserStories"/>
		</th>
		<td>
			<acme:print value="${couldUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.wontUserStories"/>
		</th>
		<td>
			<acme:print value="${wontUserStories}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.avgCostOfUserStory"/>
		</th>
		<td>
			<acme:print value="${avgCostOfUserStory}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.minCostOfUserStory"/>
		</th>
		<td>
			<acme:print value="${minCostOfUserStory}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.maxCostOfUserStory"/>
		</th>
		<td>
			<acme:print value="${maxCostOfUserStory}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.devCostOfUserStory"/>
		</th>
		<td>
			<acme:print value="${deviationCostOfUserStory}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.avgCostOfProject"/>
		</th>
		<td>
			<acme:print value="${avgCostOfProject}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.minCostOfProject"/>
		</th>
		<td>
			<acme:print value="${minCostOfProject}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.maxCostOfProject"/>
		</th>
		<td>
			<acme:print value="${maxCostOfProject}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:message code="manager.dashboard.form.label.devCostOfProject"/>
		</th>
		<td>
			<acme:print value="${deviationCostOfProject}"/>
		</td>
	</tr>		
</table>

<acme:return/>

