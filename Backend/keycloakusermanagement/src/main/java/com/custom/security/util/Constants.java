package com.custom.security.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

	public static final String KEY_USER_NAME = "username";
	public static final String KEY_GRANT_TYPE = "grant_type";
	public static final String KEY_CLIENT_SECRET = "client_secret";
	public static final String KEY_CLIENT_ID = "client_id";
	public static final String KEY_PASSWORD = "password";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String REFRESH_TOKEN = "refresh_token";
	
	
}
