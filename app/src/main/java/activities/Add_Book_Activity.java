package activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.bcs.bookexchangev2.R;

import java.util.ArrayList;

import adapters.Book_List_Adapter;
import classes.Book;
import controllers.BooksAPIManager;
import controllers.DataBaseManager;

public class Add_Book_Activity extends AppCompatActivity {

    private ListView booksListView;
    private SearchView searchView;
    private RadioGroup languageRadioGroup ;

    private DataBaseManager dataBaseManager = new DataBaseManager();
    private android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__book_);

        // Views :
        booksListView  = findViewById(R.id.searchBookListview);
        searchView = findViewById(R.id.booksSearchView);
        languageRadioGroup = findViewById(R.id.radioGroup2);

        /************************************* Make listview scralling inside nestedScrallView *******************************/

        booksListView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        /******************************************Execute search Query *****************************************************/
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                    if(languageRadioGroup.getCheckedRadioButtonId() != -1)
                    {
                        int selectedId = languageRadioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton = findViewById(selectedId);
                        if(radioButton.getText() == "Français")
                        {
                            query = query +"&langRestrict:fr";

                        }else if (radioButton.getText() == "Anglais")
                        {
                            query = query +"&langRestrict:en";
                        }
                    }

                    new BooksAPIManager(query){
                        @Override
                        protected void onPostExecute(ArrayList<Book> bookList) {
                            super.onPostExecute(bookList);
                            Book_List_Adapter booksAdapter = new Book_List_Adapter(bookList, Add_Book_Activity.this);
                            booksListView.setAdapter(booksAdapter);
                        }
                    }.execute();

                    // one of the radio buttons is checked
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        /************************************** Add selected Book to user library ************************************/

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the selected item text from ListView
                Book book = (Book) adapterView.getItemAtPosition(i);
                dataBaseManager.addBookToCurentUser(book);
                Toast.makeText(Add_Book_Activity.this, "Livre Ajouté avec succes", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Add_Book_Activity.this, NavigationActivity.class);
                intent.putExtra("item_id",R.id.nav_books);
                startActivity(intent);
            }
        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
        Intent intent = new Intent(Add_Book_Activity.this, NavigationActivity.class);
        intent.putExtra("item_id",R.id.nav_books);
        startActivity(intent);
    }
}
