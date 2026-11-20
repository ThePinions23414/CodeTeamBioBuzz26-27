package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

@TeleOp
public class Bumble_RED extends OpMode{
    testSTOOOOOP stooooop = new testSTOOOOOP();
    LimeLight limeLight = new LimeLight();
    private TurretMechanism turret = new TurretMechanism();
    private Limelight3A limelight;
    DcMotorEx shooter;
    DcMotor intake;
    Servo wedge;
    Servo gate;
    Servo PTO;
    Servo hood;

    LEDLights_Bumble light = new LEDLights_Bumble();

    double velocity;
    double P;
    double F;
    boolean shooterActivated;
    boolean readyToFly;
    double PTO_engaged;
    double PTO_disengaged;
    double gate_closedPosition;
    double gate_openPosition;
    double wedge_downPosition;
    double wedge_intakePosition;
    double wedge_shootPosition;
    double hoodAngle;
    int tagNumber;
    ElapsedTime shooterTimer;



    @Override
    public void init() {
        turret.init(hardwareMap);
        limeLight.init(hardwareMap);
        limeLight.startLimeLight(3);

        light.init(hardwareMap);
        stooooop.init(hardwareMap);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        shooter = hardwareMap.get(DcMotorEx.class,"shooter");
        intake = hardwareMap.get(DcMotor.class,"intake");
        wedge = hardwareMap.get(Servo.class,"wedge");
        gate = hardwareMap.get(Servo.class, "gate");
        PTO = hardwareMap.get(Servo.class, "PTO");
        hood = hardwareMap.get(Servo.class, "hood");

        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        velocity = 1500;
        shooterActivated = false;
        readyToFly = false;
        stooooop.setDriveSpeed(0.75);
        P = 65;
        F = 13.3;
        PTO_engaged = 0.41;
        PTO_disengaged = 0.21;
        gate_closedPosition = 0;
        gate_openPosition = 0.17;
        hoodAngle = 0.68; // previously was 0.65, all the way down is 0.69, all the way up is 0.15
        wedge_downPosition = 0.03;
        wedge_intakePosition = 0.4;
        wedge_shootPosition = 0.35;
        tagNumber = 35;
        shooterTimer = new ElapsedTime();




    }
    @Override
    public void start() {
        limelight.start();
        gate.setPosition(gate_closedPosition);
        PTO.setPosition(PTO_disengaged);
        hood.setPosition(hoodAngle);
        wedge.setPosition(wedge_intakePosition);
        shooterTimer.reset();


    }

    @Override
    public void loop() {
//        if (gamepad1.dpadUpWasReleased()){
//            gate.setPosition(gate.getPosition()+ 0.01);
//        }
//        else if (gamepad1.dpadDownWasReleased()){
//            gate.setPosition(gate.getPosition()- 0.01);
//        }
//        if (gamepad1.dpadRightWasReleased()){
//            PTO.setPosition(PTO.getPosition()+ 0.01);
//        }
//        else if (gamepad1.dpadLeftWasReleased()){
//            PTO.setPosition(PTO.getPosition()- 0.01);
//        }
//        if (gamepad1.rightBumperWasReleased()){
//            hood.setPosition(hood.getPosition()+ 0.01);
//        }
//        else if (gamepad1.leftBumperWasReleased()){
//            hood.setPosition(hood.getPosition()- 0.01);
//        }
//        if (gamepad1.rightBumperWasReleased()){
//            wedge.setPosition(wedge.getPosition()+ 0.01);
//        }
//        else if (gamepad1.leftBumperWasReleased()){
//            wedge.setPosition(wedge.getPosition()- 0.01);
//        }

        LLResult id = limeLight.getSpecificId(tagNumber);
        turret.update(id);

        turret.setkP(0.03);
        turret.setkD(0.00001);

        stooooop.gamepadDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
        stooooop.update();
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        LLResult llResult = limelight.getLatestResult();
        if (llResult != null && llResult.isValid()) {
//            if (llResult.getTa() < 0.85) {
//                velocity = 1275;
//                P = 55;
//                F = 13;
//
//            }
//            else if (llResult.getTa() > 0.85) {
//                velocity = 1275;
//                P = 60;
//                F = 13.2;
//
//            }
            List<LLResultTypes.FiducialResult> fiducialResults = llResult.getFiducialResults();
            for (LLResultTypes.FiducialResult fr : fiducialResults) {
                int tagID = fr.getFiducialId();
                if(tagID == 35){
                    tagNumber = 35;
                    turret.setGoalXOffset(-2);

                } else if (tagID == 32) {
                    tagNumber = 32;
                    turret.setGoalXOffset(2);
                }
                telemetry.addData("tag ID", tagID);


            }

           if(llResult.getTx() < -5 && llResult.getTx() > 5){
               light.setRightGreenLed(true);
               light.setRightRedLed(true);
           } else if (llResult.getTx() < 5 || llResult.getTx() > -5) {
               light.setRightGreenLed(false);
               light.setRightRedLed(true);
           }
        } else{
            light.setRightGreenLed(true);
            light.setRightRedLed(false);
        }
        if (llResult != null && llResult.isValid()) {
            Pose3D botPose = llResult.getBotpose();
            telemetry.addData("Tx", llResult.getTx());

        }

        if(gamepad2.dpadLeftWasPressed()){
            intake.setPower(1);

        } else if (gamepad2.dpadLeftWasReleased()) {
            intake.setPower(0);

        }
        if (gamepad2.dpadDownWasPressed()) {
            intake.setPower(-1);

        } else if (gamepad2.dpadDownWasReleased()) {
            intake.setPower(0);

        }
        if(gamepad2.dpad_right){
            shooter.setVelocity(0);
            shooterActivated = false;
            gate.setPosition(gate_closedPosition);
            PTO.setPosition(PTO_disengaged);
            wedge.setPosition(wedge_intakePosition);
        }

        if((gamepad2.bWasPressed() && readyToFly) || (gamepad2.bWasPressed() && shooterTimer.seconds() < 2)) {
            PTO.setPosition(PTO_engaged);
            gate.setPosition(gate_openPosition);
            intake.setPower(0.8);
        } else if (gamepad2.bWasReleased()){
            intake.setPower(0);
        }


        if (gamepad2.x) {
            shooterActivated = true;
            wedge.setPosition(wedge_shootPosition);
        }
        if (shooterActivated) {
            shooter.setVelocity(velocity);
        }

        if (gamepad2.a || gamepad1.a) {
            wedge.setPosition(wedge_downPosition);
            stooooop.setDriveSpeed(0.25);
        }
        if (gamepad2.y || gamepad1.y) {
            wedge.setPosition(wedge_shootPosition);
            stooooop.setDriveSpeed(0.75);
        }




        if(shooter.getVelocity() > velocity - 50 && shooter.getVelocity() < velocity + 50){
            light.setLeftRedLed(true);
            light.setLeftGreenLed(false);
            shooterTimer.reset();
            readyToFly = true;
        } else if (shooter.getVelocity() > velocity - 150 && shooter.getVelocity() < velocity - 50 || shooter.getVelocity() < velocity + 150 && shooter.getVelocity() > velocity + 50) {
            light.setLeftRedLed(true);
            light.setLeftGreenLed(true);
            readyToFly = false;
        }else {
            light.setLeftRedLed(false);
            light.setLeftGreenLed(true);
            readyToFly = false;
        }




        if (llResult != null && llResult.isValid()){
            telemetry.addLine("I saw the sign");
        }



        telemetry.addData("Ta", llResult.getTa());
        telemetry.addData("Current Velocity",shooter.getVelocity());
        telemetry.addData("Target Velocity", velocity);
        telemetry.addData("Shooter Timer", shooterTimer.seconds());
//        telemetry.addData("PTO", PTO.getPosition());
//        telemetry.addData("gate", gate.getPosition());
//        telemetry.addData("wedge", wedge.getPosition());



    }
}
