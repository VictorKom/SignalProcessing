package controller

import javafx.scene.Scene
import javafx.scene.chart.XYChart
import model.DataRepository
import utils.ImageSaver

class ChartTRvsXRDelayController {
    private val dataRepository = DataRepository.newInstance()

    fun getChartTRvsXRDelay() : XYChart.Series<Double, Double> {
        return dataRepository.getChartOfTRvsXRDelay(0)
    }

    fun saveToFile(scene: Scene, pathToImage: String) {
        ImageSaver.saveToFile(scene, pathToImage)
    }

    fun getNameOfExp(index: Int = 0) = dataRepository.getNameOfExp(index)
}