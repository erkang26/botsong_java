// FileName: HttpResponse.java
//
// Desc:
//
// Created by token.tong at 2019-05-14 11:15:52

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class HttpResponse {

	HttpRequest _request = null;
	byte[] _body = null;
	HashMap<String, String> _headers = new HashMap<>();
	ArrayList<Cookie> _cookies = new ArrayList<>();
	String _ver = null;
	int _code = 0;
	String _codeDesc = null;

	public HttpResponse( HttpRequest req ) {
		_request = req;
	}

	public boolean parse( final byte[] body, Map<String, List<String>> headers ) {
		_body = body;

		GlobalManager.getInstance().log_debug( new String(body) );

		for( String key : headers.keySet() ) {
			List<String> l = headers.get(key);
			String value = l.get(0);
			if ( null == key ) {
				parseFirstLine(value);
			}
			else if ( key.equalsIgnoreCase( "Set-Cookie" ) ) {
				Cookie ck = new Cookie();
				if ( !ck.parse( value ) ) {
					GlobalManager.getInstance().log_error( "parse cookie failed:" + value );
				} else {
					_cookies.add( ck );
				}
			} else {
				_headers.put( key, value );
			}
		}

		return true;
	}

	public boolean hasHeader( final String key ) {
		return _headers.containsKey( key );
	}

	public String getHeader( final String name ) {
		return (String)_headers.get(name);
	}

	public int getCode() { return _code; }
	public byte[] getBody() { return _body; }
	public ArrayList<Cookie> getCookies() { return _cookies; }
	public HttpRequest getRequest() { return _request; }

	void parseFirstLine( final String line ) {
		int n = line.indexOf( " " );
		_ver = line.substring( 0, n );
		int m = line.indexOf( " ", n+1 );
		_code = Integer.parseInt( line.substring( n+1, m ) );
		_codeDesc = line.substring( m+1 );
	}
}
