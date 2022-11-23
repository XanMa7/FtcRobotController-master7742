// Defines the package

package org.firstinspires.ftc.teamcode;

// Imports needed resources

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp
        (name="Mecanum Drive")  // Defines that it's a TeleOp program

public class MecanumDrive extends LinearOpMode  // Defines the program file name and type of op mode
{
    DcMotor frontLeft, frontRight, backLeft, backRight, lift;// Defines motors
    Servo gripServo; //Defines servo(s)
    float   leftY, mlefty, rightY, coclock, clock;  // Defines joystick positions for cleaner code
    double gripServoPosition;
    double MIN_POSITION = 0.45, MAX_POSITION = 1;

    // Initialization (When the "Init" button is pressed)
    @Override
    public void runOpMode() throws InterruptedException
    {
        frontLeft = hardwareMap.dcMotor.get("front_Left");  // Maps the leftMotor to physical motor
        frontRight = hardwareMap.dcMotor.get("front_Right"); // Maps the frong right motor to the physical motor
        backLeft = hardwareMap.dcMotor.get("back_Left"); // Maps the left rear motor to the physical motor
        backRight = hardwareMap.dcMotor.get("back_Right");// Maps the right rear motor to physical motor
        lift = hardwareMap.dcMotor.get("slide"); //Maps the lift motor to the physical motor
        gripServo = hardwareMap.servo.get("grab"); //Maps the gripper servo to the physical servo
        frontRight.setDirection(DcMotor.Direction.REVERSE); //reverses to allow robot to function correctly with mechanums, it would do circles without this
        backRight.setDirection(DcMotor.Direction.REVERSE); //reverses to allow robot to function correctly with mechanums, it would do circles without this

        telemetry.addData("Mode", "waiting");  // Gets the current status ready to display on robot controller screen
        telemetry.update();  // Updates the display of the robot controller

        waitForStart();  // Waits for the "Start" button to be pressed on robot controller phone
        gripServoPosition = 0.45; //sets servo to the "open" position with 1 being the "closed" position
        while (opModeIsActive())  // After the "Start" button is pressed and before the "Stop" button is pressed or error occurs
        {
            double vertical = 0; //declare variables
            double horizontal = 0;// ''
            double pivot = 0; // ''
            vertical = -gamepad1.left_stick_y * 3/8; //set variables to 3/8's of the up and down value of the stick, reducing speed and risk of de-scoring
            horizontal = gamepad1.left_stick_x * 3/8 * 1.1;//the 1.1 is to counteract imperfect strafing, with 3/8ths to slow down the bot, reducing de-scoring risk
            pivot = gamepad1.right_stick_y * 3/8;//set variables to 3/8's of the up and down value of the stick, reducing speed and risk of de-scoring
            clock = gamepad1.right_trigger; //allows for the robot to rotate with y being pressed due to the nature of how mecanum wheels move the robot
            coclock = gamepad1.left_trigger;//allows for the robot to rotate with y being pressed due to the nature of how mecanum wheels move the robot

            mlefty = gamepad2.right_stick_y; //sets the stick for the lifting mechanism to the up and down movement of player two's stick

            frontLeft.setPower(pivot + (-vertical - horizontal));
            backLeft.setPower(pivot + (-vertical + horizontal));
            frontRight.setPower(pivot + (-vertical + horizontal));
            backRight.setPower(pivot + (-vertical - horizontal));

            lift.setPower(mlefty * 1/2); //sets the motor power to 1/2 the value of player two's left stick
            gripServo.setPosition(Range.clip(gripServoPosition, MIN_POSITION, MAX_POSITION));
            if(gamepad2.b && gripServoPosition < MAX_POSITION) { //allows for more simplistic use of the gripper servo, when player two's b button is pressed, it "opens" and allows a cone to be captured
                gripServoPosition += .5;//"opened"
            }
            if(gamepad2.a && gripServoPosition > MIN_POSITION) { //allows for more simplistic use of the gripper servo, when player two's a button is pressed, it "closes" and releases the captured cone
                gripServoPosition -= 1;//"closed"
            }

            if(gamepad1.y){ //allows the robot to rotate with the triggers when player 1 holds "y" down
                frontLeft.setPower(-clock * 4/8);  // Right trigger rotates right
                frontRight.setPower(clock * 4/8);
                backLeft.setPower(-clock * 4/8);
                backRight.setPower(clock * 4/8);

                frontLeft.setPower(coclock * 4/8);  // Left trigger rotates left
                frontRight.setPower(-coclock * 4/8);
                backLeft.setPower(coclock * 4/8);
                backRight.setPower(-coclock* 4/8);
            }
            telemetry.addData("Mode","Running");
            telemetry.update(); //update to show battery voltage

        }
    }
}