<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="developer.training-module.list.label.code" path="code" width="10%"/>
	<acme:list-column code="developer.training-module.list.label.details" path="details" width="10%"/>	
	<acme:list-column code="developer.training-module.list.label.difficulty-level" path="difficultyLevel" width="10%"/>	
	<acme:list-column code="developer.training-module.list.label.total-time" path="totalTime" width="10%"/>

</acme:list>

<jstl:if test="${_command == 'list-mine'}">
	<acme:button code="developer.training-module.list.button.create-form" action="/developer/training-module/create"/>
</jstl:if>