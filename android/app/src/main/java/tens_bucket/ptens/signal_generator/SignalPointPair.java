package tens_bucket.ptens.signal_generator;

import com.jjoe64.graphview.series.DataPoint;

import tens_bucket.ptens.fragments.PadFragment;

/**
 * Created by Conor on 3/1/2015.
 */
public class SignalPointPair {

    public DataPoint[] dataPoint;
    public PadFragment padFragment;

    SignalPointPair(DataPoint[] dataPoint, PadFragment padFragment){
        this.dataPoint = dataPoint;
        this.padFragment = padFragment;
    }
}
