package view

import Main
import controller.SeriesChartsController
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.StackedBarChart
import javafx.scene.chart.XYChart
import javafx.scene.control.TextField
import javafx.stage.Stage

class ChartsOfOneExpView {
    private val controller: SeriesChartsController = SeriesChartsController(this)

    @FXML
    lateinit var scatterChart: ScatterChart<Double,Double>
    lateinit var barChart:  StackedBarChart<String, Double>

    @FXML
    fun initialize(){
        val chartsOfOneExperiment = controller.getChartsOfOneExperiments(0)
        scatterChart.data.add(chartsOfOneExperiment["scatter"] as XYChart.Series<Double, Double>)
        barChart.data.add(chartsOfOneExperiment["lineXR"] as XYChart.Series<String, Double>)
        barChart.data.add(chartsOfOneExperiment["lineTR"] as XYChart.Series<String, Double>)
    }

    fun createStage() {
        val secondStage = Stage()
        secondStage.scene = Scene(FXMLLoader.load(Main::class.java.getResource("seriesChartsView.fxml")))
        secondStage.show()
    }
}