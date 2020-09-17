package com.example.qrbarcodescanner.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrbarcodescanner.R;
import com.example.qrbarcodescanner.adapter.AppsAdapter;
import com.example.qrbarcodescanner.model.App;

import java.util.ArrayList;
import java.util.List;

public class OtherAppsActivity extends AppCompatActivity implements AppsAdapter.OnAd {
    RecyclerView recyclerView;
    ArrayList<App> ListApp;
    ImageView back, notice;
    TextView textad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_other_apps);
        Init();

        ListApp = new ArrayList<>();

        ArrayList<Integer> Period = new ArrayList();
        ArrayList<Integer> Slide = new ArrayList();
        ArrayList<Integer> Speed = new ArrayList();
        ArrayList<Integer> Cliper = new ArrayList();

        //Period
        Period.add(R.mipmap.period_1);
        Period.add(R.mipmap.period_2);
        Period.add(R.mipmap.period_3);
        Period.add(R.mipmap.period_4);
        ListApp.add(new App(R.mipmap.ic_period, "Period and Ovulation Tracker", "com.heafit.periodtracker.ovulationtracker", "Remind you of your next period, ovulation day as well as your fertile time", Period));

        //Video
        Slide.add(R.mipmap.slide_1);
        Slide.add(R.mipmap.slide_2);
        Slide.add(R.mipmap.slide_3);
        Slide.add(R.mipmap.slide_4);
        ListApp.add(new App(R.mipmap.ic_slide_show, "Video Slideshow Editor", "com.vtool.photovideomaker.slideshow.videoeditor", "Slideshow: Photo Video Editor, Music Video.", Slide));

        //Speed
        Speed.add(R.mipmap.speed_1);
        Speed.add(R.mipmap.speed_2);
        Speed.add(R.mipmap.speed_3);
        Speed.add(R.mipmap.speed_4);
        ListApp.add(new App(R.mipmap.ic_speedtest, "Internet Speed test - Speed Test Wifi", "com.vtool.speedtest.speedcheck.internet", "Speed test app, check my interet speed quickly and accurately", Speed));

        //Cliper
        Cliper.add(R.mipmap.ic_cliper_1);
        Cliper.add(R.mipmap.ic_cliper_2);
        Cliper.add(R.mipmap.ic_cliper_3);
        ListApp.add(new App(R.mipmap.ic_cliper, "Hair Clipper Prank", "com.vtool.hairclippersimulated", "The trimmer simulator app is a entertainment app to have fug jokes with ", Cliper));

        AppsAdapter appsAdapter = new AppsAdapter(ListApp, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(appsAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textad.animate()
                        .alpha(0f)
                        .setDuration(8000)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                textad.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

    public void Init() {
        recyclerView = findViewById(R.id.recyclerviewotherapps);
        back = findViewById(R.id.back);
        notice = findViewById(R.id.baseline1);
        textad = findViewById(R.id.textad);
    }

    @Override
    public void OnAdListener(int position) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + ListApp.get(position).getUrl())));
    }

}