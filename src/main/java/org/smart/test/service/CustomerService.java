package org.smart.test.service;

import java.util.List;
import java.util.Map;

import org.smart.framework.annotation.Service;
import org.smart.framework.helper.DataBaseHelper;
import org.smart.test.model.Customer;


@Service
public class CustomerService {
	/**
	 * ��ȡ�ͻ��б�
	 */
	public List<Customer> getCustomerList(){
		String sql = "select * from customer";
		return DataBaseHelper.queryEntityList(Customer.class, sql);
	}
	
	/**
	 * ��ȡ�ͻ�
	 */
	public Customer getCustomer(long id){
		//TODO
		return null;
	}
	
	/**
	 * �����ͻ�
	 */
	public boolean createCustomer(Map<String, Object> fieldMap){
		//TODO
		return false;
	}
	
	/**
	 * ���¿ͻ�
	 */
	public boolean updateCustomer(long id, Map<String, Object> fieldMap){
		//TODO
		return false;
	}
	
	/**
	 * ɾ���ͻ�
	 */
	public boolean deleteCustomer(long id){
		//TODO
		return false;
	}
}
