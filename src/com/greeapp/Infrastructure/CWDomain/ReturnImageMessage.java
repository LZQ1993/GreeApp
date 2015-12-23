package com.greeapp.Infrastructure.CWDomain;

public class ReturnImageMessage {
	
	public String FileAndPath;
	public String Size;
	public String WebAddress;
	public String BaseUrl;

	public String getFileAndPath() {
		return FileAndPath;
	}

	public void setFileAndPath(String _FileAndPath) {
		this.FileAndPath = _FileAndPath;
	}

	public String getSize() {
		return Size;
	}

	public void setName(String _Size) {
		this.Size = _Size;
	}

	public String getWebAddress() {
		return WebAddress;
	}

	public void setWebAddress(String WebAddress) {
		this.WebAddress = WebAddress;
	}

	public String getBaseUrl() {
		return BaseUrl;
	}

	public void setBaseUrl(String BaseUrl) {
		this.BaseUrl = BaseUrl;
	}

}
