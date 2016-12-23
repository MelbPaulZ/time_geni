package org.unimelb.itime.ui.contact.Model;

import java.util.regex.Pattern;

/**
 * Created by 37925 on 2016/12/8.
 */

public class ContactChecker {
    private static ContactChecker instance;
    Pattern emailPattern;
    private ContactChecker(){
        String str="^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$";
        emailPattern = Pattern.compile(str);
    }

    public static ContactChecker getInsstance(){
        if(instance == null){
            instance = new ContactChecker();
        }
        return instance;
    }

    public boolean isEmail(String email){
        return emailPattern.matcher(email).matches();
    }

    public boolean isPhone(String str){
        return false;
    }
}
