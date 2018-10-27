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

public class BooksAPIManager extends AsyncTask<Void, Void, Book> {

  //  public AsyncResponse delegate = null;

    private String search;
    static final String API_URL = "https://www.googleapis.com/books/v1/volumes?";


    public BooksAPIManager(String query) {
        this.search = query;
    }

    protected void onPreExecute() {

    }

    protected Book doInBackground(Void... urls) {


        try {
            URL url = new URL(API_URL + "q=" + search);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

                String imageURL ;
                String id ;
                String title ;
                String language ;
                String description ;
                ArrayList<String> authorsList;
                ArrayList<String> categorieList;
                Book book = null;

                try {
                    JSONObject object = (JSONObject) new JSONTokener(stringBuilder.toString()).nextValue();
                    JSONArray items = object.optJSONArray("items");
                    JSONObject oneItem = items.optJSONObject(0);
                    JSONObject volumeInfo = oneItem.optJSONObject("volumeInfo");
                    if (volumeInfo != null){

                        JSONObject imageLinks = volumeInfo.optJSONObject("imageLinks");
                        imageURL = imageLinks.optString("thumbnail");

                        id = oneItem.getString("id");
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

                        book = new Book(id, title, authorsList, categorieList, language, description, imageURL);
                    }



                    return book;
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


}