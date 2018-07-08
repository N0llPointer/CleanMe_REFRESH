package com.guglprogers.cleanme;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.guglprogers.Address;
import com.here.android.mpa.common.GeoCoordinate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.guglprogers.cleanme.AddDialog.COORDS;

public class MarkerInfoDialog extends BottomSheetDialogFragment {
    View main_view;
    Problem problem;

    public static MarkerInfoDialog newInstance(Problem problem){
        MarkerInfoDialog dialog = new MarkerInfoDialog();
        dialog.problem = problem;
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.marker_info_dialog,container,false);
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog b = (BottomSheetDialog) dialog;
                View bottomSheetInternal = b.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        main_view.findViewById(R.id.dialog_add_close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView textView = main_view.findViewById(R.id.dialog_marker_radius_textview);
        textView.setText("Радиус " + problem.getRadius() + " м");
        textView = main_view.findViewById(R.id.dialog_marker_description);
        textView.setText(problem.getDescription());

        ImageButton image  = main_view.findViewById(R.id.dialog_marker_image);
        image.setImageDrawable(problem.getPhoto());

        final TextView addressText = main_view.findViewById(R.id.dialog_marker_dialog);
        addressText.setText("Загрузка...");
        GeoCoordinate coord = problem.getAddress();

        if(problem.hasStreet())
            addressText.setText(problem.getStreet());
        else
            App.getApi().getData(GestureListener.APP_ID,GestureListener.APP_CODE,GestureListener.MODE,coord.getLatitude() + "," + coord.getLongitude() + ",10")
                    .enqueue(new Callback<PostModel>() {
                        @Override
                        public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                            Address address = response.body().getResponse().getView().get(0).getResult().get(0).getLocation().getAddress();
                            addressText.setText(address.getLabel());
                        }

                        @Override
                        public void onFailure(Call<PostModel> call, Throwable t) {
                            Snackbar.make(main_view,"NETWORK FAIL",Snackbar.LENGTH_SHORT).show();
                        }
                    });
        main_view.findViewById(R.id.dialog_marker_info_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDialog.newInstance(problem).show(getActivity().getSupportFragmentManager(),null);
                dismiss();
            }
        });
        return main_view;
    }
}
