/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.genetic;

import edu.utfpr.genetic.data.IterationDigest;
import edu.utfpr.genetic.data.Pool;
import edu.utfpr.genetic.mechanics.Roulette;
import java.util.ArrayList;

/**
 *
 * @author henrique
 */
public class Session<T> {
    private final Roulette roulette;
    private final Pool pool;
    private Long fitnessCalc = 0l;
    private ArrayList<IterationDigest<T>> digest;
    private EvaluationMethod evaluationM;
    private final long stopConditionParameter;

    public Session(long seed, double mutationChance, double crossOverChance, int poolSize, int childGeneration, Class ChromossomeClass, char evaluationMethod, long stopConditionParameter) throws Exception{
        this.roulette = new Roulette(seed, mutationChance, crossOverChance);
        this.pool = new Pool(this, poolSize, childGeneration, ChromossomeClass);
        switch(evaluationMethod){
            case 'I':
                this.evaluationM = new IterationCountMethod(this);
                break;
            case 'F':
                this.evaluationM = new FitnessCalculationMethod(this);
                break;
        }
        this.stopConditionParameter = stopConditionParameter;
    }
    
    public Pool getPool() {
        return pool;
    }

    public Roulette getRoulette() {
        return roulette;
    }
    
    public synchronized void countFitnessCalculation(){
        fitnessCalc++;
    }
    
    public void execute(){
        do{
            pool.processGeneration();
            digest.add(new IterationDigest<>(pool));
        }while(evaluationM.hasNextIteration());
    }

    public ArrayList<IterationDigest<T>> getDigest() {
        return digest;
    }

    public long getStopParameter() {
        return stopConditionParameter;
    }

    public Long getFitnessCalc() {
        return fitnessCalc;
    }
}
