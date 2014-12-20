package com.epam.news.exception;

public class DaoException extends Exception {
	private static final long serialVersionUID = 1L;

	public DaoException() {
		super();
	}

	public DaoException(String s) {
		super(s);
	}

	public DaoException(Exception e) {
		super(e);
	}

	public DaoException(String s, Exception e) {
		super(s, e);
	}

}
