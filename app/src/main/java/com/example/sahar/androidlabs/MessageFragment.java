package com.example.sahar.androidlabs;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Sahar on 2017-12-03.
 */

public class MessageFragment extends Fragment {
    String messageId;
    Button deleteBtn;
    TextView messageTextView, idTextView;
    View myView;
    ChatDatabaseHelper chatDatabase;
    ChatWindow chatWindow;

    public MessageFragment() {
    }

    public static MessageFragment newInstance(ChatWindow chatWindow) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.chatWindow = chatWindow;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.fragment_message, container, false);

        deleteBtn=(Button) myView.findViewById(R.id.deleteMsgButton);
        messageTextView=(TextView) myView.findViewById(R.id.messageTextView);
        idTextView=(TextView)myView.findViewById(R.id.idTextView);

        Bundle bundle = this.getArguments();
        String myValue   = bundle.getString("message");
        messageId = bundle.getString("messageId");




        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chatWindow==null) {//on phone
                    Intent intent = new Intent(getActivity(), ChatWindow.class);
                    getActivity().setResult(Integer.parseInt(messageId), intent);
                    getActivity().finish();
                }
                else//on tablet
                {
                    chatWindow.deleteTabletMsg(Integer.parseInt(messageId));
                }

            }
        });

        idTextView.setText("");
        idTextView.setText(messageId);
        messageTextView.setText("");
        messageTextView.setText(myValue);

        return myView;
    }










}
