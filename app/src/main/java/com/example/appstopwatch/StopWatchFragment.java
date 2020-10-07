package com.example.appstopwatch;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class StopWatchFragment extends Fragment {
    public static final String SHARED_PREFS= "sharedPrefs";
    public static final String MINUTE_LEFT= "minuteLeft";
    public static final String MINUTE_RIGHT ="minuteRight";
    public static final String SECOND_LEFT = "secongLeft";
    public static final String SECOND_RIGHT = "secondRight";
    public static final String MILI_SECOND_LEFT = "milisecondLeft";
    public static final String MILI_SECOND_RIGHT = "milisecondRight";

    public static final String IS_LOADED = "isLoaded";

    private TextView tvMinuteLeft, tvMinuteRight, tvSecondLeft, tvSecondRight, tvMilisecondLeft, tvMilisecondRight;
    private Button btnStart, btnReset;

    private HandlerThread handlerThread = new HandlerThread("Handler Thread");
    private Handler handler;
    private ClockRun clockRun;
    private volatile boolean stop;

    private ArrayList<LapItem> mexampleList;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int lapCount=0;

    private OnFragmentInteractionListener mListener;

    public StopWatchFragment() {}

    public static StopWatchFragment newInstance(String param1, String param2) {
        StopWatchFragment fragment = new StopWatchFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        stop = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_stop_watch, container, false);

        initialise(view);
        createList();
        buildRecyclerView();
        setButtonListener();
        return view;
    }

    private void initialise(View view ){
        tvMinuteLeft = view.findViewById(R.id.tv_minute_left);
        tvMinuteRight = view.findViewById(R.id.tv_minute_right);
        tvSecondLeft = view.findViewById(R.id.tv_second_left);
        tvSecondRight = view.findViewById(R.id.tv_second_right);
        tvMilisecondLeft = view.findViewById(R.id.tv_milisecond_left);
        tvMilisecondRight = view.findViewById(R.id.tv_milisecond_right);
        btnStart = view.findViewById(R.id.btn_start);
        btnReset = view.findViewById(R.id.btn_reset);

        mRecyclerView = view.findViewById(R.id.recyclerView);

        //saveData();
    }

    private void setButtonListener(){
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stop) {
                    stop = false;
                    clockRun = new ClockRun();
                    handler.post(clockRun);
                    btnStart.setText("STOP");
                    btnReset.setText("Lap");
                }else{
                    stop=true;
                    btnStart.setText("START");
                    btnReset.setText("RESET");
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stop) {
                    reset();
                }else{
                    lapCount +=1;
                    addLap(lapCount);
                }
            }
        });
    }

    private void createList(){
        mexampleList = new ArrayList<>();
    }

    private void buildRecyclerView(){

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new Adapter(mexampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    class ClockRun implements Runnable{

        @Override
        public void run() {
            long currntTime = System.currentTimeMillis(), nextTime;

            while(!stop){
                nextTime = System.currentTimeMillis();
                if(nextTime >= currntTime+10){
                    //updateMainUI();
                    updateData();
                    currntTime = nextTime;
                }
            }
        }
    }

    private void reset(){
        tvMinuteLeft.setText("0");
        tvMinuteRight.setText("0");
        tvSecondLeft.setText("0");
        tvSecondRight.setText("0");
        tvMilisecondLeft.setText("0");
        tvMilisecondRight.setText("0");

        mexampleList.clear();
        lapCount=0;
        mAdapter.notifyDataSetChanged();
    }

    private void addLap(int position){
        String minl, minr, secl, secr,msecl,msecr;

        minl = tvMinuteLeft.getText().toString();
        minr = tvMinuteRight.getText().toString();
        secl = tvSecondLeft.getText().toString();
        secr = tvSecondRight.getText().toString();
        msecl = tvMilisecondLeft.getText().toString();
        msecr = tvMilisecondRight.getText().toString();

        mexampleList.add(position-1,new LapItem(position+".",minl,minr,secl,secr,msecl,msecr));
        mRecyclerView.smoothScrollToPosition(position-1);
        mAdapter.notifyItemInserted(position-1);
    }

    private void updateMainUI(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateData();
            }
        });
    }

    private void updateData(){
        int minl, minr, secl, secr,msecl,msecr;

        minl = Integer.parseInt(tvMinuteLeft.getText().toString());
        minr = Integer.parseInt(tvMinuteRight.getText().toString());
        secl = Integer.parseInt(tvSecondLeft.getText().toString());
        secr = Integer.parseInt(tvSecondRight.getText().toString());
        msecl = Integer.parseInt(tvMilisecondLeft.getText().toString());
        msecr = Integer.parseInt(tvMilisecondRight.getText().toString());

        boolean carry=false;

        if(msecr==9){
            msecr=0;
            carry=true;
        }else{
            msecr += 1;
            carry = false;
        }
        if(carry){
            if(msecl==9){
                msecl=0;
            }else{
                msecl += 1;
                carry=false;
            }
        }
        if(carry){
            if(secr==9){
                secr=0;
            }else{
                secr += 1;
                carry=false;
            }
        }
        if(carry){
            if(secl==5){
                secl=0;
            }else{
                secl += 1;
                carry=false;
            }
        }
        if(carry){
            if(minr==9){
                minr=0;
            }else{
                minr += 1;
                carry=false;
            }
        }
        if(carry){
            if(minl==5){
                minl=0;
            }else{
                minl += 1;
                carry=false;
            }
        }

        tvMinuteLeft.setText(""+minl);
        tvMinuteRight.setText(""+minr);
        tvSecondLeft.setText(""+secl);
        tvSecondRight.setText(""+secr);
        tvMilisecondLeft.setText(""+msecl);
        tvMilisecondRight.setText(""+msecr);
    }

    private void saveData(){
        String minl, minr, secl, secr,msecl,msecr;

        minl = tvMinuteLeft.getText().toString();
        minr = tvMinuteRight.getText().toString();
        secl = tvSecondLeft.getText().toString();
        secr = tvSecondRight.getText().toString();
        msecl = tvMilisecondLeft.getText().toString();
        msecr = tvMilisecondRight.getText().toString();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(MINUTE_LEFT,minl);
        editor.putString(MINUTE_RIGHT,minr);
        editor.putString(SECOND_LEFT,secl);
        editor.putString(SECOND_RIGHT,secr);
        editor.putString(MILI_SECOND_LEFT,msecl);
        editor.putString(MILI_SECOND_RIGHT,msecr);
        editor.putString(IS_LOADED,"abcd");
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        System.out.println("###########################3 "+sharedPreferences.getString(MINUTE_LEFT,""));
        tvMinuteLeft.setText(sharedPreferences.getString(MINUTE_LEFT,""));
        tvMinuteRight.setText(sharedPreferences.getString(MINUTE_RIGHT,""));
        tvSecondLeft.setText(sharedPreferences.getString(SECOND_LEFT,""));
        tvSecondRight.setText(sharedPreferences.getString(SECOND_RIGHT,""));
        tvMilisecondLeft.setText(sharedPreferences.getString(MILI_SECOND_LEFT,""));
        tvMilisecondRight.setText(sharedPreferences.getString(MILI_SECOND_RIGHT,""));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
