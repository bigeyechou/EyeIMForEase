package com.zgz.eyeimforease.app;

import android.app.Application;
import android.content.Context;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;

/**
 * Created by 眼神 on 2018/5/5.
 */

public class EyeIMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化环信EaseUI
        EMOptions options = new EMOptions();
        //添加好友需要验证
        options.setAcceptInvitationAlways(false);
        EaseUI.getInstance().init(this,options);
        EMClient.getInstance().setDebugMode(true);//Debug模式打开书写日志

    }

    public static Context getContext(){
        return EyeIMApplication.getContext();
    }


}
