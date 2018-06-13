package com.example.abhishek.cineverse.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.abhishek.cineverse.R;

import java.util.Objects;

public class SortDialogFragment extends DialogFragment {

    SortDialogListener mListener;
    private int position = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(R.string.sort_by)
                .setSingleChoiceItems(R.array.filter_sort, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        position = which;
                        mListener.onDialogItemClick(which);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SortDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SortDialogListener");
        }
    }

    public interface SortDialogListener {
        public void onDialogItemClick(int which);
    }
}
