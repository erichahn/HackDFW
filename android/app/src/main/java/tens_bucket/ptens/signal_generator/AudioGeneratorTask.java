package tens_bucket.ptens.signal_generator;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.base.Preconditions;

import java.nio.ShortBuffer;

public class AudioGeneratorTask extends AsyncTask<AudioGeneratorParams, Void, Void> {

    private static final String LOG_TAG = AudioGeneratorTask.class.getSimpleName();
    private AudioTrack track;
    private int minBufferSize;

    private static final int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
    private static final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

    @Override
    protected Void doInBackground(AudioGeneratorParams... params) {
        Preconditions.checkNotNull(params);
        Preconditions.checkArgument(params.length == 1, "AudioGeneratorTask can only handle " +
                "a single parameter");
        AudioGeneratorParams parameters = params[0];

        minBufferSize = AudioTrack.getMinBufferSize(parameters.getSampleRate(),
                channelConfig, audioFormat);
        
        Log.d(LOG_TAG, "Min Buffer Size: " + minBufferSize);

        track = new AudioTrack(AudioManager.STREAM_MUSIC, parameters.getSampleRate(),
                channelConfig, audioFormat, minBufferSize, AudioTrack.MODE_STREAM);

        track.play();

        for(int i = 0; !isCancelled(); i++) {
            short[] buffer = generateAudio(parameters, i, minBufferSize);
            int ret = track.write(buffer, 0, buffer.length);
            Log.d(LOG_TAG, "Return value: " + ret);
            if(ret < 0) {
                Log.e(LOG_TAG, "ERROR CODE: " + ret);
            }
        }

        track.release();

        return null;
    }

    private short[] generateAudio(AudioGeneratorParams parameters, int iteration, int bufferSize) {
        short[] buffer = new short[bufferSize];
        int startOffset = iteration * bufferSize / 2;

        ShortBuffer buf = ShortBuffer.wrap(buffer);

        Log.d(LOG_TAG, "Iteration: " + iteration + "; Buffer size: " + bufferSize);
        Log.d(LOG_TAG, "PARAMS: " + parameters);

        for(int i = 0; i < buffer.length / 2; i++) {
            double time = (double) ( i + startOffset ) / parameters.getSampleRate();
            double rightValue = getTensValue(time, parameters.getRightFrequency(),
                    parameters.getRightDutyCycle());
            double leftValue = getTensValue(time, parameters.getLeftFrequency(),
                    parameters.getLeftDutyCycle());
            buf.put((short) (rightValue * parameters.getRightAmplitude() * Short.MAX_VALUE));
            buf.put((short) (leftValue * parameters.getLeftAmplitude() * Short.MAX_VALUE));
        }
        return buffer;
    }

    private double getTensValue(double time, int frequency, double dutyCycle) {
        double period = 1.0/((double)frequency);
        double step = (time) % period;
        double val =  tens(100.0*step/period, dutyCycle);
        return val;
    }

    private double tens(double x, double dutyCycle) {
        double Vh = 1.0;
        double Vl = Vh/3;


        if (x < dutyCycle){
            return 0.25 * Vh / Math.pow(10, x) + 0.75 * Vh;
        }
        else if (x < 2* dutyCycle){
            return -Vl;
        }
        else {
            return -Vl / Math.pow(2, x-2* dutyCycle);
        }

    }
}
