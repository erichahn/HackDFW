package tens_bucket.ptens;

import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;


public class SineGenerator extends ActionBarActivity {

    private static final String LOG_TAG = SineGenerator.class.getSimpleName();
    private Button stopButton;
    private Button startButton;

    private SeekBar frequency;
    private SeekBar amplitude;

    private AudioGeneratorTask backgroundTask;
    private AudioGeneratorParams parameters;

    private SeekBar.OnSeekBarChangeListener frequencyListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            parameters.setFrequency(progress + 1);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private SeekBar.OnSeekBarChangeListener amplitudeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            parameters.setAmplitude((double) progress / 100.0);
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

        frequency = (SeekBar) findViewById(R.id.freq_bar);
        amplitude = (SeekBar) findViewById(R.id.amp_bar);

        frequency.setOnSeekBarChangeListener(frequencyListener);
        amplitude.setOnSeekBarChangeListener(amplitudeListener);

        parameters = new AudioGeneratorParams();

        int sampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);
        
        Log.d(LOG_TAG, "Sample Rate: " + sampleRate);

        parameters.setSampleRate(sampleRate);
        parameters.setFrequency(frequency.getProgress() + 1);
        parameters.setAmplitude((double) amplitude.getProgress() / 100.0);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sine_generator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
