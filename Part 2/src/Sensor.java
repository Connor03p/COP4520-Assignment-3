class Sensor extends Task 
{
    public Sensor() {
        super((long)(App.sensorInterval / App.timeMultiplier), "Sensor");
    }    

    @Override
    public void run() 
    {   
        Reading reading = new Reading(System.currentTimeMillis());
        SharedMem.addReading(reading);
        if (App.PRINT_READINGS)
            System.out.println("Recorded Data: " + reading.value + "F, Offset: " + intervalOffset + " ms");
        checkIntervalOffset();        
    }
}