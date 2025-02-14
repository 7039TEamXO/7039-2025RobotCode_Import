package frc.robot.subsystems;

import java.io.File;
import java.lang.annotation.ElementType;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

//import javax.xml.transform.Source;

//import edu.wpi.first.math.geometry.Pose2d;
//import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import frc.robot.Gamepiece;
//import frc.robot.Robot;
import frc.robot.RobotState;
import frc.robot.subsystems.Climb.Climb;
import frc.robot.subsystems.Climb.ClimbConstants;
import frc.robot.subsystems.Climb.ClimbState;
import frc.robot.subsystems.Elevator.ElevatorState;
import frc.robot.subsystems.Handler.HandlerState;
import frc.robot.subsystems.Handler.Handler;
import frc.robot.subsystems.Wrist.Wrist;
import frc.robot.subsystems.Wrist.WristState;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

public class SubsystemManager {

    // The robot's subsystems and commands are defined here...
    private static final SwerveSubsystem drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),
                                                                         "swerve/falcon"));

    private static final CommandPS4Controller ps4Joystick = new CommandPS4Controller(0);
    private static final PS4Controller psController_HID = ps4Joystick.getHID();

    private static boolean isLocked = false;

    private static RobotState state = RobotState.TRAVEL;
    private static RobotState lastState;

    private static ElevatorState elevatorState = ElevatorState.BASE;
    private static ElevatorState lastElevatorState = ElevatorState.BASE;
    
    private static HandlerState handlerState = HandlerState.STOP;

    private static ClimbState climbState = ClimbState.STOP;

    public static Command travelCommand = Commands.run(() -> operateAuto(RobotState.TRAVEL, null));
    public static Command intakeCoralCommand = Commands.run(() -> operateAuto(RobotState.INTAKE, ElevatorState.BASE));
    public static Command intakeAlgaeLowCommand = Commands.run(() -> operateAuto(RobotState.INTAKE, ElevatorState.ALGAE_LOW));
    public static Command intakeAlgaeHighCommand = Commands.run(() -> operateAuto(RobotState.INTAKE, ElevatorState.ALGAE_HIGH));
    public static Command level0Command = Commands.run(() -> operateAuto(RobotState.TRAVEL, ElevatorState.LEVEL0));
    public static Command level1Command = Commands.run(() -> operateAuto(RobotState.TRAVEL, ElevatorState.LEVEL1));
    public static Command level2Command = Commands.run(() -> operateAuto(RobotState.TRAVEL, ElevatorState.LEVEL2));
    public static Command level3Command = Commands.run(() -> operateAuto(RobotState.TRAVEL, ElevatorState.LEVEL3));
    public static Command depleteCommand = Commands.run(() -> operateAuto(RobotState.DEPLETE, null));
    // public static Command alighRightCommand = SubsystemManager.getDriveBase().alignByLimelight((-psController_HID.getLeftY()));

    public static void init() {
        state = RobotState.TRAVEL;
        lastState = state;
        DeliveryManager.init();
        Handler.init();
        Climb.init();
    }

    public static void operate(boolean onAuto) {
        if (!onAuto) {
            state = psController_HID.getL2Button() ? RobotState.DEPLETE :
            psController_HID.getR2Button() ? RobotState.INTAKE :
            psController_HID.getShareButton() ? RobotState.CLIMB :
            psController_HID.getTouchpadButton() ? RobotState.TRAVEL : // right stick
            psController_HID.getPOV(0) == 90 ? RobotState.INTAKE :
            psController_HID.getPOV(0) == 270 ? RobotState.INTAKE :
            psController_HID.getCrossButton() && !Handler.isAlgaeIn() && lastElevatorState != ElevatorState.BASE ? 
                RobotState.INTAKE : state;
        
            elevatorState = psController_HID.getCrossButton() ? ElevatorState.BASE :
            psController_HID.getSquareButton() ? ElevatorState.LEVEL1 :
            psController_HID.getTriangleButton() ? ElevatorState.LEVEL3 :
            psController_HID.getCircleButton() ? ElevatorState.LEVEL2 :
            psController_HID.getPOV(0) == 90 ? ElevatorState.ALGAE_HIGH : //right
            psController_HID.getPOV(0) == 180 ? ElevatorState.LEVEL0 : // down
            psController_HID.getPOV(0) == 270 ? ElevatorState.ALGAE_LOW : // left
            elevatorState;
         
            
         
           }
           
           
        switch (state) {
            case TRAVEL:
                // if(Handler.isCoralIn() && !Handler.getCoralIr()){
                //     handlerState = HandlerState.PUSH_BACK_CORAL;
                // } else if (!Handler.getCoralIr() && )

                if(Handler.isAlgaeIn() && (elevatorState == ElevatorState.ALGAE_LOW || 
                                            elevatorState == ElevatorState.ALGAE_HIGH ||
                                              elevatorState == ElevatorState.BASE)){
                    handlerState = HandlerState.HOLD_ALGAE;
                } else {
                    handlerState = HandlerState.STOP;
                }
                isLocked = false;
                break;

            case CLIMB:
                handlerState = HandlerState.STOP;
                elevatorState = ElevatorState.BASE;
                if (psController_HID.getOptionsButton()) {
                    climbState = ClimbState.UP;
                } else if (psController_HID.getShareButton()) {
                    climbState = ClimbState.DOWN;
                }
                 else {
                    climbState = ClimbState.STOP;
                }
                break;

            case DEPLETE:
                if (elevatorState == ElevatorState.LEVEL3 || Handler.isAlgaeIn()) {
                    handlerState = HandlerState.DEPLETE_ALGAE;
                }
                else if (elevatorState == ElevatorState.LEVEL0 ) {
                    handlerState =  HandlerState.DEPLETE_CORAL_LEVEL0;
                }
                else {
                    handlerState = HandlerState.DEPLETE_CORAL;
                }
                break;

            case INTAKE:
                Handler.updateHandlerIr(lastState != RobotState.INTAKE);
                if (elevatorState == ElevatorState.BASE && !Handler.isCoralIn()) {
                    handlerState = HandlerState.INTAKE_CORAL;
                }
                else if (elevatorState == ElevatorState.ALGAE_HIGH || elevatorState == ElevatorState.ALGAE_LOW) {
                    handlerState = HandlerState.INTAKE_ALGAE;
                }
                // if (lastElevatorState == ElevatorState.LEVEL3) {
                //     handlerState = HandlerState.INTAKE_CORAL;
                // }
                state = Handler.isAlgaeIn() || Handler.isCoralIn() ? RobotState.TRAVEL : RobotState.INTAKE;
                break;
        }

        DeliveryManager.operate(elevatorState);
        Handler.operate(handlerState);
        Climb.operate(climbState);
        // System.out.println(state + "" + climbState);
        

        if (isLocked) drivebase.lock();   
        lastState = state;
        lastElevatorState = elevatorState;
        
    }

    private static void operateAuto(RobotState chosenState, ElevatorState choosenElevatorState) {
        state = chosenState == null ? state : chosenState;
        elevatorState = choosenElevatorState == null ? elevatorState : choosenElevatorState;
        operate(true);
    }

    public static SwerveSubsystem getDriveBase() {
        return drivebase;
    }


    public static void setDefaultCommand(Command defultCommand){
        drivebase.setDefaultCommand(defultCommand);
    }

    public static CommandPS4Controller getpsJoystick() {
        return ps4Joystick;
    } 

    public static void initDriveBase() {
        drivebase.resetOdometry(new Pose2d(0, 0, Rotation2d.fromDegrees(180))); 
    }

    public static RobotState getRobotState() {
        return state;
    }

    public static ElevatorState getElevatorState() {
        return elevatorState;
    }

    public static HandlerState getHandlerState() {
        return handlerState;
    }

    public static ClimbState getClimbState() {
        return climbState;
    }

}
