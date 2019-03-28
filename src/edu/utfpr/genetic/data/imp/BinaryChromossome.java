/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.genetic.data.imp;

import edu.utfpr.genetic.Session;
import edu.utfpr.genetic.data.Chromosome;
import edu.utfpr.genetic.mechanics.Roulette;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author henrique
 */
public abstract class BinaryChromossome extends Chromosome<Byte[]>{

    public BinaryChromossome(Session session) {
        super(session);
    }

    public BinaryChromossome(Byte[] chromosome, Session session) {
        super(chromosome, session);
    }

    @Override
    protected Byte[] generateRandomGenes() {
        int shift = getGeneCount() % 8;
        byte mask = (byte) (0xff << shift);
        mask = (byte)(mask >>> shift);
        
        Roulette roulette = this.session.getRoulette();
        
        Byte[] chr = new Byte[(int)Math.ceil(getGeneCount() / 8.0)];
        chr[0] = (byte) (roulette.getNextByte() & mask);
        
        for(int k = 1; k < chr.length; k++)
            chr[k] = roulette.getNextByte();
        
        return chr;
    }

    @Override
    public void applyMutation(int gene) {
        if(gene > getGeneCount() - 1 || gene < 0){
            return;
        }
        
        int pos = (int)(gene / 8);
        int shift = gene % 8;
        
        byte mask = 0x01;
        
        mask = (byte) (mask << gene % shift);
        
        this.chromosome[pos] = (byte) (this.chromosome[pos] ^ mask);
    }

    @Override
    public Chromosome<Byte[]> generateChild(Chromosome<Byte[]> otherParent, int start, int end) {
        if(start < end || end > getGeneCount() + 1 || start < 0){
            return null;
        }
        
        if(start == end)
            end = getGeneCount() - 1;
        
        Byte[] child = new Byte[this.chromosome.length];
        
        Byte[] parent2 = otherParent.getChromosome();
        
        for(int k = 0; k < child.length; k++){
            int byteBegin = k * 8;
            int byteEnd = byteBegin + 7;
            
            if(start < byteEnd || end > byteBegin){
                child[k] = this.chromosome[k];
            }else if(start < byteBegin){
                int shift = (byteBegin) - start;
                byte p1 = (byte) ((this.chromosome[k] >>> shift) << shift);
                shift = 8 - shift;
                byte p2 = (byte) ((parent2[k] << shift) >>> shift);
                
                child[k] = (byte) (p1 | p2);
            }else if(end > byteEnd){
                int shift = end - (byteEnd);
                byte p2 = (byte) ((this.chromosome[k] << shift) >>> shift);
                shift = 8 - shift;
                byte p1 = (byte) ((parent2[k] >>> shift) << shift);
                
                child[k] = (byte) (p1 | p2);
            }else{
                child[k] = parent2[k];
            }
        }
        
        try {
            return this.getClass().getConstructor(Session.class, Byte[].class).newInstance(session, child);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(BinaryChromossome.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public int compareTo(Chromosome<Byte[]> t) {
        double delta = this.getFitness() - t.getFitness();
        
        return (int)(delta < 0 ? Math.min(delta, -1) : Math.max(delta, 1));
    }
    
}