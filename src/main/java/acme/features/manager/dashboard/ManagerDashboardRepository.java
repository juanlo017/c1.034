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

package acme.features.manager.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select count(us) from UserStory us where lower(us.priority) = lower('MUST') AND us.manager.id = :managerId")
	int countMustUserStories(int managerId);

	@Query("select count(us) from UserStory us where lower(us.priority) = lower('SHOULD') AND us.manager.id = :managerId")
	int countShouldUserStories(int managerId);

	@Query("select count(us) from UserStory us where lower(us.priority) = lower('COULD') AND us.manager.id = :managerId")
	int countCouldUserStories(int managerId);

	@Query("select count(us) from UserStory us where lower(us.priority) = lower('WONT') AND us.manager.id = :managerId")
	int countWontUserStories(int managerId);

	@Query("select avg(us.estimatedCost) from UserStory us where us.manager.id = :managerId")
	double avgCostOfUserStory(int managerId);

	@Query("select min(us.estimatedCost) from UserStory us where us.manager.id = :managerId")
	int minCostOfUserStory(int managerId);

	@Query("select max(us.estimatedCost) from UserStory us where us.manager.id = :managerId")
	int maxCostOfUserStory(int managerId);

	@Query("select stddev(us.estimatedCost) from UserStory us where us.manager.id = :managerId")
	double deviationCostOfUserStory(int managerId);

	@Query("select avg(p.cost) from Project p where p.manager.id = :managerId")
	double avgCostOfProject(int managerId);

	@Query("select min(p.cost) from Project p where p.manager.id = :managerId")
	int minCostOfProject(int managerId);

	@Query("select max(p.cost) from Project p where p.manager.id = :managerId")
	int maxCostOfProject(int managerId);

	@Query("select stddev(p.cost) from Project p where p.manager.id = :managerId")
	double deviationCostOfProject(int managerId);

}
