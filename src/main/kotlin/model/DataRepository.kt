package model

import javafx.scene.chart.XYChart
import javafx.scene.control.Tooltip
import model.service.Peaks
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.collections.HashMap
import kotlin.math.abs
import kotlin.streams.toList

class DataRepository {

    private var currentFilesMap: HashMap<Path,Path> = HashMap()
    private var sweep = 1


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
        var distance = 0
        val allFilesOfDir = Files.walk(Paths.get(dir.absolutePath)).toList()
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
            if (xrFileName == "_INFO.txt")
                distance = findParameters(XR)
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

    private fun calculateAmplitude(waveForm: ArrayList<Double>) : Double {
        return (-1000) * waveForm.min()!!
    }

    fun createSeriesXRvsTR(name: String = "default name") : XYChart.Series<Double,Double> {
        val series = XYChart.Series<Double,Double>()
        series.name = name
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

    private fun getWaveForm(pathToFile: Path) : ArrayList<Double> {
        val waveForm = ArrayList<Double>()
        val lines: MutableList<String> = Files.readAllLines(pathToFile)
        lines.removeAt(0)
        for (line in lines){
            val value = line.split("\t")[1]
            waveForm.add(value.toDouble())
       }
        return waveForm
    }

     fun createSeriesOfWaveForm(pathToFile: String) : XYChart.Series<Double,Double> {
        val series = XYChart.Series<Double,Double>()
        val waveForm = getWaveForm(Paths.get(pathToFile))
        for (i in waveForm.indices){
            series.data.add(XYChart.Data<Double,Double>(i.toDouble(), waveForm[i]) )
        }
        return series
    }

}