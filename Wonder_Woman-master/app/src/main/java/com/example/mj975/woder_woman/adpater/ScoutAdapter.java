package com.example.mj975.woder_woman.adpater;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.adpater.viewholder.ScoutViewHolder;

public class ScoutAdapter<Scout> extends AbstractRecyclerAdapter<Scout> {
    @NonNull
    @Override
    public ScoutViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scout_list_item, viewGroup, false);

        return new ScoutViewHolder(view);
    }
}
