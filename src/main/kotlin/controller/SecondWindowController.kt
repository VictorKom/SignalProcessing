package controller

import javafx.scene.chart.XYChart
import model.DataRepository
import java.nio.file.Path

class SecondWindowController {
    private val dataRepository: DataRepository = DataRepository.newInstance()

    fun createSeriesOfXR() : XYChart.Series<Double,Double>{
        return dataRepository.createSeriesOfWaveFormXR(0, 7000)
    }

    fun createSeriesOfTR() : XYChart.Series<Double,Double>{
        return dataRepository.createSeriesOfWaveFormTR(1000, 5000)
    }

    fun getFileName() : String{
        return "${dataRepository.currentLineChartOfXR.substringAfterLast("_")}\n" +
                "%.1f".format(dataRepository.amplitudeOfTR)
    }
}