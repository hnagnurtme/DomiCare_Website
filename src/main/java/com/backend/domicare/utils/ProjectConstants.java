package com.backend.domicare.utils;

import java.util.Locale;

public final class ProjectConstants {

	public static final String DEFAULT_ENCODING = "UTF-8";

	public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

	public static final String ROLE_ADMIN = "ROLE_ADMIN";

	public static final String ROLE_USER = "ROLE_USER";

	public static final String ROLE_SALE = "ROLE_SALE";

	public static final String DEFAULT_ROLE = "ROLE_USER";

	public static final String DEFAULT_AVATAR = "https://res.cloudinary.com/drtizu6d3/image/upload/v1742485551/jw7da5qpbc2gip1qc2bo.jpg";

	private ProjectConstants() {

		throw new UnsupportedOperationException();
	}

}
