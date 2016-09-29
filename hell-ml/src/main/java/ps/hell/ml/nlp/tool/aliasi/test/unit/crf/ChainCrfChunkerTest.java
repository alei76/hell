package ps.hell.ml.nlp.tool.aliasi.test.unit.crf;

import ps.hell.ml.nlp.tool.aliasi.chunk.*;
import ps.hell.ml.nlp.tool.aliasi.corpus.Corpus;
import ps.hell.ml.nlp.tool.aliasi.corpus.ObjectHandler;
import ps.hell.ml.nlp.tool.aliasi.crf.ChainCrf;
import ps.hell.ml.nlp.tool.aliasi.crf.ChainCrfChunker;
import ps.hell.ml.nlp.tool.aliasi.io.LogLevel;
import ps.hell.ml.nlp.tool.aliasi.io.Reporter;
import ps.hell.ml.nlp.tool.aliasi.io.Reporters;
import ps.hell.ml.nlp.tool.aliasi.stats.AnnealingSchedule;
import ps.hell.ml.nlp.tool.aliasi.stats.RegressionPrior;
import ps.hell.ml.nlp.tool.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;

public class ChainCrfChunkerTest {


    @Test
    public void test1() throws IOException {
        boolean addIntercept = false;
        int minFeatureCount = 1;
        boolean cacheFeatureVectors = false;
        double minImprovement = 0.00000001;
        int minEpochs = 10;
        int maxEpochs = 10000;
        int priorBlockSize = 3;
        Reporter reporter
            = Reporters.stdOut();
        reporter.setLevel(LogLevel.WARN);

        ChainCrfChunker chunker
            = ChainCrfChunker.estimate(TrainCorpus1.INSTANCE,
                                       TAG_CHUNK_CODEC,
                                       IndoEuropeanTokenizerFactory.INSTANCE,
                                       ChainCrfTest.FEATURE_EXTRACTOR,
                                       addIntercept,
                                       minFeatureCount,
                                       cacheFeatureVectors,
                                       RegressionPrior.noninformative(), // laplace(20,addIntercept),
                                       priorBlockSize,
                                       AnnealingSchedule.exponential(0.005,0.9999),
                                       minImprovement,
                                       minEpochs,
                                       maxEpochs,
                                       reporter);

        assertNotNull(chunker);

        ChainCrf<String> crf = chunker.crf();

        String test = "John likes New York City.";
        Chunking chunking = chunker.chunk(test);
        assertEquals(chunking("John likes New York City.",
                            // 0123456789012345678901234
                              chunk(0,4,"PER"),
                              chunk(11,24,"LOC")),
                     chunking);
    }

    static final TagChunkCodec TAG_CHUNK_CODEC
        = new BioTagChunkCodec(IndoEuropeanTokenizerFactory.INSTANCE,
                               true);

    static Chunking chunking(String s, Chunk... chunks) {
        ChunkingImpl chunking = new ChunkingImpl(s);
        for (Chunk chunk : chunks)
            chunking.add(chunk);
        return chunking;
    }
    static Chunk chunk(int start, int end, String type) {
        return ChunkFactory.createChunk(start,end,type);
    }

    static class TrainCorpus1 extends Corpus<ObjectHandler<Chunking>> {
        static final Corpus<ObjectHandler<Chunking>> INSTANCE
            = new TrainCorpus1();
        static final Chunking[] TRAIN_CHUNKINGS
            = new Chunking[] {
            chunking(""),
            chunking("The"),
            chunking("John ran.",
                     chunk(0,4,"PER")),
            chunking("Mary ran.",
                     chunk(0,4,"PER")),
            chunking("The kid ran."),
            chunking("John likes Mary.",
                   // 0123456789012345
                     chunk(0,4,"PER"),
                     chunk(11,15,"PER")),
            chunking("Tim lives in Washington",
                   // 012345678901234567890123456789
                     chunk(0,3,"PER"),
                     chunk(13,23,"LOC")),
            chunking("Mary Smith is in New York City",
                   // 0123456789012345678901234567890
                     chunk(0,10,"PER"),
                     chunk(17,30,"LOC")),
            chunking("New York City is fun",
                   // 012345678901234567890123456789
                     chunk(0,13,"LOC")),
            chunking("Chicago is not like Washington",
                   // 0123456789012345678901234567890
                     chunk(0,7,"LOC"),
                     chunk(20,30,"LOC"))
        };
        public void visitTrain(ObjectHandler<Chunking> handler) {
            for (Chunking chunking : TRAIN_CHUNKINGS)
                handler.handle(chunking);
        }
        public void visitTest(ObjectHandler<Chunking> handler) {
            /* no op */
        }
    }



}