package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class shootAuto extends LinearOpMode {

    ElapsedTime timer = new ElapsedTime();

    DcMotor fL;
    DcMotor bL;
    DcMotor fR;
    DcMotor bR;
    DcMotorEx shooter;
    CRServo flipper;
    Servo gate;
    DcMotor intake;

    double velocity;
    boolean readyToMove;
    int readyToReset;

    @Override
    public void runOpMode() throws InterruptedException {


        velocity = 1350;
        readyToMove = false;
        readyToReset = 0;

        fL = hardwareMap.get(DcMotor.class, "frontLeft");
        bL = hardwareMap.get(DcMotor.class, "backLeft");
        fR = hardwareMap.get(DcMotor.class, "frontRight");
        bR = hardwareMap.get(DcMotor.class, "backRight");
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        flipper = hardwareMap.get(CRServo.class,"flipper");
        gate = hardwareMap.get(Servo.class,"gate");
        intake = hardwareMap.get(DcMotor.class, "intake");

        fL.setDirection(DcMotorSimple.Direction.REVERSE);
        bL.setDirection(DcMotorSimple.Direction.REVERSE);

        fL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
            timer.reset();

        if (isStopRequested()) {
            return;
        }

        if (opModeIsActive()) {
            PIDFCoefficients pidfCoefficients = new PIDFCoefficients(55, 0, 0, 13);
            shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
            shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            shooter.setVelocity(velocity);
            gate.setPosition(0);



            while (opModeIsActive()) {


                if (shooter.getVelocity() > velocity - 50 && shooter.getVelocity() < velocity + 50 && !readyToMove) {
                    if (readyToReset == 0){
                        timer.reset();
                        readyToReset = 1;
                    }
                    gate.setPosition(0.25);
                    flipper.setPower(1);
                    intake.setPower(1);
                    if(timer.seconds() > 10){
                        readyToMove = true;
                    }
                }

                if (readyToMove){
                    flipper.setPower(0);
                    intake.setPower(0);
                    gate.setPosition(0);
                    shooter.setVelocity(0);
                    bL.setPower(-0.5);
                    bR.setPower(-0.5);
                    fL.setPower(-0.5);
                    fR.setPower(-0.5);
                    sleep(250);
                    bL.setPower(0);
                    bR.setPower(0);
                    fL.setPower(0);
                    fR.setPower(0);

                    bL.setPower(-0.5);
                    bR.setPower(0.5);
                    fL.setPower(0.5);
                    fR.setPower(-0.5);
                    sleep(1000);
                    bL.setPower(0);
                    bR.setPower(0);
                    fL.setPower(0);
                    fR.setPower(0);

                    bL.setPower(-0.5);
                    bR.setPower(-0.5);
                    fL.setPower(-0.5);
                    fR.setPower(-0.5);
                    sleep(1500);
                    bL.setPower(0);
                    bR.setPower(0);
                    fL.setPower(0);
                    fR.setPower(0);

                    bL.setPower(-0.5);
                    bR.setPower(0.5);
                    fL.setPower(0.5);
                    fR.setPower(-0.5);
                    sleep(400);
                    bL.setPower(0);
                    bR.setPower(0);
                    fL.setPower(0);
                    fR.setPower(0);

                    readyToMove = false;
                }

            }

        }

    }


}
