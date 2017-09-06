package org.smart.framework.helper;

import java.util.Properties;

import org.smart.framework.ConfigConstant;
import org.smart.framework.util.PropsUtil;

/**
 * �����ļ�������
 * @author Administrator
 *
 */
public class ConfigHelper {

	private static final Properties CONFIG_PROPS = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);
	
	/**
	 * ��ȡJDBC����
	 */
	public static String getJdbcDriver(){
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_DRIVER);
	}
	
	/**
	 * ��ȡJDBC URL
	 */
	public static String getJdbcUrl(){
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_URL);
	}
	
	/**
	 * ��ȡJDBC �û���
	 */
	public static String getJdbcUsername(){
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_USERNAME);
	}
	
	/**
	 * ��ȡJDBC ����
	 */
	public static String getJdbcPassword(){
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.JDBC_PASSWORD);
	}
	
	/**
	 * ��ȡӦ�û�������
	 */
	public static String getAppBasePackage(){
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_BASE_PACKAGE);
	}
	
	/**
	 * ��ȡӦ��JSP·��
	 */
	public static String getAppJspPath(){
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_JSP_PATH,"/WEB-IMF/view/");
	}
	
	/**
	 * ��ȡӦ�þ�̬��Դ·��
	 */
	public static String getAppAssetPath(){
		return PropsUtil.getString(CONFIG_PROPS, ConfigConstant.APP_ASSET_PATH,"/asset/");
	}
	
}
