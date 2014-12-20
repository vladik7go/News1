package com.epam.news.database.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.operation.InsertOperation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.epam.news.entity.News;
import com.epam.news.exception.DaoException;
import com.epam.news.exception.TechnicalException;
import com.epam.news.util.DateConverter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/testApplicationContext.xml" })
public class NewsDAOTest {
	private static final Logger LOGGER = Logger.getLogger(NewsDAOTest.class);
	protected JdbcTemplate template;
	protected Connection con;
	protected IDatabaseConnection dbUnitCon;

	@Autowired
	private INewsDao dao;

	@Autowired
	private DataSource dataSource;

	/**
	 * This method Clear test-DB, and fill with test data. NOTE! We MUST provide
	 * the schema name when creating the database connection. Note that for
	 * Oracle you must specify the schema name in uppercase.
	 * 
	 * @throws Exception
	 */
	@Before
	public void loadTestData() throws Exception {
		template = new JdbcTemplate(dataSource);
		con = DataSourceUtils.getConnection(dataSource);
		dbUnitCon = new DatabaseConnection(con, "TESTUSER");

		try {
			dbUnitCon.getConfig().setProperty(
					DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
					new OracleDataTypeFactory());
			InsertOperation.CLEAN_INSERT.execute(dbUnitCon,
					loadClasspathDataSet(getDataSetPaths()));
		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
	}

	/**
	 * This method CLEAN all data in test-DB. NOTE! We MUST provide the schema
	 * name when creating the database connection. Note that for Oracle you must
	 * specify the schema name in uppercase.
	 * 
	 * @throws DatabaseUnitException
	 * @throws SQLException
	 */
	@After
	public void tearDown() throws DatabaseUnitException, SQLException {
		template = new JdbcTemplate(dataSource);
		con = DataSourceUtils.getConnection(dataSource);
		dbUnitCon = new DatabaseConnection(con, "TESTUSER");

		try {
			dbUnitCon.getConfig().setProperty(
					DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
					new OracleDataTypeFactory());
			InsertOperation.DELETE_ALL.execute(dbUnitCon,
					loadClasspathDataSet(getDataSetPaths()));
		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
	}

	@Test
	public void getAllTest() throws DaoException, TechnicalException {

		List<News> list = new ArrayList<News>();
		list = dao.getAll();
		Assert.assertNotNull(list);
		for (News news : list) {
			System.out.println(news);
		}
		System.out.println("--- End of getAllTest ---");
	}

	

	@Test
	public void testGetNewsById() throws TechnicalException, DaoException {
		News news = dao.getById(1);
		Assert.assertNotNull(news);
		Assert.assertEquals("2014-12-12", String.valueOf(news.getDate()));
		Assert.assertEquals("test title 1", news.getTitle());
		Assert.assertEquals("asd 1", news.getBrief());
		Assert.assertEquals("Test content -1", news.getContent());
	}
	
	
	
	/**
	 * This method creates an "object map" of DB, in order to use it for PUT or
	 * CLEAN data.
	 * 
	 * @param names
	 * @return
	 */
	public IDataSet loadClasspathDataSet(String[] names) {
		IDataSet dataset = null;
		FlatXmlDataSet[] dataSets = new FlatXmlDataSet[names.length];

		try {
			for (int i = 0; i < names.length; ++i) {
				dataSets[i] = new FlatXmlDataSetBuilder().build(getClass()
						.getResourceAsStream(names[i]));
			}

			dataset = new CompositeDataSet(dataSets);
		} catch (Exception ex) {
			try {
				for (int i = 0; i < names.length; ++i) {
					dataSets[i] = new FlatXmlDataSetBuilder().build(getClass()
							.getResourceAsStream("/" + names[i]));
				}

				dataset = new CompositeDataSet(dataSets);
			} catch (Exception e) {
				LOGGER.debug("Error loading XML DataSet", e);
			}
		}
		Map<String, Object> objectMap = new HashMap<String, Object>(1);
		objectMap.put("(NULL)", null);

		return new ReplacementDataSet(dataset, objectMap, null);
	}

	/**
	 * This method create array of Strings, with length 1 element, and return
	 * it.
	 * 
	 * @return
	 */
	public String[] getDataSetPaths() {
		return new String[] { "/testData.xml" };
	}

}
