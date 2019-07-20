import com.demo.knn.DataLoader;
import com.demo.knn.SampleData;
import com.demo.knn.KhmerNumericLabel;
import com.demo.knn.KnnClassifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
/**
 * @author Engleang
 * Demo kNN application class
 *
 * */
public class Application {
    public static void main(String[] args) throws IOException {
        DataLoader dataLoader = new DataLoader("data/train");
        dataLoader.load();
        final ConcurrentHashMap<String, List<SampleData>> dataSamples = dataLoader.getData(); // load as map in order to group it by folder
        DataLoader testLoader = new DataLoader("data/test");
        testLoader.load();
        final ConcurrentHashMap<String,List<SampleData>> testSamples = testLoader.getData();//load as map in order to group it by folder
        KnnClassifier classifier = new KnnClassifier(3,10);
        List<SampleData> samples = dataSamples.values().parallelStream().collect(ArrayList::new, List::addAll,List::addAll);
        double errorCount = 0;
        long startTestTime = System.currentTimeMillis();
        int totalTest = 0;
        StringBuilder resultText = new StringBuilder();
        try {
            for (Map.Entry<String, List<SampleData>> test : testSamples.entrySet()) {
                System.out.println("Begin to test for  folder [ ./test/" + test.getKey() +" ]  for Khmer character : "+KhmerNumericLabel.valueOf(test.getKey()));
                for (SampleData instance : test.getValue()) {
                    String result =  classifier.doClassify(instance, samples);
                    String matched = result.equals( test.getKey()) ? " matched  \t":" not matched ";
                    resultText.append( "Result found " +matched +": " + KhmerNumericLabel.valueOf( result ) + " for "
                            +  KhmerNumericLabel.valueOf( instance.getIdentifier() ) + " , file ->./test/" + instance.getFileName()
                    ).append("\n");
                    if( !result.equals( test.getKey())) {
                        errorCount++;
                    }
                    totalTest++;
                }
            }
            System.out.println("================================================================");
            System.out.println("Error rate : "+(errorCount/ totalTest));
            double duration = (double)(System.currentTimeMillis()  - startTestTime)/1000;
            System.out.println("Total duration take : "+ duration +" (s)");

            System.out.println("================Summary result =================================");
            System.out.println(resultText.toString());
            System.out.println("=====================***========================================");
        }catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            classifier.close();
        }



    }
}
