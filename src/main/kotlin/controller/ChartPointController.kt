package controller

import javafx.scene.Scene
import javafx.scene.chart.XYChart
import model.OnePulse
import model.DataRepository
import utils.ImageSaver
import view.ChartPointView

class ChartPointController (private var view: ChartPointView) {
    private val dataRepository: DataRepository = DataRepository.newInstance()
    private val onePulse: OnePulse = OnePulse.newInstance()

    fun createSeriesOfXR() : XYChart.Series<Double,Double>{
        return dataRepository.createSeriesOfWaveFormXR(onePulse.pathToCurrentFileOfXR, 0 , 7000, onePulse.sweep)
    }

    fun createSeriesOfTR() : XYChart.Series<Double,Double>{
        return dataRepository.createSeriesOfWaveFormTR(onePulse.pathToCurrentFileOfTR ,1000, 5000)
    }

    fun getFileInfo() : String {
        return onePulse.getInfo()
//        return onePulse.getShortInfo()
    }

    fun saveToFile(scene: Scene, pathToImage: String) {
        ImageSaver.saveToFile(scene, pathToImage)
    }

    fun getNameOfExp() = onePulse.getNumber()
}