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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bcs.bookexchangev2.R;

import classes.User;
import controllers.DataBaseManager;
import controllers.ImageManager;
import fragments.Books_profil_fragment;
import fragments.Events_profil_Fragment;

public class ProfilActivity extends AppCompatActivity {

    private TextView userTextView = null;
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
        /*************************************** Display name surname of user ****************************************/

        userTextView = findViewById(R.id.username);
        userProfilPhoto = findViewById(R.id.overlapImage);

        dataBaseManager.getUserById(new DataBaseManager.ResultGetter<User>() {
            @Override
            public void onResult(User user) {
                userTextView.setText(user.getName().toString()+" "+user.getSurname().toString());
                userProfilPhoto.setImageBitmap(imageManager.decodeBase64(user.getProfilPhotoUri()));
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

        /******************************************** Navigation drawer menu *******************************************/

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
            public boolean onQueryTextSubmit(String query) {

                //do the search here
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
