package com.moapp.meet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

   // FirebaseAuth mAuth;
    private View navheadermain;
    private String tname,tid,tnickname;;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = user.getUid();

    //자동로그인
    //자동로그인
    //자동로그인
    //자동로그인

    SharedPreferences auto;
    SharedPreferences.Editor autoLogin;
    SharedPreferences.Editor editor;



    //자동로그인
    //자동로그인
    //자동로그인
    //자동로그인


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_main);
/////toolbar 설정
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //기본타이틀 안보여줌
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navheadermain = navigationView.getHeaderView(0); //nav_header_main 레이아웃 가져오기

        final TextView txtNick = (TextView)navheadermain.findViewById(R.id.textNick);
        final TextView txtId = (TextView)navheadermain.findViewById(R.id.textId);
        final TextView txtMajor = (TextView)navheadermain.findViewById(R.id.textMajor);
        final TextView txtPhone = (TextView)navheadermain.findViewById(R.id.textPhone);

        db.collection("users").document(user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //닉네임 받아오기
                                tname=document.getString("UserName");
                                tnickname = document.getString("NickName");
                                tid=document.getString("Email");
                                txtNick.setText(tnickname);
                                txtId.setText(tid);
                                txtMajor.setText(tname);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        // sports
        ImageButton btn_sports = findViewById(R.id.btn_sports);

        btn_sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,BoardListActivity.class);
                intent.putExtra("category","sports");
                startActivity(intent);
            }
        });

        // study
        ImageButton btn_study = findViewById(R.id.btn_study);

        btn_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,BoardListActivity.class);
                intent.putExtra("category","study");
                startActivity(intent);
            }
        });

        // language
        ImageButton btn_language = findViewById(R.id.btn_language);

        btn_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,BoardListActivity.class);
                intent.putExtra("category","language");
                startActivity(intent);
            }
        });

        // friend
        ImageButton btn_friend = findViewById(R.id.btn_friend);

        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,BoardListActivity.class);
                intent.putExtra("category","friend");
                startActivity(intent);
            }
        });

        // book
        ImageButton btn_book = findViewById(R.id.btn_book);

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,BoardListActivity.class);
                intent.putExtra("category","book");
                startActivity(intent);
            }
        });

        // etc
        ImageButton btn_etc = findViewById(R.id.btn_etc);

        btn_etc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,BoardListActivity.class);
                intent.putExtra("category","etc");
                startActivity(intent);
            }
        });

        // viewpager
        ViewPager viewPager = findViewById(R.id.viewPager);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

        // ViewPager와  FragmentAdapter 연결
        viewPager.setAdapter(fragmentAdapter);

        // Fragment로 넘길 Image Resource
        ArrayList<Integer> listImage = new ArrayList<>();
        listImage.add(R.drawable.image_meet);
        listImage.add(R.drawable.img_meet2);
        listImage.add(R.drawable.img_meet3);

//        // FragmentAdapter에 Fragment 추가, Image 개수만큼 추가
//        for (int i = 0; i < 4; i++) {
//            ImageFragment imageFragment = new ImageFragment();
//            fragmentAdapter.addItem(imageFragment);
//        }
//
//        fragmentAdapter.notifyDataSetChanged();


        viewPager.setClipToPadding(false);
        int dpValue = 8;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin/2);
        // FragmentAdapter에 Fragment 추가, Image 개수만큼 추가
        for (int i = 0; i < listImage.size(); i++) {
            ImageFragment imageFragment = new ImageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("imgRes", listImage.get(i));
            imageFragment.setArguments(bundle);
            fragmentAdapter.addItem(imageFragment);
        }
        fragmentAdapter.notifyDataSetChanged();
    }
    class FragmentAdapter extends FragmentPagerAdapter {

        // ViewPager에 들어갈 Fragment들을 담을 리스트
        private ArrayList<Fragment> fragments = new ArrayList<>();

        // 필수 생성자
        FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        // List에 Fragment를 담을 함수
        void addItem(Fragment fragment) {
            fragments.add(fragment);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void sendEmail(String email) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "< meet > 문의하기");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "앱 버전(AppVersion):" + "\n기기명(Device):\n안드로이드OS (Android OS):\n내용(Content):\n");
            emailIntent.setType("plain/Text");startActivity(Intent.createChooser(emailIntent, "어떤 이메일을 사용하시겠습니까?"));
        }catch(Exception e) {
            e.printStackTrace();
        }

    }
    //네비게이션드로어 이벤트
    @Override
    public boolean onNavigationItemSelected(MenuItem item) { //menu select

        int id = item.getItemId();

        if (id == R.id.check_chat) {
            startActivity(new Intent(getApplicationContext(), CheckChatListActivity.class));
        } else if (id == R.id.check_map) {
            startActivity(new Intent(getApplicationContext(), MapActivity.class));
        } else if(id == R.id.ask){
            sendEmail("bomin2641@gmail.com");
        }
        else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            auto = getSharedPreferences("auto", MODE_PRIVATE);
            editor = auto.edit();
            //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지운다
            editor.clear();
            editor.commit();
            startActivity(intent);
            finish();
            Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

        }
        //네비게이션 드로어 닫기 : 왼쪽
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
