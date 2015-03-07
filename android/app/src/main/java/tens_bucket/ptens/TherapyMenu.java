package tens_bucket.ptens;

import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import java.util.Random;

import tens_bucket.ptens.fragments.PadFragment;
import tens_bucket.ptens.signal_generator.SignalGeneratorParameters;
import tens_bucket.ptens.signal_generator.SignalGeneratorTask;
import tens_bucket.ptens.signal_generator.SignalParameters;

/**
 * Created by Conor on 3/1/2015.
 */
public class TherapyMenu extends ActionBarActivity {

    private SignalGeneratorTask backgroundTask;
    private SignalGeneratorParameters parameters;
    private boolean started = false;

    private Button stopButton;
    private Button relievePainButton;
    private Button relaxButton;
    private Button pulseButton;
    private Button luckyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.therapy_menu);
        parameters = new SignalGeneratorParameters();
        int sampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);

        parameters.setSampleRate(sampleRate);

        stopButton = (Button)findViewById(R.id.stop);
        relievePainButton = (Button)findViewById(R.id.button);
        relaxButton = (Button)findViewById(R.id.button2);
        pulseButton = (Button)findViewById(R.id.button3);
        luckyButton = (Button)findViewById(R.id.button4);
        stopButton.setEnabled(false);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void clickRelievePain(View view){
        // 1 - 150
        parameters.leftWave.setFrequency(140);
        parameters.rightWave.setFrequency(140);

        // 0 - 1
        parameters.leftWave.setAmplitude(1);
        parameters.rightWave.setAmplitude(1);

        // 3.5 - 20
        parameters.leftWave.setDutyCycle(3.5);
        parameters.rightWave.setDutyCycle(3.5);
        start();
        relievePainButton.setEnabled(false);

    }

    public void clickRelax(View view){
        // 1 - 150
        parameters.leftWave.setFrequency(70);
        parameters.rightWave.setFrequency(70);

        // 0 - 1
        parameters.leftWave.setAmplitude(1);
        parameters.rightWave.setAmplitude(1);

        // 3.5 - 20
        parameters.leftWave.setDutyCycle(3.5);
        parameters.rightWave.setDutyCycle(3.5);
        start();
        relaxButton.setEnabled(false);
    }

    public void clickBurst(View view){
        // 1 - 150
        parameters.leftWave.setFrequency(1);
        parameters.rightWave.setFrequency(1);

        // 0 - 1
        parameters.leftWave.setAmplitude(1);
        parameters.rightWave.setAmplitude(1);

        // 3.5 - 20
        parameters.leftWave.setDutyCycle(3.5);
        parameters.rightWave.setDutyCycle(3.5);
        start();
        pulseButton.setEnabled(false);

    }

    public void clickLucky(View view){
        Random r = new Random();

        // 1 - 150
        parameters.leftWave.setFrequency(r.nextInt(150));
        parameters.rightWave.setFrequency(r.nextInt(150));

        // 0 - 1
        parameters.leftWave.setAmplitude(r.nextInt(100)/100.0);
        parameters.rightWave.setAmplitude(r.nextInt(100)/100.0);

        // 3.5 - 20
        parameters.leftWave.setDutyCycle(r.nextInt(17) + 3.5 );
        parameters.rightWave.setDutyCycle(r.nextInt(17) + 3.5 );
        start();
        luckyButton.setEnabled(false);

    }

    public void clickAdvanced(View view){
        Intent intent = new Intent(this, SignalGenerator.class);
        startActivity(intent);
    }

    public void clickStop(View view){
        this.stop();
    }

    public void stop(){

        if(backgroundTask != null) {
            Log.d("stopped","stopped");
            backgroundTask.cancel(true);
        }
        enableAllOptions();
        parameters.leftWave.setEnabled(false);
        parameters.rightWave.setEnabled(false);
        stopButton.setEnabled(false);
        started = false;
        backgroundTask = null;
    }
    public void start(){
        if (started){
            stop();
        }
        enableAllOptions();
        parameters.leftWave.setEnabled(true);
        parameters.rightWave.setEnabled(true);
        stopButton.setEnabled(true);
        backgroundTask = new SignalGeneratorTask();
        backgroundTask.execute(parameters);
    }

    public void enableAllOptions(){
        relievePainButton.setEnabled(true);
        relaxButton.setEnabled(true);
        pulseButton.setEnabled(true);
        luckyButton.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_options,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.advanced:
                clickAdvanced(stopButton);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
