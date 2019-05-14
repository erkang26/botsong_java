// FileName: HtmlCommon.java
//
// Desc:
//
// Created by token.tong at 2019-05-14 15:13:39

import java.io.FileOutputStream;

public class HtmlCommon extends UrlDelegate {

	public byte[] parseHtml( Url parentUrl, final byte[] data ) {
		GlobalManager.getInstance().log_debug( "html common parse" );
		parseWebUrl( parentUrl.getUrl(), new String(data) );
		parseImgUrl( parentUrl.getUrl(), new String(data) );
		return data;
	}

	public void save( Url url, final String dir, final byte[] data ) {
		String htmlFile = dir + "/";
		htmlFile += getFileByUrl( url.getUrl() );
		htmlFile += ".html";

		try
		{
			FileOutputStream fw = new FileOutputStream( htmlFile );
			fw.write(data);
			fw.close();
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		}
	}

	String getFileByUrl( final String url ) {
		String tmp = url;
		if ( 0 == tmp.indexOf( Url.HTTP_HEADER ) ) {
			tmp = tmp.substring( Url.HTTP_HEADER_SIZE );
		} else if ( 0 == tmp.indexOf( Url.HTTPS_HEADER ) ) {
			tmp = tmp.substring( Url.HTTPS_HEADER_SIZE );
		}

		tmp = tmp.trim();

		return tmp;
	}

	void parseWebUrl( final String domainUrl, final String data ) {

		GlobalManager.getInstance().log_debug( data );
		final String tag = "href=";
		int n = 0;
		int m = 0;
		n = data.indexOf( tag, m );
		while( -1 != n && n <= data.length() ) {
			n+= tag.length();
			int np = 0;
			if ( '\"' == data.charAt(n) ) {
				++n;
				np = data.indexOf( "\"", n );
			} else {
				int np1 = data.indexOf( ">", n );
				int np2 = data.indexOf( " ", n );
				if ( -1 == np1 ) {
					np = np2;
				} else if ( -1 == np2 ) {
					np = np1;
				} else {
					if ( np1 <= np2 ) {
						np = np1;
					} else {
						np = np2;
					}
				}
			}

			if ( -1 != np ) {
				String url = data.substring( n, np );
				m = np+1;
				Url u = new Url();
				u.setUrl( domainUrl, url );
				u.setFlag( Url.UT_COMMON );
				UrlManager.getInstance().addUrl(u);
			} else {
				m = n;
			}

			n = data.indexOf( tag, m );
		}
	}

	void parseImgUrl( final String domainUrl, final String data ) {
		final String tag = "<img ";
		final String att1 = "src=\"";
		final String att2 = "lazy-src=\"";

		int n = 0;
		int m = 0;
		int n0 = 0;
		n0 = data.indexOf( tag, m );
		while( -1 != n0 && n0 <= data.length() ) {
			n0 += tag.length();
			int n2 = data.indexOf( att2, n0 );
			if ( -1 == n2 || n2 > data.indexOf( ">", n0 ) ) {
				n = data.indexOf( att1, n0 );
				if ( -1 == n ) {
					m = n0;
					continue;
				} else {
					n += att1.length();
				}
			} else {
				n += n2 + att2.length();
			}

			int np = data.indexOf( "\"", n );
			if ( -1 != np ) {
				String url = data.substring( n, np );
				Url u = new Url();
				u.setUrl( domainUrl, url );
				u.setFlag( Url.UT_IMAGE );
				UrlManager.getInstance().addImg(u);

				m = np + 1;
			} else {
				m = n;
			}

			n0 = data.indexOf( tag, m );
		}
	}
}
