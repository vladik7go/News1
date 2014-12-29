package com.epam.news.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.epam.news.entity.News;
import com.epam.news.exception.DaoException;

/**
 * This class uses JDBC-template methods for managing DB.
 * 
 * @author Ivan_Filimonau
 *
 */
@SuppressWarnings("deprecation")
public class JdbcNewsDao extends SimpleJdbcDaoSupport implements INewsDao {
	public static Logger log = Logger.getLogger(JdbcNewsDao.class);
	private static final String GET_ALL_QUERY = "SELECT * FROM news ORDER BY news_date desc,id desc";
	private static final String GET_NUMBER_OF_ROWS_QUERY = " SELECT COUNT(*) FROM news";
	private static final String GET_BY_ID_QUERY = "SELECT * FROM news WHERE id=?";
	private static final String ADD_NEWS_QUERY = "INSERT INTO news(title,news_date,brief,content) VALUES (?,?,?,?)";
	private static final String GET_ALL_BETWEEN_QUERY = "select * from ( select a.*, ROWNUM rnum from ( select * from news order by news.NEWS_DATE desc, news.id asc ) a where rownum <= ? ) where rnum >= ? ";// First
	// BIG,
	// second
	// SMALL
	// private static final String ADD_NEWS_QUERY =
	// "INSERT INTO news(title,news_date,brief,content) VALUES (:title, :news_date, :brief, :content)";
	private static final String UPDATE_NEWS_QUERY = "UPDATE news SET title=?, news_date=?, brief=?, content=? WHERE id=?";
	private static final String DELETE_MANY_NEWS_QUERY = "DELETE FROM news WHERE id IN (";

	/**
	 * This anonymous class provides method mapRow, that returns filled with
	 * data News object.
	 */
	private RowMapper<News> rowMapper = new RowMapper<News>() {
		@Override
		public News mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			News news = new News();

			news.setId(resultSet.getInt("id"));
			news.setTitle(resultSet.getString("title"));
			news.setDate(resultSet.getDate("news_date"));
			news.setBrief(resultSet.getString("brief"));
			news.setContent(resultSet.getString("content"));

			return news;
		}
	};

	@Override
	public int countRows() {
		int numberOfRows = getSimpleJdbcTemplate().queryForInt(
				GET_NUMBER_OF_ROWS_QUERY);

		return numberOfRows;
	}

	@Override
	public List<News> getAll() throws DaoException {
		List<News> allNews = new ArrayList<News>();
		allNews = getSimpleJdbcTemplate().query(GET_ALL_QUERY,
				ParameterizedBeanPropertyRowMapper.newInstance(News.class));

		return allNews;
	}

	/**
	 * This method perform paging and ordering(by date, then - by ID).
	 * 
	 */
	@Override
	public List<News> getAll(int targetPage, int objectsOnPage)
			throws DaoException {
		log.info("Called getAll with params method (targetPage, objectsOnPage)"
						+ targetPage + " " + objectsOnPage);
		List<News> allNews = new ArrayList<News>();
		int begin = 1 + (targetPage * (int) objectsOnPage) - objectsOnPage;
		int end = targetPage * (int) objectsOnPage;
		allNews = getSimpleJdbcTemplate().query(GET_ALL_BETWEEN_QUERY,
				ParameterizedBeanPropertyRowMapper.newInstance(News.class),
				end, begin);

		return allNews;

	}

	@Override
	public News getById(int id) throws DaoException {

		News news = getSimpleJdbcTemplate().queryForObject(GET_BY_ID_QUERY,
				rowMapper, id);

		/*
		 * News news = getSimpleJdbcTemplate().queryForObject(GET_BY_ID_QUERY,
		 * ParameterizedBeanPropertyRowMapper.newInstance(News.class), id);
		 */

		return news;
	}

	@Override
	public int addNews(final News news) throws DaoException {

		KeyHolder keyHolder = new GeneratedKeyHolder();

		getSimpleJdbcTemplate().getJdbcOperations().update(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(
							Connection connection) throws SQLException {
						PreparedStatement preparedStatement = connection.prepareStatement(
								ADD_NEWS_QUERY, new String[] { "ID" });

						preparedStatement.setString(1, news.getTitle());
						preparedStatement.setDate(2, news.getDate());
						preparedStatement.setString(3, news.getBrief());
						preparedStatement.setString(4, news.getContent());
						return preparedStatement;
					}
				}, keyHolder);

		int result = keyHolder.getKey().intValue(); // now contains the
													// generated key
		return result;

	}

	@Override
	public int updateNews(News news) throws DaoException {

		int result = getSimpleJdbcTemplate().update(UPDATE_NEWS_QUERY,
				news.getTitle(), news.getDate(), news.getBrief(),
				news.getContent(), news.getId());

		return result;
	}

	@Override
	public int deleteManyNews(Integer[] ids) throws DaoException {
		String deleteManyNewsQuery = createDeleteManyNewsQuery(ids);
		int result = getSimpleJdbcTemplate().update(deleteManyNewsQuery);

		return result;
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

}
