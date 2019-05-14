// FileName: Stat.java
//
// Desc:
//
// Created by token.tong at 2019-05-13 16:29:27

import java.lang.Runnable;
import java.lang.Thread;

public class Stat implements Runnable {

	static Stat _instance = null;

	Counter _waitingUrlCount = new Counter();
	Counter _waitingImgCount = new Counter();
	Counter _downloadingUrlCount = new Counter();
	Counter _downloadingImgCount = new Counter();
	Counter _downloadedUrlCount = new Counter();
	Counter _downloadedImgCount = new Counter();

	Thread _t = null;
	boolean _stop = true;
	boolean _stopped = true;
	Locker _locker = new Locker();

	public static Stat getInstance() {
		if ( null == _instance ) {
			_instance = new Stat();
		}

		return _instance; 
	}

	public void start() {
		_stop = false;
		_t = new Thread(this);
		_t.start();
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

	public void run() {
		int SLEEP = 1000;
		_stopped = false;
		while( !_stop ) {

			GlobalManager.getInstance().log_stat( "" );
			GlobalManager.getInstance().log_stat( "waiting url: " + _waitingUrlCount.get() );
			GlobalManager.getInstance().log_stat( "waiting img: " + _waitingImgCount.get() );
			GlobalManager.getInstance().log_stat( "downloading url: " + _downloadingUrlCount.get() );
			GlobalManager.getInstance().log_stat( "downloading img: " + _downloadingImgCount.get() );
			GlobalManager.getInstance().log_stat( "downloaded url: " + _downloadedUrlCount.get() );
			GlobalManager.getInstance().log_stat( "downloaded img: " + _downloadedImgCount.get() );
			GlobalManager.getInstance().log_stat( "" );
			try {
				Thread.sleep(SLEEP);
			} catch( InterruptedException e ) {
				GlobalManager.getInstance().get_except().log_exception(e);
			}
		}
	}


	public Counter getWaitingUrlCount() { return _waitingUrlCount; }
	public Counter getWaitingImgCount() { return _waitingImgCount; }
	public Counter getDownloadingUrlCount() { return _downloadingUrlCount; }
	public Counter getDownloadingImgCount() { return _downloadingImgCount; }
	public Counter getDownloadedUrlCount() { return _downloadedUrlCount; }
	public Counter getDownloadedImgCount() { return _downloadedImgCount; }
}
