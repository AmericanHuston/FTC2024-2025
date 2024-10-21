package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name="Mecanum TeleOp", group="Linear OpMode")
public class Mecc extends LinearOpMode {

    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;
    private Servo Grab0;


    @Override
    public void runOpMode() {

        // Initialize motors
        frontLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");
        frontRightMotor = hardwareMap.get(DcMotor.class, "backRight");
        backLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
        backRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
        Grab0 = hardwareMap.get(Servo.class, "Grab0");
       
        

        // Reverse motor directions as needed
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
            
            while (opModeIsActive()) {
                // Get joystick values
                double lt = gamepad1.left_trigger;
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
                
                telemetry.addData("ROBOT", "Status:" + "Servo:" + Grab0.getPosition());
                telemetry.addData("ROBOT", "Status:" + "Front Right =" + Math.round(frontRightPower * 100.0)/100.0);
                telemetry.addData("ROBOT", "Status:" + "Front Left =" + Math.round(frontLeftPower * 100.0)/100.0);//Echos information using classification.
                telemetry.addData("ROBOT", "Status:" + "Back Left =" + Math.round(backLeftPower * 100.0)/100.0);
                telemetry.addData("ROBOT", "Status:" + "Back Right =" + Math.round(backRightPower * 100.0)/100.0);
                telemetry.update(); //updates the console
            }
        double tgtPower = 0;
        while (opModeIsActive()) {
            tgtPower = -this.gamepad1.left_stick_y;
            Grab0.getDirection();
            
            // check to see if we need to move the servo.
            if(gamepad1.y) {
                // move to 0 degrees.
                Grab0.setPosition(0);
            } else if (gamepad1.x || gamepad1.b) {
                // move to 90 degrees.
                Grab0.setPosition(0.5);
            } else if (gamepad1.a) {
                // move to 180 degrees.
                Grab0.setPosition(1);
        }
    telemetry.addData("Servo Position", Grab0.getPosition());
    telemetry.addData("Target Power", tgtPower);
    telemetry.addData("Motor Power", Grab0.getDirection());
    telemetry.addData("Status", "Running");
    telemetry.update();

    


        }  
    }
}