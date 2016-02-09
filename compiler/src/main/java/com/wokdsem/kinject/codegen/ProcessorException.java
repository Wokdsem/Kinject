package com.wokdsem.kinject.codegen;

class ProcessorException extends Exception {

	public ProcessorException(String message, Object... args) {
		super(String.format(message, args));
	}

}
