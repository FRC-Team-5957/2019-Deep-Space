package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Constants;
import frc.robot.RobotMap;
import frc.robot.Utils;

public class ShiftingWestCoast extends Subsystem {

  WPI_VictorSPX leftMaster, leftSlave, rightMaster, rightSlave; // WPI version so it can be used with differential drive
  DifferentialDrive drive;
  DoubleSolenoid shifter;
  Encoder leftEnc, rightEnc;
  public AHRS navx;

  double distpert = 2500 / 36;

  /*
   * High Gear: TODO: find this Low Gear: TODO: find this
   */

  public ShiftingWestCoast() {
    initDrive();
    initShift();
    navx = new AHRS(SPI.Port.kMXP);
    navx.reset();
  }

  private void initDrive() {
    leftMaster = new WPI_VictorSPX(RobotMap.DRIVE_LEFT_MASTER);
    leftSlave = new WPI_VictorSPX(RobotMap.DRIVE_LEFT_SLAVE);
    rightMaster = new WPI_VictorSPX(RobotMap.DRIVE_RIGHT_MASTER);
    rightSlave = new WPI_VictorSPX(RobotMap.DRIVE_RIGHT_SLAVE);

    leftEnc = new Encoder(0, 1);
    rightEnc = new Encoder(2, 3);

    // leftEnc.setDistancePerPulse(distpert);
    // rightEnc.setDistancePerPulse(distpert);

    resetMotors();

    leftSlave.follow(leftMaster);
    rightSlave.follow(rightMaster);

    leftMaster.setNeutralMode(NeutralMode.Brake);
    rightMaster.setNeutralMode(NeutralMode.Brake);

    drive = new DifferentialDrive(leftMaster, rightMaster);
    drive.setMaxOutput(Constants.DRIVE_MAX_LOW);
  }

  private void resetMotors() {
    Utils.resetWPIVictor(leftMaster);
    Utils.resetWPIVictor(leftSlave);
    Utils.resetWPIVictor(rightMaster);
    Utils.resetWPIVictor(rightSlave);
  }

  private void initShift() {
    shifter = new DoubleSolenoid(RobotMap.PCM_ID, RobotMap.DRIVE_SHIFTER_A, RobotMap.DRIVE_SHIFTER_B);
    shifter.set(Value.kForward); // Setting default state TODO: set this to low gear;
  }

  public void shift(boolean highGear) {
    if (highGear) {
      shifter.set(Value.kForward);
    } else {
      shifter.set(Value.kReverse);
    }
  }

  public void drive(DriveMode mode, double speedInput, double turnInput, int sensitivityLevel) {
    double speed = speedInput * Constants.DRIVE_SPEED;
    double rotation = turnInput * Constants.TURN_SPEED;
    switch (mode) {
    case kArcade:
      drive.arcadeDrive(speed, rotation);
      break;
    case kCurve:
      drive.curvatureDrive(speed, rotation, true);
      break;
    }
  }

  private double desensitize(double input, int level) {
    return Math.pow(Math.abs(input), level - 1) * input;
  }

  public enum DriveMode {
    kArcade, kCurve;
  }

  @Override
  public void initDefaultCommand() {
  }

  public void motorControlEP(double left, double right) {
    drive.tankDrive(left, right);
  }

  public double getAngle() {
    return navx.getYaw();
  }

  public void resetEnc() {
    leftEnc.reset();
    rightEnc.reset();
  }

  public double leftEncInches() {
    return leftEnc.getDistance() / distpert;
  }

  public double rightEncInches() {
    return rightEnc.getDistance() / distpert;
  }
}
