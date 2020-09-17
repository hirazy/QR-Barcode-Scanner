package com.example.qrbarcodescanner.model;

import android.widget.ImageView;

import java.util.ArrayList;

public class App {
    private int symbol;
    private String name;
    private String url;
    private String content;
    private ArrayList<Integer> Listimg;


    public App(int symbol, String name, String url, String content, ArrayList<Integer> listimg) {
        this.symbol = symbol;
        this.name = name;
        this.url = url;
        Listimg = listimg;
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Integer> getListimg() {
        return Listimg;
    }

    public void setListimg(ArrayList<Integer> listimg) {
        Listimg = listimg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
