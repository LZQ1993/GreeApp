package com.greeapp.Entity;

import java.io.Serializable;
import java.util.List;

public class OrderDetail implements Serializable {
	public String WorkID;
	public String WorkType;
	public String AppointTime;
	public String AssignTime;
	public String Remark;
	public String ClientName;
	public String ClientTel1;
	public String ClientTel2;
	public String InstallAddress;
	public String SaleOrder;
	public String BuyTime;
	public String SaleDepartment;
	public List<Object> ProductList;
}
