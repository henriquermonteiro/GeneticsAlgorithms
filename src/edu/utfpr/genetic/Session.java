/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.genetic;

import edu.utfpr.genetic.data.IterationDigest;
import edu.utfpr.genetic.data.Pool;
import edu.utfpr.genetic.data.imp.BackPackBinaryChromossome;
import edu.utfpr.genetic.mechanics.Roulette;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author henrique
 */
public class Session<T> {
    private final Roulette roulette;
    private final Pool pool;
    private Long fitnessCalc = 0l;
    private ArrayList<IterationDigest<T>> digest;
    private EvaluationMethod evaluationM;
    private final long stopConditionParameter;
    private final char infactibilityTreatment;
    private boolean continueFlag = true;
    private ArrayList<SessionListener> sessionListeners;

    public Session(long seed, double mutationChance, double crossOverChance, int poolSize, int childGeneration, Class ChromossomeClass, char evaluationMethod, long stopConditionParameter, char infactibilityTreatment) throws Exception{
        this.infactibilityTreatment = infactibilityTreatment;
        this.roulette = new Roulette(seed, mutationChance, crossOverChance);
        this.pool = new Pool(this, poolSize, childGeneration, ChromossomeClass);
        switch(evaluationMethod){
            case 'I':
                this.evaluationM = new IterationCountMethod(this);
                break;
            case 'F':
                this.evaluationM = new FitnessCalculationMethod(this);
                break;
        }
        this.stopConditionParameter = stopConditionParameter;
        digest = new ArrayList<>();
        sessionListeners = new ArrayList<>();
    }
    
    public void addSessionListner(SessionListener listener){
        sessionListeners.add(listener);
    }
    
    public void removeSessionListner(SessionListener listener){
        sessionListeners.remove(listener);
    }
    
    public void stopExecution(){
        continueFlag = false;
    }
    
    public Pool getPool() {
        return pool;
    }

    public Roulette getRoulette() {
        return roulette;
    }
    
    public synchronized void countFitnessCalculation(){
//    public void countFitnessCalculation(){
        fitnessCalc++;
    }
    
    public void execute(){
        do{
            pool.processGeneration();
            IterationDigest dig = new IterationDigest<>(pool);
            digest.add(dig);
            
            for(SessionListener sL: sessionListeners){
                sL.receiveLogInfo(dig.getLogLines());
            }
        }while(evaluationM.hasNextIteration() && continueFlag);
        
        for(SessionListener sL: sessionListeners){
            sL.receiveStopStatus('C');
        }
    }

    public ArrayList<IterationDigest<T>> getDigest() {
        return digest;
    }

    public long getStopParameter() {
        return stopConditionParameter;
    }

    public Long getFitnessCalc() {
        return fitnessCalc;
    }

    public char getInfactibilityTreatment() {
        return infactibilityTreatment;
    }
    
    public static void main(String[] args) {
        try {
            Session s;
            ArrayList<IterationDigest> list;
            IterationDigest dig;
            
            Long[] seeds = new Long[]{1636845245286l, 7640635291721l, 199592223697l, 4004508840715l, 6552196957585l, 7159986868434l, 4252466632138l, 
                                      21770754606l, 220200465110l, 4379318796776l, 3591023517058l, 288778709313l, 8913324145372l, 7662551042433l, 
                                      7736415661691l, 5080121331140l, 5198388406094l, 4707831208023l, 570129701954l, 6870065350072l, 777365268788l, 
                                      791141022092l, 733204841842l, 7367626153537l, 9341995002337l, 57296576746l,114029963969l,892177945110l,
                                      152553321824l, 999420795113l, 131794017317l, 277337992602l, 611295535401l, 913156387855l,
                                      908734491987l,206717071964l,313055538490l,586486978998l,288994909780l,2256006255l,
                                      496558740011l,849809808612l,212637762926l,889558179184l,944879813282l,595887030522l,
                                      146960835203l,239346852163l,392857414602l,909442767711l,373818624952l,990071139404l,
                                      175451147277l,233299322909l,358957190607l,386274538953l,788508104746l,545150309011l,
                                      888402145674l,350257074141l,522825289393l,515213158013l,769674187169l,875452793797l,
                                      739385668881l,5296677294l,266633507657l,538576540135l,105389758207l,920184330394l,
                                      439011844146l,807481868326l,791700121556l,306478778696l,594105791263l,809380615328l,
                                      549877167839l,993985121493l,761626337240l,156357510076l,896571104795l,662394519167l,
                                      3518127305l,634468782066l,302782689590l,353575349213l,74620744475l,754270080347l,
                                      374530815374l,333872514596l,402569656456l,955181326443l,150033507117l,77339351610l,
                                      560660314407l,161499731764l,695503052518l,756812127456l,121061631672l,593439565423l};
            int run = 0;
            double avrg = 0.0;
            ArrayList<IterationDigest> bestDig = null;
            double bestDigFit = 0.0;
            double bestDigAvr = 0.0;
            
            for(Long seed : seeds){
                System.out.println("Run " + (run < 10 ? "0" + run : run) + ": Seed(" + seed + ")");
                s = new Session<Byte[]>(seed, 0.4, 0.8, 200, 200, BackPackBinaryChromossome.class, 'I', 500, 'p');
                s.execute();
                list = s.getDigest();
                dig = list.get(list.size() - 1);
                
                if(dig.getBestFitness() >= bestDigFit){
                    double avr = 0.0;
                    for(IterationDigest it : list){
                        avr += it.getBestFitness();
                    }
                    avr /= list.size();
                    
                    if(avr > bestDigAvr){
                        bestDigFit = dig.getBestFitness();
                        bestDigAvr = avr;
                        bestDig = list;
                    }
                }
                
                System.out.println("<" + (run < 10 ? "0" + run : run) + "," + dig.getChromossomeString() +", " + dig.getChromossomeString().replaceAll("[^1]", "").length() + ", " + dig.getBestFitness() + "," + dig.getBestWeight() + ">");
                avrg += dig.getBestFitness();
                
                run++;
            }
            
            avrg /= seeds.length;
            System.out.println("Config quality: " + avrg);
            
            if(bestDig != null)
                bestDig.forEach((t) -> {
                    System.out.println(t.getGenerationID() + "\t" + t.getBestFitness());
                });
        } catch (Exception ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
