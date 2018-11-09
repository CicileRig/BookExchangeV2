package fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.constraint.solver.widgets.Helper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bcs.bookexchangev2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import activities.Add_Book_Activity;
import activities.NavigationActivity;
import activities.ProfilActivity;
import adapters.Book_List_Adapter;

import classes.Book;
import classes.User;
import controllers.BooksAPIManager;
import controllers.DataBaseManager;

public class Books_profil_fragment extends Fragment {

    private ArrayList<Book> my_books_list;
    private ListView book_listView;
    private ProgressBar progressBar;
    private Book_List_Adapter booksAdapter;
    private DataBaseManager dataBaseManager = new DataBaseManager();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.books_fragment, container, false);


        /*************************************** Populate books list ********************************************/
        my_books_list = new ArrayList<Book>();
        booksAdapter = new Book_List_Adapter(my_books_list, getActivity());
        book_listView = view.findViewById(R.id.books_list);
        progressBar =  view.findViewById(R.id.progressBar);

        if(savedInstanceState == null || !savedInstanceState.containsKey("key")) {

            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
            progressBar.setVisibility(View.VISIBLE);
            book_listView.setVisibility(View.GONE);
            dataBaseManager.getUserIsbnBooksList(userId, new DataBaseManager.ResultGetter<ArrayList<Book>>() {
                @Override
                public void onResult(final ArrayList<Book> booksList) {
                    new BooksAPIManager(constructBooksIsbnRequest(booksList)){
                        @Override
                        protected void onPostExecute(ArrayList<Book> bookList) {
                            super.onPostExecute(bookList);

                            Iterator<Book> it1  = booksList.iterator();
                            Iterator<Book> it2 = bookList.iterator();
                            while(it2.hasNext()){
                                it2.next().setSoumissionDate(it1.next().getSoumissionDate());
                            }
                            if (getActivity()!=null){
                                my_books_list = booksList;
                                booksAdapter = new Book_List_Adapter(bookList, getActivity());
                                book_listView.setAdapter(booksAdapter);

                            }else{
                                Log.d("Log", "getActivity est nul");
                            }
                        }
                    }.execute();
                    progressBar.setVisibility(View.GONE);
                    book_listView.setVisibility(View.VISIBLE);


                }
            });
        }
        else {
            my_books_list = savedInstanceState.getParcelableArrayList("key");
            booksAdapter = new Book_List_Adapter(my_books_list, getActivity());
            book_listView.setAdapter(booksAdapter);
        }

        book_listView.setAdapter(booksAdapter);

        /************************************ Select book from list action **********************************/
        book_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the selected item text from ListView
                Book value = (Book) adapterView.getItemAtPosition(i);
                My_Book_Detail_Fragment my_book_detail_fragment = new My_Book_Detail_Fragment();

                Bundle args = new Bundle();
                args.putSerializable("book", value);
                my_book_detail_fragment.setArguments(args);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.dynamic_fragment_frame_layout, my_book_detail_fragment);
                transaction.commit();
            }
        });

        return view;
    }

    public String constructBooksIsbnRequest(ArrayList<Book> bookList){

        String result = "";
        Iterator<Book> it = bookList.iterator();
        while(it.hasNext()){

            if(!result.equals("")){
                result = result + "%20|%20";
            }
            Book book = it.next();

            result = result + "isbn:"+book.getId();
        }
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", my_books_list);
        super.onSaveInstanceState(outState);
    }




}
