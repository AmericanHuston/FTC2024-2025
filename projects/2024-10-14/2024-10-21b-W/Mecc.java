package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.LED;
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
    private DcMotor ElbowMotor;
    private Servo servo0;
    
    private int ELBOW_MAX = 200;
    private int ELBOW_MIN = -200;
    
    private LED led0green;
    private LED led0red;

    @Override
    public void runOpMode() {
        double INCREMENT = 0.01;
        double MAX_POS = 1.0;
        double MIN_POS = 0.0;
        int CYCLE_MS = 50;
        boolean rampUp = true;
        double position = (MIN_POS - MAX_POS);
        // Initialize motors
        frontLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");
        frontRightMotor = hardwareMap.get(DcMotor.class, "backRight");
        backLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
        backRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
        ElbowMotor = hardwareMap.get(DcMotor.class, "ElbowMotor");
        servo0 = hardwareMap.get(Servo.class, "servo0");
        ElbowMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int zero = 0;
        ElbowMotor.setTargetPosition(zero);
        ElbowMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        led0green = hardwareMap.get(LED.class, "led0green");
        led0red = hardwareMap.get(LED.class, "led0red");
        led0green.on();
        led0red.off();
        
        // Reverse motor directions as needed
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        waitForStart();
            
            while (opModeIsActive())
            {
                // Get joystick values
                double lt = gamepad1.left_trigger;
                double y = gamepad1.left_stick_y/2; 
                double x = gamepad1.left_stick_x/2; 
                double rx = -gamepad1.right_stick_x/2;
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
                
                int elbow = ElbowMotor.getCurrentPosition();
                if (gamepad1.dpad_left && elbow > ELBOW_MIN) {
                    ElbowMotor.setTargetPosition(elbow - 10);
                    ElbowMotor.setPower(0.5);
                }
                if (gamepad1.dpad_right && elbow < ELBOW_MAX) {
                    ElbowMotor.setTargetPosition(elbow + 10);
                    ElbowMotor.setPower(0.5);
                }
                
                if (elbow >= ELBOW_MAX) {
                    led0red.off();
                    led0green.on();
                } else if (elbow <= ELBOW_MIN) {
                    led0red.off();
                    led0green.on();
                } else {
                    led0red.on();
                    led0green.off();
                }
                
                telemetry.addData("ROBOT", "Status:" + "ElbowMotor: GCP" + elbow + " " + ElbowMotor.getCurrentPosition());
                
                telemetry.addData("ROBOT", "Status:" + "Servo:" + servo0.getPosition());
                
                telemetry.addData("ROBOT", "Status:" + "Front Right =" + Math.round(frontRightPower * 100.0)/100.0);
                telemetry.addData("ROBOT", "Status:" + "Front Left =" + Math.round(frontLeftPower * 100.0)/100.0);//Echos information using classification.
                telemetry.addData("ROBOT", "Status:" + "Back Left =" + Math.round(backLeftPower * 100.0)/100.0);
                telemetry.addData("ROBOT", "Status:" + "Back Right =" + Math.round(backRightPower * 100.0)/100.0);
                
                //telemetry.update(); //updates the console
                
                double tgtPower = 0.0;
                if (this.gamepad1.b && rampUp) {
                    // Keep stepping up until we hit the max value.
                    position += INCREMENT ;
                    if (position >= MAX_POS ) {
                        position = MAX_POS;
                        rampUp = !rampUp;   // Switch ramp direction
                    }
                }
                else {
                    // Keep stepping down until we hit the min value.
                    position -= INCREMENT ;
                    if (position <= MIN_POS ) {
                        position = MIN_POS;
                        rampUp = !rampUp;  // Switch ramp direction
                    }
                }
                // Display the current value
                //lemetry.addData("Servo Position", "%5.2f", position);
                //lemetry.addData(">", "Press Stop to end test." );
                //telemetry.update();
                // Set the servo to the new position and pause;
                servo0.setPosition(position); //Tell the servo to go to the correct pos
                sleep(CYCLE_MS);
                idle();
                //lemetry.addData(">", "Done");
                //telemetry.update();
                //lemetry.addData("Servo Position", servo0.getPosition());
                //lemetry.addData("Target Power", tgtPower);
                //lemetry.addData("Motor Power", servo0.getDirection());
                //lemetry.addData("Status", "Running");
                telemetry.update();
        }
    }
}