package com.example.mj975.woder_woman.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.adpater.AbstractRecyclerAdapter;
import com.example.mj975.woder_woman.adpater.ScoutAdapter;
import com.example.mj975.woder_woman.data.SafeHouse;
import com.example.mj975.woder_woman.data.Scout;
import com.example.mj975.woder_woman.service.DatabaseClient;

import java.io.IOException;
import java.util.List;

public class ServiceScoutFragment extends Fragment {

    private List<Scout> list;
    private ScoutAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_scout, container, false);
        new BackgroundTask().execute();

        TextView main = v.findViewById(R.id.main_num);
        main.setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:120"));
            startActivity(callIntent);
        });

        RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new ScoutAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((AbstractRecyclerAdapter.OnItemClickListener<Scout>) (item, position) -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + item.getName().replace("-", "")));
            startActivity(callIntent);
        });

        return v;
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                list = DatabaseClient.getInstance().getAllScout().execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            adapter.setItems(list);
            adapter.notifyDataSetChanged();
        }
    }
}
