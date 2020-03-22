package view

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.stage.Stage
import Main
import controller.SecondWindowController
import javafx.beans.Observable
import javafx.collections.ListChangeListener
import javafx.scene.chart.XYChart
import javafx.scene.control.TextArea

class SecondWindow {

    private val controller: SecondWindowController = SecondWindowController()
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
        secondStage.scene = Scene(FXMLLoader.load(Main::class.java.getResource("secondWindow.fxml")))
        secondStage.show()
    }
}