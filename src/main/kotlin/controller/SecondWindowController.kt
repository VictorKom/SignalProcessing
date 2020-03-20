package controller

import javafx.scene.chart.XYChart
import model.DataRepository
import java.nio.file.Path

class SecondWindowController {
    private val dataRepository: DataRepository = DataRepository.newInstance()

    fun createSeries(pathToFile: String) : XYChart.Series<Double,Double>{
        return dataRepository.createSeriesOfWaveForm(pathToFile)
    }
}