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
import backtype.storm.tuple.Tuple;

import backtype.storm.tuple.Values;
import backtype.storm.tuple.Fields;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionTopo {

    //
    // Spout Implementation
    //
    public static class QuestionSpout implements IRichSpout{
        private SpoutOutputCollector collector;
        private FileReader fileReader;
        private boolean completed = false;

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("line"));
        }

        @Override
        public Map<String, Object> getComponentConfiguration() {
            return null;
        }

        @Override
        public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
            try {
                this.fileReader = new FileReader(map.get("inputFile").toString());
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Error reading file " + map.get("inputFile"));
            }
            this.collector = spoutOutputCollector;
        }

        @Override
        public void close() {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void activate() {

        }

        @Override
        public void deactivate() {

        }

        @Override
        public void nextTuple() {
            if (completed) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // do nothing
                }
            }

            String str;
            BufferedReader reader = new BufferedReader(fileReader);
            try {
                while ((str = reader.readLine()) != null) {
                    this.collector.emit(new Values(str), str);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error reading typle", e);
            } finally {
                completed = true;
            }
        }

        @Override
        public void ack(Object o) {

        }

        @Override
        public void fail(Object o) {

        }
    }

    //
    // Bolt Implementation
    //
    public static class QuestionBolt implements IRichBolt {
        private OutputCollector _collector;
        private List<String> _list = new ArrayList<>();

        @Override
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
            _collector = outputCollector;
        }

        @Override
        public void execute(Tuple tuple) {
            String question = tuple.getString(0) + "???";
            _collector.emit(tuple, new Values(question));
            _list.add(question);
            _collector.ack(tuple);

            System.out.println("QuestionBolt.execute: " + question);
        }

        @Override
        public void cleanup() {
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
            outputFieldsDeclarer.declare(new Fields("word"));
            outputFieldsDeclarer.declareStream();
        }

        @Override
        public Map<String, Object> getComponentConfiguration() {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        Config config = new Config();
        config.put("inputFile", args[0]);
        config.setDebug(true);
        config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);


        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("QuestionSpout", new QuestionSpout());
        builder.setBolt("QuestionBolt", new QuestionBolt()).shuffleGrouping("QuestionSpout");

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("QuestionTopology", config, builder.createTopology());
        Thread.sleep(10000);
        cluster.shutdown();
    }
}
