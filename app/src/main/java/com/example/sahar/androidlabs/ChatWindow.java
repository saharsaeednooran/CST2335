package com.example.sahar.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {


    Button sendButton;
    EditText editText;
    ListView chatView;
    ArrayList<String> chatList=new ArrayList<>();
    ChatAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        editText=(EditText) findViewById(R.id.editText);
        sendButton=(Button) findViewById(R.id.sendButton);
        chatView=(ListView) findViewById(R.id.chatView);

        messageAdapter=new ChatAdapter( this );
        chatView.setAdapter (messageAdapter);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatList.add(editText.getText().toString());
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
                editText.setText("");
            }
        });



    }

    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context context) {
            super(context, 0);
        }
        public int getCount(){
            return chatList.size();
        }

        public String getItem(int position){
            return chatList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(   getItem(position)  ); // get the string at position
            return result;

        }



    }


}
