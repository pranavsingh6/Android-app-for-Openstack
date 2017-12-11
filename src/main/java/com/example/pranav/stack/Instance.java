package com.example.pranav.stack;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pranav on 20/09/15.
 */
public class Instance {
    public final String TAG = App_urls.class.getSimpleName();
    private  String instanceName;
    private  String instanceID;
    private  String instanceImage;
    private  String instanceIPAddr;
    private  String instanceSize;
    private  String instancePowerState;
    private  String instanceRunningTime;
    private  String instanceVCU;
    private  String instanceRAM;
    private  String tenantID;
    private  String instanceURL;
    private String instanceFlavor;

    public static Map <String,String>computeAttributes=new HashMap();
    public static String computeURL="";
    public static ArrayList<String> servers_names=new ArrayList<>();
    public static ArrayList<String> servers_uls=new ArrayList<>();
    public static ArrayList<String> servers_status=new ArrayList<>();

    public Instance(){
        //Log.d(TAG,"---------create instance------");
         instanceName = " ";
         instanceID = " ";
         instanceImage = " ";
         instanceIPAddr = " ";
         instanceSize = " ";
         instancePowerState = " ";
         instanceRunningTime = " ";
         instanceVCU = " ";
         instanceRAM = " ";
         tenantID = " ";
         instanceURL = " ";
        instanceFlavor = " ";
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }

    public String getInstanceImage() {
        return instanceImage;
    }

    public void setInstanceImage(String instanceImage) {
        this.instanceImage = instanceImage;
    }

    public String getInstanceIPAddr() {
        return instanceIPAddr;
    }

    public void setInstanceIPAddr(String instanceIPAddr) {
        this.instanceIPAddr = instanceIPAddr;
    }

    public String getInstanceSize() {
        return instanceSize;
    }

    public void setInstanceSize(String instanceSize) {
        this.instanceSize = instanceSize;
    }

    public String getInstancePowerState() {
        return instancePowerState;
    }

    public void setInstancePowerState(String instancePowerState) {
        this.instancePowerState = instancePowerState;
    }

    public String getInstanceRunningTime() {
        return instanceRunningTime;
    }

    public void setInstanceRunningTime(String instanceRunningTime) {
        this.instanceRunningTime = instanceRunningTime;
    }

    public String getInstanceVCU() {
        return instanceVCU;
    }

    public void setInstanceVCU(String instanceVCU) {
        this.instanceVCU = instanceVCU;
    }

    public String getInstanceRAM() {
        return instanceRAM;
    }

    public void setInstanceRAM(String instanceRAM) {
        this.instanceRAM = instanceRAM;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public String getInstanceURL() {
        return instanceURL;
    }

    public void setInstanceURL(String instanceURL) {
        this.instanceURL = instanceURL;
    }

    public String getInstanceFlavor() {
        return instanceFlavor;
    }

    public void setInstanceFlavor(String instanceFlavor) {
        this.instanceFlavor = instanceFlavor;
    }
}
