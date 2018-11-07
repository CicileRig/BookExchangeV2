package fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.constraint.solver.widgets.Helper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bcs.bookexchangev2.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Iterator;

import adapters.Book_List_Adapter;
import classes.Book;
import controllers.BooksAPIManager;
import controllers.DataBaseManager;

public class Search_Fragment extends BaseFragment {

    private TextView textView;
    private ListView finded_books_lv;
    private View view;
    FragmentActivity listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.search_fragment_layout,container, false);
        setRetainInstance(true);

        // Views :
        textView = view.findViewById(R.id.finded_book);
        finded_books_lv = view.findViewById(R.id.finded_books);


        final DataBaseManager dataBaseManager = new DataBaseManager();
        // chercher par titre

        dataBaseManager.findBookByTitle(getArguments().getString("query"), new DataBaseManager.ResultGetter<ArrayList<Book>>() {
            @Override
            public void onResult(ArrayList<Book> books) {

                if(!books.isEmpty()){
                    if (getActivity()!=null){
                        Book_List_Adapter booksAdapter = new Book_List_Adapter(books, getActivity());
                        finded_books_lv.setAdapter(booksAdapter);
                    }else{
                        Log.d("Log", "getActivity est nul");
                    }

                }else{
                    dataBaseManager.findBookByAuthor(getArguments().getString("query"), new DataBaseManager.ResultGetter<ArrayList<Book>>() {
                        @Override
                        public void onResult(ArrayList<Book> books2) {
                            if(!books2.isEmpty())
                            {
                                if (getActivity()!=null){
                                    Book_List_Adapter booksAdapter = new Book_List_Adapter(books2, getActivity());
                                    finded_books_lv.setAdapter(booksAdapter);
                                }else{
                                    Log.d("Log", "getActivity est nul");
                                }
                            }else{
                                textView.setText("Le livre que vous cherchez n'existe pas");
                            }
                        }
                    });
                }
            }
        });

        return view;
    }


}
