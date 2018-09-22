package com.lwc.gaodetest.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.lwc.gaodetest.R;

/**
 * Created by lingwancai on
 * 2018/9/21 20:27
 */
public class MyAlertDialog extends Dialog {
    private AlertController mAlert;

    public MyAlertDialog(@NonNull Context context) {
        this(context, 0);
    }

    public MyAlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);

        mAlert = new AlertController(this, getWindow());
    }

    public static class Builder {

        public AlertController.AlertParams P;

        public Builder(Context context) {
            this(context, R.style.MyDialog);
        }

        public Builder(Context context, int themeResId) {

            P = new AlertController.AlertParams(context, themeResId);
        }

        /**
         * 1079         * Creates an {@link MyAlertDialog} with the arguments supplied to this
         * 1080         * builder.
         * 1081         * <p>
         * 1082         * Calling this method does not display the dialog. If no additional
         * 1083         * processing is needed, {@link #show()} may be called instead to both
         * 1084         * create and display the dialog.
         * 1085
         */
        public MyAlertDialog create() {
            // Context has already been wrapped with the appropriate theme.
            final MyAlertDialog dialog = new MyAlertDialog(P.mContext, P.mThemeResId);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        /**
         * 1103         * Creates an {@link MyAlertDialog} with the arguments supplied to this
         * 1104         * builder and immediately displays the dialog.
         * 1105         * <p>
         * 1106         * Calling this method is functionally identical to:
         * 1107         * <pre>
         * 1108         *     AlertDialog dialog = builder.create();
         * 1109         *     dialog.show();
         * 1110         * </pre>
         * 1111
         */
        public MyAlertDialog show() {
            final MyAlertDialog dialog = create();
            dialog.show();
            return dialog;
        }

        /**
         * 设置布局
         *
         * @param layoutid
         * @return
         */
        public Builder setContentView(int layoutid) {
            P.mView = null;
            P.mViewLayoutResId = layoutid;
            return this;
        }

        public Builder setText(int viewid, CharSequence text) {
            P.textArray.put(viewid, text);
            return this;
        }

        public Builder setOnclickLisener(int viewid, View.OnClickListener listener) {
            P.mClickArray.put(viewid, listener);
            return this;
        }

        /**
         * 683         * Sets whether the dialog is cancelable or not.  Default is true.
         * 684         *
         * 685         * @return This Builder object to allow for chaining of calls to set methods
         * 686
         */
        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        /**
         * 693         * Sets the callback that will be called if the dialog is canceled.
         * 694         *
         * 695         * <p>Even in a cancelable dialog, the dialog may be dismissed for reasons other than
         * 696         * being canceled or one of the supplied choices being selected.
         * 697         * If you are interested in listening for all cases where the dialog is dismissed
         * 698         * and not just when it is canceled, see
         * 699         * {@link #setOnDismissListener(android.content.DialogInterface.OnDismissListener) setOnDismissListener}.</p>
         * 700         * @see #setCancelable(boolean)
         * 701         * @see #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
         * 702         *
         * 703         * @return This Builder object to allow for chaining of calls to set methods
         * 704
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * 711         * Sets the callback that will be called when the dialog is dismissed for any reason.
         * 712         *
         * 713         * @return This Builder object to allow for chaining of calls to set methods
         * 714
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * 721         * Sets the callback that will be called if a key is dispatched to the dialog.
         * 722         *
         * 723         * @return This Builder object to allow for chaining of calls to set methods
         * 724
         */
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        //配置一些万能的参数
        public Builder setFullWidth() {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        /**
         *
         *
         * @param isAnimation
         * @return
         */
        public Builder setStartBottom(boolean isAnimation) {
            if (isAnimation){
                //动画
                P.mAnimations = R.style.mydialogstyle;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }


        /**
         * 设置宽高
         *
         * @param width
         * @param height
         * @return
         */
        public Builder setWidthAndHeight(int width, int height){
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        /**
         * 默认动画
         *
         * @return
         */
        public Builder adddefaultAnimation(){
            P.mAnimations = R.style.mydialogstyle;
            return this;
        }

        /**
         * 设置动画
         *
         * @param animation
         * @return
         */
        public Builder setAnimation(int animation){
            P.mAnimations = animation;
            return this;
        }


    }

}
