package com.example.mj975.woder_woman.adpater;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.adpater.viewholder.AbstractViewHolder;
import com.example.mj975.woder_woman.adpater.viewholder.DeliveryViewHolder;
import com.example.mj975.woder_woman.data.Delivery;

public class DeliveryAdapter extends AbstractRecyclerAdapter<Delivery> {
    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivery_list_item, viewGroup, false);
        return new DeliveryViewHolder(view);
    }
}
