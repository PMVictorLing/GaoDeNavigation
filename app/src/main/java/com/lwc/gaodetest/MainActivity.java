package com.lwc.gaodetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lwc.gaodetest.dialog.MyAlertDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.findViewById(R.id.tv_start_nva).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SingleRouteCalculateActivity.class));
            }
        });

        this.findViewById(R.id.bt_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAlertDialog.Builder builder = new MyAlertDialog.Builder(MainActivity.this)
                        .setContentView(R.layout.dialog_layout)
                        .setText(R.id.tv_start_nva,"修改")
                        .setOnclickLisener(R.id.bt_cancle, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this,"取消", Toast.LENGTH_LONG).show();
                            }
                        }).setFullWidth().adddefaultAnimation().setStartBottom(true);
                builder.show();
            }
        });
    }
}
