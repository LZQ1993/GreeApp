package com.greeapp.Infrastructure.CWDataDecoder;

import java.util.List;

public class DataResult {
	public String resultcode;
	public String currentpage;
	public String totalpage;
	public String rowsofapage;
	public List<Object> result;
	
	
	public String getResultcode() {
		return resultcode;
	}
	public void setResultcode(String resultcode) {
		this.resultcode = resultcode;
	}
	public String getCurrentpage() {
		return currentpage;
	}
	public void setCurrentpage(String currentpage) {
		this.currentpage = currentpage;
	}
	public String getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(String totalpage) {
		this.totalpage = totalpage;
	}
	public String getRowsofapage() {
		return rowsofapage;
	}
	public void setRowsofapage(String rowsofapage) {
		this.rowsofapage = rowsofapage;
	}
	public List<Object> getResult() {
		return result;
	}
	public void setResult(List<Object> result) {
		this.result = result;
	}
	
	
	

}
