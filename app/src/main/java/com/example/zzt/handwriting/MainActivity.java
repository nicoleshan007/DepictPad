package com.example.zzt.handwriting;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.indris.material.RippleView;

import java.io.File;
import java.nio.channels.NetworkChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    // private GoogleApiClient client;


    MyDatabaseHelper dbHelper;
    TextView t;






    @Override
    protected void onCreate(Bundle savedInstanceState) {


        DisplayMetrics displayMetrics = new DisplayMetrics();
        //获取创建的宽度和高度
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        // 创建一个DrawView，该DrawView的宽度高度与该Activity保持相同
        final int x = displayMetrics.widthPixels;
        int y = displayMetrics.heightPixels;
        /*---------------------------首先判断是否存在已经登陆的人-------------------------*/
        SharedPreferences sharedPreferences= getSharedPreferences("test",
                Activity.MODE_PRIVATE);
// 使用getString方法获得value，注意第2个参数是value的默认值
        String name_check=sharedPreferences.getString("name", "游客，欢迎您！");

        // 执行查询
//        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
//                "select * from dict ",
//               null);
        final ArrayList<String> list = new ArrayList<String>();

        dbHelper = new MyDatabaseHelper(this, "pictureset.db", 2);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues values = new ContentValues();
        //values.put("name","asdfghjk");
        //db.insert("Pictureset",null,values);
        //db.delete("Pictureset",null,null);
        values.clear();


        Cursor cursor = db.query("Pictureset",null,null,null,null,null,null);
        if (cursor.moveToFirst())
        {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                list.add(name);
            }while(cursor.moveToNext());
        }
        cursor.close();


        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BaseAdapter adapter = new BaseAdapter() {



            @Override
            public int getCount() {
                //return imageId.length;
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LinearLayout line = new LinearLayout(MainActivity.this);
//                line.setOrientation(LinearLayout.HORIZONTAL);
//                TextView tv = new TextView(MainActivity.this);
//                String s = String.valueOf(position);
//                tv.setText(s);
//                //tv.setHeight();
//                double m = x*0.2;
//                int n = Integer.parseInt(new java.text.DecimalFormat("0").format(m));
//                tv.setWidth(n);
//
//
//

//                m = x*0.8;
//                n = Integer.parseInt(new java.text.DecimalFormat("0").format(m));
//                btn.setWidth(n);
//
//
//                line.addView(tv);
//                line.addView(btn);
                //View view = View.inflate(MainActivity.this,R.layout.line,null);
                //LinearLayout line = (LinearLayout)findViewById(R.id.llline);
//                TextView tv = (TextView)findViewById(R.id.tvline);
//                String s = String.valueOf(position);
//                tv.setText(s);

//                Button btn = (Button)findViewById(R.id.btnline);
//                btn.setText(list.get(position));
//                line.addView(tv);
                  int number = list.size()-1;
                  final String strname = list.get(number-position);
                  Button btn = new Button(MainActivity.this);
                  btn.setText(strname);
                  btn.setBackgroundColor(getResources().getColor(R.color.lightyellow));
                  btn.setWidth(x);
                  //btn.setBackgroundColor(getResources().getColor(R.color.powderblue));
                  line.addView(btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String filepath = "/sdcard/Handwriting/Gridpic/" + strname + "paint.png";
//                        File file = new File(filepath);
//                        Bitmap bm = BitmapFactory.decodeFile(filepath);
                        Intent intent=new Intent(MainActivity.this,Showpicture.class);
                        Bundle data = new Bundle();
                        data.putSerializable("bitmap",strname);
                        intent.putExtras(data);
                        startActivity(intent);
                    }
                });


                //return view;
                return line;
            }
        };

        ListView listview = (ListView) findViewById(R.id.lvpic);
        // 填充ListView
        listview.setAdapter(adapter);

        adapter.notifyDataSetChanged();


//        short str =(short)'牛';
//        String st = " "+str;
//        TextView tv=(TextView)findViewById (R.id.tvdraft1);
//        tv.setText(st);
//
//        short str1 =(short)'王';
//        String st1 = " "+str1;
//        TextView tv1=(TextView)findViewById (R.id.tvdraft2);
//        tv1.setText(st1);


//        IntentFilter filter = new IntentFilter(Matchpage.action);
//        registerReceiver(broadcastReceiver, filter);

        //将LinearLayout设置背景图片
//        LinearLayout line = (LinearLayout)findViewById(R.id.activity_main);
//        //line.setBackground(R.drawable.bgpic);
//        line.setBackgroundResource(R.drawable.bgpic);


        Button btn1 = (Button) findViewById(R.id.btnwrite);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                创建需要启动的Activity对应的Intent
                Intent intent = new Intent(MainActivity.this, Writingpage.class);
//                启动Intent对应的Activity
                startActivity(intent);
            }
        });
        t=(TextView)findViewById(R.id.tvhead);
        t.setText("Hi "+name_check);
        //返回按钮可以返回主界面
        Button btn2 = (Button) findViewById(R.id.btnback);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent();
                mIntent.setAction(Intent.ACTION_MAIN);
                mIntent.addCategory(Intent.CATEGORY_HOME);
                startActivity(mIntent);
            }
        });

        Button btn3;
        btn3 = (Button) findViewById( R.id.btnmatch);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                创建需要启动的Activity对应的Intent
                Intent intent = new Intent(MainActivity.this, Matchpage.class);
//                启动Intent对应的Activity
                startActivity(intent);
            }
        });

        Button btn4 = (Button) findViewById(R.id.btnsettings);

        btn4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO 自动生成的方法存根
                showPopupMenu(view);
            }

        });


    }


    protected ArrayList<String>
    converCursorToList(Cursor cursor)
    {
        ArrayList<String> result =
                new ArrayList<String>();
        // 遍历Cursor结果集
        while (cursor.moveToNext())
        {
            // 将结果集中的数据存入ArrayList中
            String str = null;
            // 取出查询记录中第2列、第3列的值
            str = cursor.getString(1);
            //map.put("detail", cursor.getString(2));
            result.add(str);
        }
        return result;
    }



    private void showPopupMenu(View v) {
        // TODO 自动生成的方法存根
        PopupMenu pop = new PopupMenu(this, v);
        pop.getMenuInflater().inflate(R.menu.main, pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                // TODO 自动生成的方法存根
                switch (arg0.getItemId()) {
                    case R.id.settings:
                        Intent intent = new Intent(MainActivity.this, Login.class);
//                启动Intent对应的Activity
                        startActivity(intent);
                    break;
                    case R.id.scanfontbase:
                        Intent intent2 = new Intent(MainActivity.this, Waiting.class);
//                启动Intent对应的Activity
                        startActivity(intent2);
                        break;
                    case R.id.cancel_user:
                        SharedPreferences mySharedPreferences= getSharedPreferences("test",
                                Activity.MODE_PRIVATE);
                        //实例化SharedPreferences.Editor对象（第二步）
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        //用putString的方法保存数据
                        editor.putString("name", "游客，欢迎您");
                        //提交当前数据
                        editor.commit();
                        t=(TextView) findViewById(R.id.tvhead);
                        t.setText("游客，欢迎您");
                    default:
                        break;
                }
                return false;
            }
        });
        pop.show();

    }

    public void showToast(String str) {

        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // 退出程序时关闭MyDatabaseHelper里的SQLiteDatabase
        if (dbHelper != null)
        {
            dbHelper.close();
        }
    }

}