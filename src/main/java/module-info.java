module chess {

	requires javafx.controls;
	requires javafx.fxml;

	// requires org.slf4j;

	opens cz.martinendler.chess to javafx.fxml;

	exports cz.martinendler.chess;

}
