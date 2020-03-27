package view

import model.OneExperiment

interface MainView {
    fun refreshTextArea( currentExperiment: OneExperiment )

}