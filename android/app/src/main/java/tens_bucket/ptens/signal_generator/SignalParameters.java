package tens_bucket.ptens.signal_generator;

public class SignalParameters {
    private volatile int frequency;
    private volatile double dutyCycle;
    private volatile double amplitude;
    private volatile boolean enabled;

    public SignalParameters() {
        this(1, 4.5, 1.0, true);
    }

    public SignalParameters(int frequency, double dutyCycle, double amplitude, boolean enabled) {
        this.frequency = frequency;
        this.dutyCycle = dutyCycle;
        this.amplitude = amplitude;
        this.enabled = enabled;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double tens(double percentOfPeriodComplete) {
        double Vh = getAmplitude();
        double Vl = -Vh/3;

        if (percentOfPeriodComplete < getDutyCycle()){
            return 0.25 * Vh / Math.pow(10, percentOfPeriodComplete) + 0.75 * Vh;
        }
        else if (percentOfPeriodComplete < 2* getDutyCycle()){
            return Vl;
        }
        else {
            return Vl / Math.pow(4, percentOfPeriodComplete-2* getDutyCycle());
        }

    }

    public double getSignalValue(double time) {
        double period = 1.0/((double) getFrequency());
        double step = (time) % period;

        double val = tens(100.0 * step / period);

        return val;

    }
}

