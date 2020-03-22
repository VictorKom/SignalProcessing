package view

import Main
import controller.MainSceneController
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.XYChart
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextArea
import javafx.scene.control.Tooltip
import javafx.stage.Stage
import model.Parameters

class MainViewImpl : MainView {

    private val controller: MainSceneController = MainSceneController(this)
    @FXML
  //  lateinit var fileChooser: Button
    lateinit var clearData: Button
    lateinit var addChart: Button
    lateinit var info: TextArea
    lateinit var scatterChart: ScatterChart<Double,Double>
    lateinit var sweepTimeChooser: ComboBox<String>

    @FXML
    fun initialize(){
        sweepTimeChooser.selectionModel.selectedItemProperty().
        addListener { _: ObservableValue<out String>, _: String?, newValue: String? ->
            info.text = newValue ?: "" }
       // fileChooser.setOnAction { controller.chooseDir() }
        clearData.setOnAction {
            scatterChart.data.clear()
            info.clear() }
        addChart.setOnAction {
            controller.chooseDir()
            scatterChart.data.add(controller.createSeries())
            createNodeLabel(scatterChart) }
    }

    fun start(primaryStage: Stage?) {
        primaryStage?.scene = Scene(FXMLLoader.load(Main::class.java.getResource("main.fxml")))
        primaryStage?.show()
    }

    override fun refreshTextArea(parameters: Parameters) {
        info.appendText("$parameters\n")
    }

    private fun createNodeLabel(chart: XYChart<Double,Double>) {
        for (series in chart.data) {
            for (data in series.data) {
                val label = "( %.2f ; %.2f )".format(data.xValue, data.yValue)
                val node = data.node
                val tooltip = Tooltip(label)
                tooltip.style = "-fx-font-size: 17"
                Tooltip.install(node, tooltip)
                val pathsToFiles = data.extraValue.toString().split("\t")
                node.setOnMouseClicked {
                    controller.setCurrentLineChart(pathsToFiles, data.yValue)
                    SecondWindow().createStage() }
            }
        }
    }

}