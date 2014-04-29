package example.flockers;

import org.json.JSONException;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends Activity {

	private static final String TAG = "LoginActivity";
    TextView unameText ;
	RequestQueue queue ;
	Context context=this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		queue = Volley.newRequestQueue(this);
	}
 
	@Override
	public void onRestart() {
		super.onRestart();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void login(View view){
		queue = Volley.newRequestQueue(this);
		//String uname,pass;
		//uname=((EditText) findViewById(R.id.username)).getText().toString();
		//pass=((EditText) findViewById(R.id.password)).getText().toString();
		String url = "http://10.0.2.2:9292/login";//?user_name="+uname+"&pass="+pass;
		JSONObject obj = new JSONObject();
		try{
			obj.put("user_name",((EditText) findViewById(R.id.username)).getText() );		
			obj.put("pass",((EditText) findViewById(R.id.password)).getText() );
		}
		catch(JSONException e){
            Log.e(TAG, "JSON Object put failed");
		}
		BackendSync.getInstance(this).addRequest(Request.Method.POST, url ,obj,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					try{
						if(response.has("error")){
							Toast toast = Toast.makeText(getApplicationContext(),"Invalid UserName and Password",Toast.LENGTH_SHORT);
							toast.show();	
						}
						else{
							SendIntent s1 =new SendIntent(context);
							s1.login();						
						}
					
					}
					catch(Exception e){
					} 
					}
			},new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
				    Log.e(TAG, "Login failed");
				}
			});
	}
	public void signup(View view){
		SendIntent s1=new SendIntent(context);
		s1.signup();
	}
}