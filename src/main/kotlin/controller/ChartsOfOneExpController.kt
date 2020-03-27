package controller

import javafx.embed.swing.SwingFXUtils
import javafx.scene.Scene
import javafx.scene.SnapshotParameters
import javafx.scene.chart.XYChart
import javafx.scene.image.WritableImage
import model.DataRepository
import view.ChartsOfOneExpView
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


class ChartsOfOneExpController (private val view: ChartsOfOneExpView) {
    private val dataRepository = DataRepository.newInstance()


    fun getChartsOfOneExperiments(index: Int): Map<String, XYChart.Series<out Any,Double>> {
       return dataRepository.getChartsOfOneExperiments(index)
    }

    fun saveToFile(scene: Scene, pathToImage: String) {
        val image: WritableImage = scene.snapshot(null)
        val file = File(pathToImage)
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}