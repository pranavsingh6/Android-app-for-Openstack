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
public class VolumesListAdapter extends RecyclerView.Adapter <VolumesViewHolder>{

    public static String TAG= VolumesFragment.class.getSimpleName();
    //private static final int START_QUOTES;
    //List<Volume> volumeDataset;
    public VolumesListAdapter(){
        //volumeDataset=(initialize this list);
        Log.d(TAG, "-------adaper constructor------");
    }
    @Override
    public VolumesViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.volume_row,viewGroup,false);
        return new VolumesViewHolder(view);
    }
    @Override
    public void onBindViewHolder(VolumesViewHolder volumeViewHolder,int i){
        Log.d(TAG, "-------adaper bind------");
        volumeViewHolder.volumeName.setText(VolumesFragment.VolumeList.get(i).getVolumeName());
        volumeViewHolder.volumeType.setText(VolumesFragment.VolumeList.get(i).getVolumeType());
        volumeViewHolder.volumeSize.setText(VolumesFragment.VolumeList.get(i).getVolumeSize().concat("GB"));
        if(VolumesFragment.VolumeList.get(i).getVolumeStatus().equalsIgnoreCase("error"))
            volumeViewHolder.volumeStatus.setBackgroundColor(Color.RED);
        else if (VolumesFragment.VolumeList.get(i).getVolumeStatus().equalsIgnoreCase("active"))
            volumeViewHolder.volumeStatus.setBackgroundColor(Color.GREEN);
        else
            volumeViewHolder.volumeStatus.setBackgroundColor(Color.GRAY);
        volumeViewHolder.view.setOnClickListener(onClickListener(i));
    }
    @Override
    public int getItemCount(){
        return VolumesFragment.VolumeList.size();
    }
    private View.OnClickListener onClickListener(final int position){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG,"----------------------position----------------------------");
                Log.d(TAG,String.valueOf(position));
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.details_layout);
                dialog.setTitle("Volume Details:");
                dialog.setCancelable(true);
                TextView name = (TextView) dialog.findViewById(R.id.textView_name_det);
                TextView id = (TextView) dialog.findViewById(R.id.textView_id_det);
                TextView status = (TextView) dialog.findViewById(R.id.textView_status_det);
                TextView ram = (TextView) dialog.findViewById(R.id.textView_ram_det);
                Button button_ok=(Button)dialog.findViewById(R.id.button_ok);
                Switch on_off=(Switch)dialog.findViewById(R.id.switch_power);
                on_off.setVisibility(View.INVISIBLE);
                name.setText(VolumesFragment.VolumeList.get(position).getVolumeName());
                id.setText(VolumesFragment.VolumeList.get(position).getVolumeID());
                status.setText(VolumesFragment.VolumeList.get(position).getVolumeStatus());
                ram.setText((VolumesFragment.VolumeList.get(position).getVolumeSize()));
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

}

