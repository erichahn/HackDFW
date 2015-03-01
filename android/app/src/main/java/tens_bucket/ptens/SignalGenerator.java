package tens_bucket.ptens;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import tens_bucket.ptens.fragments.PadFragment;
import tens_bucket.ptens.signal_generator.SignalGeneratorParams;
import tens_bucket.ptens.signal_generator.SignalGeneratorTask;
import tens_bucket.ptens.signal_generator.WaveParams;


public class SignalGenerator extends FragmentActivity {

    private static final String LOG_TAG = SignalGenerator.class.getSimpleName();
    private Button stopButton;
    private Button startButton;

    private SignalGeneratorTask backgroundTask;
    private SignalGeneratorParams parameters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sine_generator);

        startButton = (Button) findViewById(R.id.start_button);
        stopButton = (Button) findViewById(R.id.stop_button);


        parameters = new SignalGeneratorParams();
        PadFragment pad1 =(PadFragment)(getFragmentManager().findFragmentById(R.id.pad_1));
        pad1.setWaveParameters(parameters.leftWave);

        PadFragment pad2 =(PadFragment)(getFragmentManager().findFragmentById(R.id.pad_2));
        pad2.setWaveParameters(parameters.rightWave);


        int sampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
        
        Log.d(LOG_TAG, "Sample Rate: " + sampleRate);

        parameters.setSampleRate(sampleRate);
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
