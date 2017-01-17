package pl.projektorion.krzysztof.blesensortag.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krzysztof on 16.01.17.
 */

public class MConvolve
{
    public static List<Double> convolve(List<Double> data, List<Double> kernel)
    {
        /*
        Implementation based on the following resource
        https://docs.scipy.org/doc/scipy/reference/tutorial/signal.html

        def convolve(data, kernel):
            convoluted = []
            data_size = len(data)
            kernel_size = len(kernel)
            for n in range(data_size + kernel_size - 1):
                k_min = max(n + 1 - kernel_size, 0)
                k_max = min(n + 1, data_size)
                value = 0
                for k in range(k_min, k_max):
                    value += data[k] * kernel[n - k]
                convoluted.append(value)
            return convoluted

         */
        final List<Double> convoluted = new ArrayList<>();
        final int dataSize = data.size();
        final int kernel_size = kernel.size();
        for( int n = 0; n < dataSize + kernel_size - 1; n++)
        {
            final int kMin = Math.max(n + 1 - kernel_size, 0);
            final int kMax= Math.min(n + 1, dataSize);
            double value = 0.0f;
            for(int k = kMin; k < kMax; k++)
                value += data.get(k) * kernel.get(n - k);
            convoluted.add(value);
        }
        return convoluted;
    }
}
