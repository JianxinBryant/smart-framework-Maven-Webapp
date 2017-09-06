package org.smart.framework.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ClassUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);
	
	/**
	 * ��ȡ�������
	 */
	public static ClassLoader getClassLoader(){
		
		return Thread.currentThread().getContextClassLoader();
	}
	

    public static Class<?> loadClass(String className) {
        return loadClass(className, true);
    }

	
	/**
	 * ������
	 * Ϊ����߼���������ܣ��ɽ�isInitialized����Ϊfalse
	 */
	public static Class<?> loadClass(String className, boolean isInitialized){
		
		Class<?> cls;
		try {
			cls = Class.forName(className, isInitialized, getClassLoader());
		} catch (ClassNotFoundException e) {
			LOGGER.error("load class failure", e);
			throw new RuntimeException(e);
		}
		return cls;
	}
	
	/**
	 * ��ȡָ��������������
	 * ��Ҫ���ݰ���������ת��Ϊ�ļ�·������ȡclass�ļ���jar������ȡָ��������ȥ�����ࡣ
	 */
	public static Set<Class<?>> getClassSet(String packageName){
		
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		try {
			Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
			while(urls.hasMoreElements()){
				URL url = urls.nextElement();
				if(url != null){
					String protocol = url.getProtocol();
					if(protocol.equals("file")){
						String packagePath = url.getPath().replaceAll("%20", " ");
						addClass(classSet, packagePath, packageName);
					}else if (protocol.equals("jar")) {
						JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
						if(jarURLConnection != null){
							JarFile jarFile = jarURLConnection.getJarFile();
							if(jarFile != null){
								Enumeration<JarEntry> jarEntries = jarFile.entries();
								while(jarEntries.hasMoreElements()){
									JarEntry jarEntry = jarEntries.nextElement();
									String jarEntryName = jarEntry.getName();
									if(jarEntryName.endsWith(".class")){
										String className = jarEntryName.substring(0,jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
										doAddClass(classSet, className);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("get class set failure", e);
			throw new RuntimeException(e);
		}
		
		return classSet;
		
	}
	
	private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName){
		
		File[] files = new File(packagePath).listFiles(new FileFilter() {
			public boolean accept(File file) {
				
				return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
			}
		});
		for (File file : files) {
			String fileName = file.getName();
			if(file.isFile()){
				String className = fileName.substring(0, fileName.lastIndexOf("."));
				if(StringUtil.isNotEmpty(packageName)){
					className = packageName + "." + className;
				}
				doAddClass(classSet, className);
			}else{
				String subPackagePath = fileName;
				if(StringUtil.isNotEmpty(packagePath)){
					subPackagePath = packagePath + "/" + subPackagePath;
				}
				String subPackageName = fileName;
				if(StringUtil.isNotEmpty(packageName)){
					subPackageName = packageName + "." + subPackageName;
				}
				addClass(classSet, subPackagePath, subPackageName);
			}
		}
	}
	
	private static void doAddClass(Set<Class<?>> classSet, String className){
		
		Class<?> cls = loadClass(className, false);
		classSet.add(cls);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	/**
//	 * ��ȡĳ����������
//	 * @param packageName ����
//	 * @param isRecursion �Ƿ�����Ӱ�
//	 * @return �����������
//	 */
//	public static Set<Class<?>> getClassSet(String packageName, boolean isRecursion) {
//		Set<String> classNames = null;
//		ClassLoader loader = Thread.currentThread().getContextClassLoader();
//		String packagePath = packageName.replace(".", "/");
//
//		URL url = loader.getResource(packagePath);
//		if (url != null) {
//			String protocol = url.getProtocol();
//			if (protocol.equals("file")) {
//				classNames = getClassNameFromDir(url.getPath(), packageName, isRecursion);
//			} else if (protocol.equals("jar")) {
//				JarFile jarFile = null;
//				try{
//	                jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
//				} catch(Exception e){
//					e.printStackTrace();
//				}
//				
//				if(jarFile != null){
//					getClassNameFromJar(jarFile.entries(), packageName, isRecursion);
//				}
//			}
//		} else {
//			/*�����е�jar���в��Ұ���*/
//			classNames = getClassNameFromJars(((URLClassLoader)loader).getURLs(), packageName, isRecursion);
//		}
//		Set<Class<?>> classSet = new HashSet<Class<?>>();
//		for (String className : classNames) {
//			Class<?> cls = loadClass(className, false);
//			classSet.add(cls);
//		}
//		return classSet;
//	}
//
//	/**
//	 * ����Ŀ�ļ���ȡĳ����������
//	 * @param filePath �ļ�·��
//	 * @param className ��������
//	 * @param isRecursion �Ƿ�����Ӱ�
//	 * @return �����������
//	 */
//	private static Set<String> getClassNameFromDir(String filePath, String packageName, boolean isRecursion) {
//		Set<String> className = new HashSet<String>();
//		File file = new File(filePath);
//		File[] files = file.listFiles();
//		for (File childFile : files) {
//			if (childFile.isDirectory()) {
//				if (isRecursion) {
//					className.addAll(getClassNameFromDir(childFile.getPath(), packageName+"."+childFile.getName(), isRecursion));
//				}
//			} else {
//				String fileName = childFile.getName();
//				if (fileName.endsWith(".class") && !fileName.contains("$")) {
//					className.add(packageName+ "." + fileName.replace(".class", ""));
//				}
//			}
//		}
//
//		return className;
//	}
//
//	
//	/**
//	 * @param jarEntries
//	 * @param packageName
//	 * @param isRecursion
//	 * @return
//	 */
//	private static Set<String> getClassNameFromJar(Enumeration<JarEntry> jarEntries, String packageName, boolean isRecursion){
//		Set<String> classNames = new HashSet<String>();
//		
//		while (jarEntries.hasMoreElements()) {
//			JarEntry jarEntry = jarEntries.nextElement();
//			if(!jarEntry.isDirectory()){
//				/*
//	             * ������Ϊ�˷��㣬�Ȱ�"/" ת�� "." ���ж� ".class" ���������ܻ���bug
//	             * (FIXME: �Ȱ�"/" ת�� "." ���ж� ".class" ���������ܻ���bug)
//	             */
//				String entryName = jarEntry.getName().replace("/", ".");
//				if (entryName.endsWith(".class") && !entryName.contains("$") && entryName.startsWith(packageName)) {
//					entryName = entryName.replace(".class", "");
//					if(isRecursion){
//						classNames.add(entryName);
//					} else if(!entryName.replace(packageName+".", "").contains(".")){
//						classNames.add(entryName);
//					}
//				}
//			}
//		}
//		
//		return classNames;
//	}
//	
//	/**
//	 * ������jar�������ð�������ȡ�ð���������
//	 * @param urls URL����
//	 * @param packageName ��·��
//	 * @param isRecursion �Ƿ�����Ӱ�
//	 * @return �����������
//	 */
//	private static Set<String> getClassNameFromJars(URL[] urls, String packageName, boolean isRecursion) {
//		Set<String> classNames = new HashSet<String>();
//		
//		for (int i = 0; i < urls.length; i++) {
//			String classPath = urls[i].getPath();
//			
//			//��������classes�ļ���
//			if (classPath.endsWith("classes/")) {continue;}
//
//			JarFile jarFile = null;
//			try {
//				jarFile = new JarFile(classPath.substring(classPath.indexOf("/")));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			if (jarFile != null) {
//				classNames.addAll(getClassNameFromJar(jarFile.entries(), packageName, isRecursion));
//			}
//		}
//		
//		return classNames;
//	}

}
