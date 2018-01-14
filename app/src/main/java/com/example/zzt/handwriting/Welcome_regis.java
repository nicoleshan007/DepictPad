package com.example.zzt.handwriting;

/**
 * Created by master on 2016/12/21 0021.
 */

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Welcome_regis extends Activity {
    private Button mReturnButton;
    private TextView T;
    private ImageView i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_login);
        mReturnButton = (Button)findViewById(R.id.returnback);
        i=(ImageView) findViewById(R.id.logo);
        i.setImageResource(R.drawable.logo);
        Intent in=getIntent();
        Bundle bund=in.getExtras();
        String name=bund.getString("imfor");
        T=(TextView) findViewById(R.id.textView);
        T.setText(name+"您好，欢迎加入DepictPad世界！");
        SharedPreferences sharedPreferences= getSharedPreferences("test",
                Activity.MODE_PRIVATE);
// 使用getString方法获得value，注意第2个参数是value的默认值
        String name_check=sharedPreferences.getString("name", "游客，欢迎您");
        if(name_check.equals("游客，欢迎您")||name_check.equals(name))
        {
                    /*---------------------------存储名字---------------------------*/
            //实例化SharedPreferences对象（第一步）
            SharedPreferences mySharedPreferences= getSharedPreferences("test",
                    Activity.MODE_PRIVATE);
            //实例化SharedPreferences.Editor对象（第二步）
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            //用putString的方法保存数据
            editor.putString("name", name);
            //提交当前数据
            editor.commit();
        }
        else
        {
            Toast.makeText(this,"当前已有用户："+name_check+"在线，用户："+name+"将取代原用户登陆。",Toast.LENGTH_LONG).show();
            SharedPreferences mySharedPreferences= getSharedPreferences("test",
                    Activity.MODE_PRIVATE);
            //实例化SharedPreferences.Editor对象（第二步）
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            //用putString的方法保存数据
            editor.putString("name", name);
            //提交当前数据
            editor.commit();
        }

    }
    public void back_to_login(View view) {
        Intent intent3 = new Intent(Welcome_regis.this,MainActivity.class) ;
        startActivity(intent3);
        finish();
    }
}