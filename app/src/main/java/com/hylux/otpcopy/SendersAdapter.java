package com.hylux.otpcopy;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.hylux.otpcopy.MainActivity.otpSenders;

public class SendersAdapter extends RecyclerView.Adapter<SendersAdapter.SendersViewHolder> {

    private ArrayList<Long> otpSenderNumbers = new ArrayList<>(otpSenders.keySet());

    static class SendersViewHolder extends RecyclerView.ViewHolder {

        TextView nameText, numberText;

        SendersViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.name);
            numberText = itemView.findViewById(R.id.number);
        }
    }

    @NonNull
    @Override
    public SendersAdapter.SendersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ConstraintLayout rootLayout = (ConstraintLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_sender, viewGroup, false);
        return new SendersViewHolder(rootLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull SendersViewHolder sendersViewHolder, int i) {
        sendersViewHolder.numberText.setText(otpSenderNumbers.get(i).toString());
        sendersViewHolder.nameText.setText(otpSenders.get(otpSenderNumbers.get(i)));
        Log.d("senderNumber", otpSenderNumbers.get(i).toString());
        Log.d("senderName", otpSenders.get(otpSenderNumbers.get(i)));
    }

    @Override
    public int getItemCount() {
        return otpSenderNumbers.size();
    }

    public void update() {
        otpSenderNumbers = new ArrayList<>(otpSenders.keySet());
        notifyDataSetChanged();
    }
}
