package example.flockers;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class SignUp extends Activity{
	Button SignUp;
	EditText username,password,confirmpassword;
	public RequestQueue queue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);
		queue = Volley.newRequestQueue(this);

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void Signup(View view){
		// TODO Auto-generated method stubqueue = Volley.newRequestQueue(this);
		String url = "http://10.0.2.2:9292/signup";
		JSONObject obj = new JSONObject();
		try{
			username=(EditText) findViewById(R.id.uname);
			obj.put("user_name",(""+username.getText()));
			password=(EditText) findViewById(R.id.pass);
			confirmpassword=(EditText) findViewById(R.id.cpass);
			if(password.getText().toString().compareTo(confirmpassword.getText().toString())==0){
				obj.put("pass",(""+password.getText()));
				obj.put("cpass",(""+confirmpassword.getText()));
			}
			else{
				Toast toast = Toast.makeText(this,"Password does not match",Toast.LENGTH_SHORT);
				toast.show();
				return;
			}
		}
		catch(JSONException e){
		}
		BackendSync.getInstance(this).addRequest(Request.Method.POST, url ,obj,
				new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					       try{
					        if(response.get("success").toString().compareTo("Success")==0){
							    SendIntent s1=new SendIntent(getApplicationContext());
								s1.redirectlogin();
					        }
					        else{
					        	Toast toast = Toast.makeText(getApplicationContext(),"Account Cannot be Created",Toast.LENGTH_SHORT);
								toast.show();
					        }
					       }
					       catch(Exception e){
					    	   
					       }
				}
			},new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					Toast toast = Toast.makeText(getApplicationContext(),"Response not coming",Toast.LENGTH_SHORT);
					toast.show();
				}
			});		
	}
}
