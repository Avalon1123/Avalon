package com.librariy.bean.myinterface;

import android.content.Intent;

public interface OnActivityResultListener {
    public boolean onActivityResult(int requestCode, int resultCode, Intent data);
}
