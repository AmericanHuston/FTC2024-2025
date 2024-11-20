package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.GoBildaPinpointDriver;

import java.util.Locale;

@TeleOp(name="CoachTesting", group="Linear OpMode")
//@Disabled
public class CoachTesting extends LinearOpMode {
    boolean rampUp = true;
    double position = 0.0;
    double MAX_POS =  1.0;
    double MIN_POS = 0.0;
    double INCREMENT = 0.02;
    @Override
    public void runOpMode() throws InterruptedException {
        //Don't edit code below this point
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
        dashboard.updateConfig();
        //Don't edit code above this point
        GoBildaPinpointDriver odo; // Declare OpMode member for the Odometry Computer


        double SLIDER_POWER = 0.80;
        int CYCLE_MS = 50;
        Servo servo0 = hardwareMap.get(Servo.class, "servo0");
        Servo arm = hardwareMap.get(Servo.class, "arm");
        Servo claw = hardwareMap.get(Servo.class, "claw");
        DcMotor sliderLeft = hardwareMap.get(DcMotor.class, "SliderLeft");
        DcMotor sliderRight = hardwareMap.get(DcMotor.class, "SliderRight");

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
                RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu.initialize(parameters);

        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
        odo.resetPosAndIMU();

        double DesiredArmPosition = 0.0;
        double DesiredClawPosition = 0.0;
        double DesiredServo0Position = 0.0;
        waitForStart();

        if (isStopRequested()) return;
        while (opModeIsActive()) {
            //Simultaneous motion causes problems so wait to move until the other controller stops moving.
            double DesiredSliderPower;
            if (gamepad2.dpad_up){
                DesiredSliderPower = SLIDER_POWER;
            } else if (gamepad2.dpad_down){
                DesiredSliderPower = -SLIDER_POWER;
            } else {
                DesiredSliderPower = 0.0;
            }

            if (gamepad2.a) {
                DesiredArmPosition = 0.3;
            } else if (gamepad2.b){
                DesiredArmPosition = 0.0;
            }

            if (gamepad2.x) {
                DesiredClawPosition = 0.0;
            } else if (gamepad2.y) {
                DesiredClawPosition = 1.0;
            }


            odo.update();
            Pose2D pos = odo.getPosition();
            String data = String.format(Locale.US, "{X: %.3f, Y: %.3f, H: %.3f}", pos.getX(DistanceUnit.MM), pos.getY(DistanceUnit.MM), pos.getHeading(AngleUnit.DEGREES));
            telemetry.addData("Position", data);

            double y = gamepad1.left_stick_y/2; // Remember, Y stick value is reversed
            double x = -gamepad1.left_stick_x/2;
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
            if (gamepad1.back) {
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

            sliderLeft.setPower(DesiredSliderPower);
            sliderRight.setPower(-DesiredSliderPower);

            if (gamepad1.a) {
                DesiredArmPosition = servoSlide();
            }
            arm.setPosition(DesiredArmPosition);

            if (gamepad1.y) {
                DesiredClawPosition = servoSlide();
            }
            claw.setPosition(DesiredClawPosition);
            servo0.setPosition(DesiredServo0Position);

            telemetry.addData("ROBOT", "Status:" + "Servo:" + servo0.getPosition());
            telemetry.addData("ROBOT", "Status:" + "Servo:" + arm.getPosition());
            telemetry.addData("ROBOT", "Status:" + "Servo:" + claw.getPosition());

            telemetry.addData("DesiredSliderPower", DesiredSliderPower);
            telemetry.addData("SliderLeftPos", sliderLeft.getCurrentPosition());
            telemetry.addData("SliderRightPos", sliderRight.getCurrentPosition());
            telemetry.addData("DesiredArmPosition", DesiredArmPosition);
            telemetry.addData("DesiredClawPosition", DesiredClawPosition);

            telemetry.addData("ROBOT", "Status:" + "Front Right =" + Math.round(frontRightPower * 100.0)/100.0);
            telemetry.addData("ROBOT", "Status:" + "Front Left =" + Math.round(frontLeftPower * 100.0)/100.0);//Echos information using classification.
            telemetry.addData("ROBOT", "Status:" + "Back Left =" + Math.round(backLeftPower * 100.0)/100.0);
            telemetry.addData("ROBOT", "Status:" + "Back Right =" + Math.round(backRightPower * 100.0)/100.0);

            //servo0.setPosition(position); //Tell the servo to go to the correct pos
            sleep(CYCLE_MS);
            idle();
            telemetry.update();
        }
    }

    public double servoSlide() {
        if (rampUp) {
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
        return position;
    }
}
