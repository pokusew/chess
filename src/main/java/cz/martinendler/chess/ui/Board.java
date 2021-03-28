package cz.martinendler.chess.ui;

import cz.martinendler.chess.ui.controllers.PageOneController;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;

public class Board extends Region {

	private static final Logger log = LoggerFactory.getLogger(Board.class);

	public Board() {

	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();

		log.info(MessageFormat.format("layoutChildren w: {0} h: {1}", getWidth(), getHeight()));

		double boardSize = Double.min(getWidth(), getHeight());

		double squareSize = Math.floor(boardSize / 8);

		log.info(MessageFormat.format("boardSize = {0}, squareSize = {1}", boardSize, squareSize));

		List<Node> children = getManagedChildren();

		for (int i = 0; i < children.size(); i++) {
			final Node child = children.get(i);

			int row = i / 8;
			int column = i % 8;

			child.resizeRelocate(
				row * squareSize, column * squareSize,
				squareSize, squareSize
			);
			// log.info("i = " + i);
		}
	}

	@Override
	public ObservableList<Node> getChildren() {
		return super.getChildren();
	}

}
