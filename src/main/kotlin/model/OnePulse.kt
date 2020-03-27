package model

class OnePulse {
    var pathToCurrentFileOfXR: String = ""
    var pathToCurrentFileOfTR: String = ""
    var amplitudeOfTR = 0.0
    var delay = ""
    var sweep = ""

    companion object {
        private var INSTANCE: OnePulse? = null
        fun newInstance(): OnePulse {
            if (INSTANCE == null) {
                INSTANCE = OnePulse()
            }
            return INSTANCE!!
        }
    }

    fun getInfo() : String{
        return "File number: ${pathToCurrentFileOfXR.substringAfterLast("_").substringBefore(".")}\n" +
                "TR signal amplitude: %.1f mV\n".format(amplitudeOfTR) + "X-Ray signal delay: $delay ns (sweep = $sweep)"
    }

}