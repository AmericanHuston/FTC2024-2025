package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name="Coach Nathan", group="Linear OpMode")
public class CoachNathan extends LinearOpMode {

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
    private GoBildaPinpointDriver odo;

    @Override
    public void runOpMode() {
        double INCREMENT = 0.01;
        double MAX_POS = 1.0;
        double MIN_POS = 0.0;
        int CYCLE_MS = 50;
        boolean rampUp = true;
        double position = (MIN_POS - MAX_POS);
        
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
        
        // Initalize Odometry
        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");
        odo.setOffsets(-145.0, -90.0); //fix this
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        //fix this too.
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        odo.resetPosAndIMU();
        
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("frontLeft");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("backLeft");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("frontRight");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("backRight");
        
        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        
        // Retrieve the IMU from the hardware map
        IMU imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameters to match your robot
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);
        
        waitForStart();
        
        if (isStopRequested()) return;
        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y/2; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x/2;
            double rx = gamepad1.right_stick_x/2;
            if (gamepad1.right_trigger >= 0.01){
                y = y*2;
                x = x*2;
                rx = rx*2;
            }
            // This button choice was made so that it is hard to hit on accident,
            // it can be freely changed based on preference.
            // The equivalent button is start on Xbox-style controllers.
            //rev hub facing away if you want this to work.
            if (gamepad1.left_stick_button) {
                imu.resetYaw();
            }

            double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // Rotate the movement direction counter to the bot's rotation
            double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
            double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

            rotX = rotX * 1.1;  // Counteract imperfect strafing

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
            double frontLeftPower = (rotY + rotX + rx) / denominator;
            double backLeftPower = (rotY - rotX + rx) / denominator;
            double frontRightPower = (rotY - rotX - rx) / denominator;
            double backRightPower = (rotY + rotX - rx) / denominator;
            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
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
            
            
            odo.update();
            Pose2D pos = odo.getPosition();
            String data = String.format(Locale.US, "{X: %.3f, Y: %.3f, H: %.3f}", pos.getX(DistanceUnit.MM), pos.getY(DistanceUnit.MM), pos.getHeading(AngleUnit.DEGREES));
            telemetry.addData("Position", data);
            Pose2D vel = odo.getVelocity();
            String velocity = String.format(Locale.US,"{XVel: %.3f, YVel: %.3f, HVel: %.3f}", vel.getX(DistanceUnit.MM), vel.getY(DistanceUnit.MM), vel.getHeading(AngleUnit.DEGREES));
            telemetry.addData("Velocity", velocity);
            
            
            telemetry.addData("ROBOT", "Status:" + "ElbowMotor: GCP" + elbow + " " + ElbowMotor.getCurrentPosition());
            
            telemetry.addData("ROBOT", "Status:" + "Servo:" + servo0.getPosition());
            
            telemetry.addData("ROBOT", "Status:" + "Front Right =" + Math.round(frontRightPower * 100.0)/100.0);
            telemetry.addData("ROBOT", "Status:" + "Front Left =" + Math.round(frontLeftPower * 100.0)/100.0);//Echos information using classification.
            telemetry.addData("ROBOT", "Status:" + "Back Left =" + Math.round(backLeftPower * 100.0)/100.0);
            telemetry.addData("ROBOT", "Status:" + "Back Right =" + Math.round(backRightPower * 100.0)/100.0);
            
            
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
            servo0.setPosition(position); //Tell the servo to go to the correct pos
            sleep(CYCLE_MS);
            idle();
            telemetry.update();
        }
    }
}
