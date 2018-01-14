package com.example.zzt.handwriting;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import static com.example.zzt.handwriting.R.string.emboss;

/**
 * Created by SDD on 2016/12/13.
 */

public class WritingpageExtra extends AppCompatActivity{

    static String s ;

    DrawView drawView;
    EmbossMaskFilter emboss;
    BlurMaskFilter blur;

    public static int mark = 3;//记录书写笔触的颜色选项
    public static int mark2 = 3;

    static int x = 0;
    static int y = 0;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writingpageextra);


        LinearLayout line = (LinearLayout) findViewById(R.id.llwriteboard_extra);
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





        Button btn_savesingleword = (Button)findViewById(R.id.bsavesingleword_extra);
        btn_savesingleword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savesinglewordname();

            }
        });


        Button mBtnClear = (Button) findViewById(R.id.btnoverride_extra);
        mBtnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawView.clear();
                //btml.list.clear();
                //System.out.println(btml.list.size());
            }
        });


        Button btnundo = (Button) findViewById(R.id.btnundo_extra);
        btnundo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                drawView.undo();
            }
        });


        Button btn_settings1 = (Button) findViewById(R.id.btnsettings1_extra);
        btn_settings1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu1(view);
            }
        });


        //返回按钮可以返回主界面
        Button btn;
        btn = (Button) findViewById(R.id.btnback2_extra);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                创建需要启动的Activity对应的Intent
                Intent intent = new Intent(WritingpageExtra.this, MainActivity.class);
//               启动Intent对应的Activity
                startActivity(intent);
            }
        });


    }

    public void savesinglewordname() {

        final EditText et = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("准备存储的文字为：")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s = et.getText().toString();
                        System.out.println(s);
                        if (s.isEmpty())
                        {
                            Toast.makeText(WritingpageExtra.this,"请输入一个字",Toast.LENGTH_SHORT).show();
                        }
                        else if(s.length()==1)
                        {
                            drawView.savebitmap(s);
                            Toast.makeText(WritingpageExtra.this,"恭喜您，单字保存成功！",
                                                Toast.LENGTH_SHORT).show();
                            Thread thread = new Thread();
                            try {
                                thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            transfertomatch();
                        }
                        else
                        {
                            Toast.makeText(WritingpageExtra.this,"只能输入一个字",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        builder.create();
        builder.show();
    }

    public void transfertomatch()
    {
//        Intent intent = new Intent(WritingpageExtra.this,Matchpage.class);
//        startActivity(intent);
        finish();
    }

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


                            }
                        }
                );
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                drawView.paint.setStrokeWidth(drawView.paint_width);
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

}
