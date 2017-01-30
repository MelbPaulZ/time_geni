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

    /**
     * Ellipsize email more than 12 letters to 9 letters plus "..."
     * @param input
     * @return
     */
    public String ellipsizeEmail(String input){
        return ellipsizeEmail(input, 12);
    }

    /**
     * Ellipsize email to length len plus "..."
     * @param input
     * @param len
     * @return
     */
    public String ellipsizeEmail(String input, int len){
        if(input.length()>len){
            return input.substring(0, len-3)+"...";
        }else{
            return input;
        }

    }
}
