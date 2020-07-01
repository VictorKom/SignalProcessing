package model

import javafx.scene.chart.XYChart
import model.service.Peaks
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.abs
import kotlin.streams.toList

class DataRepository {

    val listOfSomeExperiments: ArrayList<OneExperiment> = ArrayList()

    companion object {
        private var INSTANCE: DataRepository? = null
        fun newInstance(): DataRepository {
            if (INSTANCE == null) {
                INSTANCE = DataRepository()
            }
            return INSTANCE!!
        }
    }

    fun findTRandXRFiles(dir: File): OneExperiment {
        val filesMap = HashMap<Path, Path>()
        val currentExperiment = OneExperiment()
        val allFilesOfDir = Files.walk(Paths.get(dir.absolutePath), 1).toList()
        for (XR in allFilesOfDir) {
            val xrFileName = XR.fileName.toString()
            if (xrFileName.startsWith("Hard _XRay")) {
                for (TR in allFilesOfDir) {
                    val trFileName = TR.fileName.toString()
                    val fileNumber = xrFileName.substringAfterLast("_")
                    if (trFileName.startsWith("TR_D1") && trFileName.endsWith(fileNumber))
                        filesMap.put(XR, TR)
                }
            }
            if (xrFileName == "_INFO.txt") {
                currentExperiment.distance = findParameters(XR)["distance"] ?: 0
                currentExperiment.sweep = findParameters(XR)["sweep"] ?: 0
                currentExperiment.dateOfExperiment = dir.name
            }
        }
        currentExperiment.filesMap = filesMap
        listOfSomeExperiments.add(currentExperiment)
        return currentExperiment
    }

    private fun findParameters(pathInfo: Path): Map<String, Int> {
        var sweep = 1
        var distance = 0
        val fileLines: MutableList<String> = Files.readAllLines(pathInfo, Charset.forName("windows-1251"))
        for (line in fileLines) {
            if (line.startsWith("d"))
                distance = line.replace("\\D".toRegex(), "").toInt()
            if (line.startsWith("t"))
                sweep = line.replace("\\D".toRegex(), "").toInt()
        }
        return mapOf("distance" to distance, "sweep" to sweep)
    }

    private fun calculateIntegral(waveForm: ArrayList<Double>, sweep: Int): Double {
        val sweepCoeff = getSweepCoefficient(sweep)
        var maxOfNoise = waveForm.subList(2, 800).max()
        val averageNoise = waveForm.subList(2, 800).average()
        maxOfNoise = maxOfNoise?.plus(abs(maxOfNoise))
        waveForm.removeIf { it < maxOfNoise!! }
        val integral = 100 * (waveForm.sum() - averageNoise * waveForm.size) / (waveForm.size * sweepCoeff)
        return if (integral.isNaN()) 0.0 else integral
    }

    private fun getSweepCoefficient(sweep: Int): Double {
        var coeff = 1.0
        when (sweep) {
            400 -> coeff = 2.5
            200 -> coeff = 5.0
        }
        return coeff
    }

    private fun calculateAmplitude(waveForm: ArrayList<Double>): Double {
        val averageNoise = waveForm.subList(2, 800).average()
        val waveFormInvert = waveForm.map { (it - averageNoise) * (-1000) } as ArrayList<Double>
        val peaks = Peaks.findPeaks(waveFormInvert.toDoubleArray(), 7, 10.0)
        var amplitude = 0.0
        if (peaks.size > 2) {
            for (i in 0 until peaks.lastIndex) {
                val ratio = waveFormInvert[peaks[i + 1]] / waveFormInvert[peaks[i]]
                if (ratio > 0.7 && ratio < 1.3) {
                    amplitude = waveFormInvert[peaks[i]]
                    break
                }
            }
        }
        return amplitude
    }

    fun createSeriesXRvsTR(currentExperiment: OneExperiment): XYChart.Series<Double, Double> {
        val series = XYChart.Series<Double, Double>()
        series.name = "${currentExperiment.dateOfExperiment}\nd = ${currentExperiment.distance} mm"
        for ((XRFile, TRFile) in currentExperiment.filesMap) {
            val xrWaveForm = getWaveForm(XRFile)
            val delay = calculateDelay(xrWaveForm, currentExperiment.sweep)
            val integralOfXR = calculateIntegral(xrWaveForm, currentExperiment.sweep)
            val amplitudeOfTR = calculateAmplitude(getWaveForm(TRFile))
            val node = XYChart.Data(integralOfXR, amplitudeOfTR)
            node.extraValue = "$XRFile\t$TRFile\t$delay\t${currentExperiment.sweep}"
            series.data.add(node)
            listOfSomeExperiments.last().listOfTRAmplitude.add(amplitudeOfTR)
            listOfSomeExperiments.last().listOfXRIntegral.add(integralOfXR)
            if (delay == "")
                listOfSomeExperiments.last().listOfXRDelay.add(0.0)
            else
                listOfSomeExperiments.last().listOfXRDelay.add(delay.toDouble())
        }
        return series
    }

    private fun getWaveForm(pathToFile: Path, fromIndex: Int = 0, toIndex: Int = 7000): ArrayList<Double> {
        val waveForm = ArrayList<Double>()
        val lines: MutableList<String> = Files.readAllLines(pathToFile)
        lines.removeAt(0)
        for (i in fromIndex..toIndex) {
            val value = lines[i].split("\t")[1]
            waveForm.add(value.toDouble())
        }
        return waveForm
    }

    fun createSeriesOfWaveFormTR(pathToFile: String, fromIndex: Int, toIndex: Int): XYChart.Series<Double, Double> {
        val series = XYChart.Series<Double, Double>()
        var waveForm = getWaveForm(Paths.get(pathToFile), fromIndex, toIndex)
        val averageNoise = waveForm.subList(2, 800).average()
        waveForm = waveForm.map { (it - averageNoise) * (-1000) } as ArrayList<Double>
        for (i in waveForm.indices) {
            series.data.add(XYChart.Data(i.toDouble(), waveForm[i]))
        }
        return series
    }

    fun createSeriesOfWaveFormXR(
        pathToFile: String,
        fromIndex: Int,
        toIndex: Int,
        sweep: String
    ): XYChart.Series<Double, Double> {
        val coeff = getSweepCoefficient(sweep.toInt())
        val series = XYChart.Series<Double, Double>()
        var waveForm = getWaveForm(Paths.get(pathToFile), fromIndex, toIndex)
        val averageNoise = waveForm.subList(2, 800).average()
        waveForm = waveForm.map { it - averageNoise } as ArrayList<Double>
        for (i in waveForm.indices) {
            series.data.add(XYChart.Data(i / coeff, waveForm[i]))
        }
        return series
    }

    fun clearData() {
        listOfSomeExperiments.clear()
    }

    fun createSeriesOfPeaks(pathToFile: String, fromIndex: Int, toIndex: Int): XYChart.Series<Double, Double> {
        val series = XYChart.Series<Double, Double>()
        var waveForm = getWaveForm(Paths.get(pathToFile), fromIndex, toIndex)
        val averageNoise = waveForm.subList(2, 800).average()
        waveForm = waveForm.map { (it - averageNoise) * (-1000) } as ArrayList<Double>
        val peaks = Peaks.findPeaks(waveForm.toDoubleArray(), 7, 7.0)
        for (peak in peaks) {
            series.data.add(XYChart.Data(peak.toDouble(), waveForm[peak]))
        }
        return series
    }

    fun getChartsOfOneExperiments(index: Int): Map<String, XYChart.Series<out Any, Double>> {
        val oneExperiment = listOfSomeExperiments[index]
        val listOfTRAmplitude = getSubList(oneExperiment.listOfTRAmplitude)
        val listOfXRIntegral =  getSubList(oneExperiment.listOfXRIntegral)
        val seriesOfScatterChart = XYChart.Series<Double, Double>()
        val seriesTROfBarChart = XYChart.Series<String, Double>()
        val seriesXROfBarChart = XYChart.Series<String, Double>()
        seriesOfScatterChart.name = "${oneExperiment.dateOfExperiment}  d = ${oneExperiment.distance} mm"
        seriesTROfBarChart.name = "Amplitude of TR"
        seriesXROfBarChart.name = "Integral of X-Ray"
        for (i in 0 until listOfTRAmplitude.size) {
            seriesOfScatterChart.data.add(XYChart.Data(listOfXRIntegral[i], listOfTRAmplitude[i]))
            seriesXROfBarChart.data.add(XYChart.Data((i+1).toString(), listOfXRIntegral[i]))
            seriesTROfBarChart.data.add(XYChart.Data((i+1).toString(), listOfTRAmplitude[i]))
        }
        return mapOf(
            "scatter" to seriesOfScatterChart, "lineXR" to seriesXROfBarChart,
            "lineTR" to seriesTROfBarChart
        )
    }

    fun getScatterChartOfTRvsXRDelay(index: Int): XYChart.Series<Double, Double> {
        val oneExperiment = listOfSomeExperiments[index]
        val listOfTRAmplitude = getSubList(oneExperiment.listOfTRAmplitude)
        val listOfXRDelay = getSubList(oneExperiment.listOfXRDelay)
        val seriesOfScatterChart = XYChart.Series<Double, Double>()
        seriesOfScatterChart.name = "d = ${oneExperiment.distance} mm"
        for (i in 0 until listOfTRAmplitude.size) {
            seriesOfScatterChart.data.add(XYChart.Data(listOfXRDelay[i], listOfTRAmplitude[i]))
        }
        return seriesOfScatterChart
    }

    fun getBarChartOfTRvsXRDelay(index: Int): XYChart.Series<String, Double> {
        val oneExperiment = listOfSomeExperiments[index]
        return getSeriesXvsXRDelay(index, getSubList(oneExperiment.listOfTRAmplitude))
            .apply { name = "d = ${oneExperiment.distance} mm" }
    }

    fun getBarChartOfIntegralvsXRDelay(index: Int): XYChart.Series<String, Double> {
        val oneExperiment = listOfSomeExperiments[index]
        return getSeriesXvsXRDelay(index, getSubList(oneExperiment.listOfXRIntegral))
    }

    private fun getSeriesXvsXRDelay(index: Int, x: MutableList<Double>):  XYChart.Series<String, Double> {
        val oneExperiment = listOfSomeExperiments[index]
        val listOfXRDelay = getSubList(oneExperiment.listOfXRDelay)
        // val listOfXRDelay = oneExperiment.listOfXRDelay
        val seriesOfBarChart = XYChart.Series<String, Double>()
        var a = 0
        var b = 50
        val tempList = ArrayList<Double>()
        while (b <= 900){
            for ((i,delay) in listOfXRDelay.withIndex()){
                if (delay >= a && delay <b){
                    if (x[i] > 0) {
                        tempList.add(x[i])
                    }
                }
            }
           // seriesOfBarChart.data.add(XYChart.Data(b.toString(), tempList.sum()))
            seriesOfBarChart.data.add(XYChart.Data(b.toString(), tempList.size.toDouble()))
            tempList.clear()
            a += 50
            b += 50
        }
        return seriesOfBarChart
    }


    private fun getSubList(list: MutableList<Double>, size: Int = 50): MutableList<Double> {
        return if (list.size > 49) list.subList(0, size)
        else list
    }

    fun getDateOfExp(index: Int) = listOfSomeExperiments[index].dateOfExperiment

    fun getSortedChartsOfOneExperiments(index: Int): Map<String, XYChart.Series<out Any, Double>> {
        val oneExperiment = listOfSomeExperiments[index]
        val listOfTRAmplitude = getSubList(oneExperiment.listOfTRAmplitude)
        val listOfXRIntegral =  getSubList(oneExperiment.listOfXRIntegral)
        val treeMap = TreeMap<Double, Double>()
        for (i in 0 until listOfTRAmplitude.size) {
            treeMap.put(listOfXRIntegral[i], listOfTRAmplitude[i])
        }
        val seriesOfScatterChart = XYChart.Series<Double, Double>()
        val seriesTROfBarChart = XYChart.Series<String, Double>()
        val seriesXROfBarChart = XYChart.Series<String, Double>()
        //seriesOfScatterChart.name = "${oneExperiment.dateOfExperiment}  d = ${oneExperiment.distance} mm"
        seriesOfScatterChart.name = "d = ${oneExperiment.distance} mm"
        seriesTROfBarChart.name = "Amplitude of TR"
        seriesXROfBarChart.name = "Integral of X-Ray"
        var i = 1
        for (key in treeMap.keys) {
            seriesOfScatterChart.data.add(XYChart.Data(key, treeMap[key]))
            seriesXROfBarChart.data.add(XYChart.Data(i.toString(), key))
            seriesTROfBarChart.data.add(XYChart.Data(i.toString(), treeMap[key]))
            i++
        }
        return mapOf(
            "scatter" to seriesOfScatterChart, "lineXR" to seriesXROfBarChart,
            "lineTR" to seriesTROfBarChart
        )
    }

    private fun calculateDelay(waveForm: ArrayList<Double>, sweep: Int): String {
        val sweepCoeff = getSweepCoefficient(sweep)
        var delay = ""
        var maxOfNoise = waveForm.subList(2, 800).max()
        maxOfNoise = maxOfNoise?.plus(abs(maxOfNoise))
        for (i in 1150 until waveForm.size) {
            if (waveForm[i] > maxOfNoise!!) {
                delay = "${(i - 1000) / sweepCoeff}"
                break
            }
        }
        return delay
    }
}