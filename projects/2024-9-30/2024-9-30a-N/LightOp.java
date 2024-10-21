package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp

public class LightOp extends LinearOpMode {
    private Blinker control_Hub;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private IMU imu;
    private LED led0;
    private Servo servo0;
    @TeleOp(name="LightOp", group="Linear OpMode")
    
    public void runOpMode(){
        while (runOpMode()){
        led0.on();
        }
    }
}