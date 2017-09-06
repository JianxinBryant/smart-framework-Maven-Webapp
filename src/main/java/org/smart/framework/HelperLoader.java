package org.smart.framework;

import org.smart.framework.helper.AopHelper;
import org.smart.framework.helper.BeanHelper;
import org.smart.framework.helper.ClassHelper;
import org.smart.framework.helper.ControllerHelper;
import org.smart.framework.helper.IocHelper;
import org.smart.framework.util.ClassUtil;

/**
 * 集中加载相应的Helper类
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
			//其中Class.forname是要求JVM查找并加载指定的类
			ClassUtil.loadClass(cls.getName(), true);
		}
	}
}
