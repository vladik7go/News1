package com.epam.news.action;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.MappingDispatchAction;

import com.epam.news.entity.News;
import com.epam.news.exception.DaoException;
import com.epam.news.form.NewsForm;
import com.epam.news.service.NewsService;

/**
 * This class provides all actions of application.
 * 
 * @author Ivan_Filimonau
 *
 */
public final class NewsAction extends MappingDispatchAction {
	private static final Logger log = Logger.getLogger(NewsAction.class);
	private static final String MAIN_PAGE = "mainPage";
	private static final String NEWS_LIST_PAGE = "newsList";
	private static final String VIEW_NEWS_PAGE = "viewNews";
	private static final String VIEW_NEWS_PAGE_ACTION = "viewNewsAction";
	private static final String ADD_NEWS_PAGE = "addNews";
	private static final String EDIT_NEWS_PAGE = "editNews";
	private static final String ERROR_PAGE = "error";
	private static final String BACK_PAGE = "back";
	private static final String PREVIOUS_PAGE = "previousPage";

	/**
	 * The Referer request-header field allows the client to specify, for the
	 * server's benefit, the address (URI) of the resource from which the
	 * Request-URI was obtained
	 */
	private static final String REFERER = "referer";
	private NewsService newsService;

	/**
	 * @return the newsService
	 */
	public NewsService getNewsService() {
		return newsService;
	}

	/**
	 * @param newsService
	 *            the newsService to set
	 */
	public void setNewsService(NewsService newsService) {
		this.newsService = newsService;
	}

	/**
	 * Action named /NewsList. Shows list of all news.
	 * 
	 * @param mapping
	 *            ActionMapping of this action
	 * @param form
	 *            form of this action
	 * @param request
	 *            current request
	 * @param response
	 *            current response
	 * @return forward to page
	 * @throws Exception
	 *             if something is wrong
	 */
	public ActionForward viewNewsList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		saveToken(request);// saveToken() method is used for duplicate form
							// submission.A call to saveToken() only puts the
							// token in the session.

		String target = ERROR_PAGE;
		request.getSession().setAttribute(PREVIOUS_PAGE, NEWS_LIST_PAGE);
		NewsForm newsForm = (NewsForm) form;

		List<News> newsList = null;
		try {
			newsList = newsService.getAll();
		} catch (DaoException e) {
			log.error(e.getMessage(), e);
		}
		if (newsList != null) {
			target = NEWS_LIST_PAGE;
			newsForm.setNewsList(newsList);
		}
		ActionRedirect redirect = new ActionRedirect(
				mapping.findForward(target));
		return redirect;
	}

	/**
	 * Action named /AddNews. Forwards to Add News page
	 * 
	 * @param mapping
	 *            ActionMapping of this action
	 * @param form
	 *            form of this action
	 * @param request
	 *            current request
	 * @param response
	 *            current response
	 * @return forward to page
	 * @throws Exception
	 *             if something is wrong
	 */
	public ActionForward addNews(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		saveToken(request);
		String target = ADD_NEWS_PAGE;
		NewsForm newsForm = (NewsForm) form;
		News news = new News();
		Calendar calendar = Calendar.getInstance();
		Date today = new Date(calendar.getTimeInMillis());
		news.setDate(today);
		HttpSession session = request.getSession();
		newsForm.setNews(news,
				(Locale) session.getAttribute(Globals.LOCALE_KEY));
		ActionRedirect redirect = new ActionRedirect(
				mapping.findForward(target));
		return redirect;
	}

	/**
	 * Action named /EditNews. Forwards to Edit News page.
	 * 
	 * @param mapping
	 *            ActionMapping of this action
	 * @param form
	 *            form of this action
	 * @param request
	 *            current request
	 * @param response
	 *            current response
	 * @return forward to page
	 * @throws Exception
	 *             if something is wrong
	 */
	public ActionForward editNews(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String target = ERROR_PAGE;
		saveToken(request);
		if (setNewsDetails(request, form)) {
			target = EDIT_NEWS_PAGE;
		}
		ActionRedirect redirect = new ActionRedirect(
				mapping.findForward(target));
		return redirect;
	}

	/**
	 * Action named /ViewNews. Forwards to View News page.
	 * 
	 * @param mapping
	 *            ActionMapping of this action
	 * @param form
	 *            form of this action
	 * @param request
	 *            current request
	 * @param response
	 *            current response
	 * @return forward to page
	 * @throws Exception
	 *             if something is wrong
	 */
	public ActionForward viewNews(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		saveToken(request);
		String target = ERROR_PAGE;
		if (setNewsDetails(request, form)) {
			target = VIEW_NEWS_PAGE;
		}
		request.getSession().setAttribute(PREVIOUS_PAGE, VIEW_NEWS_PAGE);
		ActionRedirect redirect = new ActionRedirect(
				mapping.findForward(target));
		return redirect;
	}

	/**
	 * Action named /DeleteNews. Delete news from View News page or Edit News
	 * page.
	 * 
	 * @param mapping
	 *            ActionMapping of this action
	 * @param form
	 *            form of this action
	 * @param request
	 *            current request
	 * @param response
	 *            current response
	 * @return forward to page
	 * @throws Exception
	 *             if something is wrong
	 */
	public ActionForward deleteNews(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String target = ERROR_PAGE;
		if (isTokenValid(request, true)) {
			NewsForm newsForm = (NewsForm) form;
			if (newsForm != null) {
				int id = newsForm.getNews().getId();
				Integer[] ids = { id };
				int result = newsService.deleteManyNews(ids);
				if (result == 1) {
					target = MAIN_PAGE;
					log.info("News delete with id = " + id);
				}
			}
		} else {
			target = BACK_PAGE;
		}
		ActionRedirect redirect = new ActionRedirect(
				mapping.findForward(target));
		return redirect;
	}

	/**
	 * Action named /DeleteGroupOfNews. Delete one or more news from News List
	 * page.
	 * 
	 * @param mapping
	 *            ActionMapping of this action
	 * @param form
	 *            form of this action
	 * @param request
	 *            current request
	 * @param response
	 *            current response
	 * @return forward to page
	 * @throws Exception
	 *             if something is wrong
	 */
	public ActionForward deleteGroupOfNews(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String target = ERROR_PAGE;
		if (isTokenValid(request, true)) {// validate the submitted token
											// key(hidden field) with the token
											// key stored previously on
											// request/session. If match, it
											// will return true
			if (form != null) {
				NewsForm newsForm = (NewsForm) form;
				Integer[] selectedItems = newsForm.getSelectedItems();
				if (selectedItems.length > 0) {
					int result = newsService.deleteManyNews(selectedItems);
					if (result > 0) {
						target = MAIN_PAGE;
						log.info("News multiple delete");
					}
				}
			}
		} else {
			target = BACK_PAGE;
		}
		ActionRedirect redirect = new ActionRedirect(
				mapping.findForward(target));
		return redirect;
	}

	/**
	 * Action named /Save. Save current news from Add News page or Edit News
	 * page.
	 * 
	 * @param mapping
	 *            ActionMapping of this action
	 * @param form
	 *            form of this action
	 * @param request
	 *            current request
	 * @param response
	 *            current response
	 * @return forward to page
	 * @throws Exception
	 *             if something is wrong
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String target = ERROR_PAGE;
		NewsForm newsForm = (NewsForm) form;
		if (newsForm != null) {
			News news = newsForm.getNews();
			boolean result = false;
			if (isTokenValid(request, true)) {
				try {
					result = newsService.saveOrEditNewsSaveButton(news);
				} catch (DaoException e) {
					log.error(e.getMessage(), e);
				}
			} else {
				target = BACK_PAGE;
			}
			if (result) {
				target = VIEW_NEWS_PAGE_ACTION;
				// target = NEWS_LIST_PAGE;
			}
		}
		ActionRedirect redirect = new ActionRedirect(
				mapping.findForward(target));
		return redirect;

	}

	/**
	 * This method described in struts-config.xml as the "Action". This Action
	 * cancel current operation and go back to previous page. This action can be
	 * called from JSP pages, as a URL for java-script method:
	 * "location.replace(URL)".
	 * 
	 * @param mapping
	 *            ActionMapping of this action
	 * @param form
	 *            form of this action
	 * @param request
	 *            current request
	 * @param response
	 *            current response
	 * @return forward to page
	 * @throws Exception
	 *             if something is wrong
	 */
	public ActionForward cancel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String target = (String) request.getSession().getAttribute(
				PREVIOUS_PAGE);
		ActionRedirect redirect = new ActionRedirect(
				mapping.findForward(target));
		return redirect;
	}

	/**
	 * This method described in struts-config.xml as the "Global forward". This
	 * forward can be called from another methods of Action-class(NewsAction),
	 * in order to return to previous page.
	 * 
	 * @return redirect to previous page.
	 * @throws Exception
	 */
	public ActionForward back(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return cancel(mapping, form, request, response);
	}

	/**
	 * Action named /ChangeLocale. Changes current locale to another.
	 * 
	 * @param mapping
	 *            ActionMapping of this action
	 * @param form
	 *            form of this action
	 * @param request
	 *            current request
	 * @param response
	 *            current response
	 * @return forward to page
	 * @throws Exception
	 *             if something is wrong
	 */
	public ActionForward changeLocale(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String target = ERROR_PAGE;
		if (form != null) {
			NewsForm newsForm = (NewsForm) form;
			String lang = newsForm.getLang();
			Locale locale = new Locale(lang);

			setLocale(request, locale);// Set the user's currently selected
										// Locale into their HttpSession.

			/*
			 * The Referer request-header field allows the client to specify,
			 * for the server's benefit, the address (URI) of the resource from
			 * which the Request-URI was obtained
			 */
			target = request.getHeader(REFERER);
		}
		ActionRedirect redirect = new ActionRedirect(target);
		return redirect;
	}

	/**
	 * This method: 1) Get "id" parameter from newsForm("id" was put into
	 * newsForm(in JSP, using struts "html:link/>" tag ), when action
	 * "viewNews.do" or "editNews.do" was called). 2) Creates new object
	 * News(and fill it with data from DB, due to "id" parameter). 3) Put this
	 * News object into the newsForm.
	 * 
	 * @param request
	 * @param form
	 * @return true - when success. False - if newsForm == null.
	 */
	private boolean setNewsDetails(HttpServletRequest request, ActionForm form) {
		NewsForm newsForm = (NewsForm) form;
		if (newsForm != null) {
			int id = newsForm.getNews().getId();
			News news = null;
			try {
				news = newsService.getById(id);
			} catch (DaoException e) {
				log.error(e.getMessage(), e);
			}
			HttpSession session = request.getSession();
			newsForm.setNews(news,
					(Locale) session.getAttribute(Globals.LOCALE_KEY));
			return true;
		} else {
			return false;
		}
	}

}