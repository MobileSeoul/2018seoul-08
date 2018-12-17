package com.example.mj975.woder_woman.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.adpater.AbstractRecyclerAdapter;
import com.example.mj975.woder_woman.adpater.ExpeditionAdapter;
import com.example.mj975.woder_woman.data.Expedition;
import com.example.mj975.woder_woman.data.Report;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExpeditionBoardFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseUser user;

    private ExpeditionAdapter adapter;
    private List<Expedition> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expedition_board, container, false);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());

        FloatingActionButton writeButton = view.findViewById(R.id.write_button);
        writeButton.setOnClickListener(v -> {
            if (user != null) {
                FragmentManager fm = getFragmentManager();
                DialogFragment dialogFragment = ExpeditionWriteDialogFragment.newInstance(user.getEmail());
                dialogFragment.setTargetFragment(ExpeditionBoardFragment.this, 100);
                dialogFragment.show(fm, "InputDialog");

            } else
                Snackbar.make(getView(), "로그인 해주세요.", Snackbar.LENGTH_SHORT).show();
        });

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);


        adapter = new ExpeditionAdapter();
        adapter.setOnItemClickListener((AbstractRecyclerAdapter.OnItemClickListener<Expedition>) (item, position) -> {
            if (user != null)
                showDialog(item);
            else
                Snackbar.make(getView(), "로그인 해주세요.", Snackbar.LENGTH_SHORT).show();
        });

        list = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Board")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(document.toObject(Expedition.class));
                        }
                        adapter.setItems(list);
                        adapter.notifyDataSetChanged();
                    } else {
                        Snackbar.make(getView(), "정보를 읽어오는데 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
                    }
                });
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void showDialog(Expedition expedition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("참가 하기");
        builder.setMessage("이 원정대에 참가 하시겠습니까?");
        builder.setPositiveButton("예",
                (dialog, which) -> {
                    List<String> p = expedition.getParty();
                    p.add(user.getEmail());

                    db.collection("Board").document(expedition.getEmail() + expedition.getLocation())
                            .update("party", p)
                            .addOnSuccessListener(aVoid -> {
                                dialog.dismiss();
                                Snackbar.make(getView(), "참가 신청에 성공하였습니다.", Snackbar.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> {
                        dialog.dismiss();
                        Snackbar.make(getView(), "참가 신청에 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
                    });
                });
        builder.setNegativeButton("아니오",
                (dialog, which) -> {
                });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        list.add((Expedition) data.getSerializableExtra("EXPEDITION"));
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }
}
