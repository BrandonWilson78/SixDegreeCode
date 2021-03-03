package app.sixdegree.view.activity.home_module;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import app.sixdegree.R;
import app.sixdegree.view.activity.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindFriendmap extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.bottom_lay)
    RelativeLayout bottomLay;
    @BindView(R.id.btns)
    LinearLayout btns;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friendmap);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        onBackPressed();
    }
}
