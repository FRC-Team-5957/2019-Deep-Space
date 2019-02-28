package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class Utils {

    public static void resetTalon(TalonSRX talon) {
        talon.configFactoryDefault();
    }

    public static void resetWPITalon(WPI_TalonSRX talon) {
        talon.configFactoryDefault();
    }

    public static void resetVictor(VictorSPX victor) {
        victor.configFactoryDefault();
    }

    public static void resetWPIVictor(WPI_VictorSPX victor) {
        victor.configFactoryDefault();
    }
}