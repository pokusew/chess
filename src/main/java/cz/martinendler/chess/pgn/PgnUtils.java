package cz.martinendler.chess.pgn;

import cz.martinendler.chess.pgn.antlr4.PGNLexer;
import cz.martinendler.chess.pgn.antlr4.PGNParser;
import cz.martinendler.chess.pgn.entity.PgnDatabase;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * Utils for working with Portable Game Notation (PGN)
 *
 * @see <a href="http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm">PGN satndard specification</a>
 */
public class PgnUtils {

	/**
	 * Escapes a string to be used as a PGN string token
	 *
	 * @param str string to escape, can be null (in that case, an empty string will be returned {@code ""})
	 * @return an escaped string as a PGN string token
	 * @see <a href="http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c7">Tokens in the PGN specification</a>
	 */
	public static String escapeString(@Nullable String str) {

		// A quote inside a string is represented by the backslash
		// immediately followed by a quote. A backslash inside a string is represented by
		// two adjacent backslashes.

		if (str == null) {
			return "";
		}

		return str
			.replace("\\", "\\\\")  // \ -> \\
			.replace("\"", "\\\""); // " -> \"

	}

	/**
	 * Creates a tag pair string from tag name and value
	 *
	 * @param tagName  tag name
	 * @param tagValue tag value (is automatically escaped via {@link PgnUtils#escapeString(String str)}
	 * @return a tag pair
	 * @see <a href="http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c8.1">Tag pair section in the PGN specification</a>
	 */
	public static String tagToString(String tagName, String tagValue) {
		return "[" + tagName + " \"" + escapeString(tagValue) + "\"]";
	}

	private static @NotNull String recognitionExceptionToMessage(@NotNull RecognitionException e) {

		int line = e.getOffendingToken().getLine();
		int charPositionInLine = e.getOffendingToken().getCharPositionInLine();

		return "a recognition error at line " + line + ":" + charPositionInLine;

	}

	private static @NotNull ErrorCollectorListener setupErrorHandling(@NotNull PGNParser parser) {

		// BailErrorStrategy throws a ParseCancellationException on first syntax error
		// and does NOT attempt any error recovery
		// parser.setErrorHandler(new BailErrorStrategy());

		// BUT we will use the DefaultErrorStrategy
		// so we can more easily collect meaningful error messages (which are created by DefaultErrorStrategy)
		ErrorCollectorListener errorCollectorListener = new ErrorCollectorListener();
		parser.removeErrorListeners();
		parser.addErrorListener(errorCollectorListener);

		return errorCollectorListener;

	}

	private static @NotNull <T> T parseAndHandleErrors(CharStream input, Function<PGNParser, T> parserFunction) throws PgnParseException {

		// create lexer and parser
		PGNLexer lexer = new PGNLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PGNParser parser = new PGNParser(tokens);

		// setup custom error handling
		ErrorCollectorListener errorCollectorListener = setupErrorHandling(parser);

		// parse and handle RecognitionException (just to be sure, because DefaultErrorStrategy
		// normally does not throw any exceptions during parsing)
		T tree;
		try {

			tree = parserFunction.apply(parser);

		} catch (RecognitionException e) {

			String friendlyMessage = recognitionExceptionToMessage(e);

			throw new PgnParseException(friendlyMessage, e);

		}

		if (errorCollectorListener.hasAnyErrors()) {
			throw new PgnParseException(
				"The following errors occurred during PGN parsing:\n"
					+ errorCollectorListener.getErrors()
			);
		}

		return tree;

	}

	/**
	 * Parses the given PGN file into a {@link PgnDatabase}
	 *
	 * @param fileName path to the PGN file
	 * @return a PGN database (all games inside the the given PGN file)
	 * @throws IOException       when there is an error loading the file (e.g.: the file does not exist)
	 * @throws PgnParseException where here is an parsing error
	 */
	public static @NotNull PgnDatabase parseFile(@NotNull String fileName) throws IOException, PgnParseException {

		// convert file to a char stream
		CharStream input = CharStreams.fromFileName(fileName);

		// parse into an AST
		PGNParser.ParseContext tree = parseAndHandleErrors(input, PGNParser::parse);

		// use listener design pattern to automatically walk trough the AST
		// and convert it into a PgnDatabase object
		PgnListener pgnListener = new PgnListener();
		ParseTreeWalker.DEFAULT.walk(pgnListener, tree);

		PgnDatabase pgnDatabase = pgnListener.getDatabase();

		// every PGN file contains exactly one PGN database
		// so it the parsing went okay, pgnDatabase cannot be null
		if (pgnDatabase == null) {
			throw new PgnParseException("No PGN database in the parsed PGN file.");
		}

		return pgnDatabase;

	}

	/**
	 * Parses the given PGN move text into a SAN moves array
	 *
	 * @param moveText PGN move text
	 * @return a SAN moves array
	 * @throws PgnParseException where here is an parsing error
	 */
	public static @NotNull List<@NotNull String> parseMoveText(@NotNull String moveText) throws PgnParseException {

		// convert string to a char stream
		CharStream input = CharStreams.fromString(moveText);

		// parse the given move text into an AST
		PGNParser.Movetext_sectionContext tree = parseAndHandleErrors(input, PGNParser::movetext_section);

		// use listener design pattern to automatically walk trough the AST
		// and convert it into a SAN moves array
		PgnMoveTextListener listener = new PgnMoveTextListener();
		ParseTreeWalker.DEFAULT.walk(listener, tree);

		return listener.getMoves();

	}

}
