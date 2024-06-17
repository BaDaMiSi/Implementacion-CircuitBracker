import javax.management.RuntimeErrorException;

//Patron Circuit Breacker

public enum CircuitBreackerState{

CLOSED,
OPEN,
HALF_OPEN

}

//Paso2: Crear la clase CircuitBreacker Class

CircuitBreacker.java

public class CircuitBreacker{

    private CircuitBreackerState state;
    private int failureThreshold;
    private int consecutiveFailures;
    private int resetThreshold;
    private long lastStateChangedTimestamp;

    public CircuitBreacker(int failureThreshold, int resetThreshold){

           this.state=CircuitBreackerState.CLOSED;
           this.failureThreshold=failureThreshold;
           this.failureThreshold=resetThreshold;
           this.consecutiveFailures=0;
           this.lastStateChangedTimestamp=System.currentTimeMillis();

    }

    public boolean allowRequest(){

            if(state == CircuitBreackerState.OPEN && isTimeToReset()){
            state= CircuitBreackerState.HALF_OPEN;
            consecutiveFailures=0;


            }

        return state == CircuitBreackerState.CLOSED || state == CircuitBreackerState.HALF_OPEN;

            }


    public void requestFailed(){

            consecutiveFailures++;
            if(consecutiveFailures>= failureThreshold){
                state= CircuitBreackerState.OPEN;
                lastStateChangedTimestamp=System.currentTimeMillis();


            }
            }

    public void requestSucceeded(){

            if( state == CircuitBreackerState.HALF_OPEN || state == CircuitBreackerState.OPEN){

                state=CircuitBreackerState.CLOSED;
                consecutiveFailures=0;
            }
            }

            private boolean isTimeToReset(){

                return(System.currentTimeMillis() - lastStateChangedTimestamp) >= resetThreshold;
            }
}

//Paso3: Usando el codigo de CircuitBreacker

ExternalService.java

    public class ExternalService{

        private CircuitBreacker circuitBreacker;
        public ExternalService(CircuitBreacker circuitBreacker){
            this.circuitBreacker=circuitBreacker;
            
        }

    public String performRequest(){
        if(circuitBreacker.allowRequest()){
            try{
                // simulamos a request to the external service

                if(shouldFail()){
                    circuitBreacker.requestFailed();
                    throw new RuntimeException("Service unavailable");
                } else{
                    circuitBreacker.requestSucceeded();
                    return "Response from external service";

                }

            } catch (Exception ex){
                circuitBreacker.requestFailed();
                throw ex;
            }
         } else{
                return"Fallback response";
            }
            }

            private boolean shouldFail(){

                // simulate conditions that cause failures
                return Math.random()<0.3;//30% chance of failure
            }
             }

CircuitBreackerPattern.java

     public class CircuitBreackerPattern{

         public static void main(String[] args) {
             CircuitBreacker circuitBreacker= new CircuitBreacker(3,500);

             ExternalService externalService=new ExternalService(circuitBreacker);
             for (int ¡=0; ¡ < 10; ¡ ++){
              try{

                String response= externalService.performRequest();
                System.out.println("Response:" + response);
              }catch (Exception ex) {
                  System.out.println("Exception" + ex.geMessage());
            }
             
            }
            }
             }          
            

