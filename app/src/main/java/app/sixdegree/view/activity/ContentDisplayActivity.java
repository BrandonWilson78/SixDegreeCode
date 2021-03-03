package app.sixdegree.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;

import app.sixdegree.R;
import app.sixdegree.databinding.ActivityContentDisplayBinding;
import app.sixdegree.viewModel.PageContentVm;

public class ContentDisplayActivity extends BaseActivity{
    PageContentVm pageContentVm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_content_display);

        pageContentVm= new PageContentVm(getIntent().getStringExtra("url"));
        ActivityContentDisplayBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_content_display);
        binding.setViewModel(pageContentVm);

View view=binding.getRoot();
        ImageView back_btn=view.findViewById( R.id.back_btn );
        back_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                onBackPressed();
            }
        } );
    }
}
