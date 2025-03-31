package com.backend.domicare.exception;


public enum ExceptionConstants {

	INVALID_AUTHENTICATED(102, "invalid_authentication"),

	INTERNAL_SERVER_ERROR(103, "internal_server_error"),

	INVALID_SYNTAX(104, "invalid_syntax"),

	EMAIL_ALREADY_EXISTS(108, "email_already_exists"),

	USER_ID_NOT_EXISTS( 112, "not_found_user_id"),

	UNAUTHORIZED(113,"unauthorized"),

	ROLE_ID_NOT_EXISTS( 115, "not_found_role_id"),

	ADMIN_UNAUTHORIZED(114,"admin_unauthorized"),

	UNAUTHORIZED_ADMIN_DELETE_OTHER_ADMINS(113,"unauthorized_admin_delete_other_admins"),

	ORDER_ID_NOT_EXISTS(211, "not_found_order_id"),

    INVALID_REFRESH_TOKEN(116, "invalid_refresh_token"),

    NOT_FOUND_USER_ID( 112, "not_found_user_id"),

    NOT_FOUND_EMAIL( 117, "not_found_email"),

    EMAIL_NOT_COMFIRMED(118, "email_not_confirmed"),

    BAD_CREDENTIALS(119, "bad_credentials"),

	INVALID_EMAIL(120, "invalid_email"),

	PRODUCT_NAME_ALREADY_EXISTS(121, "product_name_already_exists"),
	
	NOT_FOUND_PRODUCT_ID(122, "not_found_product_id"),
	NOT_FOUND_CATEGORY_ID(123, "not_found_category_id");

	private final int code;
	private final String messageName;

	ExceptionConstants(int code, String messageName) {
		this.code = code;
		this.messageName = messageName;
	}

	public int getCode() {
		return code;
	}

	public String getMessageName() {
		return messageName;
	}
}
