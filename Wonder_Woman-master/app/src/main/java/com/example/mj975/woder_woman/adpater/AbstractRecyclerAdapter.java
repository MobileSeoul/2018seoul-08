package com.example.mj975.woder_woman.adpater;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.example.mj975.woder_woman.adpater.viewholder.AbstractViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractRecyclerAdapter<T> extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<T> items = Collections.emptyList();

    private OnItemClickListener<T> onItemClickListener;

    public interface OnItemClickListener<T> {
        void onItemClick(T item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AbstractRecyclerAdapter() {
    }

    public AbstractRecyclerAdapter(List<T> items) {
        this.items = items;
    }

    @Override
    public void onBindViewHolder(@NonNull final AbstractViewHolder holder, final int position) {
        holder.onBindView(getItem(holder.getAdapterPosition()), position);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getItem(position), position);
            }
        });
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public int get(@NonNull T item) {
        return items.indexOf(item);
    }

    public void setItems(@NonNull List<T> items) {
        this.items.clear();
        this.items = items;
    }


    public void clear() {
        this.items.clear();
    }

    public void addItemToIndex(int index, @NonNull T item) {
        items.add(index, item);
    }

    public void addItem(@NonNull T item) {
        items.add(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}