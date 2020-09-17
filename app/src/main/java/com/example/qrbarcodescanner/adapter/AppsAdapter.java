package com.example.qrbarcodescanner.adapter;


import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrbarcodescanner.R;
import com.example.qrbarcodescanner.model.App;

import java.util.ArrayList;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.View1> {

    ArrayList<App> ListApp;
    Context context;
    OnAd onAd;
    @NonNull
    @Override
    public View1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapps, parent, false);
        return new View1(view);
    }

    public AppsAdapter(ArrayList<App> listApp, Context context, OnAd onAd){
        this.ListApp = listApp;
        this.context = context;
        this.onAd = onAd;
    }
    @Override
    public void onBindViewHolder(@NonNull View1 holder, int position) {
        App app = ListApp.get(position);
        holder.symbol.setImageResource(app.getSymbol());
        holder.name.setText(app.getName());
        holder.contentapp.setText(app.getContent());
        PackageManager packageManager = context.getPackageManager();
        if(isPackageInstalled(app.getUrl(), packageManager)){
            holder.install.setText("INSTALLED");
        }

        for(int i = 0;i < app.getListimg().size(); i++){
            ImageView ii= new ImageView(this.context);
            ii.setBackgroundResource(app.getListimg().get(i));
            holder.linearLayout.addView(ii);
        }

        holder.install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAd.OnAdListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class View1 extends RecyclerView.ViewHolder {
        ImageView symbol;
        TextView name, contentapp;
        Button install;
        LinearLayout linearLayout;
        public View1(View itemview) {
            super(itemview);
            symbol = itemview.findViewById(R.id.symbol1);
            name = itemview.findViewById(R.id.name);
            install = itemview.findViewById(R.id.install);
            linearLayout = itemview.findViewById(R.id.layout1);
            contentapp  =   itemview.findViewById(R.id.contentapp);
        }
    }

    public interface OnAd{
        void OnAdListener(int position);
    }
    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        boolean found = true;
        try {
            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            found = false;
        }
        return found;
    }
}
