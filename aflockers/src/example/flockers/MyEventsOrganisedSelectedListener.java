package example.flockers;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyEventsOrganisedSelectedListener implements OnItemClickListener{
	public Activity activity;
	public Context context;
	RequestQueue queue;
	String selected;
	public MyEventsOrganisedSelectedListener(Activity activity,Context context)
	{
		this.activity = activity;
		this.context = context;
	}
	@Override
     public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		Responses response = new Responses(activity,context);
		queue = Volley.newRequestQueue(context);
		selected = parent.getItemAtPosition(position).toString();
		String url = "http://10.0.2.2:9292/events/"+Variables.selectedEvent;
		BackendSync.getInstance(this.context).addRequest(Request.Method.GET,url ,null
				,response.MyeventShow,response.MyeventShowErrorListener);	
    }
}
