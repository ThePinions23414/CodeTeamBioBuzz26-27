//package org.firstinspires.ftc.teamcode.pedro;
//
//import com.pedropathing.algorithm.Foresight;
//import com.pedropathing.algorithm.ForesightConfig;
//import com.pedropathing.controllers.Controller;
//import com.pedropathing.follower.Follower;
//import com.pedropathing.math.Matrix;
//import com.pedropathing.math.Vector2D;
//import com.pedropathing.revhub.drivetrains.Mecanum;
//import com.pedropathing.revhub.drivetrains.MecanumConfig;
//import com.pedropathing.revhub.localizers.PinpointConfig;
//import com.pedropathing.revhub.localizers.PinpointLocalizer;
//import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//
//public class Constants {
//
//    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
//        c.frontLeftName.set("frontLeft");
//        c.frontRightName.set("frontRight");
//        c.backLeftName.set("backLeft");
//        c.backRightName.set("backRight");
//        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
//        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
//        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
//        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
//    });
//
//    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
//        c.name.set("pinpoint");
//        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
//        c.xPodOffset.set(-3.2417576707254248);
//        c.yPodOffset.set(-0.7908592824860822);
//        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
//        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
//        c.globalDistanceUnit.set(DistanceUnit.INCH);
//        c.offsetUnits.set(DistanceUnit.INCH);
//    });
//
//    public static ForesightConfig foresightConfig = new ForesightConfig(
//            c -> {
//                Controller primaryTranslationalForward = Controller.proportional(0.29484322091475595);
//                Controller secondaryTranslationalForward = Controller.proportional(0.10893669177204172);
//                Controller primaryTranslationalLateral = Controller.proportional(0.4550338721606113);
//                Controller secondaryTranslationalLateral = Controller.proportional(0.16812285703435123);
//
//                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
//                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));
//
//                c.coast.set(Controller.proportionalFeedforward(0.011899803061807976));
//                c.brake.set(Controller.proportionalFeedforward(0.01011483260253678));
//
//                c.headingFeedback.set(Controller.proportional(5.491577698282715));
//                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.06643696201807467, 0.0029648372055725743));
//
//                c.linearBrakeCoefficients.set(Matrix.diag(0.09821673183324676, 0.06262870807032767));
//                c.quadraticBrakeCoefficients.set(Matrix.diag(9.872208476340361E-4, 0.0016251171137772994));
//
//                c.maxAchievableForwardVelocity.set(79.857107287636);
//                c.maxAchievableStrafeVelocity.set(63.26264955138763);
//                c.naturalForwardDeceleration.set(25.337419543216313);
//                c.naturalStrafeDeceleration.set(68.97670682327872);
//            }
//    );
//
//    public static Follower create(HardwareMap h) {
//        return new Follower(
//                new PinpointLocalizer(h, localizerConfig),
//                new Mecanum(h, drivetrainConfig),
//                new Foresight(foresightConfig)
//        );
//    }
//
//}