package org.usfirst.frc.team4322.practice_robot;

import edu.wpi.first.wpilibj.*;

public class RangeFinder extends SensorBase
{
    private final double IN_TO_CM_CONVERSION = 2.54; //Converts reported inches to cm
    private final double IN_TO_FT_CONVERSION = 1 / 12; //Converts reported inches to ft
    private final double MM_TO_IN_CONVERSION = 1 / 25.4; //Converts reported millimeters to inches
    AnalogInput channel;

    /**
     * Constructor for RangeFinder class
     * @param _channel
     */
    public RangeFinder(int _channel)
    {
        channel = new AnalogInput(_channel);
    }

    /* GetVoltage
     * Returns the raw voltage
     */
    double GetVoltage()
    {
        return channel.getVoltage();
    }

    /* GetRangeInInches
     * Gets voltage being supplied to Analog Breakout, then divides by 512
     * Returns the range in inches
     */
    
    double GetRangeInInches()
    {
    	double millimeters = GetRangeInMillimeters();
    	double inches = millimeters * MM_TO_IN_CONVERSION;
    	return inches;
    }
    
    double GetRangeInMillimeters()
    {
        double range; //distance, to be determined
        double analogBreakoutVoltage = DriverStation.getInstance().getBatteryVoltage() * 0.4; //supplied voltage
        double scaling_factor = analogBreakoutVoltage / 5120; //the supplied voltage / vpi

        //determine the raw voltage
        range = channel.getVoltage();
        //divide by scaling factor to get distance
        range = (range / scaling_factor);
        return range;
    }

    double GetBatteryVoltage()
    {
        return (DriverStation.getInstance().getBatteryVoltage() * .4);
    }

    /* GetRangeInFeet
     * Returns the range feet
     */
    double GetRangeInFT()
    {
        double range;
        range = GetRangeInInches();
        range = range * IN_TO_FT_CONVERSION;
        return range;
    }

    /* GetRangeInCentimeters
     * Returns the range in centimeters
     */
    double GetRangeInCM()
    {
        double range;
        range = GetRangeInInches();
        range = range * IN_TO_CM_CONVERSION;
        return range;
    }
}
