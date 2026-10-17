package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp
public class starerbot_program extends OpMode{
    DriveTrain driveTrain = new DriveTrain();
    DcMotor shooter;
    DcMotor intake;
    CRServo flipper;
    CRServo rightsideintake;
    CRServo leftIntake;

    @Override
    public void init() {
        driveTrain.init(hardwareMap);
        driveTrain.setDriveSpeed(1);

        shooter = hardwareMap.get(DcMotor.class,"shooter");
        intake = hardwareMap.get(DcMotor.class,"intake");
        flipper = hardwareMap.get(CRServo.class,"flipper");
        rightsideintake = hardwareMap.get(CRServo.class,"rightsideintake");
        leftIntake = hardwareMap.get(CRServo.class,"leftIntake");
    }

    @Override
    public void loop() {
        driveTrain.gamepadDrive(gamepad1);
        if(gamepad2.dpadLeftWasPressed()){
            intake.setPower(1);
            rightsideintake.setPower(1);
            leftIntake.setPower(-1);
        } else if (gamepad2.dpadLeftWasReleased()) {
            intake.setPower(0);
            rightsideintake.setPower(0);
            leftIntake.setPower(0);
        }
        if(gamepad2.dpad_right){
            shooter.setPower(0);
        }
        if(gamepad2.x) {
            shooter.setPower(0.75);
        }
        if(gamepad2.bWasPressed()) {

            flipper.setPower(1);
        } else if (gamepad2.bWasReleased()){
            flipper.setPower(0);
        }

    }
}
