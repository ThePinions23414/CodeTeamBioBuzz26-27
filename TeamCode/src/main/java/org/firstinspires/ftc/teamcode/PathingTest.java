package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
@Disabled
@Autonomous(name = "AutoPath", group = "Autonomous")
public class PathingTest extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(56, 8, 90);
    private final Pose penStroke = poseFactory.of(55.2388, 36.4355, -4.5483);
    private final Pose penStrokeThrough1 = poseFactory.of(82.9052, 19.2149, 0);
    private final Pose penStrokeThrough2 = poseFactory.of(102.5954, 42.7359, 0);
    private final Pose penStrokeThrough3 = poseFactory.of(101.0267, 84.1051, 0);
    private final Pose penStrokeThrough4 = poseFactory.of(77.1509, 113.3611, 0);
    private final Pose penStrokeThrough5 = poseFactory.of(37.7467, 114.6878, 0);
    private final Pose penStrokeThrough6 = poseFactory.of(29.1027, 80.7499, 0);
    private final Pose penStrokeThrough7 = poseFactory.of(21.6864, 41.5003, 0);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
//                follow(follower, penStroke())
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        waitForStart();
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }
    }

//    public Path penStroke() {
//        return PathingTest.through(start, penStrokeThrough1, penStrokeThrough2, penStrokeThrough3, penStrokeThrough4, penStrokeThrough5, penStrokeThrough6, penStrokeThrough7, penStroke).tangent();
    }
//}
