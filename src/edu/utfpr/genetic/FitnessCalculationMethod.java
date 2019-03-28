/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.genetic;

/**
 *
 * @author henrique
 */
public class FitnessCalculationMethod extends EvaluationMethod{

    public FitnessCalculationMethod(Session session) {
        super(session);
    }

    @Override
    public boolean hasNextIteration() {
        return this.session.getStopParameter() <= this.session.getFitnessCalc();
    }
    
}
