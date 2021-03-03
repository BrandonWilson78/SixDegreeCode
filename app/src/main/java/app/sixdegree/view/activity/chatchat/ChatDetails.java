package app.sixdegree.view.activity.chatchat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.sixdegree.R;
 import app.sixdegree.network.responses.getallchats.Datum;
import app.sixdegree.network.responses.getallchats.GetAllChatsRes;
import app.sixdegree.network.responses.receivemessageres.ReceiveMsgRes;
import app.sixdegree.network.responses.sendmessage.SendMessageRes;
import app.sixdegree.utils.AppSession;
import app.sixdegree.view.activity.BaseActivity;
import app.sixdegree.view.activity.chatchat.adapters.ChatDetailAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;



public class ChatDetails extends BaseActivity {


    EditText sendmsg;
    RecyclerView chats_rv;
    ChatDetailAdapter chatDetailAdapter;
ProgressBar pbar;
    List<Datum> chatUserList = new ArrayList<>();
    Timer timer;
    MyTimerTask myTimerTask;
    TextView txt_top_header;

    ImageView sendBtn, user_img;
    Context context = this;
AppSession appSession;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        txt_top_header = findViewById(R.id.txt_top_header);
appSession=new AppSession( context );
        sendBtn = findViewById(R.id.sendBtn);

        sendmsg = findViewById(R.id.sendmsg);
        pbar = findViewById(R.id.pbar);
        user_img = findViewById(R.id.user_img);
        chats_rv = findViewById(R.id.chats_rv);

        txt_top_header.setText(getIntent().getStringExtra("name"));
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });
        if (timer != null) {
            timer.cancel();
        }

        Glide.with(this)
                .load(getIntent().getStringExtra("image"))
                .centerCrop()
                .apply(new RequestOptions().override( getScreenWidth() / 3, getScreenWidth() / 3))
                .apply( RequestOptions.circleCropTransform())
                 .into(user_img);



        chats_rv.setLayoutManager(new LinearLayoutManager(this));
        sendmsg.setOnEditorActionListener(new DoneOnEditorActionListener());

        chatDetailAdapter = new ChatDetailAdapter(chatUserList, this);
        chats_rv.setAdapter(chatDetailAdapter);
        chatDetailAdapter.notifyDataSetChanged();
        if (!isNetworkConnected()) {
            createInternetMsgToast();
            return;
        }
        //fetch messages
        fetchMessages();


        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1000, 3000);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sendmsg.getText().toString().isEmpty()) {
                    Datum datum = new Datum();
                    datum.setId(0l);
                    datum.setConversationId(Long.parseLong(getIntent().getStringExtra("chat_group_id")));
                    datum.setFromUserId(    Long.valueOf(appSession.getData().getId()));
                    datum.setCreatedAt("");
                    datum.setMessage(sendmsg.getText().toString());
                    Date d = new Date();
                    CharSequence s = DateFormat.format("yyyy-MM-dd HH:mm:ss", d.getTime());
                    datum.setCreatedAt(String.valueOf(s));

                    datum.setFromName(new AppSession(context).getData().getName() );
              chatUserList.add(datum);
                    chatDetailAdapter.notifyDataSetChanged();
                    if (chatUserList.size() > 0) {
                        chats_rv.scrollToPosition(chatUserList.size() - 1);
                    }
                    if (!isNetworkConnected()) {
                        createInternetMsgToast();
                        return;
                    }
                    sendMsg(sendmsg.getText().toString());
                    sendmsg.setText("");
                    hideKeyboard(ChatDetails.this);


                }
            }
        });
    }
    public static void hideKeyboard( Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    protected void onResume() {

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1000, 3000);
        super.onResume();
    }

    public void sendMsg(String msg) {

        HashMap<String, String> map = new HashMap<>();
        map.put("conversation_id", getIntent().getStringExtra("chat_group_id"));
        map.put("from_user_id", String.valueOf(appSession.getData().getId() ));
        map.put("to_user_id", getIntent().getStringExtra( "to_user_id" ) );
        map.put("message", msg);

        Observer<SendMessageRes> observer = apiService.sendMessage(getSession()  .getToken(),map)
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<SendMessageRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(SendMessageRes res) {
                        if (!res.getStatus()) {
                            chatUserList.remove(chatUserList.size() - 1);
                            chatDetailAdapter.notifyDataSetChanged();

                    }


                }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("err", "=" + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void fetchMessages() {
     pbar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("from_user_id", getIntent().getStringExtra("from_user_id"));
        map.put("to_user_id", getIntent().getStringExtra("to_user_id"));
        Observer<GetAllChatsRes> observer = apiService.getAllChatMessages(getSession().getToken(),map)
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<GetAllChatsRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(GetAllChatsRes res) {
                        if (res.getStatus()) {
                            chatUserList.addAll(res.getData());
                            chatDetailAdapter.notifyDataSetChanged();
                            if (chatUserList.size() > 0) {
                                chats_rv.scrollToPosition(chatUserList.size() - 1);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        pbar.setVisibility(View.GONE);
                        Log.e("err", "=" + e);
                    }

                    @Override
                    public void onComplete() {
                        pbar.setVisibility(View.GONE);
                    }
                });

    }

    public void recieveMsg() {


        HashMap<String, String> map = new HashMap<>();
        map.put("conversation_id", getIntent().getStringExtra("chat_group_id"));
        map.put("to_user_id", String.valueOf( appSession.getData().getId() ));
        Observer<GetAllChatsRes> observer = apiService.receiveMsg(getSession().getToken(),map)
                .subscribeOn( Schedulers.io())
                .observeOn( AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<GetAllChatsRes>() {
                    @Override
                    public void onSubscribe( Disposable d) {

                    }

                    @Override
                    public void onNext(GetAllChatsRes res) {
                        if (res.getStatus()) {
                            chatUserList.addAll(res.getData());
                            chatDetailAdapter.notifyDataSetChanged();

                            if (chatUserList.size() > 0) {
                                chats_rv.scrollToPosition(chatUserList.size() - 1);
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("err", "=" + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    @Override
    public void onStop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }

    class DoneOnEditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        }
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (!isNetworkConnected()) {
                        createInternetMsgToast();
                        return;
                    }

                    recieveMsg();
                }
            });
        }

    }

}
