package ps.hell.util.csv;
import java.io.File;  
import java.io.FileWriter;  
import java.io.Writer;  
  
import au.com.bytecode.opencsv.CSVWriter;  
  
public class WriteCSV {  
    public static void main(String[] args) throws Exception {  
        File file = new File("e:\\write.csv");  
        Writer writer = new FileWriter(file);  
        CSVWriter csvWriter = new CSVWriter(writer, ',');  
        String[] strs = {"abc" , "abc" , "abc"};  
        csvWriter.writeNext(strs);  
        csvWriter.close();  
    }  
} 