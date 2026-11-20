package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.LimeLight;
import org.firstinspires.ftc.teamcode.TurretMechanism;


@Utility
public class TurretAutoAlign extends OpMode {

    // Import private limelight class
    LimeLight limeLight = new LimeLight();

    private TurretMechanism turret = new TurretMechanism();

    double[] stepSizes = {0.1, 0.01, 0.001, 0.0001, 0.00001, 0.000001};
    int stepIndex = 2;
    DcMotor fL;
    DcMotor bL;
    DcMotor fR;
    DcMotor bR;
    double driveSpeed = 1;


    @Override
    public void init() {
        // limelight.init(hardwareMap, telemetry);
        turret.init(hardwareMap);
        limeLight.init(hardwareMap);
        limeLight.startLimeLight(3);




        telemetry.addLine("Initialized all mechanisms");
    }

    @Override
    public void start() {
        turret.resetTimer();

    }

    @Override
    public void loop() {
        // run vision logic - ll.update, he did "aprilTagDetection id20 = aprilTagWebcam.getTagBySpecificId(20);" to only get id 20

        LLResult id35 = limeLight.getSpecificId(35);
        turret.update(id35);
        limeLight.update();

        turret.setkP(0.033);
        turret.setkD(0.00001);


        if (gamepad1.bWasPressed()){
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }

        if (gamepad1.dpadLeftWasPressed()){
            turret.setkP(turret.getkP() - stepSizes[stepIndex]);
        }

        if (gamepad1.dpadRightWasPressed()){
            turret.setkP(turret.getkP() + stepSizes[stepIndex]);
        }

        if (gamepad1.dpadUpWasPressed()){
            turret.setkD(turret.getkD() + stepSizes[stepIndex]);
        }

        if (gamepad1.dpadDownWasPressed()){
            turret.setkD(turret.getkD() - stepSizes[stepIndex]);
        }

        if (id35 != null){
            telemetry.addLine("I saw the sign");
        }




        telemetry.addData("Tuning P","%.5f (D-Pad L/R)", turret.getkP());
        telemetry.addData("Tuning D","%.5f (D-Pad U/D)", turret.getkD());
        telemetry.addData("Step Size","%.5f (B Button)", stepSizes[stepIndex]);
    }
}
