package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedro.bumbleConstants;

public class testSTOOOOOP {

    private Follower follower;
    private double driveSpeed = 1.0;

    public void init(HardwareMap hardwareMap) {
        this.follower = bumbleConstants.create(hardwareMap);
    }

    public void setDriveSpeed(double speed) {
        driveSpeed = speed;
    }

    public void gamepadDrive(double forward, double strafe, double turn) {
        ManualDrive.driveOrHold(
                follower,
                forward * driveSpeed,
                strafe * driveSpeed,
                turn * driveSpeed
        );
    }

    public void update() {
        follower.update();
    }
}
