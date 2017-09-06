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
 * ����ת����
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
		
		//��ȡ���󷽷�������·��
		String requestMethod = request.getMethod().toLowerCase();
		String requestPath = request.getPathInfo();
		//��ȡAction������
		Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
		if(handler != null){
			//��ȡController���Լ�beanʵ��
			Class<?> controllerClass = handler.getControllerClass();
			Object beanProxy = BeanHelper.getBean(controllerClass);
			//��ȡ�����������
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Enumeration<String> parameterNames = request.getParameterNames();//ʹ��Enumerationԭ�����ص�
			while (parameterNames.hasMoreElements()) {
				
				String paramName = parameterNames.nextElement();
				String paramValue = request.getParameter(paramName);
				paramMap.put(paramName, paramValue);
			}
			//��ȡio����Ĳ�������
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
			//����Action����
			Method actionMethod = handler.getActionMethod();
			Object result = ReflectionUtil.invokeMethod(beanProxy, actionMethod, param);
			//����Action�����ķ���ֵ
			if(result instanceof View){
				//����jspҳ��
				View view = (View) result;
				//Ҫ���û����տ��������View��ĳ�Ա������װAction���صĶ���
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
				//����JSON����
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