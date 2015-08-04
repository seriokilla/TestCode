import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WordCountByLetterTopology {

    public static void main(String[] args) throws Exception{
	    Config config = new Config();
        config.put("inputFile", "C:\\Temp\\sentances.txt");
        config.setDebug(true);
        config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("LineReaderSpout", new LineReaderSpout());
        builder.setBolt("LineSplitterBolt", new LineSplitterBolt()).shuffleGrouping("LineReaderSpout");
        builder.setBolt("WordCounterBolt", new WordCounterBolt()).shuffleGrouping("LineSplitterBolt");

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("WordCountByLetterTopology", config, builder.createTopology());
        Thread.sleep(10000);
        cluster.shutdown();
    }

    //
    // Spout - reads file line by line
    //
    public static class LineReaderSpout extends BaseRichSpout {
        private FileReader reader;
        private SpoutOutputCollector collector;
        private boolean isReaderCompleted;

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("line"));
        }

        @Override
        public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
            try {
                this.reader = new FileReader(map.get("inputFile").toString());
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Error reading file " + map.get("inputFile").toString());
            }
            collector = spoutOutputCollector;
        }

        @Override
        public void close() {
            try {
                this.reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void nextTuple() {
            if (this.isReaderCompleted)
                return;

            String str;
            BufferedReader bufferedReader = new BufferedReader(this.reader);
            try{
                while((str = bufferedReader.readLine()) != null){
                    this.collector.emit(new Values(str), str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally{
                this.isReaderCompleted = true;
            }
        }
    }

    //
    // Bolt - Splits line into words and performs lowercase
    //
    public static class LineSplitterBolt extends BaseRichBolt{
        private OutputCollector collector;

        @Override
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
            this.collector = outputCollector;
        }

        @Override
        public void execute(Tuple tuple) {

            String line = tuple.getString(0);
            String[] words = line.split(" ");
            for(String word :words){
                String cleanWord = word.toLowerCase().replace("[^a-z]", "");
                this.collector.emit(tuple, new Values(cleanWord));
            }

            this.collector.ack(tuple);
            System.out.println("LineSplitterBolt.execute: " + line);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("word"));
        }
    }

    //
    // Bolt - count each word by first letter
    //
    public static class WordCounterBolt extends BaseRichBolt{
        private HashMap<Character, Integer> countHash = new HashMap<>();

        @Override
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        }

        @Override
        public void execute(Tuple tuple) {
            String word = tuple.getString(0);
            Character firstCharacter = word.charAt(0);
            int count = 1;
            if (countHash.containsKey(firstCharacter)) {
                count = countHash.get(firstCharacter);
                count ++;
            }

            countHash.put(firstCharacter, count);
            System.out.println("WordCounterBolt.execute: " + firstCharacter + ":" + count);
        }

        @Override
        public void cleanup() {
            for(Map.Entry e : countHash.entrySet()){
                System.out.println("WordCounterBolt.cleanup: " + e.getKey() + ":" + e.getValue());
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

        }

    }
}


