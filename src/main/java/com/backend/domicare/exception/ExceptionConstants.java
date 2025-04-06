package com.backend.domicare.exception;


public enum ExceptionConstants {

	INVALID_AUTHENTICATED(102, "invalid_authentication"),

	INTERNAL_SERVER_ERROR(103, "internal_server_error"),

	INVALID_SYNTAX(104, "invalid_syntax"),

	EMAIL_ALREADY_EXISTS(108, "email_already_exists"),

	USER_ID_NOT_EXISTS( 112, "not_found_user_id"),

	UNAUTHORIZED(113,"unauthorized"),

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

	CATEGORY_ALREADY_EXISTS(124, "category_already_exists"),

	CATEGORY_NOT_FOUND(125, "category_not_found"),

	PRODUCT_NOT_IN_CATEGORY(126, "product_not_in_category"),

	ROLE_ALREADY_EXISTS(127, "role_already_exists"),

	NOT_FOUND_ROLE(128, "not_found_role"),

	ALREADY_REVIEWED(129, "already_reviewed");

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
