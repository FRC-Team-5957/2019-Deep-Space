/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Hatch extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  Solenoid hatchGrip, hatchExtend;


  public Hatch() {
    initSolenoid();
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  private void initSolenoid() {
    hatchGrip = new Solenoid(RobotMap.PCM_ID, RobotMap.HATCH_GRIPPER);
    hatchExtend = new Solenoid(RobotMap.PCM_ID, RobotMap.HATCH_EXTEND);

    hatchGrip.set(true);
    hatchExtend.set(false);
  }

  public void gripOpen() {
    hatchGrip.set(true);
  }

  public void gripClose() {
    hatchGrip.set(false);
  }

  public void hatchOut() {
    hatchExtend.set(true);
  }

  public void hatchIn() {
    hatchExtend.set(false);
  }
}
