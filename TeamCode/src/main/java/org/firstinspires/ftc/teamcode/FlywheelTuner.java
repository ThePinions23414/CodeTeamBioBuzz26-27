package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp
public class FlywheelTuner extends OpMode {
    public DcMotorEx shooter;

    public double highVelocity = 1350;
    public double lowVelocity = 300;

    public double targetVelocity = highVelocity;

    double P = 0;
    double F = 0;

    double[] stepSizes = {10.0, 1.0, 0.1, 0.01, 0.001, 0.0001, 0.00001, 0.000001};

    int stepIndex = 1;

    @Override
    public void init() {
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P,0,0,F);
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

    }

    @Override
    public void loop() {
        if (gamepad1.yWasPressed()) {
            if (targetVelocity == highVelocity) {
                targetVelocity = lowVelocity;
            } else {
                targetVelocity = highVelocity;
            }
        }

        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }

        if (gamepad1.dpadLeftWasPressed()) {
            F -= stepSizes[stepIndex];
        }

        if (gamepad1.dpadRightWasPressed()) {
            F += stepSizes[stepIndex];
        }

        if (gamepad1.dpadDownWasPressed()) {
            P -= stepSizes[stepIndex];
        }

        if (gamepad1.dpadUpWasPressed()) {
            P += stepSizes[stepIndex];
        }

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P,0,0,F);
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        shooter.setVelocity(targetVelocity);

        double velocity = shooter.getVelocity();
        double error = targetVelocity - velocity;

        telemetry.addData("Target Velocity: ", targetVelocity);
        telemetry.addData("Current Velocity: ", "%.2f", velocity);
        telemetry.addData("Error", "%.2f", error);
        telemetry.addLine("----------------------------");
        telemetry.addData("Tuning P: ", "%.4f (D-Pad U/D)", P);
        telemetry.addData("Tuning F: ", "%.4f (D-Pad R/L)", F);
        telemetry.addData("Step Size: ", "%.4f (B Button", stepSizes[stepIndex]);
        telemetry.update();

    }
}
