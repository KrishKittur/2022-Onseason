package frc.robot.simulation;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.swerve.VisionFollowerController;

public class FakeRobot extends TimedRobot {

    XboxController controller = new XboxController(0);

    Pose2d fieldPose = new Pose2d();
    public final Field2d field = new Field2d();

    @Override
    public void robotInit() {
        super.robotInit();
        SmartDashboard.putData(field);
        setNetworkTablesFlushEnabled(true);
        field.getObject("Goal").setPose(new Pose2d(4, 4, new Rotation2d()));
    }

    private final PIDController turnPID = new PIDController(
            10 * (Math.PI/180.0), 0, 0.004
    );

    @Override
    public void robotPeriodic() {
        // X_k+1 = X_k + X-dot * dt
        double vy = -controller.getLeftY() * 3;
        double vx = controller.getLeftX() * 3;

        var omega = VisionFollowerController.getFeedforward(new Translation2d(vx, vy), fieldPose);
        Rotation2d robotToGoalAngle = new Rotation2d(fieldPose.getX(), fieldPose.getY()).rotateBy(Rotation2d.fromDegrees(180.0));
        double pid = turnPID.calculate(fieldPose.getRotation().getDegrees(), robotToGoalAngle.getDegrees());
        omega += pid;
        omega += controller.getRawAxis(2) * 5;

        var speeds = ChassisSpeeds.fromFieldRelativeSpeeds(vx, vy, omega, fieldPose.getRotation());

        fieldPose = fieldPose.plus(new Transform2d(
                new Translation2d(speeds.vxMetersPerSecond, speeds.vyMetersPerSecond),
                new Rotation2d(speeds.omegaRadiansPerSecond)).times(0.020));

        var pose = new Pose2d(fieldPose.getX() + 4, fieldPose.getY() + 4, fieldPose.getRotation());
        field.setRobotPose(pose);
    }
}
