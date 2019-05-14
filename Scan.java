// FileName: Scan.java
//
// Desc:
//
// Created by token.tong at 2019-05-13 11:51:27

import java.lang.Runnable;
import java.lang.Thread;

public class Scan implements Runnable {
	private Thread _t = null;
	private boolean _stop = true;
	private boolean _stopped = true;

	public void start() {
		_stop = false;
		_t = new Thread(this);
		_t.start();
	}

	public void run() {
		_stopped = false;
		UrlManager mg = UrlManager.getInstance();

		Url url = null;
		while( !_stop ) {
			boolean needSleep = false;

			try {
				do {
					if ( null != ( url = mg.popImg() ) ) {
						Stat.getInstance().getDownloadingImgCount().incr();
						downloadImg( mg, url );
						Stat.getInstance().getDownloadingImgCount().decr();
						Stat.getInstance().getDownloadedImgCount().incr();
						break;
					}

					if ( null != ( url = mg.popUrl() ) ) {
						Stat.getInstance().getDownloadingUrlCount().incr();
						downloadWeb( mg, url );
						Stat.getInstance().getDownloadingUrlCount().decr();
						Stat.getInstance().getDownloadedUrlCount().incr();
						break;
					}

					needSleep = true;
				} while(false);

				if ( needSleep ) {
					Thread.sleep(1000);
				}
			} catch( Exception e ) {
				GlobalManager.getInstance().get_except().log_exception(e);
			}
		}

		_stopped = true;
	}

	public void stop() {
		_stop = true;
	}

	public boolean isStopped() {
		return _stopped; 
	}

	public void waitForStopping() {
		if ( null != _t )
		{
			try {
				_t.join();
			} catch( InterruptedException e ) {
				GlobalManager.getInstance().get_except().log_exception(e);
			}
			_t = null;
		}
	}

	void downloadImg( UrlManager mg, Url url ) {
		HttpRequest rq = new HttpRequest();
		HttpResponse res = rq.get( url.getUrl() );
		if ( null != res ) {
			if ( 200 != res.getCode() ) {
				GlobalManager.getInstance().log_debug( res.getCode() + ": " + res.getRequest().getOriginUrl() );
				if ( 301 == res.getCode() && res.hasHeader( "Location" ) ) {
					Url o = new Url(url);
					o.setUrl( res.getHeader( "Location" ) );
					mg.addImg(o);
				}
			} else {
				UrlDelegate ud = UrlDelegate.getInstance( url.getFlag() );
				byte[] data = res.getBody();
				data = ud.parseHtml( url, data );
				ud.save( url, GlobalManager.getInstance().getImgDir(), data );
			}
		}
		else if ( rq.isTimeout() ) {
			mg.addImg( url, true );
		}
	}

	void downloadWeb( UrlManager mg, Url url ) {
		HttpRequest rq = new HttpRequest();
		HttpResponse res = rq.get( url.getUrl() );
		if ( null != res ) {
			if ( 200 != res.getCode() ) {
				GlobalManager.getInstance().log_debug( res.getCode() + ": " + res.getRequest().getOriginUrl() );
				if ( 301 == res.getCode() && res.hasHeader( "Location" ) ) {
					Url o = new Url(url);
					o.setUrl( res.getHeader( "Location" ) );
					mg.addUrl(o);
				}
			} else {
				UrlDelegate ud = UrlDelegate.getInstance( url.getFlag() );
				byte[] data = res.getBody();
				data = ud.parseHtml( url, data );
				ud.save( url, GlobalManager.getInstance().getHtmlDir(), data );
			}
		}
		else if ( rq.isTimeout() ) {
			mg.addUrl( url, true );
		}
	}
}
