/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.genetic.data;

import edu.utfpr.genetic.mechanics.Roulette;
import java.util.ArrayList;

/**
 *
 * @author henrique
 */
public class Session<T> {
    private Roulette roulette;
    private Pool pool;
    private Long fitnessCalc = 0l;
    private ArrayList<IterationDigest<T>> digest;

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
        }while(true);
    }

    public ArrayList<IterationDigest<T>> getDigest() {
        return digest;
    }
}
