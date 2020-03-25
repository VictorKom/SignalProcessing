package view

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.stage.Stage
import Main
import controller.ChartPointController
import javafx.scene.control.Label

class ChartPointView {

    private val controller: ChartPointController = ChartPointController(this)
    @FXML
    lateinit var chartName: Label
    lateinit var xrLineChart: LineChart<Double,Double>
    lateinit var trLineChart: LineChart<Double,Double>


    @FXML
    fun initialize(){
        chartName.text = controller.getFileName()
        xrLineChart.data.add(controller.createSeriesOfXR())
        trLineChart.data.add(controller.createSeriesOfTR())
    }

    fun createStage() {
        val secondStage = Stage()
        secondStage.title = "Signals of X-Ray and TR"
        secondStage.scene = Scene(FXMLLoader.load(Main::class.java.getResource("charPointView.fxml")))
        secondStage.show()
    }
}