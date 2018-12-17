package com.example.mj975.woder_woman.adpater.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.data.Scout;

public class ScoutViewHolder extends AbstractViewHolder<Scout> {
    private TextView title;
    private TextView number;

    public ScoutViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        number = itemView.findViewById(R.id.number);
    }

    @Override
    public void onBindView(@NonNull Scout item, int position) {
        title.setText(item.getName());
        number.setText(item.getNumber());
    }
}
