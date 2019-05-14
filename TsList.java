// FileName: TsList.java
//
// Desc:
//
// Created by token.tong at 2019-05-13 14:19:31

import java.util.ArrayList;

public class TsList {
	private ArrayList<Object> _list = new ArrayList<>();
	Locker _locker = new Locker();

	public void push( Object value ) {
		try {
			_locker.lock();
			_list.add(value);
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		} finally {
			_locker.unlock();
		}
	}

	public Object pop() {
		Object ret = null;
		try {
			_locker.lock();
			do
			{
				if ( _list.isEmpty() ) {
					break;
				}
				ret = _list.get(0);
				_list.remove(0);
			} while(false);
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		} finally {
			_locker.unlock();
		}

		return ret;
	}

	public ArrayList<Object> popAll() {
		ArrayList<Object> ret = new ArrayList<>();
		try {
			_locker.lock();
			for ( int i=0; i<_list.size(); ++i ) {
				ret.add( _list.get(i) );
			}
			_list.clear();
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		} finally {
			_locker.unlock();
		}

		return ret;
	}

	public int size() {
		return _list.size(); 
	}

	public boolean empty() {
		return _list.isEmpty(); 
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Object> copy() {
		ArrayList<Object> r = null;
		try {
			_locker.lock();
			r = (ArrayList<Object>)_list.clone();
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		} finally {
			_locker.unlock();
		}

		return r;
	}
}
