package com.demo.knn;


import java.util.*;
import java.util.concurrent.*;

public class KnnClassifier {
    private int k;
    ThreadPoolExecutor threadPoolExecutor;

    public KnnClassifier(int k,int numberOfThread) {
        this.k = k;
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberOfThread);
    }

    public PriorityBlockingQueue<SampleDistanceValue> reversePriorityQueue( int init)
    {
         return new PriorityBlockingQueue<>( init,(o1,o2)->{
            if( o1.getValue() < o2.getValue())
            {
                return -1;
            }
            else if (o1.getValue() == o2.getValue()) {
                return 0;
            }
            return 1;

        });
    }

    public String doClassify(SampleData target, List<SampleData> sampleData) throws ExecutionException, InterruptedException {

        List<CompletableFuture<SampleDistanceValue>> distanceValues = new ArrayList<>();
        for (SampleData item : sampleData) {
            CompletableFuture<SampleDistanceValue>completableFuture = new CompletableFuture<>();
            threadPoolExecutor.submit(()->{

                 final double distance = DistanceCalculator.getDistance( target.getData() ,  item.getData() );
                //System.out.println("current thread name : "+Thread.currentThread().getName() +" , "+target.getFileName() +" vs "+sampleData.getFileName() + " value : "+distance);
                 completableFuture.complete(new SampleDistanceValue(  item.getIdentifier() , distance ));
            });
            distanceValues.add( completableFuture );
        }
        CompletableFuture<Void>combine = CompletableFuture.allOf(  distanceValues.toArray(new CompletableFuture[0]) );
        combine.get();//wait for all tasks to complete
        final PriorityBlockingQueue<SampleDistanceValue> reverseDistancePriorityQueue = reversePriorityQueue(10);
        distanceValues.parallelStream().forEach(it->{
            try {
                reverseDistancePriorityQueue.add(it.get() );
            } catch (InterruptedException  | ExecutionException e) {
                e.printStackTrace();
            }
        });
        Map<String,Integer> classification = new HashMap<>();
        for (int i = 0; i < k; i++)
        {
            Optional<SampleDistanceValue>  value =   Optional.of(  reverseDistancePriorityQueue.poll() );
            value.ifPresent(it-> classification.merge(it.getIdentifier(),1, (v1,v2)->v1+v2) );
        }
        Map.Entry<String,Integer> entry = Collections.max(classification.entrySet(), Map.Entry.comparingByValue());
        return entry.getKey();

    }

    public void close()
    {
        threadPoolExecutor.shutdown();
    }

}
