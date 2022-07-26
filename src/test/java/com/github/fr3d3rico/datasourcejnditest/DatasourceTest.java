package com.github.fr3d3rico.datasourcejnditest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatasourceTest {

	private static HikariConfig config = new HikariConfig();
	private static HikariDataSource ds;
	
	private static InitialContext initContext;
	
	static {
        config.setJdbcUrl("jdbc:sqlserver://localhost:1433;database=DATA_BASE_NAME;trustServerCertificate=true;");
        config.setUsername("sa");
        config.setPassword("");
        ds = new HikariDataSource(config);
    }
	
	@BeforeAll
	public static void setup() throws Exception {
		initContext = new InitialContext();
	}
	
	@AfterAll
	public static void tearDown() throws Exception {
		initContext.close();
	}
	
	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
	
	@Test
	public void dataSourceTest() throws SQLException {
		assertNotNull(DatasourceTest.getConnection());
	}
	
	@Test
	public void jndiTest() throws NamingException, SQLException {
		Context envContext = (Context) this.initContext.lookup("java:/comp/env");
		
		DataSource ds = (DataSource) envContext.lookup("datasource/ds");
		Connection conn = ds.getConnection();
		assertNotNull(conn);
		
		
		
		ds = (DataSource) envContext.lookup("jdbc/name1");
		conn = ds.getConnection();
		assertNotNull(conn);
		
		ds = (DataSource) envContext.lookup("jdbc/name2");
		conn = ds.getConnection();
		assertNotNull(conn);
	}
	
	@Test
	public void sqlTest() throws Exception {
		Context envContext = (Context) this.initContext.lookup("java:/comp/env");
		
		DataSource ds = (DataSource) envContext.lookup("datasource/ds");
		Connection conn = ds.getConnection();
		assertNotNull(conn);
		
		//sql test
		Statement st = conn.createStatement();
		String sqlStr = "select * from TABLE_NAME";
		ResultSet rs = st.executeQuery(sqlStr);
		while (rs.next()) {
			assertNotNull(rs.getString("name"));
		}
	}
	
}
