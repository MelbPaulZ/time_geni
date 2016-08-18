package com.example;


import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class ITimeDaoGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");
    private static final String OUT_DIR = PROJECT_DIR + "/app/src/main/java";

    public static void main(String[] args){
        Schema schema = new Schema(1, "org.unimelb.itime.dao");
        addTables(schema);
        try {
            new DaoGenerator().generateAll(schema, OUT_DIR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addTables(Schema schema){
        Entity user = addUser(schema);
    }

    private static Entity addUser(Schema schema){
        Entity user = schema.addEntity("User");
        user.addStringProperty("id").notNull();
        user.addStringProperty("userId").notNull();
        user.addStringProperty("personalAlias");
        user.addStringProperty("bindingEmail");
        user.addStringProperty("bindingFacebookId");
        user.addStringProperty("bindingPhone");
        user.addStringProperty("profilePhotoUrl");
        user.addLongProperty("defaultEventAlertTimeId");
        user.addStringProperty("deviceToken");
        user.addStringProperty("deviceId");
        user.addStringProperty("defaultRatingVisibilityTypeId");
        user.addStringProperty("defaultEventVisibilityTypeId");
        user.addStringProperty("ifAcceptPublicEventPush");
        user.addStringProperty("averageRatingValue");
        user.addStringProperty("createdAt");
        user.addStringProperty("updatedAt");
        return user;
    }
}
