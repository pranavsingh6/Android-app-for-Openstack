package com.example.pranav.stack;

/**
 * Created by sid on 14/9/15.
 */
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class InstancesFragment extends Fragment {
    public static String TAG= InstancesFragment.class.getSimpleName();
    InstancesListAdapter instancesListAdapter;
    OpenStackDatabase openStackDatabase;
    private ProgressDialog progressDialog;
    public static List<Instance> InstanceList=new ArrayList<Instance>();
    public JSONObject server_details;
    FloatingActionButton floatingActionButton;
    /** The default number of retries */
    public static final int DEFAULT_MAX_RETRIES = 0;

    //private final Handler mHandler = new Handler();
    //private Thread thread;

    public InstancesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Log.d(TAG, "---InstanceFragment___");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_instances, container, false);
        final SwipeRefreshLayout swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        final RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.instance_list);
        floatingActionButton=(FloatingActionButton)rootView.findViewById(R.id.fab_instance);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        instancesListAdapter =new InstancesListAdapter();
        recyclerView.setAdapter(instancesListAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SendRequest(Request.Method.GET,null, getInstanceURL());
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
                //Log.d(TAG, "---Item location----");
                //Log.d(TAG,String.valueOf(viewHolder.getAdapterPosition()));
                //Log.d(TAG, String.valueOf(InstanceList.size()));
                if(viewHolder.getAdapterPosition() < InstanceList.size()) {
                    SendRequest(Request.Method.DELETE,null,getDeleteInstanceURL(InstanceList.get(viewHolder.getAdapterPosition()).getInstanceID()));
                    InstanceList.remove(viewHolder.getAdapterPosition());
                    //openStackDatabase.
                }
                instancesListAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.add_instance);
                dialog.setTitle("Create Instance :");
                dialog.setCancelable(false);
                EditText name = (EditText) dialog.findViewById(R.id.name_instance);
                EditText count = (EditText) dialog.findViewById(R.id.instance_count);
                Spinner spnFlavorsList = (Spinner) dialog.findViewById(R.id.flavors_list_spn);
                Spinner spnImagesList = (Spinner) dialog.findViewById(R.id.images_list_spn);
                View btnAdd = dialog.findViewById(R.id.btn_ok);
                View btnCancel = dialog.findViewById(R.id.btn_cancel);
                openStackDatabase.getReadableDatabase();
                ////Log.d(TAG, "---------button-------");
                List<Flavor> flavorArrayList = new ArrayList<Flavor>();
                List<Image> imagesArrayList = new ArrayList<Image>();
                flavorArrayList = openStackDatabase.getAllFlavors();
                imagesArrayList = openStackDatabase.getAllImages();
                openStackDatabase.close();
                if (flavorArrayList.size() <= 0 || imagesArrayList.size() <= 0) {

                    Toast.makeText(getActivity(),"Flavors/Images list is empty",Toast.LENGTH_SHORT).show();
                    SendRequest(Request.Method.GET,null, getImageURL());
                    SendRequest(Request.Method.GET, null, getFlavorURL());
                }
                if (flavorArrayList.size() > 0 && imagesArrayList.size() > 0)
                {
                    //Log.d(TAG, flavorArrayList.get(0).getFlavorName());
                    //Log.d(TAG, imagesArrayList.get(0).getImageName());

                    ArrayList<String> list_flavors = new ArrayList<String>();
                    for (int index = 0; index < flavorArrayList.size(); index++) {
                        list_flavors.add(flavorArrayList.get(index).getFlavorName());
                    }
                    ArrayList<String> list_images = new ArrayList<String>();
                    for (int index = 0; index < imagesArrayList.size(); index++) {
                        list_images.add(imagesArrayList.get(index).getImageName());
                    }
                    ArrayAdapter<String> spnAdapter_flavor = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_dropdown_item_1line, list_flavors);
                    spnFlavorsList.setAdapter(spnAdapter_flavor);
                    spnFlavorsList.setOnItemSelectedListener(onItemSelectedListener());

                    ArrayAdapter<String> spnAdapter_image = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_dropdown_item_1line, list_images);
                    spnImagesList.setAdapter(spnAdapter_image);
                    spnImagesList.setOnItemSelectedListener(onItemSelectedListener());


                    btnAdd.setOnClickListener(onConfirmListener(name, count, spnFlavorsList, spnImagesList, dialog));
                    btnCancel.setOnClickListener(onCancelListener(dialog));
                    //Log.d(TAG, spnFlavorsList.getSelectedItem().toString());
                    //Log.d(TAG, spnImagesList.getSelectedItem().toString());
                    dialog.show();
                }
                else
                    Toast.makeText(getActivity(),"Unable to fetch Image/Flavor details",Toast.LENGTH_LONG).show();

            }
        });

// Inflate the layout for this fragment
        return rootView;
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
                //ImageList=openStackDatabase.getAllImages();
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
    public void getFlavorsDetails(JSONObject jsonObject){
        openStackDatabase.getWritableDatabase();
        try{
            for (int i = 0; i <jsonObject.getJSONArray("flavors").length(); i++) {
                Flavor flavor=new Flavor();
                JSONObject flavorObj=jsonObject.getJSONArray("flavors").getJSONObject(i);
                //Log.d(TAG, flavorObj.toString());
                flavor.setFlavorID(flavorObj.getString("id"));
                flavor.setFlavorName(flavorObj.getString("name"));
                flavor.setFlavorSize(flavorObj.getString("ram"));
                flavor.setFlavorDisk(flavorObj.getString("disk"));
                flavor.setFlavorURL(flavorObj.getJSONArray("links").getJSONObject(0).getString("href"));
                openStackDatabase.insertFlavor(flavor);
                //FlavorList=openStackDatabase.getAllFlavors();
            }

        }catch (JSONException js){}
        openStackDatabase.close();
    }
    public String getFlavorURL(){
        String flavorURL=App_urls.allURLs.get("compute");
        if(flavorURL==null){
            Toast.makeText(getActivity(), "Unable to fetch image details!", Toast.LENGTH_LONG).show();
            flavorURL="";
        }
        else
           flavorURL=flavorURL.concat("/flavors/detail");
        return flavorURL;
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(TAG,"------------position---------");
                //Log.d(TAG,String.valueOf(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }
    private View.OnClickListener onConfirmListener(final EditText name, final EditText count,final Spinner flavor_spinner,final Spinner image_spinner, final Dialog dialog) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SendRequest(Request.Method.POST,createInstanceJSONObject(name.getText().toString().trim(), count.getText().toString().trim(), flavor_spinner.getSelectedItem().toString().trim(), image_spinner.getSelectedItem().toString().trim()),getInstanceCreateURL());
                instancesListAdapter.notifyDataSetChanged();
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

    public JSONObject createInstanceJSONObject(String instanceName, String count,String flavor,String image) {
        JSONObject retJSON;
        retJSON = new JSONObject();
        try {
            openStackDatabase.getReadableDatabase();
            List<Flavor> flavorArrayList=new ArrayList<Flavor>();
            List<Image> imageArrayList=new ArrayList<Image>();
            flavorArrayList=openStackDatabase.getAllFlavors();
            imageArrayList=openStackDatabase.getAllImages();
            openStackDatabase.close();
            ////Log.d(TAG, "---------------flavor----------------\n");
            for (int i = 0; i <flavorArrayList.size() ; i++) {
                ////Log.d(TAG, flavorArrayList.get(i).getFlavorName());
                ////Log.d(TAG, flavorArrayList.get(i).getFlavorURL());
                if(flavorArrayList.get(i).getFlavorName().equals(flavor)) {
                    ////Log.d(TAG, "----match---flavor---");
                    flavor=flavorArrayList.get(i).getFlavorURL();
                }

            }
            ////Log.d(TAG, "---------------image----------------\n");
            for (int i = 0; i < imageArrayList.size(); i++) {
                ////Log.d(TAG, imageArrayList.get(i).getImageName());
                ////Log.d(TAG, imageArrayList.get(i).getImageID());
                if (imageArrayList.get(i).getImageName().equals(image)) {
                    ////Log.d(TAG, "----match---image---");
                    image=imageArrayList.get(i).getImageID();
                    image=App_urls.HOST_URL.concat("/v2/images/").concat(image);
                }
            }
            //Log.d(TAG, "---------------logic----------------\n");

            JSONObject server_obj = new JSONObject();
            server_obj.put("name", instanceName);
            server_obj.put("imageRef", image);
            server_obj.put("flavorRef", flavor);

            JSONObject metaData = new JSONObject();
            metaData.put("My Server Name", "Apache1");
            server_obj.put("metadata", metaData);

            server_obj.put("min_count", count);
            server_obj.put("max_count", count);
            retJSON.put("server", server_obj);

            ////Log.d(TAG, "---------------start----------------\n");
            ////Log.d(TAG, retJSON.toString(2));
            ////Log.d(TAG, "----------------end----------------");

        } catch (JSONException ex) {
            //Log.d(TAG, "Error Occurred while building JSON");
            ex.printStackTrace();
        }
        return retJSON;
    }

    private void SendRequest(final int requestType,final JSONObject jsonObject,final String url)
    {
        showDialog();
        //Log.d(TAG,"------------------send request-----------------------");
        //Log.d(TAG,url);
        JsonObjectRequest request = new JsonObjectRequest(requestType,url,jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        //Log.d(TAG, response.toString());
                        if(url.contains("usage") && url.contains("tenant"))
                            getOverview(response);
                        else if (url.contains("image"))
                            getImageDetails(response);
                        else if (url.contains("flavor"))
                            getFlavorsDetails(response);
                        else if(url.contains("detail")&&url.contains("servers"))
                          getInstanceDetails(response);
                        else
                            //getInstanceDetails(response);
                            getNewInstanceDetails(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d(TAG, "overview error:");
                        //Log.d(TAG,error.toString());
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
                headers.put("X-Auth-Token", App_urls.TokenID);
                ////Log.d(TAG, "--------headers----");
                ////Log.d(TAG,headers.toString());
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



    public String getInstanceURL(){
        String instanceURL=App_urls.allURLs.get("compute");
        if(instanceURL==null){
            Toast.makeText(getActivity(), "Unable to fetch instance details!", Toast.LENGTH_LONG).show();
            instanceURL="";
        }
        else
            instanceURL = instanceURL.concat("/servers/detail");

        return instanceURL;
    }
    public String getInstanceCreateURL(){

        String instanceURL=App_urls.allURLs.get("compute");
        if(instanceURL==null){
            Toast.makeText(getActivity(), "Unable to fetch instance details!", Toast.LENGTH_LONG).show();
            instanceURL="";
        }
        else
            instanceURL = instanceURL.concat("/servers");

        return instanceURL;
    }
    public String getDeleteInstanceURL(String id){

        String instanceURL=App_urls.allURLs.get("compute");
        if(instanceURL==null){
            Toast.makeText(getActivity(), "Unable to fetch instance details!", Toast.LENGTH_LONG).show();
            instanceURL="";
        }
        else
          instanceURL=instanceURL.concat("/servers/").concat(id);
        return instanceURL;
    }
    public void getInstanceDetails(JSONObject jsonObject) {
       server_details=jsonObject;
        SendRequest(Request.Method.GET,null,getUsageURL());

    }
    public void getNewInstanceDetails(JSONObject jsonObject) {
        //Log.d(TAG, "-----servers add----");
        //Log.d(TAG, jsonObject.toString());

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                //Log.d(TAG, "-----servers sleep----");
                SendRequest(Request.Method.GET, null, getInstanceURL());
            }
        }, 5000);
        /*
        thread = new Thread() {
            public void run() {
                // do something here
                SendRequest(Request.Method.GET, null, getInstanceURL());
                mHandler.postDelayed(this, 3000);
            }
        };*/

    }
    public String getUsageURL(){
        Calendar cal = Calendar.getInstance();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        String time = new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
        time=time.concat(".000000");
        String currentDateTime=date.concat("T").concat(time);
        cal.add(Calendar.DATE, -60);
        String previousDate=new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        String previousDateTime=previousDate.concat("T").concat(time);
        String tenantUsageURL=App_urls.allURLs.get("compute");
        int last=tenantUsageURL.lastIndexOf("/");
        tenantUsageURL=tenantUsageURL.substring(0, last);
        //tenantUsageURL=tenantUsageURL.concat("/").concat(App_urls.TenantID).concat("/os-simple-tenant-usage?start=").concat(previousDateTime).concat("&end=").concat(currentDateTime).concat("&detailed=1");
        //tenantUsageURL=tenantUsageURL.concat("/").concat(App_urls.TenantID).concat("/os-simple-tenant-usage?start=").concat(previousDateTime).concat("&end=").concat(currentDateTime).concat("&detailed=1");
        tenantUsageURL=tenantUsageURL.concat("/").concat(App_urls.TenantID).concat("/os-simple-tenant-usage?detailed=1");

        return tenantUsageURL;
    }
    public void getOverview(JSONObject jsonObject)
    {
        //Log.d(TAG, jsonObject.toString());
        openStackDatabase.getWritableDatabase();
        openStackDatabase.createAllTables(1);
        try {
            for (int i = 0; i <1 /*jsonObject.getJSONArray("tenant_usages").length()*/; i++) {

                ////Log.d(TAG,String.valueOf(jsonObject.getJSONArray("tenant_usages").getJSONObject(i).getJSONArray("server_usages").length()));
                for (int j = 0; j < jsonObject.getJSONArray("tenant_usages").getJSONObject(i).getJSONArray("server_usages").length(); j++)
                {
                    Instance instance=new Instance();
                    JSONObject server_det=server_details.getJSONArray("servers").getJSONObject(j);
                    instance.setInstancePowerState(server_det.getString("status"));
                    Log.d(TAG, "----------------status------------------------");
                    Log.d(TAG,server_det.getString("status"));
                    instance.setInstanceName(server_det.getString("name"));
                    instance.setTenantID(server_det.getString("tenant_id"));
                    instance.setInstanceID(server_det.getString("id"));
                    Log.d(TAG,server_det.getJSONObject("addresses").toString());
                    if(server_det.getJSONObject("addresses").has("private") )
                    {
                        if(server_det.getJSONObject("addresses").getJSONArray("private").length()>1)
                            instance.setInstanceIPAddr(server_det.getJSONObject("addresses").getJSONArray("private").getJSONObject(1).getString("addr").toString());
                        else
                            instance.setInstanceIPAddr(server_det.getJSONObject("addresses").getJSONArray("private").getJSONObject(0).getString("addr").toString());
                        Log.d(TAG, "-----------private--------");
                         Log.d(TAG, server_det.getJSONObject("addresses").getJSONArray("private").getJSONObject(0).toString());
                    }
                    else if(server_det.getJSONObject("addresses").has("public") )
                    {
                        if(server_det.getJSONObject("addresses").getJSONArray("public").length()>1)
                            instance.setInstanceIPAddr(server_det.getJSONObject("addresses").getJSONArray("public").getJSONObject(1).getString("addr").toString());
                        else
                            instance.setInstanceIPAddr(server_det.getJSONObject("addresses").getJSONArray("public").getJSONObject(0).getString("addr").toString());
                        Log.d(TAG, "-----------private--------");
                        Log.d(TAG, server_det.getJSONObject("addresses").getJSONArray("public").getJSONObject(0).toString());
                    }
                    else if(server_det.getJSONObject("addresses").has("IPNA"))
                    {
                        if(server_det.getJSONObject("addresses").getJSONArray("IPNA").length()>1)
                            instance.setInstanceIPAddr(server_det.getJSONObject("addresses").getJSONArray("IPNA").getJSONObject(1).getString("addr").toString());
                        else
                            instance.setInstanceIPAddr(server_det.getJSONObject("addresses").getJSONArray("IPNA").getJSONObject(0).getString("addr").toString());
                        //Log.d(TAG, "-----------overview -begin----");
                        //Log.d(TAG, server_det.getJSONObject("addresses").getJSONArray("IPNA").getJSONObject(0).toString());

                    }


                    JSONObject server=jsonObject.getJSONArray("tenant_usages").getJSONObject(i).getJSONArray("server_usages").getJSONObject(j);
                    //Log.d(TAG, "-----------overview -if----");
                    //Log.d(TAG,"----------2------");
                    instance.setInstanceRAM(server.getString("memory_mb"));
                    instance.setInstanceVCU(server.getString("vcpus"));
                    //Log.d(TAG, "----------3------");
                    instance.setInstanceRunningTime(server.getString("uptime"));
                    instance.setInstanceFlavor(server.getString("flavor"));
                    //Log.d(TAG,instance.getInstanceName());
                    //Log.d(TAG,instance.getInstanceID());
                    //Log.d(TAG,instance.getInstancePowerState());
                    //Log.d(TAG,instance.getInstanceRAM());
                    //Log.d(TAG,instance.getInstanceIPAddr());
                    //Log.d(TAG,instance.getInstanceVCU());
                    //Log.d(TAG,instance.getInstanceRunningTime());
                    //Log.d(TAG, instance.getInstanceFlavor());
                    openStackDatabase.insertInstance(instance);

                }

            }
            InstanceList=openStackDatabase.getAllInstances();
            instancesListAdapter.notifyDataSetChanged();
            openStackDatabase.close();
        }catch (JSONException js){
            //Log.d(TAG,"-----------overview exception-----");
            //Log.d(TAG,js.toString());
            openStackDatabase.close();
        }
        /*
        progressBar_instances.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        ProgressBarAnimator anim_instance = new ProgressBarAnimator(progressBar_instances, 0, 70);
        anim_instance.setDuration(1000);
        progressBar_instances.startAnimation(anim_instance);
        */
        //ProgressBarAnimator anim_ram = new ProgressBarAnimator(progressBar_ram, 0, 70);
        //anim_ram.setDuration(1500);
        //progressBar_ram.startAnimation(anim_ram);

        //total_instances.setText(tenant.getTotal_instances());
        //total_ram.setText(tenant.getTotal_ram());
        //total_vcpu.setText(tenant.getTotal_vcpus());
        //SendRequest(null,getLimitsURL());
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        openStackDatabase = new OpenStackDatabase(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCancelable(false);
        SendRequest(Request.Method.GET, null, getInstanceURL());
        SendRequest(Request.Method.GET, null, getImageURL());
        SendRequest(Request.Method.GET, null, getFlavorURL());
        //mHandler.removeCallbacks(thread);
        //mHandler.postDelayed(thread, 0);
        //Log.d(TAG, "onResume");
        //InstanceList=openStackDatabase.getAllInstances();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mHandler.removeCallbacks(thread);
        //Log.d(TAG, "onPause");
        openStackDatabase.close();
    }
    private void showDialog() {

        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog(){
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }
}