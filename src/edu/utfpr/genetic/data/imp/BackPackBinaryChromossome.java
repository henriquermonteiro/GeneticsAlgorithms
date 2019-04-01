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
    private static final double[] values = {1, 3,  1, 8, 9, 3, 2, 8, 5, 1, 1, 6, 3, 2, 5, 2, 3, 8, 9, 3, 2, 4, 5, 4, 3, 1, 3, 2, 14, 32, 20, 19, 15, 37, 18, 13, 19, 10, 15, 40, 17, 39};
    private static final double[] weights = {3, 8, 12, 2, 8, 4, 4, 5, 1, 1, 8, 6, 4, 3, 3, 5, 7, 3, 5, 7, 4, 3, 7, 2, 3, 5, 4, 3,  7, 19, 20, 21, 11, 24, 13, 17, 18,  6, 15, 25, 12, 19};
    private static final double capacity = 120;
    
    private double weight = 0.0;

    public BackPackBinaryChromossome(Session session) {
        super(session);
    }

    public BackPackBinaryChromossome(Byte[] chromosome, Session session, boolean calculateFitness) {
        super(chromosome, session, calculateFitness);
    }

    @Override
    public int getGeneCount() {
        return 42;
    }
    
    private void axCalculateFitness(){
        this.fitness = 0.0;
        
        byte buffer = 0;
        int pos = this.chromosome.length;
        for(int k = 0; k < getGeneCount(); k++){
            if(k % 8 == 0){
                pos--;
                buffer = this.chromosome[pos];
            }
            
            boolean lastBit = (buffer & 0x01) == 0x01;
            if(lastBit){
                this.fitness += values[(getGeneCount() - 1) - k];
                this.weight += weights[(getGeneCount() - 1) - k];
            }
            
            buffer = (byte) (buffer >>> 1);
        }
    }

    @Override
    protected void calculateFitness() {
        axCalculateFitness();
        
        if(weight > 120){
            switch (session.getInfactibilityTreatment()) {
                case 'e': // eliminate
                    if(this.session.getPool() != null){
                        fitness = 0.0;
                        break;
                    }
                case 'r': // repair
                    repair();
                    axCalculateFitness();
                    break;
                case 'p': // punish
                    double punishment = (Math.max(weight - 120, 0));
                    punishment /= 120;
                    punishment = Math.sqrt(punishment);
                    fitness *= 1-punishment;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void repair() {
        while(!evaluateValidity()){
            String encoding = this.toString().replaceAll("\\D", "");
            String itens = encoding.replaceAll("[^1]", "");
            
            int length = itens.length();
            
            int remove = session.getRoulette().getNextInt(itens.length());
            
            int count1 = 0;
            
            for(int k = encoding.length() - 1; k > 0; k--){
                if(encoding.charAt(k) == '1'){
                    count1++;
                    
                    if(count1 == remove){
                        this.applyMutation(k);
                        break;
                    }
                }
            }
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
                wgt += weights[(getGeneCount() - 1) - k];
            
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
