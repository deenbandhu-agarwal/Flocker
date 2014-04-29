package example.flockers;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class expandablelistbuttonlistener implements OnClickListener{
	RequestQueue queue;
	Context context;
	JSONObject event;
	expandablelistbuttonlistener(JSONObject event,Context context){
		this.event = event;
		this.context =context;
	}
	@Override
	public void onClick(View v){
		Button testButton = (Button) v.findViewById(R.id.explistbutton);
    	String url = null;	
    	try{
    			 url = "http://10.0.2.2:9292/users/events/participant?event_id="+event.getString("id");
    		}
    		catch(JSONException e){
    			
    		}
    			BackendSync.getInstance(context).addRequest(Request.Method.DELETE,url ,null,
              new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // TODO Auto-generated method stub
                	Toast.makeText(context, "Successfully Done", Toast.LENGTH_LONG).show();
                }
              },new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                }
              });
	}

}
