package view

import Main
import controller.MainViewController
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.XYChart
import javafx.scene.control.*
import javafx.stage.Stage
import model.OneExperiment

class MainViewImpl : MainView {

    private val controller: MainViewController = MainViewController(this)

    @FXML
    lateinit var clearData: Button
    lateinit var addChart: Button
    lateinit var plotAllSelected: Button
    lateinit var oneExpSave: CheckBox
    lateinit var plotTRvsXRDelay: Button
    lateinit var tRvsXRDelaySave: CheckBox
    lateinit var isBarChart: CheckBox
    lateinit var info: TextArea
    lateinit var pathToSave: TextField
    lateinit var scatterChart: ScatterChart<Double,Double>
    //lateinit var sweepTimeChooser: ComboBox<String>

    @FXML
    fun initialize(){
/*        sweepTimeChooser.selectionModel.selectedItemProperty().
        addListener { _: ObservableValue<out String>, _: String?, newValue: String? ->
            info.text = newValue ?: "" }*/
        isBarChart.isSelected = true
        clearData.setOnAction {
            controller.clearData()
            scatterChart.data.clear()
            info.clear() }
        addChart.setOnAction {
            controller.chooseDir()
            scatterChart.data.add(controller.createSeries())
            createNodeLabel(scatterChart) }
        plotAllSelected.setOnAction { controller.plotAllSelected(pathToSave.text, oneExpSave.isSelected) }
        plotTRvsXRDelay.setOnAction { controller.plotTRvsXRDelay(pathToSave.text, tRvsXRDelaySave.isSelected, isBarChart.isSelected) }
    }

    fun start(primaryStage: Stage?) {
        primaryStage?.title = "Signal-processing"
        primaryStage?.scene = Scene(FXMLLoader.load(Main::class.java.getResource("mainView.fxml")))
        primaryStage?.show()
    }

    override fun refreshTextArea(currentExperiment: OneExperiment) {
        info.appendText("$currentExperiment\n")
    }

    private fun createNodeLabel(chart: XYChart<Double,Double>) {
        for (series in chart.data) {
            for (data in series.data) {
                val label = "( %.1f ; %.1f )".format(data.xValue, data.yValue)
                val node = data.node
                val tooltip = Tooltip(label)
                tooltip.style = "-fx-font-size: 17"
                Tooltip.install(node, tooltip)
                val property = data.extraValue.toString().split("\t")
               node.setOnMouseClicked { controller.plotCurrentLineChart(property, data.yValue) }
               // controller.plotCurrentLineChart(property, data.yValue)
            }
        }
    }

}