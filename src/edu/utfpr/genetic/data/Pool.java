/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.genetic.data;

import edu.utfpr.genetic.Session;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 *
 * @author henrique
 */
public class Pool {
    private Chromosome[] pool;
    private Chromosome[] children;
    private long generation;
    
    private final Session session;
    
    public Pool(Session session, int poolSize, int childGeneration, Class ChromossomeClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        this.session = session;
        
        if(childGeneration % 2 == 1){
            childGeneration++;
        }
        
        pool = new Chromosome[poolSize];
        children = new Chromosome[childGeneration];
        generation = 0;
        
        for(int k = 0; k < pool.length; k++){
            pool[k] = (Chromosome) ChromossomeClass.getConstructor(Session.class).newInstance(session);
        }
    }
    
    public void processGeneration(){
        generation++;
        
        double poolTotalFitness = 0.0;
        
        for(Chromosome individual : pool){
            poolTotalFitness += individual.getFitness();
        }
        
        for(int k = 0; k < children.length; k += 2){
            Chromosome[] childs = session.getRoulette().tryCrossOver(pool, poolTotalFitness);
            
            children[k] = childs[0];
            children[k+1] = childs[1];
            
            session.getRoulette().tryMutation(children[k]);
            session.getRoulette().tryMutation(children[k+1]);
        }
        
        Arrays.parallelSort(children);
        Arrays.parallelSort(pool);
        
        Chromosome[] newPool = new Chromosome[pool.length];
        
        int i = pool.length - 1, j = children.length - 1;
        
        for(int k = 0; k < newPool.length; k++){
            newPool[k] = (pool[i].compareTo(children[j]) >= 0 ? pool[i--] : children[j--]);
        }
        
        pool = newPool;
    }

    public Chromosome[] getPool() {
        return pool;
    }

    public long getGeneration() {
        return generation;
    }
}
