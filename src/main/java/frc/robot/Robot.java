package frc.robot;

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

public class Robot extends TimedRobot {

  ShiftingWestCoast drive;
  DriverStation ds;
  Arm arm;
  Cargo cargo;
  Hatch hatch;
  Thread m_visionThread;
  Command test;
  Timer t;

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
        drive::resetEnc, // function to reset your encoders to 0
        0.6// kP value for P loop
    );

    EasyPath.configure(config);
  }

  @Override
  public void robotPeriodic() {
    System.out.println("left:");
    System.out.println(drive.leftEncInches());
    System.out.println("right:");
    System.out.println(drive.rightEncInches());
    System.out.println("gyro:");
    System.out.println(drive.getAngle());
    // Timer.delay(0.1);
  }

  @Override
  public void autonomousInit() {
    test = new FollowPath(new Path(t ->
    /*
     * {"start":{"x":64,"y":215},"mid1":{"x":174,"y":223},"mid2":{"x":176,"y":171},
     * "end":{"x":213,"y":176}}
     */
    (351 * Math.pow(t, 2) + -360 * t + 24) / (429 * Math.pow(t, 2) + -648 * t + 330), 158.05), x -> {
      return 0.6;
      // if (x < 0.15) return 0.6;
      // else if (x < 0.75) return 0.8;
      // else return 0.25;
    });

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
  public void testPeriodic() {
  }

  public void teleopControl() {
    driveTrainControl();
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

  // public void hatchControl() {
  // TODO add hatch class and control for it here
  // Recommend:
  // hold button = Hatch extend out, claw close
  // release button = hatch retract, claw open
}
