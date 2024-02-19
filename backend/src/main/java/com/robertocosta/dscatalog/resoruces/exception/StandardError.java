package com.robertocosta.dscatalog.resoruces.exception;

import java.io.Serializable;
import java.time.Instant;

public class StandardError implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Instant timestamp;
    private Integer status;
    private String message;
    private String path;
    
    public StandardError() {
    }

	public StandardError(Instant timestamp, Integer status, String message, String path) {
		this.timestamp = timestamp;
		this.status = status;
		this.message = message;
		this.path = path;
	}


	public Instant getTimestamp() {
		return timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}
}
