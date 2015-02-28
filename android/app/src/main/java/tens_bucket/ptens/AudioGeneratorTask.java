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
            byte[] buffer = generateAudio(parameters, i, minBufferSize);
            track.write(buffer, 0, buffer.length);
        }

        track.release();

        return null;
    }

    private byte[] generateAudio(AudioGeneratorParams parameters, int iteration, int minBufferSize) {
        byte[] buffer = new byte[minBufferSize];
        int startOffset = iteration * minBufferSize;
        ShortBuffer buf = ByteBuffer.wrap(buffer).asShortBuffer();

        double maxValue = parameters.getAmplitude() * Short.MAX_VALUE;
        int frequency = parameters.getFrequency();

        Log.d(LOG_TAG, "Max value: " + maxValue + "; Frequency: " + frequency +
                "; Buffer size: " + minBufferSize);

        for(int i = 0; i < buffer.length / 2 ; i++) {
            double time = (double) i / parameters.getSampleRate() + startOffset;
            double sinValue = Math.sin(2 * Math.PI * frequency * time);
            buf.put((short) (maxValue * sinValue));
        }
        return buffer;
    }
}
