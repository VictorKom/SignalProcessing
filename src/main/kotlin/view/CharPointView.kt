package view

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.stage.Stage
import Main
import controller.CharPointController
import javafx.scene.control.TextArea

class CharPointView {

    private val controller: CharPointController = CharPointController(this)
    @FXML
    lateinit var chartName: TextArea
    lateinit var xrLineChart: LineChart<Double,Double>
    lateinit var trLineChart: LineChart<Double,Double>


    @FXML
    fun initialize(){
        xrLineChart.createSymbols = false
        trLineChart.createSymbols = false
        chartName.text = controller.getFileName()
        xrLineChart.data.add(controller.createSeriesOfXR())
        trLineChart.data.add(controller.createSeriesOfTR())
    }

    fun createStage() {
        val secondStage = Stage()
        secondStage.scene = Scene(FXMLLoader.load(Main::class.java.getResource("charPointView.fxml")))
        secondStage.show()
    }
}