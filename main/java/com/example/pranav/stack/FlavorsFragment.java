package com.example.pranav.stack;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by pranav on 29/09/15.
 */
public class FlavorsFragment extends Fragment{
    public static String TAG= FlavorsFragment.class.getSimpleName();
    FlavorsListAdapter flavorsListAdapter;
    OpenStackDatabase openStackDatabase;
    private ProgressDialog progressDialog;
    FloatingActionButton floatingActionButton;
    /** The default number of retries */
    public static final int DEFAULT_MAX_RETRIES = 0;

    public static List<Flavor> FlavorList=new ArrayList<Flavor>();
    public FlavorsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "---FlavorFragment___");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_flavors, container, false);
        final RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.flavor_list);
        final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        flavorsListAdapter =new FlavorsListAdapter();
        floatingActionButton=(FloatingActionButton)rootView.findViewById(R.id.fab_flavor);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.add_flavor);
                dialog.setTitle("Create Flavor :");
                dialog.setCancelable(false);
                EditText name = (EditText) dialog.findViewById(R.id.image_name);
                EditText id = (EditText) dialog.findViewById(R.id.image_id);
                EditText ram = (EditText) dialog.findViewById(R.id.image_ram);
                EditText vcpu = (EditText) dialog.findViewById(R.id.image_vcpu);
                EditText disk = (EditText) dialog.findViewById(R.id.image_disk);
                View btnAdd = dialog.findViewById(R.id.btn_ok);
                View btnCancel = dialog.findViewById(R.id.btn_cancel);

                btnAdd.setOnClickListener(onConfirmListener(name, id, ram, vcpu,disk, dialog));
                btnCancel.setOnClickListener(onCancelListener(dialog));
                dialog.show();

            }
        });
        recyclerView.setAdapter(flavorsListAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SendRequest(null, getFlavorURL());
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
                if(viewHolder.getAdapterPosition() < FlavorList.size())
                    FlavorList.remove(viewHolder.getAdapterPosition());
                flavorsListAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
// Inflate the layout for this fragment
        return rootView;
    }

    private View.OnClickListener onConfirmListener(final EditText name, final EditText id, final EditText ram, final EditText vcpu, final EditText disk,final Dialog dialog) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SendRequest(createFlavorObject(name.getText().toString().trim(), id.getText().toString().trim(), ram.getText().toString().trim(), vcpu.getText().toString().trim(), disk.getText().toString().trim()), getCreateFlavorURL());
                flavorsListAdapter.notifyDataSetChanged();
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
    private String getCreateFlavorURL(){
        String flavorURL=App_urls.allURLs.get("compute");
        if(flavorURL==null){
            Toast.makeText(getActivity(), "Unable to fetch flavor details!", Toast.LENGTH_LONG).show();
            flavorURL="";
        }
        else
        if(flavorURL != null)
            flavorURL=flavorURL.concat("/flavors");
        return flavorURL;
    }

    private JSONObject createFlavorObject(String name, String id, String ram, String vcpu, String disk){
        JSONObject final_image=new JSONObject();
        JSONObject flavor_obj = new JSONObject();
        try {
            flavor_obj.put("name", name);
            flavor_obj.put("id", Integer.parseInt(id));
            flavor_obj.put("ram", Integer.parseInt(ram));
            flavor_obj.put("vcpus", Integer.parseInt(vcpu));
            flavor_obj.put("disk", Integer.parseInt(disk));
            final_image.put("flavor",flavor_obj);
        }catch (JSONException js){

        }
        return final_image;
    }

    private void SendRequest(JSONObject jsonObject, final String url)
    {
        showDialog();
        JsonObjectRequest request = new JsonObjectRequest((jsonObject == null) ? Request.Method.GET:Request.Method.POST,url,jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, response.toString());
                        hideDialog();
                        Log.d(TAG, "Response-flavor2--------");
                        if(url.contains("detail"))
                          getFlavorsDetails(response);
                        else
                            SendRequest(null, getFlavorURL());
                        Log.d(TAG, response.toString());
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
                headers.put("Content-Type", "application/json; charset=utf-8");
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
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(request);
    }
    public void getFlavorsDetails(JSONObject jsonObject){
        openStackDatabase.getWritableDatabase();
        try{
            for (int i = 0; i <jsonObject.getJSONArray("flavors").length(); i++) {
                Flavor flavor=new Flavor();
                JSONObject flavorObj=jsonObject.getJSONArray("flavors").getJSONObject(i);
                Log.d(TAG,flavorObj.toString());
                flavor.setFlavorID(flavorObj.getString("id"));
                flavor.setFlavorName(flavorObj.getString("name"));
                flavor.setFlavorSize(flavorObj.getString("ram"));
                flavor.setFlavorDisk(flavorObj.getString("disk"));
                flavor.setFlavorURL(flavorObj.getJSONArray("links").getJSONObject(0).getString("href"));
                openStackDatabase.insertFlavor(flavor);
                FlavorList=openStackDatabase.getAllFlavors();
                Log.d(TAG,String.valueOf(FlavorList.size()));
                flavorsListAdapter.notifyDataSetChanged();
            }

        }catch (JSONException js){}
        openStackDatabase.close();
    }
    public String getFlavorURL(){

        String flavorURL=App_urls.allURLs.get("compute");
        if(flavorURL==null){
            Toast.makeText(getActivity(), "Unable to fetch flavor details!", Toast.LENGTH_LONG).show();
            flavorURL="";
        }
        else
        if(flavorURL != null)
         flavorURL=flavorURL.concat("/flavors/detail");
        return flavorURL;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        openStackDatabase = new OpenStackDatabase(getActivity());
        SendRequest(null, getFlavorURL());
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
    public void onDetach() {
        super.onDetach();
        openStackDatabase.close();
    }
}
