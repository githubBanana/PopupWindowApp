package com.xs.popupwindowapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MyListPopup myListPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("你好");
        }
        myListPopup = new MyListPopup(this,list);
    }

    public void show(View view) {

        myListPopup.show(view);

    }
}
