<%--
- menu.jsp
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
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar code="master.menu.home">
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.juan-lopez" action="https://chat.openai.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.jose-castro" action="https://www.realbetisbalompie.es/"/>
			<acme:menu-suboption code="master.menu.anonymous.juan-del-junco" action="https://www.hermandaddelosgitanos.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.pablo-mejias" action="https://www.centropkmn.com/pokemon-bw/?"/>
			<acme:menu-suboption code="master.menu.anonymous.david-blanco" action="https://www.fcbarcelona.es/es/"/>
			<acme:menu-separator/>
      <acme:menu-suboption code="master.menu.anonymous.published-sponsorships" action="/any/sponsorship/list"/>
			<acme:menu-separator/>	
			<acme:menu-suboption code="master.menu.anonymous.all-projects" action="/any/project/list-all"/>	
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.anonymous.published-modules" action="/any/training-module/list"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.authenticated" access="isAuthenticated()">
		    <acme:menu-suboption code="master.menu.anonymous.all-projects" action="/any/project/list-all"/>
		    <acme:menu-separator/>	
		    <acme:menu-suboption code= "master.menu.authenticated.published-modules" action="/any/training-module/list"/>
        <acme:menu-separator/>
      	<acme:menu-suboption code="master.menu.authenticated.published-sponsorships" action="/any/sponsorship/list"/>
		</acme:menu-option>
	

		<acme:menu-option code="master.menu.administrator" access="hasRole('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-sample" action="/administrator/system/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-down" action="/administrator/system/shut-down"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.developer" access="hasRole('Developer')">
			<acme:menu-suboption code="master.menu.developer.list.mine.training-modules" action="/developer/training-module/list-mine"/>
			<acme:menu-separator/>	
			<acme:menu-suboption code="master.menu.developer.developer-dashboard" action="/developer/developer-dashboard/show"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.manager" access="hasRole('Manager')">
			<acme:menu-suboption code="master.menu.manager.list.mine.projects" action="/manager/project/list-mine"/>
			<acme:menu-suboption code="master.menu.manager.list.assignment" action="/manager/assignment/list-mine"/>
			<acme:menu-separator/>	
			<acme:menu-suboption code="master.menu.manager.list.dashboard" action="/manager/manager-dashboard/show"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.sponsor" access="hasRole('Sponsor')">
			<acme:menu-suboption code="master.menu.sponsor.list.all.sponsorships" action="/sponsor/sponsorship/list-all"/>
			<acme:menu-suboption code="master.menu.sponsor.list.mine.sponsorships" action="/sponsor/sponsorship/list-mine"/>
			<acme:menu-suboption code="master.menu.sponsor.dashboard" action="/sponsor/sponsor-dashboard/show"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.auditor" access="hasRole('Auditor')">	
			<acme:menu-suboption code="master.menu.auditor.my-code-audit" action="/auditor/code-audit/list-mine"/>
			<acme:menu-suboption code="master.menu.auditor.all-code-audit" action="/auditor/code-audit/list-all"/>
			<acme:menu-separator/>	
			<acme:menu-suboption code="master.menu.auditor.my-dashboard" action="/auditor/auditor-dashboard/show"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.client" access="hasRole('Client')">	
			<acme:menu-suboption code="master.menu.client.my-contracts" action="/client/contract/list-mine"/>
			<acme:menu-suboption code="master.menu.client.my-progress-logs" action="/client/progress-log/list-mine"/>
			<acme:menu-separator/>	
			<acme:menu-suboption code="master.menu.client.my-dashboard" action="/client/client-dashboard/show"/>
		</acme:menu-option>
		

	</acme:menu-left>

	<acme:menu-right>
		<acme:menu-option code="master.menu.sign-up" action="/anonymous/user-account/create" access="isAnonymous()"/>
		<acme:menu-option code="master.menu.sign-in" action="/anonymous/system/sign-in" access="isAnonymous()"/>

		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-data" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.manager" action="/authenticated/manager/update" access="hasRole('Manager')"/>
			<acme:menu-suboption code="master.menu.user-account.developer" action="/authenticated/developer/update" access="hasRole('Developer')"/>
			<acme:menu-suboption code="master.menu.user-account.sponsor" action="/authenticated/sponsor/update" access="hasRole('Sponsor')"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.user-account.become-manager" action="/authenticated/manager/create" access="!hasRole('Manager')"/>
			<acme:menu-suboption code="master.menu.user-account.become-developer" action="/authenticated/developer/create" access="!hasRole('Developer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-sponsor" action="/authenticated/sponsor/create" access="!hasRole('Sponsor')"/>

		</acme:menu-option>

		<acme:menu-option code="master.menu.sign-out" action="/authenticated/system/sign-out" access="isAuthenticated()"/>
	</acme:menu-right>
</acme:menu-bar>

