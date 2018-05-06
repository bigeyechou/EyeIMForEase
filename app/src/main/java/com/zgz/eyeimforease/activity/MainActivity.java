package com.zgz.eyeimforease.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.zgz.eyeimforease.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Bind(R.id.et_with_who)
    EditText etWithWho;
    @Bind(R.id.btn_send_chat)
    Button btnSendChat;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initLayout();
    }

    private void initLayout() {
        etWithWho.setOnClickListener(this);
        btnSendChat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_chat:
                Intent intent = new Intent(MainActivity.this, IMChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, etWithWho.getText().toString().trim());
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
                startActivity(intent);
                break;
        }
    }
}


