package org.smart.framework;

import org.smart.framework.helper.AopHelper;
import org.smart.framework.helper.BeanHelper;
import org.smart.framework.helper.ClassHelper;
import org.smart.framework.helper.ControllerHelper;
import org.smart.framework.helper.IocHelper;
import org.smart.framework.util.ClassUtil;

/**
 * ���м�����Ӧ��Helper��
 */
public final class HelperLoader {

	public static void init(){
		
		Class<?> [] classList = {
				ClassHelper.class,
				BeanHelper.class,
				AopHelper.class,
				IocHelper.class,
				ControllerHelper.class
		};
		for (Class<?> cls : classList) {
			//����Class.forname��Ҫ��JVM���Ҳ�����ָ������
			ClassUtil.loadClass(cls.getName(), true);
		}
	}
}
