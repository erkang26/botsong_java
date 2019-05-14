// FileName: LogBody.java
//
// Desc:
//
// Created by token.tong at 2019-05-13 17:39:24

public class LogBody {

	String _file;
	String _data;

	public LogBody( final String file, final String data ) {
		_file = file;
		_data = data;
	}

	public String getFile() {
		return _file; 
	}

	public String getData() {
		return _data;
	}
}
