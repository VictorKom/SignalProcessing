package controller

import javafx.scene.chart.XYChart
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import model.OnePulse
import model.DataRepository
import model.OneExperiment
import view.MainView
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
        view.refreshTextArea(currentExperiment)
    }

    fun createSeries() : XYChart.Series<Double,Double> {
        return dataRepository.createSeriesXRvsTR(currentExperiment)
    }

    fun setCurrentLineChart(paths: List<String>, amplitude: Double) {
        onePulse.pathToCurrentFileOfXR = paths[0]
        onePulse.pathToCurrentFileOfTR = paths[1]
        onePulse.amplitudeOfTR = amplitude
    }

    fun clearData() {
        dataRepository.clearData()
    }

}