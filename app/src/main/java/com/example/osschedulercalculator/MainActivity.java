package com.example.osschedulercalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    Spinner choosenAlgorithmSpinner;
    GanttRecyclerAdapter adapter;

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<Process> processes = new ArrayList<>();
    ArrayList<Process> processesTable = new ArrayList<>();

    EditText arrivalTime, burstTime;
    String algo = "FCFS";
    TextView output_algo_mention;
    RecyclerView tableRecyclerView;

    LinearLayout timeQuetumLayout, outputLayout;
    MaterialButton solve;
    RecyclerView ganattRecyclerView;
    TableRowAdapter tableRowAdapter;

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intialization();
        choosenAlgorithmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, arrayList.get(i), Toast.LENGTH_SHORT).show();

                switch (arrayList.get(i)) {
                    case "FCFS-First come first serve":
                        algo = ("FCFS");
                        timeQuetumLayout.setVisibility(View.GONE);
                        break;
                    case "SJF-Shortest job first":
                        algo = ("SJF");
                        timeQuetumLayout.setVisibility(View.GONE);
                        break;
                    case "SRJF-Shortest remaining job first(pre-emptive)":
                        timeQuetumLayout.setVisibility(View.GONE);
                        algo = ("SRJF");
                        break;
                    case "RR-Round robin (pre-emptive)":
                        timeQuetumLayout.setVisibility(View.VISIBLE);
                        algo = ("RR");
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                algo = "FCFS";
            }
        });

        solve.setOnClickListener(view -> {
            try {


                processes.clear();
                outputLayout.setVisibility(View.VISIBLE);
                output_algo_mention.setText(algo);
                String arrival[] = arrivalTime.getText().toString().trim().split("\\s+");
                String burst[] = burstTime.getText().toString().trim().split("\\s+");


                if (arrival.length == burst.length) {

                    for (int i = 0; i < burst.length; i++) {
                        if (Long.parseLong(burst[i]) == 0) {
                            throw new Exception();
                        }
                        processes.add(new Process("P" + (i + 1), Long.parseLong(arrival[i]), Long.parseLong(burst[i])));
                    }
                    //** IF PROCESS ID EMPTY
                    if (processes.size() == 0) {
                        return;
                    }
                    processes.sort(Comparator.comparingLong(process -> process.arrivalTime));

                    //** For FIRST COME FIRST SERVE ALGORITHM
                    if (algo.equals("FCFS")) {


                        long mx = 0;

                        int size = processes.size();
                        if (size > 0) {
                            mx = processes.get(0).arrivalTime;
                        }
                        for (int i = 0; i < size; i++) {

                            long currentProcessBurstTime = processes.get(i).getBurstTime();
                            mx += currentProcessBurstTime;
                            processes.get(i).setCompleteTime(mx);
                            processes.get(i).setTurnArondTime(mx - processes.get(i).getArrivalTime());
                            processes.get(i).setWaitingTime(processes.get(i).getTurnArondTime() - processes.get(i).getBurstTime());
                            if (i == 0) {
                                processes.get(i).setResponseTime(processes.get(i).arrivalTime);
                            } else {
                                processes.get(i).setResponseTime(processes.get(i - 1).completeTime);
                            }


                            if (i <= size - 2) {
                                if (mx < processes.get(i + 1).getArrivalTime()) {
                                    Process nw = new Process("_", mx, processes.get(i + 1).getArrivalTime() - mx);

                                    nw.setCompleteTime(mx + nw.burstTime);
                                    Log.i("test1", "burst " + processes.get(i + 1).burstTime + " arrival" + processes.get(i + 1).arrivalTime + " complete" + nw.completeTime);
                                    processes.add(i + 1, nw);
                                    size++;
                                }
                            }
                        }
                        if (size > 0) {
                            processesTable.clear();
                        }
                        for (Process p : processes) {
                            if (!Objects.equals(p.getJob(), "_")) {
                                processesTable.add(p);
                            }
                        }


                    }

                    if (algo.equals("SJF")) {
                        //    PriorityQueue<Process> processpriority
//                                =new PriorityQueue<>(new ProcessComparator());


//                        processpriority.addAll(processes);
//                        processes.clear();
//                        while(!processpriority.isEmpty()){
//                            Process p=processpriority.poll();
//                            processes.add(p);
//                            Log.i("process","Arrival :"+p.arrivalTime+" burst time"+p.burstTime+"" );
//                        }

                        if (processes.size() > 0) {
                            long time = 0;

                            time = processes.get(0).getArrivalTime();

                            int size = processes.size();

                            ArrayList<Process> resultprocess = new ArrayList<>();
                            ArrayList<Process> filteredProcesses = new ArrayList<>();

                            while (size > 0) {

                                for (Process fileter : processes) {
                                    if (fileter.arrivalTime <= time) {

                                        filteredProcesses.add(fileter);
                                        size--;

                                    }
                                }
                                // prioritize based on burst time
                                PriorityQueue<Process> processpriority = new PriorityQueue<>(new ProcessComparator());

                                processpriority.addAll(filteredProcesses);


                                if (processpriority.size() > 0) {
                                    while (!processpriority.isEmpty()) {
                                        Process p = processpriority.poll();
                                        processes.remove(p);
                                        assert p != null;
                                        p.setResponseTime(time);
                                        time += p.burstTime;

                                        p.setCompleteTime(time);
                                        p.setTurnArondTime(time - p.arrivalTime);
                                        p.setWaitingTime(p.turnArondTime - p.burstTime);


                                        resultprocess.add(p);
                                        Log.i("process", "Arrival :" + p.arrivalTime + " burst time " + p.burstTime + "  " + time);
                                    }

                                    Log.i("process", "_____________________________");

                                } else if (processes.size() > 0) {
                                    // if any process arrivate at late

                                    processes.sort(Comparator.comparingLong(process -> process.arrivalTime));
                                    Process p = processes.get(0);
                                    processes.add(0, new Process("_", time, p.arrivalTime - time));

                                    size++;
                                }

                                filteredProcesses.clear();

                            }


                            processesTable.clear();
                            processes.clear();
                            processes.addAll(resultprocess);
                            for (Process p : processes) {
                                if (!Objects.equals(p.getJob(), "_")) {
                                    processesTable.add(p);
                                }
                            }

                        }

                    }


                    if (algo.equals("SRJF")) {

                    }


                    if (algo.equals("RR")) {

                    }


                    tableRowAdapter.notifyDataSetChanged();
                    adapter.notifyDataSetChanged();

                } else {
                    //** IF ARRIVAL TIME NOT EQUALS BURST TIMES
                    throw new Exception();

                }
            } catch (Exception e) {

                Toast.makeText(this, "invalid input", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void intialization() {
        choosenAlgorithmSpinner = findViewById(R.id.inputAlgorithm_spinner);

        arrayList.add("FCFS-First come first serve");
        arrayList.add("SJF-Shortest job first");
        arrayList.add("SRJF-Shortest remaining job first(pre-emptive)");
        arrayList.add("RR-Round robin (pre-emptive)");
        CustomSpinnerAdapter adapter1 = new CustomSpinnerAdapter(this, arrayList);
        choosenAlgorithmSpinner.setAdapter(adapter1);
        timeQuetumLayout = findViewById(R.id.time_quotum_layout);
        ganattRecyclerView = findViewById(R.id.gantt_recycler_view);
        adapter = new GanttRecyclerAdapter(this, processes);
        ganattRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ganattRecyclerView.setAdapter(adapter);
        solve = findViewById(R.id.solve_btn);
        output_algo_mention = findViewById(R.id.algor_mention_output);
        arrivalTime = findViewById(R.id.arrival_times);
        burstTime = findViewById(R.id.burst_times);
        tableRecyclerView = findViewById(R.id.table_recycler_view);
        tableRowAdapter = new TableRowAdapter(this, processesTable);
        tableRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tableRecyclerView.setAdapter(tableRowAdapter);
        outputLayout = findViewById(R.id.output_linear_layout_tabke_gannatt);
        outputLayout.setVisibility(View.INVISIBLE);

        Toolbar tb = findViewById(R.id.toolbar);
        tb.setOverflowIcon(getDrawable(R.drawable.more_vert_24));


    }

    class ProcessComparator implements Comparator<Process> {
        @Override
        public int compare(Process p1, Process p2) {

            int result = 0;
            result = Long.compare(p1.burstTime, p2.burstTime);

            return result;
        }
    }

    class CustomSpinnerAdapter extends BaseAdapter {

        List<String> list;
        Context context;

        CustomSpinnerAdapter(Context context, List<String> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            @SuppressLint("ViewHolder")
            View view1 = LayoutInflater.from(context).inflate(R.layout.custom_spinner_layout, viewGroup, false);
            TextView text = view1.findViewById(R.id.text1);
            text.setText(list.get(i));
            return view1;
        }
    }


}