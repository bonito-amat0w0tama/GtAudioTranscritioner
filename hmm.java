import java.util.ArrayList;
import java.util.List;

import be.ac.ulg.montefiore.run.jahmm.*;
import be.ac.ulg.montefiore.run.jahmm.toolbox.MarkovGenerator;


public class hmm {

    public enum Action{
        walk, souji;
        
        public ObservationDiscrete<Action> observation() {
        	return new ObservationDiscrete<Action>(this);
        }
    }
    
    static Hmm<ObservationDiscrete<Action>> buildHmm() {
    	Hmm<ObservationDiscrete<Action>> hmm =
    			new Hmm<ObservationDiscrete<Action>>
                    (2, new OpdfDiscreteFactory<Action>(Action.class));
    	
    	hmm.setPi(0, 0.95);
    	hmm.setPi(1, 0.05);
    	
    	hmm.setOpdf(0, new OpdfDiscrete<Action>(Action.class, 
    			new double[] {0.2, 0.8}));
    	hmm.setOpdf(1, new OpdfDiscrete<Action>(Action.class, 
    			new double[] {0.20, 0.80}));
    	
    	hmm.setAij(0, 1, 0.05);
    	hmm.setAij(0, 0, 0.95);
    	hmm.setAij(1, 0, 0.10);
    	hmm.setAij(1, 1, 0.90);
    	
    	return hmm;
    }
    
    static <O extends Observation> List<List<O>> generateSequences(Hmm<O> hmm) {
    	MarkovGenerator<O> mg = new MarkovGenerator<O>(hmm);
    	
    	List<List<O>> sequences = new ArrayList<List<O>>();
    	
    	int numMg = 10;
    	for (int i = 0; i < numMg; i++) {
    		sequences.add(mg.observationSequence(10));
    	}
    	
    	return sequences;
    }
    
    public static void main(String[] args) {
    	Hmm<ObservationDiscrete<Action>> hmm = buildHmm();
    	List<List<ObservationDiscrete<Action>>> sequences;
    	sequences = generateSequences(hmm);
    	
    	for (List<ObservationDiscrete<Action>> s: sequences) {
            System.out.println(s);
    	}
    }
}
