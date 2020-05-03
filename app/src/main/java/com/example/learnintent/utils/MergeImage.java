package com.example.learnintent.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.Display;

public class MergeImage {
        /**
         * 合成碟片图片
         *
         * @param discBitmap  黑胶碟片底图
         * @param embeddedPic 专辑封面图
         * @return
         */
        public static Bitmap mergeThumbnailBitmap(Bitmap discBitmap, Bitmap embeddedPic) {

            //获得黑胶碟片底图宽和高
            int w = discBitmap.getWidth();
            int h = discBitmap.getHeight();


            embeddedPic = Bitmap.createScaledBitmap(embeddedPic, w, h, true);//根据黑胶碟片底图的宽和高，对专辑图片进行缩放

            Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);//argb_8888是系统文件格式

            Canvas canvas = new Canvas(bm);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
            //这里需要先画出一个圆
            canvas.drawCircle(w / 2, h / 2, w / 3 +10, paint);
            //圆画好之后将画笔重置一下
            paint.reset();

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); //设置图像合成模式，该模式为只在源图像和目标图像相交的地方绘制源图像
            canvas.drawBitmap(embeddedPic, 0, 0, paint);
            paint.reset();
            canvas.drawBitmap(discBitmap, 0, 0, null);
            return bm;
        }

    public static Bitmap FitTheScreenSizeImage(Bitmap m, Activity activity)
    {
        Display display = activity.getWindowManager().getDefaultDisplay();
        float ScreenWidth = display.getWidth();
        float ScreenHeight = display.getHeight();
        //float width  = (float)ScreenWidth/m.getWidth();
        //float height = (float)ScreenHeight/m.getHeight();
        float bias=(float)ScreenHeight/m.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(bias,bias);
        return Bitmap.createBitmap(m, 0, 0, m.getWidth(), m.getHeight(), matrix, true);
    }
    }