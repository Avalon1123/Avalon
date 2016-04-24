package com.librariy.dialog;

import com.librariy.base.DialogBase;
import com.librariy.utils.PhotosUtils;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cn.sharesdk.R;
/**
 * 选择照片方式弹窗
 * @author 易申
 *
 */
public class PhotosUtilsDialog extends DialogBase {
    View paizhao,xiangce;
    TextView cancel;
    public PhotosUtilsDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    public PhotosUtilsDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
    }

    public PhotosUtilsDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getViewId() {
        // TODO Auto-generated method stub
        return R.layout.dialog_photo_utils;
    }

    @Override
    protected void IntialComponent(Bundle savedInstanceState) throws Exception {
        // TODO Auto-generated method stub
        paizhao=findViewById(R.id.paizhao);
        xiangce=findViewById(R.id.xiangce);
        cancel=(TextView) findViewById(R.id.cancel);
    }

    @Override
    protected void IntialListener(Bundle savedInstanceState) throws Exception {
        // TODO Auto-generated method stub
        cancel.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dismiss();
            }
        });
        xiangce.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(onChoosePhotosTypeLinstener!=null)
                    onChoosePhotosTypeLinstener.chooseEnd(PhotosUtils.TO_GALLERY);
                dismiss();
            }
        });
        paizhao.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(onChoosePhotosTypeLinstener!=null)
                    onChoosePhotosTypeLinstener.chooseEnd(PhotosUtils.TO_CAMERA);
                dismiss();
            }
        });
    }
    OnChoosePhotosTypeLinstener onChoosePhotosTypeLinstener;
    public void setOnChoosePhotosTypeLinstener(OnChoosePhotosTypeLinstener onChoosePhotosTypeLinstener) {
        this.onChoosePhotosTypeLinstener = onChoosePhotosTypeLinstener;
    }
    public interface OnChoosePhotosTypeLinstener 
    {
        public void chooseEnd(int type);
    }
    

}
