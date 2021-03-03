//package app.sixdegree.viewModel;
//
//import android.util.Log;
//import android.widget.ImageView;
//
//import androidx.databinding.Bindable;
//import androidx.databinding.BindingAdapter;
//import androidx.databinding.library.baseAdapters.BR;
//
//import java.util.HashMap;
//
//import app.sixdegree.network.responses.getallchats.GetAllChatsRes;
//import app.sixdegree.network.responses.getinboxres.GetInboxRes;
//import app.sixdegree.network.responses.login_.Data;
//import app.sixdegree.network.responses.sendmessage.SendMessageRes;
//import app.sixdegree.utils.AppSession;
//import app.sixdegree.utils.AppUtils;
//import app.sixdegree.view.activity.chatmodule.adapter.ChatDetailAdapter;
//import io.reactivex.Observer;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//
//public class ChatDetailVm extends BaseVm {
//AppSession appSession;
//
//    public ChatDetailAdapter getChatDetailAdapter() {
//        return chatDetailAdapter;
//    }
//
//    public void setChatDetailAdapter( ChatDetailAdapter chatDetailAdapter ) {
//        this.chatDetailAdapter = chatDetailAdapter;
//    }
//
//    ChatDetailAdapter chatDetailAdapter;
//
//    public String getConverstaion_id() {
//        return converstaion_id;
//    }
//
//    public void setConverstaion_id( String converstaion_id ) {
//        this.converstaion_id = converstaion_id;
//        notifyPropertyChanged( BR.converstaion_id );
//    }
//
//
//
//    public boolean isLoading() {
//        return loading;
//    }
//
//    public void setLoading( boolean loading ) {
//        this.loading = loading;
//        notifyPropertyChanged( BR.loading );
//    }
//
//    @Bindable
//            boolean loading;
//    @Bindable
//String converstaion_id="";
//    @Bindable
//String image="";
//    @Bindable
//String name="";
// AppSession sessiondata;
//
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken( String token ) {
//        this.token = token;
//        notifyPropertyChanged( BR.token );
//    }
//
//    @Bindable
// String token="";
//
//    public ChatDetailVm( AppSession appSession,String conversation_id, String name,String image) {
//        this.appSession=appSession;
//        this.image=image;
//        this.name=name;
//        this.converstaion_id=conversation_id;
//
//
//        chatDetailAdapter=new ChatDetailAdapter(String.valueOf(appSession.getData().getId()) );
//
//        //fetch messages
//        fetchMessages();
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName( String name ) {
//        this.name = name;
//        notifyPropertyChanged( BR.name );
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage( String image ) {
//        this.image = image;
//        notifyPropertyChanged( BR.image );
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage( String message ) {
//        this.message = message;
//        notifyPropertyChanged( BR.message );
//    }
//
//
//    @Bindable
//    String message;
//
//
//
//    @BindingAdapter({"bind:setuserimg"})
//    public static void loadfimage( ImageView view, String coverPic) {
//        if (coverPic != null) {
//            AppUtils.roundImageWithGlide(view, coverPic);
//        }
//    }
//
//
//
//
//    public void fetchMessages() {
//        setLoading(true);
//        HashMap<String, String> map = new HashMap<>();
//        map.put("conversation_id",getConverstaion_id() );
//        apiService.getAllChatMessages(appSession.getToken(),map).subscribeOn( Schedulers.io())
//                .observeOn( AndroidSchedulers.mainThread())
//                .subscribeWith(new Observer<GetAllChatsRes>() {
//                    @Override
//                    public void onSubscribe( Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(GetAllChatsRes res) {
//                        setErrorMsg(res.getMessage());
//                        if (res.getStatus()) {
//                            getChatDetailAdapter().setData( res.getData() );
//                            notifyChange();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        setLoading(false);
//
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        setLoading(false);
//                    }
//                });
//
//    }
//
//    public void sendMsg(String msg) {
////
////        HashMap<String, String> map = new HashMap<>();
////        map.put("conversation_id", getConverstaion_id());
////        map.put("from_user_id",);
////        map.put("message", getMessage());
////
////     apiService.sendMessage(getToken(),map)
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribeWith(new Observer<SendMessageRes>() {
////                    @Override
////                    public void onSubscribe(Disposable d) {
////
////                    }
////
////                    @Override
////                    public void onNext(SendMessageRes res) {
////                        if (!res.getStatus()) {
////                            chatUserList.remove(chatUserList.size() - 1);
////                            chatDetailAdapter.notifyDataSetChanged();
////                        }
////                    }
////
////                    @Override
////                    public void onError(Throwable e) {
////
////                        Log.e("err", "=" + e);
////                    }
////
////                    @Override
////                    public void onComplete() {
////
////                    }
////                });
//
//
//    }
//
//
//
//
//
//
//}
