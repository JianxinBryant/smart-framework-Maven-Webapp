package org.smart.framework;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smart.framework.bean.Data;
import org.smart.framework.bean.Handler;
import org.smart.framework.bean.Param;
import org.smart.framework.bean.View;
import org.smart.framework.helper.BeanHelper;
import org.smart.framework.helper.ConfigHelper;
import org.smart.framework.helper.ControllerHelper;
import org.smart.framework.util.ArrayUtil;
import org.smart.framework.util.CodecUtil;
import org.smart.framework.util.JsonUtil;
import org.smart.framework.util.ReflectionUtil;
import org.smart.framework.util.StreamUtil;
import org.smart.framework.util.StringUtil;

/**
 * 请求转发器
 * @author Administrator
 *
 */
@WebServlet(urlPatterns = "/*", loadOnStartup=0)
public class DispatcherServlet extends HttpServlet {

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		
		HelperLoader.init();
		ServletContext servletContext = servletConfig.getServletContext();
		ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
		jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
		ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
		defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
	}
	
	
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//获取请求方法与请求路径
		String requestMethod = request.getMethod().toLowerCase();
		String requestPath = request.getPathInfo();
		//获取Action处理器
		Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
		if(handler != null){
			//获取Controller类以及bean实例
			Class<?> controllerClass = handler.getControllerClass();
			Object beanProxy = BeanHelper.getBean(controllerClass);
			//获取请求参数对象
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Enumeration<String> parameterNames = request.getParameterNames();//使用Enumeration原因及其特点
			while (parameterNames.hasMoreElements()) {
				
				String paramName = parameterNames.nextElement();
				String paramValue = request.getParameter(paramName);
				paramMap.put(paramName, paramValue);
			}
			//获取io流里的参数对象
			String body = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
			if(StringUtil.isNotEmpty(body)){
				String[] params = StringUtil.splitString(body, "&");
				if(ArrayUtil.isNotEmpty(params)){
					for (String param : params) {
						String[] array = StringUtil.splitString(param, "=");
						if(ArrayUtil.isNotEmpty(array) && array.length == 2){
							String paramName = array[0];
							String paramValue = array[1];
							paramMap.put(paramName, paramValue);
						}
					}
				}
			}
			
			Param param = new Param(paramMap);
			//调用Action方法
			Method actionMethod = handler.getActionMethod();
			Object result = ReflectionUtil.invokeMethod(beanProxy, actionMethod, param);
			//处理Action方法的返回值
			if(result instanceof View){
				//返回jsp页面
				View view = (View) result;
				//要求用户按照框架所给的View类的成员变量封装Action返回的对象
				String path = view.getPath();
				if(StringUtil.isNotEmpty(path)){
					if(path.startsWith("/")){
						response.sendRedirect(request.getContextPath() + path);
					}else{
						Map<String, Object> model = view.getModel();
						for (Map.Entry<String, Object> entry : model.entrySet()) {
							request.setAttribute(entry.getKey(), entry.getValue());
						}
						request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
					}
				}
			}else if (result instanceof Data) {
				//返回JSON数据
				Data data = (Data) result;
				Object model = data.getModel();
				if(model != null){
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					PrintWriter writer = response.getWriter();
					String json = JsonUtil.toJson(model);
					writer.write(json);
					writer.flush();
					writer.close();
				}
			}
			
		}
	}


	

}
