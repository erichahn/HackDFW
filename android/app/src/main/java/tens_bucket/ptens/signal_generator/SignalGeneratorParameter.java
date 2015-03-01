package tens_bucket.ptens.signal_generator;

public class SignalGeneratorParameter {
    public final SignalParameter rightWave = new SignalParameter();
    public final SignalParameter leftWave = new SignalParameter();

    private int sampleRate;

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }


}
