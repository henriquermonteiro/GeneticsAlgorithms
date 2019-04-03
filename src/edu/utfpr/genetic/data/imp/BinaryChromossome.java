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

    public BinaryChromossome(Byte[] chromosome, Session session, boolean calculateFitness) {
        super(chromosome, session, calculateFitness);
    }

    @Override
    protected Byte[] generateRandomGenes() {
        int shift = (getGeneCount() - 1) % 8;
        byte mask = (byte) (0x01);
        
        for(;shift > 0; shift --){
            mask = (byte)((mask << 1) | 0x01);
        }
        
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
        
        int pos = (int)((gene + (8 - (getGeneCount() % 8))) / 8);
        int shift = (gene + (8 - (getGeneCount() % 8))) % 8;
        
        byte mask = 0x01;
        
        mask = (byte) (mask << (7 - shift));
        
        this.chromosome[pos] = (byte) (this.chromosome[pos].byteValue() ^ mask);
    }

    @Override
    public Chromosome<Byte[]> generateChild(Chromosome<Byte[]> otherParent, int start, int end) {
        if(start > end || end > getGeneCount() || start < 0){
            return null;
        }
        
        if(start == end)
            end = getGeneCount() - 1;
        
        Byte[] child = new Byte[this.chromosome.length];
        
        Byte[] parent2 = otherParent.getChromosome();
        
        int offset = (8 - (getGeneCount() % 8) % 8);
        
        for(int k = 0; k < child.length; k++){
            int byteBegin = (k * 8) - offset;
            int byteEnd = byteBegin + 7;
            
            if(start < byteEnd || end < byteBegin){
                child[k] = this.chromosome[k].byteValue();
            }else if(start > byteBegin){
                int shift = start - (byteBegin);
                byte p2 = (byte) ((parent2[k] << shift) >>> shift);
                shift = 8 - shift;
                byte p1 = (byte) ((this.chromosome[k].byteValue() >>> shift) << shift);
                
                child[k] = (byte) (p1 | p2);
            }else if(end < byteEnd){
                int shift = (byteEnd) - end;
                byte p1 = (byte) ((parent2[k] >>> shift) << shift);
                shift = 8 - shift;
                byte p2 = (byte) ((this.chromosome[k].byteValue() << shift) >>> shift);
                
                child[k] = (byte) (p1 | p2);
            }else{
                child[k] = parent2[k].byteValue();
            }
        }
        
        try {
            return this.getClass().getConstructor(Byte[].class, Session.class, boolean.class).newInstance(child, session, false);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(BinaryChromossome.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public int compareTo(Chromosome<Byte[]> t) {
        double delta = this.getFitness() - t.getFitness();
        
        return (int)(delta < 0 ? -1 : delta > 0 ? 1 : 0);
    }

    @Override
    public String toString() {
        int i = (getGeneCount() - 1) % 8;
        String ret = "[";
        for(int j = 0; j < this.chromosome.length ; j++){
            for(; i >= 0; i--){
                ret = ret.concat(((this.chromosome[j].byteValue() >> i) & 0x01) == 0x01 ? "1" : "0");
            }
            
            i = 7;
        }
        
        return ret.concat("]").replace(", ]", "]");
    }
    
    
    
}
