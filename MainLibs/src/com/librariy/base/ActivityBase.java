package com.librariy.base;




import com.librariy.utils.UIHelper;
import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
/**
 * activity 父类，加入一些便捷方法和 恢复时savedInstanceState处理
 * @author 易申
 *
 */
public abstract class ActivityBase extends FragmentActivity{
//	MyToast toast;
	Bundle bundle=new Bundle();
//	  protected ComTitleView title;
	  
//	  public static String DeviceId=null;
//	public User getUser() {
//		return LemaoUtils.getUser();
//	}
	public void toast(String msg)
	{
//		toast.showmsg(msg);
		UIHelper.showToast(this, msg);
	}
//	public void toast(String msg,int time)
//	
//	{
//		toast.showmsg(msg,time);
//	}
	public abstract SharedPreferences getSharedPreferences();
//	public SharedPreferences getUserInfo() {
//		return getSharedPreferences("userInfo");
//	}
	
	protected void superOncreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.superOncreate(savedInstanceState);
		if(savedInstanceState!=null&&savedInstanceState.containsKey("bundle")&&savedInstanceState.getBundle("bundle")!=null)
			bundle=savedInstanceState.getBundle("bundle");
		 this.setContentView(this.getViewId());
//		 toast=new MyToast(this);
		 
		 try {
//			 title=(ComTitleView) findViewById(R.id.title);
			 superInit();
			 IntialComponent(savedInstanceState);
			 afterInit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 try {
			IntialListener(savedInstanceState);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		 if(DeviceId==null)
//		 {
//			 TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//			 DeviceId = tm.getDeviceId(); 
//		 }
		 
		 OnLoad();
		
		
	}
	
	public abstract void IntialComponent(Bundle savedInstanceState)throws Exception;
	public abstract void IntialListener(Bundle savedInstanceState) throws Exception;
	public abstract int getViewId();
	protected void superInit()
	{
		
	}
	
	protected void afterInit()
	{
		
	}
	
	protected void OnLoad()
	{
		
	}
	public void showDialog()
	{
		UIHelper.showDialog(this);
	}
	public void showDialog(String msg)
	{
	    UIHelper.showDialog(this,msg);
	}
	public void dismissDialog()
	{
		UIHelper.dismissDialog();;
	}
	
	public void replace(int id,Fragment fragment)
	{
		getSupportFragmentManager().beginTransaction().replace(id, fragment).commitAllowingStateLoss();
	}
	
	
//	public SharedPreferences getSetting()
//	{
//		return getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
//	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		HttpTool.init(this);
		
		MobclickAgent.onResume(this);
		
	}
	public Bundle getSaveInstance()
	{
		return this.bundle;
	}
	public Object getSaveObject(String key,Object def)
	{
		 if(this.bundle==null)
		 {
			 return def;
		 }
		 if(!this.bundle.containsKey(key))
			 return def;
		 return this.bundle.get(key);
	}
	
	
	public String getSaveString(String key,String def)
	{
		return (String)getSaveObject(key, def);
	}
	
	public String getSaveString(String key)
	{
		return (String)getSaveObject(key, null);
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub		
		super.onSaveInstanceState(outState);
		outState.putBundle("bundle", bundle);
//		if(LemaoUtils.getLotteryApp()!=null)
//		{
//			try {
//				outState.putParcelable("app", LemaoUtils.getLotteryApp());
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//			
////			outState.putSerializable("app", LemaoUtils.getLotteryApp());
//		}

		
		
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
		
	}
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
