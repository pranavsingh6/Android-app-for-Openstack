package com.example.pranav.stack;
/**
 * Created by sid on 14/9/15.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VolumesFragment extends Fragment{
    public static String TAG= VolumesFragment.class.getSimpleName();
    VolumesListAdapter volumesListAdapter;
    OpenStackDatabase openStackDatabase;
    private ProgressDialog progressDialog;
    ImageButton imageButton;
    public static List<Volume> VolumeList=new ArrayList<Volume>();
    public VolumesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedVolumestate) {

        super.onCreate(savedVolumestate);
        Log.d(TAG, "---VolumeFragment___");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedVolumeState) {
        View rootView = inflater.inflate(R.layout.fragment_volumes, container, false);
        final RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.volume_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView.setLayoutManager(layoutManager);
        volumesListAdapter =new VolumesListAdapter();
        recyclerView.setAdapter(volumesListAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SendRequest(null, getVolumeURL());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target){
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder,int direction){
                Log.d(TAG, "---Item location----");
                if(viewHolder.getAdapterPosition() < VolumeList.size())
                     VolumeList.remove(viewHolder.getAdapterPosition());
                /*
                if(viewHolder.getAdapterPosition() < Volume.servers_names.size()) {
                    Volume.servers_uls.remove(viewHolder.getAdapterPosition());
                    Volume.servers_status.remove(viewHolder.getAdapterPosition());
                    Volume.servers_names.remove(viewHolder.getAdapterPosition());
                }
                */
                volumesListAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
// Inflate the layout for this fragment
        return rootView;
    }

    private void SendRequest(String str, String url)
    {
        showDialog();
        StringRequest request = new StringRequest((str == null) ? Request.Method.GET:Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d(TAG, response.toString());
                        hideDialog();
                        getVolumeDetails(response);
                        Log.d(TAG, "Response-volume--------");
                        Log.d(TAG, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "overview error:");
                        Log.d(TAG,error.toString());
                        hideDialog();
                        if(error instanceof TimeoutError || error instanceof NoConnectionError)
                        {
                            Toast.makeText(getActivity(), "Please check you connection!", Toast.LENGTH_LONG).show();
                        }
                        else if(error instanceof AuthFailureError) {
                            Toast.makeText(getActivity(), "Incorrect Username or Password.", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        ){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers=new HashMap<String,String>();
                headers.put("Content-Type", "application/xml; charset=utf-8");
                headers.put("X-Auth-Token",App_urls.TokenID);
                Log.d(TAG, "--------headers----");
                Log.d(TAG,headers.toString());
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        Volley.newRequestQueue(getActivity()).add(request);
    }
    public void getVolumeDetails(String response){
        openStackDatabase.getWritableDatabase();
        try{
            JSONObject jsonObject=new JSONObject(response);
            for (int i = 0; i <jsonObject.getJSONArray("volumes").length(); i++) {
                Volume volume=new Volume();
                JSONObject volumeObj=jsonObject.getJSONArray("volumes").getJSONObject(i);
                volume.setVolumeID(volumeObj.getString("id"));
                volume.setVolumeName(volumeObj.getString("display_name"));
                volume.setVolumeSize(volumeObj.getString("size"));
                volume.setVolumeType(volumeObj.getString("volume_type"));
                volume.setVolumeStatus(volumeObj.getString("status"));
                Log.d(TAG,volume.getVolumeName());
                openStackDatabase.insertVolume(volume);
                VolumeList=openStackDatabase.getAllVolumes();
                volumesListAdapter.notifyDataSetChanged();
            }

        }catch (JSONException js){}
        openStackDatabase.close();
    }
    public String getVolumeURL(){

        String volumeURL=App_urls.allURLs.get("volume");
        if(volumeURL==null){
            Toast.makeText(getActivity(), "Unable to fetch volume details!", Toast.LENGTH_LONG).show();
            volumeURL="";
        }
        else
            volumeURL=volumeURL.concat("/volumes/detail");
        return volumeURL;
    }
    private void showDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog(){
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        openStackDatabase = new OpenStackDatabase(getActivity());
        SendRequest(null, getVolumeURL());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
