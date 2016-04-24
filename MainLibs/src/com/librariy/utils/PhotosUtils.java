package com.librariy.utils;

import java.io.File;
import java.util.List;
import java.util.UUID;

import com.librariy.bean.PhotosBean;
import com.librariy.widget.ZorImgCrop;
import com.librariy.widget.ZorImgCrop.CropListener;
import com.librariy.widget.ZorImgPicker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * 图片获取处理类
 * 
 * @author 易申
 * 
 */
public class PhotosUtils  {
    public final String filePhth = Utils.CACHE_FILE_PATH;
    public static final int TO_CAMERA = 31001;
    public static final int TO_GALLERY = 31002;
    int TO_CAMERA_CODE = 0;
    int TO_GALLERY_CODE = 1;
    private Uri uri;
//    PhotosUtilsDialog dialog;
    ZorImgPicker dialog;
//    ZorImgCrop
    Activity activity;
    PhotosBean photo;

    public PhotosUtils(Activity activity) {
        this.activity = activity;

    }

//    @Override
//    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        if (requestCode == TO_CAMERA_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                if (uri != null && Judge.StringNotNull(uri.getPath())) {
//                    photo.paths.add(uri.getPath());
//                    if (photosUtilsListener != null)
//                        photosUtilsListener.onPhotosBeanChange(photo);
//                }
//            }
//
//            return true;
//        } else if (requestCode == TO_GALLERY_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                if (data != null) {
//                    photo = (PhotosBean) data.getSerializableExtra("PhotosBean");
//                    if (photosUtilsListener != null)
//                        photosUtilsListener.onPhotosBeanChange(photo);
//                }
//            }
//
//            return true;
//        }
//
//        return false;
//    }

    public void chooseType(PhotosBean photo) {

        if (photo == null)
            return;
        try {
            this.photo = (PhotosBean) photo.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
//        if (photosUtilsListener != null) {
//            TO_CAMERA_CODE = photosUtilsListener.CameraCode();
//            TO_GALLERY_CODE = photosUtilsListener.GalleryCode();
//        }
        if (photo.needCrop) {
	        new ZorImgCrop(activity).showCropDialog(photo.width, photo.higth, new CropListener() {
				
				@Override
				public void onImgCroped(String path) {
					// TODO Auto-generated method stub
					if (Judge.StringNotNull(path)) {
						PhotosUtils.this.photo.paths.clear();
						PhotosUtils.this.photo.paths.add(path);
					}
					 if (photosUtilsListener != null)
	            		 photosUtilsListener.onPhotosBeanChange(PhotosUtils.this.photo);
				}
			});
		} else {
			if (dialog == null)
	            dialog = getPhotosUtilsDialog();
	        if (!dialog.isShowing())
	            dialog.showPicker(photo.maxSize);
		}
        
    }

    public ZorImgPicker getPhotosUtilsDialog() {
    	if (null!=photo) {
    		
    		 dialog = new ZorImgPicker(activity);
    	        
    	        dialog.setImageConfig(photo.width, photo.higth, true).setPickListener(new ZorImgPicker.PickListener() {
    	            public void onImgPicked(List<String> images) {
//    	            	photo.paths.clear();
    	            	if (Judge.ListNotNull(images)) {
    	            		photo.paths.clear();
    	            		photo.paths.addAll(images);
    					} 
    	            	 if (photosUtilsListener != null)
    	            		 photosUtilsListener.onPhotosBeanChange(photo);
    	            }
    	        });
    	        

		}
       
//        dialog.setOnChoosePhotosTypeLinstener(new OnChoosePhotosTypeLinstener() {
//
//            @Override
//            public void chooseEnd(int type) {
//                // TODO Auto-generated method stub
//                if (type == TO_CAMERA) {
//            		if (!photo.canAdd()) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
//            			UIHelper.showToast(activity, "最多只能选择"+photo.maxSize+"张图片");
//        			return;
//        		}
//                    getImageFromCamera();
//                } else {
//                    getImageFromGallery();
//                }
//            }
//        });
        return dialog;
    }

    // 从照相机获取图片
    protected void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {

            File destDir = new File(filePhth);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            UUID uuid = UUID.randomUUID();
            File file = new File(filePhth, uuid.toString() + ".jpg");
            uri = Uri.fromFile(file);

            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(getImageByCamera, TO_CAMERA_CODE);

        } else {
            UIHelper.showToast(activity, "请确认已经插入SD卡");
        }
    }

//    // 从相册获取图片
//
//    protected void getImageFromGallery() {
//        if (photosUtilsListener == null)
//            return;
//        Intent intent = photosUtilsListener.getGalleryIntent();
//        if (intent != null) {
//            intent.putExtra("PhotosBean", photo);
//            activity.startActivityForResult(intent, TO_GALLERY_CODE);
//
//        }
//
//    }

    PhotosUtilsListener photosUtilsListener;

    public void setPhotosUtilsListener(PhotosUtilsListener photosUtilsListener) {
        this.photosUtilsListener = photosUtilsListener;
    }

    public interface PhotosUtilsListener {
//        public Intent getGalleryIntent();
//
//        public int CameraCode();
//
//        public int GalleryCode();

        public void onPhotosBeanChange(PhotosBean photo);
    }

	

}
