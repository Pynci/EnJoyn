package it.unimib.enjoyn.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unimib.enjoyn.R;

public class MainButtonMenuActivity extends AppCompatActivity {

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_button_menu);

        Toolbar toolBar = findViewById(R.id.activityMainButtonMenu_topAppbar);
        setSupportActionBar(toolBar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.activityMainButtonMenu_navHostFragment);

        navController = navHostFragment.getNavController();

        BottomNavigationView bottomNav = findViewById(R.id.activityMainButtonMenu_bottomNavigation);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.fragmentDiscover,
                R.id.fragmentTodo,
                R.id.fragmentNewEventMap,
                R.id.friendsFragment,
                R.id.profileFragment
                ).build();




        // For the Toolbar
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        // For the BottomNavigationView
        NavigationUI.setupWithNavController(bottomNav, navController);


    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }


}