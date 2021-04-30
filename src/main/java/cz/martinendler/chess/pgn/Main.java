package cz.martinendler.chess.pgn;

import cz.martinendler.chess.pgn.antlr4.PGNLexer;
import cz.martinendler.chess.pgn.antlr4.PGNParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * A small demo class that demonstrates how to use the
 * generated parser classes.
 */
public class Main {

	public static void main(String[] args) throws Exception {

		String fileName = "src/test/resources/example.pgn";
		System.out.printf("Parsing `%s`...", fileName);



		System.out.println("\nDone!");

	}

}
