package view

import Main
import controller.ChartTRvsXRDelayController
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.chart.ScatterChart
import javafx.stage.Stage
import utils.TxtSaver

class ScatterChartTRvsXRDelay {
    private val controller = ChartTRvsXRDelayController()

    @FXML
    lateinit var scatterChartTRvsXRDelay: ScatterChart<Double, Double>

    @FXML
    fun initialize(){
        scatterChartTRvsXRDelay.data.add(controller.getScatterChartTRvsXRDelay())
        TxtSaver.saveToTxtFileScatter(controller.getScatterChartTRvsXRDelay(),"C:/Эксперимент/heap/${controller.getNameOfExp(0)}.txt")
    }

    fun createStage(pathToSave: String, enableSaving: Boolean) {
        val secondaryStage = Stage()
        secondaryStage.title = "Charts of TR vs XRDelay"
        secondaryStage.scene = Scene(FXMLLoader.load(Main::class.java.getResource("scatterChartTRvsXRDelay.fxml")))
        secondaryStage.show()
        if (enableSaving) {
            controller.saveToFile(secondaryStage.scene, "$pathToSave${controller.getNameOfExp()}TRvsDelay.png")
        }
    }
}