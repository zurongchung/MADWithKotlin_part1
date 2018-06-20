package journaler.ollie.study.journaler.model

import android.location.Location
import journaler.ollie.study.journaler.database.DbModle

abstract class Entry(
    var title: String,
    var message: String,
    var location: Location
) : DbModle()