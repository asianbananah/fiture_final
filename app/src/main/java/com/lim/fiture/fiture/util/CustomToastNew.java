package com.lim.fiture.fiture.util;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customtoast.chen.customtoast.CustomToast;

/**
 * Created by User on 10/10/2018.
 */

public class CustomToastNew extends CustomToast {
    private Context context;
    private int backColor;
    private int textColor;

    public CustomToastNew(Context context) {
        super(context);
        this.backColor = context.getResources().getColor(com.developer.chen.customtoast.R.color.colorPrimary);
        this.textColor = context.getResources().getColor(com.developer.chen.customtoast.R.color.white);
        this.context = context;
    }

    @Override
    public void showSuccessToast(String text) {
        if(this.context != null) {
            LayoutInflater inflater = ((Activity)this.context).getLayoutInflater();
            View toastRoot = inflater.inflate(com.developer.chen.customtoast.R.layout.custom_toast, (ViewGroup)null);
            LinearLayout linearLayout = (LinearLayout)toastRoot.findViewById(com.developer.chen.customtoast.R.id.whole_layout);
            linearLayout.setBackgroundColor(this.backColor);
            TextView toastText = (TextView)toastRoot.findViewById(com.developer.chen.customtoast.R.id.toast_Tv);
            toastText.setText(text);
            toastText.setTextColor(this.textColor);
            ImageView toastImage = (ImageView)toastRoot.findViewById(com.developer.chen.customtoast.R.id.toast_Iv);
            toastImage.setImageResource(com.developer.chen.customtoast.R.drawable.icn_success);
            TextView toastSuccessTv = (TextView)toastRoot.findViewById(com.developer.chen.customtoast.R.id.toast_successTv);
            toastSuccessTv.setText("Success");
            toastSuccessTv.setTextColor(this.textColor);
            Toast toast = new Toast(this.context);
            toast.setView(toastRoot);
            toast.setGravity(23, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
