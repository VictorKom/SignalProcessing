package utils

import javafx.scene.chart.XYChart
import java.io.File


class TxtSaver {
    companion object {
        fun saveToTxtFile(
            seriesTR: XYChart.Series<String, Double>,
            seriesXR: XYChart.Series<String, Double>, pathToTxt: String
        ) {
            val file = File(pathToTxt)
            val sb = StringBuilder()
            val sep = System.getProperty("line.separator")
            sb.append("ns\tTR\tns\tXR$sep")
            for (i in 0 until seriesTR.data.size){
                val xr = "%.1f".format(seriesXR.data[i].yValue).replace(",",".")
                val tr = "%.1f".format(seriesTR.data[i].yValue).replace(",",".")
                val delay = seriesTR.data[i].xValue
                sb.append("$delay\t$tr\t$delay\t$xr$sep")
            }
            file.writeText(sb.toString())
        }

        fun saveToTxtFileBars(
            seriesTR: XYChart.Series<String, Double>,
            seriesXR: XYChart.Series<String, Double>, pathToTxt: String
        ) {
            val file = File(pathToTxt)
            val sb = StringBuilder()
            val sep = System.getProperty("line.separator")
            //sb.append("ns\tTR\tns\tXR$sep")
            for (i in 0 until seriesTR.data.size){
                val xr = "%.1f".format(seriesXR.data[i].yValue).replace(",",".")
                val tr = "%.1f".format(seriesTR.data[i].yValue).replace(",",".")
                //val delay = seriesTR.data[i].xValue
                sb.append("${i+1}\t$tr\t$xr$sep")
            }
            file.writeText(sb.toString())
        }

        fun saveToTxtFileScatter(
            seriesTR: XYChart.Series<Double, Double>, pathToTxt: String
        ) {
            val file = File(pathToTxt)
            val sb = StringBuilder()
            val sep = System.getProperty("line.separator")
            for (i in 0 until seriesTR.data.size){
                val delay = "%.1f".format(seriesTR.data[i].xValue).replace(",",".")
                val tr = "%.1f".format(seriesTR.data[i].yValue).replace(",",".")
                //val delay = seriesTR.data[i].xValue
                sb.append("$delay\t$tr$sep")
            }
            file.writeText(sb.toString())
        }
    }
}