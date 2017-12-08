package com.example.sahar.androidlabs;

import android.app.Activity;
import android.os.Bundle;

public class MessageDetails extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        String message   = getIntent().getStringExtra("message");
        String messageId = getIntent().getStringExtra("messageId");
        Bundle bundle = new Bundle();
        bundle.putString("message", message );
        bundle.putString("messageId", messageId );

        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().add(R.id.fragment_container, messageFragment).commit();
    }
}
