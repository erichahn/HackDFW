package tens_bucket.ptens.signal_generator;

public class SignalParameter {
    private volatile int frequency;
    private volatile double dutyCycle;
    private volatile double amplitude;

    public SignalParameter() {
        this(1, 4.5, 1.0);
    }

    public SignalParameter(int frequency, double dutyCycle, double amplitude) {
        this.frequency = frequency;
        this.dutyCycle = dutyCycle;
        this.amplitude = amplitude;
    }

    public int getFrequency() {
        return frequency;
    }

    public double getDutyCycle() {
        return dutyCycle;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setDutyCycle(double dutyCycle) {
        this.dutyCycle = dutyCycle;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }
}
