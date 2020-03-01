package com.app.deyaka.retrofit;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("my_kids")
    Call<MyKids_RetroModel> getKidsList(@Body MyKids_RetroModel usr_id);

    @POST("ward_details")
    Call<WardDetails_RetroModel> getWardDetails(@Body WardDetails_RetroModel kid_id);

    @POST("student_fee_new")
    Call<Fees_RetroModel> getFeesList(@Body Fees_RetroModel kid_id);

    @POST("student_fee_details")
    Call<FeeDetails_RetroModel> getFeeDetails(@Body FeeDetails_RetroModel kid_id);

    @POST("student_other_fee_new")
    Call<OtherFees_RetroModel> getOtherFeesList(@Body OtherFees_RetroModel kid_id);

    @POST("news_events")
    Call<NewsEvents_RetrofitModel> getNewsEvents(@Body NewsEvents_RetrofitModel id);

    @POST("home_screen_images")
    Call<HomePageImg_RetroModel> getImages(@Body HomePageImg_RetroModel kid_id);

    @POST("student_installement_payment")
    Call<PaymentOption_RetroModel> makePayment(@Body PaymentOption_RetroModel data);

    @POST("payment_history_basedon_year")
    Call<PayHistory_RetroModel> showPayHistory(@Body PayHistory_RetroModel data);

    @POST("student_payament_status")
    Call<PayStatusTerms_RetroModel> checkPayStatus(@Body PayStatusTerms_RetroModel data);

    @POST("payment_years")
    Call<YearList_retroModel> getYearList(@Body YearList_retroModel data);

    //yogesh

    @POST("user_login")
    Call<LoginRequest> getLoginResult(@Body LoginRequest loginRequest);

    @POST("login_otpverify")
    Call<VerifyRequest> getVerifyOtpResult(@Body VerifyRequest verifyRequest);

    @POST("forgot_password")
    Call<ForgotPasswordRequest> getPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    @POST("signup_mobileno_check")
    Call<SignUpRequest> checkMobileNumber(@Body SignUpRequest signUpRequest);

    @POST("signup_mobileno_newpassword_new")
    Call<SignUpNewPasswordRequest> setNewPassword(@Body SignUpNewPasswordRequest signUpNewPasswordRequest);

    @POST("signup_mobileno_otpverify")
    Call<SignUpOtpVerifyRequest> verifyOtp(@Body SignUpOtpVerifyRequest SignUpOtpVerifyRequest);

    @POST("forgot_newpassword")
    Call<ForgotNewPasswordRequest> forgotNewPassword(@Body ForgotNewPasswordRequest forgotNewPasswordRequest);

    @POST("forgot_password_otpverify")
    Call<ForgotPasswordOtpVerify> forgotPasswordOtpVerify(@Body ForgotPasswordOtpVerify forgotPasswordOtpVerify);

    @POST("logout")
    Call<Logout_RetroModel> logout(@Body Logout_RetroModel data);


}
