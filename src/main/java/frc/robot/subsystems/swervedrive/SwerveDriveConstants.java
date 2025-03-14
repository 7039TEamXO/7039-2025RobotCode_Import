package frc.robot.subsystems.swervedrive;

/*
      -----------DRIVER STATION-----------

                    FACE ONE
*                 -----------
 *              /             \ 
 //*  FACE SIX /               \  FACE TWO
 *            /                 \
 *            \                 /
 *             \               /  FACE THREE
 * FACE FIVE    \             /
 *                -----------
 *                   FACE FOUR
 *       
 */                   
public class SwerveDriveConstants {
    // x, y
    public static final int BLUE_REEF_POS_FACE_ONE_ID   = 18;
    public static final int BLUE_REEF_POS_FACE_TWO_ID   = 19;
    public static final int BLUE_REEF_POS_FACE_THREE_ID = 20;
    public static final int BLUE_REEF_POS_FACE_FOUR_ID  = 21;
    public static final int BLUE_REEF_POS_FACE_FIVE_ID  = 22;
    public static final int BLUE_REEF_POS_FACE_SIX_ID   = 17;
    public static final int[] BLUE_RIFF_TAGS_ARRAY = {BLUE_REEF_POS_FACE_ONE_ID,
                                                    BLUE_REEF_POS_FACE_TWO_ID,
                                                    BLUE_REEF_POS_FACE_THREE_ID,
                                                    BLUE_REEF_POS_FACE_FOUR_ID,
                                                    BLUE_REEF_POS_FACE_FIVE_ID,
                                                    BLUE_REEF_POS_FACE_SIX_ID};        

    public static final int RED_REEF_POS_FACE_ONE_ID    = 7;
    public static final int RED_REEF_POS_FACE_TWO_ID    = 6;
    public static final int RED_REEF_POS_FACE_THREE_ID  = 11;
    public static final int RED_REEF_POS_FACE_FOUR_ID   = 10;
    public static final int RED_REEF_POS_FACE_FIVE_ID   = 9;
    public static final int RED_REEF_POS_FACE_SIX_ID    = 8;       
    public static final int[] RED_RIFF_TAGS_ARRAY = {RED_REEF_POS_FACE_ONE_ID,
                                                    RED_REEF_POS_FACE_TWO_ID,
                                                    RED_REEF_POS_FACE_THREE_ID,
                                                    RED_REEF_POS_FACE_FOUR_ID,
                                                    RED_REEF_POS_FACE_FIVE_ID,
                                                    RED_REEF_POS_FACE_SIX_ID}; 

    public static final double M_FROM_TAG_TO_POLES = 0.2;
    public static final double ALIGN_LIMELIGHT_X_KP = -0.055;
    public static final double ALIGN_LIMELIGHT_ROTATION_KP = -0.02; //align to tx 
    public static final double ALIGN_BY_TAG_ANGLE_ROTATION_KP = -0.043; //align to reef angle

    public static final double Kp_NET_AUTO_DRIVE_X = -2;
    public static final double Kp_NET_AUTO_DRIVE_ROTATION = -0.04;

    public static final double WANTED_X_NET_ALGAE_POS_BLUE = 7.2;
    public static final double WANTED_ROTATION_ANGLE_NET_ALGAE_POS_BLUE = 0;

    public static final double WANTED_X_NET_ALGAE_POS_RED = 10.4;
    
    public static final double WANTED_ROTATION_ANGLE_NET_ALGAE_POS_RED = 180;

}