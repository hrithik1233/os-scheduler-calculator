package com.example.osschedulercalculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class GanttRecyclerAdapter extends RecyclerView.Adapter<GanttRecyclerAdapter.MyViewHolder> {

Context context;
    ArrayList<Process> list;

    public GanttRecyclerAdapter(Context context, ArrayList<Process> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public GanttRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.gantt_instance,parent,false);
        return  new MyViewHolder(view);
    }

    @SuppressLint({"SuspiciousIndentation", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull GanttRecyclerAdapter.MyViewHolder holder, int position) {
             holder.processName.setText(list.get(position).getJob());
                 if(position==0){
                     holder.end.setVisibility(View.INVISIBLE);
                     holder.start.setText(""+list.get(position).getArrivalTime());
                     if(list.size()==1){
                         holder.end.setVisibility(View.VISIBLE);
                         holder.end.setText("" + list.get(position).getCompleteTime());
                     }
                 }else if(position<list.size()-1){
                     holder.end.setVisibility(View.INVISIBLE);
                     holder.start.setText(""+list.get(position-1).getCompleteTime());
                 }else{
                     holder.end.setVisibility(View.VISIBLE);
                     holder.start.setText(""+list.get(position-1).getCompleteTime());
                     if(list.get(position).completeTime!=0) {
                         holder.end.setText("" + list.get(position).getCompleteTime());
                     }
                 }





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView start,end,processName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            start=itemView.findViewById(R.id.process_start_instance);
            end=itemView.findViewById(R.id.process_end_instance);
            processName=itemView.findViewById(R.id.process_name_instance);
        }
    }
}
