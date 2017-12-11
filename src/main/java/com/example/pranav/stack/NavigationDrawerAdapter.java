package com.example.pranav.stack;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Collections;

import java.util.List;

/**
 * Created by Ravi Tamada on 12-03-2015.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
         if (current.getTitle().equalsIgnoreCase("overview")){
            holder.title_image.setImageResource(R.drawable.overview);
        } else if (current.getTitle().equalsIgnoreCase("Instances")) {
            holder.title_image.setImageResource(R.drawable.server_cloud);
        }else if (current.getTitle().equalsIgnoreCase("Volumes")){
            holder.title_image.setImageResource(R.drawable.storage);
        }else if (current.getTitle().equalsIgnoreCase("Images")){
            holder.title_image.setImageResource(R.drawable.image);
        }
        else if (current.getTitle().equalsIgnoreCase("Flavors")){
            holder.title_image.setImageResource(R.drawable.image2);
        }
         else if (current.getTitle().equalsIgnoreCase("Topology")){
             holder.title_image.setImageResource(R.drawable.topology);
         }



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView title_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            title_image=(ImageView)itemView.findViewById(R.id.imageView_navDraw);
        }
    }
}