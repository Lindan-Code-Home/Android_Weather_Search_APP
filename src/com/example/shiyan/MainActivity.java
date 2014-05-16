package com.example.shiyan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

//import com.example.samplecode.MainActivity.DownloadWebpageTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;







import com.example.shiyan.R;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.widget.TextView;
import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.WebDialog;
//import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
//import com.weatherseach.MainActivity;

//import android.content.DialogInterface.OnClickListener;
import android.app.Dialog;

public class MainActivity extends Activity implements OnClickListener  {
	private int fbInDialog;
	
	final Context context = this;
	
	private String[] high = new String[5];
	private String[] day = new String[5];
	private String[] low = new String[5];
	private String[] weatherText = new String[10];
	private String contentAsString = "";
	
	private String region;
	private String country;
	private String city;
	private EditText ZipLocation;
	private String type;
	
	private JSONObject json;
	private TextView City;
	private TextView Location;
	private TextView current;
	private TextView forecast;
	private String input;
	
	private String unit;

	private ImageView imgArea;
	private TextView currentWeatherText;
	private TextView currentTempText;
	private TextView title;
	private TableLayout t1;
	private String currentWeather;
	private String currentTemp;
	private String searchUnit;
	private String imageurl;
	private static Bitmap bmp = null;
	private String link;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
		    PackageInfo info = getPackageManager().getPackageInfo("com.example.shiyan",
		                                PackageManager.GET_SIGNATURES);
		    for (Signature signature : info.signatures) {
		        MessageDigest md = MessageDigest.getInstance("SHA");
		        md.update(signature.toByteArray());
		        Log.i("Digest: ", Base64.encodeToString(md.digest(), 0));
		    }
		} catch (NameNotFoundException e) {
		    Log.e("Test", e.getMessage());
		} catch (NoSuchAlgorithmException e) {
		    Log.e("Test", e.getMessage());
		}

		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button search_btn = (Button) findViewById(R.id.search_btn);
		City = (TextView)findViewById(R.id.city);
		imgArea = (ImageView)findViewById(R.id.img);
		currentWeatherText= (TextView)findViewById(R.id.weathertext);
		currentTempText= (TextView)findViewById(R.id.temperature);
		title = (TextView)findViewById(R.id.forecast);
		searchUnit="F";
		
		Location = (TextView)findViewById(R.id.location);
		current = (TextView)findViewById(R.id.sharecurrent);
	    current.setOnClickListener(this);
	    forecast = (TextView)findViewById(R.id.shareforecast);
	    forecast.setOnClickListener(this);
	     t1 = (TableLayout) findViewById(R.id.table);
	    }
		
		
		
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	public void sendMessage(View view) {
		title.setText(""); 
		currentWeatherText.setText("");
		currentTempText.setText("");
		City.setText(" ");
		Location.setText("");
		current.setText("");
		   forecast.setText("");
		   t1.removeAllViews();
		   imgArea.setImageBitmap(null);
		ZipLocation =(EditText) findViewById(R.id.edit_message);
		 input = ZipLocation.getText().toString();
		if(ZipLocation.getText().toString().trim().equals("")){
			Toast.makeText(MainActivity.this, "please enter city name or zip code",
					Toast.LENGTH_LONG).show();///****
			return;
		}//end if
		else if (isInteger(input))
		{    if(input.length() != 5){
			   Toast.makeText(MainActivity.this, "Must five digit Zip",
					Toast.LENGTH_LONG).show();
			 return;
		    }
		 else{
			 type = "Zip";
		 }
			
		}
		
		else {
			String CurrentString=input;
			String[] separated = CurrentString.split(",");
			
			if((separated.length==2)||(separated.length==3))
			type="city";
			else{Toast.makeText(MainActivity.this, "Please enter a valid location,must include state and country seperated my comma",
					Toast.LENGTH_LONG).show();
			 return;}	
		}
		
		String urlstr="";
		
		DownloadWebpageTask mTask = new DownloadWebpageTask();
		System.out.println("Here is coming");
		try {
			mTask.execute(urlstr).get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		if(contentAsString=="")
		{City.setText("No Results");
		return;
		}
		
		
		
		//json = new JSONObject(xml);
		 //json = json.getJSONObject("weather");
		JSONObject res;
		JSONObject jsonObj;
		//json = json.getJSONObject("weather");
		
		
		try {
			
			System.out.println(contentAsString);
			res = new JSONObject(contentAsString);
			json = new JSONObject(contentAsString);
			 json = json.getJSONObject("weather");
			jsonObj= (JSONObject) res.getJSONObject("weather");
			JSONArray results = (JSONArray) jsonObj.getJSONArray("forecast");
		
			JSONObject locitem = (JSONObject) jsonObj.getJSONObject("location");
			city= locitem.get("city").toString();
			region= locitem.get("region").toString();
			country=locitem.get("country").toString();
			
			JSONObject Units = (JSONObject) jsonObj.getJSONObject("units");
			unit=Units.get("temperature").toString();
			
			
			JSONObject Conditions = (JSONObject) jsonObj.getJSONObject("condition");
			currentWeather=Conditions.get("text").toString();
			currentTemp=Conditions.get("temp").toString();	
			
			JSONArray forecastArray = json.getJSONArray("forecast"); 
			 for(int i = 0; i < forecastArray.length(); i++){
				 JSONObject forecastTable = forecastArray.getJSONObject(i);
				 day[i] = forecastTable.getString("day");
				 low[i] = forecastTable.getString("low");
				 high[i] = forecastTable.getString("high");
				 weatherText[i] = forecastTable.getString("text");
			 }
			 imageurl = json.getString("img").toString();
			 link=json.getString("link").toString();
			 
			 ImageDisplayTask  imageTask  =new ImageDisplayTask();
				 //imageurl = "http://l.yimg.com/a/i/us/we/52/26.gif";

				try {
					imageTask.execute(imageurl).get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				imgArea.setImageBitmap(bmp);
				
			 
			
			}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(city=="") city="N/A";
		City.setText(city);
		if(region.length()==0) region="N/A";
		if(country.length()==0) country="N/A";
		Location.setText(region + "," + country);
		/**************************showImage**************************************/
		 
		 imgArea.setImageBitmap(bmp);
		 /******************************show temperature**************************************/
		// JSONObject condition = json.getJSONObject("condition");
		// String currentWeather = condition.getString("text");
		 //String currentTemp = condition.getString("temp");
		 currentWeatherText.setText(currentWeather);
		 currentTempText.setText(currentTemp + "°" + unit); 
		
		 /******************************show table title**************************************/
		 title.setText("Forecast"); 
		
		
		

		//TableLayout tl = new TableLayout(MainActivity.this);
//Post the forecast
		//1.post the header
        TableRow tr1 = new TableRow(MainActivity.this);

		TextView row11 = new TextView(MainActivity.this);
		row11.setText("day  ");
		row11.setTextColor(0xFF000000);
		row11.setBackgroundResource(R.drawable.shape_cell);
		row11.setPadding(5, 5, 5, 5);
		row11.setWidth(100);
		
	    TextView row12 = new TextView(MainActivity.this);
		row12.setText("weather  ");
		row12.setTextColor(0xFF000000);
		row12.setBackgroundResource(R.drawable.shape_cell);
		row12.setPadding(5, 5, 5, 5);
		row12.setWidth(200);
		
		TextView row13 = new TextView(MainActivity.this);
		row13.setText("high  ");
		row13.setTextColor(0xFF000000);
		row13.setBackgroundResource(R.drawable.shape_cell);
		row13.setPadding(5, 5, 5, 5);
		row13.setWidth(100);
		
		TextView row14 = new TextView(MainActivity.this);
		row14.setText("low  ");
		row14.setTextColor(0xFF000000);
		row14.setBackgroundResource(R.drawable.shape_cell);
		row14.setPadding(5, 5, 5, 5);
		row14.setWidth(100);
		
		tr1.addView(row11);
		tr1.addView(row12);
		tr1.addView(row13);
		tr1.addView(row14);
		t1.addView(tr1);
		
		//2 Post the table content
		for(int i=0;i<5;i++){

			TableRow tr = new TableRow(MainActivity.this);
			TextView content1 = new TextView(MainActivity.this);
			content1.setText(day[i] );
			content1.setTextColor(0xFF000000);
			content1.setBackgroundResource(R.drawable.content_cell);
			content1.setPadding(5, 5, 5, 5);
			content1.setWidth(100);
			
			
			
		    TextView content2 = new TextView(MainActivity.this);
		    content2.setText(weatherText[i]);
		    content2.setTextColor(0xFF000000);
			content2.setBackgroundResource(R.drawable.content_cell);
			content2.setPadding(5, 5, 5, 5);
			content2.setWidth(200);
			
			TextView content3 = new TextView(MainActivity.this);
			content3.setText(high[i]+"°"+unit);
			content3.setTextColor(0xFF000000);
			content3.setBackgroundResource(R.drawable.content_cell);
			content3.setPadding(5, 5, 5, 5);
			content3.setWidth(100);
			
			TextView content4 = new TextView(MainActivity.this);
			content4.setText(low[i]+"°"+unit);
			content4.setTextColor(0xFF000000);
			content4.setBackgroundResource(R.drawable.content_cell);
			content4.setPadding(5, 5, 5, 5);
			content4.setWidth(100);

			tr.addView(content1);
			tr.addView(content2);
			tr.addView(content3);
			tr.addView(content4);
			t1.addView(tr);
		
			// ((ViewGroup)tr.getParent()).removeView(tr);
		}
		
		
		
		current.setText("Share Current Weather");
		   forecast.setText("Share Weather Forecast");
		
   }//end sendMessage
	
	public void onClick(View view){
		
		boolean click = ((TextView) view).isClickable();
		if(click)
		
		{if(view.getId()==R.id.sharecurrent)
		  {sharecurrent(view);
		  fbInDialog = R.id.sharecurrent;}
		else  if(view.getId()==R.id.shareforecast)
		  {shareforecast(view);
		  fbInDialog = R.id.shareforecast;}}
		
		
	
		
	}
	/*************************************share current weather to facebook*********************************************/
	public void sharecurrent(View view){
		
		fbAlert("Post Current Weather");
	}
	/*********************************************share forecast to facebook*********************************************/
	public void shareforecast(View view){
		
		fbAlert("Post Weather Forecast");
	}
		
	    //TextView textView = new TextView(this);
	    //textView.setTextSize(40);
	    //textView.setText(message);
	private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
	@Override
		protected String doInBackground(String... urls) {
			InputStream is = null;
			int len = 20;
			
			try {
		      ZipLocation =(EditText) findViewById(R.id.edit_message);
		      String ZiporLocation  = ZipLocation.getText().toString();

			   String searchtype="zipcode";
			   
			   
			   String temp="F";
			   temp=searchUnit;
			   String search_type=type;
			   
			   ZiporLocation = new String(ZiporLocation.getBytes("iso-8859-1"),"UTF-8");
			   ZiporLocation = java.net.URLEncoder.encode(ZiporLocation,"UTF-8");
			   String urlstr="http://cs-server.usc.edu:27604/examples/servlet/servletQ?locName="+ZiporLocation+"&locType="+search_type+"&tempUnit="+temp;
			   
			  
			   
				
				URL url = new URL(urlstr);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				
				conn.setConnectTimeout(15000 /* milliseconds */);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				// Starts the query
				conn.connect();
				int response = conn.getResponseCode();
				
				if(response!=200)
					{Log.d("error", "The response is: " + response);
					contentAsString="";
					return(contentAsString);
					}
				
				
				is = conn.getInputStream();
				
				
				Reader rd = null;
				rd = new InputStreamReader(is, "UTF-8");
				BufferedReader reader = new BufferedReader(rd);
				contentAsString =new String(reader.readLine());
				
				
				
				return contentAsString;

				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(MainActivity.this, "FAILURE 1",
						Toast.LENGTH_LONG).show();
				return "";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(MainActivity.this, "FAILURE 2",
						Toast.LENGTH_LONG).show();
				return "";
			} 
			catch (Exception ex) {
				ex.printStackTrace();
				Toast.makeText(MainActivity.this, "FAILURE 3",
						Toast.LENGTH_LONG).show();
				return "";
			 }
			finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
						Toast.makeText(MainActivity.this, "FAILURE 3",
								Toast.LENGTH_LONG).show();
					}
				} 
			}
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

			Log.d("error", "onPostExecute");
			//textView.setText(result);
			//setContentView(textView);
			//displayResult(result);
		}
	}
	
	
	
	
	private class ImageDisplayTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

           String urlen = imageurl;
			try {
				//getImage(urlen);
				URL url = new URL(urlen);  
				 bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
				 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return "";

		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

			Log.d("error", "onPostExecute");
			

		}
	}
	
	
	
	
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	public void publishFeedDialog() throws JSONException {
	    Bundle params = new Bundle();
	    JSONObject location = json.getJSONObject("location");
	    String region1=location.getString("region").toString();
	    String country1=location.getString("country").toString();
	    if(country1.length()==0) country1="N/A";
	    
	    if(region1.length()==0) region1="N/A";
	    String name = city + ", " + region + ", " + country;
	    String caption = "";
	    String descrp = "";
	    
	    
	    if(fbInDialog == R.id.sharecurrent){
	    	caption = "The current condition for " + city+ " is " + currentWeather + ".";
	    	descrp = "Temperature is " +currentWeather+ "°" + currentTemp + ".";
	    	
	    }
	    else if(fbInDialog == R.id.shareforecast){
	    	caption = "Weather Forecast for " + city ;
	    	
			String unit = json.getJSONObject("units").getString("temperature");
			for(int i = 0; i < 5; i++){
				
					descrp += day[i] + ": " + weatherText[i] + ", " + high[i] + "/" + low[i] + "°" + unit + "; "; 
				
				if(i<4) 
				descrp+=";" ;
			else
				descrp+=".";
				
			}
			imageurl = "http://cs-server.usc.edu:27604/examples/servlets/weather.jpg";
	    }
	    params.putString("name", name);
	    params.putString("caption", caption);
	    params.putString("description", descrp);
	    params.putString("link", link);
	    params.putString("picture", imageurl);
	    JSONObject property = new JSONObject();
	    JSONObject det = new JSONObject();
 	   	det.put("text", "here");
 	   	det.put("href", link);
 	   	property.put("Look at details ", det);
 	   	params.putString("properties", property.toString());

	    WebDialog feedDialog = (
	        new WebDialog.FeedDialogBuilder(MainActivity.this, Session.getActiveSession(), params))
	        .setOnCompleteListener(new OnCompleteListener() {

	            @Override
	            public void onComplete(Bundle values,
	                FacebookException error) {
	                if (error == null) {
	                    
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                        Toast.makeText(MainActivity.this,
	                            "Posted Successfully! PostID:"+postId,
	                            Toast.LENGTH_SHORT).show();
	                    } else {
	                        // User clicked the Cancel button
	                        Toast.makeText(MainActivity.this.getApplicationContext(), 
	                            "Not post", 
	                            Toast.LENGTH_SHORT).show();
	                    }
	                } 
	            }

	        })
	        .build();
	    feedDialog.show();
	}
	public void fbAlert(String s){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		
		builder.setTitle("Post to facebook");

		
		
		builder.setCancelable(false).setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
		   	public void onClick(DialogInterface dialog,int id) {
		   		 dialog.cancel();
		   	}
		})
		.setNegativeButton(s,new DialogInterface.OnClickListener() {
		   	public void onClick(DialogInterface dialog,int id) {
		   		// start Facebook Login
		   		Session.openActiveSession(MainActivity.this, true, new Session.StatusCallback() {
		   	    // callback when session changes state
				@Override
		   	    public void call(Session session, SessionState state, Exception exception) {
		   	    	if (session.isOpened()) {
				   	    try {
							publishFeedDialog();
						} 
		   	    		catch (JSONException e) {
						// TODO Auto-generated catch block
		   	    			Toast.makeText(MainActivity.this, "publishDialogure Error!",
									Toast.LENGTH_LONG).show();
						}
		   	    	}
		   	    	else{Toast.makeText(MainActivity.this, "Cannot log in!",
							Toast.LENGTH_LONG).show();}
		   	    }
		   	  });
		   	}
		});
		
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
	
	
	public void onRadioButtonClicked(View view) {
		
		if(((RadioButton) view).isChecked())
		{if(view.getId()==R.id.radioC)
			searchUnit = "C";
		else if(view.getId()==R.id.radioF)
			searchUnit = "F";}
		
	}

	
}
