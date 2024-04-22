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
	<acme:input-textbox code="auditor.auditRecord.form.label.code" path="code"/>
	<acme:input-moment code="auditor.auditRecord.form.label.auditPeriod" path="auditPeriod"/>
	<acme:input-textbox code="auditor.auditRecord.form.label.mark" path="mark"/>
	<acme:input-url code="auditor.auditRecord.form.label.link" path="link"/>
	<acme:input-textbox code="auditor.auditRecord.form.label.draftMode" path="draftMode"/>

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="auditor.auditRecord.form.button.update" action="/auditor/auditRecord/update"/>
			<acme:submit code="auditor.auditRecord.form.button.delete" action="/auditor/auditRecord/delete"/>
			<acme:submit code="auditor.auditRecord.form.button.publish" action="/auditor/auditRecord/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.auditRecord.form.button.create" action="/auditor/auditRecord/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>