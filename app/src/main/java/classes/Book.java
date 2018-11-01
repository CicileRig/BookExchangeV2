package classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Book implements Serializable{

    private String isbn;
    private String title;
    private ArrayList<String> authors;
    private ArrayList<String> categories;
    private String language;
    private String imageURL;
    private String description;
    private String soumissionDate ;

    public Book(String id, String title, ArrayList<String> authors, ArrayList<String> categories, String language, String description, String imageURL) {
        this.isbn = id;
        this.title = title;
        this.authors = authors;
        this.categories = categories;
        this.language = language;
        this.description = description;
        this.imageURL = imageURL;
    }

    public Book(String id, String title, ArrayList<String> authorsList, String language, String imageURL) {
        this.isbn = id;
        this.title = title;
        this.language = language;
        this.setAuthors(authorsList);
        this.imageURL = imageURL;
    }

    public Book() {

    }

    public String getId() {
        return isbn;
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
        this.isbn = id;
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getSoumissionDate() {
        return soumissionDate;
    }

    public void setSoumissionDate(String soumissionDate) {
        this.soumissionDate = soumissionDate;
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

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("isbn_13", isbn);
        result.put("soumission_date", soumissionDate);
        return result;
    }

    @Override
    public boolean equals(Object v) {

        if (v instanceof Book){
            Book ptr = (Book) v;
            if( ptr.getId()== this.getId()){
                return  true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {

        return isbn.hashCode();
    }



}
