package tens_bucket.ptens;

import android.app.FragmentTransaction;
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
import android.widget.Switch;

import tens_bucket.ptens.fragments.PadFragment;
import tens_bucket.ptens.signal_generator.SignalGeneratorParameters;
import tens_bucket.ptens.signal_generator.SignalGeneratorTask;


public class SignalGenerator extends ActionBarActivity {

    private static final String LOG_TAG = SignalGenerator.class.getSimpleName();

    private Switch pad1Switch;
    private Switch pad2Switch;

    private SignalGeneratorTask backgroundTask;
    private SignalGeneratorParameters parameters;
    private PadFragment pad2;
    private PadFragment pad1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sine_generator);


        pad1Switch = (Switch) findViewById(R.id.pad1_switch);
        pad2Switch = (Switch) findViewById(R.id.pad2_switch);


        parameters = new SignalGeneratorParameters();
        int sampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC);

        Log.d(LOG_TAG, "Sample Rate: " + sampleRate);

        parameters.setSampleRate(sampleRate);

        pad1 = (PadFragment)(getFragmentManager().findFragmentById(R.id.pad_1));
        pad1.setWaveParameters(parameters.leftWave);

        pad2 = (PadFragment)(getFragmentManager().findFragmentById(R.id.pad_2));
        pad2.setWaveParameters(parameters.rightWave);

        togglePad(pad1Switch);
        togglePad(pad2Switch);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disableOutput();
    }

    public void togglePad(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        if (view == pad1Switch){
            boolean checked = pad1Switch.isChecked();
            if (checked) {
                parameters.leftWave.setAmplitude(0);
                ft.show(pad1);
            }else{
                ft.hide(pad1);
            }
            parameters.leftWave.setEnabled(checked);
            if(checked) pad1.updateBarsFromParams();
        }
        else if (view == pad2Switch){
            boolean checked = pad2Switch.isChecked();
            if (checked) {
                parameters.rightWave.setAmplitude(0);
                ft.show(pad2);
            }else{
                ft.hide(pad2);
            }
            parameters.rightWave.setEnabled(checked);
            if(checked) pad2.updateBarsFromParams();
        }
        ft.commit();

        if(parameters.leftWave.isEnabled() || parameters.rightWave.isEnabled()) {
            enableOutput();
        } else {
            disableOutput();
        }
    }

    private void enableOutput() {
        backgroundTask = new SignalGeneratorTask();
        backgroundTask.execute(parameters);
    }

    private void disableOutput() {
        if(backgroundTask != null) {
            backgroundTask.cancel(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.basic:
                return switchToBasic();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean switchToBasic() {
        Intent intent = new Intent(this, TherapyMenu.class);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.avd_options, menu);
        return true;
    }
}
