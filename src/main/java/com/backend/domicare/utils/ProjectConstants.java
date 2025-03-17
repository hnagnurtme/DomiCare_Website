package com.backend.domicare.utils;

import java.util.Locale;

public final class ProjectConstants {

	public static final String DEFAULT_ENCODING = "UTF-8";

	public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

	public static final String ROLE_ADMIN = "ROLE_ADMIN";

	public static final String ROLE_USER = "ROLE_USER";

	public static final String ROLE_SALE = "ROLE_SALE";

	public static final String DEFAULT_ROLE = "ROLE_USER";

	public static final String DEFAULT_AVATAR = "http://localhost:8080/files/DEFAULT";

	private ProjectConstants() {

		throw new UnsupportedOperationException();
	}

}
