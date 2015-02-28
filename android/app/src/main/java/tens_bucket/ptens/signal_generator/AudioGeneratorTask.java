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
            double rightSinValue = Math.sin(2 * Math.PI * parameters.getRightFrequency() * time);
            double leftSinValue = Math.sin(2 * Math.PI * parameters.getLeftFrequency() * time);
            buf.put((short) (rightSinValue * parameters.getRightAmplitude() * Short.MAX_VALUE));
            buf.put((short) (leftSinValue * parameters.getLeftAmplitude() * Short.MAX_VALUE));
        }
        return buffer;
    }
}
