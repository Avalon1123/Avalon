package com.librariy.view;
import com.librariy.utils.Judge;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.sharesdk.R;


public class StarView extends LinearLayout {	
	
	public StarView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StarView(Context context) {
		super(context);
	}
	public void updateStar(String record) {
        if (!Judge.StringNotNull(record))
            return;
        if("000".equals(record))
            record="1";
        this.removeAllViews();
        for (int i = 0; i < record.length(); i++) {
            String s = record.charAt(i) + "";
            ImageView vv = new ImageView(getContext());
            this.setPadding(2, 2, 2, 2);
            if ("3".equals(s)) {
                vv.setImageResource(R.drawable.rank_3);
            } else if ("2".equals(s)) {
                vv.setImageResource(R.drawable.rank_2);
            } else if ("1".equals(s)) {
                vv.setImageResource(R.drawable.rank_1);
            } else if ("4".equals(s)) {
                vv.setImageResource(R.drawable.rank_4);
            } else if ("5".equals(s)) {
                vv.setImageResource(R.drawable.rank_5);
            } else if ("6".equals(s)) {
                vv.setImageResource(R.drawable.rank_6);
            } else if ("7".equals(s)) {
                vv.setImageResource(R.drawable.rank_7);
            } else if ("0".equals(s)) {
            } else {
                vv.setImageResource(R.drawable.rank_huangguan);
            }
            this.addView(vv);
        }
    }
}
