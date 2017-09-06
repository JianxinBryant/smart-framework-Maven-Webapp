package org.smart.test.service;

import java.util.List;
import java.util.Map;

import org.smart.framework.annotation.Service;
import org.smart.framework.helper.DataBaseHelper;
import org.smart.test.model.Customer;


@Service
public class CustomerService {
	/**
	 * 获取客户列表
	 */
	public List<Customer> getCustomerList(){
		String sql = "select * from customer";
		return DataBaseHelper.queryEntityList(Customer.class, sql);
	}
	
	/**
	 * 获取客户
	 */
	public Customer getCustomer(long id){
		//TODO
		return null;
	}
	
	/**
	 * 创建客户
	 */
	public boolean createCustomer(Map<String, Object> fieldMap){
		//TODO
		return false;
	}
	
	/**
	 * 更新客户
	 */
	public boolean updateCustomer(long id, Map<String, Object> fieldMap){
		//TODO
		return false;
	}
	
	/**
	 * 删除客户
	 */
	public boolean deleteCustomer(long id){
		//TODO
		return false;
	}
}
