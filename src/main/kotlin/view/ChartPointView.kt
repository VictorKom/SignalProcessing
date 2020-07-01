package view

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.stage.Stage
import Main
import controller.ChartPointController
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField

class ChartPointView {

    private val controller: ChartPointController = ChartPointController(this)
    private val secondStage = Stage()
    @FXML
    lateinit var chartName: Label
    lateinit var xrLineChart: LineChart<Double,Double>
    lateinit var trLineChart: LineChart<Double,Double>

    @FXML
    fun initialize(){
        chartName.text = controller.getFileInfo()
        xrLineChart.data.add(controller.createSeriesOfXR())
        trLineChart.data.add(controller.createSeriesOfTR())
    }

    fun createStage() {
        secondStage.title = "Signals of X-Ray and TR"
        secondStage.scene = Scene(FXMLLoader.load(Main::class.java.getResource("charPointView.fxml")))
        secondStage.show()
        //controller.saveToFile(secondStage.scene, "C:/Эксперимент/heap/${controller.getNameOfExp()}.png")
        //secondStage.close()
    }


}