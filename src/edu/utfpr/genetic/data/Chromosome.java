/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.genetic.data;

/**
 *
 * @author henrique
 */
public abstract class Chromosome<T> implements Comparable<T>{
    private T chromosome;
    private Double fitness;
    private Session session;
    
    public Chromosome(Session session){
        this.session = session;
        this.chromosome = generateRandomGenes();
        updateFitness();
    }
    
    public Chromosome(T chromosome, Session session){
        this.session = session;
        this.chromosome = chromosome;
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
    
    protected abstract T generateRandomGenes();
    
    public abstract void applyMutation(int gene);
    
    public abstract Chromosome generateChild(Chromosome otherParent, int start, int end);
}
