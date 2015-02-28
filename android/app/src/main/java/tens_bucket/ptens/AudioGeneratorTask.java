package tens_bucket.ptens;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.base.Preconditions;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

public class AudioGeneratorTask extends AsyncTask<AudioGeneratorParams, Void, Void> {

    private static final String LOG_TAG = AudioGeneratorTask.class.getSimpleName();
    private AudioTrack track;
    private int minBufferSize;

    private static final int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
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
        int startOffset = iteration * bufferSize;

        ShortBuffer buf = ShortBuffer.wrap(buffer);

        Log.d(LOG_TAG, "Iteration: " + iteration + "; Buffer size: " + bufferSize);

        for(int i = 0; i < buffer.length; i++) {
            double time = (double) ( i + startOffset ) / parameters.getSampleRate();
            double sinValue = Math.sin(2 * Math.PI * parameters.getFrequency() * time);
            buf.put((short) (sinValue * parameters.getAmplitude() * Short.MAX_VALUE));
        }
        return buffer;
    }
}
