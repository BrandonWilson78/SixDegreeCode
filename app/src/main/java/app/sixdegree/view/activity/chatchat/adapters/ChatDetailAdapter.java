package app.sixdegree.view.activity.chatchat.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.sixdegree.R;
 import app.sixdegree.network.responses.getallchats.Datum;
import app.sixdegree.utils.AppSession;



public class ChatDetailAdapter extends RecyclerView.Adapter<ChatDetailAdapter.MyViewHolder> {

    Context context;
    private List<Datum> chatUserList;
    private AppSession mSession;

    public ChatDetailAdapter(List<Datum> dataList, Context context) {
        this.chatUserList = dataList;
        this.context = context;
        mSession = new AppSession(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.chat_detail_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Datum chatM = chatUserList.get(position);

//        if (chatM.getSender_image() != null) {
//            Picasso.get().load(chatM.getSender_image() )
//                    .transform(new CircleTransform())
//                    .into(holder.img_detail);
//        }else {
//
//        }
        Log.wtf("id by ", "--" + chatM.getFromUserId() +
                "--id--userid-" + mSession.getData().getId());



        if (String.valueOf(mSession.getData().getId()).equals(String.valueOf(chatM.getFromUserId()))) {

            holder.l1.setVisibility(View.GONE);
            holder.l2.setVisibility(View.VISIBLE);
//            holder.img_detail.setVisibility(View.GONE);
            holder.time1.setText(chatM.getCreatedAt());
            holder.msg1.setText(chatM.getMessage());
            holder.sender.setText("");

        } else {
            holder.l2.setVisibility(View.GONE);
            holder.l1.setVisibility(View.VISIBLE);
         //   holder.img_detail.setVisibility(View.VISIBLE);
            holder.time.setText(chatM.getCreatedAt());
            holder.msg.setText(chatM.getMessage());
            holder.sender.setText(chatM.getFromName());
        }

    }

    @Override
    public int getItemCount() {
        return chatUserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView msg;
        TextView msg1;
        ImageView img_detail;
        TextView sender;
        TextView time1;
        TextView time;
        LinearLayout l1;
        LinearLayout l2;

        public MyViewHolder(View view) {
            super(view);
            msg = view.findViewById(R.id.msg);
            msg1 = view.findViewById(R.id.msg1);
            time1 = view.findViewById(R.id.time1);
            time = view.findViewById(R.id.time);
            sender = view.findViewById(R.id.sender);
       //     img_detail = view.findViewById(R.id.img_detail);

            l1 = view.findViewById(R.id.l1);
            l2 = view.findViewById(R.id.l2);


        }
    }
}