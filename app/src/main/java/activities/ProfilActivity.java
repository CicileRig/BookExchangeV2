package activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bcs.bookexchangev2.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import classes.Book;
import classes.User;
import controllers.BooksAPIManager;
import controllers.DataBaseManager;
import controllers.ImageManager;
import controllers.MyBounceInterpolator;
import fragments.Books_profil_fragment;
import fragments.Events_profil_Fragment;
import fragments.Search_Fragment;


public class ProfilActivity extends AppCompatActivity {

    private TextView userTextView = null;
    private TextView booksNumber = null;
    private TextView eventsNumber = null;
    private ImageView userProfilPhoto = null;
    private BottomNavigationView bottomNavigationView;
    private static DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    private android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
    private DataBaseManager dataBaseManager = new DataBaseManager();
    private ImageManager imageManager = new ImageManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // view :
        userTextView = findViewById(R.id.username);
        userProfilPhoto = findViewById(R.id.overlapImage);
        booksNumber = findViewById(R.id.myBooksNumber);
        eventsNumber = findViewById(R.id.myEventsNumber);

        /*************************************** Display user informations ****************************************/

        // get current user isntance
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // getting the name and surname of the current user
        dataBaseManager.getUserById( userId, new DataBaseManager.ResultGetter<User>() {
            @Override
            public void onResult(User user) {
                userTextView.setText(user.getName().toString()+" "+user.getSurname().toString());
                userProfilPhoto.setImageBitmap(imageManager.decodeBase64(user.getProfilPhotoUri()));
            }
        });

        // getting the number of books of the current user
        dataBaseManager.getCUserBookNumber(new DataBaseManager.ResultGetter<String>() {
            @Override
            public void onResult(String bookNbr) {
                booksNumber.setText(bookNbr);
            }
        });

        // getting the number of events created by the user
        dataBaseManager.getCUserEventsNumber(new DataBaseManager.ResultGetter<String>() {
            @Override
            public void onResult(String eventNbr) {
                eventsNumber.setText(eventNbr);
            }
        });

        /******************************************** Toolbar configuration *******************************************/
        //getting the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        //placing toolbar in place of actionbar
        toolbar.inflateMenu(R.menu.search_bar_menu);
        setSupportActionBar(toolbar);
        //set navigation icon in the toolbar
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        /******************************************** Navigation drawer menu *******************************************/

        // getting the navigatio drawer view
        dl = findViewById(R.id.drawer_layout);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);
        dl.addDrawerListener(t);

        t = new ActionBarDrawerToggle(this, dl, toolbar, R.string.Open, R.string.Close);
        t.syncState();

        nv = findViewById(R.id.nv);
        dl.closeDrawers();
        configureNavigationDrawer(nv);

        /*********************************************** Buttom menu bar  ************************************************/

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        configureBottomNavigationBar(bottomNavigationView);


    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_bar_menu, menu);

        //getting the search view from the menu
        MenuItem searchViewItem = menu.findItem(R.id.menuSearch);

        //getting search manager from systemservice
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        //getting the search view
        final SearchView searchView = (SearchView) searchViewItem.getActionView();

        //put a hint for the search input field
        searchView.setQueryHint("Trouver un livre ...");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(true);

        //here we will get the search query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                Search_Fragment search_fragment = new Search_Fragment();


                Bundle args = new Bundle();
                args.putString("query", query);
                search_fragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.dynamic_fragment_frame_layout, search_fragment).commit();

                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return  true;
    }

    private void configureNavigationDrawer(NavigationView nv)
    {
        dl.closeDrawers();
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent navigation_intent = new Intent(ProfilActivity.this, NavigationActivity.class);
                navigation_intent.putExtra("item_id",id );
                startActivity(navigation_intent);
                return true;
            }
        });

    }

    public void configureBottomNavigationBar(BottomNavigationView bottomNavigationView)
    {
        Events_profil_Fragment events_profil_fragment = new Events_profil_Fragment();
        fragmentManager.beginTransaction().replace(R.id.dynamic_fragment_frame_layout, events_profil_fragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_books:
                        Books_profil_fragment books_profil_fragment = new Books_profil_fragment();
                        fragmentManager.beginTransaction().replace(R.id.dynamic_fragment_frame_layout, books_profil_fragment).commit();
                        return true;
                    case R.id.item_events:
                        Events_profil_Fragment events_profil_fragment = new Events_profil_Fragment();
                        fragmentManager.beginTransaction().replace(R.id.dynamic_fragment_frame_layout, events_profil_fragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

}
