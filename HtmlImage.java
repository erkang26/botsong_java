// FileName: HtmlImage.java
//
// Desc:
//
// Created by token.tong at 2019-05-14 16:09:48

import java.io.FileOutputStream;

public class HtmlImage extends UrlDelegate {

	static final int MIN_IMG_SIZE = 10240;

	public byte[] parseHtml( Url parentUrl, final byte[] data ) {
		return data;
	}

	public void save( Url url, final String dir, final byte[] data ) {
		try {
			if ( data.length >= MIN_IMG_SIZE ) {
				String filePath = dir + "/" + getImageName( url.getUrl() );
				FileOutputStream fw = new FileOutputStream( filePath );
				fw.write( data );
				fw.close();
			}
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		}
	}

	String getImageName( final String url ) {
		int n = url.lastIndexOf( '.' );
		String suffix = "";
		if ( -1 != n ) {
			suffix = url.substring( n );
			if ( suffix.length() > 6 ) {
				suffix = "";
			}
		}

		String file = String.format( "%09d", UrlManager.getInstance().getNextSn() );
		if ( suffix.isEmpty() ) {
			suffix = ".jpg";
		}
		file += suffix;
		return file;
	}
}
