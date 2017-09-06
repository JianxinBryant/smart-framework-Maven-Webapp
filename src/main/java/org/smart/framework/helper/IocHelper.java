package org.smart.framework.helper;

import java.lang.reflect.Field;
import java.util.Map;

import org.smart.framework.annotation.Inject;
import org.smart.framework.util.ArrayUtil;
import org.smart.framework.util.CollectionUtil;
import org.smart.framework.util.ReflectionUtil;

/**
 * 依赖注入助手类
 * @author Administrator
 * Ioc原理就是在有依赖关系存在时达到解耦的目的，控制反转的关键就是那三种方法中的传参过程
 * 同时，我们也应该像记住硬编码一样记住，new（对象创建）是有毒的
 * 封装Ioc目标：在依赖类构造方法内传有被依赖类类型和实例（方法一），在入口文件中根据定义的构造方法创建实例完成依赖。
 */
public class IocHelper {

	static{
		System.out.println("IocHelper is loading...");
		//获取所有bean类和bean实例之间的映射关系BeanMap
		Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
		if(CollectionUtil.isNotEmpty(beanMap)){
			//纵向遍历beanMap
			for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
				//从beanMap中获取bean和bean实例
				Class<?> beanClass = beanEntry.getKey();
				Object beanInstance = beanEntry.getValue();
				//获取bean类定义的所有成员变量
				Field[] beanFields = beanClass.getDeclaredFields();
				if(ArrayUtil.isNotEmpty(beanFields)){
					for (Field beanField : beanFields) {
						//判断当前beanfield是否有Inject注解
						if(beanField.isAnnotationPresent(Inject.class)){
							//在beanMap中获取 beanfield 对应的实例
							Class<?> beanFiledClass = beanField.getType();
							
							//Object beanFieldInstance = ReflectionUtil.newInstance(beanFiledClass);
							Object beanFieldInstance = beanMap.get(beanFiledClass);
							if(beanFieldInstance != null){
								//通过反射初始化BeanField的值
								ReflectionUtil.setField(beanInstance, beanFieldInstance, beanField);
							}
						}
					}
				}
			}
		}
	}
}
