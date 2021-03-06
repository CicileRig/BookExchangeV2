package activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
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


import com.example.bcs.bookexchangev2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.List;

import fragments.BaseFragment;
import fragments.Books_profil_fragment;
import fragments.Events_profil_Fragment;
import fragments.Library_fragment;
import fragments.Search_Fragment;

public class NavigationActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private Toolbar toolbar;

    final String PREFER_NAME = "SessionRef";

    // Shared Preferences reference
    SharedPreferences pref;

    // Editor reference for Shared preferences
    SharedPreferences.Editor editor;

    // Shared pref mode
    int PRIVATE_MODE = 0;


    private android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_activity);


        /******************************************** Toolbar configuration *******************************************/
        //getting the toolbar
        toolbar = findViewById(R.id.toolbar);
        //placing toolbar in place of actionbar
        toolbar.inflateMenu(R.menu.search_bar_menu);
        setSupportActionBar(toolbar);
        //set navigation icon in the toolbar
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");


        /******************************************** Navigation drawer menu *******************************************/

        dl = findViewById(R.id.drawer_layout);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);
        dl.addDrawerListener(t);

        t = new ActionBarDrawerToggle(this, dl, toolbar, R.string.Open, R.string.Close);
        t.syncState();

        int id_item = getIntent().getIntExtra("item_id", 0);
        configureDefaultItemNavigationDrawer(id_item);

        nv = findViewById(R.id.nv);
        dl.closeDrawers();
        configureNavigationDrawer(nv);

        /*************************************************Boom Menu*************************************************/

        BoomMenuButton bmb =  findViewById(R.id.bmb);

        HamButton.Builder builder = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_add_book)
                .normalText("Ajouter un nouveau livre")
                .textSize(18)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                       startActivity(new Intent(NavigationActivity.this, Add_Book_Activity.class));
                    }
                });
        bmb.addBuilder(builder);

        HamButton.Builder builder2 = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_add_event)
                .normalText("Créer un evenement")
                .textSize(20)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        startActivity(new Intent(NavigationActivity.this, Add_Event_Activity.class));
                    }
                });
        bmb.addBuilder(builder2);

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed()
    {
        dl.closeDrawers();
        // super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
        List fragmentList = getSupportFragmentManager().getFragments();

        boolean handled = false;
        for(Object f : fragmentList) {
            if(f instanceof BaseFragment) {
                handled = ((BaseFragment)f).onBackPressed();

                if(handled) {
                    break;
                }
            }
        }

        if(!handled) {
            super.onBackPressed();
        }
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
                Log.d("LOG", "ici");
                Search_Fragment search_fragment = new Search_Fragment();
                Bundle args = new Bundle();
                args.putString("query", query);
                search_fragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.dynamic_fragment_frame_layout, search_fragment).commit();

                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    Library_fragment library_fragment = new Library_fragment();
                    fragmentManager.beginTransaction().replace(R.id.dynamic_fragment_frame_layout, library_fragment).commit();
                }
                return false;
            }
        });


        return  true;
    }


    private void configureDefaultItemNavigationDrawer( int id_item) {

        if (id_item == R.id.nav_profil)
        {
            Intent intent = new Intent(NavigationActivity.this, ProfilActivity.class);
            startActivity(intent);
        }
        else if(id_item == R.id.nav_library)
        {
            toolbar.setTitle("Bibliothèque des livres");
            Library_fragment library_fragment = new Library_fragment();
            fragmentManager.beginTransaction().replace(R.id.dynamic_fragment_frame_layout, library_fragment).commit();
            dl.closeDrawers();
        }else if (id_item == R.id.nav_books){
            toolbar.setTitle("Mes livres");
            Books_profil_fragment books_fragment = new Books_profil_fragment();
            fragmentManager.beginTransaction().replace(R.id.dynamic_fragment_frame_layout, books_fragment).commit();
            dl.closeDrawers();

        }else if (id_item == R.id.nav_event){
            toolbar.setTitle("Evenements à venir");
            Events_profil_Fragment events_fragment = new Events_profil_Fragment();
            fragmentManager.beginTransaction().replace(R.id.dynamic_fragment_frame_layout, events_fragment).commit();
            dl.closeDrawers();
        }else if (id_item == R.id.nav_log_out){
            dl.closeDrawers();
            FirebaseAuth.getInstance().signOut();
            // Clear the User session data
            // and redirect user to LoginActivity
            SharedPreferences preferences =getSharedPreferences("SESSION",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(NavigationActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }



    private void configureNavigationDrawer(NavigationView nv)
    {

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                configureDefaultItemNavigationDrawer(id);
                return true;
            }
        });

    }


}
