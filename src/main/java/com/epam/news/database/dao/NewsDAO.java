package com.epam.news.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.epam.news.database.pool.ConnectionPool;
import com.epam.news.entity.News;
import com.epam.news.exception.DaoException;
import com.epam.news.exception.TechnicalException;

/**
 * This class implements all DAO methods, described in INewsDao interface.
 * 
 * @author Ivan_Filimonau
 *
 */
public class NewsDAO implements INewsDao {

	public static Logger log = Logger.getLogger(NewsDAO.class);

	private static final String GET_ALL_QUERY = "SELECT * FROM news ORDER BY news_date desc,id desc";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM news WHERE id=?";
	private static final String ADD_NEWS_QUERY = "INSERT INTO news(title,news_date,brief,content) VALUES (?,?,?,?)";
	private static final String UPDATE_NEWS_QUERY = "UPDATE news SET title=?, news_date=?, brief=?, content=? WHERE id=?";
	private static final String DELETE_MANY_NEWS_QUERY = "DELETE FROM news WHERE id IN (";

	/**
	 * Variable - type of ConnectionPool. Object will be instantiated by Spring
	 * DI.
	 */
	ConnectionPool connectionPool;

	/**
	 * Public constructor
	 */
	public NewsDAO() {
	}

	/**
	 * @return - the connectionPool
	 */
	public ConnectionPool getConnectionPool() {
		return connectionPool;
	}

	/**
	 * @param connectionPool
	 *            the connectionPool to set
	 */
	public void setConnectionPool(ConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
	}

	@Override
	public int countRows() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.epam.news.database.dao.INewsDao#getAll()
	 */
	@Override
	public List<News> getAll() throws DaoException {
		List<News> allNews = new ArrayList<News>();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			log.debug("Method getAll started");
			connection = connectionPool.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(GET_ALL_QUERY);
			while (resultSet.next()) {
				News news = setParameters(resultSet);
				allNews.add(news);
			}

		} catch (TechnicalException e) {
			throw new DaoException("Technical exception", e);
			// log.error(e.getMessage(), e);
		} catch (SQLException e1) {
			throw new DaoException("SQL exception", e1);
		}

		finally {
			releaseResources(statement, null, resultSet);
			connectionPool.returnConnection(connection);

		}

		return allNews;
	}

	@Override
	public News getById(int id) throws DaoException {
		News news = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = connectionPool.getConnection();
			preparedStatement = connection.prepareStatement(GET_BY_ID_QUERY);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				news = setParameters(resultSet);
			}
		} catch (TechnicalException e) {
			throw new DaoException("Technical exception", e);
			// log.error(e.getMessage(), e);
		} catch (SQLException e1) {
			throw new DaoException("SQL exception", e1);
		}

		finally {
			releaseResources(null, preparedStatement, resultSet);
			connectionPool.returnConnection(connection);

		}

		return news;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.epam.news.database.dao.INewsDao#addNews(com.epam.news.entity.News)
	 */
	@Override
	public int addNews(News news) throws DaoException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet set = null;
		int result = 0;

		try {
			connection = connectionPool.getConnection();

			preparedStatement = connection.prepareStatement(ADD_NEWS_QUERY,
					new String[] { "ID" });// Creates a default
											// PreparedStatement object capable
											// of returning the auto-generated
											// keys designated by the given
											// array.
			preparedStatement.setString(1, news.getTitle());
			preparedStatement.setDate(2, news.getDate());
			preparedStatement.setString(3, news.getBrief());
			preparedStatement.setString(4, news.getContent());
			result = preparedStatement.executeUpdate();
			if (result > 0) {
				set = preparedStatement.getGeneratedKeys();// get the
															// auto-generated
															// keys of all rows
															// created by that
															// execution.
				if (set.next()) {
					return set.getInt(1);
				} else {
					log.info("Id is not generated");
				}
			} else {
				log.info("No news added");
			}
		} catch (TechnicalException e) {
			throw new DaoException("Technical exception", e);
			// log.error(e.getMessage(), e);
		} catch (SQLException e1) {
			throw new DaoException("SQL exception", e1);
		}

		finally {
			releaseResources(null, preparedStatement, set);
			connectionPool.returnConnection(connection);
		}

		return result;
	}

	@Override
	public int updateNews(News news) throws DaoException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int result = 0;

		try {
			connection = connectionPool.getConnection();
			preparedStatement = connection.prepareStatement(UPDATE_NEWS_QUERY);
			preparedStatement.setString(1, news.getTitle());
			preparedStatement.setDate(2, news.getDate());
			preparedStatement.setString(3, news.getBrief());
			preparedStatement.setString(4, news.getContent());
			preparedStatement.setInt(5, news.getId());
			result = preparedStatement.executeUpdate();
		} catch (TechnicalException e) {
			throw new DaoException("Technical exception", e);
			// log.error(e.getMessage(), e);
		} catch (SQLException e1) {
			throw new DaoException("SQL exception", e1);
		}

		finally {
			releaseResources(null, preparedStatement, null);
			connectionPool.returnConnection(connection);
		}

		return result;
	}

	@Override
	public int deleteManyNews(Integer[] ids) throws DaoException {
		Connection connection = null;
		Statement statement = null;
		int result = 0;

		try {
			connection = connectionPool.getConnection();
			statement = connection.createStatement();
			String deleteManyNewsQuery = createDeleteManyNewsQuery(ids);
			result = statement.executeUpdate(deleteManyNewsQuery);

		} catch (TechnicalException e) {
			log.error(e.getMessage(), e);
			throw new DaoException("Technical exception", e);
		} catch (SQLException e1) {
			log.error(e1.getMessage(), e1);
			throw new DaoException("SQL exception", e1);
		}

		finally {
			releaseResources(statement, null, null);
			connectionPool.returnConnection(connection);
		}

		return result;
	}

	/**
	 * This method creates new News object, and populate it with data from
	 * resultSet.
	 * 
	 * @param resultSet
	 *            - data, received from database, due to SQL_request.
	 * @return - an object(entity) already populated with data.
	 */
	private News setParameters(ResultSet resultSet) {
		News news = new News();

		try {
			news.setId(resultSet.getInt("id"));
			news.setTitle(resultSet.getString("title"));
			news.setDate(resultSet.getDate("news_date"));
			news.setBrief(resultSet.getString("brief"));
			news.setContent(resultSet.getString("content"));
		} catch (SQLException e) {
			log.error("error, while populating News object in DAO", e);
		}

		return news;
	}

	/**
	 * This method close Statement, PreparedStatement, ResultSet.
	 * 
	 * @param statement
	 *            - statement to close (null, if not used.)
	 * @param preparedStatement
	 *            - prepared statement to close (null, if not used.)
	 * @param resultSet
	 *            - result set to close (null, if not used.)
	 */
	private void releaseResources(Statement statement,
			PreparedStatement preparedStatement, ResultSet resultSet) {

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
		}

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
		}

		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Create string(SQL query) for deleting list of news by one query
	 * 
	 * @param ids
	 *            - IDs of news, that should be deleted.
	 * 
	 * @return - String (SQL query for deleting list of news by one query)
	 */
	private String createDeleteManyNewsQuery(Integer[] ids) {
		StringBuffer query = new StringBuffer(DELETE_MANY_NEWS_QUERY);
		Integer lastId = ids[ids.length - 1];
		for (Integer id : ids) {
			query.append(id);
			if (lastId.equals(id)) {
				query.append(")");
			} else {
				query.append(",");
			}
		}
		return query.toString();
	}

	/**
	 * 
	 * This method is not working with paging for that very DAO !!! Just mock.
	 * 
	 */
	@Override
	public List<News> getAll(int targetPage, int objectsOnPage)
			throws DaoException {

		return getAll();
	}

}
