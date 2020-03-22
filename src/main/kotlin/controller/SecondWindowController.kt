package controller

import javafx.scene.chart.XYChart
import model.DataRepository
import java.nio.file.Path

class SecondWindowController {
    private val dataRepository: DataRepository = DataRepository.newInstance()

    fun createSeriesOfXR() : XYChart.Series<Double,Double>{
        return dataRepository.createSeriesOfWaveForm(dataRepository.currentLineChartOfXR, 0, 5500)
    }

    fun createSeriesOfTR() : XYChart.Series<Double,Double>{
        return dataRepository.createSeriesOfWaveForm(dataRepository.currentLineChartOfTR, 1500, 3000)
    }

    fun getFileName() : String{
        return dataRepository.currentLineChartOfXR.substringAfterLast("_")
    }
}