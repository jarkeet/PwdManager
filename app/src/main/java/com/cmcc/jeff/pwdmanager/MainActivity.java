package com.cmcc.jeff.pwdmanager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cmcc.jeff.pwdmanager.adapter.ItemTouchHelperCallback;
import com.cmcc.jeff.pwdmanager.adapter.NormalAdapter;
import com.cmcc.jeff.pwdmanager.event.MessageAddEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.cmcc.jeff.pwdmanager.UserManager.getUserInfo;

public class MainActivity extends AppCompatActivity {

    public static final String DIALOG_FRAGMENT_TAG = "MyFingerFragment";
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private CoordinatorLayout rootLayout;
    private FloatingActionButton fabBtn;

    private RecyclerView recyclerview;
    private List<UserInfo> dataList = new ArrayList<>();
    private NormalAdapter mDataAdapter;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initInstances();
        EventBus.getDefault().register(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initInstances() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.hello_world, R.string.hello_world);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);

        fabBtn = (FloatingActionButton) findViewById(R.id.fabBtn);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showSnackBar(getString(R.string.str_add_tips), getString(R.string.str_go_add));
                startActivity(new Intent(MainActivity.this, AddUserInfoActivity.class));
            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("My Passwords");

        initRecycleView();
    }

    private void showSnackBar(String content, String action) {
        Snackbar.make(rootLayout, content, Snackbar.LENGTH_SHORT)
                .setAction(action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, AddUserInfoActivity.class));
                    }
                }).show();
    }

    private void initRecycleView() {
        List<String> tags = UserManager.getTags(this);
        if(tags != null && tags.size() > 0) {
            for(String tag : tags) {
                UserInfo userInfo = new UserInfo();
                userInfo.setTag(tag);
                try {
                    userInfo = UserManager.getUserInfo(this, tag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(userInfo != null) {
                    userInfo.setUserName(userInfo.getUserName());
                    userInfo.setPassword(userInfo.getPassword());
                    dataList.add(userInfo);
                }
            }
        }

        recyclerview = (RecyclerView) findViewById(R.id.recircleView);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mDataAdapter = new NormalAdapter(this, dataList);
        recyclerview.setAdapter(mDataAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);
        //add ItemTouchHelper to recyclerview
        ItemTouchHelperCallback itemTouchHelperCallback = new ItemTouchHelperCallback(mDataAdapter);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerview);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageAddEvent event){
//        Toast.makeText(this, "on MainActivity message evnent.", Toast.LENGTH_SHORT).show();
        UserInfo userInfo = null;
        try {
            userInfo = getUserInfo(this, event.tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataList.add(userInfo);
        mDataAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
