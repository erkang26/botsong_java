// FileName: GlobalManager.java
//
// Desc:
//
// Created by token.tong at 2019-05-13 17:20:31

public class GlobalManager {

	static GlobalManager _instance = null;

	boolean _debug = false;
	boolean _daemon = false;
	String _curDir = "";
	String _imgDir = "";
	String _htmlDir = "";

	LogManager _log_debug = new LogManager( "debug.log", true );
	LogManager _log_error = new LogManager( "error.log", false );
	LogManager _log_stat = new LogManager( "stat.log", false );
	LogManager _log_run = new LogManager( "run.log", false );
	LogManager _log_except = new LogManager( "except.log", false );

	public static GlobalManager getInstance() {
		if ( null == _instance ) {
			_instance = new GlobalManager();
		}

		return _instance;
	}

	public void setDebug( boolean debug ) {
		_debug = debug; 
	}
	public boolean isDebug() {
		return _debug;
	}

	public void setDaemon( boolean d ) {
		_daemon = d;
	}
	public boolean isDaemon() {
		return _daemon;
	}

	public void setCurDir( final String d ) {
		_curDir = d;
	}
	public String getCurDir() {
		return _curDir;
	}

	public void setImgDir( final String d ) {
		_imgDir = d;
	}
	public String getImgDir() {
		return _imgDir;
	}

	public void setHtmlDir( final String d ) {
		_htmlDir = d;
	}
	public String getHtmlDir() {
		return _htmlDir;
	}

	public void log_debug( final String data ) {
		_log_debug.log( data );
	}

	public void log_error( final String data ) {
		_log_error.log( data );
	}

	public void log_stat( final String data ) {
		_log_stat.log( data );
	}

	public void log_run( final String data ) {
		_log_run.log( data );
	}

	public void log_except( final String data ) {
		_log_except.log( data );
	}

	public LogManager get_except() { return _log_except; }
}
