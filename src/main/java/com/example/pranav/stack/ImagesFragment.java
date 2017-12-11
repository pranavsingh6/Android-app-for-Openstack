package com.example.pranav.stack;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pranav on 29/09/15.
 */
public class ImagesFragment extends Fragment {
    public static String TAG= ImagesFragment.class.getSimpleName();
    ImagesListAdapter imagesListAdapter;
    OpenStackDatabase openStackDatabase;
    private ProgressDialog progressDialog;
    public static List<Image> ImageList=new ArrayList<Image>();
    //ImageButton imageButton;
    public ImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedImagestate) {

        super.onCreate(savedImagestate);
        Log.d(TAG, "---ImageFragment---");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedImagestate) {
        View rootView = inflater.inflate(R.layout.fragment_images, container, false);
        final RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.images_list);
        //imageButton=(ImageButton)rootView.findViewById(R.id.fab_image);
        final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        imagesListAdapter =new ImagesListAdapter();
        recyclerView.setAdapter(imagesListAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SendRequest(null, getImageURL());
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
                Log.d(TAG, "---Item location----");

                if(viewHolder.getAdapterPosition() < ImageList.size()) {
                    ImageList.remove(viewHolder.getAdapterPosition());
                    //openStackDatabase.
                }
                imagesListAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
// Inflate the layout for this fragment
        return rootView;
    }
    private void SendRequest(JSONObject jsonObject, String url)
    {
        showDialog();
        JsonObjectRequest request = new JsonObjectRequest((jsonObject == null) ? Request.Method.GET:Request.Method.POST,url,jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, response.toString());
                        hideDialog();
                        getImageDetails(response);
                        Log.d(TAG, "Response-image--------");
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
        Volley.newRequestQueue(getActivity()).add(request);
    }

    public void getImageDetails(JSONObject jsonObject) {
        openStackDatabase.getWritableDatabase();
        try{
            for (int i = 0; i <jsonObject.getJSONArray("images").length(); i++) {
                Image image=new Image();
                JSONObject imageObj=jsonObject.getJSONArray("images").getJSONObject(i);
                image.setImageID(imageObj.getString("id"));
                image.setImageName(imageObj.getString("name"));
                image.setImageSize(imageObj.getString("size"));
                image.setImageState(imageObj.getString("status"));
                image.setImageType(imageObj.getString("disk_format"));
                openStackDatabase.insertImage(image);
                ImageList=openStackDatabase.getAllImages();

                imagesListAdapter.notifyDataSetChanged();
            }

        }catch (JSONException js){}
        openStackDatabase.close();
    }
    public String getImageURL(){

        String imageURL=App_urls.allURLs.get("image");
        if(imageURL==null){
            Toast.makeText(getActivity(), "Unable to fetch image details!", Toast.LENGTH_LONG).show();
            imageURL="";
        }
        else
          imageURL=imageURL.concat("/v2/images");
        return imageURL;
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
        SendRequest(null, getImageURL());
        //ImageList=openStackDatabase.getAllInstances();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        openStackDatabase.close();
    }
}

