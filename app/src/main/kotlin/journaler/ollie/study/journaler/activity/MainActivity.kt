package journaler.ollie.study.journaler.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import journaler.ollie.study.journaler.R
import journaler.ollie.study.journaler.fragment.ItemsFragment
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity() {

    override val tag = "MAIN ACTIVITY"
    override fun getLayout() = R.layout.activity_main
    override fun getActivityTile() = R.string.app_name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pager.adapter = ViewPagerAdapter(supportFragmentManager)

    }

    private class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

        override fun getItem(position: Int): Fragment {
            return ItemsFragment()
        }

        override fun getCount(): Int {
            return 5
        }
}

}
