// FileName: Url.java
//
// Desc:
//
// Created by token.tong at 2019-05-08 17:56:45

import java.util.ArrayList;

public class Url {

	public final static String HTTP_HEADER = "http://";
	public final static String HTTPS_HEADER = "https://";

	public final static int HTTP_HEADER_SIZE = 7;
	public final static int HTTPS_HEADER_SIZE = 8;

	final static String SEPERATE_S = "__|,|__";
	final static String VER = "1.0";

	public final static int UT_NONE = 0;
	public final static int UT_COMMON = 1;
	public final static int UT_IMAGE = 2;
	public final static int UT_NT_BQG_LIST = 3;
	public final static int UT_NT_BQG_CONTENT = 4;
	public final static int UT_NT_BQG_BK = 5;

	private String _url;
	private int _flag = UT_NONE;
	private String _ex;
	private ArrayList<String> _arr;

	public Url() {}

	@SuppressWarnings( "unchecked" )
	public Url( Url u ) {
		_url = u._url;
		_flag = u._flag;
		_ex = u._ex;
		_arr = (ArrayList<String>)u._arr.clone();
	}

	public void setUrl( String url ) {
		_url = url; 
	}
	public void setUrl( String parentUrl, String url ) {
		
		String domain = getDomain( parentUrl );
		String domainWithDir = getDomainWithDir( parentUrl );

		_url = formatUrl( domain, domainWithDir, url );
	}
	public String getUrl() { return _url; }

	public void setFlag( int flag ) { _flag = flag; }
	public int getFlag() { return _flag; }

	public void setEx( final String ex ) { _ex = ex; }
	public String getEx() { return _ex; }

	public void addArr( final String s ) { _arr.add(s); }
	public ArrayList<String> getArr() { return _arr; }

	void reset() {
		_url = "";
		_flag = UT_NONE;
		_ex = "";
		_arr.clear();
	}

	public String serial() {
		String ret = VER;
		ret += SEPERATE_S;

		ret += _url;
		ret += SEPERATE_S;

		ret += _flag;
		ret += SEPERATE_S;

		ret += _ex;
		for ( int i=0; i<_arr.size(); ++i ) {
			ret += SEPERATE_S;
			ret += _arr.get(i);
		}

		return ret;
	}

	public boolean load( final String data ) {
		String[] v = data.split( SEPERATE_S );
		if ( 0 == v.length ) {
			GlobalManager.getInstance().log_error( "url empty" );
			return false;
		}

		if ( "1.0" == v[0] ) {
			return load1_0(v);
		}

		return false;
	}

	private boolean load1_0( final String[] v ) {
		if ( v.length < 4 ) {
			GlobalManager.getInstance().log_error( "size:" + v.length + " error" );
			return false;
		}

		_url = v[1];
		_flag = UT_NONE;
		if ( v[2].length() > 0 ) {
			_flag = Integer.parseInt(v[2]);
		}

		if ( UT_NONE == _flag ) {
			GlobalManager.getInstance().log_error( "flag:" + _flag + " error" );
			return false;
		}

		_ex = v[3];

		for ( int i=4; i<v.length; ++i ) {
			_arr.add(v[i]);
		}

		return true;
	}

	private String formatUrl( final String domain, final String domainWithDir, final String url ) {
		String ret = url;
		int pos = ret.indexOf( HTTP_HEADER );

		if ( -1 == pos ) {
			pos = ret.indexOf( HTTPS_HEADER );
		}

		if ( -1 == pos ) {
			if ( '/' == ret.charAt(0) ) {
				if ( ret.length() < 2 || '/' != ret.charAt(1) ) {
					ret = domain + ret;
				} else {
					ret = "http:" + ret;
				}
			} else {
				ret = domainWithDir + "/" + ret;
			}
		}

		return ret;
	}

	private String getDomainWithDir( final String url ) {
		int len = HTTP_HEADER_SIZE;
		int pos = url.indexOf( HTTP_HEADER );
		if ( -1 == pos ) {
			len = HTTPS_HEADER_SIZE;
			pos = url.indexOf( HTTPS_HEADER );
		}

		String ret = "";
		int start = 0;
		if ( -1 != pos ) {
			start = len;
		} else {
			ret = HTTP_HEADER;
		}

		pos = url.lastIndexOf( '/' );
		if ( -1 != pos ) {
			ret += url.substring( 0, pos );
		} else {
			ret += url;
		}

		return ret;
	}

	private String getDomain( final String url ) {
		int len = HTTP_HEADER_SIZE;
		int pos = url.indexOf( HTTP_HEADER );
		if ( -1 == pos ) {
			len = HTTPS_HEADER_SIZE;
			pos = url.indexOf( HTTPS_HEADER );
		}

		String ret = "";
		int start = 0;
		if ( -1 != pos ) {
			start = len;
		} else {
			ret = HTTP_HEADER;
		}

		pos = url.indexOf( "/", start );
		if ( -1 != pos ) {
			ret += url.substring( 0, pos );
		} else {
			ret += url;
		}

		return ret;
	}

}
