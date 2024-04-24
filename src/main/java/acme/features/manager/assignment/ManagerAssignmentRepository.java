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

package acme.features.manager.assignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.projects.Assignment;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;

@Repository
public interface ManagerAssignmentRepository extends AbstractRepository {

	@Query("select a from Assignment a where a.id =:id")
	Assignment findOneAssignmentById(int id);

	@Query("select p from Project p where p.id = :projectId")
	Project findProjectById(int projectId);

	@Query("select us from UserStory us where us.id = :userStoryId")
	UserStory findUserStoryById(int userStoryId);

	@Query("select p from Project p where p.draftMode = true and p.manager.id = :managerId")
	Collection<Project> findAllNotPublishedProjects(int managerId);

	@Query("select us from UserStory us where us.draftMode = true and us.manager.id = :managerId")
	Collection<UserStory> findAllNotPublishedUserStories(int managerId);

	@Query("select a from Assignment a where a.project.manager.id = :id")
	Collection<Assignment> findManyAssignmentsByManagerId(int id);

}
