package controller

import javafx.scene.chart.XYChart
import model.OnePulse
import model.DataRepository
import view.ChartPointView

class ChartPointController (private var view: ChartPointView) {
    private val dataRepository: DataRepository = DataRepository.newInstance()
    private val onePulse: OnePulse = OnePulse.newInstance()

    fun createSeriesOfXR() : XYChart.Series<Double,Double>{
        return dataRepository.createSeriesOfWaveFormXR(onePulse.pathToCurrentFileOfXR, 0 , 7000)
    }

    fun createSeriesOfTR() : XYChart.Series<Double,Double>{
        return dataRepository.createSeriesOfWaveFormTR(onePulse.pathToCurrentFileOfTR ,1000, 5000)
    }

    fun getFileName() : String {
        return onePulse.getInfo()
    }
}