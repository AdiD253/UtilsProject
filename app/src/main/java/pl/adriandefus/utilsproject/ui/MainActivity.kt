package pl.adriandefus.utilsproject.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import pl.adriandefus.utilsproject.R
import pl.adriandefus.utilsproject.di.viewmodel.UtilsViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: UtilsViewModelFactory

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initVM()

        helloWorldButton.setOnClickListener {
            viewModel.toggleAnimation()
        }
    }

    private fun initVM() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.animationStatus.observe(this, Observer {
            when (it?.status) {
                Status.ACTIVE -> lottieView.resumeAnimation()
                Status.INACTIVE -> lottieView.pauseAnimation()
            }
            Toast.makeText(this, it?.statusInfo, Toast.LENGTH_SHORT).show()
        })
    }
}
