package com.lwc.gaodetest.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by lingwancai on
 * 2018/9/21 20:31
 */
public class AlertController {


    private MyAlertDialog myAlertDialog;
    private Window mWindow;

    public AlertController(MyAlertDialog myAlertDialog, Window window) {
        this.myAlertDialog = myAlertDialog;
        this.mWindow = window;
    }

    public MyAlertDialog getMyAlertDialog() {
        return myAlertDialog;
    }

    /**
     * 获取dialog的window
     *
     * @return
     */
    public Window getmWindow() {
        return mWindow;
    }

    public static class AlertParams {
        public int mThemeResId;
        public Context mContext;
        //能否取消 默认可以取消true
        public boolean mCancelable = true;
        //取消 Cancel监听
        public DialogInterface.OnCancelListener mOnCancelListener;
        //取消 Dismiss监听
        public DialogInterface.OnDismissListener mOnDismissListener;

        public DialogInterface.OnKeyListener mOnKeyListener;
        //布局的view
        public View mView;
        //布局的id
        public int mViewLayoutResId;

        //存值text
        SparseArray<CharSequence> textArray = new SparseArray<>();
        //存值 点击事件
        SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        //布局宽度
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        //动画
        public int mAnimations = 0;
        //位置
        public int mGravity = Gravity.CENTER;
        //高度
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;


        public AlertParams(Context context, int themeResId) {

            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        /**
         * 绑定和设置参数
         *
         * @param dialog
         */
        public void apply(AlertController dialog) {

            DialogViewHelper mDialogViewHelper = null;
            //1.设置布局 DialogViewHelper
            if (mViewLayoutResId != 0) {
                mDialogViewHelper = new DialogViewHelper(mContext, mViewLayoutResId);
            }

            if (mView != null) {
                mDialogViewHelper = new DialogViewHelper();
                mDialogViewHelper.setContentView(mView);
            }

            if (mDialogViewHelper == null) {
                throw new IllegalArgumentException("请设置布局setContentView()");
            }

            //给dialog设置布局
            dialog.getMyAlertDialog().setContentView(mDialogViewHelper.getContentView());

            //2.设置文本 在集合中
            int textSize = textArray.size();
            for (int i = 0; i < textSize; i++) {
                mDialogViewHelper.setText(textArray.keyAt(i), textArray.valueAt(i));
            }

            //3.设置点击事件 在集合中
            int clickSize = mClickArray.size();
            for (int i = 0; i < clickSize; i++) {
                mDialogViewHelper.setOnClickLisener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }

            //4.配置自定义效果 全屏 弹出动画 位置
            Window window = dialog.getmWindow();
            //位置
            window.setGravity(mGravity);

            //动画
            if (mAnimations != 0) {
                window.setWindowAnimations(mAnimations);
            }

            //宽度
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);


        }

    }
}
