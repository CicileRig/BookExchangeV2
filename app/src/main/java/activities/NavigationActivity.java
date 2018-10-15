package activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.widget.Toast;


import com.example.bcs.bookexchangev2.R;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;

import fragments.Books_fragment;
import fragments.Events_fragment;
import fragments.Library_fragment;

public class NavigationActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    private android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_activity);
        /******************************************** Toolbar configuration *******************************************/
        //getting the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        //placing toolbar in place of actionbar
        toolbar.inflateMenu(R.menu.search_bar_menu);
        //set navigation icon in the toolbar
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu));

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
                .textSize(18);
        bmb.addBuilder(builder);

        HamButton.Builder builder2 = new HamButton.Builder()
                .normalImageRes(R.drawable.ic_add_event)
                .normalText("Créer un evenement")
                .textSize(20);
        bmb.addBuilder(builder2);

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed(); // Comment this super call to avoid calling finish() or fragmentmanager's backstack pop operation.
        Intent intent_profil = new Intent(NavigationActivity.this, ProfilActivity.class);
        startActivity(intent_profil);
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


    private void configureDefaultItemNavigationDrawer( int id_item) {

        if(id_item == R.id.nav_library)
        {
            Library_fragment library_fragment = new Library_fragment();
            fragmentManager.beginTransaction().replace(R.id.dynamic_fragment_frame_layout, library_fragment).commit();
            dl.closeDrawers();
        }else if (id_item == R.id.nav_books){
            Books_fragment books_fragment = new Books_fragment();
            fragmentManager.beginTransaction().replace(R.id.dynamic_fragment_frame_layout, books_fragment).commit();
            dl.closeDrawers();

        }else if (id_item == R.id.nav_event){

            Events_fragment events_fragment = new Events_fragment();
            fragmentManager.beginTransaction().replace(R.id.dynamic_fragment_frame_layout, events_fragment).commit();
            dl.closeDrawers();
        }else if (id_item == R.id.nav_log_out){

            dl.closeDrawers();
            Toast.makeText(NavigationActivity.this, "deconnexion",Toast.LENGTH_SHORT).show();
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
