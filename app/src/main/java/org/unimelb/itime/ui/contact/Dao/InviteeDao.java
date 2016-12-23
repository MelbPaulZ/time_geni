package org.unimelb.itime.ui.contact.Dao;

import org.unimelb.itime.ui.contact.Beans.BaseContact;
import org.unimelb.itime.ui.contact.Beans.ITimeContactInterface;
import org.unimelb.itime.ui.contact.Beans.ITimeUser;
import org.unimelb.itime.ui.contact.Beans.Invitee;
import org.unimelb.itime.ui.contact.Beans.RequestFriend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 37925 on 2016/12/4.
 */

public class InviteeDao {
    private String[] emails = new String[] {"123456@gmail.com", "379258670@gmail.com", "2345678@gmail.com", "lili5432@gmail.com", "123456@gmail.com", "379258670@gmail.com","123456@gmail.com", "379258670@gmail.com", "2345678@gmail.com", "lili5432@gmail.com", "123456@gmail.com", "379258670@gmail.com","123456@gmail.com", "379258670@gmail.com", "2345678@gmail.com", "lili5432@gmail.com", "123456@gmail.com", "379258670@gmail.com"};
    private String[] phones = new String[] {"0420123123", "0420234234", "0420345345", "0420678678","0420789789"};
    private String avatar = "http://common.cnblogs.com/images/wechat.png";
    private String[] names = {"Tom", "Alice", "Adel", "Mike", "John", "Jason", "Jay","Jim","Tom", "Alice", "Adel", "Mike", "John", "Jason", "Jay","Jim","Tom", "Alice", "Adel", "Mike", "John", "Jason", "Jay","Jim"};
    public List<BaseContact> getEmails(){
        ArrayList<BaseContact> result = new ArrayList<>();
        for(String str:emails){
            BaseContact in = new BaseContact();
            in.setContactId(str);
            result.add(in);
        }
        return result;
    }

    public List<BaseContact> getPhones(){
        ArrayList<BaseContact> result = new ArrayList<>();
        for(String str:phones){
            BaseContact in = new BaseContact();
            in.setContactId(str);
            result.add(in);
        }
        return result;
    }

    public List<BaseContact> getFriends(){
        ArrayList<BaseContact> result = new ArrayList<>();
        for(int i=0;i<15;i++){
            BaseContact in = new BaseContact();
            in.setContactId(emails[i]);
            in.setName(names[i]);
            in.setPhoto(avatar);
            result.add(in);
        }
        return result;
    }

    public List<RequestFriend> getRequestFriends(){
        ArrayList<RequestFriend> result = new ArrayList<>();
        RequestFriend inn = new RequestFriend();
        inn.setContactId(emails[6]);
        inn.setName(names[6]);
        inn.setPhoto(avatar);
//        inn.setAccepted(false);
//        result.add(inn);
//        for(int i=0;i<5;i++){
//            RequestFriend in = new RequestFriend();
//            in.setContactId(emails[i]);
//            in.setName(names[i]);
//            in.setPhoto(avatar);
//            in.setAccepted(true);
//            result.add(in);
//        }
        return result;
    }

    public List<ITimeContactInterface> getEmailInvitees(){
        ArrayList<ITimeContactInterface> result = new ArrayList<>();
        for(String str:emails){
            ITimeContactInterface in = new Invitee();
            in.setContactId(str);
            result.add(in);
        }
        return result;
    }

    public List<ITimeContactInterface> getPhoneInvitees(){
        ArrayList<ITimeContactInterface> result = new ArrayList<>();
        for(String str:phones){
            ITimeContactInterface in = new Invitee();
            in.setContactId(str);
            result.add(in);
        }
        return result;
    }

    public List<ITimeContactInterface> getFriendInvitees(){
        ArrayList<ITimeContactInterface> result = new ArrayList<>();
        for(int i=0;i<15;i++){
            ITimeContactInterface in = new Invitee();
            in.setContactId(emails[i]);
            in.setName(names[i]);
            in.setPhoto(avatar);
            result.add(in);
        }
        return result;
    }

    public ITimeUser findFirend(String str){
        ArrayList<ITimeUser> result = new ArrayList<>();
        for(int i=0;i<5;i++){
            ITimeUser in = new ITimeUser();
            in.setContactId(emails[i]);
            in.setName(names[i]);
            in.setPhoto(avatar);
            in.setPhone(phones[i]);
            in.setEmail(emails[i]);
            in.setSex("male");
            in.setCountry("Australia");
            in.setState("Melbourne");
            if(str.equals(in.getPhone())
                    ||str.equals(in.getEmail())){
                return in;
            }
        }
        return null;
    }

    public List<BaseContact> getGoogleContacts(){
        ArrayList<BaseContact> result = new ArrayList<>();
        for(int i=0;i<4;i++){
            BaseContact in = new BaseContact();
            in.setContactId(emails[i]);
            in.setName(names[i]);
            in.setPhoto(avatar);
            result.add(in);
        }
        result.get(0).setDisplayStatus(0);
        result.get(1).setDisplayStatus(1);
        result.get(2).setDisplayStatus(2);
        result.get(3).setDisplayStatus(3);
        return result;
    }

    public List<BaseContact> getFacebookContacts(){
        ArrayList<BaseContact> result = new ArrayList<>();
        for(int i=0;i<4;i++){
            BaseContact in = new BaseContact();
            in.setContactId(emails[i]);
            in.setName(names[i]);
            in.setPhoto(avatar);
            result.add(in);
        }
        result.get(0).setDisplayStatus(1);
        result.get(1).setDisplayStatus(2);
        result.get(3).setDisplayStatus(3);
        result.get(4).setDisplayStatus(4);
        return result;
    }

    public List<ITimeUser> getITimeFriends(){
        ArrayList<ITimeUser> result = new ArrayList<>();
        for(int i=0;i<15;i++){
            ITimeUser in = new ITimeUser();
            in.setContactId(emails[i]);
            in.setName(names[i]);
            in.setPhoto(avatar);
            in.setSex("male");
            in.setEmail("iamlanmi@gmail.com");
            in.setPhone("0420643167");
            in.setCountry("Australia");
            in.setState("Melbourne");
            result.add(in);
        }
        return result;
    }
}
