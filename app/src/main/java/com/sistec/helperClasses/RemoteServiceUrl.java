package com.sistec.helperClasses;

public interface RemoteServiceUrl {
    public static final String SERVER_URL = "http://sistecstudent.000webhostapp.com/";

    public static interface METHOD_NAME {
        String LOGIN = "login.php";
        String BASIC_DTL = "basic_dtl.php";
    }

    public static interface SHARED_PREF {
        String USER_FILE_NAME = "user";
        String LOGIN_STATUS_FILE_NAME = "login";
        String ENROLL_PREF_KEY = "enroll_pref_key";
        String PASSWORD_PREF_KEY = "password_pref_key";
        String IS_LOGIN_PREF_KEY = "is_login";
    }
}