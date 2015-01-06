package com.epam.news.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.epam.news.database.dao.INewsDao;
import com.epam.news.entity.News;
import com.epam.news.exception.DaoException;
import com.epam.news.exception.TechnicalException;

/**
 * This class provides logic for NewsAction class.
 * 
 * @author Ivan_Filimonau
 *
 */
@Transactional
public class NewsService {
	private static final Logger log = Logger.getLogger(NewsService.class);
	private INewsDao newsDao;

	/**
	 * @return the newsDao
	 */
	public INewsDao getNewsDao() {
		return newsDao;
	}

	/**
	 * @param newsDao
	 *            the newsDao to set
	 */
	public void setNewsDao(INewsDao newsDao) {
		this.newsDao = newsDao;
	}

	/**
	 * This method could perform to actions: 1) Add news 2) Edit news That
	 * chosen action depends on received ID parameter(passed from JSP page)
	 * 
	 * @param id
	 * @param newsForm
	 * @return
	 * @throws DaoException
	 * @throws TechnicalException
	 */
	public boolean saveOrEditNewsSaveButton(News news) throws DaoException {
		boolean result = false;
		int id = news.getId();
		if (id == 0) {
			// add page save

			result = addNewsSaveButton(news);

		} else {
			// edit page save

			result = editNewsSaveButton(news);

		}

		return result;
	}

	
	public List<News> getAll(int targetPage, int objectsOnPage)
			throws DaoException {

		return newsDao.getAll(targetPage, objectsOnPage);
	}

	/**
	 * @return
	 * @throws DaoException
	 */
	public List<News> getAll() throws DaoException {

		return newsDao.getAll();
	}

	/**
	 * This method calculate number of rows in table.
	 * @return - number of rows in the table.
	 */
	public int getNumberOfRows() {
		return newsDao.countRows();
	}

	/**
	 * @param ids
	 * @return
	 * @throws DaoException
	 */
	public int deleteManyNews(Integer[] ids) throws DaoException {

		return newsDao.deleteManyNews(ids);
	}

	public News getById(int id) throws DaoException {

		return newsDao.getById(id);
	}

	/**
	 * This method performs adding News into DB, and put ID of new record into
	 * the actionForm.
	 * 
	 * @param form
	 *            - Struts actionForm
	 * @return
	 * @throws DaoException
	 * @throws TechnicalException
	 */
	private boolean addNewsSaveButton(News news) throws DaoException {

		int result = 0;
		result = newsDao.addNews(news);
		news.setId(result);
		if (result > 0) {
			log.info("Add news");
			return true;
		}
		return false;
	}

	/**
	 * This method just edit News record, due to received ID.
	 * 
	 * @param form
	 *            - Struts actionForm
	 * @param id
	 *            - ID of editing News.
	 * @return
	 * @throws DaoException
	 */
	private boolean editNewsSaveButton(News news) throws DaoException {

		int result = 0;
		result = newsDao.updateNews(news);
		if (result > 0) {
			log.info("Edit news with id = " + news.getId());
			return true;
		}
		return false;
	}

}
