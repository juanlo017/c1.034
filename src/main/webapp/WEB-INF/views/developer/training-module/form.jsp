<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="developer.training-module.form.label.code" path="code"/>
	<acme:input-moment code="developer.training-module.form.label.creation-moment" path="creation-moment"/>
	<acme:input-textbox code="developer.training-module.form.label.details" path="details"/>
	<acme:input-select code="developer.training-module.form.label.difficulty-level" path="difficulty-level" choices="${levels}"/>	
	<acme:input-moment code="developer.training-module.form.label.update-moment" path="update-moment"/>
	<acme:input-url code="developer.training-module.form.label.optional-link" path="optional-link"/>
	<acme:input-integer code="developer.training-module.form.label.total-time" path="total-time"/>
	<acme:input-textbox code="developer.training-module.form.label.draftMode" path="draftMode"/>

	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="developer.training-module.form.button.sessions" action="/developer/training-session/list?masterId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:button code="developer.training-module.form.button.sessions" action="/developer/training-session/list?masterId=${id}"/>
			<acme:submit code="developer.training-module.form.button.update" action="/developer/training-module/update"/>
			<acme:submit code="developer.training-module.form.button.delete" action="/developer/training-module/delete"/>
			<acme:submit code="developer.training-module.form.button.publish" action="/developer/training-module/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="developer.training-module.form.button.create" action="/developer/tranining-module/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>