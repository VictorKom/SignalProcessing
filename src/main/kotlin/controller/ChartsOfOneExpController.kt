package controller

import javafx.scene.chart.XYChart
import model.DataRepository
import view.ChartsOfOneExpView

class ChartsOfOneExpController (private val view: ChartsOfOneExpView) {
    private val dataRepository = DataRepository.newInstance()


    fun getChartsOfOneExperiments(index: Int): Map<String, XYChart.Series<out Any,Double>> {
       return dataRepository.getChartsOfOneExperiments(index)
    }


}