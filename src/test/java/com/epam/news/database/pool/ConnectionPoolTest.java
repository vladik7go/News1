package com.epam.news.database.pool;

import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


import com.epam.news.database.pool.ConnectionPool;
import com.epam.news.exception.TechnicalException;




public class ConnectionPoolTest {
	private ConnectionPool pool;

	@Test
	public void getConnectionTest() throws TechnicalException {

		ConnectionPool pool = ConnectionPool.getSinglePool();
		Connection connection = pool.getConnection();
		Assert.assertNotNull(connection);
		pool.returnConnection(connection);

	}

	public ConnectionPool getPool() {
		return pool;
	}

	public void setPool(ConnectionPool pool) {
		this.pool = pool;
	}

}
