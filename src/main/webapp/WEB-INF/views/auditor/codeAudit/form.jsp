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
	<acme:input-textbox code="auditor.codeAudit.form.label.code" path="code"/>
	<acme:input-moment code="auditor.codeAudit.form.label.executionDate" path="executionDate"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.type" path="type"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.actions" path="actions"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.mark" path="mark"/>
	<acme:input-url code="auditor.codeAudit.form.label.link" path="link"/>
	<acme:input-textbox code="auditor.codeAudit.form.label.draftMode" path="draftMode"/>

	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="auditor.auditRecord.form.button.list" action="/auditor/auditRecord/list?masterId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:button code="auditor.auditRecord.form.button.list" action="/auditor/auditRecord/list?masterId=${id}"/>
			<acme:submit code="auditor.codeAudit.form.button.update" action="/auditor/codeAudit/update"/>
			<acme:submit code="auditor.codeAudit.form.button.delete" action="/auditor/codeAudit/delete"/>
			<acme:submit code="auditor.codeAudit.form.button.publish" action="/auditor/codeAudit/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.codeAudit.form.button.create" action="/auditor/codeAudit/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>