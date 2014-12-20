package com.epam.news.database.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import com.epam.news.entity.News;
import com.epam.news.exception.DaoException;
import com.epam.news.util.HibernateUtil;

/**
 * This class implements DAO used with Hibernate
 * 
 * @author Ivan_Filimonau
 *
 */
public class NewsDAOHibernate implements INewsDao {
	private static final Logger logger = Logger
			.getLogger(NewsDAOHibernate.class);
	private static HibernateUtil hibernateUtil;
	private static final String NEWS_DATE_COLUMN = "date";
	private static final SessionFactory sessions = HibernateUtil
			.getSessionFactory();

	public static HibernateUtil getHibernateUtil() {
		return hibernateUtil;
	}

	public static void setHibernateUtil(HibernateUtil hibernateUtil) {
		NewsDAOHibernate.hibernateUtil = hibernateUtil;
	}

	@Override
	public List<News> getAll() throws DaoException {
		Session session = sessions.getCurrentSession();
		List<News> list = new ArrayList<News>();
		Transaction transaction = session.beginTransaction();
		Criteria criteria = session.createCriteria(News.class);
		criteria = criteria.addOrder(Order.desc(NEWS_DATE_COLUMN));
		list = (List<News>) criteria.list();
		transaction.commit();
		return list;
	}

	@Override
	public News getById(int id) throws DaoException {
		Session session = sessions.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		News news = new News();
		news = (News) session.get(News.class, id);
		transaction.commit();
		return news;
	}

	@Override
	public int addNews(News news) throws DaoException {
		Session session = sessions.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		session.save(news);

		transaction.commit();

		int id = news.getId();
		return id;
	}

	@Override
	public int updateNews(News news) throws DaoException {
		Session session = sessions.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		news = (News) session.merge(news);
		session.update(news);

		transaction.commit();

		return 1;
	}

	@Override
	public int deleteManyNews(Integer[] ids) throws DaoException {
		Session session = sessions.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.getNamedQuery("deleteManyNewsQuery")
				.setParameterList("deleteIds", ids);
		int result = query.executeUpdate();

		transaction.commit();

		return result;
	}

}
