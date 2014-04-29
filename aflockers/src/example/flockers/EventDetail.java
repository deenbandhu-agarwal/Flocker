package example.flockers;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

public class EventDetail extends Activity{
	String message[] ;
	TextView ename,date,time,place,fees,prize,description;
	Activity activity= this;
	Context context=this;
	public RequestQueue queue;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		message = new String[8];
		Intent intent = getIntent();
		message = intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);
		setContentView(R.layout.event_detail);
		
		ename=(TextView) findViewById(R.id.eventName);
		ename.setText(message[0]);
		date=(TextView) findViewById(R.id.Date);
		date.setText(message[1]);
		time=(TextView) findViewById(R.id.Time);
		time.setText(message[2]);
		place=(TextView) findViewById(R.id.Place);
		place.setText(message[3]);
		fees=(TextView) findViewById(R.id.Fees);
		fees.setText(message[4]);
		prize=(TextView) findViewById(R.id.Prize);
		prize.setText(message[5]);
		description=(TextView) findViewById(R.id.Description);
		description.setText(message[6]);
		
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void participate(View view){
		Responses response = new Responses(activity,context);
		String url = "http://10.0.2.2:9292/users/events/participant";
		JSONObject obj = new JSONObject();
		try{
			obj.put("event", message[7]);
		}
		catch(JSONException e){
			
		}
		BackendSync.getInstance(this).addRequest(Request.Method.POST, url ,obj,response.participateListener,response.participateErrorlistener);
	}
	
}