package example.flockers;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class CreateEvent extends Activity{
	private static final String TAG = "CreateEventActivity";
	Button Create;
	TextView abc;
	EditText ename,date,time,place,fees,prize,description;
	public RequestQueue queue;
	String message[] = new String[2];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		message = intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);
		setContentView(R.layout.create_event);
		queue = Volley.newRequestQueue(this);

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void create(View view){
		// TODO Auto-generated method stubqueue = Volley.newRequestQueue(this);
		String url = "http://10.0.2.2:9292/users/events";
		JSONObject obj = new JSONObject();
		JSONObject event = new JSONObject();
		try{
			ename=(EditText) findViewById(R.id.ename);
			obj.put("ename",(""+ename.getText()));
			obj.put("verb",message[0]);
			abc = (TextView) findViewById(R.id.txt1);
			abc.setText(message[1]);
			obj.put("activity",message[1]);
			date=(EditText) findViewById(R.id.date);
			obj.put("date",(""+date.getText()));
			time=(EditText) findViewById(R.id.time);
			obj.put("time",(""+time.getText()));
			place=(EditText) findViewById(R.id.place);
			obj.put("place",(""+place.getText()));
			fees=(EditText) findViewById(R.id.fees);
			obj.put("fees",(""+fees.getText()));
			prize=(EditText) findViewById(R.id.prize);
			obj.put("prize",(""+prize.getText()));
			description=(EditText) findViewById(R.id.description);
			obj.put("description",(""+description.getText()));
			event.put("event",obj);
		}
		catch(JSONException e){
			Log.e(TAG, "create event - something is wrong with json");
		}
		BackendSync.getInstance(this).addRequest(Request.Method.POST, url ,event,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if(response.get("failure").toString().compareTo("Failure")==0){
                        Toast toast = Toast.makeText(getApplicationContext(),"Event Cannot be Created",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                catch(Exception e){
                    SendIntent s1=new SendIntent(getApplicationContext());
                    s1.redirectMyEvents();
                }
            }
        },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "create event - something is wrong with create event");
            }
        });
    }

}