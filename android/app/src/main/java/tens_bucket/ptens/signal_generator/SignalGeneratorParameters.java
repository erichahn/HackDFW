package tens_bucket.ptens.signal_generator;

public class SignalGeneratorParameters {
    public final SignalParameters rightWave = new SignalParameters();
    public final SignalParameters leftWave = new SignalParameters();

    private int sampleRate;

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }


}
