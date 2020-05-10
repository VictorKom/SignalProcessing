package controller

import javafx.embed.swing.SwingFXUtils
import javafx.scene.Scene
import javafx.scene.SnapshotParameters
import javafx.scene.chart.StackedBarChart
import javafx.scene.chart.XYChart
import javafx.scene.image.WritableImage
import javafx.scene.transform.Scale
import model.DataRepository
import utils.ImageSaver
import view.ChartsOfOneExpView
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


class ChartsOfOneExpController (private val view: ChartsOfOneExpView) {
    private val dataRepository = DataRepository.newInstance()


    fun getChartsOfOneExperiments(index: Int, sorted: Boolean = true): Map<String, XYChart.Series<out Any,Double>> {
        return if (sorted) dataRepository.getSortedChartsOfOneExperiments(index)
                else dataRepository.getChartsOfOneExperiments(index)
    }

    fun saveToFile(scene: Scene, pathToImage: String) {
        ImageSaver.saveToFile(scene, pathToImage)
    }

    fun getNameOfExp(index: Int) = dataRepository.getNameOfExp(index)

}