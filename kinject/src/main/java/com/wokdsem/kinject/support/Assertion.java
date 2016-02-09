package com.wokdsem.kinject.support;

public class Assertion {

	public static <T> void assertNonNull(T nonNullable, String errMsg, Object... errArgs) {
		assertion(nonNullable == null, errMsg, errArgs);
	}

	public static void assertNonEmpty(String nonEmpty, String errMsg, Object... errArgs) {
		assertion(nonEmpty == null || nonEmpty.isEmpty(), errMsg, errArgs);
	}

	public static void assertion(boolean failCondition, String errMsg, Object... errArgs) {
		if (failCondition) {
			throw new IllegalArgumentException(String.format(errMsg, errArgs));
		}
	}

}
