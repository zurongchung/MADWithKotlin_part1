package journaler.ollie.study.journaler.activity

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import journaler.ollie.study.journaler.R
import journaler.ollie.study.journaler.database.Db
import journaler.ollie.study.journaler.location.LocationProvider
import journaler.ollie.study.journaler.model.Note
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : ItemActivity() {
    override val tag = "NOTE ACTIVITY"
    override fun getLayout() = R.layout.activity_note
    private var mNote: Note? = null
    private var mLocation: Location? = null

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            updateNote()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

    private fun updateNote() {
        if (mNote == null) {
            if (!TextUtils.isEmpty(getNoteTitle()) && !TextUtils.isEmpty(getNoteContent())) {
                LocationProvider.subscribe(locationListener)
            } else {
                mNote?.title = getNoteTitle()
                mNote?.message = getNoteContent()

                val task = @SuppressLint("StaticFieldLeak")
                object : AsyncTask<Note, Void, Boolean>() {
                    override fun doInBackground(vararg params: Note?): Boolean {
                        if (!params.isEmpty()) {
                            val param = params[0]
                            param?.let {
                                return Db.NOTE.update(param) > 0
                            }
                        }
                        return false
                    }

                    override fun onPostExecute(result: Boolean?) {
                        result?.let {
                            if (result) {
                                Log.i(tag, "Note updated.")
                            } else {
                                Log.e(tag, "Note not updated.")
                            }
                        }
                    }
                }
                task.execute(mNote)
            }
        }
    }

    private fun getNoteContent(): String {
        return note_content.text.toString()
    }
    private fun getNoteTitle(): String {
        return note_title.text.toString()
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            location?.let {
                LocationProvider.unsubscribe(this)
                mLocation = location
                val title = getNoteTitle()
                val content = getNoteContent()
                mNote = Note(title, content, location)
                val task = @SuppressLint("StaticFieldLeak")
                object : AsyncTask<Note, Void, Boolean>() {
                    override fun doInBackground(vararg params: Note?): Boolean {
                        if (!params.isEmpty()) {
                            val param = params[0]
                            param?.let {
                                return Db.NOTE.insert(param) > 0
                            }
                        }
                        return false
                    }

                    override fun onPostExecute(result: Boolean?) {
                        result?.let {
                            if (result) {
                                Log.i(tag, "Note inserted.")
                            } else {
                                Log.e(tag, "Note not inserted.")
                            }
                        }
                    }
                }
                task.execute(mNote)
            }
        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onProviderDisabled(provider: String?) {

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        note_title.addTextChangedListener(textWatcher)
        note_content.addTextChangedListener(textWatcher)
    }

}
