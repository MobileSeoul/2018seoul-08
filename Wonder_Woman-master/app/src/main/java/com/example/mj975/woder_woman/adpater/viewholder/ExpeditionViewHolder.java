package com.example.mj975.woder_woman.adpater.viewholder;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.data.Expedition;

public class ExpeditionViewHolder extends AbstractViewHolder<Expedition> {
    private TextView email;
    private TextView location;
    private TextView time;
    private TextView end;

    public ExpeditionViewHolder(@NonNull View itemView) {
        super(itemView);
        email = itemView.findViewById(R.id.email);
        location = itemView.findViewById(R.id.loc);
        time = itemView.findViewById(R.id.time);
        end = itemView.findViewById(R.id.end);
    }

    @Override
    public void onBindView(@NonNull Expedition item, int position) {
        email.setText(item.getEmail());
        location.setText(item.getLocation());
        time.setText(item.getTime());
        if (item.isEnd()) {
            end.setTextColor(Color.rgb(240, 10, 10));
            end.setText("모집 완료");
        } else {
            end.setTextColor(Color.rgb(10, 240, 10));
            end.setText("모집 중");
        }
    }

}
