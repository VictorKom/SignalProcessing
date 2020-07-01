package view

import Main
import controller.ChartsOfOneExpController
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.StackedBarChart
import javafx.scene.chart.XYChart
import javafx.stage.Stage

class ChartsOfOneExpView {
    private val controller: ChartsOfOneExpController = ChartsOfOneExpController(this)

    @FXML
    lateinit var scatterChart: ScatterChart<Double,Double>
    lateinit var barChart:  StackedBarChart<String, Double>

    @FXML
    fun initialize(){
        val chartsOfOneExperiment = controller.getChartsOfOneExperiments(0, false)
        scatterChart.data.add(chartsOfOneExperiment["scatter"] as XYChart.Series<Double, Double>)
        barChart.data.add(chartsOfOneExperiment["lineXR"] as XYChart.Series<String, Double>)
        barChart.data.add(chartsOfOneExperiment["lineTR"] as XYChart.Series<String, Double>)
        //controller.saveToFile(barChart.data[0], barChart.data[1], "C:/Эксперимент/${controller.getNameOfExp(0)}.txt")
    }

    fun createStage(pathToSave: String, enableSaving: Boolean) {
        val secondaryStage = Stage()
        secondaryStage.title = "Charts of one experiment"
        secondaryStage.scene = Scene(FXMLLoader.load(Main::class.java.getResource("seriesChartsView.fxml")))
        secondaryStage.show()
        if (enableSaving) {
            controller.saveToFile(secondaryStage.scene, "$pathToSave${controller.getNameOfExp(0)}.png")
        }
    }
}