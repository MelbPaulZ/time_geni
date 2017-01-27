package org.unimelb.itime.util;

import java.util.regex.Pattern;

/**
 * Created by 37925 on 2016/12/8.
 */

public class ContactCheckUtil {
    private static ContactCheckUtil instance;
    Pattern emailPattern;
    private ContactCheckUtil(){
        String str="^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,})+)$";
        emailPattern = Pattern.compile(str);
    }

    public static ContactCheckUtil getInsstance(){
        if(instance == null){
            instance = new ContactCheckUtil();
        }
        return instance;
    }

    public boolean isUnimelbEmail(String input){
        String email = input.trim();
        return isEmail(email)&&email.toLowerCase().trim().endsWith("unimelb.edu.au");
    }

    public boolean isEmail(String input){
        String email = input.trim();
        return emailPattern.matcher(email).matches();
    }

    public boolean isPhone(String str){
        return false;
    }
}
