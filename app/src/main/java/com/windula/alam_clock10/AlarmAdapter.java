package com.windula.alam_clock10;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private List<AlarmView> malarmViews;
    private AlarmDbHelper dbHelper;
    private AlarmReceiver ar;

    private final int REMOVE_ALARM=0;

    public AlarmAdapter(List<AlarmView> malarmViews) {
        this.malarmViews = malarmViews;
    }

    public AlarmReceiver getAr() {
        return ar;
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        AlarmView av=malarmViews.get(i);

        viewHolder.tv_time.setText(av.getTime());
        viewHolder.tv_title.setText(av.getTitle());
        viewHolder.iv_delete.setClickable(true);
        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmView delete=malarmViews.get(i);

                if(Alarm.removeAlarm(new AlarmDbHelper(v.getContext()),delete.getTime())){
                    Toast.makeText(v.getContext(),"Alarm Deleted",Toast.LENGTH_SHORT).show();
                    malarmViews.remove(i);

                    Log.i("Alarm Remove",""+i);
                    //register broadcaster
                   /* ar=new AlarmReceiver();
                    IntentFilter filter = new IntentFilter("com.windula.alarm_clock10.ALARM_RECEIVER2");
                    //filter.addAction(Intent.);
                    v.getContext().registerReceiver(ar, filter);*/

                    //set intent to broadcaster
                    Intent broadcast = new Intent();
                    broadcast.setAction("com.windula.alarm_clock10.ALARM_RECEIVER2");
                    broadcast.putExtra("data",REMOVE_ALARM);
                    v.getContext().sendBroadcast(broadcast);

                    //notify recyclerviewchange
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, getItemCount());

                    //unregister receiver
                    //v.getContext().unregisterReceiver(ar);
                }
                else {
                    Toast.makeText(v.getContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        LocalBroadcastManager.getInstance(recyclerView.getContext()).unregisterReceiver(ar);
        Log.i("AlarmAdapter","onDetachedFromRecyclerView");
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
