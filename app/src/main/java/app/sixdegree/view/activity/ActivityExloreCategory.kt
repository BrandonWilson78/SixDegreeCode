package app.sixdegree.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import app.sixdegree.R
import app.sixdegree.databinding.ActivityExloreCategoryBinding
import app.sixdegree.utils.AppSession
import app.sixdegree.view.activity.home_module.HomeActivity
import app.sixdegree.viewModel.ExploreCategoryVm
import io.reactivex.android.schedulers.AndroidSchedulers
class ActivityExloreCategory : AppCompatActivity() {
    var vm: ExploreCategoryVm? = null
    lateinit var binding: ActivityExloreCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ExploreCategoryVm()
        vm!!.sessionInstance = AppSession(applicationContext)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exlore_category)
        binding.viewModel = vm
        vm!!.toolbarTitle = "Choose Interest"
        binding.executePendingBindings()
        binding.exploreRve.layoutManager = GridLayoutManager(this, 1)
        vm!!.getExloreList()
        vm!!.selected.subscribeOn(AndroidSchedulers.mainThread()).doOnNext {
            vm!!.isBtnVisible = it.size > 0
        }.subscribe()
        binding.back.setOnClickListener({
            onBackPressed()
        })
        vm!!.isSubmitted.subscribeOn(AndroidSchedulers.mainThread()).doOnNext {
            handleit(it)
        }.subscribe()
    }

    private fun handleit(it: Boolean?) {
        if (it!!) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}
