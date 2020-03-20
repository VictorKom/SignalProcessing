package view

import model.Parameters
import java.nio.file.Path

interface MainView {
    fun refreshTextArea( parameters: Parameters ){}

}