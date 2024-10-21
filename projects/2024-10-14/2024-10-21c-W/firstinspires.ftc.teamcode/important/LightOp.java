package org.firstinspires.ftc.teamcode.important;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
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
    private LED led0green;
    private LED led0red;
    private Servo servo0;
    
    @TeleOp(name="LightOp", group="Linear OpMode")
    
    public void runOpMode(){
        led0green = hardwareMap.get(LED.class, "led0green");
        led0red = hardwareMap.get(LED.class, "led0red");
        waitForStart();
        while (this.gamepad1.a == false){
            if (this.gamepad1.left_bumper == false){
                led0green.on();
                led0red.off();
                telemetry.addData("ROBOT", "Status:" + "LED Red:" + led0red.isLightOn());
                telemetry.addData("ROBOT", "Status:" + "LED Green:" + led0green.isLightOn());
                telemetry.update();
            }else{
                led0green.off();
                led0red.on();
                telemetry.addData("ROBOT", "Status:" + "LED Red:" + led0red.isLightOn());
                telemetry.addData("ROBOT", "Status:" + "LED Green:" + led0green.isLightOn());
                telemetry.update();
            }
        }
    }
}
