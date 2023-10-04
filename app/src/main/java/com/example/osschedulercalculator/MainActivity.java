package com.example.osschedulercalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.Comparator;

import java.util.LinkedList;
import java.util.Queue;


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

                String arrival[] = arrivalTime.getText().toString().trim().split("\\s+");
                String burst[] = burstTime.getText().toString().trim().split("\\s+");


                if (arrival.length == burst.length) {

                    for (int i = 0; i < burst.length; i++) {
                        if (Long.parseLong(burst[i]) <= 0 || Long.parseLong(arrival[i]) < 0) {
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
                    outputLayout.setVisibility(View.VISIBLE);
                    output_algo_mention.setText(algo);
                    if (algo.equals("FCFS")) {
                        if (processes.size() > 0) {


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
                            adapter.notifyDataSetChanged();
                            tableRowAdapter.notifyDataSetChanged();
                        }


                    }

                    if (algo.equals("SJF")) {


                        if (processes.size() > 0) {
                            ArrayList<Process> result = new ArrayList<>();
                            long time = 0;

                            time = processes.get(0).getArrivalTime();

                            long currentTime = time;

                            while (!processes.isEmpty()) {
                                Process shortestJob = null;
                                long shortestTime = Integer.MAX_VALUE;

                                for (Process process : processes) {
                                    if (process.arrivalTime <= currentTime && process.burstTime < shortestTime) {
                                        shortestJob = process;
                                        shortestTime = process.burstTime;
                                    }
                                }

                                if (shortestJob == null) {
                                    // No eligible process, find the next arriving process, if any
                                    long nextArrivalTime = Integer.MAX_VALUE;
                                    Process tmp = null;
                                    for (Process process : processes) {
                                        if (process.arrivalTime > currentTime && process.arrivalTime < nextArrivalTime) {
                                            nextArrivalTime = process.arrivalTime;
                                            tmp = process;
                                        }
                                    }

                                    // If a late-arriving process is found, update the current time
                                    // and continue the loop to process the late-arriving process immediately
                                    if (nextArrivalTime != Integer.MAX_VALUE) {
                                        currentTime = nextArrivalTime;
                                        Process p = new Process("_", nextArrivalTime, tmp.getBurstTime());
                                        p.completeTime = nextArrivalTime;
                                        result.add(p);
                                        continue;
                                    }
                                    // If there are no more arriving processes, break the loop
                                    break;
                                }

                                processes.remove(shortestJob);
                                shortestJob.setResponseTime(currentTime);
                                currentTime += shortestJob.burstTime;
                                shortestJob.setCompleteTime(currentTime);
                                shortestJob.setTurnArondTime(currentTime - shortestJob.arrivalTime);
                                shortestJob.setWaitingTime(shortestJob.turnArondTime - shortestJob.burstTime);

                                result.add(shortestJob);

                                Log.i("process", shortestJob.getJob() + " executed from time " +
                                        currentTime + " with waiting time " + (currentTime - shortestJob.arrivalTime));
                            }
                            processesTable.clear();
                            processes.clear();
                            processes.addAll(result);

                            for (Process p : result) {
                                if (!Objects.equals(p.getJob(), "_")) {
                                    processesTable.add(p);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            tableRowAdapter.notifyDataSetChanged();

                        }

                    }


                    if (algo.equals("SRJF")) {
                        if (processes.size() > 0) {
                            ArrayList<Process> result = new ArrayList<>();
                            long currentTime = processes.get(0).arrivalTime;
                            int processsize = processes.size();
                            int completedProcesses = 0;


                            List<Process> queue = new ArrayList<>();

                            while (completedProcesses < processsize) {
                                for (Process process : processes) {
                                    if (process.arrivalTime <= currentTime && process.remainingBurstTime > 0 && !Objects.equals(process.job, "_")) {
                                        if (!queue.contains(process)) {
                                            queue.add(process);
                                        }
                                    }
                                }

                                if (queue.isEmpty()) {

                                    currentTime++;

                                } else {
                                    Process shortestProcess = queue.get(0);

                                    for (Process process : queue) {
                                        if (process.remainingBurstTime < shortestProcess.remainingBurstTime) {
                                            shortestProcess = process;
                                        }
                                    }

                                    shortestProcess.remainingBurstTime--;
                                    Process tmp = new Process(shortestProcess.job);
                                    tmp.arrivalTime = shortestProcess.arrivalTime;
                                    tmp.completeTime = currentTime + 1;
                                    if (shortestProcess.responseTime == -1) {
                                        shortestProcess.responseTime = currentTime;
                                    }
                                    result.add(tmp);
                                    currentTime++;

                                    if (shortestProcess.remainingBurstTime == 0) {
                                        shortestProcess.turnArondTime = currentTime - shortestProcess.arrivalTime;
                                        shortestProcess.completeTime = currentTime;
                                        shortestProcess.waitingTime = shortestProcess.turnArondTime - shortestProcess.burstTime;

                                        shortestProcess.rank = completedProcesses;
                                        completedProcesses++;
                                        queue.remove(shortestProcess);
                                    }
                                }
                            }


                            processesTable.clear();

                            for (Process p : processes) {
                                if (!Objects.equals(p.getJob(), "_")) {
                                    processesTable.add(p);
                                }
                            }
                            processes.clear();
                            int i = 0, j = 0;

                            while (i < result.size()) {
                                j = i + 1;
                                while (j < result.size() && Objects.equals(result.get(j).job, result.get(i).job)) {
                                    j++;
                                }

                                Log.i("process", i + "  " + j);

                                Process pr = new Process(result.get(i).job);

                                pr.arrivalTime = result.get(i).arrivalTime;
                                pr.completeTime = result.get(j - 1).completeTime;
                                int resSize = processes.size() - 1;

                                if (resSize >= 0 && processes.get(resSize).completeTime < pr.arrivalTime) {
                                    Process gap = new Process("_", pr.completeTime,
                                            pr.arrivalTime - pr.completeTime);
                                    gap.completeTime = pr.arrivalTime;
                                    processes.add(resSize + 1, gap);
                                }
                                i = j;
                                processes.add(pr);


                            }

                            //   processes.addAll(result);
                            tableRowAdapter.notifyDataSetChanged();

                            adapter.notifyDataSetChanged();
                        }
                    }


                    if (algo.equals("RR")) {

                        if (!processes.isEmpty()) {
                            ArrayList<Process> result = new ArrayList<>();
                            EditText timeQuantum = findViewById(R.id.time_quantum);
                            String time = timeQuantum.getText().toString();
                            if (time.equals("")) throw new Exception();
                            long timequantum = Long.parseLong(time);
                            long currentTime = processes.get(0).arrivalTime;
                            long clock = 0;


                            Queue<Process> queue = new LinkedList<>();


                            while (true) {
                                boolean allProcessesCompleted = true;

                                // Add arriving processes to the queue
                                for (Process process : processes) {
                                    if (process.arrivalTime <= currentTime && process.remainingBurstTime > 0) {
                                        if (process.rank == 0) {
                                            queue.add(process);
                                            process.rank = 1;
                                        }
                                        allProcessesCompleted = false;
                                    }
                                }

                                if (allProcessesCompleted) {
                                    long nextArrivalTime = Integer.MAX_VALUE;
                                    Process tmp = null;
                                    for (Process process : processes) {
                                        if (process.arrivalTime > currentTime && process.arrivalTime < nextArrivalTime && process.rank == 0) {
                                            nextArrivalTime = process.arrivalTime;
                                            tmp = process;
                                            allProcessesCompleted = false;
                                        }
                                    }

                                    if (nextArrivalTime != Integer.MAX_VALUE) {
                                        Process p = new Process("_", currentTime, tmp.arrivalTime);
                                        currentTime = tmp.arrivalTime;
                                        p.completeTime = currentTime;
                                        result.add(p);
                                        tmp.rank = 1;
                                        queue.add(tmp);

                                    }
                                }
                                if (allProcessesCompleted && queue.isEmpty()) {
                                    break; // All processes are completed, exit the loop
                                }

                                // Process the front process in the queue
                                Process currentProcess = queue.poll();
                                assert currentProcess != null;
                                if (currentProcess.responseTime == -1) {
                                    currentProcess.responseTime = currentTime;
                                }
                                if (currentProcess.remainingBurstTime > timequantum) {
                                    // Process for the time quantum
                                    currentTime += timequantum;
                                    currentProcess.remainingBurstTime -= timequantum;
                                } else {
                                    // Process for the remaining burst time
                                    currentTime += currentProcess.remainingBurstTime;
                                    currentProcess.remainingBurstTime = 0;
                                    currentProcess.completeTime = currentTime;
                                    currentProcess.turnArondTime = currentTime - currentProcess.arrivalTime;

                                    currentProcess.waitingTime = currentProcess.turnArondTime - currentProcess.burstTime;

                                }
                                Process p = new Process(currentProcess.job);
                                p.arrivalTime = currentProcess.arrivalTime;
                                p.completeTime = currentTime;
                                result.add(p);
                                for (Process process : processes) {
                                    if (process.arrivalTime <= currentTime && process.remainingBurstTime > 0) {
                                        if (process.rank == 0) {
                                            queue.add(process);
                                            process.rank = 1;
                                        }

                                    }
                                }

                                Log.i("process", "\t\t" + currentProcess.job + "\t\t\t" + currentProcess.remainingBurstTime + "\t\t\t\t" + currentProcess.waitingTime);
                                // Add the current process back to the queue if it's not completed
                                if (currentProcess.remainingBurstTime > 0) {
                                    queue.add(currentProcess);
                                }
                            }


                            processesTable.clear();
                            processesTable.addAll(processes);

                            processes.clear();
                            processes.addAll(result);
                            tableRowAdapter.notifyDataSetChanged();
                            adapter.notifyDataSetChanged();
                        }


                    }


                } else {
                    //** IF ARRIVAL TIME NOT EQUALS BURST TIMES
                    outputLayout.setVisibility(View.INVISIBLE);
                    throw new Exception();

                }
            } catch (Exception e) {

                Toast.makeText(this, "invalid input", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void intialization() {
        choosenAlgorithmSpinner = findViewById(R.id.inputAlgorithm_spinner);
        @SuppressLint("CutPasteId") Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        return true;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_item_1:
                Intent intent = new Intent(MainActivity.this, Notes.class);
                startActivity(intent);
                break;
            case R.id.menu_item_2:
                Intent intent1 = new Intent(MainActivity.this, About.class);
                startActivity(intent1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}