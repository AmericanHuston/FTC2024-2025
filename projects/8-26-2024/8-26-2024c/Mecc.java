package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="Mecanum TeleOp", group="Linear OpMode")
public class Mecc extends LinearOpMode {

    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;

    @Override
    public void runOpMode() {

        // Initialize motors
        frontLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");
        frontRightMotor = hardwareMap.get(DcMotor.class, "backRight");
        backLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
        backRightMotor = hardwareMap.get(DcMotor.class, "frontRight");

        // Reverse motor directions as needed
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
            
            while (opModeIsActive()) {
                // Get joystick values
                double y = gamepad1.left_stick_y/2; 
                double x = gamepad1.left_stick_x/2; 
                double rx = gamepad1.right_stick_x/2; 
                if (gamepad1.right_trigger >= 0.01){
                    y = y*2;
                    x = x*2;
                    rx = rx*2;
                }
                // Calculate motor powers
                double frontLeftPower = y + x + rx;
                double frontRightPower = y - x - rx;
                double backLeftPower = y - x + rx;
                double backRightPower = y + x - rx;
    
                // Set motor powers
                frontLeftMotor.setPower(frontLeftPower);
                frontRightMotor.setPower(frontRightPower);
                backLeftMotor.setPower(backLeftPower);
                backRightMotor.setPower(backRightPower);
            }
            
            
            
            
        }
    }
