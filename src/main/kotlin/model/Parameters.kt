package model

class Parameters(val date: String, val distance: Int, val numberOfFiles: Int) {
    override fun toString(): String {
        return "date: $date\td = $distance mm  amount = $numberOfFiles"
    }
}