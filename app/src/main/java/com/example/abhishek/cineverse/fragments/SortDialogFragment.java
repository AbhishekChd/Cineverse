package com.example.abhishek.cineverse.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.abhishek.cineverse.R;
import com.example.abhishek.cineverse.data.AppConstants;

import java.util.Objects;

public class SortDialogFragment extends DialogFragment {

    private SortDialogListener mListener;
    private int position = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(R.string.sort_by)
                .setSingleChoiceItems(R.array.filter_sort, position, (dialog, which) -> {
                    position = which;
                    mListener.onDialogItemClick(which);
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
        SharedPreferences preferences =
                context.getSharedPreferences(
                        getString(R.string.sort_preference),
                        Context.MODE_PRIVATE);
        position = preferences.getInt(getString(R.string.sort_by_key), AppConstants.POPULAR);
    }

    public interface SortDialogListener {
        void onDialogItemClick(int which);
    }
}
