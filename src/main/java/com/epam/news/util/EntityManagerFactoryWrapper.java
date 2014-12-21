package com.epam.news.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * This class provises JPA entity manager factory
 * 
 * @author Ivan_Filimonau
 *
 */
public class EntityManagerFactoryWrapper {
	private static final String UNIT_NAME = "NewsManagement";
	private EntityManagerFactory emf;

	public void init() {
		emf = Persistence.createEntityManagerFactory(UNIT_NAME);
	}

	public EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

}
