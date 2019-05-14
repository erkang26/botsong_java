// FileName: Locker.java
//
// Desc:
//
// Created by token.tong at 2019-05-13 13:58:42

import java.util.concurrent.locks.ReentrantLock;

public class Locker {
	private final ReentrantLock _lock = new ReentrantLock();

	public void lock() {
		_lock.lock();
	}

	public void unlock() {
		_lock.unlock();
	}
}
