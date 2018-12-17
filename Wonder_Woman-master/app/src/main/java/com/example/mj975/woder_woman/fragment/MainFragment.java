package com.example.mj975.woder_woman.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.adpater.DangerZoneAdapter;
import com.example.mj975.woder_woman.adpater.ImageViewPageAdapter;
import com.example.mj975.woder_woman.adpater.ReportAdapter;
import com.example.mj975.woder_woman.data.Event;
import com.example.mj975.woder_woman.data.Report;
import com.example.mj975.woder_woman.data.Toilet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private ArrayList<Event> events;
    private ArrayList<Toilet> toilets;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        ViewPager viewPager = v.findViewById(R.id.viewPager);

        TextView reportAlert = v.findViewById(R.id.report_view_alert);
//        TextView expeditionAlert = v.findViewById(R.id.expedition_view_alert);
        TextView expedtitionBoard = v.findViewById(R.id.expedition_board);
        expedtitionBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpeditionBoardFragment f= new ExpeditionBoardFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, f)
                        .addToBackStack(null)
                        .commit();
            }
        });
        TextView dangerAlert = v.findViewById(R.id.danger_view_alert);

        String[] src = {""};

        ImageViewPageAdapter adapter = new ImageViewPageAdapter(getLayoutInflater(), src);
        viewPager.setAdapter(adapter);

        Bundle bundle = getArguments();

        if (bundle != null && bundle.getSerializable("EVENTS") != null) {
            events = (ArrayList<Event>) bundle.getSerializable("EVENTS");

            src = new String[events.size()];
            String[] href = new String[events.size()];
            for (int i = 0; i < events.size(); i++) {
                src[i] = events.get(i).getSrc();
                href[i] = events.get(i).getHref();
            }

            adapter.setResources(src);
            adapter.setOnItemClickListener(position -> {
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(href[position]));
                startActivity(browserIntent);
            });
            adapter.notifyDataSetChanged();
        }

        if (bundle != null && bundle.getSerializable("NEAR") != null) {
            toilets = (ArrayList<Toilet>) bundle.getSerializable("NEAR");
        }


        RecyclerView dangerRecyclerView = v.findViewById(R.id.danger_view);
//        RecyclerView expeditionRecyclerView = v.findViewById(R.id.expedition_view);
        RecyclerView myReportRecyclerView = v.findViewById(R.id.my_report_view);

        LinearLayoutManager layoutManagerDanger = new LinearLayoutManager(getContext());
//        LinearLayoutManager layoutManagerExpedition = new LinearLayoutManager(getContext());
        LinearLayoutManager layoutManagerReport = new LinearLayoutManager(getContext());

        layoutManagerDanger.setOrientation(LinearLayoutManager.HORIZONTAL);
//        layoutManagerExpedition.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManagerReport.setOrientation(LinearLayoutManager.HORIZONTAL);

        dangerRecyclerView.setLayoutManager(layoutManagerDanger);
//        expeditionRecyclerView.setLayoutManager(layoutManagerExpedition);
        myReportRecyclerView.setLayoutManager(layoutManagerReport);

        DangerZoneAdapter dangerZoneAdapter = new DangerZoneAdapter();
        if (toilets.size() < 1) {
            dangerAlert.setText("근처에 등록된 화장실이 없습니다.");
        } else {
            dangerZoneAdapter.setItems(toilets);
            dangerAlert.setVisibility(View.GONE);
            dangerRecyclerView.setVisibility(View.VISIBLE);
        }
        dangerRecyclerView.setAdapter(dangerZoneAdapter);

        ReportAdapter reportAdapter = new ReportAdapter();
        myReportRecyclerView.setAdapter(reportAdapter);
        ArrayList<Report> reports = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                    .setTimestampsInSnapshotsEnabled(true)
//                    .build();
//            firestore.setFirestoreSettings(settings);
            Query dbRef = db.collection("Report").whereEqualTo("email", user.getEmail());
            dbRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        reports.add(doc.toObject(Report.class));
                    }
                    reportAdapter.setItems(reports);
                    reportAdapter.notifyDataSetChanged();
                    reportAlert.setVisibility(View.GONE);
                    myReportRecyclerView.setVisibility(View.VISIBLE);
                    PersonFragment.reportNum = reports.size();
                } else {
                    Snackbar.make(getView(), "정보를 읽어오는데 실패하였습니다.", Snackbar.LENGTH_SHORT).show();
                }
            });
        } else {
            reportAlert.setText("로그인 해주세요.");
        }

        TabLayout tabLayout = v.findViewById(R.id.tab_dots);
        tabLayout.setupWithViewPager(viewPager);


        return v;
    }

}
