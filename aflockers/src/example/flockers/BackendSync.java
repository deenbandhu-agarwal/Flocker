package example.flockers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
//import android.webkit.CookieManager;

public class BackendSync {
    private static BackendSync mInstance;
    private final static String LOGTAG = BackendSync.class.getSimpleName();
    
    private static String SESSION_TOKEN_KEY = "sessionToken";
    public static final String SERVICE_ADDRESS = "http://10.0.2.2:9292";
    // private final static String AUTH_URI = "/auth";
    private Context mContext;
    private RequestQueue mRequestQueue;

    class RetryPolicy extends DefaultRetryPolicy {
        @Override
        public void retry(VolleyError error) throws VolleyError {
            super.retry(error);
            if(error instanceof com.android.volley.AuthFailureError)
                authenticate();
        }

        private void authenticate() throws VolleyError {
            SendIntent obj=new SendIntent(mContext);
            obj.signin();
        }
    }

    private class AuthorizedJsonObjectRequest extends JsonObjectRequest {
        private static final String SET_COOKIE_KEY = "Set-Cookie";
        
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            String token = sp.getString(SESSION_TOKEN_KEY, null);
            HashMap<String, String> hs = new HashMap<String, String>();
            hs.put("Cookie", token);
            return hs;
        }
        
        @Override
        // http://stackoverflow.com/questions/16680701/using-cookies-with-android-volley-library
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            String cookie = response.headers.get(SET_COOKIE_KEY);
            if(cookie!=null)
            sp.edit().putString(SESSION_TOKEN_KEY, cookie).commit();
            return super.parseNetworkResponse(response);
        }

        public AuthorizedJsonObjectRequest(int method, String url,
                JSONObject jsonRequest, Listener<JSONObject> listener,
                ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
        }
    }

    private class AuthorizedJsonArrayRequest extends JsonArrayRequest {
        private static final String SET_COOKIE_KEY = "Set-Cookie";
        
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            String token = sp.getString(SESSION_TOKEN_KEY, null);
            HashMap<String, String> hs = new HashMap<String, String>();
            hs.put("Cookie", token);
            return hs;
        }
        
        @Override
        // http://stackoverflow.com/questions/16680701/using-cookies-with-android-volley-library
        protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            String cookie = response.headers.get(SET_COOKIE_KEY);
            if(cookie!=null)
            sp.edit().putString(SESSION_TOKEN_KEY, cookie).commit();
            return super.parseNetworkResponse(response);
        }

        public AuthorizedJsonArrayRequest(String url, Listener<JSONArray> listener,
                ErrorListener errorListener) {
            super(url,listener, errorListener);
        }
    }
    private BackendSync(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext);
    }

    public static BackendSync getInstance(Context context){
        if(mInstance == null){      
            mInstance = new BackendSync(context);
        }
        return mInstance;
    }

    public void invalidateSession() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        sp.edit().remove(BackendSync.SESSION_TOKEN_KEY).commit();
    }

    private String locateResource(String uri) {
        if(uri == null)
            return null;
        if(uri.startsWith("http:"))
            return uri;
        String a = getBaseUri();
        if(a == null)
            return null;
        String auri = a + uri;
        URI u;
        try {
            u = new URI(auri);
        } catch (URISyntaxException e) {
            return a + uri;
        }
        return u.normalize().toString();
    }

    public String getBaseUri() {
        return SERVICE_ADDRESS;
    }
    public void addArrayRequest(final String uri, Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        final String url = locateResource(uri);
        if(listener == null)
            listener = new Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) { }
            };
        if(errorListener == null)
            errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(LOGTAG, "error syncing " + url + ": " + error);
                }
            };
        // JsonObjectRequest r = new JsonObjectRequest(method, url, jsonRequest, listener, errorListener);
        AuthorizedJsonArrayRequest r = new AuthorizedJsonArrayRequest(url,listener, errorListener);
        r.setRetryPolicy(new RetryPolicy());
       // String v = null;
     /*   switch(method) {
        case Request.Method.GET:
            v = "getting";
            break;
        case Request.Method.PUT:
            v = "putting";
            r.setShouldCache(false);
            break;
        case Request.Method.POST:
            v = "posting";
            r.setShouldCache(false);
            break;
        case Request.Method.DELETE:
            v = "deleting";
            r.setShouldCache(false);
            break;
        }*/
        //Log.i(LOGTAG, v);
        mRequestQueue.add(r);
    }
    public void addRequest(int method, final String uri, JSONObject jsonRequest, Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        final String url = locateResource(uri);
        if(listener == null)
            listener = new Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) { }
            };
        if(errorListener == null)
            errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(LOGTAG, "error syncing " + url + ": " + error);
                }
            };
        // JsonObjectRequest r = new JsonObjectRequest(method, url, jsonRequest, listener, errorListener);
        AuthorizedJsonObjectRequest r = new AuthorizedJsonObjectRequest(method, url, jsonRequest, listener, errorListener);
        r.setRetryPolicy(new RetryPolicy());
        String v = null;
        switch(method) {
        case Request.Method.GET:
            v = "getting";
            break;
        case Request.Method.PUT:
            v = "putting";
            r.setShouldCache(false);
            break;
        case Request.Method.POST:
            v = "posting";
            r.setShouldCache(false);
            break;
        case Request.Method.DELETE:
            v = "deleting";
            //r.setShouldCache(false);
            break;
        }
        Log.i(LOGTAG, v);
        mRequestQueue.add(r);
    }
        
}