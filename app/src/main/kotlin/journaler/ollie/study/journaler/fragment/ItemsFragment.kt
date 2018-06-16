package journaler.ollie.study.journaler.fragment


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import journaler.ollie.study.journaler.R
import journaler.ollie.study.journaler.activity.NoteActivity
import journaler.ollie.study.journaler.activity.TodoActivity
import journaler.ollie.study.journaler.model.MODE
import java.text.SimpleDateFormat
import java.util.*


class ItemsFragment : BaseFragment() {
    override val logTag = "ITEMS FRAGMENT"
    override fun getLayout(): Int {
        return R.layout.fragment_items
    }

    private val NOTE_REQUEST = 0
    private val TODO_REQUEST = 1

    private fun animate(btn: FloatingActionButton, expand: Boolean = true) {
        val animation1 = ObjectAnimator.ofFloat(btn,
            "scaleX",
            if (expand) 1.5f else 1.0f)
        animation1.duration = 2000
        animation1.interpolator = BounceInterpolator()

        val animation2 = ObjectAnimator.ofFloat(btn,
            "scaleY",
            if (expand) 1.5f else 1.0f)
        animation2.duration = 2000
        animation2.interpolator = BounceInterpolator()

        val animation3 = ObjectAnimator.ofFloat(btn,
            "alpha",
            if (expand) 0.3f else 1.0f)
        animation3.duration = 500
        animation3.interpolator = AccelerateInterpolator()

        val set = AnimatorSet()
        set.play(animation1).with(animation2).before(animation3)
        set.start()


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayout(), container, false)
        var btn = view.findViewById<FloatingActionButton>(R.id.new_item)
        btn.setOnClickListener {

            animate(btn)

            val items = arrayOf(
                getString(R.string.todos),
                getString(R.string.notes)
            )

            val builder =
                AlertDialog.Builder(this@ItemsFragment.context!!)
                    .setTitle(R.string.choose_a_type)
                    .setCancelable(true)
                    .setOnCancelListener {
                        animate(btn, false)
                    }
                    .setItems(items) { _, which ->
                        when (which) {
                            0-> {
                                openCreateTodo()
                            }
                            1 -> {
                                openCreateNote()
                            }
                            else -> Log.v(logTag, "Unknown options selected [ $which ]")
                        }
                    }
            builder.show()

        }
        return view
    }

    private fun openCreateNote() {
        val intent = Intent(context, NoteActivity::class.java)
        val data = Bundle()
        data.putInt(MODE.EXTRA_KEY, MODE.CREATE.mode)
        intent.putExtras(data)
        startActivityForResult(intent, NOTE_REQUEST)
    }

    private fun openCreateTodo() {
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("MMM dd YYYY", Locale.ENGLISH)
        val timeFormat = SimpleDateFormat("MM:HH", Locale.ENGLISH)

        val intent = Intent(context, TodoActivity::class.java)
        val data = Bundle()
        data.putInt(MODE.EXTRA_KEY, MODE.CREATE.mode)
        data.putString(TodoActivity.EXTRA_DATE, dateFormat.format(date))
        data.putString(TodoActivity.EXTRA_TIME, timeFormat.format(date))
        intent.putExtras(data)
        startActivityForResult(intent, TODO_REQUEST)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TODO_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(logTag, "We created new TODO.")
                } else {
                    Log.w(logTag, "We didn't create new TODO.")
                }
            }
            NOTE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(logTag, "We created new NOTE.")
                } else {
                    Log.w(logTag, "We didn't create new NOTE.")
                }
            }
        }
    }


}
