package com.wokdsem.kinject.core;

import java.util.HashMap;

class Blocker {
	
	private final HashMap<String, Lock> locks;
	
	Blocker() {
		locks = new HashMap<>();
	}
	
	void block(String key) throws InterruptedException {
		Lock lock;
		synchronized (this) {
			lock = locks.get(key);
			if (lock == null) {
				lock = new Lock();
				locks.put(key, lock);
			}
			lock.subscribers++;
		}
		lock.acquire();
	}
	
	void release(String key) {
		Lock lock;
		synchronized (this) {
			lock = locks.get(key);
		}
		lock.release();
		synchronized (this) {
			if (--lock.subscribers == 0) {
				locks.remove(key);
			}
		}
	}
	
	private static class Lock {
		
		int subscribers = 0;
		private int actives = 0;
		
		synchronized void acquire() throws InterruptedException {
			if (++actives > 1) {
				wait();
			}
		}
		
		synchronized void release() {
			if (--actives > 0) {
				notify();
			}
		}
		
	}
	
}
