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
public class ImagesListAdapter extends RecyclerView.Adapter <ImagesViewHolder>{

    public static String TAG= ImagesFragment.class.getSimpleName();
    //private static final int START_QUOTES;
    //List<Image> instanceDataset;
    public ImagesListAdapter(){
        Log.d(TAG,"---------imageLIST-cons----");
        //instanceDataset=(initialize this list);
    }
    @Override
    public ImagesViewHolder onCreateViewHolder(ViewGroup viewGroup,int i){

        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.image_row,viewGroup,false);
        return new ImagesViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ImagesViewHolder imagesViewHolder,int i){
        Log.d(TAG, "------image-adaper bind------");
        Log.d(TAG,ImagesFragment.ImageList.get(i).getImageName());
        imagesViewHolder.imageName.setText(ImagesFragment.ImageList.get(i).getImageName());
        imagesViewHolder.imageSize.setText(getByteToMB(ImagesFragment.ImageList.get(i).getImageSize()));
        imagesViewHolder.imageFormat.setText(ImagesFragment.ImageList.get(i).getImageType());
        imagesViewHolder.view.setOnClickListener(onClickListener(i));
    }
    @Override
    public int getItemCount(){
        return ImagesFragment.ImageList.size();
    }
    private View.OnClickListener onClickListener(final int position){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG,"----------------------position----------------------------");
                Log.d(TAG,String.valueOf(position));
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.details_layout);
                dialog.setTitle("Image Details:");
                dialog.setCancelable(true);
                TextView name = (TextView) dialog.findViewById(R.id.textView_name_det);
                TextView id = (TextView) dialog.findViewById(R.id.textView_id_det);
                TextView status = (TextView) dialog.findViewById(R.id.textView_status_det);
                TextView ram = (TextView) dialog.findViewById(R.id.textView_ram_det);
                Button button_ok=(Button)dialog.findViewById(R.id.button_ok);
                Switch on_off=(Switch)dialog.findViewById(R.id.switch_power);
                on_off.setVisibility(View.INVISIBLE);
                name.setText(ImagesFragment.ImageList.get(position).getImageName());
                id.setText(ImagesFragment.ImageList.get(position).getImageID());
                status.setText(ImagesFragment.ImageList.get(position).getImageState());
                ram.setText(getByteToMB(ImagesFragment.ImageList.get(position).getImageSize()));
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
    public String getByteToMB(String string){

        if (string != null && string.isEmpty()==false){
            int byte_m=Integer.valueOf(string)/(1024*1024);
            if(byte_m >0 )
                string=String.valueOf(byte_m).concat(" MB");
        }
        return string;
    }


}

