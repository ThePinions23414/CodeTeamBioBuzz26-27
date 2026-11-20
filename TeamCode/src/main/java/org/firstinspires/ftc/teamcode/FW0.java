package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
@Disabled
@TeleOp
public class FW0 extends OpMode {

    Servo wedge;

    @Override
    public void init() {
        wedge = hardwareMap.get(Servo.class, "wedge");

    }

    @Override
    public void start() {
        wedge.setPosition(0);

    }

    @Override
    public void loop() {

    }
}
