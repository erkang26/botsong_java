// FileName: UrlDelegate.java
//
// Desc:
//
// Created by token.tong at 2019-05-14 14:59:47

import java.util.HashMap;

public class UrlDelegate {
	
	static HashMap< Integer, UrlDelegate > _mapInst = new HashMap<>();
	
	public static UrlDelegate getInstance( int type ) {
		if ( _mapInst.containsKey(type) ) {
			return _mapInst.get(type);
		}

		UrlDelegate ret = null;
		switch( type ) {
			case Url.UT_COMMON:
				ret = new HtmlCommon();
				break;

			case Url.UT_IMAGE:
				ret = new HtmlImage();
				break;

			default:
				break;
		}

		if ( null != ret ) {
			_mapInst.put( type, ret );
		}

		return ret;
	}

	public byte[] parseHtml( Url parentUrl, final byte[] data ) {
		String s = "";
		return s.getBytes();
	}
	public void save( Url url, final String dir, final byte[] data ) {}
}
