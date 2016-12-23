package org.unimelb.itime.ui.contact.Model;

/**
 * Created by 37925 on 2016/12/20.
 */

public class DecodeHelper {
    private static DecodeHelper instance;
    private DecodeHelper(){}

    public DecodeHelper getInstance(){
        if(instance==null){
            instance = new DecodeHelper();
        }
        return instance;
    }
}
