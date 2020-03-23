package view

import Main
import controller.SeriesChartsController
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.scene.chart.ScatterChart
import javafx.stage.Stage

class SeriesChartsView {
    private val controller: SeriesChartsController = SeriesChartsController(this)

    @FXML
    lateinit var firstScatterChart: ScatterChart<Double,Double>
    lateinit var firstLineChart: LineChart<Double, Double>
    lateinit var secondScatterChart: ScatterChart<Double,Double>
    lateinit var secondLineChart: LineChart<Double, Double>

    @FXML
    fun initialize(){
        var chartsOfOneExperiment = controller.getChartsOfOneExperiments(0)
        firstScatterChart.data.add(chartsOfOneExperiment["scatter"])
        firstLineChart.data.addAll(chartsOfOneExperiment["lineXR"], chartsOfOneExperiment["lineTR"])
        chartsOfOneExperiment = controller.getChartsOfOneExperiments(1)
        secondScatterChart.data.add(chartsOfOneExperiment["scatter"])
        secondLineChart.data.addAll(chartsOfOneExperiment["lineXR"], chartsOfOneExperiment["lineTR"])
    }

    fun createStage() {
        val secondStage = Stage()
        secondStage.scene = Scene(FXMLLoader.load(Main::class.java.getResource("seriesChartsView.fxml")))
        secondStage.show()
    }
}