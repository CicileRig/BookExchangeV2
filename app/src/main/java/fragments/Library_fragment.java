package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import adapters.Book_List_Adapter;
import classes.Book;
import controllers.BooksAPIManager;
import controllers.DataBaseManager;

public class Library_fragment extends Fragment {

    private ListView libraryListview ;
    private ArrayList<Book> library_books_list;
    private ProgressBar progressBar;
    private Book_List_Adapter booksAdapter;

    private DataBaseManager dataBaseManager = new DataBaseManager();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.library_fragment_layout,container, false);

        // Views :
        libraryListview = view.findViewById(R.id.libraryListview);
        progressBar =  view.findViewById(R.id.progressBar);

        /*************************************** Populate books list ********************************************/
        library_books_list = new ArrayList<>();
        booksAdapter = new Book_List_Adapter(library_books_list, getActivity());

        if(savedInstanceState == null || !savedInstanceState.containsKey("key")) {

            progressBar.setVisibility(View.VISIBLE);
            libraryListview.setVisibility(View.GONE);
            dataBaseManager.getAllBooksList(new DataBaseManager.ResultGetter<ArrayList<Book>>() {
                @Override
                public void onResult(ArrayList<Book> books) {

                    if (getActivity()!=null){
                        library_books_list = books;
                        booksAdapter = new Book_List_Adapter(books, getActivity());
                        libraryListview.setAdapter(booksAdapter);
                        progressBar.setVisibility(View.GONE);
                        libraryListview.setVisibility(View.VISIBLE);

                    }else{
                        Log.d("Log", "getActivity est nul");
                    }
                }
            });
        }
        else {
            library_books_list = savedInstanceState.getParcelableArrayList("key");
            booksAdapter = new Book_List_Adapter(library_books_list, getActivity());
            libraryListview.setAdapter(booksAdapter);
        }


        /************************************ Select book from list action **********************************/
        libraryListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the selected item text from ListView
                Book value = (Book) adapterView.getItemAtPosition(i);
                Log.d("Listview", value.getTitle());

                Exchange_Book_Fragment my_book_detail_fragment = new Exchange_Book_Fragment();

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", library_books_list);
        super.onSaveInstanceState(outState);
    }



}
