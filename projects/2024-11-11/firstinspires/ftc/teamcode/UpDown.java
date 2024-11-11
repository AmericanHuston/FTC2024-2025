package org.firstinspires.ftc.teamcode;

//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
@Disabled
@TeleOp(name="UpDown", group="TeleOp")
public class UpDown extends LinearOpMode {
    private DcMotor SliderLeft;
    private DcMotor SliderRight;
    @Override
    public void runOpMode()  {
        SliderLeft = hardwareMap.get(DcMotor.class, "SliderLeft");
        SliderRight = hardwareMap.get(DcMotor.class, "SliderRight");
        //FtcDashboard dashboard = FtcDashboard.getInstance();
        //telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
        //dashboard.updateConfig();
        waitForStart();
        double power = 0.35;
        while(opModeIsActive()){
            while (this.gamepad1.dpad_up){
                SliderLeft.setPower(power);
                SliderRight.setPower(-power);
            }
            SliderRight.setPower(0.0);
            SliderLeft.setPower(0.0);
            while (this.gamepad1.dpad_down){
                SliderLeft.setPower(-power);
                SliderRight.setPower(power);
            }
            SliderRight.setPower(0.0);
            SliderLeft.setPower(0.0);
        }
    }
}
