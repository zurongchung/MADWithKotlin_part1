package journaler.ollie.study.journaler.activity

import android.os.Bundle
import journaler.ollie.study.journaler.R
import kotlinx.android.synthetic.main.activity_todo.*

class TodoActivity : ItemActivity() {
    override val tag = "TODO ACTIVITY"
    override fun getLayout() = R.layout.activity_todo

    companion object {
        val EXTRA_DATE = "DATE"
        val EXTRA_TIME = "TIME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = intent.extras
        data?.let {
            val date = data.getString(EXTRA_DATE, "")
            val time = data.getString(EXTRA_TIME, "")
            pick_date.text = date
            pick_time.text = time
        }
    }

}
