/*
 * AuthenticatedAnnouncementRepository.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.manager.project;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contracts.Contract;
import acme.entities.projects.Assignment;
import acme.entities.projects.Project;
import acme.entities.projects.UserStory;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.trainings.TrainingModule;
import acme.roles.Manager;

@Repository
public interface ManagerProjectRepository extends AbstractRepository {

	@Query("select p from Project p where p.manager.id = :managerId")
	Collection<Project> findManyProjectsByManagerId(int managerId);

	@Query("select p from Project p")
	Collection<Project> findAllProjects();

	@Query("select p from Project p where p.id = :projectId")
	Project findOneProjectById(int projectId);

	@Query("select m from Manager m where m.id = :managerId")
	Manager findOneManagerById(int managerId);

	@Query("select p from Project p where p.code = :code")
	Project findOneProjectByCode(String code);

	@Query("select a from Assignment a where a.project.id = :id")
	Collection<Assignment> findManyAssignmentsByProjectId(int id);

	@Query("select a.userStory from Assignment a where a.project.id = :id")
	Collection<UserStory> findAllUserStoriesByProjectId(int id);

	@Query("select c from Contract c where c.project.id = :id")
	Collection<Contract> findManyContractsByProjectId(int id);

	@Query("select s from Sponsorship s where s.project.id = :id")
	Collection<Sponsorship> findManySponsorshipsByProjectId(int id);

	@Query("select tm from TrainingModule tm where tm.project.id = :id")
	Collection<TrainingModule> findManyTrainingModulesByProjectId(int id);

	//////////

	@Query("select count(a) from Assignment a where a.project.id = :id")
	int countAssignmentsByProjectId(int id);

	@Query("select count(a) from Contract a where a.project.id = :id")
	int countContractsByContractId(int id);

	@Query("select count(a) from Risk a where a.project.id = :id")
	int countRisksByProjectId(int id);

	@Query("select count(a) from Objective a where a.project.id = :id")
	int countObjectivesByProjectId(int id);

	@Query("select count(a) from Sponsorship a where a.project.id = :id")
	int countSponsorshipsByProjectId(int id);

	@Query("select count(a) from TrainingModule a where a.project.id = :id")
	int countTrainingModuleId(int id);

	@Query("select c.acceptedCurrencies from Configuration c")
	String findValidCurrencies(String code);

	@Query("select count(a) from Assignment a where a.project.id = :id")
	int findNumberAssignmentOfProject(int id);

	@Query("select count(a) from Assignment a where a.project.id = :id and a.userStory.draftMode = 1")
	int findNumberUserStoryNotPublishedOfProject(int id);

}
