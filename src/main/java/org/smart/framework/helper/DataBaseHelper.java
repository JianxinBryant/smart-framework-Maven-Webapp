package org.smart.framework.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.util.CollectionUtil;
import org.smart.framework.util.PropsUtil;



public final class DataBaseHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseHelper.class);
	private static final QueryRunner QUERY_RUNNER;
	private static final ThreadLocal<Connection> CONNECTION_HOLDER;
	private static final BasicDataSource DATA_SOURCE;
	
//	private static final String DRIVER;
//	private static final String URL;
//	private static final String USERNAME;
//	private static final String PASSWORD;
	
	
	static{
		CONNECTION_HOLDER = new ThreadLocal<Connection>();
		QUERY_RUNNER = new QueryRunner();
		
		Properties conf = PropsUtil.loadProps("config.properties");
		String driver = conf.getProperty("jdbc.driver");
		String url = conf.getProperty("jdbc.url");
		String username = conf.getProperty("jdbc.username");
		String password = conf.getProperty("jdbc.password");
		
		DATA_SOURCE = new BasicDataSource();
		DATA_SOURCE.setDriverClassName(driver);
		DATA_SOURCE.setUrl(url);
		DATA_SOURCE.setUsername(username);
		DATA_SOURCE.setPassword(password);
		
//		try {
//			Class.forName(DRIVER);
//		} catch (ClassNotFoundException e) {
//			LOGGER.error("can not load jdbc driver", e);
//		}
	}
	
	/**
	 * ��ѯʵ����
	 */
	public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object...params){
		
		List<T> entityList;
		try {
			Connection conn = getConnection();
			entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
		} catch (SQLException e) {
			LOGGER.error("query entity list failure", e);
			throw new RuntimeException(e);
		} 
//		finally{
//			closeConnection();
//		}
		return entityList;
	}
	
	/**
	 * ��ѯʵ��
	 */
	public static <T> T queryEntity(Class<T> entityClass, String sql, Object...params){
		
		T entity;
		try {
			Connection conn = getConnection();
			entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
		} catch (SQLException e) {
			LOGGER.error("query entity failure", e);
			throw new RuntimeException(e);
		} 
//		finally{
//			closeConnection();
//		}
		return entity;
	}
	
	/**
	 * ִ�в�ѯ���
	 */
	public static List<Map<String, Object>> executeQuery(String sql, Object ...params){
		List<Map<String, Object>> result;
		try {
			Connection conn = getConnection();
			result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
		} catch (Exception e) {
			LOGGER.error("execute query failure", e);
			throw new RuntimeException(e);
		} 
//		finally {
//			closeConnection();
//		}
		return result;
	}
	
	/**
	 * ִ�и�����䣨����Update��insert��delete��
	 */
	public static int executeUpdate(String sql, Object params){
		
		int rows = 0;
		try {
			Connection conn = getConnection();
			rows = QUERY_RUNNER.update(conn,sql, params);
		} catch (SQLException e) {
			LOGGER.error("execute update failure");
			throw new RuntimeException(e);
		} 
//		finally {
//			closeConnection();
//		}
		return rows;
	}
	
	/**
	 * ����ʵ��
	 */
	public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap){
		
		if(CollectionUtil.isEmpty(fieldMap)){
			LOGGER.error("can not insert entity : entity is empty");
			return false;
		}
		
		String sql = "INSERT INTO " + getTableName(entityClass);
		StringBuilder columns = new StringBuilder("(");
		StringBuilder values = new StringBuilder("(");
		for(String fieldName : fieldMap.keySet()){
			columns.append(fieldName).append(", ");
			values.append("?, ");
		}
		columns.replace(columns.lastIndexOf(", "), values.length(), ")");
		values.replace(values.lastIndexOf(", "), values.length(), ")");
		sql += columns + "VALUES " + values;
		
		Object[] params = fieldMap.values().toArray();
		return executeUpdate(sql, params) == 1;
	}
	
	/**
	 * ����ʵ��
	 */
	public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap){
		if(CollectionUtil.isEmpty(fieldMap)){
			LOGGER.error("can not update entity : entity is empty");
			return false;
		}
		
		String sql = "UPDATE " + getTableName(entityClass) + " SET ";
		StringBuilder columns = new StringBuilder();
		for (String fieldName : fieldMap.keySet()) {
			
			columns.append(fieldName).append("=?, ");
			
		}
		sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id = ?";
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.addAll(fieldMap.values());
		paramList.add(id);
		Object[] params = paramList.toArray();
		
		return executeUpdate(sql, params) == 1;
	}
	/**
	 * ɾ��ʵ��
	 */
	public static <T> boolean deleteEntity(Class<T> entityClass, long id){
		String sql = "DELETE FROM " + getTableName(entityClass) + "WHERE id = ?";
		return executeUpdate(sql, id) == 1;
	}
	
	/**
	 * �õ�ʵ����ļ���
	 */
	public static String getTableName(Class<?> entityClass){
		
		return entityClass.getSimpleName();
	}
	/**
	 * ��ȡ���ݿ�����
	 */
	public static Connection getConnection(){
		Connection conn = CONNECTION_HOLDER.get();
		if(conn == null){
			try {
//				conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
				conn = DATA_SOURCE.getConnection();
			} catch (SQLException e) {
				LOGGER.error("get connection failure", e);
				throw new RuntimeException(e);
			} finally {
				CONNECTION_HOLDER.set(conn);
			}
		}
		
		return conn;
	}
	
	/**
	 * �ر����ݿ�����
	 */
//	public static void closeConnection(){
//		Connection conn = CONNECTION_HOLDER.get();
//		if(conn != null){
//			try {
//				conn.close();
//			} catch (Exception e) {
//				LOGGER.error("close connection failure", e);
//				throw new RuntimeException(e);
//			} 
//				finally {
//				CONNECTION_HOLDER.remove();
//			}
//		}
//	}
}
