package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous
public class leaveAuto extends LinearOpMode {

    DcMotor fL;
    DcMotor bL;
    DcMotor fR;
    DcMotor bR;

    @Override
    public void runOpMode() throws InterruptedException {

        DriveTrain driveTrain = new DriveTrain();
        DcMotorEx shooter;
        DcMotor intake;
        CRServo flipper;
        CRServo rightsideintake;
        CRServo leftIntake;

        LEDLights light = new LEDLights();

        double velocity;
        double velocity2;
        double velocity3;

        fL = hardwareMap.get(DcMotor.class, "frontLeft");
        bL = hardwareMap.get(DcMotor.class, "backLeft");
        fR = hardwareMap.get(DcMotor.class, "frontRight");
        bR = hardwareMap.get(DcMotor.class, "backRight");

        fL.setDirection(DcMotorSimple.Direction.REVERSE);
        bL.setDirection(DcMotorSimple.Direction.REVERSE);

        fL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();


        if (isStopRequested()) {
            return;
        }

        if (opModeIsActive()) {
            fL.setPower(.5);
            fR.setPower(.5);
            bL.setPower(.5);
            bR.setPower(.5);
            sleep(350);
            fL.setPower(0);
            fR.setPower(0);
            bL.setPower(0);
            bR.setPower(0);

        }

    }


}
