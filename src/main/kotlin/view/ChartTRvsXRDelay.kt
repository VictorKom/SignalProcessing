package view

import Main
import controller.ChartTRvsXRDelayController
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.XYChart
import javafx.stage.Stage
import java.util.*

class ChartTRvsXRDelay {
    private val controller = ChartTRvsXRDelayController()

    @FXML
    lateinit var chartTRvsXRDelay: ScatterChart<Double, Double>

    @FXML
    fun initialize(){
        chartTRvsXRDelay.data.add(controller.getChartTRvsXRDelay())
    }

    fun createStage(path: String) {
        val secondaryStage = Stage()
        secondaryStage.title = "Charts of TR vs XRDelay"
        secondaryStage.scene = Scene(FXMLLoader.load(Main::class.java.getResource("chartTRvsXRDelay.fxml")))
        secondaryStage.show()
        controller.saveToFile(secondaryStage.scene, "$path${controller.getNameOfExp()}TRvsDelay.png")
    }
}