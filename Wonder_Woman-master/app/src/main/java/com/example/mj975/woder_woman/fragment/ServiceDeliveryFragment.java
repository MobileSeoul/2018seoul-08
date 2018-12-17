package com.example.mj975.woder_woman.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.activity.SplashActivity;
import com.example.mj975.woder_woman.adpater.DeliveryAdapter;
import com.example.mj975.woder_woman.data.Delivery;
import com.example.mj975.woder_woman.data.Event;
import com.example.mj975.woder_woman.service.DatabaseClient;

import java.io.IOException;
import java.util.ArrayList;

public class ServiceDeliveryFragment extends Fragment {

    private ArrayList<Delivery> list;
    private ArrayList<Delivery> searchList;
    private DeliveryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_delivery, container, false);
        searchList = new ArrayList<>();
        adapter = new DeliveryAdapter();

        new BackgroundTask().execute();

        EditText editText = v.findViewById(R.id.text_address);

        ImageView search = v.findViewById(R.id.find_button);
        search.setOnClickListener(view -> {
            if (editText.getText().length() > 0) {
                new SearchTask(editText.getText().toString()).execute();
            } else {
                new BackgroundTask().execute();
            }
        });

        RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return v;
    }

    private class SearchTask extends AsyncTask<Void, Void, Void> {
        private String query;

        public SearchTask(String query) {
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            searchList.clear();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                searchList = DatabaseClient.getInstance().getDeliveryList(query).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (searchList != null && searchList.size() > 0) {
                adapter.setItems(searchList);
                adapter.notifyDataSetChanged();
            } else {
                adapter.setItems(list);
                Snackbar.make(getView(), "검색 결과가 없습니다.", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                list = DatabaseClient.getInstance().getAllDelivery().execute().body();
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
