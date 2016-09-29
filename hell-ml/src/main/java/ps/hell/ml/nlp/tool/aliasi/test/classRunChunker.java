package ps.hell.ml.nlp.tool.aliasi.test;

import ps.hell.ml.nlp.tool.aliasi.chunk.Chunker;
import ps.hell.ml.nlp.tool.aliasi.chunk.Chunking;
import ps.hell.ml.nlp.tool.aliasi.util.AbstractExternalizable;

import java.io.File;

public class classRunChunker {
//以上是一些声
	public static void main(String[] args)throws Exception {
	File modelFile = new File(args[0]);
	//读取识别模块，即识别实体的方法，会在后面讲怎么学。
	System.out.println("Reading chunkerfrom file=" + modelFile);
	Chunker chunker=(Chunker) AbstractExternalizable.readObject(modelFile);

 

       for (int i = 1; i < args.length; ++i) {

           Chunking chunking = chunker.chunk(args[i]);

           System.out.println("Chunking="+ chunking);

   }

   //输入你要进行识别的句子，在这里当然可以以读取文件的形式来实现

	    }

	

}

