package com.wokdsem.kinject.codegen;

class ProcessorException extends Exception {

	ProcessorException(String message, Object... args) {
		super(String.format(message, args));
	}

}
