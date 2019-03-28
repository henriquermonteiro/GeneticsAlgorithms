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
public abstract class EvaluationMethod {
    protected Session session;

    public EvaluationMethod(Session session) {
        this.session = session;
    }
    
    public abstract boolean hasNextIteration();
}
