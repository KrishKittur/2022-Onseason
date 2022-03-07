package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.SensorVelocityMeasPeriod;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAlternateEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commons.BreadUtil;
import frc.robot.drivers.LazyCANSparkMax;
import frc.robot.drivers.TalonFXFactory;

import static frc.robot.Constants.Flywheel.*;
import static frc.robot.Constants.Hood.*;

public class Shooter extends SubsystemBase {

    // Flywheel Hardware 
    private final TalonFX leftFlywheelMotor = TalonFXFactory.createDefaultTalon(LEFT_MOTOR_ID);
    private final TalonFX rightFlywheelMotor = TalonFXFactory.createDefaultTalon(RIGHT_MOTOR_ID);

    // Hood hardware
    private final CANSparkMax hoodMotor = new LazyCANSparkMax(HOOD_MOTOR_ID, MotorType.kBrushless);
    private final RelativeEncoder hoodEncoder = hoodMotor.getAlternateEncoder(SparkMaxAlternateEncoder.Type.kQuadrature, 8192);
    private final SparkMaxPIDController hoodPID = hoodMotor.getPIDController();

    // State logic
    private ShooterState systemState = ShooterState.HOMING;

    // State variables
    private boolean homing = true;
    private boolean requestShoot = false;
    private double hoodSetpoint = 0.0;
    private double flywheelSetpoint = 0.0;

    public Shooter() {
        // Configure left flywheel motor
        TalonFXConfiguration leftMotorConfig = new TalonFXConfiguration();
        leftMotorConfig.slot0.kP = integratedSensorUnitsToFlywheelRPM(0.0015) * 1023.0;
        leftMotorConfig.slot0.kI = integratedSensorUnitsToFlywheelRPM(0.0) * 1023.0;
        leftMotorConfig.slot0.kD = integratedSensorUnitsToFlywheelRPM(0.0) * 1023.0;
        leftMotorConfig.slot0.kF = 0.0;
        leftMotorConfig.slot0.closedLoopPeakOutput = 1.0;
        leftMotorConfig.peakOutputForward = 1.0;
        leftMotorConfig.peakOutputReverse = 0.0;
        leftMotorConfig.velocityMeasurementPeriod = SensorVelocityMeasPeriod.Period_10Ms;
        leftMotorConfig.velocityMeasurementWindow = 16;
        leftMotorConfig.voltageCompSaturation = 11.5;
        leftMotorConfig.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 80, 80, 1.5);
        leftFlywheelMotor.configAllSettings(leftMotorConfig);
        leftFlywheelMotor.selectProfileSlot(0, 0);
        leftFlywheelMotor.setInverted(LEFT_MOTOR_DIRECTION);
        leftFlywheelMotor.enableVoltageCompensation(true);
        leftFlywheelMotor.setStatusFramePeriod(StatusFrame.Status_1_General, 100);
        leftFlywheelMotor.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 100);

        // Configure right flywheel motor
        TalonFXConfiguration rightMotorConfig = new TalonFXConfiguration();
        rightMotorConfig.peakOutputForward = 1.0;
        rightMotorConfig.peakOutputReverse = -0.0;
        rightMotorConfig.velocityMeasurementPeriod = SensorVelocityMeasPeriod.Period_2Ms;
        rightMotorConfig.velocityMeasurementWindow = 8;
        rightMotorConfig.voltageCompSaturation = 11.0;
        rightMotorConfig.supplyCurrLimit = new SupplyCurrentLimitConfiguration(true, 80, 80, 1.5);
        rightFlywheelMotor.configAllSettings(rightMotorConfig);
        rightFlywheelMotor.setInverted(RIGHT_MOTOR_DIRECTION);
        rightFlywheelMotor.follow(leftFlywheelMotor);
        rightFlywheelMotor.enableVoltageCompensation(true);
        rightFlywheelMotor.setStatusFramePeriod(StatusFrame.Status_1_General, 100);
        rightFlywheelMotor.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 100);

        // Configure hood motor 
        hoodEncoder.setPositionConversionFactor(HOOD_GEARING * 360.0);
        hoodPID.setFeedbackDevice(hoodEncoder);
        hoodPID.setP(0.4);
        hoodPID.setI(0.0);
        hoodPID.setD(0.0);
        hoodPID.setOutputRange(-1, 1);
        setHoodCurrentLimits(30, 40.0);
    }

    // Private method to set the hood setpoint
    private void setHoodSetpoint(double degrees) {
        double adjustedSetpoint = MathUtil.clamp(degrees, 1.0, 35.0);
        hoodPID.setReference(adjustedSetpoint, CANSparkMax.ControlType.kPosition);
    }

    // Private method to set the hood percent
    private void setHoodPercent(double percent) {
        hoodMotor.set(percent);
    }

    // Private method to set the flywheel setpoint
    private void setFlywheelSetpoint(double rpm) {
        if (rpm==0.0) {
            leftFlywheelMotor.set(ControlMode.PercentOutput, 0.0);
        } else {
            leftFlywheelMotor.set(ControlMode.Velocity, flywheelRPMToIntegratedSensorUnits(rpm), DemandType.ArbitraryFeedForward, (rpm+261.0)/6163.0);
        }
    }

    // Private method to set the hood current limits
    private void setHoodCurrentLimits(int smart, double secondary) {
        hoodMotor.setSmartCurrentLimit(smart);
        hoodMotor.setSecondaryCurrentLimit(secondary);
    }

    // Converts integrated sensor units to flyweel rpm
    private double integratedSensorUnitsToFlywheelRPM(double integratedSensorUnits) {
        return integratedSensorUnits * (600/2048.0);
    }

    // Converts flywheel rpm to integrated sensor units
    private double flywheelRPMToIntegratedSensorUnits(double flywheelRPM) {
        return flywheelRPM * (2048.0/600);
    }

    // Requests the shooter to home
    public void requestHome() {
        homing = true;
    }

    // Requests the shooter rpm and hood angle
    public void requestShoot(double flywheelRPM, double hoodAngle) {
        if (homing) {
            // Do nothing because the shooter is homing
        } else if (flywheelRPM == 0.0) {
            requestIdle(); // Request idle because the flywheel rpm is 0.0
        } else {
            requestShoot = true;
            this.flywheelSetpoint = flywheelRPM;
            this.hoodSetpoint = hoodAngle;
        }

    }

    // Requests the shooter to go into idle
    public void requestIdle() {
        requestShoot = false;
    }

    // Returns the current state of the shooter
    public ShooterState getSystemState() {
        return systemState;
    }

    // Returns the hood position
    public double getHoodPosition() {
        return hoodEncoder.getPosition();
    }

    // Returns the flywheel velocity
    public double getFlywheelVelocity() {
        return leftFlywheelMotor.getSelectedSensorVelocity();
    }

    // Returns the hood setpoint
    public double getHoodSetpoint() {
        return hoodSetpoint;
    }

    // Returns the flywheel setpoint
    public double getFlywheelSetpoint() {
        return flywheelSetpoint;
    }

    // Returns whether the hood is at its setpoint
    public boolean hoodAtSetpoint() {
        return BreadUtil.atReference(getHoodPosition(), getHoodSetpoint(), 1.0, true);
    }

    // Returns whether the flywheel is at its setpoint
    public boolean flywheelAtSetpoint() {
        return BreadUtil.atReference(getFlywheelVelocity(), getFlywheelSetpoint(), 100.0, true);
    }

    // Shooter States
    public enum ShooterState {
        HOMING,
        IDLE, 
        APPROACHING_SETPOINT, 
        AT_SETPOINT
    }
    
    // Handle statemachine in periodic
    @Override
    public void periodic() {
        ShooterState nextState = null;
        if (systemState == ShooterState.HOMING) {

        } else if (systemState == ShooterState.IDLE) {

        } else if (systemState == ShooterState.APPROACHING_SETPOINT) {
            // Outputs
            setFlywheelSetpoint(flywheelSetpoint);
            setHoodSetpoint(hoodSetpoint);
            
            // State transitions 
            if (flywheelAtSetpoint() && hoodAtSetpoint()) {
                nextState = ShooterState.AT_SETPOINT;
            } else if (homing) {
                nextState = ShooterState.HOMING;
                // TODO
            }
        } else if (systemState == ShooterState.AT_SETPOINT) {
            // Outputs
            setFlywheelSetpoint(flywheelSetpoint);
            setHoodSetpoint(hoodSetpoint);

            // State transitions
            if (!flywheelAtSetpoint() && !hoodAtSetpoint()) {
                nextState = ShooterState.APPROACHING_SETPOINT;
            } else if (homing) {
                nextState = ShooterState.HOMING;
            }
        }
        SmartDashboard.putString("Shooter State", systemState.name());
    }
     
}
