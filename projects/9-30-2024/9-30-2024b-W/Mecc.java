// package org.firstinspires.ftc.teamcode;
// import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
// import com.qualcomm.robotcore.hardware.Servo;
// import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
// import com.qualcomm.robotcore.hardware.DcMotor;
// import com.qualcomm.robotcore.hardware.DcMotorSimple;


// @TeleOp(name="Mecanum TeleOp", group="Linear OpMode")
// public class Mecc extends LinearOpMode {

//     private DcMotor frontLeftMotor;
//     private DcMotor frontRightMotor;
//     private DcMotor backLeftMotor;
//     private DcMotor backRightMotor;
//     private Servo servo0;


//     @Override
//     public void runOpMode() {

//         // Initialize motors
//         frontLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");
//         frontRightMotor = hardwareMap.get(DcMotor.class, "backRight");
//         backLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
//         backRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
//         servo0 = hardwareMap.get(Servo.class, "servo0");
       
        
//         // Reverse motor directions as needed
//         frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//         backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//         waitForStart();
            
//             while (opModeIsActive()) {
//                 // Get joystick values
//                 double lt = gamepad1.left_trigger;
//                 double y = gamepad1.left_stick_y/2; 
//                 double x = gamepad1.left_stick_x/2; 
//                 double rx = gamepad1.right_stick_x/2; 
//                 if (gamepad1.right_trigger >= 0.01){
//                     y = y*2;
//                     x = x*2;
//                     rx = rx*2;
//                 }
                
//                 // Calculate motor powers
//                 double frontLeftPower = y + x + rx;
//                 double frontRightPower = y - x - rx;
//                 double backLeftPower = y - x + rx;
//                 double backRightPower = y + x - rx;
//                 // Set motor powers
//                 frontLeftMotor.setPower(frontLeftPower);
//                 frontRightMotor.setPower(frontRightPower);
//                 backLeftMotor.setPower(backLeftPower);
//                 backRightMotor.setPower(backRightPower);
                
//                 telemetry.addData("ROBOT", "Status:" + "Servo:" + servo0.getPosition());
//                 telemetry.addData("ROBOT", "Status:" + "Front Right =" + Math.round(frontRightPower * 100.0)/100.0);
//                 telemetry.addData("ROBOT", "Status:" + "Front Left =" + Math.round(frontLeftPower * 100.0)/100.0);//Echos information using classification.
//                 telemetry.addData("ROBOT", "Status:" + "Back Left =" + Math.round(backLeftPower * 100.0)/100.0);
//                 telemetry.addData("ROBOT", "Status:" + "Back Right =" + Math.round(backRightPower * 100.0)/100.0);
//                 telemetry.update(); //updates the console
                
//                 double tgtPower = 0.0;
//                 boolean rampUp = false;
//                 if (this.gamepad1.b && rampUp) {
//                     // Keep stepping up until we hit the max value.
//                     position += INCREMENT ;
//                     if (position >= MAX_POS ) {
//                         position = MAX_POS;
//                         rampUp = !rampUp;   // Switch ramp direction
//                     }
//                 }
//                 else {
//                     // Keep stepping down until we hit the min value.
//                     position -= INCREMENT ;
//                     if (position <= MIN_POS ) {
//                         position = MIN_POS;
//                         rampUp = !rampUp;  // Switch ramp direction
//                     }
//                 }
//                 // Display the current value
//                 telemetry.addData("Servo Position", "%5.2f", position);
//                 telemetry.addData(">", "Press Stop to end test." );
//                 telemetry.update();
//                 // Set the servo to the new position and pause;
//                 servo0.setPosition(position);
//                 sleep(CYCLE_MS);
//                 idle();
//                 telemetry.addData(">", "Done");
//                 telemetry.update();
//                 telemetry.addData("Servo Position", servo0.getPosition());
//                 telemetry.addData("Target Power", tgtPower);
//                 telemetry.addData("Motor Power", servo0.getDirection());
//                 telemetry.addData("Status", "Running");
//                 telemetry.update();
//             }
//         }
//     }
// }