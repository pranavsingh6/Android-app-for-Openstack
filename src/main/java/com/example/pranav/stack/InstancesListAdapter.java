package com.example.pranav.stack;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pranav on 21/09/15.
 */
public class InstancesListAdapter extends RecyclerView.Adapter <InstanceViewHolder>{

    public static String TAG= InstancesFragment.class.getSimpleName();
    //private static final int START_QUOTES;
    List<String> instanceDataSet= new ArrayList<>();

    public InstancesListAdapter(){


        //instanceDataset=(initialize this list);
        //Log.d(TAG, "-------adaper constructor------");
    }
    @Override
    public InstanceViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.instance_row,viewGroup,false);
        return new InstanceViewHolder(view);
    }
    @Override
    public void onBindViewHolder(InstanceViewHolder instanceViewHolder,int i){
        //Log.d(TAG, "-------adaper bind------");
        instanceViewHolder.instanceName.setText(InstancesFragment.InstanceList.get(i).getInstanceName());
        instanceViewHolder.instanceIPAddr.setText(InstancesFragment.InstanceList.get(i).getInstanceIPAddr());
        instanceViewHolder.instanceRunningTime.setText(secondsToDays(InstancesFragment.InstanceList.get(i).getInstanceRunningTime()));
        instanceViewHolder.instanceStatus.setBackgroundColor(Color.RED);
        instanceViewHolder.view.setOnClickListener(onClickListener(i));

        if(InstancesFragment.InstanceList.get(i).getInstancePowerState().equalsIgnoreCase("ERROR"))
            instanceViewHolder.instanceStatus.setBackgroundResource(R.drawable.circle_red);
        else if(InstancesFragment.InstanceList.get(i).getInstancePowerState().equalsIgnoreCase("Active"))
            instanceViewHolder.instanceStatus.setBackgroundResource(R.drawable.circle_green);
        else if(InstancesFragment.InstanceList.get(i).getInstancePowerState().equalsIgnoreCase("Shutoff"))
            instanceViewHolder.instanceStatus.setBackgroundResource(R.drawable.circle_grey);
        else
            instanceViewHolder.instanceStatus.setBackgroundResource(R.drawable.circle_orange);
    }
/*
    @Override
    public void toggleSelection(int pos){

    }
    */
    @Override
    public int getItemCount(){
        return InstancesFragment.InstanceList.size();
    }

    private View.OnClickListener onClickListener(final int position){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Log.d(TAG,"----------------------position----------------------------");
                //Log.d(TAG,String.valueOf(position));
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.details_layout);
                dialog.setTitle("         Instance Details:");
                dialog.setCancelable(true);
                TextView name = (TextView) dialog.findViewById(R.id.textView_name_det);
                TextView id = (TextView) dialog.findViewById(R.id.textView_id_det);
                TextView status = (TextView) dialog.findViewById(R.id.textView_status_det);
                TextView ram = (TextView) dialog.findViewById(R.id.textView_ram_det);
                Button button_ok=(Button)dialog.findViewById(R.id.button_ok);
                Switch on_off=(Switch)dialog.findViewById(R.id.switch_power);

                name.setText(InstancesFragment.InstanceList.get(position).getInstanceName());
                id.setText(InstancesFragment.InstanceList.get(position).getInstanceID());
                status.setText(InstancesFragment.InstanceList.get(position).getInstancePowerState());
                ram.setText(getMBToGB(InstancesFragment.InstanceList.get(position).getInstanceRAM()));
                if(status.getText().toString().equalsIgnoreCase("ACTIVE"))
                    on_off.setChecked(true);
                else
                   on_off.setChecked(false);
                button_ok.setOnClickListener(onCancelListener(dialog));
                dialog.show();
            }
        };
    }
    private View.OnClickListener onCancelListener(final Dialog dialog) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        };
    }
    public String secondsToDays(String seconds)
    {
        if (seconds != null && seconds.isEmpty()==false){
            int sec=Integer.valueOf(seconds);
            int days=0,hours=0,mins=0;
            if (sec > 60)
            {
                if ((sec / 3600) > 24)
                    days = (sec / 3600) / 24;
                hours = (sec / 3600) % 24;
                if (sec > 60)
                    mins = sec / 60;
                //Log.d(TAG,"-------------datetime--------------");
                //Log.d(TAG,String.valueOf(days));
                //Log.d(TAG,String.valueOf(hours));
                if (days > 0)
                    seconds = seconds.valueOf(days).concat(" days,");
                else
                   seconds="";
                if (hours > 0)
                    seconds = seconds.concat(String.valueOf(hours)).concat(" hours");
                else
                  seconds="";
                if (days <= 0 && hours <= 0 && mins > 0) {
                    seconds = "";
                    seconds = seconds.concat(String.valueOf(mins)).concat(" mins");
                }
                return seconds;
            }
            else
              return seconds.concat("seconds");

            ////Log.d(TAG,"-------------datetime--------------");
            ////Log.d(TAG,String.valueOf(days));
            ////Log.d(TAG,String.valueOf(hours));

        }
        return seconds;
    }
    public String getMBToGB(String string){

        if (string != null && string.isEmpty()==false) {
            int mb = Integer.valueOf(string)/1024;
            if (mb >= 1)
                string=String.valueOf(mb).concat(" GB");
            else
                string=string.concat(" MB");
        }
        return string;
    }

}