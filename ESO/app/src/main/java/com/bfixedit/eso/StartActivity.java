package com.bfixedit.eso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // VIEWS
        setContentView(R.layout.activity_start);

        // BUTTON LISTENERS
        Button btn_lb = (Button) findViewById(R.id.btn_lb);
        btn_lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, LorebookActivityMain.class));
            }
        });

        Button btn_ss = (Button) findViewById(R.id.btn_ss);
        btn_ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, SkyshardActivityMain.class));
            }
        });
    }
}