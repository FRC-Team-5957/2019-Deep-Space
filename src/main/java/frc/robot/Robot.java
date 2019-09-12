package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import easypath.EasyPath;
import easypath.EasyPathConfig;
import easypath.FollowPath;
import easypath.Path;
import easypath.PathUtil;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.controls.Controls;
import frc.robot.controls.DriverStation;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.Mode;
import frc.robot.subsystems.Arm.Position;
import frc.robot.subsystems.Cargo;
import frc.robot.subsystems.Hatch;
import frc.robot.subsystems.ShiftingWestCoast;
import frc.robot.subsystems.ShiftingWestCoast.DriveMode;
import frc.robot.pathstuff.Paths;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Robot extends TimedRobot {

 

  ShiftingWestCoast drive;
  DriverStation ds;
  Arm arm;
  Cargo cargo;
  Hatch hatch;
  Thread m_visionThread;
  Command test;
  Timer t;
  double heading;
  double offset;

  private boolean m_LimelightHasValidTarget = false;
  private double m_LimelightDriveCommand = 0.0;
  private double m_LimelightSteerCommand = 0.0;

  @Override
  public void robotInit() {
    drive = new ShiftingWestCoast();
    ds = new DriverStation();
    arm = new Arm();
    cargo = new Cargo();
    hatch = new Hatch();
    t = new Timer();
    // CameraServer.getInstance().startAutomaticCapture("Hatch",0);
    // CameraServer.getInstance().startAutomaticCapture("Cargo", 1);

    EasyPathConfig config = new EasyPathConfig(drive, // the subsystem itself
        drive::motorControlEP, // function to set left/right speeds
        // function to give EasyPath the length driven
        () -> PathUtil.defaultLengthDrivenEstimator(drive::leftEncInches, drive::rightEncInches), drive::getAngle, // function
        drive::resetEncGyro, // function to reset your encoders to 0
        0.09// kP value for P loop
    );

    EasyPath.configure(config);
  }

  @Override
  public void robotPeriodic() {
    // System.out.println(tx);

    // NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    // NetworkTableEntry tx = table.getEntry("tx");
    // NetworkTableEntry ty = table.getEntry("ty");
    // NetworkTableEntry ta = table.getEntry("ta");
  
    // double x = tx.getDouble(0.0);
    // double y = ty.getDouble(0.0);
    // double area = ta.getDouble(0.0);
    
    // SmartDashboard.putNumber("LimelightX", x);
    // SmartDashboard.putNumber("LimelightY", y);
    // SmartDashboard.putNumber("LimelightArea", area);
  }

  @Override
  public void autonomousInit() {
    test = new FollowPath(Paths.PATH1, x -> {
      return 0.6;
      // if (x < 0.15) return 0.6;
      // else if (x < 0.75) return 0.8;
      // else return 0.25;
    });

    drive.shift(false);

    test.start();
    // t.reset();
    // t.start();
    // while (t.get() < 2) {
    // drive.motorControlEP(0.6, 0.6);
    // }
    // t.stop();
    // drive.motorControlEP(0, 0);
  }

  @Override
  public void autonomousPeriodic() {
    // teleopControl();
    // drive.motorControlEP(0.5, 0.5);
    // Timer.delay(5);
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopPeriodic() {
    teleopControl();
  }

  @Override
  public void disabledInit() {
    drive.setCoast();
  }

  @Override
  public void testPeriodic() {
  }

  public void teleopControl() {
    adjust();
    armControl(Mode.MANUAL);
    cargoControl();
    hatchControl();
  }

  public void driveTrainControl() {
    double speedInput = ds.getLowGear() ? ds.getGTASpeed() * Constants.DRIVE_SPEED_LOW : ds.getGTASpeed();
    double turnInput = ds.getTurn();
    boolean highGear = ds.getHighGear();
    drive.drive(DriveMode.kCurve, speedInput, turnInput, Controls.SENSITIVITY);
    drive.shift(highGear);
  }

  public void adjust() {

    final double STEER_K = 0.04;                    // how hard to turn toward the target
    final double DRIVE_K = 0.26;                    // how hard to drive fwd toward the target
    final double DESIRED_TARGET_AREA = 13.0;        // Area of the target when the robot reaches the wall
    final double MAX_DRIVE = 0.7;                   // Simple speed limit so we don't drive too fast

    double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0);
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    double ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);

    // if (tv < 1.0)
    // {
    //   m_LimelightHasValidTarget = false;
    //   m_LimelightDriveCommand = 0.0;
    //   m_LimelightSteerCommand = 0.0;
    //   return;
    // }

    //  m_LimelightHasValidTarget = true;

    // Start with proportional steering
    double steer_cmd = tx * STEER_K;
    m_LimelightSteerCommand = steer_cmd;
   
    // NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    // NetworkTableEntry tx = table.getEntry("tx");
    // NetworkTableEntry ty = table.getEntry("ty");
    // NetworkTableEntry ta = table.getEntry("ta");
    
    // double x = tx.getDouble(0.0);
    // double y = ty.getDouble(0.0);
    // double area = ta.getDouble(0.0);
      
    SmartDashboard.putNumber("LimelightX", tx);
    SmartDashboard.putNumber("LimelightY", ty);
    SmartDashboard.putNumber("LimelightArea", ta);
    

    double speedInput = ds.getLowGear() ? ds.getGTASpeed() * Constants.DRIVE_SPEED_LOW : ds.getGTASpeed();
    double turnInput = ds.getTurn();
    boolean highGear = ds.getHighGear();
    drive.shift(highGear);

    final double p = 0.035;
    if (ds.getHeadingButton() == true) {
      heading = drive.getAngle();
    }

    // if (ds.getLimelightButton() == true) {
    //   offset = tx;
    // }

    if (ds.getHeadingHeld() == true) {
      drive.drive(DriveMode.kCurve, speedInput, (p * -(heading - drive.getAngle())), Controls.SENSITIVITY);
    } else if (ds.getLimelightHeld() == true) {
      drive.drive(DriveMode.kCurve, speedInput, -m_LimelightSteerCommand, Controls.SENSITIVITY);
    }else {
      drive.drive(DriveMode.kCurve, speedInput, turnInput, Controls.SENSITIVITY);
    }

  }

  public void armControl(Mode m) {
    if (m == Mode.MAGIC)

    { // Motion Magic
      Position p = Position.STOWED; // Default state
      boolean waitForShoot = false;
      double intakeSpeed = Constants.CARGO_HOLDING_SPEED; // TODO: set intake to brake mode so it holds the ball without
                                                          // a constant infeed

      if (ds.getCargoGathering()) {
        p = Position.PICKUP;
        intakeSpeed = Constants.CARGO_INTAKE_SPEED;
        waitForShoot = false;
        // TODO set intake wheels to intake here
      } else if (ds.getCargoRocketLevel()) {
        p = Position.ROCKET;
        intakeSpeed = Constants.CARGO_SHOOT_SPEED;
        waitForShoot = true;
      } else if (ds.getCargoShipLevel()) {
        p = Position.CARGO_SHIP;
        intakeSpeed = Constants.CARGO_SHOOT_SPEED;
        waitForShoot = true;
      }

      arm.operate(p);
      if (waitForShoot) {
        if (ds.shootCargo()) {
          // set intake to intakeSpeed if DriverStation.shootCargo()
        }
      } else {
        // just set intake to intakeSpeed
      }
    } else if (m == Mode.MANUAL) {
      arm.manualOperate(ds.getArmControl() * 0.4

      );
      // implement something for manual control ONLY while tuning the PIDF values. DO
      // NOT
      // use manual control in a match unless something breaks. USE MOTON MAGIC
    }
  }

  public void cargoControl() {
    if (ds.getCargoGathering()) {
      cargo.intake();
    } else if (ds.shootCargo()) {
      cargo.shoot();
    } else {
      cargo.restPos();
    }
  }

  public void hatchControl() {

    if (ds.getGripButton() == true) {
      hatch.gripClose();
    } else {
      hatch.gripOpen();
    }

    if (ds.getExtendButton() == true) {
      hatch.hatchOut();
    } else if (ds.getRetractButton() == true) {
      hatch.hatchIn();
    }
  }

  public void cameraInit() {
    m_visionThread = new Thread(() -> {
      UsbCamera hatchcamera = CameraServer.getInstance().startAutomaticCapture("Hatch", 0);
      UsbCamera cargocamera = CameraServer.getInstance().startAutomaticCapture("Cargo", 1);

      hatchcamera.setResolution(320, 240);
      cargocamera.setResolution(320, 240);

      hatchcamera.setFPS(30);
      cargocamera.setFPS(30);

    });
    m_visionThread.setDaemon(true);
    m_visionThread.start();
  }

  

}
