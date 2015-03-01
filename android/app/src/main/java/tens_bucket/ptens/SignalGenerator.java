package tens_bucket.ptens;

import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import tens_bucket.ptens.fragments.PadFragment;
import tens_bucket.ptens.signal_generator.SignalGeneratorParameter;
import tens_bucket.ptens.signal_generator.SignalGeneratorTask;


public class SignalGenerator extends FragmentActivity {

    private static final String LOG_TAG = SignalGenerator.class.getSimpleName();
    private Button stopButton;
    private Button startButton;

    private SignalGeneratorTask backgroundTask;
    private SignalGeneratorParameter parameters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sine_generator);

        startButton = (Button) findViewById(R.id.start_button);
        stopButton = (Button) findViewById(R.id.stop_button);


        parameters = new SignalGeneratorParameter();
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
