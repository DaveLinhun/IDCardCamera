package com.wildma.wildmaidcardcamera;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wildma.idcardcamera.camera.CameraActivity;
import com.ym.idcard.reg.bean.IDCard;
import com.ym.idcard.reg.engine.OcrEngine;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Author   wildma
 * Github   https://github.com/wildma
 * Date     2018/6/24
 * Desc     ${身份证相机使用例子}
 */
public class MainActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.iv_image);
    }

    /**
     * 身份证正面
     */
    public void frontIdCard(View view) {
        CameraActivity.toCameraActivity(this, CameraActivity.TYPE_IDCARD_FRONT);
    }

    /**
     * 身份证反面
     */
    public void backIdCard(View view) {
        CameraActivity.toCameraActivity(this, CameraActivity.TYPE_IDCARD_BACK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraActivity.REQUEST_CODE && resultCode == CameraActivity.RESULT_CODE) {
            //获取图片路径，显示图片
            final String path = CameraActivity.getImagePath(data);
            if (!TextUtils.isEmpty(path)) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                imageView.setImageBitmap(bitmap);
                recogIDCard(bitmap);
            }
        }
    }

    private void recogIDCard(Bitmap icCardBitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        icCardBitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
        byte[] bitmapBytes = bos.toByteArray();
        OcrEngine engine = new OcrEngine();
        IDCard idCard = engine.recognize(this,2,bitmapBytes,null);
        if(Utils.isSfzh(idCard.getCardNo())){
            showQuestionMessage(this,"身份证信息",idCard.frontString(),null,null,
                    "确定",null);
        }else{
            showMessage("识别失败");
        }
    }

    public static void showQuestionMessage(Context context, String title,
                                           String info, DialogInterface.OnClickListener cancelListener, DialogInterface.OnClickListener okListener,
                                           String negaBtnStr, String posiBtnStr) {

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title).setMessage(info).setCancelable(false)
                .setIcon(null)
                .setNegativeButton(negaBtnStr, cancelListener)
                .setPositiveButton(posiBtnStr, okListener)
                .create();
        dialog.show();
    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
