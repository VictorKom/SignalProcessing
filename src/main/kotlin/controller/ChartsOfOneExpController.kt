package controller

import javafx.scene.Scene
import javafx.scene.chart.XYChart
import model.DataRepository
import utils.ImageSaver
import utils.TxtSaver
import view.ChartsOfOneExpView


class ChartsOfOneExpController (private val view: ChartsOfOneExpView) {
    private val dataRepository = DataRepository.newInstance()


    fun getChartsOfOneExperiments(index: Int, sorted: Boolean = true): Map<String, XYChart.Series<out Any,Double>> {
        return if (sorted) dataRepository.getSortedChartsOfOneExperiments(index)
                else dataRepository.getChartsOfOneExperiments(index)
    }

    fun saveToFile(scene: Scene, pathToImage: String) {
        ImageSaver.saveToFile(scene, pathToImage)
    }

    fun saveToFile(seriesTR: XYChart.Series<String, Double>, seriesXR: XYChart.Series<String, Double>, pathToTxt: String) {
        TxtSaver.saveToTxtFileBars(seriesTR, seriesXR, pathToTxt)
    }


    fun getNameOfExp(index: Int) = dataRepository.getDateOfExp(index)

}