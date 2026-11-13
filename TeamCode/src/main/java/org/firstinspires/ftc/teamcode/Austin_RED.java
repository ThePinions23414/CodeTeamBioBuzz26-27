package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;


@TeleOp
public class Austin_RED extends OpMode{
    DriveTrain driveTrain = new DriveTrain();
    private Limelight3A limelight;
    DcMotorEx shooter;
    DcMotor intake;
    CRServo flipper;
    CRServo rightsideintake;
    CRServo leftIntake;
    Servo the_wedge;
    Servo gate;

    LEDLights light = new LEDLights();
    HiveTargetingMechanism hiveTargetingMechanism = new HiveTargetingMechanism();

    double velocity;
    double P;
    double F;
    boolean driveMode = true;
    double[] stepSizes = {0.1, 0.01, 0.001, 0.0001, 0.00001, 0.000001};
    int stepIndex = 2;
    boolean shooterActivated;


    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(3);
        light.init(hardwareMap);
        hiveTargetingMechanism.init(hardwareMap);
        driveTrain.init(hardwareMap);

        shooter = hardwareMap.get(DcMotorEx.class,"shooter");
        intake = hardwareMap.get(DcMotor.class,"intake");
        flipper = hardwareMap.get(CRServo.class,"flipper");
        rightsideintake = hardwareMap.get(CRServo.class,"rightsideintake");
        leftIntake = hardwareMap.get(CRServo.class,"leftIntake");
        the_wedge = hardwareMap.get(Servo.class,"the_wedge");
        gate = hardwareMap.get(Servo.class, "gate");

        velocity = 1350;
        shooterActivated = false;
        driveTrain.setDriveSpeed(1);


    }
    @Override
    public void start() {
        limelight.start();
        hiveTargetingMechanism.resetTimer();
        gate.setPosition(0);

    }

    @Override
    public void loop() {
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        LLResult llResult = limelight.getLatestResult();
        if (llResult != null && llResult.isValid()) {
            if (llResult.getTa() < 0.85) {
                velocity = 1350;
                P = 55;
                F = 13;

            }
            else if (llResult.getTa() > 0.85) {
                velocity = 1300;
                P = 60;
                F = 13.2;

            }
            List<LLResultTypes.FiducialResult> fiducialResults = llResult.getFiducialResults();
            for (LLResultTypes.FiducialResult fr : fiducialResults) {
                int tagID = fr.getFiducialId();
                hiveTargetingMechanism.setHiveXOffset(2);
                telemetry.addData("tag ID", tagID);


//                if (tagID == 32) {
//                    hiveTargetingMechanism.setHiveXOffset(-2);
//                }
//                else if (tagID == 35){
//                    hiveTargetingMechanism.setHiveXOffset(4);
//                }
//                else if (tagID == 34){
//                    hiveTargetingMechanism.setHiveXOffset(2);
//                }
//                else if (tagID == 34){
//                    hiveTargetingMechanism.setHiveXOffset(2);
//                }
//                else if (tagID == 34){
//                    hiveTargetingMechanism.setHiveXOffset(2);
//                }
//                else if (tagID == 34){
//                    hiveTargetingMechanism.setHiveXOffset(2);
//                }
//                else if (tagID == 34){
//                    hiveTargetingMechanism.setHiveXOffset(2);
//                }
//                else if (tagID == 34){
//                    hiveTargetingMechanism.setHiveXOffset(2);
//                }

            }
        } else {
            velocity = 1300;
            P = 60;
            F = 13.2;
        }
        if (llResult != null && llResult.isValid()) {
            Pose3D botPose = llResult.getBotpose();
            telemetry.addData("Tx", llResult.getTx());

        }

        if (gamepad1.leftBumperWasReleased() && !driveMode) {
            driveMode = true;
            shooter.setVelocity(0);
            gate.setPosition(0);
            if (llResult == null || !llResult.isValid()) {
                hiveTargetingMechanism.stopRobot();
            }
        } else if (gamepad1.rightBumperWasReleased() && driveMode) {
            driveMode = false;
        }
        if (!driveMode){
            hiveTargetingMechanism.update(llResult);
        } else if (driveMode) {
            driveTrain.gamepadDrive(gamepad1);
        }


        hiveTargetingMechanism.setkP(0.03700);
        hiveTargetingMechanism.setkD(0.00008);

//        if ((llResult.getTa() < 0.6) && (llResult.getTa() > 0.47)){
//            velocity = 1225;
//            telemetry.addLine("We");
//        } else if ((llResult.getTa() < 0.47) && (llResult.getTa() > 0.38)) {
//            velocity = 1275;
//            telemetry.addLine("are");
//        } else if ((llResult.getTa() < 0.38)) {
//            velocity = 1350;
//            telemetry.addLine("awesome");
//        }


        if(gamepad2.dpadLeftWasPressed()){
            intake.setPower(1);
            rightsideintake.setPower(1);
            leftIntake.setPower(-1);
        } else if (gamepad2.dpadLeftWasReleased()) {
            intake.setPower(0);
            rightsideintake.setPower(0);
            leftIntake.setPower(0);
        }
        if (gamepad2.dpadDownWasPressed()) {
            intake.setPower(-1);
            rightsideintake.setPower(-1);
            leftIntake.setPower(1);
        } else if (gamepad2.dpadDownWasReleased()) {
            intake.setPower(0);
            rightsideintake.setPower(0);
            leftIntake.setPower(0);
        }
        if(gamepad2.dpad_right){
            shooter.setVelocity(0);
            shooterActivated = false;
            gate.setPosition(0);
        }

        if(gamepad2.bWasPressed() && !driveMode) {
            flipper.setPower(1);
            gate.setPosition(0.25);
        } else if (gamepad2.bWasReleased()){
            flipper.setPower(0);
        }

        if(gamepad2.dpadUpWasPressed()) {
            flipper.setPower(-1);
        } else if (gamepad2.dpadUpWasReleased()){
            flipper.setPower(0);
        }

        if (gamepad2.x) {
            shooterActivated = true;
        }
        if (shooterActivated) {
            shooter.setVelocity(velocity);
        }

        if (gamepad2.a) {
            the_wedge.setPosition(0.525);
        }
        if (gamepad2.y) {
            the_wedge.setPosition(0);
        }

        if (gamepad1.a) {
            the_wedge.setPosition(0.525);
        }
        if (gamepad1.y) {
            the_wedge.setPosition(0);
        }




        if(shooter.getVelocity() > velocity - 50 && shooter.getVelocity() < velocity + 50){
            light.setRedLed(true);
            light.setGreenLed(false);
        } else if (shooter.getVelocity() > velocity - 150 && shooter.getVelocity() < velocity - 50 || shooter.getVelocity() < velocity + 150 && shooter.getVelocity() > velocity + 50) {
            light.setRedLed(true);
            light.setGreenLed(true);
        }else {
            light.setRedLed(false);
            light.setGreenLed(true);
        }

//        if (gamepad1.bWasPressed()){
//            stepIndex = (stepIndex + 1) % stepSizes.length;
//        }
//
//        if (gamepad1.dpadLeftWasPressed()){
//            hiveTargetingMechanism.setkP(hiveTargetingMechanism.getkP() - stepSizes[stepIndex]);
//        }
//
//        if (gamepad1.dpadRightWasPressed()){
//
//            hiveTargetingMechanism.setkP(hiveTargetingMechanism.getkP() + stepSizes[stepIndex]);
//        }
//
//        if (gamepad1.dpadUpWasPressed()){
//            hiveTargetingMechanism.setkD(hiveTargetingMechanism.getkD() + stepSizes[stepIndex]);
//        }
//
//        if (gamepad1.dpadDownWasPressed()){
//            hiveTargetingMechanism.setkD(hiveTargetingMechanism.getkD() - stepSizes[stepIndex]);
//        }


        if (llResult != null && llResult.isValid()){
            telemetry.addLine("I saw the sign");
        }



        telemetry.addData("Drive Mode", driveMode);
        telemetry.addData("Ta", llResult.getTa()); // 0.58, 0.47, 0.37
//        telemetry.addData("Tuning P","%.5f (D-Pad L/R)", hiveTargetingMechanism.getkP());
//        telemetry.addData("Tuning D","%.5f (D-Pad U/D)", hiveTargetingMechanism.getkD());
//        telemetry.addData("Step Size","%.5f (B Button)", stepSizes[stepIndex]);

        telemetry.addData("Current Velocity",shooter.getVelocity());
        telemetry.addData("Target Velocity", velocity);


    }
}
