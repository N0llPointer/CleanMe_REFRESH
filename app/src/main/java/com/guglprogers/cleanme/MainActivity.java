package com.guglprogers.cleanme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapCircle;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.MapScreenMarker;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int TRASH = 0, GRASS = 1;

    ActionBarDrawerToggle toggle;

    private ArrayList<Problem> problems = new ArrayList<>();
    private ArrayList<MapMarker> markers = new ArrayList<>();
    private ArrayList<Problem> events = new ArrayList<>();

    public ArrayList<Problem> getProblems() {
        return problems;
    }

    public ArrayList<Problem> getEvents() {
        return events;
    }

    interface CameraListener{
        void setData(Bitmap bitmap);
    }

    public CameraListener getListener() {
        return listener;
    }

    public void setListener(CameraListener listener) {
        this.listener = listener;
    }

    CameraListener listener;

    public static final String APP_ID = "6ky1a2Wekw7bpKlj7Gcg", APP_CODE = "n5ywNPrMKH4IJNoTYPHaWQ", MODE = "retrieveAddresses";

    Map map;
    boolean show = true;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    Image image_fixed, image_add;
    MapScreenMarker screenMarker;
    boolean add_mode = false;
    CardView add_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add_mode)
                    showUI();
                else
                    onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        add_card = findViewById(R.id.cardview);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        bottomNavigationView = findViewById(R.id.design_navigation_view);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final MapFragment mapFragment = (MapFragment)
                getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(
                    OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    map = mapFragment.getMap();
                    map.setCenter(new GeoCoordinate(45,
                            42), Map.Animation.NONE);

                    mapFragment.getMapGesture().addOnGestureListener(new GestureListener(MainActivity.this),0, false);

                    fillProblems();

                    image_fixed = new Image();
                    image_add = new Image();
                    try {
                        image_fixed.setImageResource(R.drawable.ic_position_marker);
                        image_add.setImageResource(R.drawable.ic_add_position_marker);
                    }catch (Exception e){
                        Log.wtf("ERROR",e.getMessage());
                    }
                    setMarkers();

                } else {
                    Log.e("RNHereMapView", error.name());
                    Log.e("RNHereMapView", error.getDetails());
                    Log.e("RNHereMapView", error.getStackTrace());
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_add:
                        bottomNavigationView.animate().translationY(bottomNavigationView.getHeight()).setInterpolator(new DecelerateInterpolator()).setDuration(300).start();
                        add_card.animate().alpha(1.f).setStartDelay(300).start();
                        add_card.setClickable(true);
                        add_card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AddDialog.newInstance(map.getCenter()).show(getSupportFragmentManager(), "1");
                            }
                        });
                        if (screenMarker == null) {
                            screenMarker = new MapScreenMarker(new PointF(540, 960), image_add);
                        }
                        map.addMapObject(screenMarker);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        add_mode = true;
                        break;
                    case R.id.action_problems:
                        new ProblemsDialog().show(getSupportFragmentManager(), null);
                        break;
                    case R.id.action_events:
                        new EventsDialog().show(getSupportFragmentManager(),null);
                        break;
                }
                return true;
            }
        });

    }

    public void hideUI(){

    }

    public void showUI(){
        bottomNavigationView.animate().translationY(0).setInterpolator(new AccelerateInterpolator()).setDuration(300).start();
        add_card.animate().alpha(0.f).setStartDelay(300).start();
        add_card.setClickable(false);
        map.removeMapObject(screenMarker);
        toggle.syncState();
    }

    private void fillProblems(){
        problems.add(new Problem("Мусор возле парка",new GeoCoordinate(45.021130,41.921217),150,getDrawable(R.drawable.trash1),TRASH));
        problems.add(new Problem("Куча мусора в лесу",new GeoCoordinate(45.044028, 41.952484),100,getDrawable(R.drawable.trash2),TRASH));
        problems.add(new Problem("Кусты амброзии на обочине",new GeoCoordinate(45.035252, 41.904154),200,getDrawable(R.drawable.trash3),GRASS));
        problems.add(new Problem("Свалка за жилыми домами",new GeoCoordinate(45.001874, 41.913348),200,getDrawable(R.drawable.trash1),TRASH));
        problems.add(new Problem("Несколько десятков кустов",new GeoCoordinate(44.997038, 41.910727),250,getDrawable(R.drawable.trash2),GRASS));
        for(int i =0;i<problems.size();i++){
            final int position = i;
            GeoCoordinate coords = problems.get(position).getAddress();
            String crds = coords.getLatitude() + "," + coords.getLongitude() + ",10";
            App.getApi().getData(APP_ID,APP_CODE,MODE,crds).enqueue(new Callback<PostModel>() {
                @Override
                public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                    problems.get(position).setStreet(response.body().getResponse().getView().get(0).getResult().get(0).getLocation().getAddress().getLabel());
                }

                @Override
                public void onFailure(Call<PostModel> call, Throwable t) {

                }
            });
        }
    }

    private void setMarkers(){
        for(Problem problem : problems)
            addMarker(problem.getAddress(),problem.getRadius());
    }

    public void addProblem(Problem problem){
        problems.add(problem);
        addMarker(problem.getAddress(),problem.getRadius());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(add_mode){
            showUI();
            return;
        }else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void centerMapOnMarker(MapMarker marker){
        map.setCenter(marker.getCoordinate(),Map.Animation.LINEAR);
    }

    public void centerMapOnMarker(Problem problem){
        MapMarker marker = new MapMarker(problem.getAddress(),image_fixed);
        for(MapMarker mark : markers){
            if(mark.getCoordinate().equals(problem.getAddress())){
                marker = mark;
                break;
            }
        }
        map.setCenter(marker.getCoordinate(),Map.Animation.LINEAR);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addMarker(GeoCoordinate coords,int radius){
        MapMarker marker = new MapMarker(coords,image_fixed);
        MapCircle circle = new MapCircle(250.,coords);
        circle.setFillColor(0x32000000);
        markers.add(marker);
        map.addMapObject(marker);
        map.addMapObject(circle);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.wtf("ASDASDASD_ACTIVITY","ASDASDASDASDASD");
        listener.setData((Bitmap) data.getExtras().get("data"));
    }

    public void addEvent(Problem problem, Date date){
        problem.setDate(date);
        events.add(problem);
    }
}
