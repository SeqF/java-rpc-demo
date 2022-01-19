package utils;

public class RuntimeUtil {

    /**
     * 获取 CPU 的核心数
     * @return cpu cores
     */
    public static int cpus() {
        return Runtime.getRuntime().availableProcessors();
    }
}
