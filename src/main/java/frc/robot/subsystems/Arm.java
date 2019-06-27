package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.Utils;

public class Arm extends Subsystem {

  TalonSRX armMaster, armSlave;

  public Arm() {
    initMotors();
  }

  private void initMotors() {
    armMaster = new TalonSRX(RobotMap.ARM_MASTER);
    armSlave = new TalonSRX(RobotMap.ARM_SLAVE);

    resetMotors();

    armSlave.follow(armMaster);

    armMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.ARM_PROFILE_ID,
        Constants.ARM_TIMEOUT_MS);

    armMaster.configNominalOutputForward(0, Constants.ARM_TIMEOUT_MS);
    armMaster.configNominalOutputReverse(0, Constants.ARM_TIMEOUT_MS);
    armMaster.configPeakOutputForward(1, Constants.ARM_TIMEOUT_MS);
    armMaster.configPeakOutputReverse(-1, Constants.ARM_TIMEOUT_MS);

    armMaster.config_kF(Constants.ARM_PROFILE_ID, Constants.ARM_F);
    armMaster.config_kP(Constants.ARM_PROFILE_ID, Constants.ARM_P);
    armMaster.config_kD(Constants.ARM_PROFILE_ID, Constants.ARM_D);
    armMaster.config_kI(Constants.ARM_PROFILE_ID, Constants.ARM_D);

    armMaster.configMotionCruiseVelocity(Constants.ARM_CRUISE_VEL, Constants.ARM_TIMEOUT_MS);
    armMaster.configMotionAcceleration(Constants.ARM_ACCEL, Constants.ARM_TIMEOUT_MS);

    // sets current position to 0
    armMaster.setSelectedSensorPosition(0, Constants.ARM_PROFILE_ID, Constants.ARM_TIMEOUT_MS);
  }

  private void resetMotors() {
    Utils.resetTalon(armMaster);
    Utils.resetTalon(armSlave);

  }

  public void manualOperate(double speed) {
    armMaster.set(ControlMode.PercentOutput, speed);
    // armMaster.getSelectedSensorPosition(pidIdx);
    // System.out.println(armMaster.getSelectedSensorVelocity());
  }

  public void operate(Position p) {
    armMaster.set(ControlMode.MotionMagic, p.getPosition());
  }

  public enum Position {
    STOWED(Constants.ARM_STOWED), PICKUP(Constants.ARM_PICKUP), ROCKET(Constants.ARM_ROCKET),
    ROCKET2(Constants.ARM_ROCKET2), CARGO_SHIP(Constants.ARM_CARGOSHIP);
    private int encoderPosition;

    private Position(final int position) {
      encoderPosition = position;
    }

    public int getPosition() {
      return encoderPosition;
    }
  }

  public enum Mode {
    MANUAL, MAGIC;
  }

  @Override
  public void initDefaultCommand() {
  }
}
