package com.yu.common.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import java.security.MessageDigest;

/**
 * @author yudneghao
 * @date 2019-06-08
 */
public class GlideBlurTransformation extends CenterCrop {
  private Context context;

  public GlideBlurTransformation(Context context) {
    this.context = context;
  }

  @Override
  protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
    Bitmap bitmap = super.transform(pool, toTransform, outWidth, outHeight);
    return blurBitmap(context, bitmap, 25, (int) (outWidth * 0.5), (int) (outHeight * 0.5));
  }

  @Override
  public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  public Bitmap blurBitmap(Context context, Bitmap image, float blurRadius, int outWidth, int outHeight) {
    // 将缩小后的图片做为预渲染的图片
    Bitmap inputBitmap = Bitmap.createScaledBitmap(image, outWidth, outHeight, false);
    // 创建一张渲染后的输出图片
    Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
    // 创建RenderScript内核对象
    RenderScript rs = RenderScript.create(context);
    // 创建一个模糊效果的RenderScript的工具对象
    ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
    // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
    // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
    Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
    Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
    // 设置渲染的模糊程度, 25f是最大模糊度
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      blurScript.setRadius(blurRadius);
    }
    // 设置blurScript对象的输入内存
    blurScript.setInput(tmpIn);
    // 将输出数据保存到输出内存中
    blurScript.forEach(tmpOut);
    // 将数据填充到Allocation中
    tmpOut.copyTo(outputBitmap);
    return outputBitmap;
  }

}
