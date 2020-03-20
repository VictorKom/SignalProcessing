import javafx.application.*
import javafx.stage.Stage
import view.MainViewImpl


class Main : Application() {

    override fun start(primaryStage: Stage?) {
        MainViewImpl().start(primaryStage)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }

}