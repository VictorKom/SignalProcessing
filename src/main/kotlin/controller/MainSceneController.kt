package controller

import javafx.scene.chart.XYChart
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import model.DataRepository
import view.MainView
import java.io.File

class MainSceneController (private var view: MainView) {
    private val dataRepository: DataRepository = DataRepository.newInstance()

    fun chooseDir() {
        val dirChooser = DirectoryChooser()
        dirChooser.initialDirectory = File("F:/Эксперимент/")
        val dir = dirChooser.showDialog(Stage())
        view.refreshTextArea(dataRepository.findTRandXRFiles(dir))
    }

    fun createSeries() : XYChart.Series<Double,Double> {
        return dataRepository.createSeriesXRvsTR()
    }

    fun setCurrentLineChart(paths: List<String>) {
        dataRepository.currentLineChartOfXR = paths[0]
        dataRepository.currentLineChartOfTR = paths[1]
    }

}