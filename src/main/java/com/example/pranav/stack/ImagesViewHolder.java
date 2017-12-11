package com.example.pranav.stack;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by pranav on 29/09/15.
 */
public class ImagesViewHolder extends RecyclerView.ViewHolder {
    protected TextView imageName;
    protected TextView  imageFormat;
    protected TextView imageSize;
    public View view;
    public ImagesViewHolder(View itemView){
        super(itemView);
        view=itemView;
        imageName=(TextView)itemView.findViewById(R.id.textView_image_name);
        imageFormat=(TextView)itemView.findViewById(R.id.textView_image_format);
        imageSize=(TextView)itemView.findViewById(R.id.textView_image_size);
    }
}
