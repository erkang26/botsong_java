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

public class MainEntry {
	public static void main( String[] args ) {
		try {
			URL url = new URL( "https://www.baidu.com" );
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout( 10000 );
			conn.setReadTimeout( 60000 );
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader( is, "UTF-8" );
			BufferedReader br = new BufferedReader( isr );
			String data = br.readLine();
			while( null != data ) {
				System.out.println( data );
				data = br.readLine();
			}

			br.close();
			isr.close();
			is.close();

			Url tmp = new Url();
			tmp.setUrl( "xxx" );
			System.out.println( tmp.getUrl() );
		} catch( Exception e ) {
		}
	}
}
