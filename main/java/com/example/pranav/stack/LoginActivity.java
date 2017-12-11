package com.example.pranav.stack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dd.processbutton.iml.ActionProcessButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pranav on 11/08/15.
 */
public class LoginActivity extends Activity {

    private static final String TAG=LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private int requestCode=100;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editHost;
    private ProgressDialog progressDialog;
    private Session_helper current_session;
    ActionProcessButton btnSignIn;
    OpenStackDatabase openStackDatabase;
    App_urls app_urls;
    Instance instance;

    @Override
    public void onCreate(Bundle savedInstance) {
        app_urls=new App_urls();
        instance =new Instance();
        super.onCreate(savedInstance);
        setContentView(R.layout.login_layout);

        editUsername = (EditText) findViewById(R.id.username_editText);
        editPassword = (EditText) findViewById(R.id.password_editText);
        editHost = (EditText) findViewById(R.id.host_editText);
        //btnLogin = (Button) findViewById(R.id.btnLogin);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        btnSignIn.setProgress(0);
        current_session = new Session_helper(getApplicationContext());
        openStackDatabase=new OpenStackDatabase(this);
        openStackDatabase.getWritableDatabase();
        openStackDatabase.dropAllTables();
        openStackDatabase.close();
        if (current_session.getUserName() != null)
            editUsername.setText(current_session.getUserName());
        if (current_session.getIpAddr() != null)
            editHost.setText(current_session.getIpAddr());

        if (current_session.isLogged()) {
            //So user is already logged in
            /*
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            */
        }

        //If login button is clicked
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"----------click-2-----");
                String username=editUsername.getText().toString();
                App_urls.Username=editUsername.getText().toString();
                String password=editPassword.getText().toString();
                String hostIP=editHost.getText().toString();
                App_urls.HOST=editHost.getText().toString().trim();
                App_urls.HOST_URL="http://".concat(App_urls.HOST);
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivityForResult(intent, requestCode);
                App_urls.URL_LOGIN = App_urls.HOST_URL.concat(":5000/v2.0/tokens");
                Log.d(TAG,App_urls.HOST);
                if(username.trim().length()>0 && password.trim().length()>0 && hostIP.trim().length()>0)
                {
                    current_session.setLogin(false,username,hostIP);
                    start_login(App_urls.TenantName, username, password);

                } else {
                    btnSignIn.setProgress(-1);
                    Toast.makeText(getApplicationContext(), "Please enter valid Username/Password/Server IP.", Toast.LENGTH_LONG).show();
                }
            }
        });

    } //Verify the login creadentials

        private void start_login(final String tenantName,final String username, final String password)
        {
            Log.d(TAG, "-----checklogin------");
            Log.d(TAG,App_urls.URL_LOGIN);
            SendRequest(CreateLoginJSONObject(tenantName, username, password), App_urls.URL_LOGIN);
        }

    private void SendRequest(JSONObject jsonObject, String url)
    {
        btnSignIn.setProgress(1);
        JsonObjectRequest request = new JsonObjectRequest((jsonObject == null) ? Request.Method.GET:Request.Method.POST,url,jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        setLoginPOSTResponse(response);
                        Log.d(TAG, "Response---------");
                        Log.d(TAG, App_urls.loginJSON.toString());
                        btnSignIn.setProgress(100);
                        //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "error:");
                        Log.d(TAG,error.toString());
                        btnSignIn.setProgress(-1);
                        if(error instanceof TimeoutError || error instanceof NoConnectionError)
                        {
                            Toast.makeText(getApplicationContext(),"Please check you wifi connection!",Toast.LENGTH_LONG).show();
                        }
                        else if(error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Incorrect Username or Password.", Toast.LENGTH_LONG).show();
                        }
                        else
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        setRequestErrorResponse(error.toString());
                        hideDialog();
                    }
                }
        ){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers=new HashMap<String,String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        Volley.newRequestQueue(this).add(request);
    }


    private void setLoginPOSTResponse(JSONObject jsonObject){
        App_urls.loginJSON=jsonObject;
        if(App_urls.loginJSON.toString().equals("{}")){
            Log.d(TAG,App_urls.loginJSON.toString());
            return;
        } else {
            //Log.d(TAG, "-----token------");
            //Log.d(TAG, app_urls.getTokenID(App_urls.loginJSON));
            app_urls.getAllURLs(App_urls.loginJSON);
            //SendRequest(null, temp);

            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivityForResult(intent, requestCode);
            //finish();
            //Log.d(TAG,"-----tenant------");
            //Log.d(TAG, app_urls.getTenantID((App_urls.loginJSON)));

            return;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        btnSignIn.setProgress(0);
        if(resultCode != RESULT_OK){
            Log.d(TAG,"--------check if logout not pressed-----------");
            finish();
        }
        else
            Log.d(TAG,"--------check if logout is pressed-----------");
    }

    private void setRequestErrorResponse(String s){
    }
    private void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }
    private void hideDialog(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }

    }
    public JSONObject CreateLoginJSONObject(String tenantName, String username, String password) {
        JSONObject retJSON;
        retJSON = new JSONObject();
        try {
            JSONObject auth = new JSONObject();
            auth.put("tenantName", tenantName);

            JSONObject password_cred = new JSONObject();
            password_cred.put("username", username);
            password_cred.put("password", password);

            auth.put("passwordCredentials", password_cred);

            retJSON.put("auth", auth);
            //Log.d(TAG, "---------------start----------------\n");
            //Log.d(TAG, retJSON.toString(2));
            //Log.d(TAG, "----------------end----------------");

        } catch (JSONException ex) {
            Log.d(TAG, "Error Occurred while building JSON");
            ex.printStackTrace();
        }
        return retJSON;
    }

}




