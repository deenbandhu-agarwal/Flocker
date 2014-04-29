package example.flockers;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends Activity{
	public RequestQueue queue;
	String message;
	Activity activty= this;
	Context context=this;
	public final static String EXTRA_MESSAGE = "example.flockers.MESSAGE";
	Responses response = new Responses(activty,context);
	TabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tabHost=(TabHost)findViewById(R.id.tabHost);
		tabHost.setup();
		
		TabSpec spec1=tabHost.newTabSpec("Tab 1");
		spec1.setContent(R.id.tab1);
		spec1.setIndicator("Search");
	
		TabSpec spec2=tabHost.newTabSpec("Tab 2");
		spec2.setContent(R.id.tab2);
		spec2.setIndicator("My Events");
	
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		
		ListView verblist = (ListView) findViewById(R.id.verblist);
		verblist.setVisibility(View.GONE);
		ListView activitylist = (ListView) findViewById(R.id.activitylist);
		activitylist.setVisibility(View.GONE);
		ListView eventlist = (ListView) findViewById(R.id.eventlist);
		eventlist.setVisibility(View.GONE);
		
		// Search Tab 
		queue = Volley.newRequestQueue(this);
		String url = "http://10.0.2.2:9292/verbs";
		BackendSync.getInstance(this.context).addArrayRequest(url,response.verbShowlistener,response.verbShowErrorlistener );
	
		//My Events Tab
	
		String URL= "http://10.0.2.2:9292/users/events";
		BackendSync.getInstance(this.context).addArrayRequest(URL,response.myEventsOrganisedListlistener
			,response.myEventsOrganisedListErrorlistener);
	
	    String Url= "http://10.0.2.2:9292/users/events/participant";
	    BackendSync.getInstance(this.context).addArrayRequest(Url,response.myEventsParticipatedListlistener
		,response.myEventsParticipatedListErrorlistener);	
	    }
	@Override
	public void onRestart() {
		super.onRestart();
		tabHost=(TabHost)findViewById(R.id.tabHost);
		tabHost.setCurrentTab(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	
	public void createEvent(View view)
	{
		SendIntent s1 = new SendIntent(this);
		s1.eventClick(view);
	}
	
}