package com.example.maheshpujala.sillymonks.Model;

import java.io.Serializable;

/**
 * Created by maheshpujala on 17/10/16.
 */

public class UserData  implements Serializable {

    private String id,name,email,gender,login_type;


    public UserData (String id,String name,String email,String gender,String login_type){
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.login_type=login_type;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public  String getLoginType(){return login_type;}


}
