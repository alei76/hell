package ps.hell.ml.nlp.tool.aliasi.test;

import ps.hell.ml.nlp.tool.aliasi.matrix.SparseFloatVector;
import ps.hell.ml.nlp.tool.aliasi.matrix.Vector;
import ps.hell.ml.nlp.tool.aliasi.symbol.MapSymbolTable;
import ps.hell.ml.nlp.tool.aliasi.symbol.SymbolTable;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.TokenFeatureExtractor;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.TokenizerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ExtractFeatures {
	public static Vector[] featureVectors(String[] texts,
			SymbolTable symbolTable) {
		Vector[] vectors = new Vector[texts.length];
		TokenizerFactory tokenizerFactory = new IndoEuropeanTokenizerFactory();
		TokenFeatureExtractor featureExtractor = new TokenFeatureExtractor(
				tokenizerFactory);
		for (int i = 0; i < texts.length; ++i) {
			Map featureMap =featureExtractor.features(texts[i]);
			vectors[i] = toVectorAddSymbols(featureMap, symbolTable,
					Integer.MAX_VALUE);
		}
		return vectors;
	}

	public static SparseFloatVector toVectorAddSymbols(Map<String,Number> featureVector,
			SymbolTable table, int numDimensions) {
		int size = (featureVector.size() * 3) / 2;
		Map vectorMap = new HashMap(size);
		for (Entry<String,Number> entry : featureVector.entrySet()) {
			String feature = entry.getKey();
			Number val = entry.getValue();
			int id = table.getOrAddSymbol(feature);
			vectorMap.put(new Integer(id), val);
		}
		return new SparseFloatVector(vectorMap, numDimensions);
	}

	public static void main(String[] args) {
		args = new String[] { "this is a book", "go to school","is a school"

		};
		SymbolTable symbolTable = new MapSymbolTable();
		Vector[] vectors = featureVectors(args, symbolTable);
		System.out.println("VECTORS");
		for (int i = 0; i < vectors.length; ++i)
			System.out.println(i + ") " + vectors[i]);
		System.out.println(" SYMBOL TABLE");
		System.out.println(symbolTable);
	}
}