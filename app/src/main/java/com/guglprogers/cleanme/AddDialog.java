package com.guglprogers.cleanme;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.guglprogers.Address;
import com.here.android.mpa.common.GeoCoordinate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDialog extends BottomSheetDialogFragment implements MainActivity.CameraListener{
    public static final String COORDS = "COORDS";
    View main_view;
    int circle_radius = 200;
    MainActivity activity;
    EditText edit_text_adress;
    EditText edit_text_description;
    ImageButton image;
    GeoCoordinate coords;
    boolean camera_intent = false;
    boolean image_fullscreen = false;

    @Override
    public void setData(Bitmap bitmap) {
        image.setImageBitmap(bitmap);
    }

    public static AddDialog newInstance(GeoCoordinate coords){
        AddDialog add = new AddDialog();
        add.coords = coords;
        Bundle bundle = new Bundle();
        String coord = coords.getLatitude() + "," + coords.getLongitude() + ",10";
        bundle.putString(COORDS,coord);
        add.setArguments(bundle);
        return add;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(camera_intent) {
            image.setImageResource(R.drawable.photo);
            camera_intent = false;
            image.setOnClickListener(null);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.add_dialog_layout,container,false);
        main_view.findViewById(R.id.dialog_close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        edit_text_adress = main_view.findViewById(R.id.dialog_address);
        getAddress();
        final TextView radius = main_view.findViewById(R.id.dialog_radius_textview);
        activity = (MainActivity) getActivity();
        activity.setListener(this);
        SeekBar seekBar = main_view.findViewById(R.id.dialog_radius_seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                circle_radius = 220 + progress * 10;
                radius.setText(circle_radius + " м");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        edit_text_description = main_view.findViewById(R.id.dialog_description);
        image = main_view.findViewById(R.id.dialog_add_photo);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                camera_intent = true;
                if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    getActivity().startActivityForResult(takePictureIntent, -1);
                }
            }
        });

        ((TextView)main_view.findViewById(R.id.dialog_radius_textview)).setText("200 м");

        main_view.findViewById(R.id.dialog_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = edit_text_adress.getText().toString();
                String s_d = edit_text_description.getText().toString();
                Problem problem = new Problem(s_d,coords,circle_radius,getResources().getDrawable(R.drawable.photo),0);
                activity.addProblem(problem);
                dismiss();
            }
        });


        return main_view;
    }


    public void getAddress(){
        App.getApi().getData(GestureListener.APP_ID,GestureListener.APP_CODE,GestureListener.MODE,getArguments().getString(COORDS))
                .enqueue(new Callback<PostModel>() {
                    @Override
                    public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                        Address address = response.body().getResponse().getView().get(0).getResult().get(0).getLocation().getAddress();
                        edit_text_adress.setText(address.getLabel());

                    }

                    @Override
                    public void onFailure(Call<PostModel> call, Throwable t) {

                    }
                });
    }
}
