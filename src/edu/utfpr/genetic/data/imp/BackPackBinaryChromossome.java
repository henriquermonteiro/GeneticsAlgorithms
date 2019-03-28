/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.genetic.data.imp;

import edu.utfpr.genetic.Session;

/**
 *
 * @author henrique
 */
public class BackPackBinaryChromossome extends BinaryChromossome{
    private final double[] values = {1, 3,  1, 8, 9, 3, 2, 8, 5, 1, 1, 6, 3, 2, 5, 2, 3, 8, 9, 3, 2, 4, 5, 4, 3, 1, 3, 2, 14, 32, 20, 19, 15, 37, 18, 13, 19, 10, 15, 40, 17, 39};
    private final double[] weight = {3, 8, 12, 2, 8, 4, 4, 5, 1, 1, 8, 6, 4, 3, 3, 5, 7, 3, 5, 7, 4, 3, 7, 2, 3, 5, 4, 3,  7, 19, 20, 21, 11, 24, 13, 17, 18,  6, 15, 25, 12, 19};
    private final double capacity = 120;

    public BackPackBinaryChromossome(Session session) {
        super(session);
    }

    public BackPackBinaryChromossome(Byte[] chromosome, Session session) {
        super(chromosome, session);
    }

    @Override
    public int getGeneCount() {
        return 42;
    }

    @Override
    protected void calculateFitness() {
        this.fitness = 0.0;
        
        byte buffer = 0;
        int pos = this.chromosome.length;
        for(int k = 0; k < getGeneCount(); k++){
            if(k % 8 == 0){
                pos--;
                buffer = this.chromosome[pos];
            }
            
            boolean lastBit = (buffer & 0x01) == 0x01;
            if(lastBit)
                this.fitness += values[(getGeneCount() - 1) - k];
            
            buffer = (byte) (buffer >>> 1);
        }
    }

    @Override
    public boolean evaluateValidity() {
        double wgt = 0.0;
        
        byte buffer = 0;
        int pos = this.chromosome.length;
        for(int k = 0; k < getGeneCount(); k++){
            if(k % 8 == 0){
                pos--;
                buffer = this.chromosome[pos];
            }
            
            boolean lastBit = (buffer & 0x01) == 0x01;
            if(lastBit)
                wgt += weight[(getGeneCount() - 1) - k];
            
            if(wgt > capacity)
                return false;
            
            buffer = (byte) (buffer >>> 1);
        }
        
        return true;
    }

    @Override
    public String getIdentifier() {
        return "Binary Backpack Problem";
    }
    
}
