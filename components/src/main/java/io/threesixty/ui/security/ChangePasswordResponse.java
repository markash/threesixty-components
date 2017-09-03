package io.threesixty.ui.security;

import java.io.Serializable;


/**
 * 
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class ChangePasswordResponse<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private ChangePasswordResult result;
	private T value;

	public ChangePasswordResponse(ChangePasswordResult result) {
		super();
		this.result = result;
		this.value = null;
	}

	public ChangePasswordResponse(ChangePasswordResult result, T value) {
		super();
		this.result = result;
		this.value = value;
	}
	
	public ChangePasswordResult getResult() { return result; }
	public T getValue() { return value; }


	public enum ChangePasswordResult {

		OK(200, "OK"),
		UNAUTHORIZED(401, "Unauthorized");


		private int code;
		private String description;

		ChangePasswordResult(final int code, final String description) {
			this.code = code;
			this.description = description;
		}

		public int getCode() { return code; }
		public String getDescription() { return description; }
		public ChangePasswordResult description(String description) { this.description = description; return this; }
	}
}
