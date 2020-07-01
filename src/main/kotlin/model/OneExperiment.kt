package model

import java.nio.file.Path

class OneExperiment {
    var filesMap: Map<Path, Path> = HashMap()
    var listOfXRIntegral: MutableList<Double> = ArrayList()
    var listOfTRAmplitude: MutableList<Double> = ArrayList()
    var listOfXRDelay: MutableList<Double> = ArrayList()
    var sweep = 1
    var distance = 0
    var dateOfExperiment = ""
    var eff = "0 %"
    var sumTR = 0.0
    var sumXR = 0.0

    override fun toString(): String {
        val sumTRString = "%.1f".format(sumTR)
        val sumXRString = "%.1f".format(sumXR)
        return "date: $dateOfExperiment\td = $distance mm\tamount = ${filesMap.size}  eff = $eff" +
                "\tsumTR = $sumTRString\tsumXR = $sumXRString"
    }
}