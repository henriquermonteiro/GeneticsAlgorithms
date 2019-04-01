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
public class IterationCountMethod extends EvaluationMethod{

    public IterationCountMethod(Session session) {
        super(session);
    }

    @Override
    public boolean hasNextIteration() {
        return this.session.getPool().getGeneration() < this.session.getStopParameter();
    }
    
}
