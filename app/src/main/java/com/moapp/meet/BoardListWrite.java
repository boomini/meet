package com.moapp.meet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.moapp.meet.adapter.LocationAdapter;
import com.moapp.meet.api.ApiClient;
import com.moapp.meet.api.ApiInterface;
import com.moapp.meet.model.category_search.CategoryResult;
import com.moapp.meet.model.category_search.Document;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.moapp.meet.BusProvider.bus;


public class BoardListWrite extends AppCompatActivity implements View.OnClickListener {
    EditText mcontent, mnum, mtitle, mSearchEdit;
    String tcontent, ttitle, tname, tSearchEdit;
    private static final int REQUEST_CODE = 777; //이 값으로 호출하고 돌아올 때 받는 값을 호출한 화면이 받는다.
    int tcount;

    String Time;

    ArrayList<Document> documentArrayList = new ArrayList<>(); //지역명 검색 결과 리스트

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = user.getUid();
    LocationAdapter locationAdapter;
    Button write;

    RelativeLayout mLoaderLayout;
    RecyclerView recyclerView;
    String category;
    private double mSearchLng = -1;
    private double mSearchLat = -1;
    private String mSearchName;
    ViewGroup mMapViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardwrite);
        mtitle = (EditText) findViewById((R.id.InputTitle));
        mcontent = (EditText) findViewById(R.id.InputContent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button Alarmset = findViewById(R.id.alarm1);
        Alarmset.setOnClickListener(this);
        setSupportActionBar(toolbar);
        //기본타이틀 안보여줌
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        write = (Button) findViewById(R.id.Writebtn);
        write.setOnClickListener(this);
        Bundle b = getIntent().getExtras();
        category = b.getString("category");
        Log.d("BoardListWrite", category);
        mSearchEdit = findViewById(R.id.map_et_search);
        recyclerView = findViewById(R.id.map_recyclerview);
        // mMapViewContainer.addView(recyclerView);
        locationAdapter = new LocationAdapter(BoardListWrite.this, documentArrayList, getApplicationContext(), mSearchEdit, recyclerView, category);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false); //레이아웃매니저 생성
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL)); //아래구분선 세팅
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(locationAdapter);

        db.collection("users").document(user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //닉네임 받아오기
                                tname = document.getString("NickName");
                                tcount = Integer.valueOf(document.getString("BoardCount"));
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

        // editText 검색 텍스처이벤트
        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // 입력하기 전에
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 1) {
                    // if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {

                    documentArrayList.clear();
                    locationAdapter.clear();
                    locationAdapter.notifyDataSetChanged();
                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<CategoryResult> call = apiInterface.getSearchLocation(getString(R.string.restapi_key), charSequence.toString(), 15);
                    call.enqueue(new Callback<CategoryResult>() {
                        @Override
                        public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                for (Document document : response.body().getDocuments()) {
                                    locationAdapter.addItem(document);
                                }
                                locationAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<CategoryResult> call, @NotNull Throwable t) {

                        }
                    });
                    //}
                    //mLastClickTime = SystemClock.elapsedRealtime();
                } else {
                    if (charSequence.length() <= 0) {
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 입력이 끝났을 때

            }
        });

        mSearchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
        mSearchEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "검색리스트에서 장소를 선택해주세요", Toast.LENGTH_SHORT).show();
            }
        });

    } //onCreate() 종료

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        long curtime = System.currentTimeMillis();
        switch (v.getId()) {
            case R.id.Writebtn: // 글 작성



                ttitle = mtitle.getText().toString();
                tcontent = mcontent.getText().toString();
                tSearchEdit = mSearchEdit.getText().toString();
                if (ttitle.trim().length() == 0 || tcontent.trim().length() == 0 || tSearchEdit.trim().length() == 0) {
                    Toast.makeText(this, "제목과 내용은 빠짐없이 입력하세요!", Toast.LENGTH_SHORT).show();
                    Log.d("board", "공백 발생");
                    return;
                } else {
                    Map<String, Object> user = new HashMap<>();
                    user.put("ttitle", ttitle);
                    user.put("tname", tname);
                    user.put("tdate", curtime);
                    user.put("tcontent", tcontent);
                    user.put("tlocation", tSearchEdit);
                    user.put("time", Time);

                    // Add a new document with a generated ID
                    db.collection("board" + category).document(user_id + tcount)
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    db.collection("users").document(user_id).update("BoardCount", Integer.toString(++tcount));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });

                    Intent intent = new Intent(this, BoardListActivity.class);
                    intent.putExtra("category", category);
                    startActivity(intent);

                    finish();
                }
                break;
            case R.id.alarm1:
                Intent intent = new Intent(this, AlarmActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //값을 성공적으로 받으면 토스트 메세지 출력
        if (resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(), "수신 성공", Toast.LENGTH_SHORT).show();
            write.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getApplicationContext(), "수신 실패", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == REQUEST_CODE) {

            String time = data.getStringExtra("Time");
            Toast.makeText(getApplicationContext(), time, Toast.LENGTH_SHORT).show();

            this.Time = time;


        }
    }

}
