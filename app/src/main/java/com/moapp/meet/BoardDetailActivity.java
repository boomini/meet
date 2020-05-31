package com.moapp.meet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.moapp.meet.model.BoardListItem;

import java.util.HashMap;
import java.util.Map;


public class BoardDetailActivity extends AppCompatActivity {
    private BoardListItem m_arr;
    private Activity m_activity;
    private static final String TAG = "db 저장";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView mcontent, mtitle;
    private Button ok;
    String boardId,title;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = user.getUid();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarddetail);
        mtitle = (TextView) findViewById((R.id.Title));
        mcontent = (TextView) findViewById((R.id.Content));
        m_arr = new BoardListItem();
        ok = (Button)findViewById(R.id.btn_ok);
        setting();
        System.out.println(m_arr.getId()+m_arr.getTitle()+m_arr.getDetail());
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> chatid = new HashMap<>();
                chatid.put("chatid", boardId);
                chatid.put("chatname",title);
                db.collection("users").document(user_id)
                        .collection("chatname").document(boardId)
                        .set(chatid)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        });
    }

    private void setting() {
        Bundle b = getIntent().getExtras();
        m_arr.setTitle(b.getString("TITLE"));
        m_arr.setDate(b.getLong("DATE"));
        m_arr.setWriter(b.getString("WRITER"));
        m_arr.setDetail(b.getString("DETAIL"));
        m_arr.setId(b.getString("ID"));
        boardId=m_arr.getId();
        title=m_arr.getTitle();
        mtitle.setText(m_arr.getTitle());
        mcontent.setText(m_arr.getDetail());


    }

}
