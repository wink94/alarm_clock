package com.windula.alam_clock10;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private List<AlarmView> malarmViews;

    public AlarmAdapter(List<AlarmView> malarmViews) {
        this.malarmViews = malarmViews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context=viewGroup.getContext();
        LayoutInflater inflater=LayoutInflater.from(context);

        View alarmview=inflater.inflate(R.layout.alarm_layout,viewGroup,false);

        ViewHolder viewHolder=new ViewHolder(alarmview);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        AlarmView av=malarmViews.get(i);

        viewHolder.tv_time.setText(av.getTime());
        viewHolder.tv_title.setText(av.getTitle());
        viewHolder.iv_delete.setClickable(true);
        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return malarmViews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_time;
        public TextView tv_title;
        public ImageView iv_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_time=(TextView) itemView.findViewById(R.id.alarm_time);
            tv_title=(TextView)itemView.findViewById(R.id.alarm_title);
            iv_delete=(ImageView) itemView.findViewById(R.id.alarm_delete);
        }
    }





}