//package com.example.zzt.handwriting;
//
///**
// * Created by master on 2016/12/20 0020.
// */
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import org.json.JSONObject;
//
//import cn.bmob.v3.Bmob;
//import cn.bmob.v3.BmobUser;
//import okhttp3.Call;
//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class Register extends Activity {
//    private EditText mAccount;                        //用户名编辑
//    private EditText mPwd;                            //密码编辑
//    private EditText mPwdCheck;                       //密码编辑
//    private Button mSureButton;                       //确定按钮
//    private Button mCancelButton;                     //取消按钮
//    //private UserDataManager mUserDataManager;         //用户数据管理类
//    private Handler handler1=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if(msg.what==0x123)
//            {
//                Toast toast=Toast.makeText(getApplicationContext(), "注册成功" , Toast.LENGTH_SHORT);
//                //显示toast信息
//                toast.show();
//            }
//            else
//            {
//                Toast toast=Toast.makeText(getApplicationContext(), "注册失败" , Toast.LENGTH_SHORT);
//                //显示toast信息
//                toast.show();
//            }
//
//        }
//    };
//    private Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if(msg.what==0x123)
//            {
//                Toast toast=Toast.makeText(getApplicationContext(), "登陆成功" , Toast.LENGTH_SHORT);
//                //显示toast信息
//                toast.show();
//            }
//            else
//            {
//                Toast toast=Toast.makeText(getApplicationContext(), "登陆失败" , Toast.LENGTH_SHORT);
//                //显示toast信息
//                toast.show();
//            }
//
//        }
//    };
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.register);
//
//        Bmob.initialize(this, "72cf7a03c9653d69016025442edbf997");
//
//        mAccount = (EditText) findViewById(R.id.resetpwd_edit_name);
//        mPwd = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
//        mPwdCheck = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);
//
//        mSureButton = (Button) findViewById(R.id.register_btn_sure);
//        mCancelButton = (Button) findViewById(R.id.register_btn_cancel);
//
//        mSureButton.setOnClickListener(m_register_Listener);      //注册界面两个按钮的监听事件
//        mCancelButton.setOnClickListener(m_register_Listener);
//
//    }
//    OnClickListener m_register_Listener = new OnClickListener() {    //不同按钮按下的监听事件选择
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.register_btn_sure:                       //确认按钮的监听事件
//                    register_check();
//                    break;
//                case R.id.register_btn_cancel:                     //取消按钮的监听事件,由注册界面返回登录界面
//                    Intent intent_Register_to_Login = new Intent(Register.this,Login.class) ;    //切换User Activity至Login Activity
//                    startActivity(intent_Register_to_Login);
//                    finish();
//                    break;
//            }
//        }
//    };
//    public void register_check() {                                //确认按钮的监听事件
//
//        final  String userName = mAccount.getText().toString().trim();
//        String userPwd = mPwd.getText().toString().trim();
//        String userPwdCheck = mPwdCheck.getText().toString().trim();
//        JSONObject obj=new JSONObject();
//        try{
//
//            obj.put("name", userName);
//            obj.put("pwd", userPwd);
//        }catch (Exception e)
//        {
//            // TODO: handle exception
//        }
//        final String jsonRes1=obj.toString();
//        //检查用户是否存在
//        BmobUser bu = new BmobUser();
//        bu.setUsername(userName);
//        bu.setPassword(userPwd);
//        //bu.setEmail("sendi@163.com");
////注意：不能用save方法进行注册
//        if(!userPwd.equals(userPwdCheck))
//        {
//            Toast toast=Toast.makeText(getApplicationContext(), "确认密码不正确。请重新输出", Toast.LENGTH_SHORT);
//            //显示toast信息
//            toast.show();
//            mPwdCheck.setText("");
//        }
//        else
//        {
////            bu.signUp(new SaveListener<MyUser>() {
////                @Override
////                public void done(MyUser s, BmobException e) {
////                    if(e==null){
////                        Toast toast=Toast.makeText(getApplicationContext(), "注册成功：" +s.getUsername(), Toast.LENGTH_SHORT);
////                        //显示toast信息
////                        toast.show();
////                        Intent a = new Intent(Register.this,MainActivity.class) ;    //切换User Activity至Login Activity
////                        startActivity(a);
////                        //toast("注册成功:" +s.toString());
////                    }else{
////                        Toast toast=Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT);
////                        //显示toast信息
////                        toast.show();
////                    }
////                }
////            });
//            new Thread(){
//                @Override
//                public void run() {
//                    OkHttpClient okhc=new OkHttpClient();
//                    FormBody body=new FormBody.Builder()
//                            .add("insert", jsonRes1).build();
//                    Request request=new Request.Builder()
//                            //.url("http://115.159.77.179:8181/schoolMarket/Register")
//                            .url("http://118.89.149.165:8080/life/Register")
//                            //.url("http://123.206.174.229:8080/schoolMarket/Register")
//                            .post(body)
//                            .build();
//                    Call call=okhc.newCall(request);
//                    try{
//                        Response response=call.execute();
//                        if(response.isSuccessful()){
//                            // System.out.println("插入数据库成功");
//                            handler1.sendEmptyMessage(0x123);
//                            Intent a = new Intent(Register.this,Welcome_regis.class) ;    //切换User Activity至Login Activity
//                            Bundle bun=new Bundle();
//                            bun.putString("imfor",userName);
//                            a.putExtras(bun);
//                            startActivity(a);
//                        }else{
//                            //System.out.println("插入数据库失败");
//                            handler1.sendEmptyMessage(0x122);
//
//                        }
//                    }catch (Exception e) {
//                        // TODO: handle exception
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
//            /*------new Thread(){
//                @Override
//                public void run() {
//                    OkHttpClient okhc=new OkHttpClient();
//                    FormBody body=new FormBody.Builder()
//                            .add("login", jsonRes1).build();
//                    Request request=new Request.Builder()
//                            //.url("http://115.159.77.179:8181/schoolMarket/Login")
//                            .url("http://118.89.145.184:8080/Handwriting/Login")
//                            //.url("http://123.206.174.229:8080/schoolMarket/Login")
//                            .post(body)
//                            .build();
//                    Call call=okhc.newCall(request);
//                    try{
//                        Response response=call.execute();
//                        if(response.isSuccessful()){
//                            String res_flag=response.body().string().trim();
//                            //System.out.println(response.body().string());
//                            if(res_flag.equals("1")){
//                                handler.sendEmptyMessage(0x123);
//                            }else{
//                                handler.sendEmptyMessage(0x122);
//                            }
//                        }else{
//                            handler.sendEmptyMessage(0x121);
//                        }
//                    }catch (Exception e) {
//                        // TODO: handle exception
//                        handler.sendEmptyMessage(0x121);
//                        e.printStackTrace();
//                    }
//                }
//            }.start();------*/
//        }
//
//    }
//
//
//}


package com.example.zzt.handwriting;

/**
 * Created by master on 2016/12/20 0020.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Register extends Activity {
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private EditText mPwdCheck;                       //密码编辑
    private Button mSureButton;                       //确定按钮
    private Button mCancelButton;                     //取消按钮
    //private UserDataManager mUserDataManager;         //用户数据管理类
    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x123)
            {
                Toast toast=Toast.makeText(getApplicationContext(), "注册成功" , Toast.LENGTH_SHORT);
                //显示toast信息
                toast.show();
            }
            else
            {
                Toast toast=Toast.makeText(getApplicationContext(), "注册失败" , Toast.LENGTH_SHORT);
                //显示toast信息
                toast.show();
            }

        }
    };
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x123)
            {
                Toast toast=Toast.makeText(getApplicationContext(), "登陆成功" , Toast.LENGTH_SHORT);
                //显示toast信息
                toast.show();
            }
            else
            {
                Toast toast=Toast.makeText(getApplicationContext(), "登陆失败" , Toast.LENGTH_SHORT);
                //显示toast信息
                toast.show();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Bmob.initialize(this, "72cf7a03c9653d69016025442edbf997");

        mAccount = (EditText) findViewById(R.id.resetpwd_edit_name);
        mPwd = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
        mPwdCheck = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);

        mSureButton = (Button) findViewById(R.id.register_btn_sure);
        mCancelButton = (Button) findViewById(R.id.register_btn_cancel);

        mSureButton.setOnClickListener(m_register_Listener);      //注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(m_register_Listener);

    }
    OnClickListener m_register_Listener = new OnClickListener() {    //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_sure:                       //确认按钮的监听事件
                    register_check();
                    break;
                case R.id.register_btn_cancel:                     //取消按钮的监听事件,由注册界面返回登录界面
                    Intent intent_Register_to_Login = new Intent(Register.this,Login.class) ;    //切换User Activity至Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
                    break;
            }
        }
    };
    public void register_check() {                                //确认按钮的监听事件

        final  String userName = mAccount.getText().toString().trim();
        String userPwd = mPwd.getText().toString().trim();
        String userPwdCheck = mPwdCheck.getText().toString().trim();
        JSONObject obj=new JSONObject();
        try{

            obj.put("name", userName);
            obj.put("pwd", userPwd);
        }catch (Exception e)
        {
            // TODO: handle exception
        }
        final String jsonRes1=obj.toString();
        //检查用户是否存在
        BmobUser bu = new BmobUser();
        bu.setUsername(userName);
        bu.setPassword(userPwd);
        //bu.setEmail("sendi@163.com");
        //注意：不能用save方法进行注册
        if(!userPwd.equals(userPwdCheck))
        {
            Toast toast=Toast.makeText(getApplicationContext(), "确认密码不正确。请重新输出", Toast.LENGTH_SHORT);
            //显示toast信息
            toast.show();
            mPwdCheck.setText("");
        }
        else
        {
            /*-----------------------------------------------------------------------------------------------------------------------
            此处为租借腾讯的服务器未过期时的代码现在为了APP体验将其注释
            ----------------------------------------------------------------------------------------------------------------------*/
//            new Thread(){
//                @Override
//                public void run() {
//                    OkHttpClient okhc=new OkHttpClient();
//                    FormBody body=new FormBody.Builder()
//                            .add("insert", jsonRes1).build();
//                    Request request=new Request.Builder()
//                            //.url("http://115.159.77.179:8181/schoolMarket/Register")
//                            .url("http://118.89.145.184:8080/Handwriting/Register")
//                            //.url("http://123.206.174.229:8080/schoolMarket/Register")
//                            .post(body)
//                            .build();
//                    Call call=okhc.newCall(request);
//                    try{
//                        Response response=call.execute();
//                        if(response.isSuccessful()){
//                            // System.out.println("插入数据库成功");
//                            handler1.sendEmptyMessage(0x123);
//                            Intent a = new Intent(Register.this,Welcome_regis.class) ;    //切换User Activity至Login Activity
//                            Bundle bun=new Bundle();
//                            bun.putString("imfor",userName);
//                            a.putExtras(bun);
//                            startActivity(a);
//                        }else{
//                            //System.out.println("插入数据库失败");
//                            handler1.sendEmptyMessage(0x122);
//
//                        }
//                    }catch (Exception e) {
//                        // TODO: handle exception
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
//
            /*-----------------------------------------------------------------------------------------------------------------------
            这个部分采用的是第三方服务器方案，下方有租借腾讯的服务器未过期时的代码
            ----------------------------------------------------------------------------------------------------------------------*/
            bu.signUp(new SaveListener<MyUser>() {
                @Override
                public void done(MyUser s, BmobException e) {
                    if(e==null){
                        Toast toast=Toast.makeText(getApplicationContext(), "注册成功：" +s.getUsername(), Toast.LENGTH_SHORT);
                        //显示toast信息
                        toast.show();
                        Intent a = new Intent(Register.this,Welcome_regis.class) ;    //切换User Activity至Login Activity
                        Bundle b2=new Bundle();
                        b2.putString("imfor",userName);
                        a.putExtras(b2);
                        startActivity(a);
                        //toast("注册成功:" +s.toString());
                    }else{
                        Toast toast=Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT);
                        //显示toast信息
                        toast.show();
                    }
                }
            });

        }

    }


}