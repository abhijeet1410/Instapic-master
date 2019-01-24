package com.rsmapps.selfieall.utility;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkUtils {

    Context mContext;
    private ProgressDialog mProcessDialog;

    public NetworkUtils(Context context) {
        mContext = context;
        mProcessDialog = new ProgressDialog(mContext);
        mProcessDialog.setCancelable(false);
        mProcessDialog.setMessage("loading...");

    }

    public void showProgress(){
        if ( mProcessDialog != null) {
            mProcessDialog.show();
        }
    }

    public void hideProgress(){
        if (mProcessDialog != null && mProcessDialog.isShowing()) {
            mProcessDialog.dismiss();
        }
    }



    public void get(final String url, final ResponseCallBack callBack, final JSONObject payload, final Boolean showProgressDialog, final String callbackFlag) {

        if (!isInternetConnected(mContext)) {
            callBack.invoke("", callbackFlag, Constant.RESPONSE_ERROR);
            return;
        }

        if (showProgressDialog && mProcessDialog != null) {
            mProcessDialog.show();
        }

        try {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    url, payload,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response URL", url);
                            Log.v("Response", response.toString());
                            try {
                                callBack.invoke(response.toString(), callbackFlag, Constant.RESPONSE_SUCCESS);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (mProcessDialog != null && mProcessDialog.isShowing()) {
                                    mProcessDialog.dismiss();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    try {

                        if (mProcessDialog != null && mProcessDialog.isShowing()) {
                            mProcessDialog.dismiss();
                        }

                        callBack.invoke(error.getMessage(), callbackFlag, Constant.RESPONSE_ERROR);

                    } catch (Exception e) {
                        e.printStackTrace();
                        callBack.invoke(error.getMessage(), callbackFlag, Constant.RESPONSE_ERROR);
                    } finally {
                        if (mProcessDialog != null && mProcessDialog.isShowing()) {
                            mProcessDialog.dismiss();
                        }
                    }
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Basic MTox");
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjReq.setRetryPolicy(policy);
            VollyRequestQueue.getInstance(mContext).addToRequestQueue(jsonObjReq);
        } catch (Exception e) {

            if (mProcessDialog != null && mProcessDialog.isShowing()) {
                mProcessDialog.dismiss();
            }

            e.printStackTrace();
        }
    }


    public void postForm(final String url, final ResponseCallBack callBack, final Map<String, String> formParams, final Boolean showProgressDialog, final String callbackFlag) {
        try {
            Log.d("Params","Url: " + url + " : " + formParams.toString());
            if (!isInternetConnected(mContext)) {
                callBack.invoke("", callbackFlag, Constant.RESPONSE_ERROR);
                return;
            }

            if (showProgressDialog && mProcessDialog != null) {
                mProcessDialog.show();
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.v("Response URL", url);
                    Log.v("Response", response.toString());
                    try {
                        callBack.invoke(response.toString(), callbackFlag, Constant.RESPONSE_SUCCESS);
                    } catch (Exception e) {
                        System.out.print(e.toString());
                    } finally {
                        if (mProcessDialog != null && mProcessDialog.isShowing()) {
                            mProcessDialog.dismiss();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        if (mProcessDialog != null && mProcessDialog.isShowing()) {
                            mProcessDialog.dismiss();
                        }

                        callBack.invoke(error.getMessage(), callbackFlag, Constant.RESPONSE_ERROR);

                    } catch (Exception e) {
                        e.printStackTrace();
                        callBack.invoke(error.getMessage(), callbackFlag, Constant.RESPONSE_ERROR);
                    }
                }
            }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Map<String, String> params = new HashMap<String, String>();
                    return formParams;

                }
            };

            int socketTimeout = 7000;//10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            VollyRequestQueue.getInstance(mContext).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            if (mProcessDialog != null && mProcessDialog.isShowing()) {
                mProcessDialog.dismiss();
            }
            e.printStackTrace();
        }
    }


    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }



    public static void internetFailedDialog(Context context) {
        try {
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

            alertDialog.setTitle("Info");
            alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        } catch (Exception e) {
        }
    }

}
