package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedro.Constants;

public class STOOOOOP {

    private Follower follower;
    private double driveSpeed;

    public void init(HardwareMap hardwareMap) {

        this.follower = Constants.create(hardwareMap);


    }

    public void drive(double forward, double strafe, double turn) {
        follower.manual(forward, strafe, turn);

    }

    public void setDriveSpeed(double speed) {
        driveSpeed = speed;
    }


    public void gamepadDrive(double forward, double strafe, double turn){
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

