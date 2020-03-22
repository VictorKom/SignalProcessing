package model

import javafx.scene.chart.XYChart
import javafx.scene.control.Tooltip
import model.service.Peaks
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.collections.HashMap
import kotlin.math.abs
import kotlin.streams.toList

class DataRepository {

    private var currentFilesMap: HashMap<Path,Path> = HashMap()
    private var sweep = 1
    private var distance = 0
    private var dateOfExperiment = ""
    var currentLineChartOfXR: String = ""
    var currentLineChartOfTR: String = ""
    var amplitudeOfTR = 0.0


    companion object {
        private var INSTANCE: DataRepository? = null
        fun newInstance(): DataRepository {
            if (INSTANCE == null) {
                INSTANCE = DataRepository()
            }
            return INSTANCE!!
        }
    }

    fun findTRandXRFiles(dir: File) : Parameters {
        val filesMap = HashMap<Path,Path>()
        val allFilesOfDir = Files.walk(Paths.get(dir.absolutePath), 1).toList()
        for (XR in allFilesOfDir ){
            val xrFileName = XR.fileName.toString()
            if (xrFileName.startsWith("Hard _XRay")) {
                for (TR in allFilesOfDir){
                    val trFileName = TR.fileName.toString()
                    val fileNumber = xrFileName.substringAfterLast("_")
                    if (trFileName.startsWith("TR_D1") && trFileName.endsWith(fileNumber))
                        filesMap.put(XR,TR)
                }
            }
            if (xrFileName == "_INFO.txt") {
                distance = findParameters(XR)
                dateOfExperiment = dir.name
            }
        }
        currentFilesMap = filesMap
        return Parameters(dir.name, distance, filesMap.size)
    }

    private fun findParameters(pathInfo: Path) : Int{
        var sweep = 1
        var distance = 0
        val fileLines: MutableList<String> = Files.readAllLines(pathInfo, Charset.forName("windows-1251"))
        for (line in fileLines){
            if (line.startsWith("d"))
                distance = line.replace("\\D".toRegex(), "").toInt()
            if (line.startsWith("t"))
                sweep = line.replace("\\D".toRegex(), "").toInt()
        }
        this.sweep = sweep
        return distance
    }

    private fun calculateIntegral(waveForm: ArrayList<Double>, sweep: Int) : Double {
        var intCoeff = 1.0
        when(sweep){
            400 -> intCoeff = 2.5
            200 -> intCoeff = 5.0
        }
        var maxOfNoise = waveForm.subList(2, 800).max()
        val averageNoise = waveForm.subList(2, 800).average()
        maxOfNoise = maxOfNoise?.plus(abs(maxOfNoise))
        waveForm.removeIf { it < maxOfNoise!! }
        val integral = (waveForm.sum() - averageNoise * waveForm.size) / (waveForm.size * intCoeff)
        return if (integral.isNaN()) 0.0 else integral
    }

     fun calculateAmplitude(waveForm: ArrayList<Double>) : Double {
         val averageNoise = waveForm.subList(2, 800).average()
         val waveFormInvert = waveForm.map { (it - averageNoise) * (-1000) } as ArrayList<Double>
         val peaks = Peaks.findPeaks(waveFormInvert.toDoubleArray(), 7, 10.0)
         var amplitude = 0.0
         if (peaks.size > 2){
            for (i in 0 until peaks.lastIndex){
                val ratio = waveFormInvert[peaks[i + 1]] / waveFormInvert[peaks[i]]
                if ( ratio > 0.7 && ratio < 1.3){
                    amplitude = waveFormInvert[peaks[i]]
                    break
                }
            }
        }
        return amplitude
    }

    fun createSeriesXRvsTR() : XYChart.Series<Double,Double> {
        val series = XYChart.Series<Double,Double>()
        series.name = "$dateOfExperiment\nd = $distance mm"
        for ((XRFile, TRFile) in currentFilesMap){
            val integralOfXR = calculateIntegral(getWaveForm(XRFile), sweep)
            val amplitudeOfTR = calculateAmplitude(getWaveForm(TRFile))
            //print("$integralOfXR   $amplitudeOfTR\n")
            val node = XYChart.Data(integralOfXR, amplitudeOfTR)
            node.extraValue = "$XRFile\t$TRFile"
            series.data.add(node)
        }
        return series
    }

     fun getWaveForm(pathToFile: Path, fromIndex: Int = 0, toIndex: Int = 7000) : ArrayList<Double> {
        val waveForm = ArrayList<Double>()
        val lines: MutableList<String> = Files.readAllLines(pathToFile)
        lines.removeAt(0)
        for (i in fromIndex..toIndex){
            val value = lines[i].split("\t")[1]
            waveForm.add(value.toDouble())
       }
        return waveForm
    }

     fun createSeriesOfWaveFormTR(fromIndex: Int, toIndex: Int) : XYChart.Series<Double,Double> {
         val series = XYChart.Series<Double,Double>()
         var waveForm = getWaveForm(Paths.get(currentLineChartOfTR), fromIndex, toIndex)
         val averageNoise = waveForm.subList(2, 800).average()
         waveForm = waveForm.map { (it - averageNoise) * (-1000)  } as ArrayList<Double>
        for (i in waveForm.indices){
            series.data.add(XYChart.Data(i.toDouble(), waveForm[i]) )
        }
        return series
    }

    fun createSeriesOfWaveFormXR(fromIndex: Int, toIndex: Int) : XYChart.Series<Double,Double> {
        val series = XYChart.Series<Double,Double>()
        var waveForm = getWaveForm(Paths.get(currentLineChartOfXR), fromIndex, toIndex)
        val averageNoise = waveForm.subList(2, 800).average()
        waveForm = waveForm.map { it - averageNoise } as ArrayList<Double>
        for (i in waveForm.indices){
            series.data.add(XYChart.Data(i.toDouble(), waveForm[i]) )
        }
        return series
    }

     fun createSeriesOfPeaks(pathToFile: String, fromIndex: Int, toIndex: Int) : XYChart.Series<Double,Double> {
         val series = XYChart.Series<Double,Double>()
         var waveForm = getWaveForm(Paths.get(pathToFile), fromIndex, toIndex)
         val averageNoise = waveForm.subList(2, 800).average()
         waveForm = waveForm.map { (it - averageNoise) * (-1000)  } as ArrayList<Double>
         val peaks = Peaks.findPeaks(waveForm.toDoubleArray(), 7, 7.0)
         for (peak in peaks){
             series.data.add(XYChart.Data(peak.toDouble(), waveForm[peak]) )
         }
         return series
     }
}