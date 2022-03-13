package frc.robot.autonomus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.TrajectoryConstraint;
import edu.wpi.first.math.util.Units;

public class Trajectories {

    public static Trajectory allianceSideSemiCircle = generateTrajectory(
        true, 
        List.of(
            new Pose2d(Units.feetToMeters(21.838), Units.feetToMeters(19.19), Rotation2d.fromDegrees(135)),
            new Pose2d(Units.feetToMeters(17.279), Units.feetToMeters(21.788), Rotation2d.fromDegrees(-111.473)),
            new Pose2d(Units.feetToMeters(15.423), Units.feetToMeters(10.819), Rotation2d.fromDegrees(-83.105)),
            new Pose2d(Units.feetToMeters(19.636), Units.feetToMeters(4.267), Rotation2d.fromDegrees(-48.244)),
            new Pose2d(Units.feetToMeters(24.385), Units.feetToMeters(2.022), Rotation2d.fromDegrees(-5.029))
        ), 1.0, 0.5, 0.0, 0.0
    );

    public static Trajectory advanceToHumanPlayerStation = generateTrajectory(
        true, 
        List.of(
            new Pose2d(Units.feetToMeters(25.063), Units.feetToMeters(1.498), Rotation2d.fromDegrees(134.196)),
            new Pose2d(Units.feetToMeters(5.355), Units.feetToMeters(4.39), Rotation2d.fromDegrees(-135))
        ), 1.0, 0.5, 0.0, 0.0
    );

    public static Trajectory returnFromHumanPlayerStation = generateTrajectory(
        true, 
        List.of(
            new Pose2d(Units.feetToMeters(5.355), Units.feetToMeters(4.39), Rotation2d.fromDegrees(20)),
            new Pose2d(Units.feetToMeters(17.683), Units.feetToMeters(8.899), Rotation2d.fromDegrees(20))
        ), 1.0, 0.5, 0.0, 0.0
    );

    public static Trajectory twoCargoLeftTarmac = generateTrajectory(
        true, 
        List.of(
            new Pose2d(Units.feetToMeters(19.28), Units.feetToMeters(15.925), Rotation2d.fromDegrees(-178.417)),
            new Pose2d(Units.feetToMeters(15.891), Units.feetToMeters(16.711), Rotation2d.fromDegrees(64.495)),
            new Pose2d(Units.feetToMeters(17.03), Units.feetToMeters(19.728), Rotation2d.fromDegrees(60.288))
        ), 2.0, 1.0, 0.0, 0.0
    );

    public static Trajectory threeCargoRightTarmac = generateTrajectory(
        true, 
        List.of(
            new Pose2d(Units.feetToMeters(27.813), Units.feetToMeters(6.042), Rotation2d.fromDegrees(-88.691)),
            new Pose2d(Units.feetToMeters(28.53), Units.feetToMeters(3.01), Rotation2d.fromDegrees(-0.58)),
            new Pose2d(Units.feetToMeters(23.921), Units.feetToMeters(2.134), Rotation2d.fromDegrees(178.4)),
            new Pose2d(Units.feetToMeters(16.53), Units.feetToMeters(6.514), Rotation2d.fromDegrees(121.21))
        ), 2.0, 2.0, 0.0, 0.0
    );

    public static Trajectory advanceToHumanPlayerStationAfterThreeCargo = generateTrajectory(
        true, 
        List.of(
            new Pose2d(Units.feetToMeters(16.53), Units.feetToMeters(6.514), Rotation2d.fromDegrees(-171.607)),
            new Pose2d(Units.feetToMeters(5.145), Units.feetToMeters(4.32), Rotation2d.fromDegrees(-168.043))
        ), 2.0, 2.0, 0.0, 0.0
    );

    public static Trajectory returnFromHumanPlayerStationAfterThreeCargo = generateTrajectory(
        true, 
        List.of(
            new Pose2d(Units.feetToMeters(5.145), Units.feetToMeters(4.32), Rotation2d.fromDegrees(7.564)),
            new Pose2d(Units.feetToMeters(19.176), Units.feetToMeters(6.979), Rotation2d.fromDegrees(10.405))
        ), 2.0, 2.0, 0.0, 0.0
    );

    public static Trajectory fiveCargoRightTarmac_getFirstBall = generateTrajectory(
        true, 
        List.of(
            new Pose2d(Units.feetToMeters(23.671), Units.feetToMeters(6.192), Rotation2d.fromDegrees(-135)),
            new Pose2d(Units.feetToMeters(24.032), Units.feetToMeters(2.86), Rotation2d.fromDegrees(-38.346))
        ), 2.0, 1.0, 0.0, 0.0
    );

    public static Trajectory fiveCargoRightTarmac_getSecondBall = generateTrajectory(
        true, 
        List.of(
            new Pose2d(Units.feetToMeters(24.032), Units.feetToMeters(2.86), Rotation2d.fromDegrees(145.199)),
            new Pose2d(Units.feetToMeters(18.209), Units.feetToMeters(5.78), Rotation2d.fromDegrees(149.962))
        ), 1.0, 0.5, 0.0, 0.0
    );

    public static Trajectory fiveCargoRightTarmac_advanceToHumanPlayerStation = generateTrajectory(
        true, 
        List.of(
            new Pose2d(Units.feetToMeters(18.209), Units.feetToMeters(5.78), Rotation2d.fromDegrees(-173.498)),
            new Pose2d(Units.feetToMeters(5.355), Units.feetToMeters(4.39), Rotation2d.fromDegrees(-175.333))
        ), 1.0, 0.5, 0.0, 0.0
    );

    public static Trajectory fiveCargoRightTarmac_returnFromHumanPlayerStation = generateTrajectory(
        true, 
        List.of(
            new Pose2d(Units.feetToMeters(5.355), Units.feetToMeters(4.39), Rotation2d.fromDegrees(12.743)),
            new Pose2d(Units.feetToMeters(16.923), Units.feetToMeters(6.802), Rotation2d.fromDegrees(10.874))
        ), 1.0, 0.5, 0.0, 0.0
    );

    public static Trajectory generateTrajectory(boolean clampedCubic, List<Pose2d> points, double maxVel, double maxAccel, double startVel, double endVel, TrajectoryConstraint... constraints) {
        TrajectoryConfig config = new TrajectoryConfig(maxVel, maxAccel);
        for (TrajectoryConstraint c : constraints) {
            config.addConstraint(c);
        }
        config.setStartVelocity(startVel);
        config.setEndVelocity(endVel);
        if (!clampedCubic) {
            return TrajectoryGenerator.generateTrajectory(points.stream().map(point -> new Pose2d(point.getTranslation(), point.getRotation())).collect(Collectors.toList()), config);
        } else {
            List<Translation2d> interiorPoints = new ArrayList<Translation2d>();
            for (int i=1; i<points.size()-1; i++) {
                interiorPoints.add(points.get(i).getTranslation());
            }
            return TrajectoryGenerator.generateTrajectory(points.get(0), interiorPoints, points.get(points.size()-1), config);
        }
    }
    
}
