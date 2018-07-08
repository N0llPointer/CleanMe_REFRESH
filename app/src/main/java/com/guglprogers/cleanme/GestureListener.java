package com.guglprogers.cleanme;

import android.graphics.PointF;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.ViewObject;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GestureListener extends MapGesture.OnGestureListener.OnGestureListenerAdapter{
    public static final String APP_ID = "6ky1a2Wekw7bpKlj7Gcg", APP_CODE = "n5ywNPrMKH4IJNoTYPHaWQ", MODE = "retrieveAddresses";
    private MainActivity activity;
    boolean show = true;
    ArrayList<Problem> problems;


    public GestureListener(MainActivity activity) {
        super();
        this.activity = activity;
        problems = activity.getProblems();
    }

    @Override
    public boolean onMapObjectsSelected(List<ViewObject> list) {
        for(ViewObject v: list){
            MapObject m = (MapObject) v;
            if(m instanceof MapMarker){
                MapMarker marker = (MapMarker) v;
                GeoCoordinate coords = marker.getCoordinate();
                for(Problem problem : problems){
                    if(coords.equals(problem.getAddress())) {
                        onMarkerTapped(marker, problem);
                        break;
                    }
                }
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onTapEvent(PointF pointF) {
        return false;
    }

    @Override
    public boolean onLongPressEvent(PointF pointF) {
        return false;
    }


    public void onMarkerTapped(MapMarker marker,Problem problem){
        activity.centerMapOnMarker(marker);
        MarkerInfoDialog.newInstance(problem).show(activity.getSupportFragmentManager(),null);
    }

}
