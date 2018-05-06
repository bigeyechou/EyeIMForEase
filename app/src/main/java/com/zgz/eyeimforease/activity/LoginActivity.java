package com.zgz.eyeimforease.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zgz.eyeimforease.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 眼神 on 2018/5/5.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.et_login_account)
    EditText etLoginAccount;
    @Bind(R.id.et_login_psw)
    EditText etLoginPsw;
    @Bind(R.id.btn_register)
    Button btnRegister;
    @Bind(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initLayout();
    }

    private void initLayout() {
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }


    /**
     * 注册IM
     */
    private void registerIM() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(etLoginAccount.getText().toString().trim(),
                            etLoginPsw.getText().toString().trim());
                } catch (HyphenateException e) {
                    Log.e("IM", "注册失败");
                }
            }
        }).start();
    }

    private void loginIM() {
        EMClient.getInstance().login(etLoginAccount.getText().toString().trim(),
                etLoginPsw.getText().toString().trim(), new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.e("IM", "登录失败");
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                loginIM();
                break;
            case R.id.btn_register:
                registerIM();
                break;
        }
    }
}
