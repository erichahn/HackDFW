package tens_bucket.ptens.signal_generator;

import android.provider.ContactsContract;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.concurrent.Semaphore;

import tens_bucket.ptens.fragments.PadFragment;

public class SignalParameter {
    private volatile int frequency;
    private volatile double dutyCycle;
    private volatile double amplitude;

    private PadFragment fragment;
    private volatile boolean enabled = true;

    private LineGraphSeries<DataPoint> dataPlot = new LineGraphSeries<DataPoint>();


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

    public PadFragment getFragment() {
        return fragment;
    }

    public void setFragment(PadFragment fragment) {
        this.fragment = fragment;
    }

    public LineGraphSeries<DataPoint> getDataPlot() {
        return dataPlot;
    }

    public void setDataPlot(LineGraphSeries<DataPoint> dataPlot) {
        this.dataPlot = dataPlot;
    }

    public double tens(double percentOfPeriodComplete) {
        if (!enabled){
            return 0;
        }
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

    public double getTensValue(double time) {
        double period = 1.0/((double) getFrequency());
        double step = (time) % period;

        double val = tens(100.0 * step / period);

        return val;

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

