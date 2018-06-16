package journaler.ollie.study.journaler.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import journaler.ollie.study.journaler.R
import journaler.ollie.study.journaler.fragment.ItemsFragment
import journaler.ollie.study.journaler.navigation.NavigationDrawerAdapter
import journaler.ollie.study.journaler.navigation.NavigationDrawerItem
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity() {

    override val tag = "MAIN ACTIVITY"
    override fun getLayout() = R.layout.activity_main
    override fun getActivityTile() = R.string.app_name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pager.adapter = ViewPagerAdapter(supportFragmentManager)

        val menuItem =  mutableListOf<NavigationDrawerItem>()
        val today = NavigationDrawerItem(
            getString(R.string.today),
            Runnable {
                pager.setCurrentItem(0, true)
            }
        )

        val next7Days = NavigationDrawerItem(
            getString(R.string.next_seven_days),
            Runnable {
                pager.setCurrentItem(1, true)
            }
        )

        val todos = NavigationDrawerItem(
            getString(R.string.todos),
            Runnable {
                pager.setCurrentItem(2, true)
            }
        )

        val notes = NavigationDrawerItem(
            getString(R.string.notes),
            Runnable {
                pager.setCurrentItem(3, true)
            }
        )
        menuItem.add(today)
        menuItem.add(next7Days)
        menuItem.add(todos)
        menuItem.add(notes)

        val navigationDrawerAdapter = NavigationDrawerAdapter(this, menuItem)
        left_drawer.adapter = navigationDrawerAdapter
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
