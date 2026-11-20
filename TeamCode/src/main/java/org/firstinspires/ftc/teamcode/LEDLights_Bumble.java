package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;

public class LEDLights_Bumble {

    private LED leftRedLED;
    private LED leftGreenLED;
    private LED rightRedLED;
    private LED rightGreenLED;

    public void init(HardwareMap hwmap){
        leftRedLED = hwmap.get(LED.class, "leftRedLED");
        leftGreenLED = hwmap.get(LED.class, "leftGreenLED");
        rightRedLED = hwmap.get(LED.class, "rightRedLED");
        rightGreenLED = hwmap.get(LED.class, "rightGreenLED");
    }

    public void setLeftRedLed(boolean isOn){
        if (isOn){
            leftRedLED.on();
        }
        else{
            leftRedLED.off();
        }
    }

    public void setLeftGreenLed(boolean isOn){
        if (isOn){
            leftGreenLED.on();
        }
        else{
            leftGreenLED.off();
        }
    }

    public void setRightRedLed(boolean isOn){
        if (isOn){
            rightRedLED.on();
        }
        else{
            rightRedLED.off();
        }
    }

    public void setRightGreenLed(boolean isOn){
        if (isOn){
            rightGreenLED.on();
        }
        else{
            rightGreenLED.off();
        }
    }
}
