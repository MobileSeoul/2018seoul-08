package com.example.mj975.woder_woman.adpater.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.data.Delivery;

public class DeliveryViewHolder extends AbstractViewHolder<Delivery> {
    private TextView address;
    private TextView location;
    private TextView postal;

    public DeliveryViewHolder(@NonNull View itemView) {
        super(itemView);
        address = itemView.findViewById(R.id.address);
        location = itemView.findViewById(R.id.loc);
        postal = itemView.findViewById(R.id.postal);
    }

    @Override
    public void onBindView(@NonNull Delivery item, int position) {
        address.setText(item.getAddress());
        location.setText(item.getName());
        postal.setText("우편번호 : " + item.getPostal());
    }
}
