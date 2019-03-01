package frc.robot;

public class Constants {
    public static final double RAMP_RATE = 0.25;

    public static final double DRIVE_MAX_OUTPUT = 0.5;
    public static final double DRIVE_SPEED = 1;
    public static final double TURN_SPEED = 1;

    // TODO: SET THESE PLEASE HERES DOCUMENTATION
    // https://phoenix-documentation.readthedocs.io/en/latest/ch16_ClosedLoop.html?highlight=motion%20magic
    public static final int ARM_TIMEOUT_MS = 1000;
    public static final int ARM_PROFILE_ID = 0;
    public static final double ARM_P = 0.0;
    public static final double ARM_I = 0;
    public static final double ARM_D = 0;
    public static final double ARM_F = 0;
    public static final int ARM_CRUISE_VEL = 2; // encoder ticks per 100ms
    public static final int ARM_ACCEL = 1; // encoder ticks per 100ms per second

    public static final double CARGO_INTAKE_SPEED = -0.1;
    public static final double CARGO_HOLDING_SPEED = 0;
    public static final double CARGO_SHOOT_SPEED = 1.0; // Dont make this 100%, make it just fast enough to go in

    // TODO: SET THESE PLEASE
    public static final int ARM_STOWED = 0;
    public static final int ARM_PICKUP = 1000;
    public static final int ARM_ROCKET = 2000;
    public static final int ARM_ROCKET2 = 3000;
    public static final int ARM_CARGOSHIP = 4000;

}