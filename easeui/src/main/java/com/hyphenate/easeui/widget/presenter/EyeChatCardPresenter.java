package com.hyphenate.easeui.widget.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EyeChatCard;

/**
 * Created by 眼神 on 2018/5/6.
 */

public class EyeChatCardPresenter extends  EaseChatRowPresenter{

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return  new EyeChatCard(cxt, message, position, adapter);

    }

    @Override
    public void onBubbleClick(EMMessage message) {

    }
}
