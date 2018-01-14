package com.example.zzt.handwriting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by lenovo on 2016/12/27.
 */
public class Waiting extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);
        int i=0;
        while(i<200000000)
            i++;
        Toast.makeText(this,"点击任意地方返回O(∩_∩)O~",Toast.LENGTH_LONG).show();

        Button btn_login_btn_cancle2 = (Button)findViewById(R.id.login_btn_cancle2);
        btn_login_btn_cancle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(Waiting.this,MainActivity.class) ;
                startActivity(intent3);
            }
        });
    }
    public void click_back(){
        Intent intent3 = new Intent(Waiting.this,MainActivity.class) ;
        startActivity(intent3);
    }


}
