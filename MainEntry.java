// FileName: MainEntry.java
//
// Desc:
//
// Created by token.tong at 2019-05-08 17:35:37

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URLConnection;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class MainEntry {
	public static void main( String[] args ) {
		try {
			File f = new File("");
			GlobalManager gm = GlobalManager.getInstance();
			gm.setCurDir( f.getAbsolutePath() );

			if ( 0 == args.length ) {
				gm.log_run( "usage: MainEntry url [-s] [url] [url] ..." );
				return ;
			}

			UrlManager mg = UrlManager.getInstance();
			boolean sameSrc = false;
			Url url = null;
			boolean loaded = false;
			for ( int i=0; i<args.length; ++i ) {
				gm.log_debug( args[i] );
				if ( args[i].equals( "-s" ) ) {
					sameSrc = true;
				} else if ( args[i].equals( "-d" ) ) {
					gm.setDebug(true);
				} else if ( args[i].equals( "-l" ) ) {
				} else if ( args[i].equals( "-daemon" ) ) {
					gm.setDaemon(true);
				} else {
					url = new Url();
					url.setUrl( args[i] );
					url.setFlag( Url.UT_COMMON );
				}
			}

			if ( !loaded ) {
				if ( null != url ) {
					mg.addUrl( url );
				} else {
					gm.log_run( "usage: MainEntry url [-s] [url] [url] ..." );
					return ;
				}
			}

			if ( sameSrc && null != url ) {
				mg.setSameSource( url.getUrl() );
			}

			createDir();

			int threadNum = 20;

			Stat.getInstance().start();

			ArrayList<Scan> vScan = new ArrayList<>();
			for ( int i=0; i<threadNum; ++i ) {
				Scan s = new Scan();
				s.start();
				vScan.add(s);
			}

			Thread.sleep( 1000*60*60*24*365 );

		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		}
	}

	static void createDir() {
		GlobalManager gm = GlobalManager.getInstance();
		String rootDir = gm.getCurDir() + "/" + getDateTimeString();
		{
			File f = new File(rootDir);
			f.mkdirs();
		}

		String imgDir = rootDir + "/img";
		{
			File f = new File(imgDir);
			f.mkdirs();
			gm.setImgDir( imgDir );
		}

		String htmlDir = rootDir + "/html";
		{
			File f = new File(htmlDir);
			f.mkdirs();
			gm.setHtmlDir( htmlDir );
		}


	}

	static String getDateTimeString(){
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmss" );
		return sdf.format(dt);
	}
}
