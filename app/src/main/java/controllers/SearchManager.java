package controllers;

import android.provider.ContactsContract;

import java.text.Normalizer;

import classes.Book;

public class SearchManager {


    public SearchManager() {
    }

    public Boolean searchStringInOther(String s1, String s2){

        s1= removeDiacriticalMarks(s1.toLowerCase());
        s2= removeDiacriticalMarks(s2.toLowerCase());

        return s1.contains(s2) || s2.contains(s1);
    }
    public static String removeDiacriticalMarks(String string) {
        return Normalizer.normalize(string, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

}
