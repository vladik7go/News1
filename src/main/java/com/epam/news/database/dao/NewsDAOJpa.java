package com.epam.news.database.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.epam.news.entity.News;
import com.epam.news.exception.DaoException;

/**
 * This class performs JPA persistence.
 * 
 * @author Ivan_Filimonau
 *
 */
public class NewsDAOJpa implements INewsDao {
	private static final Logger logger = Logger.getLogger(NewsDAOJpa.class);

	private EntityManagerFactory entityManagerFactory;

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void setEntityManagerFactory(
			EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public List<News> getAll() throws DaoException {
		List<News> list = new ArrayList<News>();
		entityManagerFactory.createEntityManager();
		EntityManager em = entityManagerFactory.createEntityManager();
		list = (List<News>) em.createQuery(
				"FROM News n ORDER BY date DESC, id DESC").getResultList();
		em.close();
		return list;
	}

	@Override
	public News getById(int id) throws DaoException {
		EntityManager em = entityManagerFactory.createEntityManager();
		News news = em.find(News.class, id);
		em.close();
		return news;
	}

	@Override
	public int addNews(News news) {
		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		em.persist(news);
		transaction.commit();
		Integer id;
		id = news.getId();
		em.close();
		return id;
	}

	@Override
	public int updateNews(News news) {
		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		em.merge(news);
		transaction.commit();
		em.close();
		return 1;
	}

	@Override
	public int deleteManyNews(Integer[] ids) {
		List<Integer> idsList = new ArrayList<Integer>();
		idsList.addAll(Arrays.asList(ids));
		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		Query query = em
				.createQuery("DELETE FROM News WHERE id IN(:deleteIds)")
				.setParameter("deleteIds", idsList);
		int result = query.executeUpdate();
		transaction.commit();
		em.close();
		return result;

	}

}
