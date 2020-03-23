package controller

import javafx.scene.chart.XYChart
import model.DataRepository
import view.SeriesChartsView

class SeriesChartsController (private val view: SeriesChartsView) {
    private val dataRepository = DataRepository.newInstance()

    fun getChartsOfOneExperiments(index: Int): Map<String, XYChart.Series<Double,Double>> {
       return dataRepository.getChartsOfOneExperiments(index)
    }


}