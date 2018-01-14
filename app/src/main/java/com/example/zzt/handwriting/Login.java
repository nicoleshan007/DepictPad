//package com.example.zzt.handwriting;
//
///**
// * Created by master on 2016/12/14 0014.
// */
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.json.JSONObject;
//
//import cn.bmob.v3.Bmob;
//import cn.bmob.v3.BmobUser;
//import cn.bmob.v3.exception.BmobException;
//import cn.bmob.v3.listener.SaveListener;
//import okhttp3.Call;
//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class Login extends Activity {                 //登录界面活动
//
//    public int pwdresetFlag=0;
//    private EditText mAccount;                        //用户名编辑
//    private EditText mPwd;                            //密码编辑
//    private Button mRegisterButton;                   //注册按钮
//    private Button mLoginButton;                      //登录按钮
//    private Button quxiao;                      //取消按钮
//    private CheckBox mRememberCheck;
//
//    private SharedPreferences login_sp;
//    private String userNameValue,passwordValue;
//
//    private View loginView;                           //登录
//    private View loginSuccessView;
//    private TextView loginSuccessShow;
//    private TextView mChangepwdText;
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
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login);
//
//        Bmob.initialize(this, "72cf7a03c9653d69016025442edbf997");
//
//        //通过id找到相应的控件
//        mAccount = (EditText) findViewById(R.id.login_edit_account);
//        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
//        mRegisterButton = (Button) findViewById(R.id.login_btn_register);
//        mLoginButton = (Button) findViewById(R.id.login_btn_login);
//        quxiao = (Button)findViewById(R.id.login_btn_cancle);
//
//        loginView=findViewById(R.id.login_view);
//        loginSuccessView=findViewById(R.id.login_success_view);
//        loginSuccessShow=(TextView) findViewById(R.id.login_success_show);
//
//        login_sp = getSharedPreferences("userInfo", 0);
//        String name=login_sp.getString("USER_NAME", "");
//        String pwd =login_sp.getString("PASSWORD", "");
//        boolean choseRemember =login_sp.getBoolean("mRememberCheck", false);
//        boolean choseAutoLogin =login_sp.getBoolean("mAutologinCheck", false);
//
//
//        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
//        if(choseRemember){
//            mAccount.setText(name);
//            mPwd.setText(pwd);
//            mRememberCheck.setChecked(true);
//        }
//
//        mRegisterButton.setOnClickListener(mListener);                      //采用OnClickListener方法设置不同按钮按下之后的监听事件
//        mLoginButton.setOnClickListener(mListener);
//        quxiao.setOnClickListener(mListener);
//       // mChangepwdText.setOnClickListener(mListener);
//
//        ImageView image = (ImageView) findViewById(R.id.logo);             //使用ImageView显示logo
//        image.setImageResource(R.drawable.logo);
//
////        if (mUserDataManager == null) {
////            mUserDataManager = new UserDataManager(this);s
////            mUserDataManager.openDataBase();                              //建立本地数据库
////        }
//    }
//    OnClickListener mListener = new OnClickListener() {                  //不同按钮按下的监听事件选择
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.login_btn_register:                            //登录界面的注册按钮
//                    Intent intent = new Intent(Login.this, Register.class);
////                启动Intent对应的Activity
//                    startActivity(intent);
//                    break;
//                case R.id.login_btn_login:                              //登录界面的登录按钮
//                    login1();
//                    break;
//                case R.id.login_btn_cancle:                              //登录界面的登录按钮
//                    Intent i = new Intent(Login.this, MainActivity.class);
////                启动Intent对应的Activity
//                    startActivity(i);
//                    break;
////                case R.id.login_btn_cancle:                             //登录界面的注销按钮
////                    cancel();
////                    break;
////                case R.id.login_text_change_pwd:                             //登录界面的注销按钮
////                    Intent intent_Login_to_reset = new Intent(Login.this,Resetpwd.class) ;    //切换Login Activity至User Activity
////                    startActivity(intent_Login_to_reset);
////                    finish();
////                    break;
//            }
//        }
//    };
//
//    public void login() {                                              //登录按钮监听事件
//
//        String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
//        String userPwd = mPwd.getText().toString().trim();
//        BmobUser bu = new BmobUser();
//        bu.setUsername(userName);
//        bu.setPassword(userPwd);
//        //bu.setEmail("sendi@163.com");
////注意：不能用save方法进行注册
//
//        bu.signUp(new SaveListener<MyUser>() {
//            @Override
//            public void done(MyUser s, BmobException e) {
//                if(e==null){
//                     Toast toast=Toast.makeText(getApplicationContext(),
//                             "注册成功：" +s.getUsername(), Toast.LENGTH_SHORT);
//                     //显示toast信息
//                     toast.show();
//                    //toast("注册成功:" +s.toString());
//                }else{
//                    Toast toast=Toast.makeText(getApplicationContext(),
//                            "注册失败", Toast.LENGTH_SHORT);
//                    //显示toast信息
//                    toast.show();
//                }
//            }
//        });
//    }
//    public void login1() {                                              //登录按钮监听事件
//
//        final String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
//        String userPwd = mPwd.getText().toString().trim();
//        JSONObject obj=new JSONObject();
//        try{
//            obj.put("name", userName);
//            obj.put("pwd", userPwd);
//        }catch (Exception e)
//        {
//            // TODO: handle exception
//        }
//        final String jsonRes=obj.toString();
//        /*---------new Thread(){
//            @Override
//            public void run() {
//                OkHttpClient okhc=new OkHttpClient();
//                FormBody body=new FormBody.Builder()
//                        .add("insert", jsonRes).build();
//                Request request=new Request.Builder()
//                        //.url("http://115.159.77.179:8181/schoolMarket/Register")
//                        .url("http://118.89.145.184:8080/Handwriting/Register")
//                        //.url("http://123.206.174.229:8080/schoolMarket/Register")
//                        .post(body)
//                        .build();
//                Call call=okhc.newCall(request);
//                try{
//                    Response response=call.execute();
//                    if(response.isSuccessful()){
//                       // System.out.println("插入数据库成功");
//                        handler1.sendEmptyMessage(0x123);
//                    }else{
//                        //System.out.println("插入数据库失败");
//                        handler1.sendEmptyMessage(0x122);
//
//                    }
//                }catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                }
//            }
//        }.start();----*/
//        new Thread(){
//            @Override
//            public void run() {
//                OkHttpClient okhc=new OkHttpClient();
//                FormBody body=new FormBody.Builder()
//                        .add("login", jsonRes).build();
//                Request request=new Request.Builder()
//                        //.url("http://115.159.77.179:8181/schoolMarket/Login")
//                        .url("http://118.89.149.165:8080/life/Login")
//                        //.url("http://123.206.174.229:8080/schoolMarket/Login")
//                        .post(body)
//                        .build();
//                Call call=okhc.newCall(request);
//                try{
//                    Response response=call.execute();
//                    if(response.isSuccessful()){
//                        String res_flag=response.body().string().trim();
//                        //System.out.println(response.body().string());
//                        if(res_flag.equals("1")){
//                            handler.sendEmptyMessage(0x123);Intent intent = new Intent(Login.this, Welcome_login.class);
//                            Bundle bun=new Bundle();
//                            bun.putString("imfor",userName);
//                            intent.putExtras(bun);
////                启动Intent对应的Activity
//                            startActivity(intent);
//
//                        }else{
//                            handler.sendEmptyMessage(0x122);
//                        }
//                    }else{
//                        handler.sendEmptyMessage(0x121);
//                    }
//                }catch (Exception e) {
//                    // TODO: handle exception
//                    handler.sendEmptyMessage(0x121);
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//        //BmobUser bu = new BmobUser();
////        BmobUser bu2 = new BmobUser();
////        bu2.setUsername(userName);
////        bu2.setPassword(userPwd);
////
////        bu2.login(new SaveListener<BmobUser>() {
////
////            @Override
////            public void done(BmobUser bmobUser, BmobException e) {
////                if(e==null){
////                    Toast toast=Toast.makeText(getApplicationContext(), "登陆成功" , Toast.LENGTH_SHORT);
////                    //显示toast信息
////                    toast.show();
////                    Intent intent = new Intent(Login.this, MainActivity.class);
//////                启动Intent对应的Activity
////                    startActivity(intent);
////                }else{
////                    Toast toast=Toast.makeText(getApplicationContext(), "登陆失败，请检查输入正确性" , Toast.LENGTH_SHORT);
////                    //显示toast信息
////                    toast.show();
////                }
////            }
////        });
//    }
////    public void cancel() {           //注销
////        if (isUserNameAndPwdValid()) {
////            String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
////            String userPwd = mPwd.getText().toString().trim();
////            int result=mUserDataManager.findUserByNameAndPwd(userName, userPwd);
////            if(result==1){                                             //返回1说明用户名和密码均正确
////                Toast.makeText(this, getString(R.string.cancel_success),Toast.LENGTH_SHORT).show();<span style="font-family: Arial;">//注销成功提示</span>
////                        mPwd.setText("");
////                mAccount.setText("");
////                mUserDataManager.deleteUserDatabyname(userName);
////            }else if(result==0){
////                Toast.makeText(this, getString(R.string.cancel_fail),Toast.LENGTH_SHORT).show();  //注销失败提示
////            }
////        }
////
////    }
////    public boolean isUserNameAndPwdValid() {
////        if (mAccount.getText().toString().trim().equals("")) {
////            Toast.makeText(this, getString(R.string.account_empty),
////                    Toast.LENGTH_SHORT).show();
////            return false;
////        } else if (mPwd.getText().toString().trim().equals("")) {
////            Toast.makeText(this, getString(R.string.pwd_empty),
////                    Toast.LENGTH_SHORT).show();
////            return false;
////        }
////        return true;
////    }
////    @Override
////    protected void onResume() {
////        if (mUserDataManager == null) {
////            mUserDataManager = new UserDataManager(this);
////            mUserDataManager.openDataBase();
////        }
////        super.onResume();
////    }
////    @Override
////    protected void onDestroy() {
////        super.onDestroy();
////    }
////    @Override
////    protected void onPause() {
////        if (mUserDataManager != null) {
////            mUserDataManager.closeDataBase();
////            mUserDataManager = null;
////        }
////        super.onPause();
////    }
//}


package com.example.zzt.handwriting;

/**
 * Created by master on 2016/12/14 0014.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class Login extends Activity {                 //登录界面活动

    public int pwdresetFlag=0;
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private Button mRegisterButton;                   //注册按钮
    private Button mLoginButton;                      //登录按钮
    private Button quxiao;                      //取消按钮
    private CheckBox mRememberCheck;

    private SharedPreferences login_sp;
    private String userNameValue,passwordValue;

    private View loginView;                           //登录
    private View loginSuccessView;
    private TextView loginSuccessShow;
    private TextView mChangepwdText;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Bmob.initialize(this, "72cf7a03c9653d69016025442edbf997");

        //通过id找到相应的控件
        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        mRegisterButton = (Button) findViewById(R.id.login_btn_register);
        mLoginButton = (Button) findViewById(R.id.login_btn_login);
        quxiao = (Button)findViewById(R.id.login_btn_cancle);

        loginView=findViewById(R.id.login_view);
        loginSuccessView=findViewById(R.id.login_success_view);
        loginSuccessShow=(TextView) findViewById(R.id.login_success_show);

        login_sp = getSharedPreferences("userInfo", 0);
        String name=login_sp.getString("USER_NAME", "");
        String pwd =login_sp.getString("PASSWORD", "");
        boolean choseRemember =login_sp.getBoolean("mRememberCheck", false);
        boolean choseAutoLogin =login_sp.getBoolean("mAutologinCheck", false);


        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if(choseRemember){
            mAccount.setText(name);
            mPwd.setText(pwd);
            mRememberCheck.setChecked(true);
        }

        mRegisterButton.setOnClickListener(mListener);                      //采用OnClickListener方法设置不同按钮按下之后的监听事件
        mLoginButton.setOnClickListener(mListener);
        quxiao.setOnClickListener(mListener);


        ImageView image = (ImageView) findViewById(R.id.logo);             //使用ImageView显示logo
        image.setImageResource(R.drawable.logo);


    }
    OnClickListener mListener = new OnClickListener() {                  //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_btn_register:                            //登录界面的注册按钮
                    Intent intent = new Intent(Login.this, Register.class);
//                启动Intent对应的Activity
                    startActivity(intent);
                    break;
                case R.id.login_btn_login:                              //登录界面的登录按钮
                    login1();
                    break;
                case R.id.login_btn_cancle:                              //登录界面的登录按钮
                    Intent i = new Intent(Login.this, MainActivity.class);
//                启动Intent对应的Activity
                    startActivity(i);
                    break;

            }
        }
    };


    public void login1() {                                              //登录按钮监听事件

        final String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
        String userPwd = mPwd.getText().toString().trim();
        JSONObject obj=new JSONObject();
        try{
            obj.put("name", userName);
            obj.put("pwd", userPwd);
        }catch (Exception e)
        {
            // TODO: handle exception
        }
        final String jsonRes=obj.toString();
       /*-----------------------------------------------------------------------------------------------------------------------
            此处为租借腾讯的服务器未过期时的代码现在为了APP体验将其注释
            ----------------------------------------------------------------------------------------------------------------------*/
//        new Thread(){
//            @Override
//            public void run() {
//                OkHttpClient okhc=new OkHttpClient();
//                FormBody body=new FormBody.Builder()
//                        .add("login", jsonRes).build();
//                Request request=new Request.Builder()
//                        //.url("http://115.159.77.179:8181/schoolMarket/Login")
//                        .url("http://118.89.145.184:8080/Handwriting/Login")
//                        //.url("http://123.206.174.229:8080/schoolMarket/Login")
//                        .post(body)
//                        .build();
//                Call call=okhc.newCall(request);
//                try{
//                    Response response=call.execute();
//                    if(response.isSuccessful()){
//                        String res_flag=response.body().string().trim();
//                        //System.out.println(response.body().string());
//                        if(res_flag.equals("1")){
//                            handler.sendEmptyMessage(0x123);Intent intent = new Intent(Login.this, Welcome_login.class);
//                            Bundle bun=new Bundle();
//                            bun.putString("imfor",userName);
//                            intent.putExtras(bun);
////                启动Intent对应的Activity
//                            startActivity(intent);
//
//                        }else{
//                            handler.sendEmptyMessage(0x122);
//                        }
//                    }else{
//                        handler.sendEmptyMessage(0x121);
//                    }
//                }catch (Exception e) {
//                    // TODO: handle exception
//                    handler.sendEmptyMessage(0x121);
//                    e.printStackTrace();
//                }
//            }
//        }.start();
        /*-----------------------------------------------------------------------------------------------------------------------
            这个部分采用的是第三方服务器方案，下方有租借腾讯的服务器未过期时的代码
            ----------------------------------------------------------------------------------------------------------------------*/
        BmobUser bu = new BmobUser();
        BmobUser bu2 = new BmobUser();
        bu2.setUsername(userName);
        bu2.setPassword(userPwd);

        bu2.login(new SaveListener<BmobUser>() {

            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null){
                    Toast toast=Toast.makeText(getApplicationContext(), "登陆成功" , Toast.LENGTH_SHORT);
                    //显示toast信息
                    toast.show();
                    Intent intent = new Intent(Login.this, Welcome_login.class);
                    Bundle b1=new Bundle();
                    b1.putString("imfor",userName);
                    intent.putExtras(b1);
//                启动Intent对应的Activity
                    startActivity(intent);
                }else{
                    Toast toast=Toast.makeText(getApplicationContext(), "登陆失败，请检查输入正确性" , Toast.LENGTH_SHORT);
                    //显示toast信息
                    toast.show();
                }
            }
        });
    }
    public void login_used() {                                              //登录按钮监听事件

        String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
        String userPwd = mPwd.getText().toString().trim();
        BmobUser bu = new BmobUser();
        bu.setUsername(userName);
        bu.setPassword(userPwd);

        //注意：不能用save方法进行注册

        bu.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser s, BmobException e) {
                if(e==null){
                    Toast toast=Toast.makeText(getApplicationContext(), "注册成功：" +s.getUsername(), Toast.LENGTH_SHORT);
                    //显示toast信息
                    toast.show();
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