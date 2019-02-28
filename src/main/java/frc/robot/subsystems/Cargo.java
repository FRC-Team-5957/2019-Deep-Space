/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Constants;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Cargo extends Subsystem {

  WPI_VictorSPX cargoLeft, cargoRight;
  DifferentialDrive cargo;

  public Cargo() {
    
  }

  private void initMotors() {
    cargoLeft = new WPI_VictorSPX(RobotMap.CARGO_LEFT);
    cargoRight = new WPI_VictorSPX(RobotMap.CARGO_RIGHT);

    cargo = new DifferentialDrive(cargoLeft, cargoRight);
  }
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public void intake() {
    cargo.arcadeDrive(Constants.CARGO_INTAKE_SPEED, 0);
  }

  public void shoot() {
    cargo.arcadeDrive(Constants.CARGO_SHOOT_SPEED, 0);
  }

  public void restPos() {
    cargo.arcadeDrive(0, 0);
  }
}
