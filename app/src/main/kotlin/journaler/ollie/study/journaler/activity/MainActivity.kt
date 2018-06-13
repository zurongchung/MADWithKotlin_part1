package journaler.ollie.study.journaler.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import journaler.ollie.study.journaler.R

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override val tag = "Main Activity"
    override fun getLayout(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        Log.v(tag, "[ ON CREATE 1]")
    }

}
