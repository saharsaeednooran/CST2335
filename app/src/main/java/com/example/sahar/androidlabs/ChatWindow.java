package com.example.sahar.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.database.Cursor;

import java.util.ArrayList;

public class ChatWindow extends Activity {


    protected static final String ACTIVITY_NAME = "ChatWindow";
    Button sendButton;
    EditText editText;
    ListView chatView;
    ArrayList<String> chatList = new ArrayList<>();
    ChatAdapter messageAdapter;
    ChatDatabaseHelper chatDatabaseHelper;
    SQLiteDatabase chatDB;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        editText = (EditText) findViewById(R.id.editText);
        sendButton = (Button) findViewById(R.id.sendButton);
        chatView = (ListView) findViewById(R.id.chatView);

        messageAdapter = new ChatAdapter(this);
        chatView.setAdapter(messageAdapter);


        chatDatabaseHelper = new ChatDatabaseHelper(this);
        chatDB = chatDatabaseHelper.getWritableDatabase();
        final ContentValues cValues = new ContentValues();


        cursor = chatDB.query(ChatDatabaseHelper.TABLE_NAME, new String[]{ChatDatabaseHelper.COL_ID, ChatDatabaseHelper.COL_MESSAGE}, null, null, null, null, null);
        int colIndex = cursor.getColumnIndex(ChatDatabaseHelper.COL_MESSAGE);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String message = cursor.getString(colIndex);
            chatList.add(message);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + message);
            cursor.moveToNext();
        }

        Log.i(ACTIVITY_NAME, "Cursor's column count=" + cursor.getColumnCount());
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, cursor.getColumnName(i));
        }
        //https://github.com/AshBrandyne/CST2335/blob/master/app/src/main/java/com/algonquin/ash_lee/lab1/MessageListActivity.java

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=editText.getText().toString();
                chatList.add(message);
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()

                //inserts the new message into the database
                ContentValues cValues = new ContentValues();
                cValues.put(ChatDatabaseHelper.COL_MESSAGE, message);
                chatDB.insert(ChatDatabaseHelper.TABLE_NAME,"null",cValues);

                editText.setText("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        chatDB.close();
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        LayoutInflater inflater;

        public ChatAdapter(Context context) {

            super(context, 0);
            inflater = ChatWindow.this.getLayoutInflater();
        }

        public int getCount() {
            return chatList.size();
        }

        public String getItem(int position) {
            return chatList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View result = null;
            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return result;

        }


    }


}
