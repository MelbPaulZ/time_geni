package org.unimelb.itime.ui.contact.Model;

import android.text.TextUtils;

import org.unimelb.itime.ui.contact.Beans.BaseContact;
import org.unimelb.itime.ui.contact.Beans.ITimeUser;
import org.unimelb.itime.ui.contact.Beans.RequestFriend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 37925 on 2016/12/6.
 */

public class ContactFilter {

    private static ContactFilter instance=null;
    private ContactFilter(){}

    public static ContactFilter getInstance(){
        if(instance==null){
            instance = new ContactFilter();
        }
        return instance;
    }

    public List<BaseContact> filter(List<BaseContact> list, String filterStr){
        filterStr = filterStr.toLowerCase();
        List<BaseContact> filterList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            for (BaseContact contact : list) {
                contact.setMatchStr("");
            }
            return list;
        } else {
            for (BaseContact contact : list) {
                String name = contact.getName();
                String contactUid = contact.getContactId();
                String pinyin = contact.getSortLetters();
                if(name == null){
                    name = "";
                }
                if(contactUid == null){
                    contactUid = "";
                }
                if(pinyin == null){
                    pinyin = "";
                }
                if (name.toLowerCase().contains(filterStr)
                        || contactUid.toLowerCase().contains(filterStr)
                        || pinyin.toLowerCase().contains(filterStr)) {
                    contact.setMatchStr(filterStr);
                    filterList.add(contact);
                }else{
                    contact.setMatchStr("");
                }
            }
        }
        return filterList;
    }

    public List<RequestFriend> filterRequest(List<RequestFriend> list, String filterStr){
        filterStr = filterStr.toLowerCase();
        List<RequestFriend> filterList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            for (RequestFriend contact : list) {
                contact.setMatchStr("");
            }
            return list;
        } else {
            for (RequestFriend contact : list) {
                String name = contact.getName();
                String contactUid = contact.getContactId();
                String pinyin = contact.getSortLetters();
                if(name == null){
                    name = "";
                }
                if(contactUid == null){
                    contactUid = "";
                }
                if(pinyin == null){
                    pinyin = "";
                }
                if (name.toLowerCase().contains(filterStr)
                        || contactUid.toLowerCase().contains(filterStr)
                        || pinyin.toLowerCase().contains(filterStr)) {
                    contact.setMatchStr(filterStr);
                    filterList.add(contact);
                }else{
                    contact.setMatchStr("");
                }
            }
        }
        return filterList;
    }

    public List<ITimeUser> filterUser(List<ITimeUser> list, String filterStr){
        filterStr = filterStr.toLowerCase();
        List<ITimeUser> filterList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            for (ITimeUser contact : list) {
                contact.setMatchStr("");
            }
            return list;
        } else {
            for (ITimeUser contact : list) {
                String name = contact.getName();
                String contactUid = contact.getContactId();
                String pinyin = contact.getSortLetters();
                if(name == null){
                    name = "";
                }
                if(contactUid == null){
                    contactUid = "";
                }
                if(pinyin == null){
                    pinyin = "";
                }
                if (name.toLowerCase().contains(filterStr)
                        || contactUid.toLowerCase().contains(filterStr)
                        || pinyin.toLowerCase().contains(filterStr)) {
                    contact.setMatchStr(filterStr);
                    filterList.add(contact);
                }else{
                    contact.setMatchStr("");
                }
            }
        }
        return filterList;
    }
}
