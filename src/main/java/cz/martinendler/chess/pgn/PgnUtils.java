package cz.martinendler.chess.pgn;

import cz.martinendler.chess.pgn.antlr4.PGNLexer;
import cz.martinendler.chess.pgn.antlr4.PGNParser;
import cz.martinendler.chess.pgn.entity.PgnDatabase;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;

public class PgnUtils {

	public static PgnDatabase parseFile(String fileName) throws IOException {

		// TODO: error reporting (see the Book)

		// create lexer and parser
		PGNLexer lexer = new PGNLexer(CharStreams.fromFileName(fileName));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PGNParser parser = new PGNParser(tokens);

		// parse the file into a AST
		PGNParser.ParseContext tree = parser.parse();

		// use listener design pattern to automatically walk trough the AST
		// and convert it into a PgnDatabase object
		PgnListener pgnListener = new PgnListener();
		ParseTreeWalker.DEFAULT.walk(pgnListener, tree);

		PgnDatabase pgnDatabase = pgnListener.getDatabase();

		// every PGN file contains exactly one PGN database
		// so it the parsing went okay, pgnDatabase cannot be null
		assert pgnDatabase != null;

		return pgnDatabase;

	}

}
