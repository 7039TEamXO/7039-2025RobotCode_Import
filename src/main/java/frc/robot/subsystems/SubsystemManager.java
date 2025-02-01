package frc.robot.subsystems;

import java.io.File;

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
import frc.robot.subsystems.Elevator.ElevatorState;
import frc.robot.subsystems.Handler.HandlerState;
import frc.robot.subsystems.Handler.Handler;
import frc.robot.subsystems.Wrist.WristState;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

public class SubsystemManager {

    // The robot's subsystems and commands are defined here...
    private static final SwerveSubsystem drivebase = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),
                                                                         "swerve/falcon"));

    private static final CommandPS4Controller ps4Joystick = new CommandPS4Controller(0);
    private static final PS4Controller psController_HID = ps4Joystick.getHID();

    private static boolean isLocked = false;

    private static RobotState state;
    private static RobotState lastState;

    private static ElevatorState elevatorState = ElevatorState.BASE;
    private static HandlerState handlerState = HandlerState.STOP;
    private static Gamepiece gamepiece = Gamepiece.NONE; // gamepiece in robot

   
    public static Command travelCommand = Commands.run(() -> operateAuto(RobotState.TRAVEL));

    public static void init() {
        state = RobotState.TRAVEL;
        lastState = RobotState.TRAVEL;
    }

    public static void operate(boolean onAuto) {
        if (!onAuto) {
            state = psController_HID.getCircleButtonPressed() ? RobotState.TRAVEL : lastState;
        }
        
        switch (state) {
            case TRAVEL:
                handlerState = HandlerState.STOP;
                isLocked = false;

                break;

            case CLIMB:
                handlerState = HandlerState.STOP;

                break;

            case DEPLETE:
                if (elevatorState == ElevatorState.BASE || elevatorState == ElevatorState.LEVEL3) {
                    handlerState = HandlerState.DEPLETE;
                }
                else {
                    handlerState = HandlerState.INTAKE;
                }
                break;

            case INTAKE:
                handlerState = HandlerState.INTAKE;

                break;
        }

        DeliveryManger.operate(elevatorState);
        Handler.operate(handlerState);

        lastState = state;

        if (isLocked) drivebase.lock();

        //check if we have a algae
        if ((elevatorState == ElevatorState.ALGAE_LOW || elevatorState == ElevatorState.ALGAE_HIGH)
         &&  Handler.isGamePieceIn()
         && gamepiece == Gamepiece.NONE) {
            gamepiece = Gamepiece.ALGAE;
        }
        //check if we have a coral
        if (elevatorState == ElevatorState.BASE 
        &&  Handler.isGamePieceIn() 
        && gamepiece == Gamepiece.NONE) {
            gamepiece = Gamepiece.CORAL;
        }

        // check if we dont have a gamepiece
        if (elevatorState == ElevatorState.BASE && !Handler.isGamePieceIn()) {
            gamepiece = Gamepiece.NONE;
        }
        if ((elevatorState == ElevatorState.ALGAE_LOW || elevatorState == ElevatorState.ALGAE_HIGH) 
        && !Handler.isGamePieceIn()) {
            gamepiece = Gamepiece.NONE;
        }

    }

    private static void operateAuto(RobotState chosenState) {
        state = chosenState;
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

    public static Gamepiece getGamepiece() {
        return gamepiece;
    }
}
