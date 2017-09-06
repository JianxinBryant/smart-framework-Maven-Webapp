package org.smart.framework.util;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 属性文件助手类
 * @author Administrator
 *
 */
public class PropsUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);
	
	/**
	 * 加载属性文件
	 */
	public static Properties loadProps(String fileName){
		
		Properties props = new Properties();
		InputStream is = null;
		try{
			//得到的也是当前ClassPath的绝对URI路径，然后在此基础上new InputStream(file)
//			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
//			if(is == null){
//				throw new FileNotFoundException(fileName+"file is not found");
//			}
//			props = new Properties();
			//Properties的load方法其实就是传进去一个输入流，字节流或者字符流，字节流利用InputStreamReader转化为字符流，然后字符流用BufferedReader包装，BufferedReader读取properties配置文件，
			//每次读取一行，分割成两个字符串。因为Properties是Map的子类，然后用put将两个字符串装进Properties对象props
//			props.load(is);
			if(StringUtil.isEmpty(fileName)){
				throw new IllegalArgumentException();
			}
//			String suffix = ".properties";
//			if(fileName.lastIndexOf(suffix) == -1){
//				fileName += suffix;
//			}
			is = ClassUtil.getClassLoader().getResourceAsStream(fileName);
			System.out.println("is = " + is);
			if(is != null){
				props.load(is);
			}
		}catch(Exception e){
			LOGGER.error("load properties file failure",e);
			throw new RuntimeException(e);
		}finally{
			if(is != null){
				try{
					is.close();
				}catch(IOException e){
					LOGGER.error("close input stream failure", e);
				}
			}
		}
		return props;
	}
	/**
	 * 获取字符型属性（默认值为空字符串）
	 */
	public static String getString(Properties props, String key){
		
		return getString(props, key, "");
	}
	
	/**
	 * 获取字符型属性（可指定默认值）
	 */
	public static String getString(Properties props, String key, String defaultValue) {
		
		String value = defaultValue;
		if(props.containsKey(key)){
			value = props.getProperty(key);
		}
		return value;
	}
	
	/**
	 * 获取数值型属性（默认值为0）
	 */
	public static int getInt(Properties props, String key){
		
		return getInt(props, key, 0);
	}
	
	/**
	 * 获取数值型属性（可指定默认值）
	 */
	public static int getInt(Properties props, String key, int defaultValue) {
		int value = defaultValue;
		if(props.containsKey(key)){
			value = CastUtil.castInt(props.getProperty(key));
		}
		return value;
	}
	
	/**
	 * 获取布尔型属性（默认值为false）
	 */
	public static boolean getBoolean(Properties props, String key){
		
		return getBoolean(props, key, false);
	}
	
	/**
	 * 获取布尔型属性（可指定默认值）
	 */
	private static boolean getBoolean(Properties props, String key, boolean defaultValue) {
		boolean value = defaultValue;
		if(props.containsKey(key)){
			value = CastUtil.castBoolean(props.getProperty(key));
		}
		return value;
	}
	
}
