package com.example.zzt.handwriting;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class Showpicture extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showpicture);

        ImageView imageview=(ImageView)findViewById(R.id.ivshow);
        Intent intent=getIntent();
        if(intent!=null)
        {
            String strname = (String) intent.getSerializableExtra("bitmap");
            String filepath = "/sdcard/Handwriting/Gridpic/" + strname + ".png";
            File file = new File(filepath);
            if (file.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(filepath);
                //Bitmap bitmap=intent.getParcelableExtra("bitmap");
                imageview.setImageBitmap(bm);
            }
            else {
                Toast.makeText(Showpicture.this, "未找到该图片",
                        Toast.LENGTH_SHORT).show();
            }
        }


        //返回按钮可以返回主界面
        Button btn;
        btn = (Button) findViewById(R.id.btnbackshow);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                创建需要启动的Activity对应的Intent
                Intent intent = new Intent(Showpicture.this, MainActivity.class);
//               启动Intent对应的Activity
                startActivity(intent);
            }
        });

    }


}