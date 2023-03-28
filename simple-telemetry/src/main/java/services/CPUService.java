package services;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class CPUService {
    private static final OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();


    public double getFreeMemorySpace() {
        //calculate to GB
        double res = osBean.getFreeMemorySize() / (double) (1024 * 1024 * 1024);
        String formattedValue = String.format("%.2f", res);
        formattedValue = formattedValue.replace(",", ".");
        return Double.parseDouble(formattedValue);
    }

    public double getAvailableCores(){
        return osBean.getAvailableProcessors();
    }

    public double getCpuUsage() {
        double cpuUsage = osBean.getCpuLoad();
        if (cpuUsage < 0) {
            // cpu usage is not available
            return -1;
        } else {
            double res = cpuUsage * 100;
            String formattedValue = String.format("%.2f", res);
            formattedValue = formattedValue.replace(",", ".");
            return Double.parseDouble(formattedValue);
        }
    }
}
