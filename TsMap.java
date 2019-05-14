// FileName: TsMap.java
//
// Desc:
//
// Created by token.tong at 2019-05-13 14:07:59

import java.util.HashMap;
import java.util.Map;

public class TsMap {
	private HashMap<String, Object> _mapData = new HashMap<>();
	private Locker _locker = new Locker();

	public boolean setnx( final String key, Object value ) {
		boolean ret = false;
		try {
			_locker.lock();
			do {
				if ( !_mapData.containsKey(key) ) {
					break;
				}

				_mapData.put( key, value );

				ret = true;

			} while(false);
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		} finally {
			_locker.unlock();
		}

		return ret;
	}

	public void set( final String key, Object value ) {
		try {
			_locker.lock();
			_mapData.put( key, value );
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		} finally {
			_locker.unlock();
		}
	}

	public Object get( final String key ) {
		Object ret = null;
		try {
			_locker.lock();
			do {
				if ( _mapData.containsKey(key) ) {
					ret = _mapData.get(key);
				}
			} while(false);
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		} finally {
			_locker.unlock();
		}

		return ret;
	}

	public int size() {
		return _mapData.size(); 
	}

	public boolean empty() {
		return _mapData.isEmpty(); 
	}

	@SuppressWarnings( "unchecked" )
	public Map<String, Object> copy() {
		Map<String, Object> r = null;
		try {
			_locker.lock();
			r = (HashMap<String,Object>)_mapData.clone();
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		} finally {
			_locker.unlock();
		}

		return r;
	}
}
