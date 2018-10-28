package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bcs.bookexchangev2.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import adapters.Book_List_Adapter;
import classes.Book;
import controllers.BooksAPIManager;
import controllers.DataBaseManager;

public class Library_fragment extends Fragment {

    private ListView libraryListview ;
    private ArrayList<Book> library_books_list;
    private Book_List_Adapter booksAdapter;

    private DataBaseManager dataBaseManager = new DataBaseManager();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.library_fragment_layout,container, false);

        // Views :
        libraryListview = view.findViewById(R.id.libraryListview);

        /*************************************** Populate books list ********************************************/
        library_books_list = new ArrayList<>();
        booksAdapter = new Book_List_Adapter(library_books_list, getActivity());

        dataBaseManager.getUsersList(new DataBaseManager.ResultGetter<ArrayList<String>>() {
            @Override
            public void onResult(ArrayList<String> usersList) {

                Iterator<String> it = usersList.iterator();
                while (it.hasNext())
                {
                    dataBaseManager.getUserIsbnBooksList(it.next(), new DataBaseManager.ResultGetter<ArrayList<Book>>() {
                        @Override
                        public void onResult(ArrayList<Book> booksList) {

                            new BooksAPIManager(constructBooksIsbnRequest(booksList)){
                                @Override
                                protected void onPostExecute(ArrayList<Book> bookList) {
                                    super.onPostExecute(bookList);
                                    if(bookList != null)
                                    {
                                       library_books_list =  addListToAnother(library_books_list,bookList);
                                       libraryListview.setAdapter(booksAdapter);
                                    }
                                }
                            }.execute();


                        }
                    });
                }
            }
        });

        return view;
    }

    public String constructBooksIsbnRequest(ArrayList<Book> bookList){

        String result = "";
        Iterator<Book> it = bookList.iterator();
        while(it.hasNext()){

            if(!result.equals("")){
                result = result + " | ";
            }
            Book book = it.next();

            result = result + "isbn:"+book.getId();
        }
        return result;
    }

    public ArrayList<Book> addListToAnother(ArrayList<Book> globalList, ArrayList<Book> listToAdd)
    {
        ArrayList<Book> result = globalList;
        Iterator<Book> it = listToAdd.iterator();
        while (it.hasNext()) {

            Book book = it.next();
            if (!result.contains(book)) {
                result.add(book);
            }
        }
        return result;
    }

}
