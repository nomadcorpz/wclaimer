package com.cenmetrix.wclaimer;

import static com.cenmetrix.wclaimer.global.GlobalMethods.FirebaseAuthRequest;
import static com.cenmetrix.wclaimer.global.GlobalMethods.auth;
import static com.cenmetrix.wclaimer.global.GlobalVariables.db;
import static com.cenmetrix.wclaimer.global.GlobalVariables.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cenmetrix.wclaimer.MainActivityFragments.DashboardFragment;
import com.cenmetrix.wclaimer.MainActivityFragments.UserAccountFragment;
import com.cenmetrix.wclaimer.global.LoadingFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ImageButton logout_btn;
    static ImageButton home_IB;
    static ImageButton product_list_IB;
    static ImageButton product_list_on_warranty_IB;
    static ImageButton w_claimer_news_IB;
    static FragmentManager fragmentManager;
    static TextView navbar_home_TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        db.setFirestoreSettings(settings);
        // Initialize Firebase Auth
        FirebaseAuthRequest(MainActivity.this);

        initViews();
        initClicks();
        initClickEffects();
    }

    private static void getCurrentFragment() {
        Fragment f = fragmentManager.findFragmentById(R.id.navHostMainFragment);
        //  Log.e("TAG", "getCurrentFragment: "+f.getParentFragment().getTag() );
        Log.e("TAG", "5: " + fragmentManager.findFragmentByTag("DashboardFragment"));

    }

    private void initViews() {
        logout_btn = findViewById(R.id.logout_btn);
        home_IB = findViewById(R.id.home_IB);
        product_list_IB = findViewById(R.id.product_list_IB);
        product_list_on_warranty_IB = findViewById(R.id.product_list_on_warranty_IB);
        w_claimer_news_IB = findViewById(R.id.w_claimer_news_IB);
        navbar_home_TV = findViewById(R.id.navbar_home_TV);
    }


    private void initClicks() {
        logout_btn.setOnClickListener(view -> {
            auth.signOut();
            FirebaseAuthRequest(MainActivity.this);
        });
        home_IB.setOnClickListener(view -> {
            NavigationMainFragments("home_IB");

        });
        product_list_IB.setOnClickListener(view -> {
            NavigationMainFragments("product_list_IB");

        });
        product_list_on_warranty_IB.setOnClickListener(view -> {
            NavigationMainFragments("product_list_on_warranty_IB");

        });
        w_claimer_news_IB.setOnClickListener(view -> {
            NavigationMainFragments("w_claimer_news_IB");

        });
    }

    private static void navIconRefresh() {
        home_IB.setImageResource(R.drawable.ic_home);
        product_list_IB.setImageResource(R.drawable.ic_product_list);
        product_list_on_warranty_IB.setImageResource(R.drawable.ic_product_list_on_warranty);
        w_claimer_news_IB.setImageResource(R.drawable.ic_logo_merchant);
    }

    private void initClickEffects() {

    }

    public static void NavigationMainFragments(String switch_navigation_button) {
        switch (switch_navigation_button) {
            case "home_IB":
                navIconRefresh();
                home_IB.setImageResource(R.drawable.ic_home_pressed);
                fragmentManager.beginTransaction()
                        .replace(R.id.navHostMainFragment, DashboardFragment.class, null, "DashboardFragment")
                        .setReorderingAllowed(true)
                        .addToBackStack("replacement")
                        .commit();
                navbar_home_TV.setText("Wclaimer Merchant");
                break;

            case "product_list_IB":
                navIconRefresh();
                product_list_IB.setImageResource(R.drawable.ic_product_list_pressed);
                fragmentManager.beginTransaction()
                        .replace(R.id.navHostMainFragment, LoadingFragment.class, null, "LoadingFragment")
                        .setReorderingAllowed(true)
                        .addToBackStack("replacement")
                        .commit();
                navbar_home_TV.setText("Own products");
                break;

            case "product_list_on_warranty_IB":
                navIconRefresh();
                product_list_on_warranty_IB.setImageResource(R.drawable.ic_product_list_on_warranty_pressed);

                fragmentManager.beginTransaction()
                        .replace(R.id.navHostMainFragment, LoadingFragment.class, null, "LoadingFragment")
                        .setReorderingAllowed(true)
                        .addToBackStack("replacement")
                        .commit();
                navbar_home_TV.setText("Distributed product warranties");
                break;
            case "w_claimer_news_IB":
                navIconRefresh();
                w_claimer_news_IB.setImageResource(R.drawable.ic_logo_pressed);

                fragmentManager.beginTransaction()
                        .replace(R.id.navHostMainFragment, UserAccountFragment.class, null, "UserAccountFragment")
                        .setReorderingAllowed(true)
                        .addToBackStack("replacement")
                        .commit();
                navbar_home_TV.setText("Account Details");
                break;

            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.navHostMainFragment, LoadingFragment.class, null, "DashboardFragment")
                        .setReorderingAllowed(true)
                        .addToBackStack("replacement")
                        .commit();
                navbar_home_TV.setText("Loading");
                break;


        }
        getCurrentFragment();
    }

}