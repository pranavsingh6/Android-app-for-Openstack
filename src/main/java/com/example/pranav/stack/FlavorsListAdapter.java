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

/**
 * Created by pranav on 29/09/15.
 */
public class FlavorsListAdapter extends RecyclerView.Adapter <FlavorsViewHolder>{

    public static String TAG= FlavorsFragment.class.getSimpleName();
    //private static final int START_QUOTES;
    //List<flavor> flavorDataset;
    public FlavorsListAdapter(){
        //flavorDataset=(initialize this list);
        //Log.d(TAG, "-------adaper flavor------");
    }
    @Override
    public FlavorsViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.flavor_row,viewGroup,false);
        return new FlavorsViewHolder(view);
    }
    @Override
    public void onBindViewHolder(FlavorsViewHolder flavorsViewHolder,int i){
        //Log.d(TAG, "-------adaper bind------");

        flavorsViewHolder.flavorName.setText(FlavorsFragment.FlavorList.get(i).getFlavorName());
        flavorsViewHolder.flavorSize.setText(getMBToGB(FlavorsFragment.FlavorList.get(i).getFlavorSize()));
        String disk_supported="Disks: ".concat(FlavorsFragment.FlavorList.get(i).getFlavorDisk());
        flavorsViewHolder.flavorDisk.setText(disk_supported);
        flavorsViewHolder.view.setOnClickListener(onClickListener(i));
    }
    @Override
    public int getItemCount(){
        return FlavorsFragment.FlavorList.size();
        //return flavors.servers_names.size();
    }
    private View.OnClickListener onClickListener(final int position){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG,"----------------------position----------------------------");
                Log.d(TAG,String.valueOf(position));
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.details_layout);
                dialog.setTitle("Flavor Details:");
                dialog.setCancelable(true);
                TextView name = (TextView) dialog.findViewById(R.id.textView_name_det);
                TextView id = (TextView) dialog.findViewById(R.id.textView_id_det);
                TextView status = (TextView) dialog.findViewById(R.id.textView_status_det);
                TextView ram = (TextView) dialog.findViewById(R.id.textView_ram_det);
                Button button_ok=(Button)dialog.findViewById(R.id.button_ok);
                Switch on_off=(Switch)dialog.findViewById(R.id.switch_power);

                name.setText(FlavorsFragment.FlavorList.get(position).getFlavorName());
                id.setText(FlavorsFragment.FlavorList.get(position).getFlavorID());
                status.setText(FlavorsFragment.FlavorList.get(position).getFlavorDisk());
                ram.setText(getMBToGB(FlavorsFragment.FlavorList.get(position).getFlavorSize()));
                on_off.setVisibility(View.INVISIBLE);
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
    public String getMBToGB(String string){

        if (string != null && string.isEmpty()==false) {
            int mb = Integer.valueOf(string)/1024;
            if (mb >= 1)
                string=String.valueOf(mb).concat("GB");
            else
                string=string.concat("MB");
        }
        return string;
    }


}

