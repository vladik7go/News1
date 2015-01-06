package com.epam.news.database.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
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
	private static final String QUERY_COUNT_NEWS = " SELECT COUNT(*) FROM news";
	private static final String QUERY_SELECT_ALL_NEWS = " SELECT n FROM News n ORDER BY n.date DESC, n.id ASC";
	@PersistenceContext
	private EntityManager em;

	/**
	 * @return the em
	 */
	public EntityManager getEm() {
		return em;
	}

	/**
	 * @param em
	 *            the em to set
	 */
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public int countRows() {

		Query q = em.createNativeQuery(QUERY_COUNT_NEWS);
		int counter = ((Number) q.getSingleResult()).intValue();
		return counter;
	}

	@Override
	public List<News> getAll() throws DaoException {
		List<News> list = new ArrayList<News>();
		list = (List<News>) em.createQuery(
				"FROM News n ORDER BY date DESC, id DESC").getResultList();
		return list;
	}

	@Override
	public List<News> getAll(int targetPage, int objectsOnPage)
			throws DaoException {
		List<News> list = new ArrayList<News>();

		Query query = em.createQuery(QUERY_SELECT_ALL_NEWS);
		query.setFirstResult((targetPage - 1) * objectsOnPage);
		query.setMaxResults(objectsOnPage);
		list = (List<News>) query.getResultList();

		return list;
	}

	@Override
	public News getById(int id) throws DaoException {

		News news = em.find(News.class, id);

		return news;
	}

	@Override
	public int addNews(News news) {

		// EntityTransaction transaction = em.getTransaction();
		// transaction.begin();
		em.persist(news);
		// transaction.commit();
		Integer id;
		id = news.getId();

		return id;
	}

	@Override
	public int updateNews(News news) {

		// EntityTransaction transaction = em.getTransaction();
		// transaction.begin();
		em.merge(news);
		// transaction.commit();

		return 1;
	}

	@Override
	public int deleteManyNews(Integer[] ids) {
		List<Integer> idsList = new ArrayList<Integer>();
		idsList.addAll(Arrays.asList(ids));

		// EntityTransaction transaction = em.getTransaction();
		// transaction.begin();
		Query query = em
				.createQuery("DELETE FROM News WHERE id IN(:deleteIds)")
				.setParameter("deleteIds", idsList);
		int result = query.executeUpdate();
		// transaction.commit();

		return result;

	}

}
