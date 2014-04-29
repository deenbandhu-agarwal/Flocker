package example.flockers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class Responses {

	protected static final String TAG = "Responses";
	Activity activity;
	Context context;
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<JSONObject>> listDataChild;
    
   
	public Responses(Activity activity,Context context)
	{
		this.activity = activity;
		this.context = context;		
		listDataHeader = new ArrayList<String>();
   	    listDataChild = new HashMap<String, List<JSONObject>>();
	}
	Response.Listener<JSONObject> participateListener = new Response.Listener<JSONObject>(){
		@Override
		   public void onResponse(JSONObject response)
		   {
				SendIntent s1 = new SendIntent(context);
				s1.login();
		   }
	};
	Response.Listener<JSONObject> launchListener = new Response.Listener<JSONObject>() {
		@Override
		   public void onResponse(JSONObject response)
		   {
				SendIntent s1 = new SendIntent(context);
				s1.login();
		   }
	};
	Response.Listener<JSONArray> myEventsOrganisedListlistener = new Response.Listener<JSONArray>(){
		@Override
		   public void onResponse(JSONArray response)
		   {
			try{
		    	expListView = (ExpandableListView) activity.findViewById(R.id.listview);
		    	listDataHeader.add("Organized By Me");
		        listDataHeader.add("Participated By Me");
		        List<JSONObject> organized = new ArrayList<JSONObject>();
		        
		    	if(response.length()==0){
		    		JSONObject ename= new JSONObject();
			        ename.put("ename","No Organized Events");
		    		organized.add(ename);
		    	}
		    	else{
		    		for(int i=0;i<response.length();i++){
						JSONObject actor = response.getJSONObject(i);
				        // Adding child data
				        organized.add(actor);
		    		}
		    	}
		        listDataChild.put(listDataHeader.get(0),organized); // Header, Child data
		        }
		    catch(Exception e){
				  e.printStackTrace();
			  }
		   }
	};
	
	Response.Listener<JSONArray> myEventsParticipatedListlistener = new Response.Listener<JSONArray>(){
		@Override
		   public void onResponse(JSONArray response)
		   {
			try{
				List<JSONObject> participated = new ArrayList<JSONObject>();
				if(response.length() == 0){
					JSONObject participatedo = new JSONObject();
					participatedo.put("ename", "No Participated Events");
					participated.add(participatedo);					  
				  }
			    else{
					 for(int i=0;i<response.length();i++){
				     JSONObject actor = response.getJSONObject(i);
					 // Adding child data
					 participated.add(actor);
					 }
			    }
				listDataChild.put(listDataHeader.get(1), participated);
				listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);
				expListView.setAdapter(listAdapter);
				expListView.setOnItemClickListener(new MyEventsOrganisedSelectedListener(activity,context));
			    }
			catch(Exception e){
				Log.e(TAG, "myEventsParticipatedListlistener failed");
			}
			
		   }
	};
	
	Response.Listener<JSONObject> eventDetailShowListener = new Response.Listener<JSONObject>(){
		@Override
		public void onResponse(JSONObject response) {
		    Log.e(TAG, "show event details");
			// TODO Auto-generated method stub
			try {
				if(response.has("status"))
				{
					if(response.get("status").toString().compareTo("Failure")==0) {
						Toast.makeText(context, "No Details Associated With this Event", Toast.LENGTH_LONG).show();
					}
				}
				else 
				{
    				String ename = response.get("ename").toString();
    				String date=response.get("date").toString();
    				String time=response.get("time").toString();
    				String place=response.get("place").toString();
    				String fees=response.get("fees").toString();
    				String prize=response.get("prize").toString();
    				String description=response.get("description").toString();
    				int id = response.getInt("id");
    				
    				SendIntent obj=new SendIntent(context);
    				obj.eventDetail(ename,date,time,place,fees,prize,description, id);
			    }
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	};
			
	Response.Listener<JSONObject> MyeventShow = new Response.Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
			try{
				if(response.getString("status").compareTo("Failure")==0){
					Toast.makeText(context, "No Events In Your Account", Toast.LENGTH_LONG).show();
				}
				else{
					String ename = response.getString("ename");
					String date=response.getString("date");
					String time=response.getString("time");
					String place=response.getString("place");
					String fees=response.getString("fees");
					String prize=response.getString("prize");
					String description=response.getString("description");
					int id=response.getInt("id");
					SendIntent obj=new SendIntent(context);
					obj.eventDetail(ename,date,time,place,fees,prize,description,id);
			    }
			}
		    catch(Exception e){
		    	e.printStackTrace();
			}
		   }
	};
	
	Response.Listener<JSONArray> eventShowlistener = new Response.Listener<JSONArray>() {
		@Override
		public void onResponse(JSONArray response) {
			if(response.length()==0){
            	Toast.makeText(context, "No Matches Found,Create Your Own Event", Toast.LENGTH_LONG).show();
            	Button CreateEvent=(Button)activity.findViewById(R.id.CreateEvent);
            	CreateEvent.setVisibility(View.VISIBLE);
            }
            else{
                //String eventsArray[];
                //JSONArray activities=response.getJSONArray("activity");
                //eventsArray = new String[response.length()];
                ArrayList<JSONObject> items = new ArrayList<JSONObject>();
                Button CreateEvent=(Button)activity.findViewById(R.id.CreateEvent);
                CreateEvent.setVisibility(View.VISIBLE);
                ListView eventlist = (ListView) activity.findViewById(R.id.eventlist);

                for (int i =0; i < response.length(); i++){
                    JSONObject obj2 = null;
                    try {
                        obj2 = response.getJSONObject(i);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    items.add(obj2);
                }
                
                TextView updateString=(TextView) activity.findViewById(R.id.txt1);
                updateString.setText("I would love to "+Variables.verb+" "+Variables.selectedActivity);
                
                MyJsonArrayAdapter adapter = new MyJsonArrayAdapter(activity, items); 
                eventlist.setAdapter(adapter);
                ((ListView) activity.findViewById(R.id.verblist)).setVisibility(View.GONE);
                ((ListView) activity.findViewById(R.id.activitylist)).setVisibility(View.GONE);
                ((ListView) activity.findViewById(R.id.eventlist)).setVisibility(View.VISIBLE);
                eventlist.setOnItemClickListener(new EventSelectedListener(activity,context));
            }
		}

	};
	
	Response.Listener<JSONArray> activityShowlistener = new Response.Listener<JSONArray>() {
		@Override
		public void onResponse(JSONArray response) {
			// TODO Auto-generated method stub
			try{
				  if(response.length() == 0){
					  Toast.makeText(context, "No Matches Found ", Toast.LENGTH_LONG).show();
				  }
				  else{
				  String activitiesArray[];
				  activitiesArray = new String[response.length()];
				  ListView activitylist = (ListView) activity.findViewById(R.id.activitylist);
				  for (int i =0; i<response.length(); i++){
				  JSONObject obj2 = response.getJSONObject(i);
			      activitiesArray[i] = obj2.get("activity").toString();
				  }
				  TextView updateString=(TextView) activity.findViewById(R.id.txt1);
				  updateString.setText("I Would Like To "+Variables.verb);
				  MyArrayAdapter adapter = new MyArrayAdapter(activity,activitiesArray); 
				  activitylist.setAdapter(adapter);          
				  ((ListView) activity.findViewById(R.id.verblist)).setVisibility(View.GONE);
				  ((ListView) activity.findViewById(R.id.activitylist)).setVisibility(View.VISIBLE);
				  ((ListView) activity.findViewById(R.id.eventlist)).setVisibility(View.GONE);
				  activitylist.setOnItemClickListener(new ActivitySelectedListener(activity,context));
				}
			}
			catch(JSONException pe){	
				Toast.makeText(context, "No Matches Found with exception", Toast.LENGTH_LONG).show();
				pe.printStackTrace();
			}
		}
	};
	
	Response.Listener<JSONArray> verbShowlistener = new Response.Listener<JSONArray>() {
	    @Override
	    public void onResponse(JSONArray response)
	    {
	    	  String verbs[];
	    	  ListView verblist;
	          try{
			      int l=response.length();
				  verblist = (ListView) activity.findViewById(R.id.verblist);
				   verbs = new String[l];
				  for(int i=0;i<l;i++){
					  JSONObject actor = response.getJSONObject(i);
					  verbs[i] = actor.getString("verb");
				  }
				  MyArrayAdapter adapter = new MyArrayAdapter(activity,verbs); 
				  verblist.setAdapter(adapter);
				  ((ListView) activity.findViewById(R.id.verblist)).setVisibility(View.VISIBLE);
				  ((ListView) activity.findViewById(R.id.activitylist)).setVisibility(View.GONE);
				  ((ListView) activity.findViewById(R.id.eventlist)).setVisibility(View.GONE);
				  verblist.setOnItemClickListener(new VerbSelectedListener(activity,context));
	          }
			  catch(Exception e){
				  e.printStackTrace();
			  }
	    }
	};
	
	Response.ErrorListener verbShowErrorlistener = new Response.ErrorListener() {
        @Override
		public void onErrorResponse(VolleyError error) {
		}
	};
	
	Response.ErrorListener eventDetailShowErrorlistener = new Response.ErrorListener() {
        @Override
		public void onErrorResponse(VolleyError error) {
        	Toast.makeText(context, "No Details associated", Toast.LENGTH_LONG).show();
		}
	};
	
	Response.ErrorListener MyeventShowErrorListener = new Response.ErrorListener() {
        @Override
		public void onErrorResponse(VolleyError error) {
		}
	};
	
	Response.ErrorListener activityShowErrorlistener = new Response.ErrorListener() {
        @Override
		public void onErrorResponse(VolleyError error) {
        	Toast.makeText(context, "No Matches Found", Toast.LENGTH_LONG).show();
		}
	};
	
	Response.ErrorListener eventShowErrorlistener = new Response.ErrorListener() {
        @Override
		public void onErrorResponse(VolleyError error) {
		}
	};
	Response.ErrorListener participateErrorlistener = new Response.ErrorListener() {
        @Override
		public void onErrorResponse(VolleyError error) {
		}
	};
	Response.ErrorListener myEventsOrganisedListErrorlistener = new Response.ErrorListener() {
        @Override
		public void onErrorResponse(VolleyError error) {
        	Toast.makeText(context, "Response not coming", Toast.LENGTH_LONG).show();
			
		}
	};
	Response.ErrorListener myEventsParticipatedListErrorlistener = new Response.ErrorListener() {
        @Override
		public void onErrorResponse(VolleyError error) {
		}
	};
}
