<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>	
	<acme:input-textbox code="developer.training-module.form.label.code" path="code"/>
	<acme:input-textarea code="developer.training-module.form.label.details" path="details"/>
	<acme:input-select code="developer.training-module.form.label.difficultyLevel" path="difficultyLevel" choices="${difficultyLevels}" />
	<acme:input-url code="developer.training-module.form.label.optionalLink" path="optionalLink"/>
	<acme:input-integer code="developer.training-module.form.label.totalTime" path="totalTime"/>
	<acme:input-select code="developer.training-module.form.label.project" path="project" choices="${projects}" />
	<acme:input-moment code="developer.training-module.form.label.creationMoment" path="creationMoment" readonly="true"/>
	<acme:input-moment code="developer.training-module.form.label.updateMoment" path="updateMoment" readonly="true"/>

	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="developer.training-module.form.button.create" action="/developer/training-module/create"/>
		</jstl:when>	
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="developer.training-session.list.title" action="/developer/training-session/list?trainingModuleId=${id}"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true }">
				<acme:button code="developer.training-session.list.title" action="/developer/training-session/list?trainingModuleId=${id}"/>
				<acme:submit code="developer.training-module.form.button.update" action="/developer/training-module/update"/>
				<acme:submit code="developer.training-module.form.button.delete" action="/developer/training-module/delete"/>
				<acme:submit code="developer.training-module.form.button.publish" action="/developer/training-module/publish"/>
		</jstl:when>
	</jstl:choose>	
										
</acme:form>