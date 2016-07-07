    echo "powersave" | sudo tee /sys/devices/system/cpu/cpu*/cpufreq/scaling_governor;
    echo "1200000" | sudo tee /sys/devices/system/cpu/cpu*/cpufreq/scaling_min_freq;
    echo "3800000" | sudo tee /sys/devices/system/cpu/cpu*/cpufreq/scaling_max_freq
