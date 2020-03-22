package model

import java.nio.file.Path

class OneExperiment {
    var filesMap: Map<Path, Path> = HashMap()
    var listOfXRIntegral: MutableList<Double> = ArrayList()
    var listOfTRAmplitude: MutableList<Double> = ArrayList()
    var sweep = 1
    var distance = 0
    var dateOfExperiment = ""

    override fun toString(): String {
        return "date: $dateOfExperiment\td = $distance mm  amount = ${filesMap.size}"
    }
}