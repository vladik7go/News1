package com.epam.news.database.dao;

import java.util.List;

import com.epam.news.entity.News;
import com.epam.news.exception.DaoException;
import com.epam.news.exception.TechnicalException;

/**
 * This interface describes all methods for news table
 * 
 * @author Ivan_Filimonau
 *
 */
public interface INewsDao {

	/**
	 * Get all news from database
	 * 
	 * @return - List of News objects
	 * @throws DaoException
	 * @throws TechnicalException
	 */
	public List<News> getAll() throws DaoException;

	/**
	 * Get one single news from database
	 * 
	 * @param id
	 *            - the id of selected news
	 * @return - one News object
	 * @throws TechnicalException
	 * @throws DaoException
	 */
	public News getById(int id) throws DaoException;

	/**
	 * Add one single news to database
	 * 
	 * @param news
	 *            - object News to add
	 * @return - After successfully added news, this method returns ID of the
	 *         newborn dataRecord(written news).
	 * @throws TechnicalException
	 * @throws DaoException
	 */
	public int addNews(News news) throws  DaoException;

	/**
	 * Update (edit) one single news.
	 * 
	 * @param news
	 *            - object News to update.
	 * @return - number of affected rows. Should be equal 1(success).
	 * @throws DaoException
	 * @throws TechnicalException
	 */
	public int updateNews(News news) throws DaoException;

	/**
	 * Delete list(or single) of news, marked in form.
	 * 
	 * @param ids
	 *            - array(Integer type) of ids.
	 * @return - number of affected rows.
	 * @throws TechnicalException
	 * @throws DaoException
	 */
	public int deleteManyNews(Integer[] ids) throws DaoException;

}
