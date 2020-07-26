package com.moapp.meet;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.moapp.meet.model.BoardListItem;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class BoardDetailActivity extends AppCompatActivity {
    private BoardListItem m_arr;
    private Activity m_activity;

    String Time1;
    Calendar calendar;

    private static final String TAG = "db 저장";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView mcontent, mtitle, mlocation, mtime;
    private Button ok;
    String boardId, title;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = user.getUid();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarddetail);
        mtitle = (TextView) findViewById((R.id.Title));
        mcontent = (TextView) findViewById((R.id.Content));
        mlocation = (TextView) findViewById((R.id.location));
        mtime = (TextView) findViewById((R.id.time));
        m_arr = new BoardListItem();
        ok = (Button) findViewById(R.id.btn_ok);
        setting();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //기본타이틀 안보여줌
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        System.out.println(m_arr.getId() + m_arr.getTitle() + m_arr.getDetail());
        ok.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {


                calendar= Calendar.getInstance();


                String year = Time1.substring(0, 4);
                //System.out.println(year);
                String month = Time1.substring(5, 7);
                // System.out.println(month);
                String day = Time1.substring(8, 10);
                // System.out.println(day);
                String hour = Time1.substring(11, 13);
                // System.out.println(hour);
                String miniute = Time1.substring(14, 16);
                // System.out.println(miniute);

                int year1 = Integer.parseInt(year);
                System.out.println(year1);
                int month1 = Integer.parseInt(month);
                System.out.println(month1);
                int hour1 = Integer.parseInt(hour);
                System.out.println(hour1);
                int day1 = Integer.parseInt(day);
                System.out.println(day1);
                int miniute1 = Integer.parseInt(miniute);
                System.out.println(miniute1);


                switch (month1) {
                    case 1:
                        calendar.set(Calendar.MONTH, calendar.JANUARY);
                        break;
                    case 2:
                        calendar.set(Calendar.MONTH, calendar.FEBRUARY);
                        break;
                    case 3:
                        calendar.set(Calendar.MONTH, calendar.MARCH);
                        break;
                    case 4:
                        calendar.set(Calendar.MONTH, calendar.NOVEMBER);
                        break;
                    case 5:
                        calendar.set(Calendar.MONTH, calendar.MAY);
                        break;
                    case 6:
                        calendar.set(Calendar.MONTH, calendar.JUNE);
                        break;
                    case 7:
                        calendar.set(Calendar.MONTH, calendar.JULY);
                        break;
                    case 8:
                        calendar.set(Calendar.MONTH, calendar.AUGUST);
                        break;
                    case 9:
                        calendar.set(Calendar.MONTH, calendar.SEPTEMBER);
                        break;
                    case 10:
                        calendar.set(Calendar.MONTH, calendar.OCTOBER);
                        break;
                    case 11:
                        calendar.set(Calendar.MONTH, calendar.NOVEMBER);
                        break;
                    case 12:
                        calendar.set(Calendar.MONTH, calendar.DECEMBER);
                        break;
                }


                calendar.set(Calendar.YEAR, year1);
                calendar.set(Calendar.DATE, day1);
                calendar.set(Calendar.HOUR_OF_DAY, hour1);
                calendar.set(Calendar.MINUTE, miniute1);
                calendar.set(Calendar.SECOND, 0);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Toast.makeText(getApplicationContext(), "알람 설정 : " + Time1, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), com.moapp.meet.AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


                System.out.println(format.format(calendar.getTime()));
                System.out.println(format.format(calendar.getTime()));
                System.out.println(format.format(calendar.getTime()));
                System.out.println(format.format(calendar.getTime()));
                System.out.println(format.format(calendar.getTime()));
                System.out.println(format.format(calendar.getTime()));
                System.out.println(format.format(calendar.getTime()));


                // 참여하기 누를 때 Notification 울리기
                // 참여하기 누를 때 Notification 울리기
                // 참여하기 누를 때 Notification 울리기
                // 참여하기 누를 때 Notification 울리기
                // 참여하기 누를 때 Notification 울리기

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    String channelID = "channel_01";
                    String channelName = "MyChannel01";

                    NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                    builder = new NotificationCompat.Builder(getApplicationContext(), channelID);

                } else {
                    builder = new NotificationCompat.Builder(getApplicationContext(), null);
                }

                builder.setSmallIcon(R.raw.talk);



                //null 지우기 -- 모임에 참가해주세요
                //토스트 메세지 = 모임시간이 다 되었습니다.

                builder.setContentTitle("Join a Room");
                builder.setContentText("-" + m_arr.getTitle() + "-모임에 입장");
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.raw.talk);
                builder.setLargeIcon(bm);


                Intent intent1 = new Intent(getApplicationContext(), CheckChatListActivity.class);
                PendingIntent pendingIntent1 = PendingIntent.getActivity(getApplicationContext(), 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent1);
                builder.setAutoCancel(true);

                Uri soundUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_NOTIFICATION);
                soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.click);
                builder.setSound(soundUri);

                builder.setVibrate(new long[]{0, 500, 0, 0});

                Notification notification = builder.build();

                notificationManager.notify(1, notification);


                Toast.makeText(getApplicationContext(), "모임에 참여 하셨습니다", Toast.LENGTH_SHORT).show();

                // 참여하기 누를 때 Notification 울리기
                // 참여하기 누를 때 Notification 울리기
                // 참여하기 누를 때 Notification 울리기
                // 참여하기 누를 때 Notification 울리기
                // 참여하기 누를 때 Notification 울리기


                Map<String, Object> chatid = new HashMap<>();
                chatid.put("chatid", boardId);
                chatid.put("chatname", title);
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

    private void setting() {
        Bundle b = getIntent().getExtras();
        m_arr.setTitle(b.getString("TITLE"));
        m_arr.setDate(b.getLong("DATE"));
        m_arr.setWriter(b.getString("WRITER"));
        m_arr.setDetail(b.getString("DETAIL"));
        m_arr.setId(b.getString("ID"));
        m_arr.setLocation(b.getString("LOCATION"));
        m_arr.setTime(b.getString("TIME"));

        Time1 = b.getString("TIME");

        boardId = m_arr.getId();
        title = m_arr.getTitle();
        mtitle.setText(m_arr.getTitle());
        mcontent.setText(m_arr.getDetail());
        mlocation.setText(m_arr.getLocation());
        mtime.setText(m_arr.getTime());


    }


}
