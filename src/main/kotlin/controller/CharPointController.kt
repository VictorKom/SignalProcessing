package controller

import javafx.scene.chart.XYChart
import model.OnePulse
import model.DataRepository
import view.CharPointView

class CharPointController (private var view: CharPointView) {
    private val dataRepository: DataRepository = DataRepository.newInstance()
    private val onePulse: OnePulse = OnePulse.newInstance()

    fun createSeriesOfXR() : XYChart.Series<Double,Double>{
        return dataRepository.createSeriesOfWaveFormXR(onePulse.pathToCurrentFileOfXR, 0 , 7000)
    }

    fun createSeriesOfTR() : XYChart.Series<Double,Double>{
        return dataRepository.createSeriesOfWaveFormTR(onePulse.pathToCurrentFileOfTR ,1000, 5000)
    }

    fun getFileName() : String{
        return "${onePulse.pathToCurrentFileOfXR.substringAfterLast("_")}\n" +
                "%.1f".format(onePulse.amplitudeOfTR)
    }
}