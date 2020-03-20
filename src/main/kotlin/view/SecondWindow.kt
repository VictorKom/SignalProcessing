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
import java.nio.file.Path
import javafx.collections.ObservableList as ListC

class SecondWindow {

    private var xrPath: String = ""
    private var trPath: String = ""
    private val controller: SecondWindowController = SecondWindowController()

    @FXML
    lateinit var xrLineChart: LineChart<Double,Double>
    lateinit var trLineChart: LineChart<Double,Double>

    @FXML
    fun initialize(){
       // xrLineChart.data.add(XYChart.Series<Double,Double>())
       // trLineChart.data.add(XYChart.Series<Double,Double>())
    }

    fun createStage(xrPath: String, trPath: String) {
        this.xrPath = xrPath
        this.trPath = trPath
        //xrLineChart.data.add(controller.createSeries(xrPath))
        //trLineChart.data.add(controller.createSeries(trPath))
        val secondStage = Stage()
        secondStage.scene = Scene(FXMLLoader.load(Main::class.java.getResource("secondWindow.fxml")))
        secondStage.show()
    }

}