// FileName: Counter.java
//
// Desc:
//
// Created by token.tong at 2019-05-13 16:13:33

public class Counter {
	private long _count = 0;
	private String _s = "";
	Locker _locker = new Locker();

	public void incr() {
		try {
			_locker.lock();
			++_count;
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		} finally {
			_locker.unlock();
		}
	}

	public void decr() {
		try {
			_locker.lock();
			--_count;
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		} finally {
			_locker.unlock();
		}
	}

	public void set( long count ) {
		try {
			_locker.lock();
			_count = count;
		} catch( Exception e ) {
			GlobalManager.getInstance().get_except().log_exception(e);
		} finally {
			_locker.unlock();
		}
	}

	public long get() { return _count; }

	public String getString() {
		_s = "" + _count;
		return _s; 
	}
}
