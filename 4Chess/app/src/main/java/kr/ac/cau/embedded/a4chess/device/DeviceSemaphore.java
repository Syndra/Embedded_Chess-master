package kr.ac.cau.embedded.a4chess.device;

public class DeviceSemaphore {

    private static boolean dotSema =  true;
    private static boolean buzzerSema = true;
    private static boolean motorSema = true;

    public static void dot_init()
    {
        while(!dotSema)
        {
            //busy wait
        }
        dotSema = false;
    }

    public static void dot_deinit()
    {
        dotSema = true;
    }

    public static void buzzer_init()
    {
        while(!buzzerSema)
        {
            //busy wait
        }
        buzzerSema = false;
    }

    public static void buzzer_deinit()
    {
        buzzerSema = true;
    }

    public static void motor_init()
    {
        while(!motorSema)
        {
            //busy wait
        }
        motorSema = false;
    }

    public static void motor_deinit()
    {
        motorSema = true;
    }
}
