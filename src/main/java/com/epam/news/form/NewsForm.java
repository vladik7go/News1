package com.epam.news.form;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.validator.ValidatorForm;
import org.springframework.cglib.core.Local;

import com.epam.news.entity.News;
import com.epam.news.util.DateConverter;




/**
 * This class provides news form
 * @author Ivan_Filimonau
 *
 */
public class NewsForm extends ValidatorForm {

    /**
     * Generated serial version UID
     */
    private static final long serialVersionUID = 1216355468781937508L;
    private static final Logger log = Logger.getLogger(NewsForm.class);
    private static final String RESOURCES = "ApplicationResource";
    private static final String datePattern = "date.pattern";
    private List<News> newsList = new ArrayList<News>();
    private News news = new News();
    private String lang;
    private Integer[] selectedItems;
    private String dateString;


    /**
     * @return the dateString
     */
    public String getDateString() {
	return dateString;
    }

    /**
     * @param news
     *            the news to set
     */
    public void setNews(News news, Locale locale) {
	if (news.getDate() != null) {
	    ResourceBundle bundle = ResourceBundle.getBundle(RESOURCES, locale);
	    String pattern = bundle.getString(datePattern);
	    DateFormat dateFormat = new SimpleDateFormat(pattern);
	    dateString = dateFormat.format(news.getDate());
	}
	this.news = news;
    }

   

    /**
     * @return the selectedItems
     */
    public Integer[] getSelectedItems() {
	return selectedItems;
    }

    /**
     * @param selectedItems
     *            the selectedItems to set
     */
    public void setSelectedItems(Integer[] selectedItems) {
	this.selectedItems = selectedItems;
    }

    /**
     * @return the newsList
     */
    public List<News> getNewsList() {
	return newsList;
    }

    /**
     * @param newsList
     *            the newsList to set
     */
    public void setNewsList(List<News> newsList) {
	this.newsList = newsList;
    }

    /**
     * @return the news
     */
    public News getNews() {
	return news;
    }

    /**
     * @return the lang
     */
    public String getLang() {
	return lang;
    }

    /**
     * @param lang
     *            the lang to set
     */
    public void setLang(String lang) {
	this.lang = lang;
    }

    /**
     * @return the locale
     */


    /**
     * @param locale
     *            the locale to set
     */

}
