package frc.team4828.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {
    // Joysticks
    private Joystick driveStick;
    private Joystick liftStick;
    // Motors
    private TalonSRX motor1, motor2, motor3, motor4;
    private Victor liftMotor, leftGrabberMotor, rightGrabberMotor, leftClimberMotor, rightClimberMotor;
    private Talon armMotor;
    // DIO
    private DigitalInput liftMin, liftMax, armMin, armMax, switcher;
    // Pneumatics
    private Compressor comp;
    private DoubleSolenoid shifterSol, dumperSol, grabberSol;
    private PneumaticSwitch switcher1, switcher2, grabberSwitcher, dumper;
    // Drive
    private Gearbox leftGearbox, rightGearbox;
    private DriveTrain drive;
    // Lift
    private Lift lift;
    private boolean liftManualPrev;
    private boolean liftManual;
    private Arm arm;
    // Grabber
    private Grabber grabber;
    // Climber
    private Climber climber;
    // Auton
    private DigitalInput switch1, switch2, switch3;
    private boolean doneAuton;
    private String data;

    public void robotInit() {
        // Joysticks
        driveStick = new Joystick(Ports.DRIVE_STICK);
        liftStick = new Joystick(Ports.LIFT_STICK);
        // Motors
        motor1 = new TalonSRX(Ports.LEFT_GEARBOX[0]);
        motor2 = new TalonSRX(Ports.LEFT_GEARBOX[1]);
        motor3 = new TalonSRX(Ports.RIGHT_GEARBOX[0]);
        motor4 = new TalonSRX(Ports.RIGHT_GEARBOX[1]);
        liftMotor = new Victor(Ports.LIFT);
        leftGrabberMotor = new Victor(Ports.LEFT_GRABBER);
        rightGrabberMotor = new Victor(Ports.RIGHT_GRABBER);
        leftClimberMotor = new Victor(Ports.LEFT_CLIMBER);
        rightClimberMotor = new Victor(Ports.RIGHT_CLIMBER);
        // DIO
        liftMin = new DigitalInput(Ports.LIFT_MIN);
        liftMax = new DigitalInput(Ports.LIFT_MAX);
        armMin = new DigitalInput(Ports.ARM_MIN);
        armMax = new DigitalInput(Ports.ARM_MAX);
        switcher = new DigitalInput(Ports.SWITCHER);
        // Pneumatics
        comp = new Compressor(Ports.COMPRESSOR);
        shifterSol = new DoubleSolenoid(Ports.SHIFTER[0], Ports.SHIFTER[1]);
        dumperSol = new DoubleSolenoid(Ports.DUMPER[0], Ports.DUMPER[1]);
        grabberSol = new DoubleSolenoid(Ports.GRABBER[0], Ports.GRABBER[1]);
        switcher1 = new PneumaticSwitch(comp, shifterSol);
        switcher2 = new PneumaticSwitch(comp, shifterSol);
        grabberSwitcher = new PneumaticSwitch(comp, grabberSol);
        dumper = new PneumaticSwitch(comp, dumperSol);
        // Drive
        leftGearbox = new Gearbox(motor1, motor2, switcher1);
        rightGearbox = new Gearbox(motor3, motor4, switcher2);
        drive = new DriveTrain(leftGearbox, rightGearbox);
        // Lift
        lift = new Lift(liftMotor, liftMin, liftMax, switcher);
        // Arm
        arm = new Arm(armMotor, armMin, armMax);
        // Grabber
        grabber = new Grabber(leftGrabberMotor, rightGrabberMotor, grabberSwitcher);
        // Climber
        climber = new Climber(leftClimberMotor, rightClimberMotor);
        // Auton
        switch1 = new DigitalInput(Ports.AUTON[0]);
        switch2 = new DigitalInput(Ports.AUTON[1]);
        switch3 = new DigitalInput(Ports.AUTON[2]);
        doneAuton = false;

        comp.setClosedLoopControl(true);
    }

    public void autonomousInit() {
        System.out.println(" --- Start Auton Init ---");
        data = DriverStation.getInstance().getGameSpecificMessage();
        System.out.println(" --- Start Auton ---");
    }

    public void autonomousPeriodic() {
        if (!doneAuton) {
            System.out.println((switch1.get() ? "1" : "0") + (switch2.get() ? "1" : "0") + (switch3.get() ? "1" : "0"));
            int mode = (switch1.get() ? 4 : 0) + (switch2.get() ? 2 : 0) + (switch3.get() ? 1 : 0);
            System.out.println("Auton Mode: " + mode);
            switch (10) { // TODO Change to mode
            case 0:
                // Just go forward
                drive.moveDistance(120, .5);
                break;
            case 1:
                // Switch from Left
                // Check left or right side
                switch(data.charAt(0)) {
                case 'L':
                    drive.moveDistance(150, 1);
                    drive.turnDegAbs(-90, .5);
                    drive.moveDistance(-10, 1);
                    dumper.set(1);
                    break;
                case 'R':
                    drive.moveDistance(5, 1);
                    drive.turnDegAbs(27, .5);
                    drive.moveDistance(300, 1);
                    drive.turnDegAbs(0, .5);
                    drive.moveDistance(60, 1);
                    drive.turnDegAbs(90, .5);
                    drive.moveDistance(-10, 1);
                    dumper.set(1);
                }
                break;
            case 2:
                // Switch from right
                // Check left or right side
                switch(data.charAt(0)) {
                case 'R':
                    drive.moveDistance(150, 1);
                    drive.turnDegAbs(90, .5);
                    drive.moveDistance(-10, 1);
                    dumper.set(1);
                    break;
                case 'L':
                    drive.moveDistance(5, 1);
                    drive.turnDegAbs(-27, .5);
                    drive.moveDistance(300, 1);
                    drive.turnDegAbs(0, .5);
                    drive.moveDistance(60, 1);
                    drive.turnDegAbs(-90, .5);
                    drive.moveDistance(-10, 1);
                    dumper.set(1);
                }
                break;
            case 3:
                switch(data.charAt(1)) {
                    case 'R':
                        drive.moveDistance(5, 1);
                        drive.turnDegAbs(27, .5);
                        drive.moveDistance(300, 1);
                        drive.turnDegAbs(0, .5);
                        drive.moveDistance(190,1);
                        drive.turnDegAbs(270,0.5);
                        drive.moveDistance(10,1);
                        lift.setSpeed(1);
                        while (!lift.isIdle()){
                            Timer.delay(0.2);
                        }
                        grabber.outtake();


                        break;
                    case 'L':
                        drive.moveDistance(310, 1);
                        drive.turnDegAbs(90,0.5);
                        drive.moveDistance(10,1);
                        lift.setSpeed(1);
                        while (!lift.isIdle()){
                            Timer.delay(0.2);
                        }
                        grabber.outtake();
                        break;
                    }
                break;
                // Scale from Left


            case 4:
                switch(data.charAt(0)) {
                    case 'R':
                        drive.moveDistance(310, 1);
                        drive.turnDegAbs(270, .5);
                        drive.moveDistance(10, 1);
                        while (!lift.isIdle()){
                            Timer.delay(0.2);
                        }
                        grabber.outtake();

                        break;
                    case 'L':
                        drive.moveDistance(5, 1);
                        drive.turnDegAbs(-27, .5);
                        drive.moveDistance(300, 1);
                        drive.turnDegAbs(0, .5);
                        drive.moveDistance(190, 1);
                        drive.turnDegAbs(90, .5);
                        drive.moveDistance(10, 1);
                        while (!lift.isIdle()){
                            Timer.delay(0.2);
                        }
                        grabber.outtake();
                        break;
                }

                // Scale from Right
                break;
            case 5:
                drive.moveDistance(210, 1);
                drive.turnDegAbs(90,0.5);
                drive.moveDistance(40,1);
                // Out of the way left
                // Goes quickly and crosses line to the left of the switch. To be used if there is a chance of collision
                break;
            case 6:
                drive.moveDistance(210, 1);
                drive.turnDegAbs(270,0.5);
                drive.moveDistance(40,1);
                // Out of the way right
                // Goes quickly and crosses line to the left of the switch. To be used if there is a chance of collision
                break;
            case 7:
                // Outtake into the hole
                // Shake a bit to drop the grabber
                drive.moveDistance(-10, 1);
                drive.moveDistance(5, 1);
                grabber.outtake();
                break;
            default:
                break;
            }
            doneAuton = true;
        }
        Timer.delay(.1);
    }

    public void teleopInit() {
        System.out.println(" --- Start Teleop Init ---");
        lift.start();
        lift.setSpeed(0.5);
        liftManualPrev = false;
        liftManual = false;
        System.out.println(" --- Start Teleop ---");
    }

    public void teleopPeriodic() {
        // Drive
        if (driveStick.getTrigger()) {
            drive.arcadeDrive(JoystickUtils.processX(driveStick.getX()), JoystickUtils.processY(driveStick.getY()),
                    JoystickUtils.processTwist(driveStick.getTwist()));
        } else {
            drive.brake();
        }
        // Drive Stick Debug
        JoystickUtils.debug(driveStick.getX(), driveStick.getY(), driveStick.getTwist());
        // Dumper
        if (driveStick.getRawButton(Buttons.DUMPER_ON)) {
            dumper.set(1);
        } else if (driveStick.getRawButton(Buttons.DUMPER_OFF)) {
            dumper.set(-1);
        }
        // Lift Set Manual
        if (liftStick.getRawButton(Buttons.LIFT_MANUAL) && !liftManualPrev) {
            liftManual = !liftManual;
            lift.setManual(liftManual);
        }
        liftManualPrev = liftStick.getRawButton(Buttons.LIFT_MANUAL);
        // Lift
        if (liftManual) {
//            if (liftStick.getRawButton(Buttons.LIFT_UP)) {
//                lift.setLiftTargetDirection(1);
//            } else if (liftStick.getRawButton(Buttons.LIFT_DOWN)) {
//                lift.setLiftTargetDirection(-1);
//            } else {
//                lift.setLiftTargetDirection(0);
//            }
            lift.setSpeedManual(JoystickUtils.processY(liftStick.getY()));
        } else {
            if (liftStick.getRawButton(Buttons.LIFT[0])) {
                lift.setTarget(0);
            } else if (liftStick.getRawButton(Buttons.LIFT[1])) {
                lift.setTarget(2);
            } else if (liftStick.getRawButton(Buttons.LIFT[2])) {
                lift.setTarget(4);
            } else if (liftStick.getRawButton(Buttons.LIFT[3])) {
                lift.setTarget(6);
            } else if (liftStick.getRawButton(Buttons.LIFT[4])) {
                lift.setTarget(8);
            }
        }
        // Arm
        if (liftStick.getRawButton(Buttons.ARM_UP)) {
            arm.up();
        } else if (liftStick.getRawButton(Buttons.ARM_DOWN)) {
            arm.down();
        }
        if (arm.check()) {
            arm.stop();
        }
        // Grabber
        if (liftStick.getRawButton(Buttons.GRABBER_OPEN)) {
            grabber.open();
        } else if (liftStick.getRawButton(Buttons.GRABBER_CLOSE)) {
            grabber.close();
        }
        if (liftStick.getRawButton(Buttons.GRABBER_IN)) {
            grabber.intake();
        } else if (liftStick.getRawButton(Buttons.GRABBER_OUT)) {
            grabber.outtake();
        } else {
            grabber.stop();
        }
        // Climber
        if (driveStick.getRawButton(Buttons.CLIMB_UP)) {
            climber.up();
        } else if (driveStick.getRawButton(Buttons.CLIMB_DOWN)) {
            climber.down();
        } else {
            climber.stop();
        }

        Timer.delay(.01);
    }

    public void testInit() {
        System.out.println(" --- Start Test Init ---");
        drive.zeroEnc();
        drive.arcadeDrive(0, 0.1, 0);
        System.out.println(" --- Start Test ---");
    }

    public void testPeriodic() {
        drive.debugEnc();
        Timer.delay(0.1);
    }

    public void disabledInit() {
        lift.stop();
    }
}
