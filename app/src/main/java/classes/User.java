package classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable{

    private String id;
    private String name;
    private String surname;
    private String mailAdress;
    private String age;
    private String password;
    private ArrayList<Book> booksList;
    private String profilPhotoUri;
    private String adress  ;
    private String phoneNumber;

    public  User(){

    }

    public User(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7) {}

    public User(String name, String surname, String mailAdress, String password, String age, ArrayList<Book> booksList) {
        this.name = name;
        this.surname = surname;
        this.mailAdress = mailAdress;
        this.age = age;
        this.password = password;
        this.booksList = booksList;
    }

    public User(String name, String surname, String mailAdress, String password) {
        this.name = name;
        this.surname = surname;
        this.mailAdress = mailAdress;
        this.password = password;
    }

    public User(String email) {
        this.mailAdress = email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getMailAdress() {
        return mailAdress;
    }

    public String getAge() {
        return age;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Book> getBooksList() {
        return booksList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setMailAdress(String mailAdress) {
        this.mailAdress = mailAdress;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBooksList(ArrayList<Book> booksList) {
        this.booksList = booksList;
    }

    public String getProfilPhotoUri() {
        return profilPhotoUri;
    }

    public void setProfilPhotoUri(String profilPhotoUri) {
        this.profilPhotoUri = profilPhotoUri;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("surname", surname);
        result.put("mailAdress", mailAdress);
        result.put("password", password);
        result.put("age", age);
        result.put("profilPhotoUri", profilPhotoUri);
        result.put("adress", adress);
        result.put("phoneNumber", phoneNumber);
        return result;
    }

}
