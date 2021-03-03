package app.sixdegree.view.activity.home_module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import app.sixdegree.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.MyViewHolder> {


    Context mContext;
    int w;


    public RequestsAdapter(Context context, int width) {
        this.mContext = context;
        this.w = width;
    }

    @OnClick(R.id.parent)
    public void onViewClicked() {
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.name_comment)
        TextView nameComment;
        @BindView(R.id.city)
        TextView city;
        @BindView(R.id.parent)
        RelativeLayout parent;
        @BindView(R.id.divider)
        View divider;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
       /*     Picasso.get().load("https://as1.ftcdn.net/jpg/02/31/37/90/500_F_231379059_EtPET9klTXk0u2NSLQN8mqdHKF82JamN.jpg")
                    .resize(w, w)
                    .transform(new CircleTransform())
                    .into(image);*/


        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_row
                        , parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
     /*   if (position == 0) {

            AppUtils.htmlTextView("<b>Saar & Paul</b> Amazing!", holder.nameComment);
        } else {
            AppUtils.htmlTextView("<b>Maxine Meunich</b> " + mContext.getString(R.string.dummy_text_small), holder.nameComment);
        }*/
        if (position==getItemCount()-1){
            holder.divider.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return 2;
    }
}