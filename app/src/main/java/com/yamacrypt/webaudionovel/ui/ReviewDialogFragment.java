package com.yamacrypt.webaudionovel.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.yamacrypt.webaudionovel.DataStore;
import com.yamacrypt.webaudionovel.R;

import java.io.Serializable;

public class ReviewDialogFragment extends DialogFragment {

    public interface ReviewDialogFragmentListener extends Serializable {
        void onDoReviewButtonClick();
    }

    private ReviewDialogFragmentListener listener;

    public static ReviewDialogFragment newInstance(ReviewDialogFragmentListener listener) {
        ReviewDialogFragment dialogFragment = new ReviewDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("listener", listener);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            listener =
                    (ReviewDialogFragmentListener)getArguments().getSerializable("listener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Activity activity = getActivity();
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);

        View dialogView =
                LayoutInflater.from(activity).inflate(R.layout.review_dialog_layout ,null);

        Button topButton =
                dialogView.findViewById(R.id.top_button);
        Button centerButton =
                dialogView.findViewById(R.id.center_button);
        Button bottomButton =
                dialogView.findViewById(R.id.bottom_button);

        SharedPreferences pref= DataStore.getSharedPreferences(getContext());
        final SharedPreferences.Editor editor=pref.edit();
        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt(DataStore.review,-1);
                editor.apply();
                listener.onDoReviewButtonClick();
            }
        });

        centerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // editor.putInt(DataStore.review,0);
             //   editor.apply();
                dismiss();
            }
        });
        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt(DataStore.review, DataStore.review_span);
                editor.apply();
                dismiss();
            }
        });

        dialog.setView(dialogView);

        setCancelable(false);

        return dialog.create();
    }

    private View.OnClickListener cancelClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            };
}