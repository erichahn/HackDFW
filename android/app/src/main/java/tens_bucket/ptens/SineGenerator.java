package tens_bucket.ptens;

import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import tens_bucket.ptens.signal_generator.AudioGeneratorParams;
import tens_bucket.ptens.signal_generator.AudioGeneratorTask;


public class SineGenerator extends ActionBarActivity {

    private static final String LOG_TAG = SineGenerator.class.getSimpleName();
    private Button stopButton;
    private Button startButton;

    private SeekBar rightFrequency;
    private SeekBar rightAmplitude;

    private SeekBar leftFrequency;
    private SeekBar leftAmplitude;

    private AudioGeneratorTask backgroundTask;
    private AudioGeneratorParams parameters;

    private SeekBar.OnSeekBarChangeListener changeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(seekBar == rightFrequency) {
                parameters.rightWave.setFrequency(progress + 1);
            } else if(seekBar == rightAmplitude) {
                parameters.rightWave.setAmplitude(progress / 100.0);
            } else if(seekBar == leftFrequency) {
                parameters.leftWave.setFrequency(progress + 1);
            } else if(seekBar == leftAmplitude) {
                parameters.leftWave.setAmplitude(progress / 100.0);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sine_generator);

        startButton = (Button) findViewById(R.id.start_button);
        stopButton = (Button) findViewById(R.id.stop_button);

        rightFrequency = (SeekBar) findViewById(R.id.right_freq_bar);
        rightAmplitude = (SeekBar) findViewById(R.id.right_amp_bar);

        rightFrequency.setOnSeekBarChangeListener(changeListener);
        rightAmplitude.setOnSeekBarChangeListener(changeListener);

        leftFrequency = (SeekBar) findViewById(R.id.left_freq_bar);
        leftAmplitude = (SeekBar) findViewById(R.id.left_amp_bar);

        leftFrequency.setOnSeekBarChangeListener(changeListener);
        leftAmplitude.setOnSeekBarChangeListener(changeListener);

        parameters = new AudioGeneratorParams();

        int sampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
        
        Log.d(LOG_TAG, "Sample Rate: " + sampleRate);

        parameters.setSampleRate(sampleRate);
        parameters.setRightFrequency(rightFrequency.getProgress() + 1);
        parameters.setRightAmplitude((double) rightAmplitude.getProgress() / 100.0);
        parameters.setLeftFrequency(leftFrequency.getProgress() + 1);
        parameters.setLeftAmplitude((double) leftAmplitude.getProgress() / 100.0);
        parameters.setRightDutyCycle(4.5);
        parameters.setLeftDutyCycle(4.5);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(backgroundTask != null) {
            backgroundTask.cancel(true);
        }
    }

    public void stop(View view) {
        changeButtonStates(false);
        backgroundTask.cancel(true);
    }

    public void start(View view) {
        changeButtonStates(true);
        backgroundTask = new AudioGeneratorTask();
        backgroundTask.execute(parameters);

    }

    private void changeButtonStates(boolean started) {
        stopButton.setEnabled(started);
        startButton.setEnabled(!started);
    }
}
