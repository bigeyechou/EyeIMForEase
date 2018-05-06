/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.styles.EaseMessageListItemStyle;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseChatMessageList.MessageListItemClickListener;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.presenter.EaseChatBigExpressionPresenter;
import com.hyphenate.easeui.widget.presenter.EaseChatFilePresenter;
import com.hyphenate.easeui.widget.presenter.EaseChatImagePresenter;
import com.hyphenate.easeui.widget.presenter.EaseChatLocationPresenter;
import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter;
import com.hyphenate.easeui.widget.presenter.EaseChatTextPresenter;
import com.hyphenate.easeui.widget.presenter.EaseChatVideoPresenter;
import com.hyphenate.easeui.widget.presenter.EaseChatVoicePresenter;
import com.hyphenate.easeui.widget.presenter.EyeChatCardPresenter;

public class EaseMessageAdapter extends BaseAdapter{

	private final static String TAG = "msg";

	private Context context;
	
	private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
	private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
    private static final int HANDLER_MESSAGE_SEEK_TO = 2;

	private static final int MESSAGE_TYPE_RECV_TXT = 0;
	private static final int MESSAGE_TYPE_SENT_TXT = 1;
	private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
	private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
	private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
	private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
	private static final int MESSAGE_TYPE_SENT_VOICE = 6;
	private static final int MESSAGE_TYPE_RECV_VOICE = 7;
	private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
	private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
	private static final int MESSAGE_TYPE_SENT_FILE = 10;
	private static final int MESSAGE_TYPE_RECV_FILE = 11;
	private static final int MESSAGE_TYPE_SENT_EXPRESSION = 12;
	private static final int MESSAGE_TYPE_RECV_EXPRESSION = 13;
	//TODO 自定义消息 卡片 定义接收、发送消息类型
	private static final int MESSAGE_TYPE_SEND_CARD = 14;
	private static final int MESSAGE_TYPE_RECV_CARD = 15;

	
	public int itemTypeCount; 
	
	// reference to conversation object in chatsdk
	private EMConversation conversation;
	EMMessage[] messages = null;
	
    private String toChatUsername;

    private MessageListItemClickListener itemClickListener;
    private EaseCustomChatRowProvider customRowProvider;
    
    private boolean showUserNick;
    private boolean showAvatar;
    private Drawable myBubbleBg;
    private Drawable otherBuddleBg;

    private ListView listView;
	private EaseMessageListItemStyle itemStyle;

	public EaseMessageAdapter(Context context, String username, int chatType, ListView listView) {
		this.context = context;
		this.listView = listView;
		toChatUsername = username;
		this.conversation = EMClient.getInstance().chatManager().getConversation(username, EaseCommonUtils.getConversationType(chatType), true);
	}

	Handler handler = new Handler() {
		private void refreshList() {
			// you should not call getAllMessages() in UI thread
			// otherwise there is problem when refreshing UI and there is new message arrive
			java.util.List<EMMessage> var = conversation.getAllMessages();
			messages = var.toArray(new EMMessage[var.size()]);
			conversation.markAllMessagesAsRead();
			notifyDataSetChanged();
		}
		
		@Override
		public void handleMessage(android.os.Message message) {
			switch (message.what) {
			case HANDLER_MESSAGE_REFRESH_LIST:
				refreshList();
				break;
			case HANDLER_MESSAGE_SELECT_LAST:
                if (messages != null && messages.length > 0) {
	                listView.setSelection(messages.length - 1);
                }
                break;
            case HANDLER_MESSAGE_SEEK_TO:
	            int position = message.arg1;
	            listView.setSelection(position);
                break;
			default:
				break;
			}
		}
	};

	public void refresh() {
		if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
			return;
		}
		android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
		handler.sendMessage(msg);
	}
	
	/**
     * refresh and select the last
     */
    public void refreshSelectLast() {
        final int TIME_DELAY_REFRESH_SELECT_LAST = 100;
        handler.removeMessages(HANDLER_MESSAGE_REFRESH_LIST);
        handler.removeMessages(HANDLER_MESSAGE_SELECT_LAST);
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_REFRESH_LIST, TIME_DELAY_REFRESH_SELECT_LAST);
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_SELECT_LAST, TIME_DELAY_REFRESH_SELECT_LAST);
    }
    
    /**
     * refresh and seek to the position
     */
    public void refreshSeekTo(int position) {
	    handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
    }

	public EMMessage getItem(int position) {
		if (messages != null && position < messages.length) {
			return messages[position];
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}
	
	/**
     * get count of messages
     */
    public int getCount() {
        return messages == null ? 0 : messages.length;
    }
	
	/**
	 * get number of message type, here 14 = (EMMessage.Type) * 2
	 * TODO 增加了卡片的 接收和发送 消息类型 所以再+2
	 */
	public int getViewTypeCount() {
	    if(customRowProvider != null && customRowProvider.getCustomChatRowTypeCount() > 0){
	        return customRowProvider.getCustomChatRowTypeCount() + 14 +2;
	    }
        return 14 + 2;
    }
	

	/**
	 * get type of item
	 * TODO 增加了卡片的 接收和发送 消息类型 所以再+2
	 *
	 */
	public int getItemViewType(int position) {
		EMMessage message = getItem(position); 
		if (message == null) {
			return -1;
		}
		
		if(customRowProvider != null && customRowProvider.getCustomChatRowType(message) > 0){
		    return customRowProvider.getCustomChatRowType(message) + 13 + 2;
		}
		//TODO 定义的卡片类型是EMMessage.Type.TXT ，所以加判断
		if (message.getType() == EMMessage.Type.TXT) {
		    if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
		        return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_EXPRESSION : MESSAGE_TYPE_SENT_EXPRESSION;
		    }else if (message.getBooleanAttribute("cards",false)){
		    	return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_CARD : MESSAGE_TYPE_SEND_CARD;
			}
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
		}
		if (message.getType() == EMMessage.Type.IMAGE) {
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;

		}
		if (message.getType() == EMMessage.Type.LOCATION) {
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
		}
		if (message.getType() == EMMessage.Type.VOICE) {
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
		}
		if (message.getType() == EMMessage.Type.VIDEO) {
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
		}
		if (message.getType() == EMMessage.Type.FILE) {
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
		}

		return -1;// invalid
	}
	//TODO 设置消息类型 TXT-->创建 ChatRow（EyeChatCard） 和 ChatPresenter(EyeChatCardPresenter)
	protected EaseChatRowPresenter createChatRowPresenter(EMMessage message, int position) {
        if(customRowProvider != null && customRowProvider.getCustomChatRow(message, position, this) != null){
			return customRowProvider.getCustomChatRow(message, position, this);
        }

        EaseChatRowPresenter presenter = null;

        switch (message.getType()) {
        case TXT:
            if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
				presenter = new EaseChatBigExpressionPresenter();
            }else if (message.getBooleanAttribute("cards",false)){
            	presenter = new EyeChatCardPresenter();
			}else{
				presenter = new EaseChatTextPresenter();
            }
            break;
        case LOCATION:
        	presenter = new EaseChatLocationPresenter();
            break;
        case FILE:
        	presenter = new EaseChatFilePresenter();
            break;
        case IMAGE:
        	presenter = new EaseChatImagePresenter();
            break;
        case VOICE:
        	presenter = new EaseChatVoicePresenter();
            break;
        case VIDEO:
        	presenter = new EaseChatVideoPresenter();
            break;
        default:
            break;
        }

        return presenter;
    }
    

	@SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {
		EMMessage message = getItem(position);

		EaseChatRowPresenter presenter = null;

		if (convertView == null) {
			presenter = createChatRowPresenter(message, position);
			convertView = presenter.createChatRow(context, message, position, this);
			convertView.setTag(presenter);
		} else {
			presenter = (EaseChatRowPresenter) convertView.getTag();
		}

		presenter.setup(message, position, itemClickListener, itemStyle);

		return convertView;
	}


	public void setItemStyle(EaseMessageListItemStyle itemStyle){
		this.itemStyle = itemStyle;
	}


    public void setItemClickListener(MessageListItemClickListener listener){
	    itemClickListener = listener;
	}
	
	public void setCustomChatRowProvider(EaseCustomChatRowProvider rowProvider){
	    customRowProvider = rowProvider;
	}


    public boolean isShowUserNick() {
        return showUserNick;
    }


    public boolean isShowAvatar() {
        return showAvatar;
    }


    public Drawable getMyBubbleBg() {
        return myBubbleBg;
    }


    public Drawable getOtherBubbleBg() {
        return otherBuddleBg;
    }

}
