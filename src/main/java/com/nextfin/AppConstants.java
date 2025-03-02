package com.nextfin;

public class AppConstants {
    public static final String API_VERSION = "v1";
    public static final String API_BASE_URL = "/api/" + API_VERSION;
    public static final String DEFAULT_CURRENCY_CODE = "EUR";
    public static final String VISA_REGEX_NO_DASH = "^4[0-9]{12}(?:[0-9]{3})?$";
    public static final String VISA_REGEX = "^4[0-9]{3}-?[0-9]{4}-?[0-9]{4}-?[0-9]{4}$";
    public static final String USER_PWD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";
}
