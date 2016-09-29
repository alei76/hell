package ps.hell.ml.nlp.tool.aliasi.test.unit.chunk;

import ps.hell.ml.nlp.tool.aliasi.chunk.ChunkFactory;
import ps.hell.ml.nlp.tool.aliasi.chunk.ChunkingImpl;
import ps.hell.ml.nlp.tool.aliasi.chunk.RegExChunker;
import org.junit.Test;

import java.io.IOException;

import static ps.hell.ml.nlp.tool.aliasi.test.unit.chunk.CharLmHmmChunkerTest.assertChunkingCompile;
import static org.junit.Assert.*;

public class RegExChunkerTest {

    @Test
    public void test1() throws IOException, ClassNotFoundException {
        ChunkingImpl chunking = new ChunkingImpl("abcdef");
        chunking.add(ChunkFactory.createChunk(0,3,"typeA",-1.2));
        chunking.add(ChunkFactory.createChunk(3,5,"typeA",-1.2));
        assertChunkingCompile(new RegExChunker("abc|de","typeA", -1.2),
                              chunking);


        ChunkingImpl chunking2 = new ChunkingImpl("abcdef");
        chunking2.add(ChunkFactory.createChunk(0,3,"typeA",-1.2));
        assertChunkingCompile(new RegExChunker("(abc|ab|a)","typeA", -1.2),
                              chunking2);

        ChunkingImpl chunking3 = new ChunkingImpl("aabaa bab");
        chunking3.add(ChunkFactory.createChunk(0,5,"typeB",-12));
        chunking3.add(ChunkFactory.createChunk(6,8,"typeB",-12));
        chunking3.add(ChunkFactory.createChunk(8,9,"typeB",-12));
        assertChunkingCompile(new RegExChunker("(a*ba*|b*ab*)","typeB",-12),
                              chunking3);
    }

}
