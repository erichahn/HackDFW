package tens_bucket.ptens.signal_generator;

public class AudioGeneratorParams {
    private volatile int leftFrequency;
    private volatile double leftAmplitude;

    private volatile int rightFrequency;
    private volatile double rightAmplitude;

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
}
