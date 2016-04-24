package com.librariy.dialog;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import cn.sharesdk.R;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.google.gson.annotations.Until;
import com.librariy.base.AppContextBase;
import com.librariy.base.DialogBase;
import com.librariy.reader.base.ReaderBase;
import com.librariy.utils.Judge;
/**
 * 分享
 * @author 申
 *
 */
public class ShareDialog extends DialogBase implements View.OnClickListener , PlatformActionListener{
View qq,weixin,pengyouq,weibo,duanxin,kongjian,cancel;
ShareParams shareParams;
	@Override
	public int getViewId() {
		// TODO Auto-generated method stub
		return R.layout.dialog_share;
	}

	public ShareDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public ShareDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public ShareDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void IntialComponent(Bundle savedInstanceState) throws Exception {
		// TODO Auto-generated method stub
		ShareSDK.initSDK(getContext());
		qq=findViewById(R.id.qq);
		weixin=findViewById(R.id.weixin);
		pengyouq=findViewById(R.id.pengyouq);
		weibo=findViewById(R.id.weibo);
		duanxin=findViewById(R.id.duanxin);
		kongjian=findViewById(R.id.kongjian);
		cancel=findViewById(R.id.cancel);
	}

	@Override
	protected void IntialListener(Bundle savedInstanceState) throws Exception {
		qq.setTag(QQ.NAME);
		weixin.setTag(Wechat.NAME);
		pengyouq.setTag(WechatMoments.NAME);
		weibo.setTag(SinaWeibo.NAME);
		kongjian.setTag(QZone.NAME);
		duanxin.setTag(ShortMessage.NAME);
		
		qq.setOnClickListener(this);
		weixin.setOnClickListener(this);
		pengyouq.setOnClickListener(this);
		weibo.setOnClickListener(this);
		duanxin.setOnClickListener(this);
		kongjian.setOnClickListener(this);
		
		
		// TODO Auto-generated method stub
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (null!=shareParams) {
			Platform platform = ShareSDK.getPlatform(v.getTag()+"");
			platform.setPlatformActionListener(this);
			if (v.getId()==R.id.qq||v.getId()==R.id.pengyouq) {
				String s=shareParams.get("msg", String.class);
				if (Judge.StringNotNull(s)) {
					shareParams.setTitle(s);
				}
				
			}
			platform.share(shareParams);
		}
		
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		toast("取消分享");
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		toast("分享成功");
		dismiss();
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		toast("分享失败");
	}
	public void show(ShareParams shareParams) {
			// TODO Auto-generated method stub
			super.show();
			this.shareParams=shareParams;
		}
	public static ShareParams getDefParams(String title,String imgUrl,String url,String content)
	{
		ShareParams shareParams=new ShareParams();
		shareParams.setTitle(title);
		shareParams.setImageUrl(imgUrl);
		url=Judge.StringNotNull(url)?url:"http://www.xiaofund.com/";
		url+=url.contains("?")?"&share=yes":"?share=yes";
		shareParams.setUrl(url);
		shareParams.setShareType(Platform.SHARE_WEBPAGE);
		shareParams.setTitleUrl(url);
		shareParams.setText(content);
		shareParams.setSiteUrl("http://www.xiaofund.com/");
		shareParams.setSite("小房东");
		return shareParams; 
	}
}
