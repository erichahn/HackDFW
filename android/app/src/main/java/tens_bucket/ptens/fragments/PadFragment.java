package tens_bucket.ptens.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import tens_bucket.ptens.R;
import tens_bucket.ptens.signal_generator.SignalParameters;

public class PadFragment extends Fragment {

    private SeekBar frequency;
    private SeekBar amplitude;
    private SeekBar dutyCycle;
    private GraphView graph;

    private SignalParameters signal;
    private LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();


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
            if(signal != null){
                setPlotSignal(signal);
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View r = inflater.inflate(R.layout.pad_fragment, container, false);

        frequency = (SeekBar) r.findViewById(R.id.freq_bar);
        amplitude = (SeekBar) r.findViewById(R.id.amp_bar);
        dutyCycle = (SeekBar) r.findViewById(R.id.duty_bar);
        graph = (GraphView) r.findViewById(R.id.graph);


        frequency.setOnSeekBarChangeListener(changeListener);
        amplitude.setOnSeekBarChangeListener(changeListener);
        dutyCycle.setOnSeekBarChangeListener(changeListener);

        setupGraph();



        return r;
    }

    public void setWaveParameters(SignalParameters param){
        this.signal = param;
        setupDefaultWaveParameters();
        setPlotSignal(signal);
    }
    private void setupDefaultWaveParameters() {
        signal.setAmplitude(amplitude.getProgress() / 100.0);
        signal.setFrequency(frequency.getProgress() + 1);
        signal.setDutyCycle(dutyCycle.getProgress() / 10.0 + 3.5);
    }

    private void setupGraph(){
        graph.removeAllSeries();
        graph.addSeries(series);
    }

    public void setPlotSignal(SignalParameters signalParameters){
        graph.removeAllSeries();
        LineGraphSeries<DataPoint> points = new LineGraphSeries<DataPoint>();

        int res = 500;
        for (int i=0; i < res; i = i +1)
        {
            double x = ((double)i)/(70*res);
            double tens = signalParameters.getSignalValue(x);
            points.appendData(new DataPoint(x,tens),false, res);
        }
        graph.addSeries(points);

    }

    public LineGraphSeries<DataPoint> getSeries() {
        return series;
    }

    public void setSeries(LineGraphSeries<DataPoint> series) {
        this.series = series;
    }
}