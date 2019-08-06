package com.bazaar.forshopping;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class DashboardActivity extends BazaarActivity implements View.OnClickListener {

    public ResideMenu resideMenu;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemAdvantages;
    private ResideMenuItem itemSettings;
    private ResideMenuItem itemProfile;
    private int scaleDirection = ResideMenu.DIRECTION_LEFT;
    public static Activity this_activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        this_activity = this;

        setUpMenu();
    }

    private void setUpMenu(){
        resideMenu = new ResideMenu(this);

        resideMenu.setBackground(R.drawable.background);
        resideMenu.attachToActivity(this);
        resideMenu.setScaleValue(0.7f);
        resideMenu.setShadowVisible(true);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.setUse3D(true);

        // creating menu items
        itemHome = new ResideMenuItem(this, R.drawable.home, "İstekler");
        itemAdvantages = new ResideMenuItem(this, R.drawable.home, "Avantajlar");
        itemSettings= new ResideMenuItem(this, R.drawable.home, "Ayarlar");
        itemProfile = new ResideMenuItem(this, R.drawable.home, "Profil");

        itemHome.setOnClickListener(this);
        itemAdvantages.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        itemProfile.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, scaleDirection);
        resideMenu.addMenuItem(itemAdvantages, scaleDirection);
        resideMenu.addMenuItem(itemSettings, scaleDirection);
        resideMenu.addMenuItem(itemProfile, scaleDirection);

        changeFragment(new HomeFragment());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        }, 1000);
    }

    @Override
    public void onClick(View view) {
        if(view == itemHome){
            changeFragment(new HomeFragment());
        } else if(view == itemAdvantages){

        } else if(view == itemSettings){

        } else if(view == itemProfile){
            changeFragment(new ProfileFragment());
        }

        closeResideMenu();
    }

    private void changeFragment(Fragment targetFragment){
        // resideMenu.clearIgnoredViewList();
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dashboard_fragment, targetFragment);
        fragmentTransaction.show(targetFragment);
        fragmentTransaction.commit();
    }

    public ResideMenu getResideMenu() { return this.resideMenu; }

    public void closeResideMenu() { getResideMenu().closeMenu(); }
}
