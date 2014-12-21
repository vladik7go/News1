package com.epam.news.database.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.epam.news.entity.News;
import com.epam.news.exception.DaoException;
import com.epam.news.util.EntityManagerFactoryWrapper;

/**
 * This class performs JPA persistence.
 * 
 * @author Ivan_Filimonau
 *
 */
public class NewsDAOJpa implements INewsDao {
	private static final Logger logger = Logger.getLogger(NewsDAOJpa.class);
	private EntityManagerFactoryWrapper entityManagerWrapper;

	public EntityManagerFactoryWrapper getEntityManagerWrapper() {
		return entityManagerWrapper;
	}

	public void setEntityManagerWrapper(
			EntityManagerFactoryWrapper entityManagerWrapper) {
		this.entityManagerWrapper = entityManagerWrapper;
	}

	
	@Override
	public List<News> getAll() throws DaoException {
		List<News> list = new ArrayList<News>();
		EntityManager em = entityManagerWrapper.getEntityManager();
		list = (List<News>) em.createNamedQuery("newsFindAll").getResultList();
		em.close();
		return list;
	}

	@Override
	public News getById(int id) throws DaoException {
		EntityManager em = entityManagerWrapper.getEntityManager();
		News news = em.find(News.class, id);
		em.close();
		return news;
	}

	@Override
	public int addNews(News news) throws DaoException {
		EntityManager em = entityManagerWrapper.getEntityManager();
		em.persist(news);
		Integer id;

		id = news.getId();

		em.close();

		return ++id;
	}

	@Override
	public int updateNews(News news) throws DaoException {
		EntityManager em = entityManagerWrapper.getEntityManager();

		em.merge(news);

		em.close();

		return 1;
	}

	@Override
	public int deleteManyNews(Integer[] ids) throws DaoException {
		List<Integer> idsList = new ArrayList<Integer>();
		idsList.addAll(Arrays.asList(ids));// convert array to
											// arrayList

		EntityManager em = entityManagerWrapper.getEntityManager();

		Query query = em.createNamedQuery("deleteManyNewsQuery").setParameter(
				"deleteIds", idsList);
		int result = query.executeUpdate();

		em.close();

		return result;

	}

}
