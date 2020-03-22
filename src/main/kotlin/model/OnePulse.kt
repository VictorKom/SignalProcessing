package model

class OnePulse {
    var pathToCurrentFileOfXR: String = ""
    var pathToCurrentFileOfTR: String = ""
    var amplitudeOfTR = 0.0

    companion object {
        private var INSTANCE: OnePulse? = null
        fun newInstance(): OnePulse {
            if (INSTANCE == null) {
                INSTANCE = OnePulse()
            }
            return INSTANCE!!
        }
    }
}