package tens_bucket.ptens.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import tens_bucket.ptens.R;
import tens_bucket.ptens.signal_generator.SignalParameter;

public class PadFragment extends Fragment {

    private SeekBar frequency;
    private SeekBar amplitude;
    private SeekBar dutyCycle;
    public SignalParameter signal;

    private SeekBar.OnSeekBarChangeListener changeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (signal != null) {
                if (seekBar == frequency) {
                    signal.setFrequency(progress + 1);
                } else if (seekBar == amplitude) {
                    signal.setAmplitude(progress / 100.0);
                } else if (seekBar == dutyCycle) {
                    signal.setDutyCycle(progress / 10.0 + 3.5);
                }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View r = inflater.inflate(R.layout.pad_fragment, container, false);

        frequency = (SeekBar) r.findViewById(R.id.freq_bar);
        amplitude = (SeekBar) r.findViewById(R.id.amp_bar);
        dutyCycle = (SeekBar) r.findViewById(R.id.duty_bar);


        frequency.setOnSeekBarChangeListener(changeListener);
        amplitude.setOnSeekBarChangeListener(changeListener);
        dutyCycle.setOnSeekBarChangeListener(changeListener);

        return r;
    }

    public void setWaveParameters(SignalParameter param){
        this.signal = param;
        setupDefaultWaveParameters();
    }
    private void setupDefaultWaveParameters() {
        signal.setAmplitude(amplitude.getProgress() / 100.0);
        signal.setFrequency(frequency.getProgress() + 1);
        signal.setDutyCycle(dutyCycle.getProgress() / 10.0 + 3.5);
    }
}