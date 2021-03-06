package com.keelim.study.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.keelim.study.R;
import com.keelim.study.activity_cities.AllcityActivity;
import com.keelim.study.activity_cities.BusanActivity;
import com.keelim.study.activity_cities.ChungbukActivity;
import com.keelim.study.activity_cities.ChungnamActivity;
import com.keelim.study.activity_cities.DaegeonActivity;
import com.keelim.study.activity_cities.DaeguActivity;
import com.keelim.study.activity_cities.GangwonActivity;
import com.keelim.study.activity_cities.GwangjuActivity;
import com.keelim.study.activity_cities.GyeonggiActivity;
import com.keelim.study.activity_cities.IncheonActivity;
import com.keelim.study.activity_cities.JejuActivity;
import com.keelim.study.activity_cities.JeonbukActivity;
import com.keelim.study.activity_cities.JeonnamActivity;
import com.keelim.study.activity_cities.KyungbukActivity;
import com.keelim.study.activity_cities.KyungnamActivity;
import com.keelim.study.activity_cities.SejongActivity;
import com.keelim.study.activity_cities.SeoulActivity;
import com.keelim.study.activity_cities.UlsanActivity;
import com.keelim.study.fragment.CityTabFragment;
import com.keelim.study.fragment.FreeBoardTabFragment;
import com.keelim.study.fragment.IndividualTabFragment;
import com.keelim.study.function.BackPressedFunction;
import com.keelim.study.interfaces.CallCityInterface;
import com.keelim.study.interfaces.MyMessageInterface;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements CityTabFragment.OnFragmentInteractionListener,
        IndividualTabFragment.OnFragmentInteractionListener,
        FreeBoardTabFragment.OnFragmentInteractionListener,
        CallCityInterface, MyMessageInterface {
    static final String TAG = "MainActivityTAG";
    //드로어,메뉴
    private DrawerLayout mDrawerLayout;
    private View drawerView;
    private Button mProfileMenuButton;
    private Button mQuestionMenuButton;
    private Button mLogoutButton;
    private CircleImageView mDrawerProfileCircleImageView;
    private TextView mDrawerNickNameTextView;

    //자신 프로필
    private String mNickName;
    private String mSex; //성별 남자 or 여자
    private String mEmail; //이메일
    private String mAge; //나이대
    private String mImage; //프로필사진

    //2번뒤로가기 클릭시 종료
    private BackPressedFunction mBackPressedFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadShared(); //프로필정보받아옴
        setDrawer(); //드로어 정보세팅

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("스터디모집"));
        tabLayout.addTab(tabLayout.newTab().setText("메세지"));
        tabLayout.addTab(tabLayout.newTab().setText("자유게시판"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new com.keelim.study.adapter.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(adapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mBackPressedFunction = new BackPressedFunction(this); //뒤로가기 2번시 종료 핸들러

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadShared();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void callCity(String city) {
        Intent intent;
        switch (city) {
            case "seoul":
                intent = new Intent(this, SeoulActivity.class);
                startActivity(intent);
                break;
            case "gyeonggi":
               intent = new Intent(this, GyeonggiActivity.class);
                startActivity(intent);
                break;
            case "incheon":
                intent = new Intent(this, IncheonActivity.class);
                startActivity(intent);
                break;
            case "gangwon":
                intent = new Intent(this, GangwonActivity.class);
                startActivity(intent);
                break;
            case "chungnam":
                intent = new Intent(this, ChungnamActivity.class);
                startActivity(intent);
                break;
            case "daegeon":
                intent = new Intent(this, DaegeonActivity.class);
                startActivity(intent);
                break;
            case "chungbuk":
                intent = new Intent(this, ChungbukActivity.class);
                startActivity(intent);
                break;
            case "sejong":
                intent = new Intent(this, SejongActivity.class);
                startActivity(intent);
                break;
            case "busan":
                intent = new Intent(this, BusanActivity.class);
                startActivity(intent);
                break;
            case "ulsan":
                intent = new Intent(this, UlsanActivity.class);
                startActivity(intent);
                break;
            case "daegu":
                intent = new Intent(this, DaeguActivity.class);
                startActivity(intent);
                break;
            case "kyungbuk":
                intent = new Intent(this, KyungbukActivity.class);
                startActivity(intent);
                break;
            case "kyungnam":
                intent = new Intent(this, KyungnamActivity.class);
                startActivity(intent);
                break;
            case "jeonnam":
                intent = new Intent(this, JeonnamActivity.class);
                startActivity(intent);
                break;
            case "gwangju":
                intent = new Intent(this, GwangjuActivity.class);
                startActivity(intent);
                break;
            case "jeonbuk":
                intent = new Intent(this, JeonbukActivity.class);
                startActivity(intent);
                break;
            case "jeju":
                intent = new Intent(this, JejuActivity.class);
                startActivity(intent);
                break;
            case "allcity":
                intent = new Intent(this, AllcityActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        mBackPressedFunction.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mDrawerLayout.openDrawer(drawerView);
        return true;
    }

    // 쉐어드값을 불러오는 메소드
    private void loadShared() {
        SharedPreferences pref = getSharedPreferences("profile", MODE_PRIVATE);
        mEmail = pref.getString("email", "");
        mNickName = pref.getString("nickName", "");
        mSex = pref.getString("sex", "");
        mAge = pref.getString("age", "");
        mImage = pref.getString("image", "");
    }

    public void setDrawer() {
        //드로어,메뉴
        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        drawerView = findViewById(R.id.drawer);
        mProfileMenuButton = findViewById(R.id.drawer_btn_profileSetting);
        mQuestionMenuButton = findViewById(R.id.drawer_btn_question);
        mLogoutButton = findViewById(R.id.drawer_btn_logout);
        mDrawerProfileCircleImageView = findViewById(R.id.drawer_civ_profileimage);
        mDrawerNickNameTextView = findViewById(R.id.drawer_tv_nickName);
        mDrawerNickNameTextView.setText(mNickName);
        if (mImage.equals("basic")) { //프로필사진이 없는경우
            Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/studdytogether.appspot.com/o/Basisc%2FbasicProfile.png?alt=media&token=dd0e0e17-a057-40a4-ae7f-364fa529e2a3").into(mDrawerProfileCircleImageView);
        } else {
            Glide.with(this).load(mImage).into(mDrawerProfileCircleImageView);
        }
        //드로어관련 클릭리스너
        mProfileMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                //이 플래그는 API 11 (허니콤)부터 사용이가능한데 그 이하버전은 0.2%수준이다.
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);
            }
        });
        mQuestionMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                try {
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"seungeon.jin2@gmail.com"});

                    intent.setType("text/html");
                    intent.setPackage("com.google.android.gm");
                    if (intent.resolveActivity(getPackageManager()) != null)
                        startActivity(intent);

                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"seungeon.jin2@gmail.com"});
                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
            }
        });
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                //이 플래그는 API 11 (허니콤)부터 사용이가능한데 그 이하버전은 0.2%수준이다.
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);
            }
        });
        mDrawerProfileCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhotoZoomActivity.class);
                intent.putExtra("zoomProfilePhoto", mImage);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        mDrawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View view, float v) {

        }

        @Override
        public void onDrawerOpened(@NonNull View view) {

        }

        @Override
        public void onDrawerClosed(@NonNull View view) {

        }

        @Override
        public void onDrawerStateChanged(int i) {

        }
    };

    @Override
    public void showMyMessage() {
        Intent intent = new Intent(getApplicationContext(), MyMessageActivity.class);
        startActivity(intent);
    }
}
