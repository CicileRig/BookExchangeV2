package classes;

import java.util.ArrayList;

public class Book {

    private String id;
    private ArrayList<String> ISBNList;
    private String title;
    private ArrayList<String> authors;
    private ArrayList<String> categories;
    private String language;

    public Book(String id, ArrayList<String> ISBNList, String title, ArrayList<String> authors, ArrayList<String> categories, String language) {
        this.id = id;
        this.ISBNList = ISBNList;
        this.title = title;
        this.authors = authors;
        this.categories = categories;
        this.language = language;
    }

    public Book(String id, String title, String language) {
        this.id = id;
        this.title = title;
        this.language = language;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getISBNList() {
        return ISBNList;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public String getLanguage() {
        return language;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setISBNList(ArrayList<String> ISBNList) {
        this.ISBNList = ISBNList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
