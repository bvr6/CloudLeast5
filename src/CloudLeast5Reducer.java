import java.io.*; 
import org.apache.hadoop.io.Text; 
import org.apache.hadoop.io.IntWritable; 
import org.apache.hadoop.mapreduce.Reducer;
import java.util.Map;
import java.util.TreeMap; 
  
public class CloudLeast5Reducer extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();
    static int count;
    private TreeMap<Integer, String> tmap;
    @Override
    public void setup(Context context) throws IOException, 
                                     InterruptedException 
    { 
        tmap = new TreeMap<Integer, String>(); 
    }
    
    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      tmap.put(-sum, key.toString());
      if (tmap.size() > 5) { 
    	  tmap.remove(tmap.firstKey()); 
      }
    }
    
    @Override
    public void cleanup(Context context) throws IOException, 
                                       InterruptedException 
    { 
  
        for (Map.Entry<Integer, String> entry : tmap.entrySet())  
        { 
  
            int count = entry.getKey(); 
            String name = entry.getValue(); 
            context.write(new Text(name), new IntWritable(count)); 
        } 
    }
}