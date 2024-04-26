/*
 * AdministratorDashboardRepository.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.manager.managerDashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select count(us) from UserStory us where us.priority = 0 AND us.manager.id = :managerId")
	int countMustUserStories(int managerId);

	@Query("select count(us) from UserStory us where us.priority = 1 AND us.manager.id = :managerId")
	int countShouldUserStories(int managerId);

	@Query("select count(us) from UserStory us where us.priority = 2 AND us.manager.id = :managerId")
	int countCouldUserStories(int managerId);

	@Query("select count(us) from UserStory us where us.priority = 3 AND us.manager.id = :managerId")
	int countWontUserStories(int managerId);

	/////

	@Query("select coalesce(avg(us.estimatedCost),0) from UserStory us where us.manager.id = :managerId")
	double avgCostOfUserStory(int managerId);

	@Query("select coalesce(min(us.estimatedCost),0) from UserStory us where us.manager.id = :managerId")
	int minCostOfUserStory(int managerId);

	@Query("select coalesce(max(us.estimatedCost),0) from UserStory us where us.manager.id = :managerId")
	int maxCostOfUserStory(int managerId);

	@Query("select coalesce(stddev(us.estimatedCost),0) from UserStory us where us.manager.id = :managerId")
	double deviationCostOfUserStory(int managerId);

	@Query("select coalesce(avg(p.cost.amount),0) from Project p where p.cost.currency = :currency and p.manager.id = :managerId")
	double avgCostOfProject(String currency, int managerId);

	@Query("select coalesce(min(p.cost.amount),0) from Project p where p.cost.currency = :currency and p.manager.id = :managerId")
	double minCostOfProject(String currency, int managerId);

	@Query("select coalesce(max(p.cost.amount),0) from Project p where p.cost.currency = :currency and p.manager.id = :managerId")
	double maxCostOfProject(String currency, int managerId);

	@Query("select coalesce(stddev(p.cost.amount),0) from Project p where p.cost.currency = :currency and p.manager.id = :managerId")
	double deviationCostOfProject(String currency, int managerId);

}
