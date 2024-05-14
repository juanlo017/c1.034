<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.training-module.list.label.code" path="code"/>
	<acme:list-column code="any.training-module.list.label.totalTime" path="totalTime"/>
	<acme:list-column code="any.training-module.list.label.project" path="project"/>
</acme:list>