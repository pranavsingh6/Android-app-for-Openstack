package com.example.pranav.stack;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by pranav on 29/09/15.
 */
public class FlavorsViewHolder extends RecyclerView.ViewHolder {
    protected TextView flavorName;
    protected TextView flavorSize;
    protected TextView flavorDisk;
    public View view;
    public FlavorsViewHolder(View itemView){
        super(itemView);
        view=itemView;
        flavorName=(TextView)itemView.findViewById(R.id.textView_flavor_name);
        flavorSize=(TextView)itemView.findViewById(R.id.textView_flavor_size);
        flavorDisk=(TextView)itemView.findViewById(R.id.textView_flavor_disk);

    }
}
