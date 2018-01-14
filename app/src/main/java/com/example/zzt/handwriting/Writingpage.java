package com.example.zzt.handwriting;

/**
 * Created by SDD on 2016/11/8.
 */


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.AlteredCharSequence;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.graphics.BlurMaskFilter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.LogRecord;

import static java.security.AccessController.getContext;

public class Writingpage extends AppCompatActivity {

    public static final String action = "android.intent.action.writing";


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Writingpage Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client2.connect();
//        AppIndex.AppIndexApi.start(client2, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        AppIndex.AppIndexApi.end(client2, getIndexApiAction());
//        client2.disconnect();
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


    //  public int[] imageId = new int[] { }; // 定义并初始化保存图片id的数组
    //EditText et = new EditText(this);

    static String s ;
    static String s1 ;

    class BitmapList {
        List<Bitmap> list = new ArrayList<Bitmap>();
    }

    public BitmapList btml = new BitmapList();


    static int x = 0;
    static int y = 0;


    public static int mark = 3;//记录书写笔触的颜色选项
    public static int mark2 = 3;
    /**
     * 首先默认个文件保存路径
     */
    private static final String SAVE_PIC_PATH = //Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)
            Environment.getExternalStorageDirectory().getAbsolutePath();//保存到SD卡
    private static final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/good/savePic";//保存的确切位置

    EmbossMaskFilter emboss;
    BlurMaskFilter blur;
    DrawView drawView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    MyDatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        Resources res = getResources();
//        Bitmap    bmp = BitmapFactory.decodeResource(res, R.drawable.android);
//        btml.list.add(bmp);

        dbHelper = new MyDatabaseHelper(this, "pictureset.db", 1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.writingpage);

        //返回按钮可以返回主界面
        Button btn;
        btn = (Button) findViewById(R.id.btnback2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                创建需要启动的Activity对应的Intent
                Intent intent = new Intent(Writingpage.this, MainActivity.class);
//               启动Intent对应的Activity
                startActivity(intent);
            }
        });


        LinearLayout line = (LinearLayout) findViewById(R.id.llwriteboard);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //获取创建的宽度和高度
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        // 创建一个DrawView，该DrawView的宽度高度与该Activity保持相同
        x = displayMetrics.widthPixels;
        y = displayMetrics.heightPixels;
//        int m = line.getMeasuredHeight();
//        int n = line.getMeasuredWidth();
//        drawView = new DrawView(this, displayMetrics.widthPixels
//                , displayMetrics.heightPixels);
        //drawView = new DrawView(this, x,y);
        //LinearLayout ll = (LinearLayout)findViewById(R.id.llwriteboard);

        drawView = new DrawView(this, x, y);

        line.addView(drawView);

        emboss = new EmbossMaskFilter(new float[]
                {1.5f, 1.5f, 1.5f}, 0.6f, 6, 4.2f);
        blur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);


        Button mBtnClear = (Button) findViewById(R.id.btnoverride);
        mBtnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawView.clear();
                //btml.list.clear();
                //System.out.println(btml.list.size());
            }
        });


        Button btn_settings1 = (Button) findViewById(R.id.btnsettings1);
        btn_settings1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu1(view);
            }
        });


//        Button btn1 = (Button) findViewById(R.id.btnsavedecide);
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                View v=findViewById(R.id.llwriteboard);
//                Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),Bitmap.Config.ARGB_8888);
//                Canvas canvas = new Canvas(bitmap);
//                v.draw(canvas);
//                //以上将图片获取到了
//                //保存到sdcard中
//                File f = new File("/sdcard/namecard/b.jpg");
//                if (f.exists()) {
//                    f.delete();
//                }
//                try {
//                    FileOutputStream out = new FileOutputStream(f);
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
//                    out.flush();
//                    out.close();
//                    //Log.i(TAG, "已经保存");
//                } catch (FileNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                //ImageView iv = (ImageView)findViewById(R.id.imageview1);
//                //iv.setImageBitmap(bitmap);
//            }
//        });


//         class BitmapList {
//             List<Bitmap> list = new ArrayList<Bitmap>();
//        }
//        public BitmapList btml = new BitmapList();


        //设置GridView里面的内容，通过写的字加入进去
        final GridView gdv = (GridView) findViewById(R.id.gvfirstview);
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
                    imageview = new ImageView(Writingpage.this); // 实例化ImageView的对象
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





//        final CheckBox cb =(CheckBox)findViewById(R.id.cbsavesingleword);
//        cb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (cb.isChecked())
//                {
//                    Toast.makeText(Writingpage.this,"aaaaaaaaa",Toast.LENGTH_SHORT).show();                }
//            }
//        });





        //保存按钮可以保存所写的字
        Button btn_confirm = (Button) findViewById(R.id.btnconfirm1);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btml.list.size() == 36)
                {
                    Toast.makeText(Writingpage.this,"已达到最大输入字数",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    LinearLayout line = (LinearLayout) findViewById(R.id.llwriteboard);
                    int m = line.getMeasuredHeight() / 6;
                    int n = line.getMeasuredWidth() / 6;
//                int m = drawView.cacheBitmap.getWidth()/6;
//                int n = drawView.cacheBitmap.getHeight()/6;
                    Bitmap bitmap1 = Bitmap.createScaledBitmap(drawView.cacheBitmap, n, m, false);
                    btml.list.add(bitmap1);

                    adapter.notifyDataSetChanged();

//                    if (cb.isChecked())
//                    {
//                        savesinglewordname();
//                        //System.out.println(s);
//                        drawView.savebitmap(s);
//                    }
//                    else
//                    {
//                        //savesingleword();
//                    }
                    //drawView.clear();
                    //savesingleword();
                }
                drawView.clear();
            }
        });


        Button btnundo = (Button) findViewById(R.id.btnundo);
        btnundo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                drawView.undo();
            }
        });


        //采用子线程处理
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x123:
                        drawView.savebitmap();
                        break;
                    default:
                        break;
                }
            }
        };

        Button btnsavedecide = (Button) findViewById(R.id.btnsavedecide);
        btnsavedecide.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //drawView.savebitmap();
                //drawView.saveMyBitmap("aaa");

                //Bundle bundle = new Bundle();

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Message message = new Message();
//                        message.what = 0x123;
//                        handler.sendMessage(message);
//                    }
//                }).start();


                GridView gdv = (GridView) findViewById(R.id.gvfirstview);
                int height = gdv.getHeight();
                int width = gdv.getWidth();


                Bitmap bitmap = null;
                bitmap = getViewBitmap(gdv, width, height);
                savebitmap(bitmap);


//                // TODO Auto-generated method stub
//                Intent intent = new Intent(action);
//                intent.putExtra("data", "yes i am data");
//                sendBroadcast(intent);
//                finish();
            }
        });

        Button btn_savesingleword = (Button)findViewById(R.id.bsavesingleword);
        btn_savesingleword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savesinglewordname();

            }
        });


        Button btn_undogrid = (Button)findViewById(R.id.undogrid);
        btn_undogrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int m = btml.list.size()-1;
                if (m>=0) {
                    btml.list.remove(m);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(Writingpage.this,"不好意思，表图已经没有成员了！"
                            ,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void insertData(SQLiteDatabase db
            , String detail)
    {
        // 执行插入语句
        db.execSQL("insert into dict values(null, ?)"
                , new String[] { detail });
    }

    public void savebitmap(final Bitmap bitmap) {
        //final  str1;
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
                            Toast.makeText(Writingpage.this,"请输入表图名字",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(s.length()>10)
                        {
                            Toast.makeText(Writingpage.this,"不好意思，表图名字过长",
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

                            try {
                                FileOutputStream out = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
                                out.flush();
                                out.close();
                                //保存绘图文件路径
                                paintPath = "/sdcard/Handwriting/Gridpic/" + str;
                                MediaScannerConnection.scanFile(Writingpage.this, new String[]{paintPath},null,null);

                                String detail = s;
                                //insertData(dbHelper.getReadableDatabase(), detail);

                                //return paintPath;

                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put("name",detail);
                                db.insert("Pictureset",null,values);
                                values.clear();

                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            Toast.makeText(Writingpage.this,"恭喜您，表图已经保存成功！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        builder.create();
        builder.show();

        String paintPath = "/sdcard/Handwriting/Gridpic/" + s+ ".png";
        MediaScannerConnection.scanFile(Writingpage.this, new String[]{paintPath},null,null);


        //获得系统当前时间，并以该时间作为文件名
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//        String str = formatter.format(curDate);
//        String paintPath = "";
//        String str1 = str;
//        str = str + "paint.png";
//
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
//            MediaScannerConnection.scanFile(Writingpage.this, new String[]{paintPath},null,null);
//
//
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

//        String detail = s;
//        //insertData(dbHelper.getReadableDatabase(), detail);
//
//        //return paintPath;
//        dbHelper = new MyDatabaseHelper(this, "pictureset.db", 2);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("name",detail);
//        db.insert("Pictureset",null,values);
//        values.clear();

    }


//    public void savesingleword() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                .setMessage("是否保存所书写的单个字以作备用？");
//        setNegativeButton(builder);
//        setPositiveButton(builder);
//        builder.create();
//        builder.show();
//
//    }

    public void savesinglewordname() {

        final EditText et = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("准备存储的文字为：")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s1 = et.getText().toString();
                        System.out.println(s1);
                        if (s1.isEmpty())
                        {
                            Toast.makeText(Writingpage.this,"请输入一个字",Toast.LENGTH_SHORT).show();
                        }
                        else if(s1.length()==1)
                        {
                            drawView.savebitmap(s1);
                            Toast.makeText(Writingpage.this,"恭喜您，该字已经保存成功！",
                                    Toast.LENGTH_SHORT).show();
                            String paintPath = "/sdcard/Handwriting/singleword/" + s1+ ".png";
                            MediaScannerConnection.scanFile(Writingpage.this, new String[]{paintPath},null,null);
                        }
                        else
                        {
                            Toast.makeText(Writingpage.this,"只能输入一个字",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        builder.create();
        builder.show();
    }



//    private AlertDialog.Builder setNeutralButton(AlertDialog.Builder builder) {
//        return builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String s = null;
//                s = et.getText().toString();
//                drawView.savebitmap();
//            }
//        });
//    }


    private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {
        return builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Writingpage.this, "确定按钮被点击", Toast.LENGTH_SHORT).show();
                drawView.savebitmap();
            }
        });
    }


    private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder) {
        return builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Writingpage.this, "取消按钮被点击", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    public void onclick1(View v)
//    {
////        String str = "1";
////        try {
////            if (drawView.saveMyBitmap(str)) Toast.makeText(this,"success",Toast.LENGTH_SHORT);
////            else Toast.makeText(this,"fail",Toast.LENGTH_SHORT);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
////        BitmapDrawable bmp = new BitmapDrawable(drawView.cacheBitmap);
////       // GridView gv = (ImageView)findViewById(R.id.gdfirstview);
////        ImageView iv = (ImageView)findViewById(R.id.gdfirstview);
////        iv.setImageDrawable(bmp);
//
////        drawView.saveDrawing("1");
////        int a = drawView.cacheBitmap.getHeight();
////        Toast.makeText(this,a,Toast.LENGTH_SHORT);
//        //drawView.SaveBitmap(drawView.cacheBitmap);
//
//
//    }

    private void showPopupMenu1(View v) {
        // TODO 自动生成的方法存根
        PopupMenu pop = new PopupMenu(this, v);
        pop.getMenuInflater().inflate(R.menu.main1, pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                // TODO 自动生成的方法存根
                switch (arg0.getItemId()) {
                    case R.id.font:
                        //drawView.paint.setStrokeWidth(drawView.paint_width++);
                        singleChoice1();
                        break;
                    case R.id.color:
                        singleChoice2();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        pop.show();

    }


    public void singleChoice1() {
        final int[] flag = {0};
        String items[] = {"超小号", "小号", "偏小号", "中号", "偏大号", "大号", "超大号"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                //设置对话框标题
                .setTitle("书写笔触粗细")
                //设置单选列表项，默认是选中列表项4（索引为3）
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int pw = 5;
                                pw += 5 * which;
                                drawView.paint_width = pw;

                                flag[0] = 1;

                            }
                        }
                );
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (flag[0] == 1) {
                    drawView.paint.setStrokeWidth(drawView.paint_width);
                }
                else
                {
                    Toast.makeText(Writingpage.this,"不好意思，您未选择笔触宽度",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


    public void singleChoice2() {
        String items[] = {"蓝色", "绿色", "黑色", "红色", "粉红色", "灰色", "棕色", "黄色"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                //设置对话框标题
                .setTitle("书写笔触颜色")
                //设置单选列表项，默认是选中列表项4（索引为3）
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mark2 = which;
//                                switch (which)
//                                {
//                                    case 0:  drawView.paint.setColor(Color.BLUE);break;
//                                    case 1:  drawView.paint.setColor(Color.GREEN);break;
//                                    case 2:  drawView.paint.setColor(Color.BLACK);break;
//                                    case 3:  drawView.paint.setColor(Color.RED);break;
//                                    case 4:  drawView.paint.setColor(getResources().getColor(R.color.pink));break;
//                                    case 5:  drawView.paint.setColor(getResources().getColor(R.color.grey));break;
//                                    case 6:  drawView.paint.setColor(getResources().getColor(R.color.brown));break;
//                                    case 7:  drawView.paint.setColor(Color.YELLOW);break;
//                                    default:break;
//                                }
                            }
                        }
                );
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mark = mark2;
                switch (mark) {
                    case 0:
                        drawView.paint.setColor(Color.BLUE);
                        break;
                    case 1:
                        drawView.paint.setColor(Color.GREEN);
                        break;
                    case 2:
                        drawView.paint.setColor(Color.BLACK);
                        break;
                    case 3:
                        drawView.paint.setColor(Color.RED);
                        break;
                    case 4:
                        drawView.paint.setColor(getResources().getColor(R.color.pink));
                        break;
                    case 5:
                        drawView.paint.setColor(getResources().getColor(R.color.grey));
                        break;
                    case 6:
                        drawView.paint.setColor(getResources().getColor(R.color.brown));
                        break;
                    case 7:
                        drawView.paint.setColor(Color.YELLOW);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (mark) {
                    case 0:
                        drawView.paint.setColor(Color.BLUE);
                        break;
                    case 1:
                        drawView.paint.setColor(Color.GREEN);
                        break;
                    case 2:
                        drawView.paint.setColor(Color.BLACK);
                        break;
                    case 3:
                        drawView.paint.setColor(Color.RED);
                        break;
                    case 4:
                        drawView.paint.setColor(getResources().getColor(R.color.pink));
                        break;
                    case 5:
                        drawView.paint.setColor(getResources().getColor(R.color.grey));
                        break;
                    case 6:
                        drawView.paint.setColor(getResources().getColor(R.color.brown));
                        break;
                    case 7:
                        drawView.paint.setColor(Color.YELLOW);
                        break;
                    default:
                        break;
                }


            }
        });
        builder.show();
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


}
