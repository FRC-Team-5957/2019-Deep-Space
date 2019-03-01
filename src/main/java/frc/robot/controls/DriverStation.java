package frc.robot.controls;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotMap;

public class DriverStation {



    Joystick driver;
    Joystick operator;

    public DriverStation() {
        driver = new Joystick(RobotMap.DRIVER_USB);
        operator = new Joystick(RobotMap.OPERATOR_USB);
    }

    // DriveTrain
    // ----------------------------------------------------------------------------------
    public double getGTASpeed() {
        // TODO: change this so right trigger moves forward and left trigger moves in
        // reverse
        return getAxis(Controls.GTA_DECELL, driver) - getAxis(Controls.GTA_ACCEL, driver);
    }

    public double getSpeed() {
        return driver.getRawAxis(Controls.DRIVE_SPEED_AXIS);
    }

    public double getTurn() {
        return -driver.getRawAxis(Controls.DRIVE_TURN_AXIS);
    }

    public boolean getHighGear() {
        return driver.getRawButton(Controls.DRIVE_HIGHGEAR);
    }

    // Cargo
    // ----------------------------------------------------------------------------------
    public boolean getCargoGathering() {
        return getButton(Controls.CARGO_GATHERING, operator);
    }

    public boolean getCargoRocketLevel() {
        return getButton(Controls.CARGO_ROCKET, operator);
    }

    public boolean getCargoShipLevel() {
        return getButton(Controls.CARGO_SHIP, operator);
    }

    public boolean shootCargo() {
        return getButton(Controls.CARGO_SHOOT, operator); // Intentionally gave the shoot command to the driver
    }

    public double getArmControl() {
        return getAxis(Controls.ARM_CONTROL, operator);
    }

    //Hatch

    public boolean getGripButton() {
        return operator.getRawButton(Controls.HATCH_GATHERING);
    }

    public boolean getExtendButton() {
        return getButton(Controls.HATCH_EXTEND, operator);
    }

    public boolean getRetractButton() {
        return getButton(Controls.HATCH_RETRACT, operator);
    }
    

    // Utils
    // ----------------------------------------------------------------------------------
    private boolean getButtonPressed(int button, Joystick j) {
        return j.getRawButtonPressed(button);
    }

    private boolean getButtonReleased(int button, Joystick j) {
        return j.getRawButtonReleased(button);
    }

    private boolean getButton(int button, Joystick j) {
        return j.getRawButton(button);
    }

    private double getAxis(int axis, Joystick j) {
        return Math.abs(j.getRawAxis(axis)) < 0.05 ? 0 : j.getRawAxis(axis);
    }

    private boolean getAxisPressed(int axis, Joystick j) {
        return j.getRawAxis(axis) != 0;
    }

    
}