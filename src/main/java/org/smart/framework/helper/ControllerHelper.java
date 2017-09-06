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
 * ������������
 * @author Administrator
 * ��ACTION_MAP�����Request��Handler��ӳ���ϵ��Ȼ��ͨ��Classhelper����ȡ���д���Controllerע�����
 * ���ű�����ЩController�࣬��Actionע������ȡURL������ʼ��Request��Handler֮���ӳ���ϵ
 */
public final class ControllerHelper {

	/**
	 * ���ڴ�������봦������ӳ���ϵ
	 */
	private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();
	
	static{
		System.out.println("ControllerHelper is loading...");
		//��ȡ����Controller��
		Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
		if(CollectionUtil.isNotEmpty(controllerClassSet)){
			for (Class<?> controllerClass : controllerClassSet) {
				Method[] methods = controllerClass.getDeclaredMethods();
				if(ArrayUtil.isNotEmpty(methods)){
					for (Method method : methods) {
						if(method.isAnnotationPresent(Action.class)){
							//��Actionע���л�ȡurlӳ�����
							Action action = method.getAnnotation(Action.class);
							String mapping = action.value();
							//��֤urlӳ�����
							if(mapping.matches("\\w+:/\\w*")){
								String[] array = StringUtil.splitString(mapping, ":");
								if(ArrayUtil.isNotEmpty(array) && array.length == 2){
									//��ȡ���󷽷�������·��
									String requestMethod = array[0];
									String requestPath = array[1];
									Request request = new Request(requestMethod, requestPath);
									Handler handler = new Handler(controllerClass, method);
									//��ϵ�ķֱ�������ʽ������·���ķ�װ���ʵ����Controller����Controller���µĸ÷����ķ�װ���ʵ��
									ACTION_MAP.put(request, handler);
								}
							}
						}
					}
				}
			}
		}
	}
	
	//��ȡ Handler
	public static Handler getHandler(String requestMethod, String requestPath){
		
		Request request = new Request(requestMethod, requestPath);
		return ACTION_MAP.get(request);
	}
}
