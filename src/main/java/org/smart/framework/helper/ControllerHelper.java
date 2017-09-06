package org.smart.framework.helper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.smart.framework.annotation.Action;
import org.smart.framework.bean.Handler;
import org.smart.framework.bean.Request;
import org.smart.framework.util.ArrayUtil;
import org.smart.framework.util.CollectionUtil;
import org.smart.framework.util.StringUtil;

/**
 * 控制器助手类
 * @author Administrator
 * 用ACTION_MAP来存放Request和Handler的映射关系，然后通过Classhelper来获取所有带有Controller注解的类
 * 接着遍历这些Controller类，从Action注解中提取URL，最后初始化Request和Handler之间的映射关系
 */
public final class ControllerHelper {

	/**
	 * 用于存放请求与处理器的映射关系
	 */
	private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();
	
	static{
		System.out.println("ControllerHelper is loading...");
		//获取所有Controller类
		Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
		if(CollectionUtil.isNotEmpty(controllerClassSet)){
			for (Class<?> controllerClass : controllerClassSet) {
				Method[] methods = controllerClass.getDeclaredMethods();
				if(ArrayUtil.isNotEmpty(methods)){
					for (Method method : methods) {
						if(method.isAnnotationPresent(Action.class)){
							//从Action注解中获取url映射规则
							Action action = method.getAnnotation(Action.class);
							String mapping = action.value();
							//验证url映射规则
							if(mapping.matches("\\w+:/\\w*")){
								String[] array = StringUtil.splitString(mapping, ":");
								if(ArrayUtil.isNotEmpty(array) && array.length == 2){
									//获取请求方法与请求路径
									String requestMethod = array[0];
									String requestPath = array[1];
									Request request = new Request(requestMethod, requestPath);
									Handler handler = new Handler(controllerClass, method);
									//联系的分别是请求方式与请求路径的封装类的实例和Controller类与Controller类下的该方法的封装类的实例
									ACTION_MAP.put(request, handler);
								}
							}
						}
					}
				}
			}
		}
	}
	
	//获取 Handler
	public static Handler getHandler(String requestMethod, String requestPath){
		
		Request request = new Request(requestMethod, requestPath);
		return ACTION_MAP.get(request);
	}
}
