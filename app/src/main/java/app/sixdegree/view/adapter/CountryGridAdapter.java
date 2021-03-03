package app.sixdegree.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.sixdegree.R;
import app.sixdegree.databinding.CountryTextBinding;
import app.sixdegree.model.CountryModel;
import app.sixdegree.model.roomdb.Country;

public class CountryGridAdapter extends RecyclerView.Adapter<CountryGridAdapter.MyViewHolder> {


    Context mContext;
    boolean isCountry;
    ArrayList<Country> dataList = new ArrayList<>();

    int width = 0;
    private MyViewHolder holder;
    private int position;

    public CountryGridAdapter(Context context, ArrayList<Country> dataList, boolean isCountry, int width) {
        this.mContext = context;
        this.dataList = dataList;
        this.isCountry = isCountry;
        this.width = width;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CountryTextBinding itemRowBinding;

        public MyViewHolder(CountryTextBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_text, parent, false);
        CountryTextBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.country_text, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        this.holder = holder;
        this.position = position;


        if (isCountry) {
            Log.e("profilefragsta", "f--" + dataList.get(position).getFlag());
            Glide.with(mContext).load( dataList.get(position).getFlag())
                  //  .apply(new RequestOptions().override(width / 3))
                   // .apply(RequestOptions.circleCropTransform())
                    .into(holder.itemRowBinding.flag);
//            Picasso.with(holder.itemView.getContext())
//                    .load(dataList.get(position).getFlag())
//                    .resize(92, 65).into(holder.itemRowBinding.flag);
            holder.itemRowBinding.text.setVisibility(View.GONE);
            holder.itemRowBinding.flag.setVisibility(View.VISIBLE);
        } else {
            holder.itemRowBinding.flag.setVisibility(View.GONE);
            holder.itemRowBinding.text.setVisibility(View.VISIBLE);
            holder.itemRowBinding.text.setText(dataList.get(position).getName());
//            if (dataList.get(position).isSelected()) {
//                holder.itemRowBinding.text.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
//            } else {
//                holder.itemRowBinding.text.setTextColor(ContextCompat.getColor(mContext, R.color.light_text));
//
//            }
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}