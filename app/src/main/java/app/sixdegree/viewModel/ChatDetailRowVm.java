//package app.sixdegree.viewModel;
//
//import androidx.databinding.Bindable;
//
//import app.sixdegree.BR;
//import app.sixdegree.network.responses.login_.Data;
//import app.sixdegree.utils.AppSession;
//
//public class ChatDetailRowVm extends BaseVm {
//
//    app.sixdegree.network.responses.getallchats.Data data;
//
//    public String getSessionid() {
//        return sessionid;
//    }
//
//    public void setSessionid( String sessionid ) {
//        this.sessionid = sessionid;
//        notifyPropertyChanged( BR.sessionid );
//    }
//@Bindable
//    String sessionid="";
//
//    public String getSentbyid() {
//        return String.valueOf(  data.getFrom_user_id());
//    }
//
//    public void setSentbyid( String sentbyid ) {
//        this.sentbyid = sentbyid;
//        notifyPropertyChanged( BR.sentbyid );
//    }
//@Bindable
//    String sentbyid="";
//
//     public ChatDetailRowVm( app.sixdegree.network.responses.getallchats.Data data,String sessionid ) {
//         this.data=data;
//        this.sessionid=sessionid;
//    }
//
//    @Bindable
//    String sendername="";
//    @Bindable
//    String sendermsg="";
//
//    public String getSendername() {
//        return data.getFrom_name();
//    }
//
//    public void setSendername( String sendername ) {
//        this.sendername = sendername;
//    }
//
//    public String getSendermsg() {
//        return data.getMessage();
//    }
//
//    public void setSendermsg( String sendermsg ) {
//        this.sendermsg = sendermsg;
//    }
//
//    public String getSendermsgtime() {
//        return data.getCreated_at()==null?"":data.getCreated_at();
//    }
//
//    public void setSendermsgtime( String sendermsgtime ) {
//        this.sendermsgtime = sendermsgtime;
//    }
//
//    public String getReceivermsg() {
//        return data.getMessage();
//    }
//
//    public void setReceivermsg( String receivermsg ) {
//        this.receivermsg = receivermsg;
//    }
//
//    public String getReceivermsgtime() {
//        return data.getCreated_at()==null?"":data.getCreated_at();
//    }
//
//    public void setReceivermsgtime( String receivermsgtime ) {
//        this.receivermsgtime = receivermsgtime;
//    }
//
//    @Bindable
//    String sendermsgtime="";
//    @Bindable
//    String receivermsg="";
//    @Bindable
//    String receivermsgtime="";
//}
