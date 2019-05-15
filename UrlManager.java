// FileName: UrlManager.java
//
// Desc:
//
// Created by token.tong at 2019-05-13 13:52:29

import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileWriter;

public class UrlManager {
	
	private static UrlManager _instance = null;

	private TsMap _mapUsedUrl = new TsMap();
	private TsList _urlList = new TsList();
	private TsList _imgList = new TsList();

	private Locker _snLocker = new Locker();
	private long _sn = 0;
	String _sameSrc = "";
	boolean _debug = false;

	static UrlManager getInstance() {
		if ( null == _instance ) {
			_instance = new UrlManager();
			_instance.init();
		}
		return _instance; 
	}

	public Url popUrl() {
		if ( _urlList.empty() ) {
			return null;
		}

		Url url = (Url)_urlList.pop();
		if ( null == url ) {
			return null;
		}
		
		Stat.getInstance().getWaitingUrlCount().decr();
		
		return url;
	}

	public Url popImg() {
		if ( _imgList.empty() ) {
			return null;
		}

		Url url = (Url)_imgList.pop();
		if ( null == url ) {
			return null;
		}

		Stat.getInstance().getWaitingImgCount().decr();

		return url;
	}

	public void addUrl( Url url ) {
		addUrl( url, false );
	}
	public void addUrl( Url url, boolean force ) {
		if ( !_sameSrc.isEmpty() ) {
			if ( -1 == url.getUrl().indexOf( _sameSrc ) ) {
				return ;
			}
		}

		if ( !force ) {
			if ( _mapUsedUrl.setnx( url.getUrl(), url ) ) {
				return ;
			}
		} else {
			_mapUsedUrl.set( url.getUrl(), url );
		}

		_urlList.push(url);

		Stat.getInstance().getWaitingUrlCount().incr();
	}

	public void addImg( Url url ) {
		addImg( url, false );
	}
	public void addImg( Url url, boolean force ) {
		if ( !force ) {
			if ( _mapUsedUrl.setnx( url.getUrl(), url ) ) {
				return ;
			}
		} else {
			_mapUsedUrl.set( url.getUrl(), url );
		}

		_imgList.push(url);

		Stat.getInstance().getWaitingImgCount().incr();
	}

	public long getNextSn() {
		_snLocker.lock();
		long r = ++_sn;
		_snLocker.unlock();

		return r;
	}

	public void print() {
		GlobalManager.getInstance().log_run( "url:" );
		ArrayList<Object> urlList = _urlList.copy();
		for ( int i=0; i<urlList.size(); ++i ) {
			Url url = (Url)urlList.get(i);
			GlobalManager.getInstance().log_run( url.getUrl() );
		}

		GlobalManager.getInstance().log_run( "img:" );
		ArrayList<Object> imgList = _imgList.copy();
		for ( int i=0; i<imgList.size(); ++i ) {
			Url url = (Url)imgList.get(i);
			GlobalManager.getInstance().log_run( url.getUrl() );
		}
	}

	public void setSameSource( final String url ) {
		int len = Url.HTTP_HEADER_SIZE;
		int pos = url.indexOf( Url.HTTP_HEADER );
		if ( -1 == pos ) {
			len = Url.HTTPS_HEADER_SIZE;
			pos = url.indexOf( Url.HTTPS_HEADER );
		}

		String tmp = "";
		if ( -1 != pos ) {
			tmp = url.substring( len );
		} else {
			tmp = url;
		}
		GlobalManager.getInstance().log_debug( "tmp: " + tmp );

		pos = tmp.indexOf( "/" );
		if ( -1 != pos ) {
			tmp = tmp.substring( 0, pos );
		}

		String[] v = tmp.split( "," );
		if ( 2 == v.length ) {
			_sameSrc = tmp;
		} else {
			for ( int i=0; i<v.length; ++i ) {
				if ( i > 1 ) {
					_sameSrc += ".";
				}
				_sameSrc += v[i];
			}
		}

		GlobalManager.getInstance().log_run( "sameSrc: " + _sameSrc );
	}

	public void loadUrl( final String path ) {
		try {
			FileInputStream fis = new FileInputStream( path );
			InputStreamReader isr = new InputStreamReader( fis );
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while( null != ( line = br.readLine() ) ) {
				Url url = new Url();
				if ( url.load(line) ) {
					addUrl(url);
				} else {
					GlobalManager.getInstance().log_error( "load img:" + line + " failed" );
					url = null;
				}
			}

			br.close();
			isr.close();
			fis.close();
		} catch( Exception e ) {
		}
	}
	public void loadImg( final String path ) {
		try {
			FileInputStream fis = new FileInputStream( path );
			InputStreamReader isr = new InputStreamReader( fis );
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			while( null != ( line = br.readLine() ) ) {
				Url url = new Url();
				if ( url.load(line) ) {
					addImg(url);
				} else {
					GlobalManager.getInstance().log_error( "load img:" + line + " failed" );
					url = null;
				}
			}

			br.close();
			isr.close();
			fis.close();
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		}
	}

	public void saveUrl( final String path ) {
		try {
			FileWriter fw = new FileWriter( path );
			ArrayList<Object> v = _urlList.popAll();
			for ( int i=0; i<v.size(); ++i ) {
				Url url = (Url)v.get(i);
				fw.write( url.serial() + "\n" );
			}
			fw.close();
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		}
	}

	public void saveImg( final String path ) {
		try {
			FileWriter fw = new FileWriter( path );
			ArrayList<Object> v = _imgList.popAll();
			for ( int i=0; i<v.size(); ++i ) {
				Url url = (Url)v.get(i);
				fw.write( url.serial() + "\n" );
			}
			fw.close();
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		}
	}


	private void init() {
	}


}
