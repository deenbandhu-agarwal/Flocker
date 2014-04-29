package example.flockers;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class EventSelectedListener implements OnItemClickListener {
	private static final String TAG = "EventSelectedListener";
	public Activity activity;
	public Context context;
	public RequestQueue queue;
	String selected;
	
	public EventSelectedListener(Activity activity,Context context)
	{
		this.activity = activity;
		this.context = context;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos,long id) {
		Responses response = new Responses(activity,context);
		queue = Volley.newRequestQueue(context);
        JSONObject obj2;
        try {
            obj2 = (JSONObject) parent.getItemAtPosition(pos);
            Variables.selectedEvent = obj2.get("id").toString();
        } catch (JSONException e) {
            Log.e(TAG, "cant get the id of event");
        }
		String url = "http://10.0.2.2:9292/events/"+Variables.selectedEvent;
		BackendSync.getInstance(this.context).addRequest(Request.Method.GET,url,null, response.eventDetailShowListener,response.eventDetailShowErrorlistener);
	}
		
}