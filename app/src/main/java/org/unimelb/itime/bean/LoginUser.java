//package org.unimelb.itime.bean;
//
//import com.google.gson.Gson;
//
///**
// * Created by Paul on 23/12/2016.
// */
//
//public class LoginUser {
//
//    public static final String SOURCE_GOOGLE = "google";
//    public static final String SOURCE_FACEBOOK = "facebook";
//    public static final String SOURCE_ITIME = "itime";
//    public static final String SOURCE_EMAIL = "email";
//    public static final String SOURCE_PHONE = "phone";
//
//    private String userId ="";
//    private String password = "123456";
//    private String personalAlias ="";
//    private String email = "johncdyin@gmail.com";
//    private String phone = "";
//    private String source = "";
//    private String photo = "";
//
//    public LoginUser() {
//    }
//
//    public LoginUser(String userId, String password, String personalAlias, String email, String phone, String source, String photo) {
//        this.userId = userId;
//        this.password = password;
//        this.personalAlias = personalAlias;
//        this.email = email;
//        this.phone = phone;
//        this.source = source;
//        this.photo = photo;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getPersonalAlias() {
//        return personalAlias;
//    }
//
//    public void setPersonalAlias(String personalAlias) {
//        this.personalAlias = personalAlias;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getSource() {
//        return source;
//    }
//
//    public void setSource(String source) {
//        this.source = source;
//    }
//
//    public String getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(String photo) {
//        this.photo = photo;
//    }
//
//    public LoginUser getCopyLoginUser(){
//        LoginUser cpyLoginUser = new LoginUser(
//                this.userId,
//                this.password,
//                this.personalAlias,
//                this.email,
//                this.phone,
//                this.source,
//                this.photo
//        );
//        return cpyLoginUser;
//    }
//}
