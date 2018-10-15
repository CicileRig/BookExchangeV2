package controllers;

import org.json.JSONException;
import org.json.JSONObject;

import classes.User;

public class JsonUtil {

    public  String toJson(User user) throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("name", user.getName());
        jsonObj.put("surname", user.getSurname());
        jsonObj.put("age", user.getAge());
        jsonObj.put("mailAdress", user.getMailAdress());
        jsonObj.put("password", user.getPassword());
        return jsonObj.toString();
    }
}
