package com.example.mj975.woder_woman.adpater;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.adpater.viewholder.AbstractViewHolder;
import com.example.mj975.woder_woman.adpater.viewholder.ExpeditionViewHolder;
import com.example.mj975.woder_woman.data.Expedition;

public class ExpeditionAdapter<Expedition> extends AbstractRecyclerAdapter<Expedition> {

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expedition_list_item, viewGroup, false);
        return new ExpeditionViewHolder(view);
    }
}
