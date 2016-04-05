package com.lethalminds.udonate.client.activities;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lethalminds.udonate.R;
import com.lethalminds.udonate.client.adapters.NavDrawerListAdapter;
import com.lethalminds.udonate.client.fragments.AboutUsFragment;
import com.lethalminds.udonate.client.fragments.DonateFragment;
import com.lethalminds.udonate.client.fragments.LoginFragment;
import com.lethalminds.udonate.client.fragments.NewsFragment;
import com.lethalminds.udonate.client.fragments.PaymentFragment;
import com.lethalminds.udonate.client.fragments.ProfileEditFragment;
import com.lethalminds.udonate.client.fragments.ProfileFragment;
import com.lethalminds.udonate.client.fragments.TransactionFragment;
import com.lethalminds.udonate.client.utilities.NavDrawerItem;
import com.lethalminds.udonate.client.utilities.UserLocalStore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoginFragment.OnFragmentInteractionListener,
        NewsFragment.OnFragmentInteractionListener, DonateFragment.OnFragmentInteractionListener,
        AboutUsFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        ProfileEditFragment.OnFragmentInteractionListener, TransactionFragment.OnFragmentInteractionListener,
        PaymentFragment.OnFragmentInteractionListener{

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private LinearLayout navDrawerLayout;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                //Set drawer opener only when there is 0 backstack fragments
                if(fragmentManager.getBackStackEntryCount() == 0){
                    mDrawerToggle.setDrawerIndicatorEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);
                }
                else
                {
                    mDrawerToggle.setDrawerIndicatorEnabled(false);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);
                }
                mDrawerToggle.syncState();
            }
        });
        //check user logged in
        userLocalStore = new UserLocalStore(this);

        //Initialize nav menu
        mTitle = mDrawerTitle = getTitle();
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerLayout = (LinearLayout) findViewById(R.id.nav_drawer_list);
        navDrawerItems = new ArrayList<NavDrawerItem>();

        //Create and Add Nav menu items
        NavDrawerItem newsItem = new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1));
        NavDrawerItem donateItem =new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1));
        NavDrawerItem profileItem =new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1));
        NavDrawerItem loginItem =new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1));
        NavDrawerItem aboutUsItem =new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1));
        navDrawerItems.add(newsItem);
        navDrawerItems.add(donateItem);
        navDrawerItems.add(userLocalStore.getLoggedInUser() != null ? profileItem : loginItem);
        navDrawerItems.add(aboutUsItem);

        navMenuIcons.recycle();
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            setTitle(navMenuTitles[0]);
            displayFragment(new NewsFragment());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else if(item.getItemId() == android.R.id.home){
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(navDrawerLayout);

            switch (position){
                case 0: // news screen
                    displayFragment(new NewsFragment());
                    break;
                case 1:
                    displayFragment(new DonateFragment());
                    break;
                case 2:
                    if(userLocalStore.getLoggedInUser() != null) displayFragment(new ProfileFragment());
                    else displayFragment(new LoginFragment());
                    break;
                case 3:
                    displayFragment(new AboutUsFragment());
                    break;
                default:
                    break;
            }
        }
    }

    public void displayFragment(Fragment frag){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, frag);
        fragmentTransaction.commit();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
