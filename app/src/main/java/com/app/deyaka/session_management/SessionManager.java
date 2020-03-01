package com.app.deyaka.session_management;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {

    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "SessionData";
    private static final String KID_ID = "kid_id";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String mobilekey = "mobileKey";
    public static final String countrycodekey = "countrycodeKey";

    public static final String mobile_no = "mobile";
    public static final String country_code = "countrycode";
    public static final String user_id = "userid";
    public static final String device_id = "deviceid";
    public static final String otp_token_id = "otptokenid";

    public static final String TOKEN = "token";
    public static final String SCHL_NAME = "school_name";

    public static final String PAY_EMAIL = "email";
    public static final String PAY_MOBILE_NO = "mobile_no";
    public static final String PAY_PARENT_NAME = "parent_name";

    public static final String PAY_INSTALL_DETAIL_ID = "install_detail";
    public static final String PAY_FEE_TITLE = "fee_title";
    public static final String PAY_STUDENT_ID = "student_id";
    public static final String PAY_AMOUNT = "pay_amount";
    public static final String PAY_MODE = "pay_mode";
    public static final String KID_NAME = "kid_name";
    public static final String KID_SCHL_NAME = "kid_school_name";

    public static final String OTP_MOBILE = "otp_mobile_num";
    public static final String OTP_C_CODE = "otp_country_code";
    public static final String OTP_PASS = "otp_passwrd";

    public SessionManager(Context context) {
        this._context = context;
        sPref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sPref.edit();
    }

    public boolean isLoggedIn() {
        return sPref.getBoolean(IS_LOGIN, false);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
    //resend otp -----------signin---
    public void saveDataResendOTP(String mobile, String countrycode, String password) {
        editor.putString(OTP_MOBILE, mobile);
        editor.putString(OTP_C_CODE, countrycode);
        editor.putString(OTP_PASS, password);
        editor.commit();
    }
    public String getOTPMobile() {
        return sPref.getString(OTP_MOBILE, null);
    }
    public String getOTPCCode() {
        return sPref.getString(OTP_C_CODE, null);
    }
    public String getOTPPass() {
        return sPref.getString(OTP_PASS, null);
    }
    //-------------------------------

    public void putKidName(String kid_name) {
        editor.putString(KID_NAME, kid_name);
        editor.commit();
    }
    public void putKidSchlName(String kid_school_name) {
        editor.putString(KID_SCHL_NAME, kid_school_name);
        editor.commit();
    }

    public String getKidName() {
        return sPref.getString(KID_NAME, null);
    }
    public String getKidSchlName() {
        return sPref.getString(KID_SCHL_NAME, null);
    }

    public void putKidId(String kid_id) {
        editor.putString(KID_ID, kid_id);
        editor.commit();
    }

    public String getKidId() {
        return sPref.getString(KID_ID, null);
    }

    public void createPref(String mobile, String countrycode) {
        editor.putString(mobilekey, mobile);
        editor.putString(countrycodekey, countrycode);
        editor.commit();
    }

    public String getMobileNumber() {
        return sPref.getString(mobilekey, null);
    }

    public String getCountryCode() {
        return sPref.getString(countrycodekey, null);
    }

    public void createOtpPref(String mobile, String countrycode, String userid, String deviceid, String tokenid) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(mobile_no, mobile);
        editor.putString(country_code, countrycode);
        editor.putString(user_id, userid);
        editor.putString(device_id, deviceid);
        editor.putString(otp_token_id, tokenid);
        editor.commit();
    }

    public String getOtpMobileNo() {
        return sPref.getString(mobile_no, null);
    }

    public String getOtpCountryCode() {
        return sPref.getString(country_code, null);
    }

    public String getUserId() {
        return sPref.getString(user_id, null);
    }

    public String getOtpDeviceId() {
        return sPref.getString(device_id, null);
    }

    public String getOtpTokenId() {
        return sPref.getString(otp_token_id, null);
    }

    public void createToken(String token) {
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return sPref.getString(TOKEN, null);
    }

    public void putSchoolName(String schl_name) {
        editor.putString(SCHL_NAME, schl_name);
        editor.commit();
    }

    public String getSchoolName() {
        return sPref.getString(SCHL_NAME, null);
    }

    public void payDetails(String email) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(PAY_EMAIL, email);
        editor.commit();
    }

    public String getPayEmail() {
        return sPref.getString(PAY_EMAIL, null);
    }

    public void payDetailsMobile(String mobile_no1) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(PAY_MOBILE_NO, mobile_no1);
        editor.commit();
    }

    public String getPayMobileNo() {
        return sPref.getString(PAY_MOBILE_NO, null);
    }

    public void payDetailsParentName(String parent_name) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(PAY_PARENT_NAME, parent_name);
        editor.commit();
    }

    public String getPayParentName() {
        return sPref.getString(PAY_PARENT_NAME, null);
    }

    public void saveOnlinePayDetails(String feeInstallmentDetailId, String feeTitle,
                                     String studentId, String amount, String paymentMode) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(PAY_INSTALL_DETAIL_ID, feeInstallmentDetailId);
        editor.putString(PAY_FEE_TITLE, feeTitle);
        editor.putString(PAY_STUDENT_ID, studentId);
        editor.putString(PAY_AMOUNT, amount);
        editor.putString(PAY_MODE, paymentMode);
        editor.commit();
    }
    public String getPayInstallDetailId() {
        return sPref.getString(PAY_INSTALL_DETAIL_ID, null);
    }
    public String getPayFeeTitle() {
        return sPref.getString(PAY_FEE_TITLE, null);
    }
    public String getPayStudentId() {
        return sPref.getString(PAY_STUDENT_ID, null);
    }
    public String getPayAmount() {
        return sPref.getString(PAY_AMOUNT, null);
    }
    public String getPayMode() {
        return sPref.getString(PAY_MODE, null);
    }


}
