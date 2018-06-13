package journaler.ollie.study.journaler.activity

import journaler.ollie.study.journaler.R

class TodoActivity : ItemActivity() {
    override val tag = "TODO ACTIVITY"
    override fun getLayout() = R.layout.activity_todo

}
