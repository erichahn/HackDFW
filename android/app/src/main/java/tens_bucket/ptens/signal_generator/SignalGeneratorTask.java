package tens_bucket.ptens.signal_generator;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.base.Preconditions;

import java.nio.ShortBuffer;

public class SignalGeneratorTask extends AsyncTask<SignalGeneratorParams, Void, Void> {

    private static final String LOG_TAG = SignalGeneratorTask.class.getSimpleName();
    private AudioTrack track;
    private int minBufferSize;

    private static final int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
    private static final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private static final int MAX_SIGNAL_VALUE = Short.MAX_VALUE;

    @Override
    protected Void doInBackground(SignalGeneratorParams... params) {
        Preconditions.checkNotNull(params);
        Preconditions.checkArgument(params.length == 1, "AudioGeneratorTask can only handle " +
                "a single parameter");
        SignalGeneratorParams parameters = params[0];

        minBufferSize = AudioTrack.getMinBufferSize(parameters.getSampleRate(),
                channelConfig, audioFormat);
        
        Log.d(LOG_TAG, "Min Buffer Size: " + minBufferSize);

        track = new AudioTrack(AudioManager.STREAM_MUSIC, parameters.getSampleRate(),
                channelConfig, audioFormat, minBufferSize, AudioTrack.MODE_STREAM);

        track.play();

        for(int i = 0; !isCancelled(); i++) {
            short[] buffer = generateAudio(parameters, i, minBufferSize);
            int ret = track.write(buffer, 0, buffer.length);
            if(ret < 0) {
                Log.e(LOG_TAG, "ERROR CODE: " + ret);
            }
        }

        track.release();

        return null;
    }

    private short[] generateAudio(SignalGeneratorParams parameters, int iteration, int bufferSize) {
        short[] buffer = new short[bufferSize];
        int startOffset = iteration * bufferSize / 2;

        ShortBuffer buf = ShortBuffer.wrap(buffer);

        for(int i = 0; i < buffer.length / 2; i++) {
            double time = (double) ( i + startOffset ) / parameters.getSampleRate();
            double rightValue = getTensValue(parameters.rightWave, time);
            double leftValue = getTensValue(parameters.leftWave, time);
            buf.put((short) (rightValue * MAX_SIGNAL_VALUE));
            buf.put((short) (leftValue * MAX_SIGNAL_VALUE));
        }
        return buffer;
    }

    private double getTensValue(WaveParams waveParams, double time) {
        double period = 1.0/((double) waveParams.getFrequency());
        double step = (time) % period;
        return tens(waveParams, 100.0*step/period);
    }

    private double tens(WaveParams waveParams, double percentOfPeriodComplete) {
        double Vh = waveParams.getAmplitude();
        double Vl = -Vh/3;

        if (percentOfPeriodComplete < waveParams.getDutyCycle()){
            return 0.25 * Vh / Math.pow(10, percentOfPeriodComplete) + 0.75 * Vh;
        }
        else if (percentOfPeriodComplete < 2* waveParams.getDutyCycle()){
            return Vl;
        }
        else {
            return Vl / Math.pow(4, percentOfPeriodComplete-2* waveParams.getDutyCycle());
        }

    }
}
