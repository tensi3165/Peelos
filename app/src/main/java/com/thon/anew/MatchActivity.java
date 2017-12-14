package com.thon.anew;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MatchActivity extends AppCompatActivity {

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> arr_roomList = new ArrayList<>();
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().getRoot();

    private String str_type_name;
    private String str_user_name;

    Map<String, Object> map = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("PEELOS");
        setContentView(R.layout.activity_match);

        str_type_name = getIntent().getExtras().get("type_name").toString();
        str_user_name = getIntent().getExtras().get("user_name").toString();

        map.put("nickname", str_user_name);
        map.put("type", str_type_name);
        map.put("bool", "false");
        map.put("channel", "test");
        reference.child("chat").child("wait").updateChildren(map);


        // 리스트뷰의 채팅방을 클릭했을 때 반응
        // 채팅방의 이름과 입장하는 유저의 이름을 전달
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator i = dataSnapshot.child("type").getChildren().iterator();

                while (i.hasNext()) {
                    String str_bool = (String) ((DataSnapshot) i.next()).getValue();
                    boolean ismatched = Boolean.parseBoolean(str_bool);
                    String channel = (String) ((DataSnapshot) i.next()).getValue();

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MatchActivity.this);

                    final EditText et = new EditText(MatchActivity.this);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(et);
                    alertDialogBuilder.setTitle(String.valueOf(ismatched));

                    // set dialog message
                    alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();

                    if(ismatched){
                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        intent.putExtra("room_name", channel);
                        intent.putExtra("user_name", str_user_name);
                        startActivity(intent);
                    }
                }

            }

            @Override public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
