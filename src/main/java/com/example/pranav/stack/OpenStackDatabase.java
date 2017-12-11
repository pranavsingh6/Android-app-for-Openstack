package com.example.pranav.stack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pranav on 03/10/15.
 */
public class OpenStackDatabase extends SQLiteOpenHelper{
    public static final String TAG=OpenStackDatabase.class.getSimpleName();
    public static final String WHITESPACE=" ";
    public static final String OPENINGBRACKETS=" (";
    public static final String CLOSINGBRACKETS=");";
    public static final String TEXT=" TEXT ";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "openstackDB.db";

    private static final String TENANT_TABLE = "tenantTable";
    private static final String TENANT_ID = "tenantId";
    private static final String TENANT_NAME = "tenantName";
    private static final String TENANT_ZONE = "tenantZone";
    private static final String TENANT_RAM= "tenantRam";
    private static final String TENANT_VCPU="tenantVcpu";
    private static final String TENANT_INSTANCES="tenantInstance";

    private static final String INSTANCE_TABLE="instanceTable";
    private static final String INSTANCE_NAME="instanceName";
    private static final String INSTANCE_ID="instanceID";
    private static final String INSTANCE_URL="instanceURL";
    private static final String INSTANCE_IMAGE="instanceImage";
    private static final String INSTANCE_IPADDR="instanceIPAddr";
    private static final String INSTANCE_SIZE="instanceSize";
    private static final String INSTANCE_POWERSTATE="instancePowerState";
    private static final String INSTANCE_RUNNINGTIME="instanceRunningTime";
    private static final String INSTANCE_VCPU = "instanceVCU";
    private static final String INSTANCE_RAM = "instanceRAM";
    private static final String INSTANCE_FLAVOR="instanceFlavor";

    private static final String IMAGE_TABLE="imageTable";
    private static final String IMAGE_NAME="imageName";
    private static final String IMAGE_URL="imageURL";
    private static final String IMAGE_ID="imageID";
    private static final String IMAGE_TYPE="imageType";
    private static final String IMAGE_STATE="imageState";
    private static final String IMAGE_SIZE="imageSize";

    private static final String VOLUME_TABLE="volumeTable";
    private static final String VOLUME_ID="volumeID";
    private static final String VOLUME_NAME="volumeName";
    private static final String VOLUME_SIZE="volumeSize";
    private static final String VOLUME_STATUS="volumeStatus";
    private static final String VOLUME_TYPE="volumeType";

    private static final String FLAVOR_TABLE="flavorTable";
    private static final String FLAVOR_ID="flavorID";
    private static final String FLAVOR_NAME="flavorName";
    private static final String FLAVOR_SIZE="flavorSize";
    private static final String FLAVOR_DISK="flavorDisk";
    private static final String FLAVOR_URL="flavorURL";

    public OpenStackDatabase (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //Log.d(TAG, "-------constructor----");
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Log.d(TAG, "----create db--");
        //dropAllTables();
        createTenantTable(sqLiteDatabase);
        createInstanceTable(sqLiteDatabase);
        createImageTable(sqLiteDatabase);
        createVolumeTable(sqLiteDatabase);
        createFlavorTable(sqLiteDatabase);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int newVersion,int oldVersion){
    onCreate(sqLiteDatabase);
    }

    public void createTenantTable(SQLiteDatabase sqLiteDatabase){
        String DELETE_TENANT_TABLE = "DROP TABLE IF EXISTS "+WHITESPACE+TENANT_TABLE+";";
        sqLiteDatabase.execSQL(DELETE_TENANT_TABLE);
        String CREATE_TENANT_TABLE="CREATE TABLE"+WHITESPACE+TENANT_TABLE+OPENINGBRACKETS+TENANT_ID+TEXT+","+TENANT_NAME+WHITESPACE+TEXT+","+TENANT_ZONE+TEXT+","+TENANT_RAM+WHITESPACE+TEXT+","+TENANT_VCPU+WHITESPACE+TEXT+","+TENANT_INSTANCES+WHITESPACE+TEXT+CLOSINGBRACKETS;
        sqLiteDatabase.execSQL(CREATE_TENANT_TABLE);
        //Log.d(TAG, CREATE_TENANT_TABLE);
    }
    public void createInstanceTable(SQLiteDatabase sqLiteDatabase){
        String DELETE_INSTANCE_TABLE = "DROP TABLE IF EXISTS "+WHITESPACE+INSTANCE_TABLE+";";
        sqLiteDatabase.execSQL(DELETE_INSTANCE_TABLE);
        String CREATE_INSTANCE_TABLE="CREATE TABLE"+WHITESPACE+INSTANCE_TABLE+OPENINGBRACKETS+INSTANCE_ID+TEXT+","+INSTANCE_NAME+TEXT+","+INSTANCE_IMAGE+TEXT+","+INSTANCE_IPADDR+TEXT+","+INSTANCE_POWERSTATE+TEXT+","+INSTANCE_SIZE+TEXT+","+INSTANCE_RAM+TEXT+","+TENANT_ID+TEXT+","+INSTANCE_RUNNINGTIME+TEXT+","+INSTANCE_VCPU+TEXT+","+INSTANCE_URL+TEXT+","+INSTANCE_FLAVOR+TEXT+CLOSINGBRACKETS;
        sqLiteDatabase.execSQL(CREATE_INSTANCE_TABLE);
        //Log.d(TAG, CREATE_INSTANCE_TABLE);
    }
    public void createImageTable(SQLiteDatabase sqLiteDatabase){
        String DELETE_IMAGE_TABLE = "DROP TABLE IF EXISTS "+WHITESPACE+IMAGE_TABLE+";";
        sqLiteDatabase.execSQL(DELETE_IMAGE_TABLE);
        String CREATE_IMAGE_TABLE="CREATE TABLE"+WHITESPACE+IMAGE_TABLE+OPENINGBRACKETS+IMAGE_ID+TEXT+","+IMAGE_NAME+TEXT+","+IMAGE_TYPE+TEXT+","+IMAGE_STATE+TEXT+","+IMAGE_SIZE+TEXT+","+IMAGE_URL+TEXT+CLOSINGBRACKETS;
        sqLiteDatabase.execSQL(CREATE_IMAGE_TABLE);
        //Log.d(TAG, CREATE_IMAGE_TABLE);
    }
    public void createVolumeTable(SQLiteDatabase sqLiteDatabase){
        String DELETE_VOLUME_TABLE = "DROP TABLE IF EXISTS "+WHITESPACE+VOLUME_TABLE+";";
        sqLiteDatabase.execSQL(DELETE_VOLUME_TABLE);
        String CREATE_VOLUME_TABLE="CREATE TABLE"+WHITESPACE+VOLUME_TABLE+OPENINGBRACKETS+VOLUME_ID+TEXT+","+VOLUME_NAME+TEXT+","+VOLUME_SIZE+TEXT+","+VOLUME_STATUS+TEXT+","+VOLUME_TYPE+TEXT+CLOSINGBRACKETS;
        sqLiteDatabase.execSQL(CREATE_VOLUME_TABLE);
        //Log.d(TAG, CREATE_VOLUME_TABLE);
    }
    public void createFlavorTable(SQLiteDatabase sqLiteDatabase){
        String DELETE_FLAVOR_TABLE = "DROP TABLE IF EXISTS "+WHITESPACE+FLAVOR_TABLE+";";
        sqLiteDatabase.execSQL(DELETE_FLAVOR_TABLE);
        String CREATE_FLAVOR_TABLE="CREATE TABLE"+WHITESPACE+FLAVOR_TABLE+OPENINGBRACKETS+FLAVOR_ID+TEXT+","+FLAVOR_NAME+TEXT+","+FLAVOR_SIZE+TEXT+","+FLAVOR_DISK+TEXT+","+FLAVOR_URL+TEXT+CLOSINGBRACKETS;
        sqLiteDatabase.execSQL(CREATE_FLAVOR_TABLE);
        //Log.d(TAG, CREATE_FLAVOR_TABLE);
    }

    public void insertTenant(Tenant tenant){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TENANT_ID,tenant.getTenantId());
        values.put(TENANT_NAME, tenant.getTenantName());
        values.put(TENANT_ZONE, tenant.getTenantAvailabilityZone());
        values.put(TENANT_RAM,tenant.getTotal_ram());
        values.put(TENANT_VCPU,tenant.getTotal_vcpus());
        values.put(TENANT_INSTANCES,tenant.getTotal_instances());
        database.insert(TENANT_TABLE, null, values);
        database.close();
    }
    public void updateInstanceIP (Instance instance) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor=null;
        try {
            String query ="UPDATE "+ INSTANCE_TABLE + " SET "+ INSTANCE_IPADDR + " = \""+instance.getInstanceIPAddr()+"\""+ " WHERE " + INSTANCE_NAME + " = \""+instance.getInstanceName()+"\"";
           //Log.d(TAG,query);
           //Log.d(TAG, "------------Update INSTANCE---------------");
            cursor = database.rawQuery(query, null);
        } catch (SQLException e){
        }
        if(cursor !=null)
         cursor.close();
        database.close();
    }
    public void insertInstance(Instance instance) {
        SQLiteDatabase database = this.getWritableDatabase();
       //Log.d(TAG, "------------INSERT INSTANCE---------------");
       //Log.d(TAG, instance.getInstanceName());
        Cursor cursor=null;
        try {
            String query = "SELECT * FROM "
                    + INSTANCE_TABLE + " WHERE " + INSTANCE_ID + " = \"" + instance.getInstanceID()+"\"";
           //Log.d(TAG,query);
            cursor = database.rawQuery(query, null);
            if(cursor==null||cursor.moveToFirst()==false){

               //Log.d(TAG, "------------INSERT INSTANCE--if-------------");
                ContentValues values = new ContentValues();
                values.put(INSTANCE_ID, instance.getInstanceID());
                values.put(INSTANCE_NAME, instance.getInstanceName());
                values.put(INSTANCE_IMAGE, instance.getInstanceImage());
                values.put(INSTANCE_IPADDR, instance.getInstanceIPAddr());
                values.put(INSTANCE_POWERSTATE, instance.getInstancePowerState());
                values.put(INSTANCE_RUNNINGTIME, instance.getInstanceRunningTime());
                values.put(INSTANCE_RAM, instance.getInstanceRAM());
                values.put(INSTANCE_SIZE, instance.getInstanceSize());
                values.put(INSTANCE_VCPU, instance.getInstanceVCU());
                values.put(INSTANCE_URL, instance.getInstanceURL());
                values.put(INSTANCE_FLAVOR,instance.getInstanceFlavor());
                database.insert(INSTANCE_TABLE, null, values);
            }

        } catch (SQLException e){
        }
        if(cursor !=null)
            cursor.close();
        database.close();
    }

    public void insertImage(Image image){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor=null;

       //Log.d(TAG, "------------INSERT Image---------------");
        try {
            String query = "SELECT * FROM "
                    + IMAGE_TABLE + " WHERE " + IMAGE_ID + " = \"" + image.getImageID()+"\"";
           //Log.d(TAG,query);
            cursor = database.rawQuery(query, null);
            if(cursor == null || cursor.moveToFirst()==false){
               //Log.d(TAG, "------------INSERT Image--if-------------");
                ContentValues values = new ContentValues();
                values.put(IMAGE_ID,image.getImageID());
                values.put(IMAGE_NAME,image.getImageName());
                values.put(IMAGE_SIZE,image.getImageSize());
                values.put(IMAGE_STATE,image.getImageState());
                values.put(IMAGE_TYPE,image.getImageType());
                database.insert(IMAGE_TABLE,null,values);
            }
        } catch (SQLException e){
        }
        if(cursor !=null)
            cursor.close();
        database.close();
    }
    public void insertFlavor(Flavor flavor){
        Cursor cursor=null;
        SQLiteDatabase database = this.getWritableDatabase();

       //Log.d(TAG, "------------INSERT flavor---------------");
        try {
            String query = "SELECT * FROM "
                    + FLAVOR_TABLE + " WHERE " + FLAVOR_ID + " = \"" + flavor.getFlavorID()+"\"";
           //Log.d(TAG, query);
            cursor = database.rawQuery(query, null);
            if(cursor.moveToFirst()==false){

               //Log.d(TAG, "------------INSERT flavor--if-------------");
                ContentValues values = new ContentValues();
                values.put(FLAVOR_ID, flavor.getFlavorID());
                values.put(FLAVOR_NAME, flavor.getFlavorName());
                values.put(FLAVOR_SIZE, flavor.getFlavorSize());
                values.put(FLAVOR_DISK, flavor.getFlavorDisk());
                values.put(FLAVOR_URL, flavor.getFlavorURL());
                database.insert(FLAVOR_TABLE, null, values);
            }
        } catch (SQLException e) {
        }
        cursor.close();
        database.close();
    }
    public void insertVolume(Volume volume){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VOLUME_ID, volume.getVolumeID());
        values.put(VOLUME_NAME,volume.getVolumeName());
        values.put(VOLUME_SIZE,volume.getVolumeSize());
        values.put(VOLUME_STATUS,volume.getVolumeStatus());
        values.put(VOLUME_TYPE, volume.getVolumeType());
        database.insert(VOLUME_TABLE, null, values);
        database.close();
    }
    public void dropAllTables(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        String DELETE_TENANT_TABLE = "DROP TABLE IF EXISTS "+WHITESPACE+TENANT_TABLE+";";
        String DELETE_INSTANCE_TABLE = "DROP TABLE IF EXISTS "+WHITESPACE+INSTANCE_TABLE+";";
        String DELETE_IMAGE_TABLE = "DROP TABLE IF EXISTS "+WHITESPACE+IMAGE_TABLE+";";
        String DELETE_VOLUME_TABLE = "DROP TABLE IF EXISTS "+WHITESPACE+VOLUME_TABLE+";";
        String DELETE_FLAVOR_TABLE = "DROP TABLE IF EXISTS "+WHITESPACE+FLAVOR_TABLE+";";
        try {
            sqLiteDatabase.execSQL(DELETE_TENANT_TABLE);
            sqLiteDatabase.execSQL(DELETE_INSTANCE_TABLE);
            sqLiteDatabase.execSQL(DELETE_IMAGE_TABLE);
            sqLiteDatabase.execSQL(DELETE_VOLUME_TABLE);
            sqLiteDatabase.execSQL(DELETE_FLAVOR_TABLE);
        }
        catch (SQLException ex)
        {
           //Log.d(TAG,"--------TABLE DOES NOT EXIST____");
        }
        sqLiteDatabase.close();
    }
    public void createAllTables(int i){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        try {
            createInstanceTable(sqLiteDatabase);
            if(i !=1) {
                createImageTable(sqLiteDatabase);
                createFlavorTable(sqLiteDatabase);
            }
            createTenantTable(sqLiteDatabase);
            createVolumeTable(sqLiteDatabase);
        }
        catch (SQLException ex)
        {
           //Log.d(TAG,"--------TABLE DOES NOT EXIST____");
        }
        sqLiteDatabase.close();
    }

    public List<Instance>getAllInstances() {
        List<Instance> instanceList = new ArrayList<Instance>();
        String query = "Select * from" + WHITESPACE + INSTANCE_TABLE;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor=null;
        try{
            cursor = database.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst() != false) {
            do {
                Instance instance = new Instance();
                instance.setInstanceID(cursor.getString(0));
                instance.setInstanceName(cursor.getString(1));
                instance.setInstanceImage(cursor.getString(2));
                instance.setInstanceIPAddr(cursor.getString(3));
                instance.setInstancePowerState(cursor.getString(4));
                instance.setInstanceSize(cursor.getString(5));
                instance.setInstanceRAM(cursor.getString(6));
                instance.setTenantID(cursor.getString(7));
                instance.setInstanceRunningTime(cursor.getString(8));
                instance.setInstanceVCU(cursor.getString(9));
                instance.setInstanceURL(cursor.getString(10));
                instance.setInstanceFlavor(cursor.getString(11));
                instanceList.add(instance);
               //Log.d(TAG, "------instance-rows---------");
               //Log.d(TAG, instance.getInstanceIPAddr());
               //Log.d(TAG, instance.getInstanceName());
               //Log.d(TAG, instance.getInstanceImage());
               //Log.d(TAG, "------instance-rows-end--------");
            } while (cursor.moveToNext());
        }
    }catch (SQLiteException exception){
            createInstanceTable(database);
        }
        cursor.close();
        database.close();
        return instanceList;
    }
    public List<Image>getAllImages(){
        List<Image>imageList=new ArrayList<Image>();
        String query = "Select * from"+WHITESPACE+IMAGE_TABLE;
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        if (cursor != null && cursor.moveToFirst()!=false){
            do {
                Image image = new Image();
                image.setImageID(cursor.getString(0));
                image.setImageName(cursor.getString(1));
                image.setImageType(cursor.getString(2));
                image.setImageState(cursor.getString(3));
                image.setImageSize(cursor.getString(4));
                imageList.add(image);
               //Log.d(TAG, "------image-rows---------");
               //Log.d(TAG, image.getImageName());
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return imageList;
    }
    public List<Volume>getAllVolumes(){
        List<Volume>volumeList=new ArrayList<Volume>();
        String query = "Select * from"+WHITESPACE+VOLUME_TABLE;
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        if (cursor != null && cursor.moveToFirst()!=false){

            do {
                Volume volume = new Volume();
                volume.setVolumeID(cursor.getString(0));
                volume.setVolumeName(cursor.getString(1));
                volume.setVolumeSize(cursor.getString(2));
                volume.setVolumeStatus(cursor.getString(3));
                volume.setVolumeType(cursor.getString(4));
                volumeList.add(volume);
               //Log.d(TAG, "------volume-rows---------");
               //Log.d(TAG,volume.getVolumeName());
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return volumeList;
    }
    public List<Flavor>getAllFlavors(){
        List<Flavor>FlavorList=new ArrayList<Flavor>();

       //Log.d(TAG, "-----all-flavor-rows---------");
        String query = "Select * from"+WHITESPACE+FLAVOR_TABLE;
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        if (cursor != null && cursor.moveToFirst()!=false){
            do {
                Flavor flavor = new Flavor();
                flavor.setFlavorID(cursor.getString(0));
                flavor.setFlavorName(cursor.getString(1));
                flavor.setFlavorSize(cursor.getString(2));
                flavor.setFlavorDisk(cursor.getString(3));
                flavor.setFlavorURL(cursor.getString(4));
                FlavorList.add(flavor);
               //Log.d(TAG, "------flavor-rows---------");
               //Log.d(TAG,flavor.getFlavorName());
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return FlavorList;
    }
    public List<Tenant>getAllTenantDetails(){
        List<Tenant>TenantList=new ArrayList<Tenant>();

       //Log.d(TAG, "-----all-tenant-rows---------");
        String query = "Select * from"+WHITESPACE+TENANT_TABLE;
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        if (cursor != null && cursor.moveToFirst()!=false){
            do {
                Tenant tenant = new Tenant();
                tenant.setTenantId(cursor.getString(0));
                tenant.setTenantName(cursor.getString(1));
                tenant.setTenantAvailabilityZone(cursor.getString(2));
                tenant.setTotal_ram(cursor.getString(3));
                tenant.setTotal_vcpus(cursor.getString(4));
                tenant.setTotal_instances(cursor.getString(5));
                TenantList.add(tenant);
               //Log.d(TAG, "------tenant-rows---------");
               //Log.d(TAG,tenant.getTenantName());
            }while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return TenantList;
    }

}
