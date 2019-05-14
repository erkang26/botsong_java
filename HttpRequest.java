// FileName: HttpRequest.java
//
// Desc:
//
// Created by token.tong at 2019-05-14 11:15:45

import java.net.URLConnection;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;

public class HttpRequest {
	String _originUrl = "";
	URL _url = null;
	URLConnection _conn = null;
	HttpResponse _response = null;
	HashMap<String, String > _headers = new HashMap<>();
	String _body = "";
	String _host = "";
	int _port = 0;
	boolean _ssl = false;
	String _uri = "";
	boolean _timeout = false;

	public String getOriginUrl() { return _originUrl; }
	public boolean isTimeout() { return _timeout; }

	public HttpResponse get( final String url ) {
		_originUrl = url;
		parseRequest( url );
		_headers.put( "Host", ""+_host );
		_headers.put( "Accept", "*/*" );
		_headers.put( "User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3759.4 Safari/537.36" );
		_headers.put( "Connection", "close" );
		if ( !doRequest( "GET" ) ) {
			GlobalManager.getInstance().log_error( "doRequest failed" );
			return null;
		}

		return _response;
	}

	void parseRequest( final String url ) {
		String tmp = "";
		_ssl = true;
		int pos = url.indexOf( Url.HTTPS_HEADER );
		if ( -1 == pos ) {
			_ssl = false;
			pos = url.indexOf( Url.HTTP_HEADER );
		}

		if ( -1 != pos ) {
			int len = 0;
			if ( _ssl ) {
				len = Url.HTTPS_HEADER_SIZE;
			} else {
				len = Url.HTTP_HEADER_SIZE;
			}
			tmp = url.substring( pos+len );
		} else {
			_ssl = false;
			tmp = url;
		}

		pos = tmp.indexOf( "/" );
		if ( -1 == pos ) {
			_uri = "/";
		} else {
			_uri = tmp.substring( pos );
			tmp = tmp.substring( 0, pos );
			tmp = tmp.trim();
		}

		pos = tmp.indexOf( ":" );
		if ( -1 == pos ) {
			if ( _ssl ) {
				_port = 443;
			} else {
				_port = 80;
			}
		} else {
			_port = Integer.parseInt( tmp.substring( pos+1 ) );
			tmp = tmp.substring( 0, pos );
		}

		_host = tmp;
	}

	String makeRequest( final String method ) {
		String data = method + " " + _uri + " HTTP/1.1\r\n";

		for ( String key : _headers.keySet() ) {
			data += key + ": " + _headers.get(key) + "\r\n";
		}

		if ( !_body.isEmpty() ) {
			data += "Content-Length: " + _body.length() + "\r\n";
		}
		data += "\r\n";
		if ( !_body.isEmpty() ) {
			data += _body;
		}

		return data;
	}

	boolean doRequest( final String method ) {
		InputStream is = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		boolean ret = true;
		try {
			String data = makeRequest( method );

			GlobalManager.getInstance().log_debug( data );

			URL url = new URL( _originUrl );
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout( 10000 );
			conn.setReadTimeout( 60000 );
			is = conn.getInputStream();

			byte[] buf = new byte[1024];
			int len = 0;
			while( -1 != (len = is.read(buf)) ) {
				GlobalManager.getInstance().log_debug( "len: " + len );
				out.write( buf, 0, len );
			}

			_response = new HttpResponse(this);
			byte[] res = out.toByteArray();
			if ( !_response.parse( res, conn.getHeaderFields() ) )
			{
				GlobalManager.getInstance().log_error( "response parse failed: " + new String(res) );
				ret = false;
				_response = null;
			}

			GlobalManager.getInstance().log_debug( "parse response finished" );

		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
			ret = false;
		} finally {
			try {
				if ( null != out ) {
					out.close();
				}

				if ( null != is ) {
					is.close();
				}
			} catch( Exception e ) {
				GlobalManager.getInstance().get_except().log_exception(e);
			}
		}

		return ret;
	}
}
