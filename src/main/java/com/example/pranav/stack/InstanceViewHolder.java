package com.example.pranav.stack;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by pranav on 21/09/15.
 */
public class InstanceViewHolder extends RecyclerView.ViewHolder {
    protected TextView instanceName;
    public View view;
    protected TextView instanceIPAddr;
    protected TextView instanceRunningTime;
    protected View instanceStatus;
    public InstanceViewHolder(View itemView){
        super(itemView);
        view=itemView;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.item_recycler);
                dialog.setTitle("Position " + position);
                dialog.setCancelable(true); // dismiss when touching outside Dialog

                // set the custom dialog components - texts and image
                TextView name = (TextView) dialog.findViewById(R.id.name);
                TextView job = (TextView) dialog.findViewById(R.id.job);
                ImageView icon = (ImageView) dialog.findViewById(R.id.image);

                setDataToView(name, job, icon, position);

                dialog.show();
                */
                Log.d("-----","----clicked----");
            }
        });
        instanceName=(TextView)itemView.findViewById(R.id.textView_instance_name);
        instanceIPAddr=(TextView)itemView.findViewById(R.id.textView_instance_ipaddr);
        instanceRunningTime=(TextView)itemView.findViewById(R.id.textView_instance_running_time);
        instanceStatus=(View)itemView.findViewById(R.id.status_color);

    }
}
