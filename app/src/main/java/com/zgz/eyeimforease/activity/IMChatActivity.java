package com.zgz.eyeimforease.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.hyphenate.easeui.ui.EaseChatFragment;
import com.zgz.eyeimforease.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 眼神 on 2018/5/5.
 */

public class IMChatActivity extends AppCompatActivity {


    @Bind(R.id.fl_chat_container)
    FrameLayout flChatContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_chat_layout);
        ButterKnife.bind(this);
        initLayout();
    }

    private void initLayout() {
        //使用easeUi的聊天界面
        EaseChatFragment chatFragment = new EaseChatFragment();
        //将参数传递到聊天界面
        chatFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction().add(R.id.fl_chat_container, chatFragment).commit();
    }


}
