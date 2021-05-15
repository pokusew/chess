package cz.martinendler.chess.ui;

import javafx.scene.shape.Rectangle;

/**
 * A {@link Rectangle} that is resizable (as big as possible)
 */
public class ResponsiveRectangle extends Rectangle {

	// private static final Logger log = LoggerFactory.getLogger(ResponsiveRectangle.class);

	@Override
	public double minWidth(double height) {
		return 0.0;
	}

	@Override
	public double minHeight(double width) {
		return 0.0;
	}

	@Override
	public double maxWidth(double height) {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public double maxHeight(double width) {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	public void resize(double width, double height) {
		// log.info("width = {}, height = {}", width, height);
		setWidth(width);
		setHeight(height);
	}

}
