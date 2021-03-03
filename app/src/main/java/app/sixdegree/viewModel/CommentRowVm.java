package app.sixdegree.viewModel;

import android.widget.ImageView;

import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;

import com.google.android.material.imageview.ShapeableImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.sixdegree.network.responses.fetchcomments.Data;
import app.sixdegree.utils.AppUtils;

public class CommentRowVm extends BaseVm {
    Data data;
    @Bindable
    String comment = "";
    @Bindable
    String date;
    @Bindable
    String name = "";
    @Bindable
    String commentimg;

    public CommentRowVm(Data data) {
        this.data = data;
    }

    @BindingAdapter({"bind:commentimg"})
    public static void loadProfile(ShapeableImageView view, String profilePic) {
        if (profilePic != null) {
            AppUtils.loadPicasso(profilePic,view);
        }

    }
    public String getCommentimg() {
        return data.getUser().getProfile_image_path() + data.getUser().getProfile_image();
    }

    public void setCommentimg(String commentimg) {
        this.commentimg = commentimg;
        notifyPropertyChanged(BR.commentimg);
    }

    public String getName() {
        return data.getUser().getName() != null ? data.getUser().getName() : "";
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getDate() {
        return changeDateFormat(data.getCreated_at());
    }

    public void setDate(String date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    public String getComment() {
        return data.getComment();
    }

    public void setComment(String comment) {
        this.comment = comment;
        notifyPropertyChanged(BR.comment);
    }


    public  String setDateString(String date_) {
        Date dd = null;
        if(date_ != null){


            String date = date_;
            SimpleDateFormat input = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            SimpleDateFormat output = new SimpleDateFormat( "MMMM dd" );
            try {
                dd = input.parse( date );                 // parse input
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return String.valueOf( dd );
    }
}
