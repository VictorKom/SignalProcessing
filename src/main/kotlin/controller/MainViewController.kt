package controller

import javafx.scene.chart.XYChart
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import model.OnePulse
import model.DataRepository
import model.OneExperiment
import view.*
import java.io.File

class MainViewController (private var view: MainView) {
    private val dataRepository: DataRepository = DataRepository.newInstance()
    private val onePulse: OnePulse = OnePulse.newInstance()
    private var currentExperiment = OneExperiment()

    fun chooseDir() {
        val dirChooser = DirectoryChooser()
        dirChooser.initialDirectory = File("C:/Эксперимент/")
        val dir = dirChooser.showDialog(Stage())
        currentExperiment = dataRepository.findTRandXRFiles(dir)

    }

    fun createSeries() : XYChart.Series<Double,Double> {
        val series = dataRepository.createSeriesXRvsTR(currentExperiment)
        currentExperiment.eff = calculateEff(currentExperiment.listOfTRAmplitude)
        currentExperiment.sumTR = currentExperiment.listOfTRAmplitude.sum()
        currentExperiment.sumXR = currentExperiment.listOfXRIntegral.sum()
        view.refreshTextArea(currentExperiment)
        return series
    }

    fun plotCurrentLineChart(property: List<String>, amplitude: Double) {
        onePulse.pathToCurrentFileOfXR = property[0]
        onePulse.pathToCurrentFileOfTR = property[1]
        onePulse.amplitudeOfTR = amplitude
        onePulse.delay = property[2]
        onePulse.sweep = property[3]
        ChartPointView().createStage()
    }

    fun clearData() {
        dataRepository.clearData()
    }

    fun plotAllSelected(path: String, enableSaving: Boolean) {
        ChartsOfOneExpView().createStage(path,enableSaving)
    }

    fun plotTRvsXRDelay(path: String, enableSaving: Boolean, isBarChart: Boolean) {
        if (isBarChart){
            BarChartOfXRDelay().createStage(path, enableSaving)
        }
        else {
            ScatterChartTRvsXRDelay().createStage(path, enableSaving)
        }
    }

    private fun calculateEff(listOfTRAmplitude:  MutableList<Double>): String {
        var count = 0.0
        val size = listOfTRAmplitude.size * 1.0
        for (signal in listOfTRAmplitude){
            if (signal > 0) count++
        }
        count /= size
        count *= 100.0
        return "${count.toInt()} %"
    }


}