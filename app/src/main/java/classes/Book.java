package classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Book implements Serializable{

    private String id;
    private ArrayList<String> ISBNList;
    private String title;
    private ArrayList<String> authors;
    private ArrayList<String> categories;
    private String language;
    private String imageURL;
    private String description;

    public Book(String id, ArrayList<String> ISBNList, String title, ArrayList<String> authors, ArrayList<String> categories, String language) {
        this.id = id;
        this.ISBNList = ISBNList;
        this.title = title;
        this.authors = authors;
        this.categories = categories;
        this.language = language;
    }

    public Book(String id, String title, ArrayList<String> authorsList, String language, String imageURL) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.setAuthors(authorsList);
        this.imageURL = imageURL;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String authorsToString()
    {
        String result = "Par : ";
        Iterator<String> iterator = this.authors.iterator();
        while (iterator.hasNext())
        {
            result = result + iterator.next()+"\n";
        }
        return result;
    }

    public String categoriesToString()
    {
        String result = "Catégories : ";
        Iterator<String> iterator = this.categories.iterator();
        while (iterator.hasNext())
        {
            result = result + iterator.next()+"\n";
        }
        return result;
    }


    public String ISBNToString(){

        String result = "ISBN : ";
        Iterator<String> iterator = this.ISBNList.iterator();
        while (iterator.hasNext())
        {
            result = result + iterator.next()+"\n";
        }
        return result;
    }
}
