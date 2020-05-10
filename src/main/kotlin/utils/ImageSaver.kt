package utils

import javafx.embed.swing.SwingFXUtils
import javafx.scene.Scene
import javafx.scene.image.WritableImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

class ImageSaver {

    companion object {
        fun saveToFile(scene: Scene, pathToImage: String) {
            val image: WritableImage = scene.snapshot(null)
            val file = File(pathToImage)
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}