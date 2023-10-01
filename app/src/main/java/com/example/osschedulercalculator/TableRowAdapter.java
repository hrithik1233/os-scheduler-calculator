package com.example.osschedulercalculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class TableRowAdapter extends RecyclerView.Adapter<TableRowAdapter.MyViewHolder> {
    Context context;
    ArrayList<Process> list;

    public TableRowAdapter(Context context, ArrayList<Process> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TableRowAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.table_row_instance,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TableRowAdapter.MyViewHolder holder, int pos) {


             if(pos==list.size()){

                 holder.job.setText("");
                 holder.arrival.setText("");
                 holder.burst.setText("");

                 holder.complete.setText("Average");

                 holder.response.setText("");
                 double averageTuraound=0;
                 double averagewaiting=0;
                 int count=0;

                 for(Process p:list){
                     if(p.getJob()!="_"){
                         averageTuraound+=p.getTurnArondTime();
                         averagewaiting+=p.getWaitingTime();
                     }else {
                         count++;
                     }

                 }
                 int size=list.size()-count;
                 long aT= (long) averageTuraound;
                 long aW= (long) averagewaiting;

                 averageTuraound=averageTuraound/((double)size);
                 averagewaiting=averagewaiting/((double)size);
                 @SuppressLint("DefaultLocale")
                 String avrTr=String.format("%.3f",averageTuraound);
                 avrTr=avrTr.replaceAll("[0]+$","");

                 if(size!=0){
                     avrTr=aT+"/"+size+"="+avrTr;
                 }


                 String avrWt=String.format("%.3f",averagewaiting);
                 avrWt=avrWt.replaceAll("[0]+$","");
                 if(size!=0) {
                     avrWt = aW + "/" + size + "=" + avrWt;
                 }

                 if(avrTr.endsWith(".")){

                     avrTr=avrTr.substring(0,avrTr.length()-1);
                 }
                 if(avrWt.endsWith(".")){
                     avrWt=avrWt.substring(0,avrWt.length()-1);
                 }
                 holder.turnaround.setText(avrTr);
                 holder.wait.setText(avrWt);

                 return;
             }
               holder.job.setText(list.get(pos).getJob());
               holder.arrival.setText(""+list.get(pos).arrivalTime);
               holder.burst.setText(list.get(pos).burstTime+"");
               holder.complete.setText(list.get(pos).completeTime+"");
               holder.turnaround.setText(list.get(pos).turnArondTime+"");
               holder.wait.setText(list.get(pos).waitingTime+"");
               holder.response.setText(""+list.get(pos).responseTime);


    }

    @Override
    public int getItemCount() {
        if(list.size()!=0)
        return list.size()+1;
        else return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView job,arrival,burst,complete,turnaround,wait,response;
        LinearLayout layout;
        @SuppressLint("CutPasteId")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            job=itemView.findViewById(R.id.job_row);
            arrival=itemView.findViewById(R.id.arrival_time_row);
            burst=itemView.findViewById(R.id.burst_time_row);
            complete=itemView.findViewById(R.id.complete_time_row);
            turnaround=itemView.findViewById(R.id.turnaround_time_row);
            wait=itemView.findViewById(R.id.wait_time_row);
            response=itemView.findViewById(R.id.response_time_row);
            layout=itemView.findViewById(R.id.table_row_liearlayout_main);
        }
    }
}
