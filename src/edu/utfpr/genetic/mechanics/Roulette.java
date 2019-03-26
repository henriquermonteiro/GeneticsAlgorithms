/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.genetic.mechanics;

import edu.utfpr.genetic.data.Chromosome;
import java.util.Random;

/**
 *
 * @author henrique
 */
public class Roulette {
    private final Random rng;
    private final double mutationChance;
    private final double crossOverChance;

    public Roulette(long seed, double mutationChance, double crossOverChance) {
        this.rng = new Random(seed);
        this.mutationChance = (mutationChance > 1.0 ? mutationChance / 100.0 : mutationChance);
        this.crossOverChance = (crossOverChance > 1.0 ? crossOverChance / 100.0 : crossOverChance);
    }
    
    private Chromosome getChromossome(Chromosome[] pool, double totalFitness){
        double choice = rng.nextDouble();
        
        double agregated = 0.0;
        
        for(Chromosome ind : pool){
            agregated += ind.getFitness() / totalFitness;
            
            if(agregated > 1){
                break;
            }
            
            if(agregated > choice){
                return ind;
            }
        }
        
        return null;
    }
    
    public void tryMutation(Chromosome target){
        double choice = rng.nextDouble();
        
        if(mutationChance > choice){
            target.applyMutation(rng.nextInt(target.getGeneCount()));
        }
    }
    
    public Chromosome[] tryCrossOver(Chromosome[] pool, double totalFitness){
        Chromosome[] childs = new Chromosome[2];
        
        Chromosome parent1 = getChromossome(pool, totalFitness);
        Chromosome parent2 = getChromossome(pool, totalFitness);
        
        double choice = rng.nextDouble();
        
        if(!(crossOverChance > choice) || parent1.equals(parent2)){
            childs[0] = parent1;
            childs[1] = parent2;
        }else{
            int pos1 = rng.nextInt(parent1.getGeneCount());
            int pos2 = rng.nextInt(parent1.getGeneCount());

            if(pos1 > pos2){
                pos2 ^= pos1;
                pos1 ^= pos2;
                pos2 ^= pos1;
            }

            childs[0] = parent1.generateChild(parent2, 0, 0);
            childs[1] = parent2.generateChild(parent1, 0, 0);
        }
        
        return childs;
    }
}
