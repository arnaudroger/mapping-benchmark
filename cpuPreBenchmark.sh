    echo "performance" | sudo tee /sys/devices/system/cpu/cpu*/cpufreq/scaling_governor;
    echo "3000000" | sudo tee /sys/devices/system/cpu/cpu*/cpufreq/scaling_min_freq;
    echo "3000000" | sudo tee /sys/devices/system/cpu/cpu*/cpufreq/scaling_max_freq
