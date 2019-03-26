/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.genetic.data;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author henrique
 */
public class IterationDigest<T> {
    private final T chromossome;
    private final Double bestFitness;
    private final Double averageFitness;
    private final Double standardDeviation;
    private final long generationID;
    private final Calendar logTime;

    public IterationDigest(Pool pool) {
        logTime = GregorianCalendar.getInstance();
        generationID = pool.getGeneration();
        
        Chromosome best = pool.getPool()[0];
        
        double avgFitness = 0.0;
        for(Chromosome ind : pool.getPool()){
            if(best.compareTo(ind) < 0){
                best = ind;
            }
            
            avgFitness += ind.getFitness();
        }
        
        chromossome = (T) best.getChromosome();
        bestFitness = best.getFitness();
        averageFitness = avgFitness / pool.getPool().length;
        
        double stdDeviation = 0.0;
        for(Chromosome ind : pool.getPool()){
            stdDeviation += Math.pow(ind.getFitness() - averageFitness, 2);
        }
        
        standardDeviation = stdDeviation / pool.getPool().length;
    }

    public T getChromossome() {
        return chromossome;
    }

    public Double getBestFitness() {
        return bestFitness;
    }

    public Double getAverageFitness() {
        return averageFitness;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public long getGenerationID() {
        return generationID;
    }

    public Calendar getLogTime() {
        return logTime;
    }
}
