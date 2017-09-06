package org.smart.framework.helper;

import java.lang.reflect.Field;
import java.util.Map;

import org.smart.framework.annotation.Inject;
import org.smart.framework.util.ArrayUtil;
import org.smart.framework.util.CollectionUtil;
import org.smart.framework.util.ReflectionUtil;

/**
 * ����ע��������
 * @author Administrator
 * Iocԭ���������������ϵ����ʱ�ﵽ�����Ŀ�ģ����Ʒ�ת�Ĺؼ����������ַ����еĴ��ι���
 * ͬʱ������ҲӦ�����סӲ����һ����ס��new�����󴴽������ж���
 * ��װIocĿ�꣺�������๹�췽���ڴ��б����������ͺ�ʵ��������һ����������ļ��и��ݶ���Ĺ��췽������ʵ�����������
 */
public class IocHelper {

	static{
		System.out.println("IocHelper is loading...");
		//��ȡ����bean���beanʵ��֮���ӳ���ϵBeanMap
		Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
		if(CollectionUtil.isNotEmpty(beanMap)){
			//�������beanMap
			for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
				//��beanMap�л�ȡbean��beanʵ��
				Class<?> beanClass = beanEntry.getKey();
				Object beanInstance = beanEntry.getValue();
				//��ȡbean�ඨ������г�Ա����
				Field[] beanFields = beanClass.getDeclaredFields();
				if(ArrayUtil.isNotEmpty(beanFields)){
					for (Field beanField : beanFields) {
						//�жϵ�ǰbeanfield�Ƿ���Injectע��
						if(beanField.isAnnotationPresent(Inject.class)){
							//��beanMap�л�ȡ beanfield ��Ӧ��ʵ��
							Class<?> beanFiledClass = beanField.getType();
							
							//Object beanFieldInstance = ReflectionUtil.newInstance(beanFiledClass);
							Object beanFieldInstance = beanMap.get(beanFiledClass);
							if(beanFieldInstance != null){
								//ͨ�������ʼ��BeanField��ֵ
								ReflectionUtil.setField(beanInstance, beanFieldInstance, beanField);
							}
						}
					}
				}
			}
		}
	}
}
