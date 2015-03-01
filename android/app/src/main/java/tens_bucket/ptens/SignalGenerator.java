package tens_bucket.ptens;

import android.app.FragmentTransaction;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import tens_bucket.ptens.fragments.PadFragment;
import tens_bucket.ptens.signal_generator.SignalGeneratorParameter;
import tens_bucket.ptens.signal_generator.SignalGeneratorTask;


public class SignalGenerator extends FragmentActivity {

    private static final String LOG_TAG = SignalGenerator.class.getSimpleName();

    private Switch pad1Switch;
    private Switch pad2Switch;

    private SignalGeneratorTask backgroundTask;
    private SignalGeneratorParameter parameters;
    private PadFragment pad2;
    private PadFragment pad1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sine_generator);


        pad1Switch = (Switch) findViewById(R.id.switch1);
        pad2Switch = (Switch) findViewById(R.id.switch2);


        parameters = new SignalGeneratorParameter();
        pad1 = (PadFragment)(getFragmentManager().findFragmentById(R.id.pad_1));
        pad1.setWaveParameters(parameters.leftWave);
        parameters.leftWave.setFragment(pad1);

        pad2 = (PadFragment)(getFragmentManager().findFragmentById(R.id.pad_2));
        pad2.setWaveParameters(parameters.rightWave);
        parameters.rightWave.setFragment(pad2);


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

    public void toggle(View view) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in,
                android.R.animator.fade_out);
        if (view == pad1Switch){
            if (pad1Switch.isChecked()) {
                ft.show(pad1);
                parameters.leftWave.setEnabled(true);
            }else{
                ft.hide(pad1);
                parameters.leftWave.setEnabled(false);
            }

        }
        else if (view == pad2Switch){
            if (pad2Switch.isChecked()) {
                ft.show(pad2);
                parameters.rightWave.setEnabled(true);
            }else{
                ft.hide(pad2);
                parameters.rightWave.setEnabled(false);

            }

        }
        ft.commit();
    }


}
