package tens_bucket.ptens.signal_generator;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.base.Preconditions;

import java.nio.ShortBuffer;

public class SignalGeneratorTask extends AsyncTask<SignalGeneratorParameters, Void, Void> {

    private static final String LOG_TAG = SignalGeneratorTask.class.getSimpleName();
    private AudioTrack track;

    private static final int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
    private static final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private static final int MAX_SIGNAL_VALUE = Short.MAX_VALUE;


    @Override
    protected Void doInBackground(SignalGeneratorParameters... params) {
        Preconditions.checkNotNull(params);
        Preconditions.checkArgument(params.length == 1, "AudioGeneratorTask can only handle " +
                "a single parameter");
        SignalGeneratorParameters parameters = params[0];

        int minBufferSize = AudioTrack.getMinBufferSize(parameters.getSampleRate(),
                channelConfig, audioFormat);
        
        Log.d(LOG_TAG, "Min Buffer Size: " + minBufferSize);

        track = new AudioTrack(AudioManager.STREAM_MUSIC, parameters.getSampleRate(),
                channelConfig, audioFormat, minBufferSize, AudioTrack.MODE_STREAM);

        track.play();

        for(int i = 0; !isCancelled(); i++) {
            short[] buffer = generateAudio(parameters, i, minBufferSize / 4);
            int ret = track.write(buffer, 0, buffer.length);
            if(ret < 0) {
                Log.e(LOG_TAG, "ERROR CODE: " + ret);
            }
        }

        track.release();

        return null;
    }

    private short[] generateAudio(SignalGeneratorParameters parameters, int iteration, int bufferSize) {
        short[] buffer = new short[bufferSize];
        int startOffset = iteration * bufferSize / 2;

        ShortBuffer buf = ShortBuffer.wrap(buffer);

        for(int i = 0; i < buffer.length / 2; i++) {
            double time = (double) ( i + startOffset ) / parameters.getSampleRate();
            double rightValue = parameters.rightWave.isEnabled() ?
                    parameters.rightWave.getSignalValue(time) : 0;
            double leftValue = parameters.leftWave.isEnabled() ?
                    parameters.leftWave.getSignalValue(time) : 0;
            buf.put((short) (rightValue * MAX_SIGNAL_VALUE));
            buf.put((short) (leftValue * MAX_SIGNAL_VALUE));
        }
        return buffer;
    }

}
