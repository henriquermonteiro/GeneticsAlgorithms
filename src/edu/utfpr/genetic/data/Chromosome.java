/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.genetic.data;

import edu.utfpr.genetic.Session;

/**
 *
 * @author henrique
 */
public abstract class Chromosome<T> implements Comparable<Chromosome<T>>{
    protected T chromosome;
    protected Double fitness;
    protected final Session session;
    
    public Chromosome(Session session){
        this(null, session, true);
    }
    
    public Chromosome(T chromosome, Session session, boolean calculateFitness){
        this.session = session;
        this.chromosome = (chromosome == null ? generateRandomGenes() : chromosome);
        if(calculateFitness)
            updateFitness();
    }
    
    public T getChromosome(){
        return this.chromosome;
    }
    
    public abstract int getGeneCount();
    
    public Double getFitness(){
        return fitness;
    }
    
    public final void updateFitness(){
        calculateFitness();
        this.session.countFitnessCalculation();
    }
    
    protected abstract void calculateFitness();
    
    public abstract boolean evaluateValidity();
    
    protected abstract T generateRandomGenes();
    
    public abstract void applyMutation(int gene);
    
    public abstract Chromosome<T> generateChild(Chromosome<T> otherParent, int start, int end);
    
    public abstract String getIdentifier();

    public abstract void repair();
}
