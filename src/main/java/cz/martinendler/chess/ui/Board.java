package cz.martinendler.chess.ui;

import cz.martinendler.chess.engine.board.File;
import cz.martinendler.chess.engine.board.Rank;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.css.*;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A chessboard
 * <p>
 * It handles positioning of its children nodes (instances of `Square` and `BorderSegment`).
 * It is <strong>fully responsive</strong>. It automatically adapts the layout
 * according to the available width from its parent.
 */
public class Board extends Region {

	private static final Logger log = LoggerFactory.getLogger(Board.class);

	// ### START: custom CSS properties
	// see: https://riptutorial.com/javafx/example/11261/extending-rectangle-adding-new-stylable-properties
	// see also: https://openjfx.io/javadoc/16/javafx.graphics/javafx/css/StyleableProperty.html

	private final StyleableDoubleProperty customArcClipRelative = new SimpleStyleableDoubleProperty(
		CUSTOM_ARC_CLIP_RELATIVE_META_DATA, this, "customArcClipRelative"
	);
	private final StyleableDoubleProperty customArcClipAbsolute = new SimpleStyleableDoubleProperty(
		CUSTOM_ARC_CLIP_ABSOLUTE_META_DATA, this, "customArcClipAbsolute"
	);

	private final static List<CssMetaData<? extends Styleable, ?>> CLASS_CSS_META_DATA;

	// css metadata for the customArcClipRelative property
	// specify property name as -fx-custom-arc-clip-relative
	// and use converter for numbers
	private final static CssMetaData<Board, Number> CUSTOM_ARC_CLIP_RELATIVE_META_DATA
		= new CssMetaData<>("-fx-custom-arc-clip-relative", StyleConverter.getSizeConverter(), 1.0) {

		@Override
		public boolean isSettable(Board styleable) {
			// property can be set iff the property is not bound
			return !styleable.customArcClipRelative.isBound();
		}

		@Override
		public StyleableProperty<Number> getStyleableProperty(Board styleable) {
			// extract the property from the styleable
			return styleable.customArcClipRelative;
		}

	};

	// css metadata for the customArcClipAbsolute property
	// specify property name as -fx-custom-arc-clip-absolute
	// and use converter for numbers
	private final static CssMetaData<Board, Number> CUSTOM_ARC_CLIP_ABSOLUTE_META_DATA
		= new CssMetaData<>("-fx-custom-arc-clip-absolute", StyleConverter.getSizeConverter(), 0.0) {

		@Override
		public boolean isSettable(Board styleable) {
			return !styleable.customArcClipAbsolute.isBound();
		}

		@Override
		public StyleableProperty<Number> getStyleableProperty(Board styleable) {
			return styleable.customArcClipAbsolute;
		}
	};

	static {

		// combine already available properties in Region with new properties
		List<CssMetaData<? extends Styleable, ?>> parent = Region.getClassCssMetaData();
		List<CssMetaData<? extends Styleable, ?>> additional = List.of(
			CUSTOM_ARC_CLIP_RELATIVE_META_DATA, CUSTOM_ARC_CLIP_ABSOLUTE_META_DATA
		);

		// create arraylist with suitable capacity
		List<CssMetaData<? extends Styleable, ?>> own = new ArrayList<>(parent.size() + additional.size());

		// fill list with old and new metadata
		own.addAll(parent);
		own.addAll(additional);

		// make sure the metadata list is not modifiable
		CLASS_CSS_META_DATA = Collections.unmodifiableList(own);

	}

	// make metadata available for extending the class
	public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
		return CLASS_CSS_META_DATA;
	}

	// returns a list of the css metadata for the stylable properties of the Node
	@Override
	public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
		return CLASS_CSS_META_DATA;
	}

	// ### END: custom CSS properties

	public final static int NUM = 8;
	public final static int NUM_ROWS = NUM;
	public final static int NUM_COLS = NUM;

	// TODO: support vertical flip
	// TODO: support move highlighting

	protected final static boolean clipSupported = Platform.isSupported(ConditionalFeature.SHAPE_CLIP);
	protected final @Nullable Rectangle clipRectangle;

	protected final Square[][] squares;
	protected final BorderSegment[][] borderCorners;
	protected final BorderDescription[][] rowDescriptions;
	protected final BorderDescription[][] colDescriptions;

	protected double availableSize;
	protected double unitSize;
	protected double borderSize;
	protected double squareSize;
	protected double boardSize = -1;

	protected double clipArcRel = 0.0;
	protected double clipArcAbs = 0.0;

	private final @NotNull SimpleObjectProperty<Square> moveOrigin = new SimpleObjectProperty<>(
		null, "moveOrigin", null
	);

	private final @NotNull SimpleLongProperty legalMoves = new SimpleLongProperty(
		null, "legalMoves", 0L
	);

	private final @NotNull SimpleLongProperty highlights = new SimpleLongProperty(
		null, "highlights", 0L
	);

	private @Nullable MoveAttemptHandler moveAttemptHandler;

	public Board() {
		super();

		squares = new Square[NUM_ROWS][NUM_COLS];
		borderCorners = new BorderSegment[2][2];
		rowDescriptions = new BorderDescription[2][NUM_ROWS];
		colDescriptions = new BorderDescription[2][NUM_COLS];

		getStyleClass().add("board");

		if (clipSupported) {
			clipRectangle = new Rectangle(0.0, 0.0);
			setClip(clipRectangle);
		} else {
			clipRectangle = null;
		}

		initChildren();

		// setOnMouseClicked((MouseEvent event) -> {
		// 	log.info("onMouseClicked: source = {}, target = {}", event.getSource(), event.getTarget());
		// });

		customArcClipRelative.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {

			if (clipRectangle != null) {
				clipArcRel = newValue.doubleValue();
				log.info("customArcClipRelative changed: clipArcRel = {}", clipArcRel);
				updateClipRectangle();
			}

		});

		customArcClipAbsolute.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {

			if (clipRectangle != null) {
				clipArcAbs = newValue.doubleValue();
				log.info("customArcClipAbsolute changed: clipArcAbs = {}", clipArcAbs);
				updateClipRectangle();
			}

		});

		moveAttemptHandler = null;

	}

	public @Nullable MoveAttemptHandler getMoveAttemptHandler() {
		return moveAttemptHandler;
	}

	public void setMoveAttemptHandler(@Nullable MoveAttemptHandler moveAttemptHandler) {
		this.moveAttemptHandler = moveAttemptHandler;
	}

	public @NotNull SimpleObjectProperty<Square> moveOriginProperty() {
		return moveOrigin;
	}

	public @Nullable Square getMoveOrigin() {
		return moveOrigin.get();
	}

	public void setMoveOrigin(@Nullable Square moveOrigin) {
		log.info("setMoveOrigin: {}", moveOrigin);
		this.moveOrigin.set(moveOrigin);
	}

	public void updateMove(@NotNull Square originOrDestination) {

		log.info("updateMove: {}", originOrDestination);

		if (getMoveOrigin() != null && (getLegalMoves() & originOrDestination.getSquare().getBitboard()) != 0L) {
			// TODO: inform controller
			log.info("legal move from " + getMoveOrigin() + " to " + originOrDestination);
			if (moveAttemptHandler != null) {
				moveAttemptHandler.onLegalMoveAttempt(getMoveOrigin(), originOrDestination);
			}
			return;
		}

		if (getMoveOrigin() == originOrDestination) {
			setMoveOrigin(null);
			return;
		}

		if (originOrDestination.getPiece() != null) {
			setMoveOrigin(originOrDestination);
			return;
		}

		setMoveOrigin(null);

	}

	public long getLegalMoves() {
		return legalMoves.get();
	}

	public @NotNull SimpleLongProperty legalMovesProperty() {
		return legalMoves;
	}

	public void setLegalMoves(long legalMoves) {
		this.legalMoves.set(legalMoves);
	}

	public long getHighlights() {
		return highlights.get();
	}

	public @NotNull SimpleLongProperty highlightsProperty() {
		return highlights;
	}

	public void setHighlights(long highlights) {
		log.info("setHighlights " + Long.toBinaryString(highlights));
		this.highlights.set(highlights);
	}

	public void clean() {
		setMoveOrigin(null);
		for (int r = 0; r < NUM_ROWS; r++) {
			for (int c = 0; c < NUM_COLS; c++) {
				squares[r][c].setPiece(null);
			}
		}
	}

	protected boolean areInitialSizesComputed() {
		return borderSize != -1;
	}

	protected void updateClipRectangle() {
		if (areInitialSizesComputed() && clipRectangle != null) {
			clipRectangle.setWidth(boardSize);
			clipRectangle.setHeight(boardSize);
			double arcSize = clipArcAbs != 0
				? clipArcAbs // absolute size has precedence
				: clipArcRel * 2 * borderSize;
			clipRectangle.setArcWidth(arcSize);
			clipRectangle.setArcHeight(arcSize);
			log.info("updateClipRectangle: size = {}, arcSize = {}", boardSize, arcSize);
		}
	}

	protected void addUnmanagedChild(Node child) {
		child.setManaged(false);
		getChildren().add(child);
	}

	protected void initChildren() {

		boolean light;

		// squares
		light = false;
		for (int r = 0; r < NUM_ROWS; r++) {
			light = !light;
			for (int c = 0; c < NUM_COLS; c++) {
				light = !light;
				squares[r][c] = new Square(cz.martinendler.chess.engine.board.Square.encode(r, c));
				addUnmanagedChild(squares[r][c]);
			}
		}

		// rank border segment corners
		light = true;
		for (int r = 0; r < NUM_ROWS; r++) {
			String desc = Rank.fromIndex(r).getNotation().toLowerCase();
			rowDescriptions[0][r] = new BorderDescription(light, desc);
			light = !light;
			rowDescriptions[1][r] = new BorderDescription(light, desc);
			addUnmanagedChild(rowDescriptions[0][r]);
			addUnmanagedChild(rowDescriptions[1][r]);
		}

		// file border segment corners
		light = true;
		for (int c = 0; c < NUM_COLS; c++) {
			String desc = File.fromIndex(c).getNotation().toLowerCase();
			colDescriptions[0][c] = new BorderDescription(light, desc);
			light = !light;
			colDescriptions[1][c] = new BorderDescription(light, desc);
			addUnmanagedChild(colDescriptions[0][c]);
			addUnmanagedChild(colDescriptions[1][c]);
		}

		// border segment corners
		light = false;
		for (int r = 0; r < 2; r++) {
			light = !light;
			for (int c = 0; c < 2; c++) {
				light = !light;
				borderCorners[r][c] = new BorderSegment(light);
				addUnmanagedChild(borderCorners[r][c]);
			}
		}

	}

	protected void recomputeSizes(double width, double height) {

		availableSize = Double.min(width, height);
		unitSize = availableSize / 70;
		borderSize = Math.floor(3 * unitSize);
		squareSize = Math.floor(64 * unitSize / NUM);
		boardSize = (2 * borderSize) + (NUM * squareSize);

		log.info(
			"recomputeSizes: w = {}, h = {}," +
				" cw = {}, ch = {}," +
				" available = {}," +
				" unit = {}," +
				" border = {}," +
				" square = {}," +
				" board = {}," +
				" clip = {}",
			width, height,
			getWidth(), getHeight(),
			availableSize,
			unitSize,
			borderSize,
			squareSize,
			boardSize,
			clipSupported
		);

	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();

		log.info("layoutChildren");

		if (!areInitialSizesComputed()) {
			log.info("layoutChildren: initial sizes are not computed yet");
			recomputeSizes(getWidth(), getHeight());
		}

		updateClipRectangle();

		// layout (x,y) = (0,0) is at the top-left corner
		// BUT board (r,c) = (0,0) is the bottom-left corner

		for (int r = 0; r < NUM_ROWS; r++) {
			for (int c = 0; c < NUM_COLS; c++) {

				Square square = squares[r][c];

				square.resizeRelocate(
					borderSize + c * squareSize, borderSize + (7 - r) * squareSize,
					squareSize, squareSize
				);

			}
		}

		for (int c = 0; c < NUM_COLS; c++) {
			BorderSegment desc0 = colDescriptions[0][c];
			desc0.resizeRelocate(
				borderSize + c * squareSize, borderSize + NUM * squareSize,
				squareSize, borderSize
			);
			BorderSegment desc1 = colDescriptions[1][c];
			desc1.resizeRelocate(
				borderSize + c * squareSize, 0,
				squareSize, borderSize
			);
		}

		for (int r = 0; r < NUM_ROWS; r++) {
			BorderSegment desc0 = rowDescriptions[0][r];
			desc0.resizeRelocate(
				0, borderSize + (7 - r) * squareSize,
				borderSize, squareSize
			);
			BorderSegment desc1 = rowDescriptions[1][r];
			desc1.resizeRelocate(
				borderSize + NUM * squareSize, borderSize + (7 - r) * squareSize,
				borderSize, squareSize
			);
		}

		for (int r = 0; r < 2; r++) {
			for (int c = 0; c < 2; c++) {
				BorderSegment corner = borderCorners[r][c];
				corner.resizeRelocate(
					c * (borderSize + NUM * squareSize), (1 - r) * (borderSize + NUM * squareSize),
					borderSize, borderSize
				);
			}
		}

	}

	public Square getSquareAt(int r, int c) {
		return squares[r][c];
	}

	public Square getSquare(cz.martinendler.chess.engine.board.Square square) {
		return squares[square.getRank().ordinal()][square.getFile().ordinal()];
	}

	@Override
	public void resize(double width, double height) {
		log.info(
			"resize: w = {}, h = {}," +
				" cw = {}, ch = {}",
			width, height,
			getWidth(), getHeight()
		);
		if (width != getWidth() || height != getHeight()) {
			recomputeSizes(width, height);
			setWidth(boardSize);
			setHeight(boardSize);
		}
	}

}
