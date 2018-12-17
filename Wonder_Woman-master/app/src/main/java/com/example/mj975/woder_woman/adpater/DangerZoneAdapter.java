package com.example.mj975.woder_woman.adpater;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.adpater.viewholder.AbstractViewHolder;
import com.example.mj975.woder_woman.adpater.viewholder.DangerZoneViewHolder;
import com.example.mj975.woder_woman.data.Toilet;

import java.util.ArrayList;

public class DangerZoneAdapter<Toilet> extends AbstractRecyclerAdapter<Toilet> {

    @NonNull
    @Override
    public DangerZoneViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.toilet_list_item, viewGroup, false);
        return new DangerZoneViewHolder(view);
    }
}
