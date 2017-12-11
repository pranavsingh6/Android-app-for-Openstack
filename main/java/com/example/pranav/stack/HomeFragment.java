package com.example.pranav.stack;

/**
 * Created by sid on 14/9/15.
 */
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {
    public static String TAG= InstancesFragment.class.getSimpleName();
    OpenStackDatabase openStackDatabase;
    ProgressBar progressBar_instances;
    ProgressBar progressBar_ram;
    ProgressBar progressBar_vcpu;
    ProgressBar progressBar_security;
    private ProgressDialog progressDialog;

    public static List<Tenant> TenantList=new ArrayList<Tenant>();
    Tenant tenant;
    TextView total_instances;
    TextView total_ram;
    TextView total_vcpu;
    TextView total_security;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        tenant=new Tenant();
        //total_instances=(TextView)rootView.findViewById(R.id.textView_instances_running);
        //total_ram=(TextView)rootView.findViewById(R.id.textView_ram_used);
        //total_vcpu=(TextView)rootView.findViewById(R.id.textView_vcpu_used);
        progressBar_instances=(ProgressBar)rootView.findViewById(R.id.progressBar_instances);
        progressBar_ram=(ProgressBar)rootView.findViewById(R.id.progressBar_ram);
        progressBar_vcpu=(ProgressBar)rootView.findViewById(R.id.progressBar_vcpu);
        progressBar_security=(ProgressBar)rootView.findViewById(R.id.progressBar_security);
        total_instances=(TextView)rootView.findViewById(R.id.progress_textview_instances);
        total_ram=(TextView)rootView.findViewById(R.id.progress_textview_ram);
        total_vcpu=(TextView)rootView.findViewById(R.id.progress_textview_vcpu);
        total_security=(TextView)rootView.findViewById(R.id.progress_textview_security);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        openStackDatabase = new OpenStackDatabase(getActivity());
        //SendRequest(null, getLimitsURL());
        //SendRequest(null, getUsageURL());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        openStackDatabase.close();
    }

    private void SendRequest(final JSONObject jsonObject, final String url)
    {
showDialog();
        JsonObjectRequest request = new JsonObjectRequest((jsonObject == null) ? Request.Method.GET:Request.Method.POST,url,jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
hideDialog();
                        //Log.d(TAG,response.toString());
                        getLimits(response);
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
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        Volley.newRequestQueue(getActivity()).add(request);
    }




    public void getLimits(JSONObject jsonObject) {
        Log.d(TAG,jsonObject.toString());
        Tenant tenant_tmp=new Tenant();
        openStackDatabase.getWritableDatabase();
        openStackDatabase.createAllTables(0);
        try {
          JSONObject limits=jsonObject.getJSONObject("limits").getJSONObject("absolute");

            tenant_tmp.setTotal_instances(limits.getString("maxTotalInstances"));
            tenant_tmp.setTotal_vcpus(limits.getString("maxTotalCores"));
            tenant_tmp.setTotal_ram(limits.getString("maxTotalRAMSize"));
            tenant_tmp.setTenantAvailabilityZone(limits.getString("maxSecurityGroups"));
            tenant.setTotal_instances(limits.getString("totalInstancesUsed"));
            tenant.setTotal_ram(limits.getString("totalRAMUsed"));
            tenant.setTotal_vcpus(limits.getString("totalCoresUsed"));
            tenant.setTenantAvailabilityZone(limits.getString("totalSecurityGroupsUsed"));

            openStackDatabase.insertTenant(tenant_tmp);
            openStackDatabase.close();
        }
    catch (JSONException js){
        openStackDatabase.close();
    }

        Log.d(TAG,"--------get--limits values-----");
        Log.d(TAG,tenant_tmp.getTenantAvailabilityZone());
        Log.d(TAG, tenant_tmp.getTotal_instances());
        Log.d(TAG, tenant_tmp.getTotal_ram());
        Log.d(TAG, tenant_tmp.getTotal_vcpus());
        Log.d(TAG,"--------get--limits cal-----");
        Log.d(TAG, tenant.getTenantAvailabilityZone());
        Log.d(TAG, tenant.getTotal_instances());
        Log.d(TAG, tenant.getTotal_ram());
        Log.d(TAG, tenant.getTotal_vcpus());

        String tmp="";
        tmp=tmp.concat("INSTANCES").concat("\n    ").concat(tenant.getTotal_instances()).concat("/").concat(tenant_tmp.getTotal_instances());
        total_instances.setText(tmp);
        tmp="";
        tmp=tmp.concat("  RAM").concat("(MB)").concat("\n  ").concat(tenant.getTotal_ram()).concat("/").concat(tenant_tmp.getTotal_ram());
        total_ram.setText(tmp);
        tmp="";
        tmp=tmp.concat("VCPU").concat("\n ").concat(tenant.getTotal_vcpus()).concat("/").concat(tenant_tmp.getTotal_vcpus());
        total_vcpu.setText(tmp);
        tmp="";
        tmp=tmp.concat("SECURITY ZONE").concat("\n         ").concat(tenant.getTenantAvailabilityZone()).concat("/").concat(tenant_tmp.getTenantAvailabilityZone());
        total_security.setText(tmp);
        int per;
        per=getPercentage(Integer.valueOf(tenant.getTotal_instances()),Integer.valueOf(tenant_tmp.getTotal_instances()));
        progressBar_instances.setProgress(per);
        progressBar_instances.setSecondaryProgress(100);
        progressBar_instances.setMax(100);
        int[] p=new int[2];
        p[0]=25;
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar_instances, "progress", 0, per);
        animation.setDuration(990);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        per=getPercentage(Integer.valueOf(tenant.getTotal_ram()), Integer.valueOf(tenant_tmp.getTotal_ram()));
        progressBar_ram.setProgress(per);
        progressBar_ram.setSecondaryProgress(100);
        progressBar_ram.setMax(100);

        ObjectAnimator animation1 = ObjectAnimator.ofInt(progressBar_ram, "progress", 0, per);
        animation1.setDuration(990);
        animation1.setInterpolator(new DecelerateInterpolator());
        animation1.start();

        //ProgressBarAnimator anim_vcpu = new ProgressBarAnimator(progressBar_vcpu, 0, 70);
        //anim_vcpu.setDuration(1500);
        //progressBar_vcpu.startAnimation(anim_vcpu);
        per=getPercentage(Integer.valueOf(tenant.getTotal_vcpus()),Integer.valueOf(tenant_tmp.getTotal_vcpus()));
        progressBar_vcpu.setProgress(per);
        progressBar_vcpu.setSecondaryProgress(100);
        progressBar_vcpu.setMax(100);
        //p[0]=25;
        ObjectAnimator animation2 = ObjectAnimator.ofInt(progressBar_vcpu, "progress", 0, per);
        animation2.setDuration(990);
        animation2.setInterpolator(new DecelerateInterpolator());
        animation2.start();

        per=getPercentage(Integer.valueOf(tenant.getTenantAvailabilityZone()), Integer.valueOf(tenant_tmp.getTenantAvailabilityZone()));
        progressBar_security.setProgress(per);
        progressBar_security.setSecondaryProgress(100);
        progressBar_security.setMax(100);
        //p[0]=25;
        ObjectAnimator animation3 = ObjectAnimator.ofInt(progressBar_security, "progress", 0, per);
        animation3.setDuration(990);
        animation3.setInterpolator(new DecelerateInterpolator());
        animation3.start();
    }
    private void showDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog(){
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
    public String getLimitsURL(){

        String limitURL=App_urls.allURLs.get("compute");
        if(limitURL==null){
            Toast.makeText(getActivity(), "Unable to fetch limit details!", Toast.LENGTH_LONG).show();
            limitURL="";
        }
        if(limitURL != null)
          limitURL=limitURL.concat("/limits");

        Log.d(TAG,limitURL);
        return limitURL;
    }
    public int getPercentage(int value,int limit){
        float tmp=(value*100) / limit;
        int i=(int)tmp;
        return i;
    }

}