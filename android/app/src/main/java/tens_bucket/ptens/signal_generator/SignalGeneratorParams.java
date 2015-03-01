package tens_bucket.ptens.signal_generator;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class SignalGeneratorParams {
    public final WaveParams rightWave = new WaveParams();
    public final WaveParams leftWave = new WaveParams();

    private int sampleRate;

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }


}
