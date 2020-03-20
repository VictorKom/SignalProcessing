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

class SecondWindow constructor() {

    constructor(s: String) : this(){
        println("constructor $path")
        path = s
        println("constructor $path")
    }

    private val controller: SecondWindowController = SecondWindowController()
    private var path: String = "oldValue"
    @FXML

    lateinit var xrLineChart: LineChart<Double,Double>
    lateinit var trLineChart: LineChart<Double,Double>


    @FXML
    fun initialize(){
        xrLineChart.createSymbols = false
        trLineChart.createSymbols = false
        println("inti + $path")
        //xrLineChart.data.add(controller.createSeries(path))
        xrLineChart.data.add(XYChart.Series<Double,Double>())
        //trLineChart.data.add(controller.createSeries("C:\\Hard _XRay_1.txt"))
    }

    fun createStage() {
        println(path)
        //xrLineChart.data.add(controller.createSeries(path))
        val secondStage = Stage()
        secondStage.scene = Scene(FXMLLoader.load(Main::class.java.getResource("secondWindow.fxml")))
        secondStage.show()
        // xrLineChart.data.add(controller.createSeries(xrPath))
        // trLineChart.data.add(controller.createSeries(trPath))

    }



}