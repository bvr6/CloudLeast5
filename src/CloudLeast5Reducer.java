import java.io.*; 
import org.apache.hadoop.io.Text; 
import org.apache.hadoop.io.LongWritable; 
import org.apache.hadoop.mapreduce.Reducer; 
  
public class CloudLeast5Reducer extends Reducer<Text,LongWritable,Text,LongWritable> {
    private LongWritable result = new LongWritable();
    static int count;
    
    @Override
    public void setup(Context context) throws IOException, 
                                     InterruptedException 
    { 
        count = 0; 
    }
    
    public void reduce(Text key, Iterable<LongWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (LongWritable val : values) {
        sum -= val.get();
      }
      result.set(sum);
      if (count < 5) 
        { 
            context.write(key, result); 
            count++; 
        } 
    }
}