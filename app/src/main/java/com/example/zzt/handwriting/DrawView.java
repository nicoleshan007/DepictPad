package com.example.zzt.handwriting;

/**
 * Created by SDD on 2016/11/26.
 */


        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.graphics.PorterDuffXfermode;
        import android.media.MediaScannerConnection;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Environment;
        import android.os.Message;
        import android.provider.MediaStore;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;
        import android.graphics.PorterDuff;
        import android.widget.Toast;

        import java.io.BufferedOutputStream;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.OutputStream;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.Iterator;
        import java.util.LinkedList;
        import java.util.List;

public class DrawView extends View
{
    public static final String IMAGE_SAVE_DIRNAME = "Drawings";
    public static final String IMAGE_TEMP_DIRNAME = IMAGE_SAVE_DIRNAME + "/.temporary";


    public  int paint_width = Paint.DITHER_FLAG;
    // 定义记录前一个拖动事件发生点的坐标
    float preX;
    float preY;
    public Path path;
    public Paint paint = null;
    // 定义一个内存中的图片，该图片将作为缓冲区
    Bitmap cacheBitmap = null;
    // 定义cacheBitmap上的Canvas对象
    Canvas cacheCanvas = null;

    public class Paint_Path{
        public Path path;
        public Paint paint;
    }

    public  Paint_Path paint_path ;
    public  ArrayList<Paint_Path> list_pp = new ArrayList<Paint_Path>();



    public DrawView(Context context, int width , int height)
    {
        super(context);
        // 创建一个与该View相同大小的缓存区
        cacheBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        path = new Path();
        // 设置cacheCanvas将会绘制到内存中的cacheBitmap上
        cacheCanvas.setBitmap(cacheBitmap);
        // 设置画笔的颜色
        paint = new Paint(paint_width);
        paint.setColor(Color.RED);
        // 设置画笔风格
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
        // 反锯齿
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // 获取拖动事件的发生位置
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                path = new Path();

                paint_path = new Paint_Path();
                paint_path.paint = paint;
                paint_path.path = path;

                path.reset();

                // 从前一个点绘制到当前点之后，把当前点定义成下次绘制的前一个点
                path.moveTo(x, y);

                preX = x;
                preY = y;


                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                // 从前一个点绘制到当前点之后，把当前点定义成下次绘制的前一个点
                path.quadTo(preX, preY, x, y);
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_UP:
                cacheCanvas.drawPath(path, paint); // ①

                list_pp.add(paint_path);//用来记录画布上所书写的笔迹，用来做撤销的时候保存记忆

                //path.reset();//此语句将path重置，如果加上将得不到入栈的内容

                break;
        }
        invalidate();
        // 返回true表明处理方法已经处理该事件
        return true;
    }
    @Override
    public void onDraw(Canvas canvas)
    {
        Paint bmpPaint = new Paint();
        // 将cacheBitmap绘制到该View组件上
        canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint); // ②
        // 沿着path绘制
        canvas.drawPath(path, paint);
    }

    //清除画板
    public void clear() {
        if (cacheCanvas != null) {
            path.reset();
            cacheCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            invalidate();
            list_pp.clear();

        }
    }

    /**
     * 撤销的核心思想就是将画布清空，
     * 将保存下来的Path路径最后一个移除掉，
     * 重新将路径画在画布上面。
     */
    public void undo() {
//        Bitmap mBitmap = Bitmap.createBitmap(width,height,
//                Bitmap.Config.RGB_565);
//        cacheCanvas.setBitmap(mBitmap);// 重新设置画布，相当于清空画布

        path.reset();
        cacheCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        cacheCanvas.drawPaint(paint);

        //clear();
        // 清空画布，但是如果图片有背景的话，则使用上面的重新初始化的方法，用该方法会将背景清空掉...

        if (list_pp != null && list_pp.size() > 0) {

            // 移除最后一个path,相当于出栈操作
            list_pp.remove(list_pp.size() - 1);
            Iterator<Paint_Path> iter = list_pp.iterator();
            while (iter.hasNext()) {
                Paint_Path dp = iter.next();
                cacheCanvas.drawPath(dp.path, dp.paint);
                //cacheCanvas.drawCircle(100,100,100,dp.paint);
               // System.out.println(list_pp.iterator());
            }
            invalidate();// 刷新

   /*在这里保存图片纯粹是为了方便,保存图片进行验证*/
            String fileUrl = Environment.getExternalStorageDirectory()
                    .toString() + "/android/data/test.png";
            try {
                FileOutputStream fos = new FileOutputStream(new File(fileUrl));
                cacheBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    public void saveMyBitmap(String bitName){
        File f = new File("/sdcard/" + bitName + ".png");
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //DebugMessage.put("在保存图片时出错："+e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        cacheBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        * 保存所绘图形
        * 返回绘图文件的存储路径
        * */
    public void savebitmap(){
        //获得系统当前时间，并以该时间作为文件名
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyyMMddHHmmss");
        Date curDate   =   new   Date(System.currentTimeMillis());//获取当前时间
        String   str   =   formatter.format(curDate);
        //String str = s;
        String paintPath = "";
        str = str + "paint.png";
        File dir = new File("/sdcard/Handwriting/");
        File file = new File("/sdcard/Handwriting/",str);
        if (!dir.exists()) {
            dir.mkdir();
        }
        else{
            if(file.exists()){
                file.delete();
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            cacheBitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
            out.flush();
            out.close();
            //保存绘图文件路径
            paintPath = "/sdcard/Handwriting/" + str;


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //return paintPath;
    }


    public void savebitmap(String s1){
        //获得系统当前时间，并以该时间作为文件名
        //SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyyMMddHHmmss");
        //Date curDate   =   new   Date(System.currentTimeMillis());//获取当前时间
        //String   str   =   formatter.format(curDate);
        String str = s1;
        //String str = s;
        String paintPath = "";
        str = str + ".png";
        File dir = new File("/sdcard/Handwriting/singleword/");
        File file = new File("/sdcard/Handwriting/singleword/",str);
        if (!dir.exists()) {
            dir.mkdir();
        }
        else{
            if(file.exists()){
                file.delete();
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            cacheBitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
            out.flush();
            out.close();
            //保存绘图文件路径
            paintPath = "/sdcard/Handwriting/singleword/" + str;
            //MediaScannerConnection.scanFile(Writingpage.this, new String[]{paintPath},null,null);


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //return paintPath;
    }


    public void savebitmap1(String s1){
        //获得系统当前时间，并以该时间作为文件名
        //SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyyMMddHHmmss");
        //Date curDate   =   new   Date(System.currentTimeMillis());//获取当前时间
        //String   str   =   formatter.format(curDate);
        String str = s1;
        //String str = s;
        String paintPath = "";
        str = str + ".png";
        File dir = new File("/sdcard/Handwriting/Gridpic/");
        File file = new File("/sdcard/Handwriting/Gridpic/",str);
        if (!dir.exists()) {
            dir.mkdir();
        }
        else{
            if(file.exists()){
                file.delete();
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            cacheBitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
            out.flush();
            out.close();
            //保存绘图文件路径
            paintPath = "/sdcard/Handwriting/Gridpic/" + str;
            //MediaScannerConnection.scanFile(Writingpage.this, new String[]{paintPath},null,null);


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //return paintPath;
    }



//    public boolean saveMyBitmap(String bitName) throws IOException {
//        File dirFile = new File("./sdcard/DCIM/Camera");
//        if (!dirFile.exists()) {
//            dirFile.mkdirs();
//        }
//        File f = new File("./sdcard/DCIM/Camera" + bitName + ".png");
//        boolean flag = false;
//        f.createNewFile();
//        FileOutputStream fOut = null;
//        try {
//            fOut = new FileOutputStream(f);
//            cacheBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
//            flag = true;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        try {
//            fOut.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            fOut.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return flag;
//
//    }


//

//    //保存到本地
//    public void SaveBitmap(Bitmap bmp)
//    {
//        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap.getWidth(), cacheBitmap.getHeight(), cacheBitmap.getConfig());
//        Canvas canvas = new Canvas(bitmap);
//        //加载背景图片
//        Bitmap bmps = BitmapFactory.decodeResource(getResources(), R.color.white);
//        canvas.drawBitmap(bmps, 0, 0, null);
//        //加载要保存的画面
//        canvas.drawBitmap(bmp, 10, 100, null);
//        //保存全部图层
//        canvas.save(Canvas.ALL_SAVE_FLAG);
//        canvas.restore();
//        //存储路径
//        File file = new File("/sdcard/song/");
//        if(!file.exists())
//            file.mkdirs();
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(file.getPath() + "/xuanzhuan.jpg");
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
//            fileOutputStream.close();
//            System.out.println("saveBmp is here");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }



}

