package com.example.ravintolaapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.ravintolaapp.fragments.CartFragment;
import com.example.ravintolaapp.fragments.MenuFragment;
import com.example.ravintolaapp.fragments.OrdersFragment;
import com.example.ravintolaapp.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();

        // Set default fragment to Menu
        if (savedInstanceState == null) {
            loadFragment(new MenuFragment(), "menu");
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                String tag = "";
                
                int itemId = item.getItemId();
                if (itemId == R.id.nav_menu) {
                    fragment = new MenuFragment();
                    tag = "menu";
                } else if (itemId == R.id.nav_cart) {
                    fragment = new CartFragment();
                    tag = "cart";
                } else if (itemId == R.id.nav_orders) {
                    fragment = new OrdersFragment();
                    tag = "orders";
                } else if (itemId == R.id.nav_profile) {
                    fragment = new ProfileFragment();
                    tag = "profile";
                }

                if (fragment != null) {
                    loadFragment(fragment, tag);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commit();
    }
}