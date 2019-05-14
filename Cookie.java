// FileName: Cookie.java
//
// Desc:
//
// Created by token.tong at 2019-05-14 14:10:45

import java.util.Date;
import java.text.SimpleDateFormat;

public class Cookie {
	String _name = null;
	String _value = null;
	String _expires = null;
	String _domain = null;
	String _path = null;
	boolean _secure = false;
	long _maxAge = 0;
	boolean _httpOnly = false;
	long _expiresTime = 0;

	public boolean parse( final String line ) {
		String[] arr = line.split( ";" );
		for ( int i=0; i<arr.length; ++i ) {
			String tmp = arr[i].trim();
			int pos = tmp.indexOf( "=" );
			if ( -1 != pos ) {
				String key = tmp.substring( 0, pos );
				String value = tmp.substring( pos+1 );
				if ( key.equalsIgnoreCase( "Expires" ) ) {
					_expires = value;
				} else if ( key.equalsIgnoreCase( "Path" ) ) {
					_path = value;
				} else if ( key.equalsIgnoreCase( "Domain" ) ) {
					_domain = value;
				} else if ( key.equalsIgnoreCase( "Max-Age" ) ) {
					_maxAge = Integer.parseInt( value );
				} else if ( key.equalsIgnoreCase( "HttpOnly" ) ) {
					_httpOnly = value.equalsIgnoreCase( "true" );
				} else {
					_name = key;
					_value = value;
				}
			} else {
				if ( tmp.equalsIgnoreCase( "secure" ) ) {
					_secure = true;
				}
			}
		}

		transTime();

		return ( !_name.isEmpty() );
	}

	public String getName() { return _name; }
	public String getValue() { return _value; }
	public String getExpires() { return _expires; }
	public String getDomain() { return _domain; }
	public String getPath() { return _path; }
	public boolean isSecure() { return _secure; }
	public long getMaxAge() { return _maxAge; }
	public boolean isHttpOnly() { return _httpOnly; }
	public long getExpiresTime() { return _expiresTime; }

	public void print() {
		String out = _name + "=" + _value;
		if ( !_expires.isEmpty() ) {
			out += "; Expires=" + _expires;
		}

		if ( !_path.isEmpty() ) {
			out += "; Path=" + _path;
		}

		if ( isSecure() ) {
			out += "; SECURE";
		}

		if ( isHttpOnly() ) {
			out += "; HttpOnly=true";
		}

		if ( getMaxAge() > 0 ) {
			out += "; Max-Age=" + getMaxAge();
		}

		if ( !_domain.isEmpty() ) {
			out += "; Domain=" + _domain;
		}

		out += "; expiresTime=" + getExpiresTime();

		GlobalManager.getInstance().log_run( out );
	}
	
	void transTime() {
		try {
			if ( getMaxAge() > 0 ) {
				_expiresTime = new Date().getTime() + getMaxAge();
			} else if ( !_expires.isEmpty() ) {
				SimpleDateFormat sdf = new SimpleDateFormat( "EEE, dd MMM yyyy HH:mm:ss" );
				Date dt = sdf.parse( _expires );
				_expiresTime = dt.getTime();
			}
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		}
	}
}
