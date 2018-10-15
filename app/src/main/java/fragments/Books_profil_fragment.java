package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bcs.bookexchangev2.R;

import java.util.ArrayList;

import adapters.Book_List_Adapter;

import classes.Book;

public class Books_profil_fragment extends Fragment {

    private ArrayList<Book> books_list;
    private ListView book_listView;
    private Book_List_Adapter booksAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.books_fragment, container, false);

        books_list = new ArrayList<Book>();
        booksAdapter = new Book_List_Adapter(books_list, getActivity());
        book_listView = view.findViewById(R.id.books_list);

        books_list.add(new Book("id1", "livre 1 ", "Français"));
        books_list.add(new Book("id2", "livre 2 ", "Anglais"));
        books_list.add(new Book("id3", "livre 3 ", "Français"));
        books_list.add(new Book("id4", "livre 4 ", "Arabe"));

        book_listView.setAdapter(booksAdapter);

        return view;
    }

}
