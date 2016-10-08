package com.wokdsem.kinject.support;

import java.util.HashMap;
import java.util.Map;

public class Locker<K> {

	private final Map<K, Lock> locks;

	public Locker() {
		locks = new HashMap<>();
	}

	public void lock(K key) throws InterruptedException {
		Lock lock;
		synchronized (this) {
			lock = locks.get(key);
			if (lock == null) {
				lock = new Lock();
				locks.put(key, lock);
			}
			lock.appendSubscriber();
		}
		lock.acquire();
	}

	public synchronized void release(K key) {
		Lock lock = locks.get(key);
		if (lock != null) {
			if (lock.release()) {
				locks.remove(key);
			}
		}
	}

	private static class Lock {

		private int subscribers = 0;
		private int actives = 0;

		synchronized void appendSubscriber() {
			++subscribers;
		}

		synchronized void acquire() throws InterruptedException {
			if (++actives > 1) {
				wait();
			}
		}

		synchronized boolean release() {
			if (--actives > 0) {
				notify();
			}
			return --subscribers == 0;
		}

	}

}
