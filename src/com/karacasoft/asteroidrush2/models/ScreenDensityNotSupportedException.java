package com.karacasoft.asteroidrush2.models;

public class ScreenDensityNotSupportedException extends Exception {

	private static final long serialVersionUID = 3624744241134641778L;

	public ScreenDensityNotSupportedException() {
		
	}

	public ScreenDensityNotSupportedException(String detailMessage) {
		super(detailMessage);
		
	}

	public ScreenDensityNotSupportedException(Throwable throwable) {
		super(throwable);
		
	}

	public ScreenDensityNotSupportedException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
		
	}

}
