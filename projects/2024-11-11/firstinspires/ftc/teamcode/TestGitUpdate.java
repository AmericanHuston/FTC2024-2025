package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class TestGitUpdate extends LinearOpMode {
    private DcMotor SliderRight;
    private DcMotor frontLeftMotor;
    private DcMotor SliderLeft;
    
    public void runOpMode() {
        SliderLeft = hardwareMap.get(DcMotor.class, "SliderLeft");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
        SliderRight = hardwareMap.get(DcMotor.class,"SliderRight");
        float triggerPos;
        //Don't edit code below this point
        //Don't edit code above this point
        waitForStart();
        while (opModeIsActive()) {
            double power = -0.35;
            while (this.gamepad1.y == true){
                SliderRight.setPower(power);
                SliderLeft.setPower(-power);
            }
            SliderLeft.setPower(0.0);
            SliderRight.setPower(0.0);
            while (this.gamepad1.a == true){
                SliderRight.setPower(-power);
                SliderLeft.setPower(power);
            }
            SliderLeft.setPower(0.0);
            SliderRight.setPower(0.0);
        }
    }
}