package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class testHiveTargetingMechanism {

    private double kP = 0.03800;
    private double kD = 0.00008;
    private double hiveX = 0;

    private double lastError = 0;
    private boolean hasLastError = false;
    private boolean onTarget = false;

    private double angleTolerance = 2;
    private final double MAX_POWER = 1.0;
    private final double MIN_POWER = 0.09; // Minimum power to overcome static friction

    private final ElapsedTime timer = new ElapsedTime();

    public void setHiveXOffset(double hiveXOffset) {
        hiveX = hiveXOffset;
    }

    public void setkP(double newKP) {
        kP = newKP;
    }

    public double getkP() {
        return kP;
    }

    public void setkD(double newKD) {
        kD = newKD;
    }

    public double getkD() {
        return kD;
    }

    public boolean isOnTarget() {
        return onTarget;
    }

    public void reset() {
        timer.reset();
        lastError = 0;
        hasLastError = false;
        onTarget = false;
    }

    public double calculateTurnPower(LLResult result) {
        double deltaTime = timer.seconds();
        timer.reset();

        if (result == null || !result.isValid()) {
            lastError = 0;
            hasLastError = false;
            onTarget = false;
            return 0;
        }

        double error = hiveX - result.getTx();

        if (Math.abs(error) <= angleTolerance) {
            lastError = error;
            hasLastError = true;
            onTarget = true;
            return 0;
        }

        onTarget = false;

        // Note: If the robot spins away from the tag instead of toward it,
        // flip the signs on both pTerm and dTerm (remove the minus signs).
        double pTerm = -error * kP;

        double dTerm = 0;
        if (hasLastError && deltaTime > 0) {
            dTerm = -((error - lastError) / deltaTime) * kD;
        }

        double rawPower = pTerm + dTerm;

        if (Math.abs(rawPower) < MIN_POWER) {
            rawPower = Math.copySign(MIN_POWER, rawPower);
        }

        lastError = error;
        hasLastError = true;

        return Range.clip(rawPower, -MAX_POWER, MAX_POWER);
    }
}

