package com.example.zzt.handwriting;

/**
 * Created by SDD on 2016/11/7.
 */

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Matchpage extends AppCompatActivity {


    MyDatabaseHelper dbHelper;

    static String s;


    class BitmapList {
        List<Bitmap> list = new ArrayList<Bitmap>();
    }

    public BitmapList btml = new BitmapList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matchpage);

//        LinearLayout line = (LinearLayout) findViewById(R.id.llfirstview);
//        final int m = line.getMeasuredHeight() / 6;
//        final int n = line.getMeasuredWidth() / 6;

        //返回按钮可以返回主界面
        Button btn = (Button) findViewById(R.id.btnback3);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                创建需要启动的Activity对应的Intent
                Intent intent = new Intent(Matchpage.this,MainActivity.class);
//                启动Intent对应的Activity
                startActivity(intent);
            }
        });



        final GridView gdv = (GridView) findViewById(R.id.gvfirstview_match);
        final BaseAdapter adapter = new BaseAdapter() {


            @Override
            public int getCount() {
                //return imageId.length;
                return btml.list.size();
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


//                ImageView imageview; // 声明ImageView的对象
//                if (convertView == null) {
//                    imageview = new ImageView(Writingpage.this); // 实例化ImageView的对象
//                    imageview.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // 设置缩放方式
//                    imageview.setPadding(0, 0, 0, 0); // 设置ImageView的内边距
//                } else {
//                    imageview = (ImageView) convertView;
//                }
//                imageview.setImageResource(imageId[position]); // 为ImageView设置要显示的图片
//                return imageview; // 返回ImageView

                //if (btml.list.size() == 18) {return null;}
                ImageView imageview;
                if (convertView == null) {
                    imageview = new ImageView(Matchpage.this); // 实例化ImageView的对象
                    imageview.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // 设置缩放方式
                    imageview.setPadding(0, 0, 0, 0); // 设置ImageView的内边距
                } else {
                    imageview = (ImageView) convertView;
                }
                imageview.setImageBitmap(btml.list.get(position));
                return imageview;
            }
        };

        gdv.setAdapter(adapter);



        Button btn_confirm = (Button)findViewById(R.id.btnconfirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btml.list.size() == 36)
                {
                    Toast.makeText(Matchpage.this,"已达到最大输入字数",Toast.LENGTH_SHORT).show();
                }
                else {
                    EditText et = (EditText) findViewById(R.id.edit_match);
                    String s = et.getText().toString();
                    if (s.length() == 0) {
                        Toast.makeText(Matchpage.this, "请输入想要搜索的字", Toast.LENGTH_SHORT).show();
                    } else {
                        String filepath = "/sdcard/Handwriting/singleword/" + s + ".png";
                        File file = new File(filepath);
                        if (file.exists()) {
                            Bitmap bm = BitmapFactory.decodeFile(filepath);

                            LinearLayout line = (LinearLayout) findViewById(R.id.llfirstview);
                            int m = line.getMeasuredHeight() / 6;
                            int n = line.getMeasuredWidth() / 6;

//                int m = drawView.cacheBitmap.getWidth()/6;
//                int n = drawView.cacheBitmap.getHeight()/6;
                            Bitmap bitmap1 = Bitmap.createScaledBitmap(bm, n, m, false);

                            btml.list.add(bitmap1);
                            adapter.notifyDataSetChanged();

                            EditText edittext = (EditText)findViewById(R.id.edit_match);
                            edittext.setText("");
                            //将图片显示到ImageView中
                            //img.setImageBitmap(bm);
                        } else {
                            Toast.makeText(Matchpage.this, "未搜索到该字",
                                            Toast.LENGTH_SHORT).show();
                            choosewrite();
                        }
                    }
                }
            }
        });


        Button btn_savedecide_match = (Button)findViewById(R.id.btnsavedecide_match);
        btn_savedecide_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GridView gdv = (GridView) findViewById(R.id.gvfirstview_match);
                int height = gdv.getHeight();
                int width = gdv.getWidth();


                Bitmap bitmap = null;
                bitmap = getViewBitmap(gdv, width, height);
                savebitmap(bitmap);
            }
        });


        Button btnundo = (Button)findViewById(R.id.btnundo);
        btnundo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int m = btml.list.size()-1;
                if (m>=0) {
                    btml.list.remove(m);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(Matchpage.this,"不好意思，表图已经没有成员了！"
                                    ,Toast.LENGTH_SHORT).show();
                }
            }
        });




    }



    //获取Gridview的图片保存到sd卡上
    public Bitmap getViewBitmap(View comBitmap, int width, int height) {
        Bitmap bitmap = null;
        if (comBitmap != null) {
            comBitmap.clearFocus();
            comBitmap.setPressed(false);

            boolean willNotCache = comBitmap.willNotCacheDrawing();
            comBitmap.setWillNotCacheDrawing(false);

            // Reset the drawing cache background color to fully transparent
            // for the duration of this operation
            int color = comBitmap.getDrawingCacheBackgroundColor();
            comBitmap.setDrawingCacheBackgroundColor(0);
            float alpha = comBitmap.getAlpha();
            comBitmap.setAlpha(1.0f);

            if (color != 0) {
                comBitmap.destroyDrawingCache();
            }

            int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            comBitmap.measure(widthSpec, heightSpec);
            comBitmap.layout(0, 0, width, height);

            comBitmap.buildDrawingCache();
            Bitmap cacheBitmap = comBitmap.getDrawingCache();
            if (cacheBitmap == null) {
                Log.e("view.ProcessImageToBlur", "failed getViewBitmap(" + comBitmap + ")",
                        new RuntimeException());
                return null;
            }
            bitmap = Bitmap.createBitmap(cacheBitmap);
            // Restore the view
            comBitmap.setAlpha(alpha);
            comBitmap.destroyDrawingCache();
            comBitmap.setWillNotCacheDrawing(willNotCache);
            comBitmap.setDrawingCacheBackgroundColor(color);
        }
        return bitmap;
    }


    public void savebitmap(final Bitmap bitmap) {
        //获得系统当前时间，并以该时间作为文件名
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//        String str = formatter.format(curDate);
//        String paintPath = "";
//        String str1 = str;
//        str = str + "paint.png";
//        File dir = new File("/sdcard/Handwriting/Gridpic/");
//        File file = new File("/sdcard/Handwriting/Gridpic/", str);
//        if (!dir.exists()) {
//            dir.mkdir();
//        } else {
//            if (file.exists()) {
//                file.delete();
//            }
//        }
//
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
//            out.flush();
//            out.close();
//            //保存绘图文件路径
//            paintPath = "/sdcard/Handwriting/Gridpic/" + str;
//            Toast.makeText(Matchpage.this,"恭喜您，表图已保存成功！",
//                    Toast.LENGTH_SHORT).show();
//            MediaScannerConnection.scanFile(Matchpage.this, new String[]{paintPath},null,null);
//
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }


        dbHelper = new MyDatabaseHelper(this, "pictureset.db", 2);
        final EditText et = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("准备存储的表图为：")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s = et.getText().toString();
                        System.out.println(s);
                        if (s.isEmpty())
                        {
                            Toast.makeText(Matchpage.this,"请输入表图名字",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(s.length()>10)
                        {
                            Toast.makeText(Matchpage.this,"不好意思，表图名字过长",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //drawView.savebitmap1(s);
                            //Toast.makeText(Writingpage.this,"只能输入一个字",Toast.LENGTH_SHORT).show();
                            String paintPath = "";
                            String str =  s + ".png";

                            File dir = new File("/sdcard/Handwriting/Gridpic/");
                            File file = new File("/sdcard/Handwriting/Gridpic/", str);
                            if (!dir.exists()) {
                                dir.mkdir();
                            } else {
                                if (file.exists()) {
                                    file.delete();
                                }
                            }

                            try
                            {
                                FileOutputStream out = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
                                out.flush();
                                out.close();
                                //保存绘图文件路径
                                paintPath = "/sdcard/Handwriting/Gridpic/" + str;
                                MediaScannerConnection.scanFile(Matchpage.this,
                                        new String[]{paintPath},null,null);


                                //String paintPath1 = "/sdcard/Handwriting/Gridpic/" + s+ ".png";
                                //MediaScannerConnection.scanFile(Matchpage.this, new String[]{paintPath1},null,null);
                                //return paintPath;

                                String str1 = s;

                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put("name",str1);
                                db.insert("Pictureset",null,values);
                                values.clear();


                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Toast.makeText(Matchpage.this,"恭喜您，表图已经保存成功！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        builder.create();
        builder.show();

//        String paintPath = "/sdcard/Handwriting/Gridpic/" + s+ ".png";
//        MediaScannerConnection.scanFile(Matchpage.this, new String[]{paintPath},null,null);
//        //return paintPath;
//
//        String str1 = s;
//        dbHelper = new MyDatabaseHelper(this, "pictureset.db", 2);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("name",str1);
//        db.insert("Pictureset",null,values);
//        values.clear();
    }


    public void start_writingpage()
    {
        Intent intent = new Intent(Matchpage.this,WritingpageExtra.class);
        startActivity(intent);
    }


    public void choosewrite() {

        //final EditText et = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("字体库里不存在该字，是否想要书写？")
                //.setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        start_writingpage();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


        builder.create();
        builder.show();
    }

}