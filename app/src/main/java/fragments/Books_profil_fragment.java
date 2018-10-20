package fragments;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bcs.bookexchangev2.R;

import java.io.InputStream;
import java.util.ArrayList;

import activities.NavigationActivity;
import activities.ProfilActivity;
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

        ArrayList<String> authorsBook1 = new ArrayList<>();
        authorsBook1.add("Auteur 1");
        authorsBook1.add("Auteur 2");

        ArrayList<String> authorsBook2 = new ArrayList<>();
        authorsBook2.add("Auteur 3");

        ArrayList<String> authorsBook3 = new ArrayList<>();
        authorsBook3.add("Auteur 4");


        String imageUrl = "http://books.google.com/books/content?id=SpNmAAAAcAAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api";
        String imageUrl1 = "http://books.google.com/books/content?id=9YItAAAAYAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api";
        String imageUrl2 = "http://books.google.com/books/content?id=Ui5NDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api";
        String imageUrl3 = "http://books.google.com/books/content?id=NIy5CwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api";

        books_list.add(new Book("id1", "livre 1 ",authorsBook1,  "Français", imageUrl));
        books_list.add(new Book("id2", "livre 2 ", authorsBook2, "Anglais", imageUrl1));
        books_list.add(new Book("id3", "livre 3 ",authorsBook3, "Français", imageUrl2));
        books_list.add(new Book("id4", "livre 4 ",authorsBook3, "Arabe", imageUrl3));

        book_listView.setAdapter(booksAdapter);

        book_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the selected item text from ListView
                Book value = (Book) adapterView.getItemAtPosition(i);
                Log.d("Listview", value.getTitle());
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


}
