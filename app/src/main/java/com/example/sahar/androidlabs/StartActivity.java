package com.example.sahar.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.R.attr.duration;
import static java.security.AccessController.getContext;

public class StartActivity extends Activity {

    protected static final String ACTIVITY_NAME = "StartActivity";
    Button button,startChatButton,weatherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListItemsActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        startChatButton=(Button) findViewById(R.id.startChatButton);
        startChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
               Intent intent = new Intent(getApplicationContext(), ChatWindow.class);
               startActivity(intent);
            }
        });

        weatherButton=(Button) findViewById(R.id.weatherButton);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Weather Forecast");
                Intent intent = new Intent(getApplicationContext(), WeatherForecast.class);
                startActivity(intent);
            }
        });




    }

    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if (requestCode == 10 && responseCode == Activity.RESULT_OK) {
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
            String messagePassed = data.getStringExtra("Response");
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(StartActivity.this, messagePassed, duration);
            toast.show(); //display your message box
        }


    }

    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
