package com.example.pranav.stack;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by pranav on 29/09/15.
 */
public class VolumesViewHolder extends RecyclerView.ViewHolder {
    protected TextView volumeName;
    protected TextView volumeType;
    protected TextView volumeSize;
    public View view;
    protected View volumeStatus;
    public VolumesViewHolder(View itemView){
        super(itemView);
        view=itemView;
        volumeName=(TextView)itemView.findViewById(R.id.textView_volume_name);
        volumeType=(TextView)itemView.findViewById(R.id.textView_volume_type);
        volumeSize=(TextView)itemView.findViewById(R.id.textView_volume_size);
        volumeStatus=(View)itemView.findViewById(R.id.status_color);
    }
}
