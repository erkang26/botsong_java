// FileName: Url.java
//
// Desc:
//
// Created by token.tong at 2019-05-08 17:56:45

import java.util.ArrayList;

public class Url {

	public void setUrl( String url ) {
		_url = url; 
	}
	public void setUrl( String parentUrl, String url ) {
		_url = url;
	}
	public String getUrl() { return _url; }

	private String _url;
	private UrlType _flag;
	private String _ex;
	private ArrayList<String> _arr;
}
