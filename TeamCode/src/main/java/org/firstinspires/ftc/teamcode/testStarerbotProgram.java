package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;

@TeleOp(name = "Test Starterbot Program", group = "Test")
public class testStarerbotProgram extends OpMode {

    testSTOOOOOP stooooop = new testSTOOOOOP();
    testHiveTargetingMechanism hiveTargetingMechanism = new testHiveTargetingMechanism();
    LEDLights light = new LEDLights();

    private Limelight3A limelight;
    DcMotorEx shooter;
    DcMotor intake;
    CRServo flipper;
    CRServo rightsideintake;
    CRServo leftIntake;
    Servo the_wedge;
    Servo gate;

    double velocity = 1350;
    double P = 55;
    double F = 13;
    double lastP = -1;
    double lastF = -1;

    boolean driveMode = true;
    boolean shooterActivated = false;
    boolean override = false;

    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(4);

        light.init(hardwareMap);
        stooooop.init(hardwareMap);
        stooooop.setDriveSpeed(1.0);

        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        intake = hardwareMap.get(DcMotor.class, "intake");
        flipper = hardwareMap.get(CRServo.class, "flipper");
        rightsideintake = hardwareMap.get(CRServo.class, "rightsideintake");
        leftIntake = hardwareMap.get(CRServo.class, "leftIntake");
        the_wedge = hardwareMap.get(Servo.class, "the_wedge");
        gate = hardwareMap.get(Servo.class, "gate");

        hiveTargetingMechanism.setkP(0.03700);
        hiveTargetingMechanism.setkD(0.00008);
    }

    @Override
    public void start() {
        limelight.start();
        hiveTargetingMechanism.reset();
        gate.setPosition(0);
    }

    @Override
    public void loop() {
        LLResult llResult = limelight.getLatestResult();

        // 1. Limelight distance & AprilTag offset logic
        if (llResult != null && llResult.isValid()) {
            telemetry.addData("Tx", llResult.getTx());
            telemetry.addData("Ta", llResult.getTa());

            if (llResult.getTa() <= 0.85) {
                velocity = 1350;
                P = 55;
                F = 13;
            } else {
                velocity = 1300;
                P = 60;
                F = 13.2;
            }

            List<LLResultTypes.FiducialResult> fiducialResults = llResult.getFiducialResults();
            for (LLResultTypes.FiducialResult fr : fiducialResults) {
                int tagID = fr.getFiducialId();
                hiveTargetingMechanism.setHiveXOffset(2);
                telemetry.addData("Tag ID", tagID);
            }
        } else {
            velocity = 1300;
            P = 60;
            F = 13.2;
        }

        // Only update PIDF coefficients on the Control Hub when values change
        if (P != lastP || F != lastF) {
            PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
            shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
            lastP = P;
            lastF = F;
        }

        // 2. Driver 1: Mode switching & Drivetrain (Pedro Pathing + Auto-Aim)
        if (gamepad1.leftBumperWasReleased() && !driveMode) {
            driveMode = true;
            shooterActivated = false;
            shooter.setVelocity(0);
            gate.setPosition(0);
            hiveTargetingMechanism.reset();
        } else if (gamepad1.rightBumperWasReleased() && driveMode) {
            driveMode = false;
            hiveTargetingMechanism.reset();
        }

        double forward = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double turn;

        if (!driveMode) {
            turn = hiveTargetingMechanism.calculateTurnPower(llResult);
        } else {
            turn = -gamepad1.right_stick_x;
        }

        stooooop.gamepadDrive(forward, strafe, turn);
        stooooop.update();

        // 3. Driver 2: Intake
        if (gamepad2.dpadLeftWasPressed()) {
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

        // 4. Driver 2: Shooter Control
        if (gamepad2.dpad_right) {
            shooterActivated = false;
            shooter.setVelocity(0);
            gate.setPosition(0);
        }

        if (gamepad2.x) {
            shooterActivated = true;
        }

        if (shooterActivated) {
            shooter.setVelocity(velocity);
        }

        // 5. Auto-Fire Safety Check (Both Shooter RPM and Limelight Aim Locked)
        double currentVelocity = shooter.getVelocity();
        double velocityError = Math.abs(currentVelocity - velocity);
        boolean shooterReady = shooterActivated && (velocityError < 50);
        boolean aimReady = !driveMode && hiveTargetingMechanism.isOnTarget();
        boolean readyToFire = shooterReady && aimReady;

        if (gamepad2.rightBumperWasPressed()) {
            override = true;
        } else if (gamepad2.rightBumperWasReleased()) {
            override = false;
        }

        // Flipper & Gate Control
        if (gamepad2.dpad_up) {
            // Reverse flipper to clear jams
            flipper.setPower(-1);
        } else if ((gamepad2.b && readyToFire) || (gamepad2.b && override)) {
            // Auto-gated fire: only feeds when aim & flywheel RPM are both ready
            flipper.setPower(1);
            gate.setPosition(0.25);
        } else {
            flipper.setPower(0);
        }

        // 6. Shared Wedge Controls
        if (gamepad1.a || gamepad2.a) {
            the_wedge.setPosition(0.525);
        } else if (gamepad1.y || gamepad2.y) {
            the_wedge.setPosition(0);
        }

        // 7. LED Status Indicator
        if (readyToFire) {
            // Solid Green: Aimed and at target RPM (Ready to shoot)
            light.setRedLed(true);
            light.setGreenLed(false);
        } else if (shooterActivated && velocityError < 150) {
            // Yellow (Red + Green): Spinning up or aligning
            light.setRedLed(true);
            light.setGreenLed(true);
        } else {
            // Red: Shooter off or outside target window
            light.setRedLed(false);
            light.setGreenLed(true);
        }

        telemetry.addData("Mode", driveMode ? "MANUAL" : "AUTO-AIM");
        telemetry.addData("Shooter Velocity", "%.1f / %.1f", currentVelocity, velocity);
        telemetry.addData("Shooter Ready", shooterReady);
        telemetry.addData("Aim Ready", aimReady);
        telemetry.addData("READY TO FIRE", readyToFire);
        telemetry.update();
    }
}
