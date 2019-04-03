package com.sistec.helperClasses;

public interface RemoteServiceUrl {
    public static final String SERVER_URL = "http://sistecstudent.000webhostapp.com/";

    public static interface METHOD_NAME {
        String LOGIN = "login.php";
        String BASIC_DTL = "basic_dtl.php";
        String ACADEMICS = "academics.php";
        String ATTENDANCE = "attendance.php";
        String FEE_STATUS = "feeStatus.php";
        String FETCH_SUBJECTS = "fetchSubjects.php";
        String ASSIGNMENTS = "fetchAssignments.php";
        String MIDSEM_MARKS = "fetchMarks.php";
        String CHANGE_PASS = "change_pass.php";
        String APP_INFO = "app_info.php";
    }

    public static interface SHARED_PREF {
        String USER_FILE_NAME = "user";
        String LOGIN_STATUS_FILE_NAME = "login";
        String ENROLL_PREF_KEY = "enroll_pref_key";
        String NAME_PREF_KEY = "name_pref_key";
        String SEM_PREF_KEY = "sem_pref_key";
        String BRANCH_PREF_KEY = "branch_pref_key";
        String STREAM_PREF_KEY = "stream_pref_key";
        String PASSWORD_PREF_KEY = "password_pref_key";
        String IS_LOGIN_PREF_KEY = "is_login";
    }
}