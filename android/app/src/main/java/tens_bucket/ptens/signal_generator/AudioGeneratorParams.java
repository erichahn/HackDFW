package tens_bucket.ptens.signal_generator;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class AudioGeneratorParams {
    private volatile int leftFrequency;
    private volatile double leftAmplitude;
    private volatile double leftDutyCycle;

    private volatile int rightFrequency;
    private volatile double rightAmplitude;
    private volatile double rightDutyCycle;

    private int sampleRate;

    public int getRightFrequency() {
        return rightFrequency;
    }

    public void setRightFrequency(int rightFrequency) {
        this.rightFrequency = rightFrequency;
    }

    public double getRightAmplitude() {
        return rightAmplitude;
    }

    public void setRightAmplitude(double rightAmplitude) {
        this.rightAmplitude = rightAmplitude;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getLeftFrequency() {
        return leftFrequency;
    }

    public void setLeftFrequency(int leftFrequency) {
        this.leftFrequency = leftFrequency;
    }

    public double getLeftAmplitude() {
        return leftAmplitude;
    }

    public void setLeftAmplitude(double leftAmplitude) {
        this.leftAmplitude = leftAmplitude;
    }

    public double getLeftDutyCycle() {
        return leftDutyCycle;
    }

    public void setLeftDutyCycle(double leftDutyCycle) {
        this.leftDutyCycle = leftDutyCycle;
    }

    public double getRightDutyCycle() {
        return rightDutyCycle;
    }

    public void setRightDutyCycle(double rightDutyCycle) {
        this.rightDutyCycle = rightDutyCycle;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("Right freq", rightFrequency)
                .add("Right amp", rightAmplitude)
                .add("Left freq", leftFrequency)
                .add("Left amp", leftAmplitude)
                .add("Sample rate", sampleRate)
                .toString();
    }
}
