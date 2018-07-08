package com.guglprogers.cleanme;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProblemsDialog extends BottomSheetDialogFragment implements ProblemsAdapter.ProblemsClickListener{

    MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View main_view = inflater.inflate(R.layout.problems_dialog,container,false);
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog b = (BottomSheetDialog) dialog;
                View bottomSheetInternal = b.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        activity = (MainActivity) getActivity();
        RecyclerView recycler = main_view.findViewById(R.id.recyclerView_problem);
        recycler.setLayoutManager(new LinearLayoutManager(activity));
        ProblemsAdapter adapter = new ProblemsAdapter(activity.getProblems());
        adapter.setListener(this);
        recycler.setAdapter(adapter);
        main_view.findViewById(R.id.dialog_problem_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return main_view;
    }

    public void onClick(Problem problem){
        activity.centerMapOnMarker(problem);
        MarkerInfoDialog.newInstance(problem).show(activity.getSupportFragmentManager(),null);
        dismiss();

    }
}
