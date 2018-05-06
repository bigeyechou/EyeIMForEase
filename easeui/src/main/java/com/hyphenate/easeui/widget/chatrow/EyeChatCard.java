package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.media.Image;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter;

/**
 * Created by 眼神 on 2018/5/6.
 * 自定义消息  卡片
 */

public class EyeChatCard extends EaseChatRow {
    private ImageView iv_head;//头像
    private TextView tv_name;//姓名
    private TextView tv_city;//城市

    public EyeChatCard(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.eye_row_received_cards : R.layout.eye_row_sent_cards,this);
    }

    @Override
    protected void onFindViewById() {
        iv_head = findViewById(R.id.iv_head_card);
        tv_name = findViewById(R.id.tv_name_card);
        tv_city = findViewById(R.id.tv_city_card);
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSetUpView() {
    }
}
