// FileName: LogManager.java
//
// Desc:
//
// Created by token.tong at 2019-05-13 17:15:57

import java.io.FileWriter;
import java.io.IOException;
import java.lang.Throwable;
import java.lang.StackTraceElement;

public class LogManager {
	String _file = "";
	String _dir = "";
	boolean _debug = false;

	public LogManager( final String file, boolean debug ) {
		_file = file;
		_debug = debug;
	}

	public boolean canOutput() {
		return ( !_debug || ( _debug && GlobalManager.getInstance().isDebug()) );
	}

	public void log( final String data ) {
		if ( GlobalManager.getInstance().isDaemon() ) {
			if ( canOutput() ) {
				if ( _dir.isEmpty() ) {
					_dir = GlobalManager.getInstance().getCurDir();
					_file = _dir + "/" + _file;
				}
				try {
					FileWriter fp = new FileWriter( _file, true );
					fp.write( data );
					fp.write( "\n" );
					fp.close();
				} catch( IOException e ) {
					e.printStackTrace();
				}
			}
		} else {
			if ( canOutput() ) {
				System.out.println( data );
			}
		}
	}

	public void log_exception( Throwable t ) {
		log( t.toString() );
		StackTraceElement[] tr = t.getStackTrace();
		for ( int i=0; i<tr.length; ++i ) {
			log( "    " + tr[i].toString() );
		}
	}
}
