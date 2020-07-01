package controller

import javafx.scene.Scene
import javafx.scene.chart.XYChart
import model.DataRepository
import utils.ImageSaver
import utils.TxtSaver

class ChartTRvsXRDelayController() {
    private val dataRepository = DataRepository.newInstance()

    fun getScatterChartTRvsXRDelay() : XYChart.Series<Double, Double> {
        return dataRepository.getScatterChartOfTRvsXRDelay(0)
    }

    fun getBarChartTRvsXRDelay() : XYChart.Series<String, Double> {
        return dataRepository.getBarChartOfTRvsXRDelay(0)
    }

    fun getBarChartIntegralvsXRDelay() : XYChart.Series<String, Double> {
        return dataRepository.getBarChartOfIntegralvsXRDelay(0)
    }

    fun saveToFile(scene: Scene, pathToImage: String) {
        ImageSaver.saveToFile(scene, pathToImage)
    }

    fun saveToFile(seriesTR: XYChart.Series<String, Double>, seriesXR: XYChart.Series<String, Double>, pathToTxt: String) {
       TxtSaver.saveToTxtFile(seriesTR, seriesXR, pathToTxt)
    }

    fun getNameOfExp(index: Int = 0) = dataRepository.getDateOfExp(index)
}