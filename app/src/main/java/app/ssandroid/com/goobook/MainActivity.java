package app.ssandroid.com.goobook;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //the URL having the json data
    private static final String JSON_URL = "https://www.googleapis.com/books/v1/volumes?filter=free-ebooks&q=a";

    // to store shared preferences for read or unread book
    public SharedPreferences sharedPreferences;
    //private SharedPreferences.Editor editor;

    //listView object
    public ListView listView;

    // reload button
    ImageButton mImageButtonInternet;

    //the hero list where we will store all the hero objects after parsing json
    List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // get default shared preferences if any
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //initializing listView and hero list
        listView = findViewById(R.id.listView);
        mImageButtonInternet = findViewById(R.id.imageButtonInternet);
        bookList = new ArrayList<>();

        //this method will fetch and parse the data only if internet is available
        checkJSON();
    }


    public void checkJSON(){
        if(!isInternetAvailable()) {
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
            mImageButtonInternet.setVisibility(View.VISIBLE);
            mImageButtonInternet.setClickable(true);
        }
        else{
            // load JSON data only if the device is connected to internet and can ping to google
            mImageButtonInternet.setVisibility(View.GONE);
            mImageButtonInternet.setClickable(false);
            loadBookList();
        }

        mImageButtonInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkJSON();
            }
        });
    }


    public boolean isInternetAvailable() {
        final String command = "ping -c 1 google.com";
        try {
            return Runtime.getRuntime().exec(command).waitFor() == 0;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void loadBookList() {
        //getting the progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //making the progressbar visible
        progressBar.setVisibility(View.VISIBLE);

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        progressBar.setVisibility(View.INVISIBLE);


                        try {
                            //getting the whole json object from the response
                            JSONObject jsonObject = new JSONObject(response);

                            //so here we are getting that json array
                            JSONArray itemsArray = jsonObject.getJSONArray("items");


                            // now looping through all the elements of the json array
                            for (int i = 0; i < itemsArray.length(); i++) {
                                bookList.add(new Book(itemsArray.getJSONObject(i),sharedPreferences));
                            }

                            // creating custom adapter object
                            final ListViewAdapter adapter = new ListViewAdapter(bookList, R.layout.list_items, getApplicationContext());

                            // setting the adapter to listView
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

}