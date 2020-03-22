package model

class Parameters(private val date: String, private val distance: Int, private val numberOfFiles: Int) {
    override fun toString(): String {
        return "date: $date\td = $distance mm  amount = $numberOfFiles"
    }
}