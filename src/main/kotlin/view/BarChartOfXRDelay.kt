package view

import Main
import controller.ChartTRvsXRDelayController
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.chart.BarChart
import javafx.stage.Stage

class BarChartOfXRDelay {
    private val controller = ChartTRvsXRDelayController()

    @FXML
    lateinit var barChartTR: BarChart<String, Double>
    lateinit var barChartIntegral: BarChart<String, Double>

    @FXML
    fun initialize(){
       barChartTR.data.add(controller.getBarChartTRvsXRDelay())
       barChartIntegral.data.add(controller.getBarChartIntegralvsXRDelay())
        val d = barChartTR.data[0].name
        controller.saveToFile(barChartTR.data[0], barChartIntegral.data[0], "C:/Эксперимент/${controller.getNameOfExp()}  $d.txt")
    }

    fun createStage(pathToSave: String, enableSaving: Boolean) {
        val secondaryStage = Stage()
        secondaryStage.title = "Charts of TR vs XRDelay"
        secondaryStage.scene = Scene(FXMLLoader.load(Main::class.java.getResource("barChartOfXRDelay.fxml")))
        secondaryStage.show()
        if (enableSaving) {
            controller.saveToFile(secondaryStage.scene, "$pathToSave${controller.getNameOfExp()}BarTRvsDelay.png")

        }
    }
}