package pl.projektorion.krzysztof.blesensortag.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krzysztof on 16.01.17.
 */

public class MFilter {
    public static List<Double> gaussFilter1D(double sigma, int length)
    {
        /*
        def gauss_filter(sigma, length):
            values = []
            length = length + 1 if length % 2 == 0 else length
            start_at = int(-(length-1) / 2)
            end_at = int((length-1) / 2 + 1)
            for x in range(start_at, end_at):
                v = math.exp(-math.pow(x, 2.0) / (2.0*math.pow(sigma, 2.0))) / (sigma * math.sqrt(2.0 * math.pi))
                values.append(v)
            return values
         */
        final List<Double> values = new ArrayList<>();
        length = length % 2 == 0 ? length + 1 : length;
        final int startAt = (-(length-1) / 2);
        final int endAt = (length-1) / 2 + 1;
        for( int x = startAt; x < endAt; x++ )
        {
            final double val = Math.exp(-Math.pow(x, 2.0f) / (2.0f * Math.pow(sigma, 2.0f)))
                    / (sigma * Math.sqrt(2.0f * Math.PI));
            values.add(val);
        }
        return values;
    }
}
