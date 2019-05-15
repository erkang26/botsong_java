// FileName: Cookie.java
//
// Desc:
//
// Created by token.tong at 2019-05-14 14:10:45

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

	int getMon( final String name ) {
		int mon = 0;
		if ( name.equalsIgnoreCase( "Jan" ) ) {
			mon = 1;
		} else if ( name.equalsIgnoreCase( "Feb" ) ) {
			mon = 2;
		} else if ( name.equalsIgnoreCase( "Mar" ) ) {
			mon = 3;
		} else if ( name.equalsIgnoreCase( "Apr" ) ) {
			mon = 4;
		} else if ( name.equalsIgnoreCase( "May" ) ) {
			mon = 5;
		} else if ( name.equalsIgnoreCase( "Jun" ) ) {
			mon = 6;
		} else if ( name.equalsIgnoreCase( "Jul" ) ) {
			mon = 7;
		} else if ( name.equalsIgnoreCase( "Aug" ) ) {
			mon = 8;
		} else if ( name.equalsIgnoreCase( "Sept" ) || name.equalsIgnoreCase( "Sep" ) ) {
			mon = 9;
		} else if ( name.equalsIgnoreCase( "Oct" ) ) {
			mon = 10;
		} else if ( name.equalsIgnoreCase( "Nov" ) ) {
			mon = 11;
		} else if ( name.equalsIgnoreCase( "Dec" ) ) {
			mon = 12;
		}

		return mon;
	}

	String timeFormat() {
		int pos = _expires.indexOf( ", " );
		if ( -1 == pos ) {
			return _expires;
		}

		String tmp = _expires.substring( pos+2 );

		String[] v = tmp.split( " " );
		pos = tmp.indexOf( "-" );
		int day = 0;
		int mon = 0;
		int year = 0;
		String time = "";
		String suffix = "";
		if ( -1 == pos ) {
			day = Integer.parseInt(v[0]);
			mon = getMon(v[1]);
			year = Integer.parseInt(v[2]);
			time = v[3];
			if ( v.length > 4 ) {
				suffix = v[4];
			}
		} else {
			String[] vDate = v[0].split( "-" );
			time = v[1];
			if ( v.length > 2 ) {
				suffix = v[2];
			}
			day = Integer.parseInt( vDate[0] );
			mon = getMon(vDate[1]);
			year = Integer.parseInt( vDate[2] );
		}

		String ret = String.format( "%04d-%02d-%02d %s", year, mon, day, time );

		return ret;
	}
	
	void transTime() {
		try {
			if ( getMaxAge() > 0 ) {
				_expiresTime = new Date().getTime() + getMaxAge();
			} else if ( !_expires.isEmpty() ) {
				String time = timeFormat();
				SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss", Locale.UK );
				Date dt = sdf.parse( time );
				_expiresTime = dt.getTime();
			}
		} catch( Exception e ) {
			GlobalManager.getInstance().log_error( "time format unknown: " + _expires );
			GlobalManager.getInstance().get_except().log_exception(e);
		}
	}
}
