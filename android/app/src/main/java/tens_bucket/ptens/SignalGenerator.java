package tens_bucket.ptens;

import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import tens_bucket.ptens.signal_generator.SignalGeneratorParams;
import tens_bucket.ptens.signal_generator.SignalGeneratorTask;
import tens_bucket.ptens.signal_generator.WaveParams;


public class SignalGenerator extends ActionBarActivity {

    private static final String LOG_TAG = SignalGenerator.class.getSimpleName();
    private Button stopButton;
    private Button startButton;

    private SeekBar rightFrequency;
    private SeekBar rightAmplitude;
    private SeekBar rightDutyCycle;

    private SeekBar leftFrequency;
    private SeekBar leftAmplitude;
    private SeekBar leftDutyCycle;

    private SignalGeneratorTask backgroundTask;
    private SignalGeneratorParams parameters;

    private SeekBar.OnSeekBarChangeListener changeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(seekBar == rightFrequency) {
                parameters.rightWave.setFrequency(progress + 1);
            } else if(seekBar == rightAmplitude) {
                parameters.rightWave.setAmplitude(progress / 100.0);
            } else if(seekBar == rightDutyCycle) {
                parameters.rightWave.setDutyCycle(progress / 10.0 + 3.5);
            } else if(seekBar == leftFrequency) {
                parameters.leftWave.setFrequency(progress + 1);
            } else if(seekBar == leftAmplitude) {
                parameters.leftWave.setAmplitude(progress / 100.0);
            } else if(seekBar == leftDutyCycle) {
                parameters.leftWave.setDutyCycle(progress / 10.0 + 3.5);
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
        rightDutyCycle = (SeekBar) findViewById(R.id.right_duty_bar);

        rightFrequency.setOnSeekBarChangeListener(changeListener);
        rightAmplitude.setOnSeekBarChangeListener(changeListener);
        rightDutyCycle.setOnSeekBarChangeListener(changeListener);

        leftFrequency = (SeekBar) findViewById(R.id.left_freq_bar);
        leftAmplitude = (SeekBar) findViewById(R.id.left_amp_bar);
        leftDutyCycle = (SeekBar) findViewById(R.id.left_duty_bar);

        leftFrequency.setOnSeekBarChangeListener(changeListener);
        leftAmplitude.setOnSeekBarChangeListener(changeListener);
        leftDutyCycle.setOnSeekBarChangeListener(changeListener);

        parameters = new SignalGeneratorParams();

        int sampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
        
        Log.d(LOG_TAG, "Sample Rate: " + sampleRate);

        parameters.setSampleRate(sampleRate);

        setupDefaultWaveParameters(parameters.leftWave);
        setupDefaultWaveParameters(parameters.rightWave);
    }

    private void setupDefaultWaveParameters(WaveParams wave) {
        wave.setAmplitude(leftAmplitude.getProgress() / 100.0);
        wave.setFrequency(leftFrequency.getProgress() + 1);
        wave.setDutyCycle(leftDutyCycle.getProgress() / 10.0 + 3.5);
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
        backgroundTask = new SignalGeneratorTask();
        backgroundTask.execute(parameters);

    }

    private void changeButtonStates(boolean started) {
        stopButton.setEnabled(started);
        startButton.setEnabled(!started);
    }
}
