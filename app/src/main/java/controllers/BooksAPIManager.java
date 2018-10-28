package controllers;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import classes.Book;

public class BooksAPIManager extends AsyncTask<Void, Void, ArrayList<Book> > {

  //  public AsyncResponse delegate = null;

    private String search;
    static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";


    public BooksAPIManager(String query) {
        this.search = query;
    }

    protected void onPreExecute() {

    }

    protected  ArrayList<Book> doInBackground(Void... urls) {

        ArrayList<Book> bookList = new ArrayList<>();
        try {
            URL url = new URL(API_URL  + search);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                try {
                    JSONObject object = (JSONObject) new JSONTokener(stringBuilder.toString()).nextValue();
                    JSONArray items = object.optJSONArray("items");
                     for(int i=0; i<items.length(); i++){

                        JSONObject oneItem = items.optJSONObject(i);
                        JSONObject volumeInfo = oneItem.optJSONObject("volumeInfo");
                         String imageURL = null;
                         String isbn = null;
                         String title = null;
                         String language = null ;
                         String description = null;
                         ArrayList<String> authorsList = null;
                         ArrayList<String> categorieList = null;

                        if (volumeInfo != null){

                            if(volumeInfo.optJSONObject("imageLinks") != null)
                            {
                                JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
                                imageURL = imageLinks.optString("thumbnail");
                            }


                            isbn = getISBN13FromVolumeInfo(volumeInfo);
                            title = volumeInfo.optString("title");
                            Log.d("tag", title);

                            language = volumeInfo.optString("language");
                            Log.d("tag", language);
                            description = volumeInfo.optString("description");
                            Log.d("tag", description);

                            JSONArray authors = volumeInfo.optJSONArray("authors");
                            authorsList = getListFromJson(authors);

                            JSONArray categories = volumeInfo.optJSONArray("categories");
                            categorieList = getListFromJson(categories);

                            bookList.add( new Book(isbn, title, authorsList, categorieList, language, description, imageURL));
                        }

                    }

                    return bookList;
                } catch (JSONException e) {
                    // Appropriate error handling code
                    return null;
                }

            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }



    public ArrayList<String> getListFromJson(JSONArray jsonArray)
    {
        ArrayList<String> list = new ArrayList<String>();
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i=0;i<len;i++){
                try {
                    list.add(jsonArray.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    private String getISBN13FromVolumeInfo(JSONObject volumeInfo) throws JSONException {
        if (volumeInfo.has("industryIdentifiers")) {
            JSONArray industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");

            for (int i = 0; i < industryIdentifiers.length(); i++) {
                JSONObject industryIdentifier = industryIdentifiers.getJSONObject(i);

                if (industryIdentifier.has("type") && industryIdentifier.get("type").equals("ISBN_13")) {
                    return industryIdentifier.getString("identifier");
                }
            }
        }

        return "";
    }


}