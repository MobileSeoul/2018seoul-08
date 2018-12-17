package com.example.mj975.woder_woman.adpater.viewholder;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.data.Report;
import com.example.mj975.woder_woman.data.Toilet;

public class ReportViewHolder extends AbstractViewHolder<Report> {
    private TextView titleText;
    private TextView infoText;

    public ReportViewHolder(@NonNull View itemView) {
        super(itemView);
        titleText = itemView.findViewById(R.id.report_title);
        infoText = itemView.findViewById(R.id.report_info);
    }

    @Override
    public void onBindView(@NonNull Report item, int position) {
        String[] s = item.getAddress().split(" ");
        if (s.length > 2) {
            titleText.setText(s[s.length - 1] + " " + s[s.length - 2]);
        }
        infoText.setText("처리중");
        infoText.setTextColor(Color.rgb(240, 10, 10));
    }
}
