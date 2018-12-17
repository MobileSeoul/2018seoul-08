package com.example.mj975.woder_woman.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.data.Expedition;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ExpeditionWriteDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.expedition_dialog, null);

        TextView okButton = view.findViewById(R.id.dialog_ok_button);
        TextView cancelButton = view.findViewById(R.id.dialong_cancel_button);
        String email = getArguments().getString("EMAIL");

        final EditText loc = view.findViewById(R.id.text_loc);
        final EditText time = view.findViewById(R.id.text_time);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loc.getText().length() > 0 && time.getText().length() > 0) {
                    String l = loc.getText().toString();
                    String t = time.getText().toString();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Expedition e = new Expedition(email, l, t, false, new ArrayList<String>());
                    db.collection("Board").document(email + l)
                            .set(e)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Snackbar.make(view, "글 작성에 성공하였습니다.", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(view, "글 작성에 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                    Intent intent = new Intent();
                    intent.putExtra("EXPEDITION",e);
                    getTargetFragment().onActivityResult(
                            getTargetRequestCode(), Activity.RESULT_OK, intent);
                    dismiss();
                } else {
                    Snackbar.make(getView(), "빈칸을 작성해 주세요.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(v -> dismiss());


        return new AlertDialog.Builder(getActivity()).setView(view).create();
    }

    public static ExpeditionWriteDialogFragment newInstance(String email) {
        ExpeditionWriteDialogFragment f = new ExpeditionWriteDialogFragment();

        Bundle args = new Bundle();
        args.putString("EMAIL", email);
        f.setArguments(args);

        return f;
    }


}
